package com.liferay.training.space.gradebook.portlet;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.space.gradebook.model.Assignment;

public class GradebookPortletUtilV0 {

    public static void assembleAssignment(
            ActionRequest request, Assignment assignment)
            throws PortalException {

        ThemeDisplay themeDisplay =
                (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

        int dueDateMonth = ParamUtil.getInteger(request, "dueDateMonth");
        int dueDateDay = ParamUtil.getInteger(request, "dueDateDay");
        int dueDateYear = ParamUtil.getInteger(request, "dueDateYear");
        int dueDateHour = ParamUtil.getInteger(request, "dueDateHour");
        int dueDateMinute = ParamUtil.getInteger(request, "dueDateMinute");
        int dueDateAmPm = ParamUtil.getInteger(request, "dueDateAmPm");

        if (dueDateAmPm == Calendar.PM) {
            dueDateHour += 12;
        }

        Date dueDate = PortalUtil.getDate(
                dueDateMonth, dueDateDay, dueDateYear, dueDateHour, dueDateMinute,
                themeDisplay.getTimeZone(), PortalException.class);

        Map<Locale, String> title =
                LocalizationUtil.getLocalizationMap(request, "title");

        String description = ParamUtil.getString(request, "description");

        assignment.setCompanyId(themeDisplay.getCompanyId());
        assignment.setGroupId(themeDisplay.getScopeGroupId());
        assignment.setUserId(themeDisplay.getUserId());
        assignment.setCreateDate(DateUtil.newDate());
        assignment.setModifiedDate(DateUtil.newDate());

        assignment.setTitleMap(title);
        assignment.setDescription(description);
        assignment.setDueDate(dueDate);
    }
}
