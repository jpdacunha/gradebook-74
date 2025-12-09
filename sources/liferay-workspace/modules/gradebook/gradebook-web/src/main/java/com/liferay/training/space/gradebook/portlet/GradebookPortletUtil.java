package com.liferay.training.space.gradebook.portlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.space.gradebook.model.Assignment;

public class GradebookPortletUtil {

	/**
	 * Convertit une date venant d'un <aui:input type="date">
	 * formatée en yyyy-MM-dd
	 */
	public static Date parseDueDate(String dueDateString) {
		if (dueDateString == null || dueDateString.isEmpty()) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.parse(dueDateString);
		}
		catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Assemble l'objet Assignment à partir des paramètres du formulaire
	 */
	public static void assembleAssignment(
			ActionRequest request, Assignment assignment)
			throws PortalException {

		ThemeDisplay themeDisplay =
				(ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

		// Champs du formulaire
		Map<Locale, String> title =
				LocalizationUtil.getLocalizationMap(request, "title");

		String description = ParamUtil.getString(request, "description");
		String dueDateString = ParamUtil.getString(request, "dueDate");
		Date dueDate = parseDueDate(dueDateString);

		// Remplissage de l'objet
		assignment.setCompanyId(themeDisplay.getCompanyId());
		assignment.setGroupId(themeDisplay.getScopeGroupId());
		assignment.setUserId(themeDisplay.getUserId());

		assignment.setTitleMap(title);
		assignment.setDescription(description);
		assignment.setDueDate(dueDate);
	}
}
