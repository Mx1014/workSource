package com.everhomes.forum;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.community.Community;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.ui.forum.TopicFilterDTO;
import com.everhomes.rest.ui.forum.TopicScopeDTO;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.user.User;

@Component(PostSceneHandler.TOPIC_QUERY_FILTER_PREFIX + "discovery_park_enterprise_noauth")
public class DiscoveryParkEnterpriseNoAuthPostSceneHandler extends DiscoveryParkEnterprisePostSceneHandler {
// 园区版即使用户加入了非物业管理公司，其帖子相关菜单与园区游客相似，故继承DiscoveryParkTouristPostSceneHandler以减少代码 by lqs 20160512
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscoveryParkEnterprisePostSceneHandler.class);
        
    @Autowired
    private OrganizationProvider organizationProvider;
    
    @Autowired
    private OrganizationService organizationService;
    
    public List<TopicFilterDTO> getTopicQueryFilters(User user, SceneTokenDTO sceneToken) {
        List<TopicFilterDTO> filterList = new ArrayList<TopicFilterDTO>();
        
        SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
        if(sceneType == SceneType.PARK_ENTERPRISE_NOAUTH) {
            Organization organization = organizationProvider.findOrganizationById(sceneToken.getEntityId());
            if(organization == null) {
                LOGGER.error("Organization not found, sceneToken={}", sceneToken);
                return filterList;
            }
            
            Long communityId = organizationService.getOrganizationActiveCommunityId(organization.getId());
            if(communityId != null) {
                Community community = communityProvider.findCommunityById(communityId);
                if(community != null) {
                    filterList = getTopicQueryFilters(user, sceneToken, community);
                } else {
                    LOGGER.error("Community not found, communityId={}, sceneToken={}", sceneToken.getEntityId(), sceneToken);
                }
            } else {
                LOGGER.error("No community id found in organization, sceneToken=" + sceneToken);
            }
        }
        
        return filterList;
    }

    @Override
    public List<TopicScopeDTO> getDiscoveryTopicSentScopes(User user, SceneTokenDTO sceneToken) {
        List<TopicScopeDTO> scopeList = new ArrayList<TopicScopeDTO>();
        SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
        if(sceneType == SceneType.PARK_ENTERPRISE_NOAUTH) {
            Organization organization = organizationProvider.findOrganizationById(sceneToken.getEntityId());
            if(organization == null) {
                LOGGER.error("Organization not found, sceneToken={}", sceneToken);
                return scopeList;
            }
            
            Long communityId = organizationService.getOrganizationActiveCommunityId(organization.getId());
            if(communityId == null) {
                LOGGER.error("The organization is not in a community, organizationId={}, sceneToken={}", organization.getId(), sceneToken);
                return scopeList;
            }
            
            Community community = communityProvider.findCommunityById(sceneToken.getEntityId());
            if(community != null) {
                scopeList = getDiscoveryTopicSentScopes(user, sceneToken, community);
            } else {
                LOGGER.error("Community not found, communityId={}, sceneToken={}", sceneToken.getEntityId(), sceneToken);
            }
        } else {
            LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneToken);
        }
        
        return scopeList;
    }
}
