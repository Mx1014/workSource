package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>CONF_TYPE_VIDEO_ONLY: 仅视频 0</li>
 * <li>CONF_TYPE_PHONE_SUPPORT: 支持电话 1</li>
 * <li>CONF_TYPE_TRIAL: 支持电话 1</li>
 * </ul>
 */
public enum ConfType {

	CONF_TYPE_VIDEO_ONLY("仅视频", (byte)0), CONF_TYPE_PHONE_SUPPORT("支持电话", (byte)1),CONF_TYPE_TRIAL("试用账号", (byte)4);
    
    private String code;
    private byte status;
    
    private ConfType(String code, byte status) {
        this.code = code;
        this.status = status;
    }
    
    public String getCode() {
        return this.code;
    }

	public byte getStatus() {
		return status;
	}

	public static ConfType fromCode(String code) {
		for(ConfType v :ConfType.values()){
			if(v.getCode().equals(code))
				return v;
		}
		return null;
	}

	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
