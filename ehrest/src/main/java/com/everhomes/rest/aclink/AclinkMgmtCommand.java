// @formatter:off
package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>wifiSsid:wifiSSID</li>
 * <li>wifiPwd:wifi密码</li>
 * <li>doorId:门禁id</li>
 * <li>serverId:服务器Id</li>
 * </ul>
 */
public class AclinkMgmtCommand {
    @NotNull
    private String wifiSsid;
    
    private String wifiPwd;
    
    @NotNull
    private Long doorId;
    
    @NotNull
    private Long serverId;

    public String getWifiSsid() {
        return wifiSsid;
    }

    public void setWifiSsid(String wifiSsid) {
        this.wifiSsid = wifiSsid;
    }

    public String getWifiPwd() {
        return wifiPwd;
    }

    public void setWifiPwd(String wifiPwd) {
        this.wifiPwd = wifiPwd;
    }

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
