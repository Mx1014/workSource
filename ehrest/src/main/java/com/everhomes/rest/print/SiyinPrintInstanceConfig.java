package com.everhomes.rest.print;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>p1 : p1</li>
 * <li>currentPMId : 管理公司id</li>
 * <li>currentProjectId : 当前项目id</li>
 * <li>appId : 应用的originId</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年9月20日
 */
public class SiyinPrintInstanceConfig {
	private String url;
	private Long chargeAppToken;
	private Long billGroupToken;
	private Long chargeItemTorken;
    @Override
    public String toString() {
    	return StringHelper.toJsonString(this);
    }
	public Long getChargeAppToken() {
		return chargeAppToken;
	}
	public void setChargeAppToken(Long chargeAppToken) {
		this.chargeAppToken = chargeAppToken;
	}
	public Long getBillGroupToken() {
		return billGroupToken;
	}
	public void setBillGroupToken(Long billGroupToken) {
		this.billGroupToken = billGroupToken;
	}
	public Long getChargeItemTorken() {
		return chargeItemTorken;
	}
	public void setChargeItemTorken(Long chargeItemTorken) {
		this.chargeItemTorken = chargeItemTorken;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
