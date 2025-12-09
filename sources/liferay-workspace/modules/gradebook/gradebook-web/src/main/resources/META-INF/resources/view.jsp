<%@ include file="/init.jsp" %>
<%
int totalResults = (int) request.getAttribute("entriesCount");
String displayStyle = ParamUtil.getString(request, "displayStyle", "table");
String orderByCol = ParamUtil.getString(request, "orderByCol", "title");
String orderByType = ParamUtil.getString(request, "orderByType", "asc");
PortletURL sortingURL = renderResponse.createRenderURL();
sortingURL.setParameter("mvcRenderCommandName", "/gradebook/view");
sortingURL.setParameter("orderByCol", orderByCol);
sortingURL.setParameter("orderByType", orderByType.equals("asc") ? "desc" : "asc");

GradebookManagementToolbarDisplayContext toolbarContext =
    new GradebookManagementToolbarDisplayContext(
        PortalUtil.getHttpServletRequest(renderRequest),
        liferayPortletRequest,
        liferayPortletResponse,
        totalResults
    );
%>

    <%@ include file="/toolbar.jsp" %>
    <c:choose>
        <c:when test="<%= displayStyle.equals("cards") %>">
            <%@ include file="/card.jsp" %>
        </c:when>

        <c:when test="<%= displayStyle.equals("list") %>">
            <%@ include file="/list.jsp" %>
        </c:when>

        <c:otherwise>
            <%@ include file="/table.jsp" %>
        </c:otherwise>
    </c:choose>
