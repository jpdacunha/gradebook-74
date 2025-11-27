package com.liferay.training.space.gradebook.service.permission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.impl.VirtualLayout;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.permission.PortletPermissionUtil;
import com.liferay.training.space.gradebook.model.Assignment;
import com.liferay.training.space.gradebook.service.AssignmentLocalServiceUtil;

public class AssignmentPermissionChecker {

	public static final String ADD_ASSIGNMENT = "ADD_ASSIGNMENT";

	public static void check(
			PermissionChecker permissionChecker, Assignment entry,
			String actionId)
			throws PortalException {

		if (!contains(permissionChecker, entry, actionId)) {
			throw new PrincipalException.MustHavePermission(
					permissionChecker, Assignment.class.getName(),
					entry.getAssignmentId(), actionId);
		}
	}

	public static void check(
			PermissionChecker permissionChecker, Layout layout, String name,
			String actionId)
			throws PortalException {

		if (!contains(permissionChecker, layout, name, actionId)) {
			throw new PrincipalException.MustHavePermission(
					permissionChecker, Assignment.class.getName(), name,
					actionId);
		}
	}

	public static void check(
			PermissionChecker permissionChecker, long entryId, String actionId)
			throws PortalException {

		if (!contains(permissionChecker, entryId, actionId)) {
			throw new PrincipalException.MustHavePermission(
					permissionChecker, Assignment.class.getName(), entryId,
					actionId);
		}
	}

	public static void check(
			PermissionChecker permissionChecker, long plid, String portletId,
			String actionId)
			throws PortalException {

		if (!contains(permissionChecker, plid, portletId, actionId)) {
			throw new PrincipalException.MustHavePermission(
					permissionChecker, Assignment.class.getName(),
					portletId, actionId);
		}
	}

	public static boolean contains(
			PermissionChecker permissionChecker, Assignment entry,
			String actionId)
			throws PortalException {

		if (permissionChecker.hasOwnerPermission(
				entry.getCompanyId(), Assignment.class.getName(),
				entry.getAssignmentId(), entry.getUserId(), actionId)) {

			return true;
		}

		return permissionChecker.hasPermission(
				entry.getGroupId(), Assignment.class.getName(),
				entry.getAssignmentId(), actionId);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, Layout layout, String portletId,
			String actionId) {

		if (layout instanceof VirtualLayout) {
			VirtualLayout virtualLayout = (VirtualLayout)layout;

			layout = virtualLayout.getSourceLayout();
		}

		String primKey = PortletPermissionUtil.getPrimaryKey(
				layout.getPlid(), portletId);

		return permissionChecker.hasPermission(
				layout.getGroupId(), portletId, primKey, actionId);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, long entryId, String actionId)
			throws PortalException {

		return contains(
				permissionChecker,
				AssignmentLocalServiceUtil.getAssignment(entryId), actionId);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, long plid, String name,
			String actionId) {

		return contains(
				permissionChecker, LayoutLocalServiceUtil.fetchLayout(plid), name,
				actionId);
	}
}

