package com.liferay.training.space.gradebook.portlet.command;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.search.*;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.*;
import com.liferay.training.space.gradebook.model.Assignment;
import com.liferay.training.space.gradebook.portlet.GradebookPortletKeys;
import com.liferay.training.space.gradebook.service.AssignmentLocalService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.training.space.gradebook.service.permission.AssignmentPermissionChecker;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        property = {
                "javax.portlet.name=" + GradebookPortletKeys.PORTLET_NAME,
                "mvc.command.name=/gradebook/view"
        },
        service = MVCRenderCommand.class
)
public class ViewAssignmentMVCRenderCommand implements MVCRenderCommand {

    @Reference
    private AssignmentLocalService assignmentLocalService;

    @Override
    public String render(RenderRequest request, RenderResponse response)
            throws PortletException {

        String keywords = ParamUtil.getString(request, "keywords");
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long groupId = themeDisplay.getScopeGroupId();
        boolean hasAddAssignmentPermission = AssignmentPermissionChecker.containsTopLevel(themeDisplay.getPermissionChecker(), groupId, AssignmentPermissionChecker.ADD_ASSIGNMENT);
        int cur = ParamUtil.getInteger(request, "cur", 1);
        int delta = ParamUtil.getInteger(request, "delta", 5);

        int start = (cur - 1) * delta;
        int end = start + delta;


        // ----------------------------------------------------------
        // 📌 TRI : orderByCol (title) et orderByType (asc/desc)
        // ----------------------------------------------------------
        String orderByCol = ParamUtil.getString(request, "orderByCol", "");
        String orderByType = ParamUtil.getString(request, "orderByType", "asc");
        int count;
        List<Assignment> entries;
        boolean asc = orderByType.equalsIgnoreCase("asc");
        Comparator<Assignment> comparator;

            comparator = Comparator.comparing(
                    a -> a.getTitle(themeDisplay.getLocale()).toLowerCase()
            );

        if (!asc) {
            comparator = comparator.reversed();
        }

        if (!keywords.isEmpty()) {
            // ----- SEARCH MODE -----
            SearchContext searchContext = SearchContextFactory.getInstance(
                    PortalUtil.getHttpServletRequest(request));

            searchContext.setKeywords(keywords);
            searchContext.setStart(start);
            searchContext.setEnd(end);
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
        }
        else {
            // ----- NORMAL MODE -----
            entries = assignmentLocalService.getAssignmentsByGroupId(groupId, start, end);
            count = assignmentLocalService.getAssignmentsCountByGroupId(groupId);
            entries = new ArrayList<>(entries);
            // 🔥 appliquer tri
            entries.sort(comparator);
        }

        request.setAttribute("entries", entries);
        request.setAttribute("entriesCount", count);
        request.setAttribute("hasAddAssignmentPermission", hasAddAssignmentPermission);

        request.setAttribute("orderByCol", orderByCol);
        request.setAttribute("orderByType", orderByType);

        return "/view.jsp";
    }
}


