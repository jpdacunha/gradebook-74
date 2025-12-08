<%@ include file="/init.jsp" %>
<div class="container mt-2" style="max-width: 1200px;">
<div class="card-body p-0">
<div class="row">
    <c:forEach var="assignment" items="${entries}">
        <div class="col-md-4 mb-3">
            <div class="card shadow-sm h-100">
                <div class="card-body">
                     <h5>${assignment.getTitle(locale)}</h5>
                     <p>${assignment.description}</p>
                      <p class="text-muted">
                       Modified: ${assignment.modifiedDate}
                          </p>
                      </div>
                <liferay-util:include
                        page="/assignment/assignment_actions.jsp"
                        servletContext="<%= application %>">
                </liferay-util:include>
            </div>
        </div>
    </c:forEach>
</div>
</div>
</div>
