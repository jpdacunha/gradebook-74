<%@ include file="/init.jsp" %>
<%@ include file="/toolbar.jsp" %>

<liferay-ui:search-container delta="5" emptyResultsMessage="No assignments found">

    <liferay-ui:search-container-results results="${Assignments}" />

    <liferay-ui:search-container-row
            className="com.liferay.training.space.gradebook.model.Assignment"
            modelVar="assignment">

        <liferay-ui:search-container-column-text
                name="Title"
                value="${assignment.getTitle(locale)}" />

        <liferay-ui:search-container-column-text
                name="Description"
                value="${assignment.getDescription()}" />

        <liferay-ui:search-container-column-date
                name="Modified"
                value="${assignment.getModifiedDate()}" />

        <liferay-ui:search-container-column-jsp
                cssClass="table-column-text-end"
                path="/assignement_action.jsp"
        />

    </liferay-ui:search-container-row>

    <liferay-ui:search-iterator markupView="lexicon"/>
</liferay-ui:search-container>
