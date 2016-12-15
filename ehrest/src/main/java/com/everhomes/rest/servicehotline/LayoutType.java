package com.everhomes.rest.servicehotline;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>SERVICE_HOTLINE:  1服务热线</li>
 * <li>ZHUANSHU_SERVICE: 2专属客服</li> 
 */
public enum LayoutType {
	SERVICE_HOTLINE((byte) 1), ZHUANSHU_SERVICE((byte) 2) ;
	private Byte code;

	private LayoutType(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public static LayoutType fromCode(Byte code) {
		if (code != null) {
			for (LayoutType a : LayoutType.values()) {
				if (code.byteValue() == a.code.byteValue()) {
					return a;
				}
			}
		}
		return null;
	}
}
