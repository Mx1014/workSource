package com.everhomes.rest.videoconf;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页页码，如果没有则后面没有数据</li>
 * <li>rules: rule信息，参考{@link com.everhomes.rest.videoconf.VideoConfAccountRuleDTO}</li>
 * </ul>
 */
public class ListVideoConfAccountRuleResponse {
	
	@ItemType(VideoConfAccountRuleDTO.class)
	private List<VideoConfAccountRuleDTO> rules;
	
	private Integer nextPageOffset;

	public List<VideoConfAccountRuleDTO> getRules() {
		return rules;
	}

	public void setRules(List<VideoConfAccountRuleDTO> rules) {
		this.rules = rules;
	}

	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
