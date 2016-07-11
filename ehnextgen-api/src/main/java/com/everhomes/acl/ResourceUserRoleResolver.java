// @formatter:off
package com.everhomes.acl;

import java.util.List;

public interface ResourceUserRoleResolver {
    List<Long> determineRoleInResource(long userId, Long ownerId, String resourceType, Long resourceId);
}
