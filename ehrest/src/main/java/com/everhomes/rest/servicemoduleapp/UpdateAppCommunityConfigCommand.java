package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>visibilityFlag: 是否可见{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>displayName: 显示名称</li>
 * </ul>
 */
public class UpdateAppCommunityConfigCommand {
	private Long id;
	private Byte visibilityFlag;
	private String displayName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getVisibilityFlag() {
		return visibilityFlag;
	}

	public void setVisibilityFlag(Byte visibilityFlag) {
		this.visibilityFlag = visibilityFlag;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
