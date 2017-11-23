// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>hotlineFlag : 热线是否在app显示 ，参考 {@link com.everhomes.rest.express.ExpressShowType}</li>
 * <li>businessNoteFlag : 业务说明在app是否显示 ，参考 {@link com.everhomes.rest.express.ExpressShowType}</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class GetExpressHotlineAndBusinessNoteFlagResponse {
	private Byte hotlineFlag;
	private Byte businessNoteFlag;
	
	public GetExpressHotlineAndBusinessNoteFlagResponse() {
		super();
	}
	public GetExpressHotlineAndBusinessNoteFlagResponse(Byte hotlineFlag, Byte businessNoteFlag) {
		super();
		this.hotlineFlag = hotlineFlag;
		this.businessNoteFlag = businessNoteFlag;
	}
	public Byte getHotlineFlag() {
		return hotlineFlag;
	}
	public void setHotlineFlag(Byte hotlineFlag) {
		this.hotlineFlag = hotlineFlag;
	}
	public Byte getBusinessNoteFlag() {
		return businessNoteFlag;
	}
	public void setBusinessNoteFlag(Byte businessNoteFlag) {
		this.businessNoteFlag = businessNoteFlag;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
