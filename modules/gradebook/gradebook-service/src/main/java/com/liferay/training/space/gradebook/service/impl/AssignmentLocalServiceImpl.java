package com.liferay.training.space.gradebook.service.impl;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.training.space.gradebook.model.Assignment;
import com.liferay.training.space.gradebook.model.Submission;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.liferay.training.space.gradebook.service.base.AssignmentLocalServiceBaseImpl;
import org.osgi.service.component.annotations.Component;

@Component(
		property = "model.class.name=com.liferay.training.space.gradebook.model.Assignment",
		service = com.liferay.training.space.gradebook.service.AssignmentLocalService.class
)
public class AssignmentLocalServiceImpl extends AssignmentLocalServiceBaseImpl {

	// ----------------------------------------------------
	// ADD ASSIGNMENT
	// ----------------------------------------------------
	@Indexable(type = IndexableType.REINDEX)
	public Assignment addAssignment(
			Assignment assignment, ServiceContext serviceContext)
			throws PortalException {

		long assignmentId = counterLocalService.increment(
				Assignment.class.getName()
		);

		assignment.setAssignmentId(assignmentId);
		assignment.setCompanyId(serviceContext.getCompanyId());
		assignment.setGroupId(serviceContext.getScopeGroupId());
		assignment.setUserId(serviceContext.getUserId());

		User user = userLocalService.fetchUser(serviceContext.getUserId());

		if (user != null) {
			assignment.setUserName(user.getFullName());
		}

		Date now = new Date();

		assignment.setCreateDate(serviceContext.getCreateDate(now));
		assignment.setModifiedDate(serviceContext.getModifiedDate(now));

		// Save entity
		assignment = super.addAssignment(assignment);

		// Add permissions (MODEL-RESOURCE)
		resourceLocalService.addModelResources(assignment, serviceContext);

		// Update Asset Entry
		updateAsset(assignment, serviceContext);

		return assignment;
	}

	@Indexable(type = IndexableType.DELETE)
	public Assignment deleteAssignment(Assignment assignment) throws PortalException {

			resourceLocalService.deleteResource(
					assignment,
					ResourceConstants.SCOPE_INDIVIDUAL
			);
			AssetEntry assetEntry = assetEntryLocalService.fetchEntry(
					Assignment.class.getName(),
					assignment.getAssignmentId()
			);

			if (assetEntry != null) {
				assetEntryLocalService.deleteAssetEntry(assetEntry);
			}
		    // Depuis Liferay 7.4, l’API AssetEntryLocalService a été simplifiée, et l’effacement d’un AssetEntry se fait via la persistance, pas via le service.
			/*assetEntryLocalService.deleteEntry(Assignment.class.getName(),
					assignment.getAssignmentId()
			);*/

			return super.deleteAssignment(assignment);


	}


	@Override
	public Assignment addAssignment(Assignment assignment) {
		throw new UnsupportedOperationException(
				"Use addAssignment(Assignment, ServiceContext) instead."
		);
	}


	// ----------------------------------------------------
	// SUBMISSIONS
	// ----------------------------------------------------
	public void addBlankSubmissions(long groupId, long assignmentId)
			throws PortalException {

		long[] userIds = userLocalService.getGroupUserIds(groupId);

		List<User> students = new ArrayList<>();

		for (long userId : userIds) {
			students.add(userLocalService.getUser(userId));
		}

		addBlankSubmissions(students, assignmentId, groupId);
	}


	public void addBlankSubmissions(
			List<User> students, long assignmentId, long groupId) {

		for (User student : students) {

			Submission submission = submissionLocalService.createSubmission(0);

			submission.setStudentId(student.getUserId());
			submission.setGroupId(groupId);
			submission.setCreateDate(DateUtil.newDate());
			submission.setAssignmentId(assignmentId);
			submission.setGrade(0);

			submissionLocalService.addSubmission(submission);
		}
	}


	// ----------------------------------------------------
	// UPDATE
	// ----------------------------------------------------
	@Indexable(type = IndexableType.REINDEX)
	public Assignment updateAssignment(
			Assignment assignment, ServiceContext serviceContext)
			throws PortalException {

		assignment.setModifiedDate(serviceContext.getModifiedDate(new Date()));

		updateAsset(assignment, serviceContext);

		return super.updateAssignment(assignment);
	}


	// ----------------------------------------------------
	// FINDERS
	// ----------------------------------------------------
	public List<Assignment> getAssignmentsByGroupId(long groupId) {
		return assignmentPersistence.findByGroupId(groupId);
	}

	public List<Assignment> getAssignmentsByGroupId(
			long groupId, int start, int end) {

		return assignmentPersistence.findByGroupId(groupId, start, end);
	}

	public int getAssignmentsCountByGroupId(long groupId) {
		return assignmentPersistence.countByGroupId(groupId);
	}

	public List<Assignment> getAssignmentsByStatus(int status) {
		return assignmentPersistence.findByStatus(status);
	}


	// ----------------------------------------------------
	// ASSET ENTRY
	// ----------------------------------------------------
	private void updateAsset(
			Assignment assignment, ServiceContext serviceContext)
			throws PortalException {

		assetEntryLocalService.updateEntry(
				serviceContext.getUserId(),
				serviceContext.getScopeGroupId(),
				assignment.getCreateDate(),
				assignment.getModifiedDate(),
				Assignment.class.getName(),
				assignment.getAssignmentId(),
				assignment.getUuid(),
				0,
				serviceContext.getAssetCategoryIds(),
				serviceContext.getAssetTagNames(),
				true,
				true,
				assignment.getCreateDate(),
				null,
				null,
				null,
				ContentTypes.TEXT_HTML,
				assignment.getTitle(serviceContext.getLocale()),
				assignment.getDescription(),
				null,
				null,
				null,
				0,
				0,
				serviceContext.getAssetPriority()
		);
	}
}
