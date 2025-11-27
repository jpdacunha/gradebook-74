/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.training.space.gradebook.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link AssignmentService}.
 *
 * @author Brian Wing Shun Chan
 * @see AssignmentService
 * @generated
 */
public class AssignmentServiceWrapper
	implements AssignmentService, ServiceWrapper<AssignmentService> {

	public AssignmentServiceWrapper() {
		this(null);
	}

	public AssignmentServiceWrapper(AssignmentService assignmentService) {
		_assignmentService = assignmentService;
	}

	@Override
	public com.liferay.training.space.gradebook.model.Assignment addAssignment(
			com.liferay.training.space.gradebook.model.Assignment assignment,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _assignmentService.addAssignment(assignment, serviceContext);
	}

	@Override
	public com.liferay.training.space.gradebook.model.Assignment
			deleteAssignment(
				com.liferay.training.space.gradebook.model.Assignment
					assignment)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _assignmentService.deleteAssignment(assignment);
	}

	@Override
	public com.liferay.training.space.gradebook.model.Assignment
			deleteAssignment(long assignmentId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _assignmentService.deleteAssignment(assignmentId);
	}

	@Override
	public com.liferay.training.space.gradebook.model.Assignment getAssignment(
			long assignmentId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _assignmentService.getAssignment(assignmentId);
	}

	@Override
	public java.util.List<com.liferay.training.space.gradebook.model.Assignment>
		getAssignmentsByGroupId(long groupId) {

		return _assignmentService.getAssignmentsByGroupId(groupId);
	}

	@Override
	public java.util.List<com.liferay.training.space.gradebook.model.Assignment>
		getAssignmentsByGroupId(long groupId, int start, int end) {

		return _assignmentService.getAssignmentsByGroupId(groupId, start, end);
	}

	@Override
	public int getAssignmentsCountByGroupId(long groupId) {
		return _assignmentService.getAssignmentsCountByGroupId(groupId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _assignmentService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.training.space.gradebook.model.Assignment
			updateAssignment(
				com.liferay.training.space.gradebook.model.Assignment
					assignment)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _assignmentService.updateAssignment(assignment);
	}

	@Override
	public AssignmentService getWrappedService() {
		return _assignmentService;
	}

	@Override
	public void setWrappedService(AssignmentService assignmentService) {
		_assignmentService = assignmentService;
	}

	private AssignmentService _assignmentService;

}