<%@ include file="/init.jsp" %>
    <%
    ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
    Assignment assignment = (Assignment)row.getObject();

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
<liferay-ui:icon-menu
    direction="left-side"
    icon="<%= StringPool.BLANK %>"
    markupView="lexicon"
    message="<%= StringPool.BLANK %>"
    showWhenSingleIcon="<%= true %>"
    >
    <c:if test="<%=hasEditAssignmentPermission%>">
        <portlet:renderURL var="editURL">
            <portlet:param name="mvcRenderCommandName" value="/gradebook/assignment/edit" />
                <portlet:param name="assignmentId" value="<%= String.valueOf(assignment.getAssignmentId()) %>" />
            </portlet:renderURL>
            <liferay-ui:icon
            message="edit"
            url="<%= editURL %>"
        />
    </c:if>

    <c:if test="<%=hasPermissionsAssignmentPermission%>">
        <portlet:renderURL var="permissionURL">
            <portlet:param name="mvcRenderCommandName" value="/assignment/view_permission" />
            </portlet:renderURL>
            <liferay-ui:icon
            message="permission"
            url="<%= permissionURL %>"
        />
    </c:if>

    <c:if test="<%=hasDeleteAssignmentPermission%>">
        <portlet:actionURL name="/gradebook/assignment/delete" var="deleteURL">
            <portlet:param name="assignmentId" value="<%= String.valueOf(assignment.getAssignmentId()) %>" />
            </portlet:actionURL>
            <liferay-ui:icon
                message="delete"
                url="<%= deleteURL %>"
        />
    </c:if>

</liferay-ui:icon-menu>