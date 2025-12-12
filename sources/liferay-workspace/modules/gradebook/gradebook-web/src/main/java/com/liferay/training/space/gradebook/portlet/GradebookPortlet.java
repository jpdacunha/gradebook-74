package com.liferay.training.space.gradebook.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.space.gradebook.model.Assignment;
import com.liferay.training.space.gradebook.service.AssignmentLocalService;
import com.liferay.training.space.gradebook.service.permission.AssignmentPermissionChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
        immediate = true,
        property = {
                "com.liferay.portlet.add-default-resource=true",
                "com.liferay.portlet.display-category=category.sample",
                "com.liferay.portlet.preferences-owned-by-group=true",
                "com.liferay.portlet.private-request-attributes=false",
                "com.liferay.portlet.private-session-attributes=false",
                "com.liferay.portlet.render-weight=50",
                "com.liferay.portlet.scopeable=true",
                "com.liferay.portlet.struts-path=gradebook",
                "javax.portlet.display-name=Gradebook",
                "javax.portlet.expiration-cache=0",
                "javax.portlet.init-param.always-display-default-configuration-icons=true",
                "javax.portlet.init-param.view-template=/view.jsp",
                "javax.portlet.init-param.portlet-title-based-navigation=false",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.name=" + GradebookPortletKeys.PORTLET_NAME,
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.security-role-ref=power-user,user",
                "javax.portlet.supported-public-render-parameter=resetCur",
                "javax.portlet.supported-public-render-parameter=tag",
                "javax.portlet.version=3.0",
                "mvc.command.name.default=/gradebook/view",
        },
        service = Portlet.class
)
public class GradebookPortlet extends MVCPortlet {

    // Injection du service métier pour manipuler les Assignments
    @Reference protected AssignmentLocalService assignmentLocalService;

    // Méthode principale appelée lors du rendu du portlet
    @Override
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {

        // Récupération du contexte Liferay (utilisateur, groupe, société, permissions)
        ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

        // Récupération de l’identifiant du site courant
        long groupId = themeDisplay.getScopeGroupId();

        // Vérification de la permission d’ajout d’un Assignment
        boolean hasAddAssignmentPermission = AssignmentPermissionChecker.containsTopLevel(themeDisplay.getPermissionChecker(), groupId, AssignmentPermissionChecker.ADD_ASSIGNMENT);

        // Accès aux préférences persistées du portlet
        PortalPreferences portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(renderRequest);

        // Récupération de la page courante (pagination)
        int cur = getCurrentPage(renderRequest, portalPreferences);

        // Nombre d’éléments par page
        int delta = ParamUtil.getInteger(renderRequest, "delta", 5);

        // Calcul de l’index de début
        int start = (cur - 1) * delta;

        // Calcul de l’index de fin
        int end = start + delta;

        // Détermination du mode d’affichage (table / cards / list)
        String displayStyle = getDisplayStyle(renderRequest, renderResponse, portalPreferences);

        // Lecture de la colonne de tri
        String orderByCol = ParamUtil.getString(renderRequest, "orderByCol", "title");

        // Lecture du sens de tri
        String orderByType = ParamUtil.getString(renderRequest, "orderByType", "asc");

        // Création du comparateur selon le sens de tri
        Comparator<Assignment> comparator = getComparator(themeDisplay, orderByType);

        // Lecture du filtre catégorie
        long categoryId = getCategoryId(renderRequest);

        // Lecture des mots-clés de recherche
        String keywords = ParamUtil.getString(renderRequest, "keywords");

        // Chargement des données selon le mode actif (search / catégorie / normal)
        Result result = loadAssignments(renderRequest, themeDisplay, groupId, keywords, categoryId, start, end, comparator);

        // Envoi du mode d’affichage à la JSP
        renderRequest.setAttribute("displayStyle", displayStyle);

        // Envoi de la page courante à la JSP
        renderRequest.setAttribute("cur", cur);

        // Envoi de la liste d’assignments à afficher
        renderRequest.setAttribute("entries", result.entries);

        // Envoi du nombre total d’éléments
        renderRequest.setAttribute("entriesCount", result.count);

        // Envoi de la colonne de tri
        renderRequest.setAttribute("orderByCol", orderByCol);

        // Envoi du sens de tri
        renderRequest.setAttribute("orderByType", orderByType);

        // Envoi de la permission d’ajout pour l’IHM
        renderRequest.setAttribute("hasAddAssignmentPermission", hasAddAssignmentPermission);

        // Inclusion de la JSP principale
        include("/view.jsp", renderRequest, renderResponse);
    }

    // Gestion de la pagination avec sauvegarde de l’état dans les préférences
    private int getCurrentPage(RenderRequest request, PortalPreferences preferences) {
        int cur = ParamUtil.getInteger(request, "cur", -1);
        if (cur == -1) return GetterUtil.getInteger(preferences.getValue(GradebookPortletKeys.PORTLET_NAME, "cur"), 1);
        preferences.setValue(GradebookPortletKeys.PORTLET_NAME, "cur", String.valueOf(cur));
        return cur;
    }

