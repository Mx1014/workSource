package com.everhomes.rest.servicehotline;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>SERVICE_HOTLINE:  1服务热线</li>
 * <li>ZHUANSHU_SERVICE: 2专属客服</li> 
 */
public enum ServiceType {
	SERVICE_HOTLINE((byte) 1), ZHUANSHU_SERVICE((byte) (1<<1)) ;
	private Byte code;

	private ServiceType(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public static ServiceType fromCode(Byte code) {
		if (code != null) {
			for (ServiceType a : ServiceType.values()) {
				if (code.byteValue() == a.code.byteValue()) {
					return a;
				}
			}
		}
		return null;
	}
}
