package com.liferay.training.space.gradebook.portlet.command;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.training.space.gradebook.model.Assignment;
import com.liferay.training.space.gradebook.portlet.GradebookPortletKeys;
import com.liferay.training.space.gradebook.portlet.GradebookPortletUtil;
import com.liferay.training.space.gradebook.service.AssignmentService;
import com.liferay.training.space.gradebook.validator.AssignmentValidator;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + GradebookPortletKeys.PORTLET_NAME,
		"mvc.command.name=/gradebook/assignment/edit"
},
	service = MVCActionCommand.class)
public class EditAssignmentMVCActionCommand extends BaseMVCActionCommand {

			@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse) 
		throws Exception {

		long assignmentId = ParamUtil.getLong(actionRequest, "assignmentId");

		Assignment assignment = _assignmentService.getAssignment(assignmentId);

		GradebookPortletUtil.assembleAssignment(actionRequest, assignment);

		List<String> errors = new ArrayList<>();
		
		if (AssignmentValidator.isAssignmentValid(assignment, errors)) {
			// 🔥 Récupération du ServiceContext contenant les catégories
			ServiceContext serviceContext = ServiceContextFactory.getInstance(
					Assignment.class.getName(), actionRequest);
			_assignmentService.updateAssignment(assignment, serviceContext);

			SessionMessages.add(actionRequest, "assignment-updated");

			sendRedirect(actionRequest, actionResponse);
		} else {
			throw new Exception("Invalid form data");
		}
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	protected AssignmentService _assignmentService;
}
