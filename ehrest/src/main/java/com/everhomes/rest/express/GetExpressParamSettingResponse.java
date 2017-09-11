// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>expressUserSettingShowFlag : tab(快递员设置)是否显示，参考 {@link com.everhomes.rest.express.ExpressShowType}</li>
 * <li>businessNoteSettingShowFlag :  tab(业务说明)是否显示，参考 {@link com.everhomes.rest.express.ExpressShowType}</li>
 * <li>hotlineSettingShowFlag :  tab(客服热线)是否显示，参考 {@link com.everhomes.rest.express.ExpressShowType}</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class GetExpressParamSettingResponse {
	private Byte expressUserSettingShowFlag;
	private Byte businessNoteSettingShowFlag;
	private Byte hotlineSettingShowFlag;
	
	public GetExpressParamSettingResponse(){}
	
	public GetExpressParamSettingResponse(Byte expressUserSettingShowFlag, Byte businessNoteSettingShowFlag,
			Byte hotlineSettingShowFlag) {
		super();
		this.expressUserSettingShowFlag = expressUserSettingShowFlag;
		this.businessNoteSettingShowFlag = businessNoteSettingShowFlag;
		this.hotlineSettingShowFlag = hotlineSettingShowFlag;
	}

	public Byte getExpressUserSettingShowFlag() {
		return expressUserSettingShowFlag;
	}
	public void setExpressUserSettingShowFlag(Byte expressUserSettingShowFlag) {
		this.expressUserSettingShowFlag = expressUserSettingShowFlag;
	}
	public Byte getBusinessNoteSettingShowFlag() {
		return businessNoteSettingShowFlag;
	}
	public void setBusinessNoteSettingShowFlag(Byte businessNoteSettingShowFlag) {
		this.businessNoteSettingShowFlag = businessNoteSettingShowFlag;
	}
	public Byte getHotlineSettingShowFlag() {
		return hotlineSettingShowFlag;
	}
	public void setHotlineSettingShowFlag(Byte hotlineSettingShowFlag) {
		this.hotlineSettingShowFlag = hotlineSettingShowFlag;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
