package com.everhomes.contentserver;

import java.util.List;

import org.springframework.stereotype.Component;

import com.everhomes.acl.ResourceUserRoleResolver;

@Component("ContentServerResolver")
public class ContentServerResolver implements ResourceUserRoleResolver {

    @Override
    public List<Long> determineRoleInResource(long userId, Long ownerId, String resourceType, Long resourceId) {
        return null;
    }

}
