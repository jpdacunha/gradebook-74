<%@ include file="/init.jsp" %>
<%
int totalResults = (int) request.getAttribute("entriesCount");

GradebookManagementToolbarDisplayContext toolbarContext =
    new GradebookManagementToolbarDisplayContext(
        PortalUtil.getHttpServletRequest(renderRequest),
        liferayPortletRequest,
        liferayPortletResponse,
        totalResults
    );
%>
<div class="container mt-5" style="max-width: 1200px;">

    <div class="card shadow-sm mb-4">
        <div class="card-body p-3">
            <%@ include file="/toolbar.jsp" %>
        </div>
    </div>

    <div class="card shadow-sm">
        <div class="card-body p-0">

            <liferay-ui:search-container
                id="gradebookEntries"
                delta="5"
                curParam="cur"
                total="${entriesCount}"
                emptyResultsMessage="No assignments found">

                <liferay-ui:search-container-results  results="${entries}"/>

                <liferay-ui:search-container-row
                        className="com.liferay.training.space.gradebook.model.Assignment"
                        modelVar="assignment"
                        keyProperty="assignmentId"
                 >
                <portlet:renderURL var="rowURL">
                                   <portlet:param name="mvcRenderCommandName" value="/gradebook/assignment/edit"/>
                                   <portlet:param name="assignmentId" value="<%= String.valueOf(assignment.getAssignmentId()) %>" />
                               </portlet:renderURL>
                    <liferay-ui:search-container-column-text
                            name="Title"
                            value="${assignment.getTitle(locale)}" />

                    <liferay-ui:search-container-column-text
                            name="Description"
                            value="${assignment.getDescription()}" />

                    <liferay-ui:search-container-column-date
                            name="Modified"
                            value="${assignment.getModifiedDate()}" />

                    <liferay-ui:search-container-column-jsp
                            cssClass="table-column-text-end"
                            path="/assignment/assignment_actions.jsp"
                    />

                </liferay-ui:search-container-row>

                <liferay-ui:search-iterator markupView="lexicon"/>
            </liferay-ui:search-container>

        </div>
    </div>

</div>
