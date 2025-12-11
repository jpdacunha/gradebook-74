
package com.liferay.training.space.gradebook.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.search.*;
import com.liferay.portal.kernel.theme.ThemeDisplay;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.space.gradebook.model.Assignment;
import com.liferay.training.space.gradebook.service.AssignmentLocalService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.training.space.gradebook.service.permission.AssignmentPermissionChecker;
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
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {

        ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);


        long groupId = themeDisplay.getScopeGroupId();

        boolean hasAddAssignmentPermission = AssignmentPermissionChecker.containsTopLevel(themeDisplay.getPermissionChecker(), groupId, AssignmentPermissionChecker.ADD_ASSIGNMENT);

        String keywords = ParamUtil.getString(renderRequest, "keywords");

        PortalPreferences portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(renderRequest);

       // Lire cur depuis l'URL
        int cur = ParamUtil.getInteger(renderRequest, "cur", -1);

        if (cur == -1) {
            // Si cur n'est PAS dans l'URL → on le récupère depuis la session
            cur = GetterUtil.getInteger(portalPreferences.getValue(GradebookPortletKeys.PORTLET_NAME, "cur"), 1);
        }
        else {
            // Si cur est dans l'URL → on le sauvegarde
            portalPreferences.setValue(GradebookPortletKeys.PORTLET_NAME, "cur", String.valueOf(cur));
        }

        String displayStyle = ParamUtil.getString(renderRequest,"displayStyle","table");


        int delta = ParamUtil.getInteger(renderRequest, "delta", 5);
        int start = (cur - 1) * delta;
        int end = start + delta;


        String orderByCol = ParamUtil.getString(renderRequest, "orderByCol", "title");
        String orderByType = ParamUtil.getString(renderRequest, "orderByType", "asc");

        int count;
        List<Assignment> entries;
        boolean asc = orderByType.equalsIgnoreCase("asc");
        Comparator<Assignment> comparator;

        comparator = Comparator.comparing(a -> a.getTitle(themeDisplay.getLocale()).toLowerCase());

        if (!asc) {
            comparator = comparator.reversed();
        }

        if (!keywords.isEmpty()) {
            // ----- SEARCH MODE -----
            SearchContext searchContext = SearchContextFactory.getInstance(PortalUtil.getHttpServletRequest(renderRequest));
            searchContext.setKeywords(keywords);
            searchContext.setCompanyId(themeDisplay.getCompanyId());

            Indexer<Assignment> indexer = IndexerRegistryUtil.getIndexer(Assignment.class);

            Hits hits;

            try {
                hits = indexer.search(searchContext);
            } catch (SearchException e) {
                throw new PortletException(e);
            }

            entries = new ArrayList<>();
            for (Document doc : hits.getDocs()) {
                long pk = GetterUtil.getLong(doc.get(Field.ENTRY_CLASS_PK));
                Assignment entry = assignmentLocalService.fetchAssignment(pk);
                if (entry != null) entries.add(entry);
            }
            // 🔥 important : applique aussi le tri en mode SEARCH
            entries.sort(comparator);
            count = hits.getLength();
        } else {
            // ----- NORMAL MODE -----
            entries = assignmentLocalService.getAssignmentsByGroupId(groupId, start, end);
            count = assignmentLocalService.getAssignmentsCountByGroupId(groupId);
            entries = new ArrayList<>(entries);
            // 🔥 appliquer tri
            entries.sort(comparator);
        }

        long categoryId = ParamUtil.getLong(renderRequest, "categoryId");
        String categoryIdParam = renderRequest.getParameter("categoryId");

       // Reset si pas dans l'URL
        if (categoryIdParam == null) {
            categoryId = 0;
        }

         // Mode filtré
        if (categoryId > 0) {

            List<Assignment> filtered = null;
            try {
                filtered = assignmentLocalService.getAssignmentsByCategory(categoryId);
            } catch (PortalException e) {
                throw new RuntimeException(e);
            }

            // tri
            filtered.sort(comparator);

            // count
            count = filtered.size();

            // pagination maison
            int from = Math.min(start, filtered.size());
            int to = Math.min(end, filtered.size());
            entries = filtered.subList(from, to);

        } else {
            // Mode normal
            entries = assignmentLocalService.getAssignmentsByGroupId(groupId, start, end);
            count = assignmentLocalService.getAssignmentsCountByGroupId(groupId);

            entries = new ArrayList<>(entries);
            entries.sort(comparator);
        }

        renderRequest.setAttribute("displayStyle", displayStyle);
        renderRequest.setAttribute("cur", cur);
        renderRequest.setAttribute("entries", entries);
        renderRequest.setAttribute("orderByCol", orderByCol);
        renderRequest.setAttribute("orderByType", orderByType);
        renderRequest.setAttribute("entriesCount", count);
        renderRequest.setAttribute("hasAddAssignmentPermission", hasAddAssignmentPermission);

        include("/view.jsp", renderRequest, renderResponse);
    }

}


