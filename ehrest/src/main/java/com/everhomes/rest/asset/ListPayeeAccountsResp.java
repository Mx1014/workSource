//@formatter:off
package com.everhomes.rest.asset;

/**
 * @author created by yangcx
 * @date 2018年5月26日----下午2:59:35
 */
/**
 *<ul>
 * <li>bizPayeeAccount:收款方账户名称</li>
 * <li>bizPayeeType:收款方账户类型：EhUsers/EhOrganizations</li>
 * <li>bizPayeeId:收款方账户id</li>
 *</ul>
 */
public class ListPayeeAccountsResp {
	private String bizPayeeAccount;
	private String bizPayeeType;
	private String bizPayeeId;
	
	public String getBizPayeeAccount() {
		return bizPayeeAccount;
	}
	public void setBizPayeeAccount(String bizPayeeAccount) {
		this.bizPayeeAccount = bizPayeeAccount;
	}
	public String getBizPayeeType() {
		return bizPayeeType;
	}
	public void setBizPayeeType(String bizPayeeType) {
		this.bizPayeeType = bizPayeeType;
	}
	public String getBizPayeeId() {
		return bizPayeeId;
	}
	public void setBizPayeeId(String bizPayeeId) {
		this.bizPayeeId = bizPayeeId;
	}
}
