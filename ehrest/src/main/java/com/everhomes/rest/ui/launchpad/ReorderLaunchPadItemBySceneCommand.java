package com.everhomes.rest.ui.launchpad;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.ui.user.LaunchPadItemSort;
import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>sorts: 排序集   {@link com.everhomes.rest.ui.user.LaunchPadItemSort}</li>
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * </ul>
 */
public class ReorderLaunchPadItemBySceneCommand {
	
	private String sceneToken;
	
	@NotNull
	@ItemType(LaunchPadItemSort.class)
	private List<LaunchPadItemSort> sorts;

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}


	public List<LaunchPadItemSort> getSorts() {
		return sorts;
	}

	public void setSorts(List<LaunchPadItemSort> sorts) {
		this.sorts = sorts;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
