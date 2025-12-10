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

        <liferay-ui:search-container-column-text name="Assignment">

            <div class="card shadow-sm mb-3 assignment-card">
                <div class="card-body">

                    <!-- 3 petits boutons DANS la carte -->
                    <div class="assignment-actions">

                    </div>
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

    </liferay-ui:search-container-row>

    <liferay-ui:search-iterator markupView="lexicon" />

</liferay-ui:search-container>

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
</style>
