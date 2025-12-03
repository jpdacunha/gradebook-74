package com.liferay.training.space.gradebook.validator;

import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.training.space.gradebook.model.Assignment;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AssignmentValidator {

		public static boolean isAssignmentValid(
			Assignment assignment, List<String> errors) {

		boolean result = true;

		if (!assignment.isNew()) {
			if (Validator.isNull(assignment.getAssignmentId())) {
				errors.add("assignment-id-error");
				result = false;
			}
		}
		if ((Validator.isBlank(assignment.getTitle()))) {
			errors.add("assignment-title-error");
			result = false;
		}

		if (Validator.isNull(assignment.getDueDate())) {
			errors.add("assignment-date-error");
			result = false;
		}
		if (Validator.isBlank(HtmlUtil.stripHtml(assignment.getDescription()))) {
			errors.add("assignment-description-error");
			result = false;
		}

		return result;
	}

	/**
	 * isAssignmentValid: Determines if the given fields are valid for an assignment.
	 * @param titleMap
	 * @param description
	 * @param dueDate
	 * @param errors
	 * @return boolean <code>true</code> if the fields are valid, otherwise they are not.
	 */
	public static boolean isAssignmentValid(
		final Map<Locale, String> titleMap, final String description,
		final Date dueDate, final List<String> errors) {

		boolean result = true;

		result &= isTitleValid(titleMap, errors);
		result &= isDueDateValid(dueDate, errors);
		result &= isDescriptionValid(description, errors);

		return result;
	}

	/**
	 * isDescriptionValid: Determines if the description is valid and, if not,
	 * adds the error message to the errors list.
	 * @param description
	 * @param errors
	 * @return boolean <code>true</code> if the description is valid, otherwise it is not.
	 */
	public static boolean isDescriptionValid(
		final String description, final List<String> errors) {

		boolean result = true;

		if (Validator.isBlank(HtmlUtil.stripHtml(description))) {
			errors.add("assignment-description-error");
			result = false;
		}

		return result;
	}

	/**
	 * isDueDateValid: Determines if the due date is valid and, if not, adds the
	 * error message to the errors list.
	 * @param dueDate
	 * @param errors
	 * @return boolean <code>true</code> if the due date is valid, otherwise it is not.
	 */
	public static boolean isDueDateValid(
		final Date dueDate, final List<String> errors) {

		boolean result = true;

		if (Validator.isNull(dueDate)) {
			errors.add("assignment-date-error");
			result = false;
		}

		return result;
	}

	/**
	 * isTitleValid: Determines if the title map is valid and, if not, adds the
	 * error message key to the errors list.
	 * @param titleMap
	 * @param errors
	 * @return boolean <code>true</code> if the title map is valid, otherwise it is not.
	 */
	public static boolean isTitleValid(
		final Map<Locale, String> titleMap, final List<String> errors) {

		boolean result = true;

		// verify the map has something

		if (MapUtil.isEmpty(titleMap)) {
			errors.add("assignment-title-error");
			result = false;
		} else {

			// get the default locale

			Locale defaultLocale = LocaleUtil.getSiteDefault();

			result = isTitleValid(titleMap.get(defaultLocale), errors);
		}

		// get the default title string

		return result;
	}

	/**
	 * isTitleValid: Verifies the title string is valid and, if not, adds the
	 * error message key to the errors list.
	 * @param title
	 * @param errors
	 * @return boolean <code>true</code> if the title is valid, otherwise it is not.
	 */
	public static boolean isTitleValid(
		final String title, final List<String> errors) {

		boolean result = true;

		if ((Validator.isBlank(title))) {
			errors.add("assignment-title-error");
			result = false;
		}

		return result;
	}
}