<%@ include file="/init.jsp" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ page import="com.liferay.portal.kernel.dao.search.RowChecker" %>
<div class="container mt-2" style="max-width: 1200px;">
<div class="card-body p-0">
<aui:form name="fm" method="post">
<liferay-ui:search-container
        rowChecker="<%= new RowChecker(renderResponse) %>"
        id="gradebookEntries"
        delta="5"
        curParam="cur"
        total="${entriesCount}"
        emptyResultsMessage="No assignments found">

    <liferay-ui:search-container-results results="${entries}" />

    <liferay-ui:search-container-row
            className="com.liferay.training.space.gradebook.model.Assignment"
            modelVar="assignment"
            keyProperty="assignmentId"
            rowIdProperty="assignmentId">


        <liferay-ui:search-container-column-text
                name="Title"
                value="${assignment.getTitle(locale)}" />
       <liferay-ui:search-container-column-text
                name="Created by"
                value="${assignment.getUserName()}" />
        <liferay-ui:search-container-column-text
                name="Description"
                value="${assignment.getDescription()}" />

        <liferay-ui:search-container-column-date
                name="Modified"
                value="${assignment.getModifiedDate()}" />

        <liferay-ui:search-container-column-jsp
                cssClass="table-column-text-end"
                path="/assignment/assignment_actions.jsp" />
    </liferay-ui:search-container-row>

    <liferay-ui:search-iterator markupView="lexicon" />
</liferay-ui:search-container>
</aui:form>
</div>
</div>

