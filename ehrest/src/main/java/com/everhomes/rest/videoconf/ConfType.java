package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>CONF_TYPE_VIDEO_ONLY: 仅视频</li>
 * <li>CONF_TYPE_PHONE_SUPPORT: 支持电话</li>
 * </ul>
 */
public enum ConfType {

	CONF_TYPE_VIDEO_ONLY("仅视频"), CONF_TYPE_PHONE_SUPPORT("支持电话");
    
    private String code;
    private ConfType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
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
