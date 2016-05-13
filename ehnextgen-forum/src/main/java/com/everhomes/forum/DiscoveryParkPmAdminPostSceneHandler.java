package com.everhomes.forum;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.rest.ui.forum.TopicFilterDTO;
import com.everhomes.rest.ui.forum.TopicScopeDTO;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.user.User;

@Component(PostSceneHandler.TOPIC_QUERY_FILTER_PREFIX + "discovery_park_pm_admin")
public class DiscoveryParkPmAdminPostSceneHandler extends DiscoveryPmAdminPostSceneHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscoveryParkPmAdminPostSceneHandler.class);
    
    @Override
    public List<TopicFilterDTO> getTopicQueryFilters(User user, SceneTokenDTO sceneToken) {
        SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
        if(sceneType == SceneType.PARK_PM_ADMIN) {
            LOGGER.error("Unsupported scene for simple user, sceneToken={}", sceneToken);
            return null;
        } else {
            return getTopicQueryFilters(user, sceneToken, sceneToken.getEntityId());
        }
    }
    
    @Override
    public List<TopicScopeDTO> getTopicSentScopes(User user, SceneTokenDTO sceneTokenDto) {
        SceneType sceneType = SceneType.fromCode(sceneTokenDto.getScene());
        if(sceneType == SceneType.PARK_PM_ADMIN) {
            LOGGER.error("Unsupported scene for simple user, sceneToken={}", sceneTokenDto);
            return null;
        } else {
            return getDiscoveryTopicSentScopes(user, sceneTokenDto, sceneTokenDto.getEntityId());
        }
    }
}
