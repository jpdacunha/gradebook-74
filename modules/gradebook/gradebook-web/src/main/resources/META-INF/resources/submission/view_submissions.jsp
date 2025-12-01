<%@ include file="/init.jsp"%>

<%
    String redirect = renderRequest.getParameter("redirect");
    String title = (String) request.getAttribute("title");

    renderResponse.setTitle(title);
    portletDisplay.setShowBackIcon(true);
    portletDisplay.setURLBack(redirect);
%>

<div class="container mt-5" style="max-width: 1200px;">
<div class="card shadow-sm">
    <!-- ========================== -->
    <!--   HEADER ASSIGNMENT        -->
    <!-- ========================== -->

    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">${assignment.getTitle(locale)}</h3>
        </div>

        <div class="panel-body">
            <!-- LEFT : Description -->
            <div class="col-md-6">
                ${assignment.getDescription()}
            </div>

            <!-- RIGHT : Due Date card -->
            <div class="col-md-6">

                <clay:card-horizontal decoration="true" selectable="false">
                    <clay:sticker displayType="secondary" icon="time" />

                    <clay:content-row>
                        <clay:content-col expand="true">
                            <clay:content-title>
                                <fmt:formatDate value="${assignment.getDueDate()}"
                                                type="date"
                                                pattern="dd/MM/yyyy" />
                            </clay:content-title>
                        </clay:content-col>
                    </clay:content-row>
                </clay:card-horizontal>

            </div>

        </div>
    </div>


    <!-- ========================== -->
    <!--   LISTE DES SOUMISSIONS    -->
    <!-- ========================== -->

    <liferay-ui:search-container emptyResultsMessage="No submissions yet.">

        <liferay-ui:search-container-results results="${Submissions}" />

        <liferay-ui:search-container-row
                className="com.liferay.training.space.gradebook.model.Submission"
                modelVar="submission">

            <liferay-ui:search-container-column-date
                    property="submitDate"
                    name="Submitted" />

            <liferay-ui:search-container-column-text
                    property="grade"
                    name="Result" />

        </liferay-ui:search-container-row>

        <liferay-ui:search-iterator markupView="lexicon" />
    </liferay-ui:search-container>

</div>


<!-- ========================== -->
<!--   ADD ASSIGNMENT BUTTON    -->
<!-- ========================== -->

<portlet:renderURL var="addAssignmentURL">
    <portlet:param name="mvcRenderCommandName" value="/gradebook/assignment/edit" />
    <portlet:param name="redirect" value="${currentURL}" />
</portlet:renderURL>

    <clay:button
        style="primary"
        label="Add Assignment"
        href="${addAssignmentURL}"
    />

<!-- Success messages -->
<liferay-ui:success key="assignment-added" message="assignment-added-successfully" />
<liferay-ui:success key="assignment-deleted" message="assignment-deleted-successfully" />
<liferay-ui:success key="assignment-updated" message="assignment-updated-successfully" />


<!-- ========================== -->
<!--   ⚠️ CODE DOUTEUX DETECTÉ  -->
<!-- ========================== -->

<!-- Tu avais un search-container pour "Assignments" mais tu utilises
     des variables "submission" et "student" dedans → incohérent.
     Je le laisse ici mais commenté, à revoir complètement !
-->

<%--
<div class="container-fluid-1280">
    <div class="flex-container">

        <liferay-ui:search-container emptyResultsMessage="There are no assignments for this class.">
            <liferay-ui:search-container-results results="${Assignments}" />

            <c:set var="student" value="${students.get(submission.submissionId)}" />

            <liferay-ui:search-container-column-text name="name">
                ${student.fullName}
            </liferay-ui:search-container-column-text>

            <liferay-ui:search-container-column-date
                    property="submitDate"
                    name="assignment-submit-date" />

            <liferay-ui:search-container-column-text name="assignment-result">
                <fmt:formatNumber value="${submission.grade}" type="Percent" />
            </liferay-ui:search-container-column-text>

            <liferay-ui:search-iterator markupView="lexicon" />
        </liferay-ui:search-container>

    </div>
    </div>

</div>
--%>
