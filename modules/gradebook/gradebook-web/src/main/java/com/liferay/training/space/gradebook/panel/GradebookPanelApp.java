/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.training.space.gradebook.panel;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.bookmarks.constants.BookmarksPortletKeys;
import com.liferay.portal.kernel.model.Portlet;

import com.liferay.training.space.gradebook.portlet.GradebookPortletKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author fedi inetum
 */
@Component(
        property = {
                "panel.app.order:Integer=300",
                "panel.category.key=" + PanelCategoryKeys.SITE_ADMINISTRATION_CONTENT
        },
        service = PanelApp.class
)
public class GradebookPanelApp extends BasePanelApp {

    @Override
    public Portlet getPortlet() {
        return _portlet;
    }

    @Override
    public String getPortletId() {
        return GradebookPortletKeys.PORTLET_NAME;
    }

    @Reference(
            target = "(javax.portlet.name=" + GradebookPortletKeys.PORTLET_NAME + ")"
    )
    private Portlet _portlet;

}