/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.training.space.gradebook.model.impl;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.training.space.gradebook.model.Submission;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing Submission in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class SubmissionCacheModel
	implements CacheModel<Submission>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof SubmissionCacheModel)) {
			return false;
		}

		SubmissionCacheModel submissionCacheModel =
			(SubmissionCacheModel)object;

		if (submissionId == submissionCacheModel.submissionId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, submissionId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(25);

		sb.append("{submissionId=");
		sb.append(submissionId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", assignmentId=");
		sb.append(assignmentId);
		sb.append(", studentId=");
		sb.append(studentId);
		sb.append(", submitDate=");
		sb.append(submitDate);
		sb.append(", comment=");
		sb.append(comment);
		sb.append(", grade=");
		sb.append(grade);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public Submission toEntityModel() {
		SubmissionImpl submissionImpl = new SubmissionImpl();

		submissionImpl.setSubmissionId(submissionId);
		submissionImpl.setGroupId(groupId);
		submissionImpl.setCompanyId(companyId);
		submissionImpl.setUserId(userId);

		if (userName == null) {
			submissionImpl.setUserName("");
		}
		else {
			submissionImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			submissionImpl.setCreateDate(null);
		}
		else {
			submissionImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			submissionImpl.setModifiedDate(null);
		}
		else {
			submissionImpl.setModifiedDate(new Date(modifiedDate));
		}

		submissionImpl.setAssignmentId(assignmentId);
		submissionImpl.setStudentId(studentId);

		if (submitDate == Long.MIN_VALUE) {
			submissionImpl.setSubmitDate(null);
		}
		else {
			submissionImpl.setSubmitDate(new Date(submitDate));
		}

		if (comment == null) {
			submissionImpl.setComment("");
		}
		else {
			submissionImpl.setComment(comment);
		}

		submissionImpl.setGrade(grade);

		submissionImpl.resetOriginalValues();

		return submissionImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		submissionId = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		assignmentId = objectInput.readLong();

		studentId = objectInput.readLong();
		submitDate = objectInput.readLong();
		comment = objectInput.readUTF();

		grade = objectInput.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(submissionId);

		objectOutput.writeLong(groupId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeLong(assignmentId);

		objectOutput.writeLong(studentId);
		objectOutput.writeLong(submitDate);

		if (comment == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(comment);
		}

		objectOutput.writeInt(grade);
	}

	public long submissionId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public long assignmentId;
	public long studentId;
	public long submitDate;
	public String comment;
	public int grade;

}