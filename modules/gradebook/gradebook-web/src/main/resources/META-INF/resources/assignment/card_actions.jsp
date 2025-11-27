<%@ include file="/init.jsp"%>
<%
	ResultRow row = (ResultRow) request.getAttribute(
		WebKeys.SEARCH_CONTAINER_RESULT_ROW);

	Assignment assignment = (Assignment) row.getObject();
	String assignmentId =
		StringUtil.valueOf(assignment.getAssignmentId());

	boolean hasEditAssignmentPermission = AssignmentPermissionChecker.contains(
		permissionChecker, scopeGroupId, assignment.getAssignmentId(),
		ActionKeys.UPDATE);
	boolean hasDeleteAssignmentPermission =
		AssignmentPermissionChecker.contains(
			permissionChecker, scopeGroupId,
			assignment.getAssignmentId(), ActionKeys.DELETE);
	boolean hasPermissionsAssignmentPermission =
		AssignmentPermissionChecker.contains(
			permissionChecker, scopeGroupId,
			assignment.getAssignmentId(), ActionKeys.PERMISSIONS);
%>

<liferay-ui:icon-menu markupView="lexicon">
	<c:if test="<%=hasEditAssignmentPermission%>">
		<portlet:renderURL var="editAssignmentURL">
			<portlet:param name="mvcRenderCommandName"
				value="/gradebook/assignment/edit" />
			<portlet:param name="redirect" value="${currentURL}" />
			<portlet:param name="assignmentId" value="${SEARCH_CONTAINER_RESULT_ROW.object.assignmentId}" />
		</portlet:renderURL>

		<liferay-ui:icon message="Edit" url="${editAssignmentURL}" />
	</c:if>
	<c:if test="<%=hasPermissionsAssignmentPermission%>">
		<liferay-security:permissionsURL
			modelResource="com.liferay.training.space.gradebook.model.Assignment"
			modelResourceDescription="<%=assignment.getTitle(locale)%>"
			resourcePrimKey="<%=assignmentId%>" var="permissionsURL" />

		<liferay-ui:icon message="Permissions" url="${permissionsURL}" />
	</c:if>
	<c:if test="<%=hasDeleteAssignmentPermission%>">
		<portlet:actionURL name="/gradebook/assignment/delete"
			var="deleteAssignmentURL">
			<portlet:param name="redirect" value="${currentURL}" />
			<portlet:param name="assignmentId" value="${SEARCH_CONTAINER_RESULT_ROW.object.assignmentId}" />
		</portlet:actionURL>

		<liferay-ui:icon-delete url="${deleteAssignmentURL}" />
	</c:if>
</liferay-ui:icon-menu> 
