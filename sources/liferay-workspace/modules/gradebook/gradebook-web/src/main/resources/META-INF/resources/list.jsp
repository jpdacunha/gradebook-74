<%@ include file="/init.jsp" %>

<div class="container mt-2" style="max-width: 1200px;">
<aui:form name="fm" method="post">
<liferay-ui:search-container
       rowChecker="<%= new RowChecker(renderResponse) %>"
       iteratorURL="<%= iteratorURL %>"
       id="gradebookEntries"
        delta="5"
        curParam="cur"
        total="${entriesCount}"
        emptyResultsMessage="No assignments found">

    <liferay-ui:search-container-results results="${entries}" />
 <ul class="list-group list-group-notification show-quick-actions-on-hover">

        <liferay-ui:search-container-row
            className="com.liferay.training.space.gradebook.model.Assignment"
            modelVar="assignment"
            keyProperty="assignmentId"
            rowIdProperty="assignmentId">

          <!-- COLONNE PRINCIPALE (comme Blogs) -->
               <liferay-ui:search-container-column-text>

                   <div class="entry-row">

                       <liferay-ui:user-portrait
                           userId="<%= assignment.getUserId() %>"
                           userName="<%= assignment.getUserName() %>"
                           cssClass="entry-avatar" />

                       <div class="entry-content">

                           <div class="entry-meta">
                               ${assignment.getUserName()}
                               ·
                               <fmt:formatDate value="${assignment.getModifiedDate()}" pattern="dd/MM/yyyy HH:mm"/>
                           </div>
                               <div class="entry-title">
                               <strong><u>${assignment.getTitle(locale)}</u></strong>
                           </div>
                           <div class="entry-description">
                               ${assignment.description}
                           </div>
                       </div>

                   </div>

               </liferay-ui:search-container-column-text>

                       <liferay-ui:search-container-column-jsp
                       cssClass="assignment-actions-cell"
                       path="/assignment/assignment_actions.jsp" />

          </liferay-ui:search-container-row>
        <liferay-ui:search-iterator markupView="lexicon" />

          </liferay-ui:search-container>

          </aui:form>
      </div>
<style>
/* Ligne principale (checkbox + contenu alignés) */
.lfr-search-container-wrapper tbody tr {
    display: flex;
    align-items: flex-start;
}


/* Checkbox */
.lfr-search-container-wrapper .lfr-checkbox-column {
    flex: 0 0 32px;
    margin-top: 14px;
}

/* Contenu de la ligne */
.entry-row {
    display: flex;
    align-items: flex-start;
    gap: 12px;
}

/* Avatar */
.entry-avatar {
    flex: 0 0 auto;
}

/* Bloc texte */
.entry-content {
    display: flex;
    flex-direction: column;
}

/* Titre : UNE SEULE LIGNE */
.entry-title {
    font-weight: 600;
    white-space: nowrap;        /* 🔥 clé */
}

/* Meta */
.entry-meta {
    font-size: 13px;
    color: #6b6c7e;
}

/* Description */
.entry-description {
    font-size: 14px;
    color: #272833;
}
.table-list th, .table-list td {
    border-color: #e7e7ed;
    border-style: solid;
    border-width: 0rem;
}
/* Contenu principal */
.lfr-search-container-wrapper td:not(.lfr-checkbox-column):not(.assignment-actions-cell) {
    flex: 1;
    margin-top: 15PX;
}


/* Actions à droite */
.assignment-actions-cell {
    flex: 0 0 48px;
    display: flex;
    justify-content: flex-end;
    align-items: center;
}


</style>