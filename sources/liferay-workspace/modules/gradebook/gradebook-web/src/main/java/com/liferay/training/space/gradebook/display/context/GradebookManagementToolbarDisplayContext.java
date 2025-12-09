package com.liferay.training.space.gradebook.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItemList;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

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
                .buildPortletURL();
    }

    public String getKeywords() {
        return ParamUtil.getString(_httpServletRequest, "keywords", "");
    }

//    public List<DropdownItem> getActionDropdownItems() {
//        return Collections.emptyList();
//    }

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

        LiferayPortletURL url =
                (LiferayPortletURL) _liferayPortletResponse.createRenderURL();

        url.setParameter("mvcRenderCommandName", "/gradebook/view");
        url.setParameter("displayStyle", getDisplayStyle());

        return url.toString();
    }

    public boolean isShowSearch() {
        return true;
    }

    public int getTotal() {
        return total;
    }

    public String getFilterURL() {
        return _liferayPortletResponse.createRenderURL().toString();
    }

    public List<DropdownItem> getFilterDropdownItems() {

        String currentOrder = ParamUtil.getString(_httpServletRequest, "orderByCol", "");

        String baseURL = PortletURLBuilder.createRenderURL(
                        _liferayPortletResponse
                ).setMVCRenderCommandName("/gradebook/view")
                .buildString();

        return new DropdownItemList() {{
            add(dropdownItem -> {
                dropdownItem.setLabel("Sort by Title");
                dropdownItem.setActive(currentOrder.equals("title"));
                dropdownItem.setHref(_getSearchURL(), "orderByCol", "title");
            });

            add(dropdownItem -> {
                dropdownItem.setLabel("Sort by Description");
                dropdownItem.setActive(currentOrder.equals("description"));
                dropdownItem.setHref(_getSearchURL(), "orderByCol", "description");
            });
        }};
    }

    public String getOrderByType() {
        return ParamUtil.getString(_httpServletRequest, "orderByType", "asc");
    }

    private PortletURL _getSearchURL() {
        PortletURL url = _liferayPortletResponse.createRenderURL();
        url.setParameter("mvcRenderCommandName", "/gradebook/view");
        return url;
    }

    public boolean isShowSort() {
        return true;
    }

    public ViewTypeItemList getViewTypeItems() {

        PortletURL currentPortletURL =
                PortletURLUtil.getCurrent(_liferayPortletRequest, _liferayPortletResponse);
        return new ViewTypeItemList() {
            {
                addCardViewTypeItem(
                        item -> {
                            item.setActive("cards".equals(getDisplayStyle()));
                            item.setHref(currentPortletURL, "displayStyle", "cards");
                        });

                addListViewTypeItem(
                        item -> {
                            item.setActive("list".equals(getDisplayStyle()));
                            item.setHref(currentPortletURL, "displayStyle", "list");
                        });

                addTableViewTypeItem(
                        item -> {
                            item.setActive("table".equals(getDisplayStyle()));
                            item.setHref(currentPortletURL, "displayStyle", "table");
                        });
            }
        };
    }
    private String getDisplayStyle() {
        return ParamUtil.getString(_httpServletRequest, "displayStyle", "table");
    }

    public List<DropdownItem> getActionDropdownItems() {
        return new DropdownItemList() {{
            add(item -> {
                item.setLabel("Delete");
                item.setIcon("trash");
                item.putData("action", "deleteAssignments");
            });
        }};
    }

    public String getComponentId() {
        return "gradebookToolbar";
    }


    public String getOrderByCol() {
        return ParamUtil.getString(_httpServletRequest, "orderByCol", "title");
    }


    public String getSortingURL() {
        return PortletURLBuilder.createRenderURL(_liferayPortletResponse)
                .setMVCRenderCommandName("/gradebook/view")
                .setParameter("orderByCol", getOrderByCol())
                .setParameter(
                        "orderByType",
                        getOrderByType().equals("asc") ? "desc" : "asc"
                )
                .buildString();
    }


}
