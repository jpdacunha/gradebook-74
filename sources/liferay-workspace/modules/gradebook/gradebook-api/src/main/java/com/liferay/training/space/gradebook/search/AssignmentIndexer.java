package com.liferay.training.space.gradebook.search;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.IndexWriterHelperUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;

import com.liferay.training.space.gradebook.model.Assignment;
import com.liferay.training.space.gradebook.service.AssignmentLocalService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        service = Indexer.class,
        property = "indexer.class.name=com.liferay.training.space.gradebook.model.Assignment"

        )
public class AssignmentIndexer extends BaseIndexer<Assignment> {

    public static final String CLASS_NAME = Assignment.class.getName();

    public AssignmentIndexer() {
        setDefaultSelectedFieldNames(
                Field.COMPANY_ID, Field.ENTRY_CLASS_NAME, Field.ENTRY_CLASS_PK,
                Field.UID, Field.DESCRIPTION);

        setDefaultSelectedLocalizedFieldNames(Field.TITLE);
        setFilterSearch(true);
        setPermissionAware(true);
    }

    @Reference
    private AssignmentLocalService _assignmentLocalService;

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    @Override
    public boolean hasPermission(
            PermissionChecker permissionChecker, String entryClassName,
            long entryClassPK, String actionId)
            throws Exception {

        Assignment assignment =
                _assignmentLocalService.getAssignment(entryClassPK);

        return permissionChecker.hasPermission(
                assignment.getGroupId(),
                Assignment.class.getName(),
                assignment.getAssignmentId(),
                ActionKeys.VIEW);
    }

    @Override
    public void postProcessContextBooleanFilter(
            BooleanFilter contextBooleanFilter, SearchContext searchContext)
            throws Exception {

        addStatus(contextBooleanFilter, searchContext);
    }

    @Override
    protected void doDelete(Assignment assignment) throws Exception {
        deleteDocument(assignment.getCompanyId(), assignment.getAssignmentId());
    }

    @Override
    protected Document doGetDocument(Assignment assignment)
            throws Exception {

        Document document = getBaseModelDocument(CLASS_NAME, assignment);

        // 🔥 Correction : titleMap peut être vide à cause du XML
        Map<Locale, String> titleMap = assignment.getTitleMap();

        if (titleMap == null || titleMap.isEmpty()) {
            // fallback safe
            titleMap = new HashMap<>();
            titleMap.put(Locale.ENGLISH, "Assignment " + assignment.getAssignmentId());
        }

        document.addLocalizedText(Field.TITLE, titleMap);

        // description
        document.addText(Field.DESCRIPTION, assignment.getDescription());

        return document;
    }

    @Override
    protected Summary doGetSummary(
            Document document, Locale locale, String snippet,
            PortletRequest portletRequest, PortletResponse portletResponse)
            throws Exception {

        String prefix = Field.SNIPPET + StringPool.UNDERLINE;

        String title = document.get(prefix + Field.TITLE, Field.TITLE);
        String content = HtmlUtil.stripHtml(
                document.get(prefix + Field.DESCRIPTION, Field.DESCRIPTION));

        Summary summary = new Summary(title, content);
        summary.setMaxContentLength(200);
        return summary;
    }

    @Override
    protected void doReindex(String className, long classPK)
            throws Exception {

        Assignment assignment =
                _assignmentLocalService.getAssignment(classPK);

        doReindex(assignment);
    }

    @Override
    protected void doReindex(String[] ids) throws Exception {
        long companyId = GetterUtil.getLong(ids[0]);
        reindexAssignments(companyId);
    }

    @Override
    protected void doReindex(Assignment assignment) throws Exception {
        Document document = getDocument(assignment);

        IndexWriterHelperUtil.updateDocument(
                assignment.getCompanyId(), document);
    }

    protected void reindexAssignments(long companyId)
            throws PortalException {

        IndexableActionableDynamicQuery query =
                _assignmentLocalService.getIndexableActionableDynamicQuery();

        query.setCompanyId(companyId);

        query.setPerformActionMethod(
                new ActionableDynamicQuery.PerformActionMethod<Assignment>() {

                    @Override
                    public void performAction(Assignment assignment) {
                        try {
                            Document document = getDocument(assignment);
                            query.addDocuments(document);
                        }
                        catch (Exception e) {
                            _log.error("Unable to index assignment " +
                                    assignment.getAssignmentId(), e);
                        }
                    }
                });

        query.performActions();
    }

    private static final Log _log =
            LogFactoryUtil.getLog(AssignmentIndexer.class);
}
