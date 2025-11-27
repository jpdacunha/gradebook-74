/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.training.space.gradebook.model;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The extended model interface for the Submission service. Represents a row in the &quot;Gradebook_Submission&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see SubmissionModel
 * @generated
 */
@ImplementationClassName(
	"com.liferay.training.space.gradebook.model.impl.SubmissionImpl"
)
@ProviderType
public interface Submission extends PersistedModel, SubmissionModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>com.liferay.training.space.gradebook.model.impl.SubmissionImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<Submission, Long> SUBMISSION_ID_ACCESSOR =
		new Accessor<Submission, Long>() {

			@Override
			public Long get(Submission submission) {
				return submission.getSubmissionId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<Submission> getTypeClass() {
				return Submission.class;
			}

		};

}