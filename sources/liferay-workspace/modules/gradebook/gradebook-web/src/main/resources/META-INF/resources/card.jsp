<%@ include file="/init.jsp" %>
<div class="container mt-2" style="max-width: 1200px;">
<aui:form name="fm" method="post">
<liferay-ui:search-container
    iteratorURL="<%= iteratorURL %>"
    rowChecker="<%= new RowChecker(renderResponse) %>"
    id="gradebookEntries"
    delta="5"
    curParam="cur"
    total="${entriesCount}"
    emptyResultsMessage="No assignments found">

    <liferay-ui:search-container-results results="${entries}" />

    <liferay-ui:search-container-row
            className="com.liferay.training.space.gradebook.model.Assignment"
            modelVar="assignment"
             keyProperty="assignmentId"
             rowIdProperty="assignmentId">

        <liferay-ui:search-container-column-text name="Assignment">

            <div class="card shadow-sm mb-3 assignment-card">
                <div class="card-body">

                    <!-- 3 petits boutons DANS la carte -->
                    <div class="assignment-actions">

                    </div>
                    <br></br>
                    <liferay-ui:user-portrait
                        userId="<%= assignment.getUserId() %>"
                        userName="<%= assignment.getUserName() %>"
                        cssClass="mr-2" />
                    <h5 class="card-title">
                        ${assignment.getTitle(locale)}
                    </h5>
                     <h5 class="card-title">
                     ${assignment.getUserName()}
                     </h5>

                    <p class="card-text">
                        ${assignment.description}
                    </p>

                    <p class="text-muted">
                        Modified:
                        <fmt:formatDate value="${assignment.modifiedDate}"
                                        pattern="dd/MM/yyyy HH:mm" />
                    </p>

                </div>
            </div>

        </liferay-ui:search-container-column-text>

        <!-- Colonne des actions -->
                <liferay-ui:search-container-column-jsp
                               cssClass="assignment-actions-cell"
                               path="/assignment/assignment_actions.jsp" />

    </liferay-ui:search-container-row>

    <liferay-ui:search-iterator markupView="lexicon" />

</liferay-ui:search-container>
</aui:form>

</div>
<style>
/* GRID - 4 cartes par ligne */
.lfr-search-container-wrapper table > tbody {
    display: flex;
    flex-wrap: wrap;
    gap: 24px;
}

.lfr-search-container-wrapper table > tbody > tr {
    flex: 0 0 calc(25% - 24px);
    display: block;
}

.lfr-search-container-wrapper table > tbody > tr > td {
    display: block;
    width: 100%;
    border: none !important;
    padding: 0 !important;
}

/* Styles pour la carte */
.card {
    position: relative;
    border-radius: 12px;
    min-height: 240px;
    padding: 16px;
}

/* Boutons ⋮ dans la card */
.assignment-actions {
    position: absolute;
    top: 10px;
    right: 10px;
}

/* Masquer l’en-tête checkbox */
.lfr-search-container-wrapper thead {
    display: none;
}

/* Positionnement global des lignes */
.lfr-search-container-wrapper tbody tr {
    position: relative;
}

/* Checkbox RowChecker */
.lfr-search-container-wrapper .lfr-checkbox-column {
    position: absolute;
    top: 12px;
    left: 12px;
    z-index: 10;
    background: #fff;
    border-radius: 6px;
    padding: 4px;
}

/* Ajuster la checkbox */
.lfr-search-container-wrapper .lfr-checkbox-column input[type="checkbox"] {
    width: 18px;
    height: 18px;
    cursor: pointer;
}

/* Empêcher la checkbox de prendre de la place */
.lfr-search-container-wrapper td.lfr-checkbox-column {
    width: auto !important;
    padding: 0 !important;
    border: none !important;
}

/* Cache l’en-tête du tableau */
.lfr-search-container-wrapper thead {
    display: none !important;
}

/* Supprime le style table */
.lfr-search-container-wrapper table {
    display: block !important;
    border: none !important;
}

/* Supprime les styles table Liferay */
.lfr-search-container-wrapper table.table,
.lfr-search-container-wrapper table.table-list,
.lfr-search-container-wrapper table.table-autofit {
    background: transparent !important;
    border: none !important;
}

/* Supprime les hover / quick actions */
.show-quick-actions-on-hover .quick-action-menu,
.show-quick-actions-on-hover:hover .quick-action-menu {
    display: none !important;
}

/* Supprime les bordures */
.lfr-search-container-wrapper td,
.lfr-search-container-wrapper tr {
    border: none !important;
}

/* Désactive le fond bleu Liferay quand une ligne est sélectionnée */
.lfr-search-container-wrapper
tr.active,
.lfr-search-container-wrapper
tr.table-active,
.lfr-search-container-wrapper
tr.selected {
    background-color: transparent !important;
}

/* La ligne devient un conteneur */
.lfr-search-container-wrapper tbody tr {
    position: relative;
}

/* On enlève la colonne actions du flux */
.assignment-actions-cell {
    position: absolute !important;
    top: 14px;
    right: 14px;
    width: auto !important;
    padding: 0 !important;
    border: none !important;
    background: transparent !important;
    z-index: 20;
}

/* On supprime l’espace réservé par la colonne */
.assignment-actions-cell:empty {
    display: none;
}

/* Les boutons ⋮ */
.assignment-actions-cell .dropdown,
.assignment-actions-cell .btn,
.assignment-actions-cell .lexicon-icon {
    margin: 0;
}

</style>
