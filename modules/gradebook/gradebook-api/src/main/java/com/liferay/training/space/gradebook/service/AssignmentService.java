/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.training.space.gradebook.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.security.access.control.AccessControlled;
import com.liferay.portal.kernel.service.BaseService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.training.space.gradebook.model.Assignment;

import java.util.List;

/**
 * Provides the remote service interface for Assignment. Methods of this
 * service are expected to have security checks based on the propagated JAAS
 * credentials because this service can be accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see AssignmentServiceUtil
 * @generated
 */
@AccessControlled
@JSONWebService
@OSGiBeanProperties(
	property = {
		"json.web.service.context.name=gradebook",
		"json.web.service.context.path=Assignment"
	},
	service = AssignmentService.class
)
@ProviderType
@Transactional(
	isolation = Isolation.PORTAL,
	rollbackFor = {PortalException.class, SystemException.class}
)
public interface AssignmentService extends BaseService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add custom service methods to <code>com.liferay.training.space.gradebook.service.impl.AssignmentServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface. Consume the assignment remote service via injection or a <code>org.osgi.util.tracker.ServiceTracker</code>. Use {@link AssignmentServiceUtil} if injection and service tracking are not available.
	 */
	public Assignment addAssignment(
			Assignment assignment, ServiceContext serviceContext)
		throws PortalException;

	public Assignment deleteAssignment(Assignment assignment)
		throws PortalException;

	public Assignment deleteAssignment(long assignmentId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Assignment getAssignment(long assignmentId) throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Assignment> getAssignmentsByGroupId(long groupId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Assignment> getAssignmentsByGroupId(
		long groupId, int start, int end);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getAssignmentsCountByGroupId(long groupId);

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public String getOSGiServiceIdentifier();

	public Assignment updateAssignment(Assignment assignment)
		throws PortalException;

}