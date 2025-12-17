package com.liferay.training.space.gradebook.portlet.command;

import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.space.gradebook.model.Assignment;
import com.liferay.training.space.gradebook.portlet.GradebookPortletKeys;
import com.liferay.training.space.gradebook.portlet.GradebookPortletUtil;
import com.liferay.training.space.gradebook.service.AssignmentLocalService;
import com.liferay.training.space.gradebook.service.AssignmentService;
import com.liferay.training.space.gradebook.validator.AssignmentValidator;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + GradebookPortletKeys.PORTLET_NAME,
		"mvc.command.name=/gradebook/assignment/add"
},
	service = MVCActionCommand.class)
public class AddAssignmentMVCActionCommand extends BaseMVCActionCommand {

				@Override
	protected void doProcessAction(
		ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {



	    Assignment assignment = _assignmentLocalService.createAssignment(0);
		List<String> errors = new ArrayList<String>();

		ServiceContext serviceContext =
			ServiceContextFactory.getInstance(actionRequest);

		GradebookPortletUtil.assembleAssignment(actionRequest, assignment);

		if (AssignmentValidator.isAssignmentValid(assignment,errors)) {
			_assignmentService.addAssignment(assignment, serviceContext);
			
			SessionMessages.add(actionRequest, "assignment-added");

			hideDefaultSuccessMessage(actionRequest);

			sendRedirect(actionRequest, actionResponse);
		}
		else {
			SessionErrors.add(actionRequest, "assignment-error");

			for (String error : errors) {
				SessionErrors.add(actionRequest, error);
			}

			actionResponse.setRenderParameter("mvcRenderCommandName", "/gradebook/assignment/edit");

		}
	}

	@Reference
	protected AssignmentService _assignmentService;
	
	@Reference
	protected AssignmentLocalService _assignmentLocalService;
}