    // Gestion du mode d’affichage (table / cards) avec persistance
    private String getDisplayStyle(RenderRequest request, RenderResponse response, PortalPreferences preferences) {
        String namespace = response.getNamespace();
        String displayStyle = ParamUtil.getString(request, namespace + "displayStyle");
        if (Validator.isNull(displayStyle)) displayStyle = ParamUtil.getString(request, "displayStyle");
        boolean hasParams = Validator.isNotNull(displayStyle) || Validator.isNotNull(ParamUtil.getString(request, "orderByCol")) || Validator.isNotNull(ParamUtil.getString(request, "keywords")) || ParamUtil.getInteger(request, "cur", 0) > 0 || ParamUtil.getLong(request, "categoryId") > 0;
        if (Validator.isNotNull(displayStyle)) { preferences.setValue(GradebookPortletKeys.PORTLET_NAME, "displayStyle", displayStyle); return displayStyle; }
        if (!hasParams) { preferences.setValue(GradebookPortletKeys.PORTLET_NAME, "displayStyle", "table"); return "table"; }
        return preferences.getValue(GradebookPortletKeys.PORTLET_NAME, "displayStyle", "table");
    }

    // Création du comparateur de tri sur le titre localisé
    private Comparator<Assignment> getComparator(ThemeDisplay themeDisplay, String orderByType) {
        Comparator<Assignment> comparator = Comparator.comparing(a -> a.getTitle(themeDisplay.getLocale()).toLowerCase());
        return orderByType.equalsIgnoreCase("asc") ? comparator : comparator.reversed();
    }

    // Lecture sécurisée du filtre catégorie
    private long getCategoryId(RenderRequest request) {
        String param = request.getParameter("categoryId");
        if (param == null) return 0;
        return ParamUtil.getLong(request, "categoryId");
    }

    // Méthode centrale de chargement des données
    private Result loadAssignments(RenderRequest request, ThemeDisplay themeDisplay, long groupId, String keywords, long categoryId, int start, int end, Comparator<Assignment> comparator) throws PortletException {
        try {
            if (Validator.isNotNull(keywords)) return loadSearchAssignments(request, themeDisplay, keywords, start, end, comparator);
            if (categoryId > 0) return loadCategoryAssignments(categoryId, start, end, comparator);
            return loadDefaultAssignments(groupId, start, end, comparator);
        } catch (Exception e) { throw new PortletException(e); }
    }

    // Chargement des résultats via l’index de recherche
    private Result loadSearchAssignments(RenderRequest request, ThemeDisplay themeDisplay, String keywords, int start, int end, Comparator<Assignment> comparator) throws SearchException {
        SearchContext searchContext = SearchContextFactory.getInstance(PortalUtil.getHttpServletRequest(request));
        searchContext.setKeywords(keywords);
        searchContext.setCompanyId(themeDisplay.getCompanyId());
        Indexer<Assignment> indexer = IndexerRegistryUtil.getIndexer(Assignment.class);
        Hits hits = indexer.search(searchContext);
        List<Assignment> entries = new ArrayList<>();
        Document[] docs = hits.getDocs();
        int from = Math.min(start, docs.length);
        int to = Math.min(end, docs.length);
        for (int i = from; i < to; i++) { long pk = GetterUtil.getLong(docs[i].get(Field.ENTRY_CLASS_PK)); Assignment a = assignmentLocalService.fetchAssignment(pk); if (a != null) entries.add(a); }
        entries.sort(comparator);
        return new Result(entries, hits.getLength());
    }

    // Chargement filtré par catégorie
    private Result loadCategoryAssignments(long categoryId, int start, int end, Comparator<Assignment> comparator) throws PortalException {
        List<Assignment> all = assignmentLocalService.getAssignmentsByCategory(categoryId);
        all.sort(comparator);
        int from = Math.min(start, all.size());
        int to = Math.min(end, all.size());
        return new Result(all.subList(from, to), all.size());
    }

    // Chargement standard sans filtre
    private Result loadDefaultAssignments(long groupId, int start, int end, Comparator<Assignment> comparator) {
        List<Assignment> entries = assignmentLocalService.getAssignmentsByGroupId(groupId, start, end);
        entries = new ArrayList<>(entries);
        entries.sort(comparator);
        int count = assignmentLocalService.getAssignmentsCountByGroupId(groupId);
        return new Result(entries, count);
    }

    // Classe interne pour transporter la liste et le total
    private static class Result {
        final List<Assignment> entries;
        final int count;
        Result(List<Assignment> entries, int count) { this.entries = entries; this.count = count; }
    }
}

