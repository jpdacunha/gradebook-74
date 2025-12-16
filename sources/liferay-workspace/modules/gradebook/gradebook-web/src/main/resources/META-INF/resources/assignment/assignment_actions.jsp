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
 <liferay-security:permissionsURL
     modelResource="com.liferay.training.space.gradebook.model.Assignment"
     modelResourceDescription="<%= assignment.getTitle(locale) %>"
     resourcePrimKey="<%= String.valueOf(assignment.getAssignmentId()) %>"
     windowState="<%= LiferayWindowState.POP_UP.toString() %>"
     var="permissionsURL"
 />

   <liferay-ui:icon
       message="permissions"
       url="<%= permissionsURL %>"
       useDialog="true"
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