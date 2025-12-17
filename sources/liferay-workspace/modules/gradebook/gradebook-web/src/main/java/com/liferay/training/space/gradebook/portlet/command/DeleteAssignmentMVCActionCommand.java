package com.liferay.training.space.gradebook.portlet.command;

import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.training.space.gradebook.portlet.GradebookPortletKeys;
import com.liferay.training.space.gradebook.service.AssignmentService;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + GradebookPortletKeys.PORTLET_NAME,
		"mvc.command.name=/gradebook/assignment/delete"
},
	service = MVCActionCommand.class)
public class DeleteAssignmentMVCActionCommand extends BaseMVCActionCommand {

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	protected AssignmentService assignmentService;

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {


		String[] rowIds = ParamUtil.getParameterValues(
				actionRequest, "rowIds"
		);

		if (rowIds == null || rowIds.length == 0) {
			return;
		}

		for (String rowId : rowIds) {
			long assignmentId = Long.parseLong(rowId);
			assignmentService.deleteAssignment(assignmentId);
		}

		SessionMessages.add(actionRequest, "assignments-deleted");

	}


}
