package com.liferay.training.space.gradebook.service.permission;


import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.training.space.gradebook.service.AssignmentLocalService;
import org.osgi.service.component.annotations.Component;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.training.space.gradebook.model.Assignment;
import org.osgi.service.component.annotations.Reference;
import java.util.Objects;

@Component(property = "model.class.name=com.liferay.training.space.gradebook.model.Assignment",
           service = ModelResourcePermission.class)
public class AssignmentPermissionChecker implements ModelResourcePermission<Assignment> {

    private static final Log _log = LogFactoryUtil.getLog(AssignmentPermissionChecker.class);

    private static final String RESOURCE_NAME = Assignment.class.getName();

    private static final String TOP_LEVEL_RESOURCE = "com.liferay.training.space.gradebook";

    public static final String ADD_ASSIGNMENT = "ADD_ASSIGNMENT";

    @Reference
    private AssignmentLocalService assignmentLocalService;

   // @Reference(target = "(resource.name=" + "com.liferay.training.space.gradebook" + ")")
  //  private PortletResourcePermission portletResourcePermission;


    @Override
    public void check(PermissionChecker permissionChecker, long assignmentId, String actionId) throws PortalException {
        Assignment assignment = assignmentLocalService.getAssignment(assignmentId);
        long groupId = assignment.getGroupId();
        if (!contains(permissionChecker, groupId, assignmentId, actionId)) {
            throw new PrincipalException.MustHavePermission(
                    permissionChecker, Assignment.class.getName(), assignmentId, actionId);
        }
    }

    @Override
    public void check(PermissionChecker permissionChecker, Assignment model, String actionId) throws PortalException {
        if (Objects.nonNull(model)) {
            long assignmentId = model.getAssignmentId();
            long groupId = model.getGroupId();
            if (!contains(permissionChecker, groupId, assignmentId, actionId)) {
                throw new PrincipalException.MustHavePermission(
                        permissionChecker, Assignment.class.getName(), assignmentId, actionId);
            }
        }
        throw new NullPointerException();
    }

    @Override
    public boolean contains(PermissionChecker permissionChecker, long assignmentId, String actionId) throws PortalException {
        Assignment assignment = assignmentLocalService.getAssignment(assignmentId);
        long groupId = assignment.getGroupId();
        return contains(permissionChecker, groupId, assignmentId, actionId);
    }

    @Override
    public boolean contains(PermissionChecker permissionChecker, Assignment model, String actionId) throws PortalException {
        if (Objects.nonNull(model)) {
            long assignmentId = model.getAssignmentId();
            long groupId = model.getGroupId();
            return contains(permissionChecker, groupId, assignmentId, actionId);
        }
        return false;
    }

    @Override
    public String getModelName() {
        return Assignment.class.getName();
    }

    @Override
    public PortletResourcePermission getPortletResourcePermission() {
        return null;
    }

    public static boolean contains(
            PermissionChecker permissionChecker, long groupId, long assignmentId,
            String actionId) {
        _log.info("PermissionChecker : " + permissionChecker.getClass().getName());
        _log.info("groupId : " + groupId);
        _log.info("assignmentId : " + assignmentId);
        _log.info("actionId : " + actionId);

        boolean hasPermission = permissionChecker.hasPermission(groupId, RESOURCE_NAME, assignmentId, actionId);
        _log.info("hasPermission : " + hasPermission);
        return hasPermission;

    }
}

