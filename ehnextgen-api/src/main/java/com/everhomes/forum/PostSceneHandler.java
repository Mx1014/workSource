package com.everhomes.forum;

import java.util.List;

import com.everhomes.rest.ui.forum.TopicFilterDTO;
import com.everhomes.rest.ui.forum.TopicScopeDTO;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.user.User;

public interface PostSceneHandler {
    /** Handler的component名称构成：TOPIC_QUERY_FILTER_PREFIX + PostFilterType + _ + SceneType */
    String TOPIC_QUERY_FILTER_PREFIX = "TopicQueryFilter-";
    
    /**
     * 对于界面的帖子查询过滤器组件（不管是否是普通人还是物业管理员），通过场景信息构建查帖过滤器
     * @param user 用户
     * @param sceneToken 场景信息
     * @return 帖子过滤器列表
     */
    List<TopicFilterDTO> getTopicQueryFilters(User user, SceneTokenDTO sceneToken);
    
    /**
     * 对于界面的帖子发送范围组件（不管是否是普通人还是物业管理员），通过场景信息构建发帖范围
     * @param user 用户
     * @param sceneToken 场景信息
     * @return 发帖范围列表
     */
    List<TopicScopeDTO> getTopicSentScopes(User user, SceneTokenDTO sceneToken);
}
