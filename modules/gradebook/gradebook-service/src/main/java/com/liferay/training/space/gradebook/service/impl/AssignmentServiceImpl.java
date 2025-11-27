/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.training.space.gradebook.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.training.space.gradebook.model.Assignment;
import com.liferay.training.space.gradebook.service.base.AssignmentServiceBaseImpl;
import com.liferay.training.space.gradebook.service.permission.AssignmentPermissionChecker;

import java.util.List;

import aQute.bnd.annotation.ProviderType;

/**
 * The implementation of the assignment remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.training.space.gradebook.service.AssignmentService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssignmentServiceBaseImpl
 * @see com.liferay.training.space.gradebook.service.AssignmentServiceUtil
 */
@ProviderType
public class AssignmentServiceImpl extends AssignmentServiceBaseImpl {

	public Assignment getAssignment(long assignmentId) throws PortalException {
		Assignment assignment = assignmentLocalService.getAssignment(assignmentId);
		
		AssignmentPermissionChecker.check(getPermissionChecker(), assignment.getAssignmentId(), ActionKeys.VIEW);
	
		return assignment;
	}
	
	public Assignment deleteAssignment(Assignment assignment) throws PortalException {
		AssignmentPermissionChecker.check(
				getPermissionChecker(),
				assignment.getAssignmentId(), ActionKeys.DELETE);
		
		return assignmentLocalService.deleteAssignment(assignment);
	}
	
	public Assignment deleteAssignment(long assignmentId) throws PortalException {		
		Assignment assignment = assignmentLocalService.getAssignment(assignmentId);
		
		return assignmentLocalService.deleteAssignment(assignment);
	}	
	
	
	public Assignment addAssignment(
			Assignment assignment, ServiceContext serviceContext)
				throws PortalException {
		
		//PortalPermissionUtil.contains(getPermissionChecker(), AssignmentPermissionChecker.ADD_ASSIGNMENT);
		
		return assignmentLocalService.addAssignment(assignment, serviceContext);
	}
	
	public Assignment updateAssignment(
		Assignment assignment)
			throws PortalException {

		AssignmentPermissionChecker.check(
				getPermissionChecker(),
				assignment.getAssignmentId(), ActionKeys.UPDATE);
		
		return assignmentLocalService.updateAssignment(assignment);
	}
	
		public List<Assignment> getAssignmentsByGroupId(long groupId) {

		return assignmentPersistence.filterFindByGroupId(groupId);
	}

	public List<Assignment> getAssignmentsByGroupId(
		long groupId, int start, int end) {

		return assignmentPersistence.filterFindByGroupId(groupId, start, end);
	}

	public int getAssignmentsCountByGroupId(long groupId) {

		return assignmentPersistence.filterCountByGroupId(groupId);
	}
}