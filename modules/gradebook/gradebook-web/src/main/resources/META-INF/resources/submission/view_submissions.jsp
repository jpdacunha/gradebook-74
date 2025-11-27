<%@ include file="/init.jsp"%>

<%
	String redirect = renderRequest.getParameter("redirect");
	String title = (String) request.getAttribute("title");

	renderResponse.setTitle(title);

	portletDisplay.setShowBackIcon(true);
	portletDisplay.setURLBack(redirect);
%>

<portlet:renderURL var="addSubmissionURL">
	<portlet:param name="mvcRenderCommandName"
		value="/gradebook/submission/edit" />
	<portlet:param name="redirect" value="${currentURL}" />
</portlet:renderURL>

<div class="container-fluid-1280">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">${assignment.getTitle(locale)}</h3>
		</div>
		<div class="panel-heading">
			<h3 class="panel-title">${assignment.getTitle(locale)}</h3>
		</div>
		<div class="panel-body">
			<div class="col-md-6">
				${assignment.getDescription()}
			</div>
			<div class="col-md-6">
				<liferay-frontend:horizontal-card text="${dueDate}">
					<liferay-frontend:horizontal-card-col>
						<liferay-frontend:horizontal-card-icon icon="time" />
					</liferay-frontend:horizontal-card-col>
				</liferay-frontend:horizontal-card>
			</div>
		</div>
	</div>

	<liferay-ui:search-container
		emptyResultsMessage="No one has submitted this assignment yet.">
		<liferay-ui:search-container-results results="${Submissions}" />
		<liferay-ui:search-container-row
			className="com.liferay.training.space.gradebook.model.Submission"
			modelVar="submission">

			<liferay-ui:search-container-column-date property="submitDate"
				name="Submitted" />

			<liferay-ui:search-container-column-text property="grade"
				name="Result" />
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator markupView="lexicon" />

	</liferay-ui:search-container>
</div>

<%@ include file="/init.jsp"%>

<portlet:renderURL var="addAssignmentURL">
	<portlet:param name="mvcRenderCommandName"
		value="/gradebook/assignment/edit" />
	<portlet:param name="redirect" value="${currentURL}" />
</portlet:renderURL>



<c:if test="${hasAddAssignmentPermission}">
	<liferay-frontend:add-menu>
		<liferay-frontend:add-menu-item title="Assignment"
			url="${addAssignmentURL}" />
	</liferay-frontend:add-menu>
</c:if>
<liferay-ui:success key="assignment-added" message="assignment-added-successfully" />
<liferay-ui:success key="assignment-deleted" message="assignment-deleted-successfully" />
<liferay-ui:success key="assignment-updated" message="assignment-updated-successfully" />

<div class="container-fluid-1280">
	<div class="flex-container">

		<liferay-ui:search-container emptyResultsMessage="There are no assignments for this class.">
			<liferay-ui:search-container-results results="${Assignments}" />
						<c:set var="student"
				value="${students.get(submission.submissionId) }" />
			<liferay-ui:search-container-column-text name="name">
				${student.fullName}
			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-date property="submitDate"
				name="assignment-submit-date" />

			<liferay-ui:search-container-column-text name="assignment-result">
				<fmt:formatNumber value="${submission.grade}" type="Percent" />
			</liferay-ui:search-container-column-text>

			<liferay-ui:search-iterator markupView="lexicon" />
		</liferay-ui:search-container>
	</div>
</div>
