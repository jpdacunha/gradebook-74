package com.liferay.training.space.gradebook.display.context;

import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;

import javax.servlet.http.HttpServletRequest;

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

    public String getSearchActionURL() {
        return PortalUtil.getCurrentURL(_httpServletRequest);
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
        return PortalUtil.getCurrentURL(_httpServletRequest).replaceAll("keywords=[^&]*", "");
    }

    public boolean isShowSearch() {
        return true;
    }
    public int getTotal() {
        return total;
    }
}
