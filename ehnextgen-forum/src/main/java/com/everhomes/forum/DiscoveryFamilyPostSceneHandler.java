package com.everhomes.forum;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.community.Community;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.ui.forum.TopicFilterDTO;
import com.everhomes.rest.ui.forum.TopicScopeDTO;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.user.User;

@Component(PostSceneHandler.TOPIC_QUERY_FILTER_PREFIX + "discovery_family")
public class DiscoveryFamilyPostSceneHandler extends DiscoveryDefaultPostSceneHandler {
// 对于家庭场景，帖子过滤菜单列表的产生与小区游客场景一致，故使用继承的方式，避免重复写代码 by lqs 20160510
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscoveryFamilyPostSceneHandler.class);
    
    public List<TopicFilterDTO> getTopicQueryFilters(User user, SceneTokenDTO sceneToken) {
        List<TopicFilterDTO> filterList = null;
        Community community = null;
        SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
        if(sceneType == SceneType.FAMILY) {
            FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
            if(family != null) {
                community = communityProvider.findCommunityById(family.getCommunityId());
                if(community != null) {
                    filterList = getTopicQueryFilters(user, sceneToken, community);
                } else {
                    LOGGER.error("Community not found, communityId={}, sceneToken={}", sceneToken.getEntityId(), sceneToken);
                }
            } else {
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Family not found, sceneToken=" + sceneToken);
                }
            }
        } else {
            LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
        }
        
        return filterList;
    }
    
    @Override
    public List<TopicScopeDTO> getTopicSentScopes(User user, SceneTokenDTO sceneToken) {
        List<TopicScopeDTO> scopeList = null;
        Community community = null;
        SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
        if(sceneType == SceneType.FAMILY) {
            FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
            if(family != null) {
                community = communityProvider.findCommunityById(family.getCommunityId());
                if(community != null) {
                    scopeList = getDiscoveryTopicSentScopes(user, sceneToken, community);
                } else {
                    LOGGER.error("Community not found, communityId={}, sceneToken={}", sceneToken.getEntityId(), sceneToken);
                }
            } else {
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Family not found, sceneToken=" + sceneToken);
                }
            }
        } else {
            LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
        }
        
        return scopeList;
    }
}
