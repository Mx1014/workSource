// @formatter:off
package com.everhomes.rest.news;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>entryId : 后台管理菜单配置的入口ID</li>
 * </ul>
 *
 *  @author:dengs 2017年9月21日
 */
public class GetCategoryIdByEntryIdCommand {
	private Integer entryId;

	public GetCategoryIdByEntryIdCommand() {
		super();
	}

	public GetCategoryIdByEntryIdCommand(Integer entryId) {
		super();
		this.entryId = entryId;
	}

	public Integer getEntryId() {
		return entryId;
	}

	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
