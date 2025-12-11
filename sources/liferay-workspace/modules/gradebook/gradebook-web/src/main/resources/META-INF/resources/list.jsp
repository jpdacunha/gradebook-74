<%@ include file="/init.jsp" %>

<div class="container mt-2" style="max-width: 1200px;">

<liferay-ui:search-container
       iteratorURL="<%= iteratorURL %>"
       id="gradebookEntries"
        delta="5"
        curParam="cur"
        total="${entriesCount}"
        emptyResultsMessage="No assignments found">

    <liferay-ui:search-container-results results="${entries}" />

    <liferay-ui:search-container-row
            className="com.liferay.training.space.gradebook.model.Assignment"
            modelVar="assignment">

        <!-- Colonne principale : titre + description -->

     <liferay-ui:search-container-column-text name="Assignment">
       <liferay-ui:user-portrait
    userId="<%= assignment.getUserId() %>"
    userName="<%= assignment.getUserName() %>"
    cssClass="mr-2" />
               <strong>${assignment.getUserName()}</strong>,
               <small>${assignment.getModifiedDate()}</small><br/>
                <strong style="margin-left:44px">${assignment.getTitle(locale)}</strong><br/>
                <small style="margin-left:44px">${assignment.description}</small>

        </liferay-ui:search-container-column-text>

        <!-- Colonne des actions -->
        <liferay-ui:search-container-column-jsp
                       cssClass="table-column-text-end"
                       path="/assignment/assignment_actions.jsp" />

    </liferay-ui:search-container-row>

    <liferay-ui:search-iterator markupView="lexicon" />

</liferay-ui:search-container>

</div>
