package com.liferay.training.space.gradebook.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.space.gradebook.service.AssignmentLocalService;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.training.space.gradebook.service.permission.AssignmentPermissionChecker;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.add-default-resource=true",
		"com.liferay.portlet.display-category=category.hidden",
		"com.liferay.portlet.preferences-owned-by-group=true",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.render-weight=50",
		"com.liferay.portlet.scopeable=true",
		"com.liferay.portlet.struts-path=gradebook",
		"javax.portlet.display-name=Gradebook",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.always-display-default-configuration-icons=true",
		 "javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.init-param.portlet-title-based-navigation=false",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.name=" + GradebookPortletKeys.PORTLET_NAME,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user",
		"javax.portlet.supported-public-render-parameter=categoryId",
		"javax.portlet.supported-public-render-parameter=resetCur",
		"javax.portlet.supported-public-render-parameter=tag",
		"javax.portlet.version=3.0"
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

		boolean hasAddAssignmentPermission = AssignmentPermissionChecker.containsTopLevel(themeDisplay.getPermissionChecker(), groupId, AssignmentPermissionChecker.ADD_ASSIGNMENT);

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



