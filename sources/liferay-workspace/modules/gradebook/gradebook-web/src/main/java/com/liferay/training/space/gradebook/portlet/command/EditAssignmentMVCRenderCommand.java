package com.liferay.training.space.gradebook.portlet.command;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.training.space.gradebook.model.Assignment;
import com.liferay.training.space.gradebook.portlet.GradebookPortletKeys;
import com.liferay.training.space.gradebook.service.AssignmentService;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + GradebookPortletKeys.PORTLET_NAME,
		"mvc.command.name=/gradebook/assignment/edit"
}, service = MVCRenderCommand.class)

public class EditAssignmentMVCRenderCommand implements MVCRenderCommand {

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	private AssignmentService assignmentService;

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {



		Assignment assignment = null;

		long assignmentId = ParamUtil.getLong(renderRequest, "assignmentId", 0);
		String title = "Create Assignment";

		if (assignmentId > 0) {
			try {
				assignment = assignmentService.getAssignment(assignmentId);

				title = "Edit Assignment";
			}
			catch (PortalException e) {
				e.printStackTrace();
			}
		}

		renderRequest.setAttribute("assignment", assignment);
		renderRequest.setAttribute("title", title);

		return "/assignment/edit_assignment.jsp";
	}


}
