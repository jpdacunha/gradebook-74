<%@ include file="/init.jsp" %>
<%
    Assignment assignment = (Assignment) request.getAttribute("assignment");
    String redirect = ParamUtil.getString(request, "redirect");
    String displayStyle = (String) renderRequest.getAttribute("displayStyle");
    // Formatage correct pour <input type="date">
    String dueDateFormatted = "";

    if (assignment != null && assignment.getDueDate() != null) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dueDateFormatted = sdf.format(assignment.getDueDate());
    }

    // On récupère l'URL de retour passée en paramètre si elle existe
    String backURL = ParamUtil.getString(request, "redirect");

    // Si pas de redirect dans l'URL, on renvoie vers la vue principale du gradebook
    if (backURL == null || backURL.isEmpty()) {
        PortletURL listURL = renderResponse.createRenderURL();
        listURL.setParameter("mvcRenderCommandName", "/gradebook/view");
        backURL = listURL.toString();
    }
    portletDisplay.setShowBackIcon(true);
    portletDisplay.setURLBack(backURL);

    // 🏷 Titre de la page (en haut à côté du bouton Back)
    if (assignment != null && assignment.getAssignmentId() > 0) {
        portletDisplay.setTitle("Edit Assignment");
    }
    else {
        portletDisplay.setTitle("New Assignment");
    }
PortletURL iteratorURL = renderResponse.createRenderURL();
iteratorURL.setParameter("displayStyle", displayStyle);
%>

<portlet:actionURL var="actionURL"
    name="<%= (assignment == null) ? \"/gradebook/assignment/add\" : \"/gradebook/assignment/edit\" %>">
    <portlet:param name="redirect" value="<%= redirect %>" />
</portlet:actionURL>

<div class="lfr-form-content">
<div class="container mt-5" style="max-width: 1200px; background-color: white;">
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
       <!-- CATEGORY SELECTOR -->
        <liferay-asset:asset-categories-selector
        className="<%= Assignment.class.getName() %>"
        classPK="<%= (assignment != null) ? assignment.getAssignmentId() : 0 %>"
        />
        <aui:button type="submit" value="Save" />
        <portlet:renderURL var="cancelURL">
            <portlet:param name="mvcRenderCommandName" value="/gradebook/view" />
        </portlet:renderURL>
       <aui:button href="<%= backURL %>" value="Cancel" cssClass="btn btn-secondary ml-2" />
    </aui:form>
</div>
</div>
</div>
</div>

