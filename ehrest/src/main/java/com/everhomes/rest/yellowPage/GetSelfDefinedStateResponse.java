package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>isOpen : 0-关闭 1-开启</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年8月22日
 */
public class GetSelfDefinedStateResponse {
	private Byte isOpen;
	
	public Byte getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Byte isOpen) {
		this.isOpen = isOpen;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
