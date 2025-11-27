<%@ include file="/init.jsp" %>

<%
    Assignment assignment = (Assignment) request.getAttribute("assignment");
    String redirect = ParamUtil.getString(request, "redirect");
%>

<portlet:actionURL var="actionURL"
    name="<%= (assignment == null) ? \"/gradebook/assignment/add\" : \"/gradebook/assignment/edit\" %>">
    <portlet:param name="redirect" value="<%= redirect %>" />
</portlet:actionURL>

<div class="container-fluid-1280">

    <aui:model-context bean="<%= assignment %>" model="<%= Assignment.class %>" />

    <aui:form name="fm" action="${actionURL}">

        <aui:input name="assignmentId" type="hidden" />

        <!-- TITLE -->
        <aui:input name="title" label="Title" required="true" />

        <!-- DESCRIPTION -->
        <aui:input name="description" type="textarea" label="Description" />

        <!-- DUE DATE (simple DATE field that Liferay parses) -->
        <aui:input name="dueDate" type="date" label="Due Date"
                   value="<%= (assignment != null) ? assignment.getDueDate() : null %>" />

        <aui:button type="submit" value="Save" />

    </aui:form>
</div>
