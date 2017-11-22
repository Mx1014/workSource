package com.everhomes.rest.point;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: nextPageAnchor</li>
 *     <li>rules: 积分规则列表 {@link com.everhomes.rest.point.PointRuleDTO}</li>
 * </ul>
 */
public class ListPointRulesResponse {

    private Long nextPageAnchor;

    @ItemType(PointRuleDTO.class)
    private List<PointRuleDTO> rules;

}
