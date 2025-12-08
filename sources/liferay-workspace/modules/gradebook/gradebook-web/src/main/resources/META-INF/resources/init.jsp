<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/security" prefix="liferay-security" %>
<%@page import="com.liferay.petra.string.StringPool" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>
<%@ page import="com.liferay.training.space.gradebook.model.Assignment" %>
<%@ page import="com.liferay.portal.kernel.util.PortalUtil" %>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %>
<%@ page import="com.liferay.portal.kernel.util.StringUtil" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@ page import="com.liferay.training.space.gradebook.display.context.GradebookManagementToolbarDisplayContext" %>

<%@ page import="com.liferay.portal.kernel.dao.search.ResultRow" %>

<portlet:defineObjects />
<liferay-theme:defineObjects />
<liferay-frontend:defineObjects />

<c:set var="portletNamespace"><portlet:namespace/></c:set>

<%@ page import="com.liferay.portal.kernel.security.permission.ActionKeys" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission" %>
<%@ page import="com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionUtil" %>
<%@page import="com.liferay.training.space.gradebook.service.permission.AssignmentPermissionChecker"%>
