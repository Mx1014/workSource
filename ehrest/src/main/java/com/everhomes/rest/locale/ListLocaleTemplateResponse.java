// @formatter:off
package com.everhomes.rest.locale;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>templateList: 模板列表，参考{@link com.everhomes.rest.locale.LocaleTemplateDTO}</li>
 * </ul>
 */
public class ListLocaleTemplateResponse {
	private Long nextPageAnchor;
	@ItemType(LocaleTemplateDTO.class)
	private List<LocaleTemplateDTO> templateList;
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<LocaleTemplateDTO> getTemplateList() {
		return templateList;
	}
	public void setTemplateList(List<LocaleTemplateDTO> templateList) {
		this.templateList = templateList;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
