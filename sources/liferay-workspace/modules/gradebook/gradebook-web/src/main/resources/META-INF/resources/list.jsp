<%@ include file="/init.jsp" %>
<div class="container mt-2" style="max-width: 1200px;">
<div class="card-body p-0">
<ul class="list-group">
    <c:forEach var="assignment" items="${entries}">
        <li class="list-group-item d-flex justify-content-between align-items-center">
            <div>
                <strong>${assignment.getTitle(locale)}</strong><br/>
                <small>${assignment.description}</small>
            </div>
            <div>
                 <liferay-util:include
                    page="/assignment/assignment_actions.jsp"
                     servletContext="<%= application %>">
                </liferay-util:include>
            </div>
        </li>
    </c:forEach>
</ul>
</div>
</div>
