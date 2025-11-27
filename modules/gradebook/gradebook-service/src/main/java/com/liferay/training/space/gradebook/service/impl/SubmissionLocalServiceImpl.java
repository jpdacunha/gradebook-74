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

import com.liferay.training.space.gradebook.model.Submission;
import com.liferay.training.space.gradebook.service.base.SubmissionLocalServiceBaseImpl;

import java.util.List;

import aQute.bnd.annotation.ProviderType;

/**
 * The implementation of the submission local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.training.space.gradebook.service.SubmissionLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SubmissionLocalServiceBaseImpl
 * @see com.liferay.training.space.gradebook.service.SubmissionLocalServiceUtil
 */
@ProviderType
public class SubmissionLocalServiceImpl extends SubmissionLocalServiceBaseImpl {
		@Override
	public Submission addSubmission(Submission submission) {

		long submissionId =
			counterLocalService.increment(Submission.class.getName());

		submission.setSubmissionId(submissionId);

		return super.addSubmission(submission);
	}

	public List<Submission> getSubmissionsByAssignment(
		long groupId, long assignmentId) {

		return submissionPersistence.findByG_A(groupId, assignmentId);
	}

	public List<Submission> getSubmissionsByAssignment(
		long groupId, long assignmentId, int start, int end) {

		return submissionPersistence.findByG_A(
			groupId, assignmentId, start, end);
	}

	public int getSubmissionsCountByAssignment(
		long groupId, long assignmentId) {

		return submissionPersistence.countByG_A(groupId, assignmentId);
	}
}