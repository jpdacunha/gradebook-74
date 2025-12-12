package com.liferay.training.space.gradebook.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.space.gradebook.model.Assignment;
import com.liferay.training.space.gradebook.service.AssignmentLocalService;
import com.liferay.training.space.gradebook.service.permission.AssignmentPermissionChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
        immediate = true,
        property = {
                "com.liferay.portlet.add-default-resource=true",
                "com.liferay.portlet.display-category=category.sample",
                "com.liferay.portlet.preferences-owned-by-group=true",
                "com.liferay.portlet.private-request-attributes=false",
                "com.liferay.portlet.private-session-attributes=false",
                "com.liferay.portlet.render-weight=50",
                "com.liferay.portlet.scopeable=true",
                "com.liferay.portlet.struts-path=gradebook",
                "javax.portlet.display-name=Gradebook",
                "javax.portlet.expiration-cache=0",
                "javax.portlet.init-param.always-display-default-configuration-icons=true",
                "javax.portlet.init-param.view-template=/view.jsp",
                "javax.portlet.init-param.portlet-title-based-navigation=false",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.name=" + GradebookPortletKeys.PORTLET_NAME,
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.security-role-ref=power-user,user",
                "javax.portlet.supported-public-render-parameter=resetCur",
                "javax.portlet.supported-public-render-parameter=tag",
                "javax.portlet.version=3.0",
                "mvc.command.name.default=/gradebook/view",
        },
        service = Portlet.class
)
public class GradebookPortlet extends MVCPortlet {

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected AssignmentLocalService assignmentLocalService;

