<%@ include file="/init.jsp" %>
<%
int totalResults = (int) request.getAttribute("entriesCount");
String displayStyle = ParamUtil.getString(request, "displayStyle", "table");

GradebookManagementToolbarDisplayContext toolbarContext =
    new GradebookManagementToolbarDisplayContext(
        PortalUtil.getHttpServletRequest(renderRequest),
        liferayPortletRequest,
        liferayPortletResponse,
        totalResults
    );
%>

<div class="container-fluid">
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
</div>
