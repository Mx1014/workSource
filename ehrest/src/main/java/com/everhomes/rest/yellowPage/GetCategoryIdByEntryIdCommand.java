// @formatter:off
package com.everhomes.rest.yellowPage;

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
	private Long entryId;

	public GetCategoryIdByEntryIdCommand() {
		super();
	}

	public GetCategoryIdByEntryIdCommand(Long entryId) {
		super();
		this.entryId = entryId;
	}

	public Long getEntryId() {
		return entryId;
	}

	public void setEntryId(Long entryId) {
		this.entryId = entryId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
