<%@ include file="/init.jsp" %>
<%
    Assignment assignment = (Assignment) request.getAttribute("assignment");
    String redirect = ParamUtil.getString(request, "redirect");

    // Formatage correct pour <input type="date">
    String dueDateFormatted = "";

    if (assignment != null && assignment.getDueDate() != null) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dueDateFormatted = sdf.format(assignment.getDueDate());
    }
%>

<portlet:actionURL var="actionURL"
    name="<%= (assignment == null) ? \"/gradebook/assignment/add\" : \"/gradebook/assignment/edit\" %>">
    <portlet:param name="redirect" value="<%= redirect %>" />
</portlet:actionURL>
<div class="container mt-5" style="max-width: 1200px;">
<div class="card shadow-sm">
        <div class="card-body p-0">

    <aui:model-context bean="<%= assignment %>" model="<%= Assignment.class %>" />

    <aui:form name="fm" action="${actionURL}">

        <aui:input name="assignmentId" type="hidden" />

        <!-- TITLE -->
        <aui:input name="title" label="Title" required="true" />

        <!-- DESCRIPTION -->
        <aui:input name="description" type="textarea" label="Description" />

        <!-- DUE DATE (must be yyyy-MM-dd for HTML5 date inputs) -->
        <aui:input name="dueDate"
                   type="date"
                   label="Due Date"
                   value="<%= dueDateFormatted %>" />

        <aui:button type="submit" value="Save" />

    </aui:form>
</div>
 </div>
    </div>
