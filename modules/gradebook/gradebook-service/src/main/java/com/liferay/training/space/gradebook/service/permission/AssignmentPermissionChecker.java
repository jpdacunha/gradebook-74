package com.liferay.training.space.gradebook.service.permission;


import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.training.space.gradebook.model.Assignment;

public class AssignmentPermissionChecker {

    private static final Log _log = LogFactoryUtil.getLog(AssignmentPermissionChecker.class);

    private static final String RESOURCE_NAME = Assignment.class.getName();

    private static final String TOP_LEVEL_RESOURCE = "com.liferay.training.space.gradebook";

    public static final String ADD_ASSIGNMENT = "ADD_ASSIGNMENT";

    private AssignmentPermissionChecker() {
    }

    public static void checkTopLevel(
            PermissionChecker permissionChecker, long groupId, String actionId)
            throws PrincipalException.MustHavePermission {

        if (!containsTopLevel(permissionChecker, groupId, actionId)) {
            throw new PrincipalException.MustHavePermission(
                    permissionChecker, TOP_LEVEL_RESOURCE, actionId);
        }

    }

    public static boolean containsTopLevel(
            PermissionChecker permissionChecker, long groupId, String actionId) {

        _log.info("PermissionChecker : " + permissionChecker.getClass().getName());
        _log.info("groupId : " + groupId);
        _log.info("actionId : " + actionId);

        boolean hasPermission = permissionChecker.hasPermission(groupId, TOP_LEVEL_RESOURCE, groupId, actionId);
        _log.info("hasPermission : " + hasPermission);

        return hasPermission;

    }

    public static void check(
            PermissionChecker permissionChecker, long groupId, long assignmentId,
            String actionId) throws PrincipalException.MustHavePermission {

        _log.info("PermissionChecker : " + permissionChecker.getClass().getName());
        _log.info("groupId : " + groupId);
        _log.info("assignmentId : " + assignmentId);
        _log.info("actionId : " + actionId);

        boolean hasPermission = permissionChecker.hasPermission(groupId, RESOURCE_NAME, assignmentId, actionId);
        _log.info("hasPermission : " + hasPermission);

        if (!hasPermission) {
            throw new PrincipalException.MustHavePermission(
                    permissionChecker, Assignment.class.getName(), assignmentId, actionId);
        }

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

