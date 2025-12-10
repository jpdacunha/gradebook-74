package com.liferay.training.space.gradebook.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItemList;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.util.ParamUtil;
import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;
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

        String currentOrder = ParamUtil.getString(_httpServletRequest, "orderByCol", "");


        return new DropdownItemList() {{
            add(dropdownItem -> {
                dropdownItem.setLabel("Sort by Title");
                dropdownItem.setActive(currentOrder.equals("title"));
                dropdownItem.setHref(_getSearchURL(), "orderByCol",
                        "title",
                        "cur", getCurrentPage(),
                        "keywords", getKeywords(),
                        "displayStyle", getDisplayStyle()

// 🔥 garder la pagination !
                );
            });

            add(dropdownItem -> {
                dropdownItem.setLabel("Sort by Description");
                dropdownItem.setActive(currentOrder.equals("description"));
                dropdownItem.setHref(_getSearchURL(), "orderByCol",
                        "description",
                        "cur", getCurrentPage(),
                        "keywords", getKeywords(),
                        "displayStyle", getDisplayStyle()
                );
            });
        }};
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


}