    @Override
    public void doView(
            RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

        ThemeDisplay themeDisplay =
                (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

        long groupId = themeDisplay.getScopeGroupId();

        boolean hasAddAssignmentPermission =
                AssignmentPermissionChecker.containsTopLevel(
                        themeDisplay.getPermissionChecker(), groupId,
                        AssignmentPermissionChecker.ADD_ASSIGNMENT);

        String keywords = ParamUtil.getString(renderRequest, "keywords");

        PortalPreferences portalPreferences =
                PortletPreferencesFactoryUtil.getPortalPreferences(renderRequest);

        // -----------------------------
        // PAGINATION (cur)
        // -----------------------------
        int cur = ParamUtil.getInteger(renderRequest, "cur", -1);

        if (cur == -1) {
            cur = GetterUtil.getInteger(
                    portalPreferences.getValue(
                            GradebookPortletKeys.PORTLET_NAME, "cur"),
                    1);
        }
        else {
            portalPreferences.setValue(
                    GradebookPortletKeys.PORTLET_NAME, "cur",
                    String.valueOf(cur));
        }

        int delta = ParamUtil.getInteger(renderRequest, "delta", 5);
        int start = (cur - 1) * delta;
        int end = start + delta;

        // -----------------------------
        // DISPLAY STYLE
        // -----------------------------
        String namespace = renderResponse.getNamespace();

        String displayStyleParam = ParamUtil.getString(
                renderRequest, namespace + "displayStyle");

        if (Validator.isNull(displayStyleParam)) {
            displayStyleParam = ParamUtil.getString(
                    renderRequest, "displayStyle");
        }

        boolean hasPortletParams =
                Validator.isNotNull(displayStyleParam) ||
                        Validator.isNotNull(
                                ParamUtil.getString(renderRequest, "orderByCol")) ||
                        Validator.isNotNull(
                                ParamUtil.getString(renderRequest, "keywords")) ||
                        (ParamUtil.getInteger(renderRequest, "cur", 0) > 0) ||
                        (ParamUtil.getLong(renderRequest, "categoryId") > 0) ||
                        ParamUtil.getBoolean(renderRequest, "mine", false);

        String displayStyle;

        if (Validator.isNotNull(displayStyleParam)) {
            // Cas 1 : bouton d’affichage cliqué
            displayStyle = displayStyleParam;

            portalPreferences.setValue(
                    GradebookPortletKeys.PORTLET_NAME,
                    "displayStyle", displayStyle);
        }
        else if (!hasPortletParams) {
            // Cas 3 : arrivée “fraîche” → table par défaut
            displayStyle = "table";

            portalPreferences.setValue(
                    GradebookPortletKeys.PORTLET_NAME,
                    "displayStyle", displayStyle);
        }
        else {
            // Cas 2 : navigation interne
            displayStyle = portalPreferences.getValue(
                    GradebookPortletKeys.PORTLET_NAME,
                    "displayStyle", "table");
        }

        // -----------------------------
        // TRI
        // -----------------------------
        String orderByCol = ParamUtil.getString(
                renderRequest, "orderByCol", "title");
        String orderByType = ParamUtil.getString(
                renderRequest, "orderByType", "asc");

        boolean asc = orderByType.equalsIgnoreCase("asc");

        Comparator<Assignment> comparator =
                Comparator.comparing(
                        a -> a.getTitle(
                                themeDisplay.getLocale()).toLowerCase());

        if (!asc) {
            comparator = comparator.reversed();
        }

        // -----------------------------
        // CATÉGORIE (lecture + reset si non présent)
        // -----------------------------
        long categoryId = ParamUtil.getLong(renderRequest, "categoryId");
        String categoryIdParam = renderRequest.getParameter("categoryId");

        if (categoryIdParam == null) {
            categoryId = 0;
        }

        // -----------------------------
        // DÉTERMINATION DU MODE :
        //   1. SEARCH (keywords)
        //   2. CATEGORY FILTER
        //   3. NORMAL LIST
        // -----------------------------
        List<Assignment> entries;
        int count;

        try {

            if (Validator.isNotNull(keywords)) {
                // ====== 1. SEARCH MODE ======
                SearchContext searchContext =
                        SearchContextFactory.getInstance(
                                PortalUtil.getHttpServletRequest(renderRequest));

                searchContext.setKeywords(keywords);
                searchContext.setCompanyId(themeDisplay.getCompanyId());

                Indexer<Assignment> indexer =
                        IndexerRegistryUtil.getIndexer(Assignment.class);

                Hits hits = indexer.search(searchContext);

                entries = new ArrayList<>();

                Document[] docs = hits.getDocs();

                // pagination manuelle sur les résultats de search
                int from = Math.min(start, docs.length);
                int to = Math.min(end, docs.length);

                for (int i = from; i < to; i++) {
                    long pk = GetterUtil.getLong(
                            docs[i].get(Field.ENTRY_CLASS_PK));

                    Assignment entry =
                            assignmentLocalService.fetchAssignment(pk);

                    if (entry != null) {
                        entries.add(entry);
                    }
                }

                entries.sort(comparator);
                count = hits.getLength();
            }
            else if (categoryId > 0) {
                // ====== 2. CATEGORY FILTER MODE ======
                List<Assignment> filtered =
                        assignmentLocalService.getAssignmentsByCategory(
                                categoryId);

                filtered.sort(comparator);

                count = filtered.size();

                int from = Math.min(start, filtered.size());
                int to = Math.min(end, filtered.size());

                entries = filtered.subList(from, to);
            }
            else {
                // ====== 3. NORMAL LIST MODE ======
                entries = assignmentLocalService.getAssignmentsByGroupId(
                        groupId, start, end);

                entries = new ArrayList<>(entries);
                entries.sort(comparator);

                count = assignmentLocalService.getAssignmentsCountByGroupId(
                        groupId);
            }

        }
        catch (SearchException e) {
            throw new PortletException(e);
        }
        catch (PortalException e) {
            throw new PortletException(e);
        }

        // -----------------------------
        // ATTRIBUTS POUR LA JSP
        // -----------------------------
        renderRequest.setAttribute("displayStyle", displayStyle);
        renderRequest.setAttribute("cur", cur);
        renderRequest.setAttribute("entries", entries);
        renderRequest.setAttribute("orderByCol", orderByCol);
        renderRequest.setAttribute("orderByType", orderByType);
        renderRequest.setAttribute("entriesCount", count);
        renderRequest.setAttribute(
                "hasAddAssignmentPermission", hasAddAssignmentPermission);

        include("/view.jsp", renderRequest, renderResponse);
    }

}
