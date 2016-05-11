package com.everhomes.forum;

import org.springframework.stereotype.Component;

@Component(TopicQueryFilterHandler.TOPIC_QUERY_FILTER_PREFIX + "discovery_family")
public class DiscoveryFamilyTopicQueryFilterHandler extends DiscoveryDefaultTopicQueryFilterHandler {
    // 对于家庭场景，帖子过滤菜单列表的产生与小区游客场景一致，故使用继承的方式，避免重复写代码 by lqs 20160510
    // 若后面需要有特殊处理，则可改为自己实现而不继承小区游客场景的实现
}
