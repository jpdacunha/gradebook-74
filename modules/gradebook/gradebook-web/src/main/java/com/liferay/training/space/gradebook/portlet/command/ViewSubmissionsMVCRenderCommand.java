package com.liferay.training.space.gradebook.portlet.command;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.training.space.gradebook.model.Assignment;
import com.liferay.training.space.gradebook.model.Submission;
import com.liferay.training.space.gradebook.portlet.GradebookPortletKeys;
import com.liferay.training.space.gradebook.service.AssignmentService;
import com.liferay.training.space.gradebook.service.SubmissionLocalService;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + GradebookPortletKeys.PORTLET_NAME,
		"mvc.command.name=/gradebook/submissions/view"
},
	service = MVCRenderCommand.class)
public class ViewSubmissionsMVCRenderCommand implements MVCRenderCommand {

		@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {

		long assignmentId = ParamUtil.getLong(renderRequest, "assignmentId", 0);

		Assignment assignment = null;
		DateFormat dateFormat = DateFormatFactoryUtil.getSimpleDateFormat("EEEEE, MMMMM dd, yyyy",
				renderRequest.getLocale());
		List<Submission> submissions = null;
		Map<Long, User> studentMap = null;

		try {
			assignment = _assignmentService.getAssignment(assignmentId);
			submissions = _submissionLocalService.getSubmissionsByAssignment(PortalUtil.getScopeGroupId(renderRequest),
					assignmentId);

			studentMap = doGetSubmissionsUsers(submissions);

		} catch (PortalException e) {
			e.printStackTrace();
		}

		renderRequest.setAttribute("assignment", assignment);
		renderRequest.setAttribute("Submissions", submissions);
		renderRequest.setAttribute("students", studentMap);

		renderRequest.setAttribute("dueDate", dateFormat.format(assignment.getDueDate()));

		renderRequest.setAttribute("title", "view-submissions-title");

		return "/submission/view_submissions.jsp";
	}

	private Map<Long, User> doGetSubmissionsUsers(List<Submission> submissions) throws PortalException {
		Map<Long, User> users = new HashMap<Long, User>();
		User student = null;

		for (Submission submission : submissions) {
			student = _userLocalService.getUser(submission.getStudentId());

			users.put(submission.getSubmissionId(), student);

		}

		return users;
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	private AssignmentService _assignmentService;
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	private SubmissionLocalService _submissionLocalService;
	
		@Reference(cardinality = ReferenceCardinality.MANDATORY)
	private UserLocalService _userLocalService;
}
