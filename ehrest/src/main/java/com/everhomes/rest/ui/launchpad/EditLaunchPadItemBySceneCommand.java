package com.everhomes.rest.ui.launchpad;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.ui.user.LaunchPadItemSort;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * <ul>
 * <li>addItems: 添加item id集 </li>
 * <li>delItems: 删除item id集 </li>
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * </ul>
 */
public class EditLaunchPadItemBySceneCommand {
	
	private String sceneToken;
	
	@ItemType(LaunchPadItemSort.class)
	private List<Long> delItemIds;

	@ItemType(LaunchPadItemSort.class)
	private List<Long> addItemIds;

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}

	public List<Long> getDelItemIds() {
		return delItemIds;
	}

	public void setDelItemIds(List<Long> delItemIds) {
		this.delItemIds = delItemIds;
	}

	public List<Long> getAddItemIds() {
		return addItemIds;
	}

	public void setAddItemIds(List<Long> addItemIds) {
		this.addItemIds = addItemIds;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
