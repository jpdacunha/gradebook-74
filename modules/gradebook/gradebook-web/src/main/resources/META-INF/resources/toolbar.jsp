<%@ taglib uri="http://liferay.com/tld/clay" prefix="clay" %>
<%@ page import="javax.portlet.PortletURL" %>

<%
boolean hasAddPermission = (boolean)request.getAttribute("hasAddAssignmentPermission");

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
    creationMenu="<%= creationMenu %>"
    searchContainerId="gradebookEntries"
    showSearch="<%= false %>"
    spritemap="<%= themeDisplay.getPathThemeImages() + "/clay/icons.svg" %>"
/>
