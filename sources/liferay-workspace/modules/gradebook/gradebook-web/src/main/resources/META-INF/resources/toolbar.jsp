
<%@ include file="/init.jsp" %>

<%
Boolean hasAddObj = (Boolean) request.getAttribute("hasAddAssignmentPermission");
boolean hasAddPermission = (hasAddObj != null ? hasAddObj : false);
String addURL = "";

if (hasAddPermission) {
    PortletURL url = renderResponse.createRenderURL();
    url.setParameter("mvcRenderCommandName", "/gradebook/assignment/edit");
    addURL = url.toString();
}

final String addURLFinal = addURL;

com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu creationMenu = null;

if (hasAddPermission) {
    creationMenu = new com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu();

    creationMenu.addPrimaryDropdownItem(dropdownItem -> {
        dropdownItem.setHref(addURLFinal);
        dropdownItem.setLabel("Add Assignment");
    });
}
%>


<clay:management-toolbar
    componentId="gradebookToolbar"
    searchActionURL="<%= toolbarContext.getSearchActionURL().toString() %>"
    clearResultsURL="<%= toolbarContext.getClearResultsURL().toString() %>"
    sortingOrder="<%= orderByType %>"
    sortingURL="<%= sortingURL.toString() %>"
	itemsTotal="<%= toolbarContext.getTotal() %>"
    creationMenu="<%= creationMenu %>"
    searchContainerId="gradebookEntries"
    viewTypeItems="<%= toolbarContext.getViewTypeItems() %>"
    filterDropdownItems="<%= toolbarContext.getFilterDropdownItems() %>"
    actionDropdownItems="<%= toolbarContext.getActionDropdownItems() %>"
    showSearch="<%= true %>"
    spritemap="<%= themeDisplay.getPathThemeImages() + "/clay/icons.svg" %>"
/>

