package com.everhomes.forum;

import org.springframework.stereotype.Component;

@Component(TopicQueryFilterHandler.TOPIC_QUERY_FILTER_PREFIX + "discovery_park_enterprise_noauth")
public class DiscoveryParkEnterpriseNoAuthTopicQueryFilterHandler extends DiscoveryParkEnterpriseTopicQueryFilterHandler {
    // 在园区版，不管是认证还是非认证用户，只要有公司，则其菜单是一样的，故通过继承来减少代码
}
