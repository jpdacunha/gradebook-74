package com.liferay.training.space.gradebook.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.space.gradebook.service.AssignmentLocalService;
import com.liferay.training.space.gradebook.service.permission.AssignmentPermissionChecker;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=gradebook-web Portlet",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.init-param.add-process-action-success-action=false",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user",
		"com.liferay.portlet.add-default-resource=true",
	},
	service = Portlet.class
)
public class GradebookPortlet extends MVCPortlet {
	
		@Override
	public void doView(
		RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long groupId = themeDisplay.getScopeGroupId();

		//boolean hasAddAssignmentPermission =
			/*PortalPermissionUtil.contains(
				themeDisplay.getPermissionChecker(),
				AssignmentPermissionChecker.ADD_ASSIGNMENT);*/

			boolean hasAddAssignmentPermission = true;

		renderRequest.setAttribute(
			"Assignments",
			_assignmentLocalService.getAssignmentsByGroupId(groupId));
		renderRequest.setAttribute(
			"AssignmentsCount",
			_assignmentLocalService.getAssignmentsCountByGroupId(groupId));
		renderRequest.setAttribute(
			"hasAddAssignmentPermission", hasAddAssignmentPermission);

		super.doView(renderRequest, renderResponse);
	}

	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected AssignmentLocalService _assignmentLocalService;
}



