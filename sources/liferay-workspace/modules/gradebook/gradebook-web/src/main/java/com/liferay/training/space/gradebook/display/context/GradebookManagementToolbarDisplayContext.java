package com.liferay.training.space.gradebook.display.context;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetVocabularyLocalServiceUtil;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItemList;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.space.gradebook.model.Assignment;

import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GradebookManagementToolbarDisplayContext {

    private final HttpServletRequest _httpServletRequest;
    private final LiferayPortletRequest _liferayPortletRequest;
    private final LiferayPortletResponse _liferayPortletResponse;
    private final int total;


    public GradebookManagementToolbarDisplayContext(
            HttpServletRequest httpServletRequest,
            LiferayPortletRequest liferayPortletRequest,
            LiferayPortletResponse liferayPortletResponse,
            int totalResults
    ) {

        _httpServletRequest = httpServletRequest;
        _liferayPortletRequest = liferayPortletRequest;
        _liferayPortletResponse = liferayPortletResponse;
        this.total = totalResults;

    }

    /**
     * Search input URL
     */
    public PortletURL getSearchActionURL() {
        return PortletURLBuilder
                .createRenderURL(_liferayPortletResponse)
                .setMVCRenderCommandName("/gradebook/view")
                .setParameter("orderByCol", getOrderByCol())
                .setParameter("orderByType", getOrderByType())
                .setParameter("cur", getCurrentPage())
                .setParameter("displayStyle", getDisplayStyle())
                // 🔥 garder la page
                .buildPortletURL();
    }

    public String getKeywords() {
        return ParamUtil.getString(_httpServletRequest, "keywords", "");
    }


    public String getResultsSummary() {
        String keywords = getKeywords();

        if (keywords == null || keywords.isEmpty()) {
            return "";
        }

        if (total == 1) {
            return "1 result for \"" + keywords + "\"";
        }

        return total + " results for \"" + keywords + "\"";
    }

    public String getClearResultsURL() {

        LiferayPortletURL url = (LiferayPortletURL) _liferayPortletResponse.createRenderURL();
        url.setParameter("mvcRenderCommandName", "/gradebook/view");
        url.setParameter("displayStyle", getDisplayStyle());
        url.setParameter("cur", String.valueOf(getCurrentPage()));
        return url.toString();
    }

    public boolean isShowSearch() {
        return true;
    }

    public int getTotal() {
        return total;
    }

    public String getFilterURL() {
        LiferayPortletURL url = (LiferayPortletURL) _liferayPortletResponse.createRenderURL();
        url.setParameter("mvcRenderCommandName", "/gradebook/view");
        url.setParameter("cur", ParamUtil.getString(_httpServletRequest, "cur", "1")); // 🔥 IMPORTANT
        return url.toString();
    }

    private int getCurrentPage() {
        int cur = ParamUtil.getInteger(_liferayPortletRequest, "cur", 1);
        return cur;
    }


    public List<DropdownItem> getFilterDropdownItems() {
        // 🔥 Récupérer la locale et themeDisplay si nécessaire
        ThemeDisplay themeDisplay = (ThemeDisplay) _httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY);

        // 🔥 Récupérer l'utilisateur connecté
        long userId = themeDisplay.getUserId();

        // 🔥 Lire le paramètre "mine"
        String mineParam = ParamUtil.getString(_httpServletRequest, "mine");

        // 🔥 mineSelected = est-ce que le filtre "Mine" est actif ?
        boolean mineSelected = mineParam.equals("true");
        long selectedCategoryId = ParamUtil.getLong(_httpServletRequest, "categoryId");
        String currentOrder = ParamUtil.getString(_httpServletRequest, "orderByCol", "");


        return DropdownItemListBuilder
                // -------------------------------------------------------------
                // GROUP 1 — FILTER BY NAVIGATION
                // -------------------------------------------------------------
                .addGroup(
                        group -> {
                            group.setLabel("FILTER BY NAVIGATION");

                            group.setDropdownItems(
                                    DropdownItemListBuilder.add(
                                            dropdownItem -> {
                                                dropdownItem.setLabel("All");
                                                dropdownItem.setActive(selectedCategoryId == 0);
                                                dropdownItem.setHref(
                                                        _getSearchURL(),
                                                        "categoryId", ""
                                                );
                                            }
                                    ).build()
                            );
                        }
                )

                // -------------------------------------------------------------
                // GROUP 2 — ORDER BY
                // -------------------------------------------------------------
                .addGroup(
                        group -> {
                            group.setLabel("ORDER BY");

                            group.setDropdownItems(
                                    DropdownItemListBuilder.add(
                                            dropdownItem -> {
                                                dropdownItem.setLabel("Title");
                                                dropdownItem.setActive(currentOrder.equals("title"));
                                                dropdownItem.setHref(
                                                        _getSearchURL(),
                                                        "orderByCol",
                                                        "title",
                                                        "categoryId",
                                                        ""
                                                );
                                            }
                                    ).add(
                                            dropdownItem -> {
                                                dropdownItem.setLabel("Display Date");
                                                dropdownItem.setActive(currentOrder.equals("displayDate"));
                                                dropdownItem.setHref(
                                                        _getSearchURL(),
                                                        "orderByCol", "displayDate",
                                                        "categoryId", ""
                                                );
                                            }
                                    ).build()
                            );
                        }
                )

                // -------------------------------------------------------------
                // GROUP 3 — FILTER BY CATEGORY
                // -------------------------------------------------------------
                .addGroup(
                        group -> {
                            group.setLabel("FILTER BY CATEGORY");

                            group.setDropdownItems(_getCategoryDropdownItems(selectedCategoryId));
                        }
                )

                .build();
    }


    private List<DropdownItem> _getCategoryDropdownItems(long selectedCategoryId) {
        ThemeDisplay themeDisplay =
                (ThemeDisplay) _httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY);


        Locale locale = themeDisplay.getLocale();
        List<DropdownItem> items = new ArrayList<>();

        for (AssetVocabulary vocabulary : _getGradebookVocabularies()) {

            List<AssetCategory> categories =
                    AssetCategoryLocalServiceUtil.getVocabularyCategories(
                            vocabulary.getVocabularyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

            for (AssetCategory category : categories) {

                items.add(DropdownItemListBuilder.add(
                        dropdownItem -> {
                            dropdownItem.setLabel(category.getTitle(locale));
                            dropdownItem.setActive(category.getCategoryId() == selectedCategoryId);
                            dropdownItem.setHref(
                                    _getSearchURL(),
                                    "categoryId", category.getCategoryId()
                            );
                        }
                ).build().get(0));
            }
        }

        return items;
    }

    private List<AssetVocabulary> _getGradebookVocabularies() {
        try {
            ThemeDisplay themeDisplay = (ThemeDisplay)_httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY);

            List<AssetVocabulary> vocabularies =
                    AssetVocabularyLocalServiceUtil.getGroupVocabularies(themeDisplay.getScopeGroupId());

            long assignmentClassNameId = PortalUtil.getClassNameId(Assignment.class);

            return vocabularies.stream()
                    .filter(v -> {
                        long[] classNameIds = v.getSelectedClassNameIds();

                        // ❗ Si le vocabulaire n'est lié à aucun asset → ignoré
                        if (classNameIds == null || classNameIds.length == 0) {
                            return false;
                        }

                        // ❗ On garde UNIQUEMENT les vocabulaires contenant Assignment
                        for (long id : classNameIds) {
                            if (id == assignmentClassNameId) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            return List.of();
        }
    }



    public String getOrderByType() {
        return ParamUtil.getString(_httpServletRequest, "orderByType", "asc");
    }

    private PortletURL _getSearchURL() {
        PortletURL url = _liferayPortletResponse.createRenderURL();
        url.setParameter("mvcRenderCommandName", "/gradebook/view");
        url.setParameter("cur", ParamUtil.getString(_httpServletRequest, "cur", "1"));
        return url;
    }

    public boolean isShowSort() {
        return true;
    }

    public ViewTypeItemList getViewTypeItems() {

        PortletURL currentURL = PortletURLUtil.getCurrent(
                _liferayPortletRequest, _liferayPortletResponse);

        return new ViewTypeItemList() {
            {
                addCardViewTypeItem(item -> {
                    item.setActive("cards".equals(getDisplayStyle()));
                    item.setHref(currentURL, "displayStyle", "cards");
                });

                addListViewTypeItem(item -> {
                    item.setActive("list".equals(getDisplayStyle()));
                    item.setHref(currentURL, "displayStyle", "list");
                });

                addTableViewTypeItem(item -> {
                    item.setActive("table".equals(getDisplayStyle()));
                    item.setHref(currentURL, "displayStyle", "table");
                });
            }
        };
    }

    private String getDisplayStyle() {
        String displayStyle = ParamUtil.getString(_liferayPortletRequest, "displayStyle", "table");
        return displayStyle;
    }

    public List<DropdownItem> getActionDropdownItems() {

        return new DropdownItemList() {{
            add(dropdownItem -> {
                dropdownItem.setLabel("Delete");
                dropdownItem.setIcon("trash");
                dropdownItem.setQuickAction(true);
                dropdownItem.setHref(
                        "javascript:" + _liferayPortletResponse.getNamespace() + "submitDelete();"
                );
            });
        }};
    }

    public String getComponentId() {
        return "gradebookToolbar";
    }


    public String getOrderByCol() {
        return ParamUtil.getString(_httpServletRequest, "orderByCol", "title");
    }


}
