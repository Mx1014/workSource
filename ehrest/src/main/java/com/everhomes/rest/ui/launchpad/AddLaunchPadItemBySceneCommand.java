package com.everhomes.rest.ui.launchpad;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>id: item ID</li>
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * </ul>
 */
public class AddLaunchPadItemBySceneCommand {
	
	private String sceneToken;
	
	@NotNull
	private Long id;

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
