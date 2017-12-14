package com.everhomes.rest.point;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

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

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<PointRuleDTO> getRules() {
        return rules;
    }

    public void setRules(List<PointRuleDTO> rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
