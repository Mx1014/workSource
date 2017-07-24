// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>sendMode : 寄件方式, 参考 {@link com.everhomes.rest.express.ExpressSendMode}</li>
 * <li>sendModeName : 寄件方式名称</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class ExpressSendModeDTO {
	private Byte sendMode;
	private String sendModeName;
	
	public Byte getSendMode() {
		return sendMode;
	}

	public void setSendMode(Byte sendMode) {
		this.sendMode = sendMode;
	}
	
	public String getSendModeName() {
		return sendModeName;
	}

	public void setSendModeName(String sendModeName) {
		this.sendModeName = sendModeName;
	}


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
