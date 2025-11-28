package com.liferay.training.space.gradebook.asset;
import com.liferay.asset.kernel.model.BaseJSPAssetRenderer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.*;
import com.liferay.training.space.gradebook.model.Assignment;
import com.liferay.training.space.gradebook.portlet.GradebookPortletKeys;
import com.liferay.training.space.gradebook.service.permission.AssignmentPermissionChecker;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AssignmentAssetRenderer extends BaseJSPAssetRenderer<Assignment> {

	public AssignmentAssetRenderer(Assignment assignment) {
		_assignment = assignment;
	}
	@Override
	public boolean hasEditPermission(PermissionChecker permissionChecker)
		throws PortalException {
		return true;
		// FIXME appel permission checker
		/*return AssignmentPermissionChecker.contains(
			permissionChecker,
			_assignment.getAssignmentId(), ActionKeys.UPDATE);*/
	}
	@Override
	public boolean hasViewPermission(PermissionChecker permissionChecker)
		throws PortalException {
		return  true;
		/*return AssignmentPermissionChecker.contains(
			permissionChecker,
			_assignment.getAssignmentId(), ActionKeys.VIEW);*/
	}
	@Override
	public Assignment getAssetObject() {
		return _assignment;
	}
	@Override
	public long getGroupId() {
		return _assignment.getGroupId();
	}
	@Override
	public long getUserId() {
		return _assignment.getUserId();
	}
	@Override
	public String getUserName() {
		return _assignment.getUserName();
	}
	@Override
	public String getUuid() {
		return _assignment.getUuid();
	}
	@Override
	public String getClassName() {
		return Assignment.class.getName();
	}
	@Override
	public long getClassPK() {
		return _assignment.getAssignmentId();
	}
	@Override
	public String getSummary(
		PortletRequest portletRequest, PortletResponse portletResponse) {
		return _assignment.getDescription();
	}
	@Override
	public String getTitle(Locale locale) {
		return _assignment.getTitle(locale);
	}
	@Override
	public boolean include(
			HttpServletRequest request, HttpServletResponse response,
			String template)
		throws Exception {
		request.setAttribute("ASSIGNMENT", _assignment);
		//request.setAttribute("HtmlUtil", HtmlUtil.getHtml());
		request.setAttribute("StringUtil", new StringUtil());
		request.setAttribute(
			"ABSTRACTLENGTH", GetterUtil.getInteger(
					request.getAttribute(WebKeys.ASSET_ENTRY_ABSTRACT_LENGTH), 200));
		
		return super.include(request, response, template);
	}
	@Override
	public String getJspPath(HttpServletRequest request, String template) {
		return "/asset/" + template + ".jsp";
	}
	
	@Override
	public PortletURL getURLEdit(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {
		PortletURL portletURL = liferayPortletResponse.createLiferayPortletURL(
			getControlPanelPlid(liferayPortletRequest),
			GradebookPortletKeys.PORTLET_NAME, PortletRequest.RENDER_PHASE);
		portletURL.setParameter("mvcRenderCommandName", "/gradebook/assignment/edit");
		portletURL.setParameter(
			"assignmentId", String.valueOf(_assignment.getAssignmentId()));
		portletURL.setParameter("showback", Boolean.FALSE.toString());
		
		return portletURL;
	}	
	@Override
	public String getURLViewInContext(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse, 
			String noSuchEntryRedirect)
		throws Exception {
		try {
			long plid = PortalUtil.getPlidFromPortletId(
				_assignment.getGroupId(), 
				GradebookPortletKeys.PORTLET_NAME);
			
			PortletURL portletURL;
			if (plid == LayoutConstants.DEFAULT_PLID) {
				portletURL = liferayPortletResponse.createLiferayPortletURL(
					getControlPanelPlid(liferayPortletRequest),
					GradebookPortletKeys.PORTLET_NAME, 
					PortletRequest.RENDER_PHASE);
			}
			else {
				portletURL = PortletURLFactoryUtil.create(
					liferayPortletRequest, GradebookPortletKeys.PORTLET_NAME, 
					plid, PortletRequest.RENDER_PHASE);
			}
			
			portletURL.setParameter(
					"mvcRenderCommandName", "/gradebook/submissions/view");
			portletURL.setParameter(
				"assignmentId", String.valueOf(_assignment.getAssignmentId()));
			
			String currentUrl = PortalUtil.getCurrentURL(liferayPortletRequest);
			
			portletURL.setParameter("redirect", currentUrl);
			
			return portletURL.toString();
		
		}
		catch (PortalException e) {
			
		} 
		catch (SystemException e) {
		} 
		
		return noSuchEntryRedirect;
	}
	
	@Override
	public String getURLView(
			LiferayPortletResponse liferayPortletResponse, 
			WindowState windowState) 
		throws Exception {
		
		return super.getURLView(liferayPortletResponse, windowState);
	}
		
	private Assignment _assignment;
}
