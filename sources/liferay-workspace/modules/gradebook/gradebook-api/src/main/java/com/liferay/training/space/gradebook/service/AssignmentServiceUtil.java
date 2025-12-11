/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.training.space.gradebook.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.training.space.gradebook.model.Assignment;

import java.util.List;

/**
 * Provides the remote service utility for Assignment. This utility wraps
 * <code>com.liferay.training.space.gradebook.service.impl.AssignmentServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see AssignmentService
 * @generated
 */
public class AssignmentServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.training.space.gradebook.service.impl.AssignmentServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static Assignment addAssignment(
			Assignment assignment,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addAssignment(assignment, serviceContext);
	}

	public static Assignment deleteAssignment(Assignment assignment)
		throws PortalException {

		return getService().deleteAssignment(assignment);
	}

	public static Assignment deleteAssignment(long assignmentId)
		throws PortalException {

		return getService().deleteAssignment(assignmentId);
	}

	public static Assignment getAssignment(long assignmentId)
		throws PortalException {

		return getService().getAssignment(assignmentId);
	}

	public static List<Assignment> getAssignmentsByGroupId(long groupId) {
		return getService().getAssignmentsByGroupId(groupId);
	}

	public static List<Assignment> getAssignmentsByGroupId(
		long groupId, int start, int end) {

		return getService().getAssignmentsByGroupId(groupId, start, end);
	}

	public static int getAssignmentsCountByGroupId(long groupId) {
		return getService().getAssignmentsCountByGroupId(groupId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static Assignment updateAssignment(
			Assignment assignment,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().updateAssignment(assignment, serviceContext);
	}

	public static AssignmentService getService() {
		return _service;
	}

	public static void setService(AssignmentService service) {
		_service = service;
	}

	private static volatile AssignmentService _service;

}