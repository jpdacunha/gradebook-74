<%@ include file="/init.jsp" %>
<%
int totalResults = (int) request.getAttribute("entriesCount");
String orderByCol = ParamUtil.getString(request, "orderByCol", "title");
String cur = ParamUtil.getString(request, "cur", "1");
String orderByType = ParamUtil.getString(request, "orderByType", "asc");
PortletURL sortingURL = renderResponse.createRenderURL();
sortingURL.setParameter("mvcRenderCommandName", "/gradebook/view");
sortingURL.setParameter("orderByCol", orderByCol);
sortingURL.setParameter("orderByType", orderByType.equals("asc") ? "desc" : "asc");
sortingURL.setParameter("cur", cur);

String displayStyle = (String) renderRequest.getAttribute("displayStyle");

PortletURL iteratorURL = renderResponse.createRenderURL();
iteratorURL.setParameter("mvcRenderCommandName", "/gradebook/view");
iteratorURL.setParameter("displayStyle", displayStyle);


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

<script>
function <portlet:namespace />submitDelete() {

    const form = document.getElementById('<portlet:namespace />fm');

    if (!form) {
        console.error('Form not found');
        return;
    }

    form.action = '<portlet:actionURL name="/gradebook/assignment/delete" />';
    form.submit();
}
</script>

