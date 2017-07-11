package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>keys: 服务器返回的 sdkKeys </li>
 * <li>storeyAuthList: 楼梯权限</li>
 * <li>authStorey: 自动选择的楼梯号</li>
 * <li>authLevel: 0 普通帐号， 1 VIP</li>
 * </ul>
 * @author janson
 *
 */
public class DoorLinglingExtraKeyDTO {
    @ItemType(String.class)
    private List<String> keys;
    
    @ItemType(Long.class)
    private List<Long> storeyAuthList;
    
    private Long authStorey;
    
    private Long authLevel;
    
    private String linglingId;
    
    @ItemType(DoorLinglingAuthStoreyInfo.class)
    private List<DoorLinglingAuthStoreyInfo> storeyInfos;

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public List<Long> getStoreyAuthList() {
        return storeyAuthList;
    }

    public void setStoreyAuthList(List<Long> storeyAuthList) {
        this.storeyAuthList = storeyAuthList;
    }

    public Long getAuthStorey() {
        return authStorey;
    }

    public void setAuthStorey(Long authStorey) {
        this.authStorey = authStorey;
    }

    public Long getAuthLevel() {
        return authLevel;
    }

    public void setAuthLevel(Long authLevel) {
        this.authLevel = authLevel;
    }

	public String getLinglingId() {
		return linglingId;
	}

	public void setLinglingId(String linglingId) {
		this.linglingId = linglingId;
	}

    public List<DoorLinglingAuthStoreyInfo> getStoreyInfos() {
        return storeyInfos;
    }

    public void setStoreyInfos(List<DoorLinglingAuthStoreyInfo> storeyInfos) {
        this.storeyInfos = storeyInfos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
