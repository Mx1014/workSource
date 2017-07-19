// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>expressCompanyId : 快递公司id</li>
 * <li>sendType : 寄件类型, 参考 {@link com.everhomes.rest.express.ExpressSendType}</li>
 * <li>sendTypeName : 寄件类型的名称</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class ExpressSendTypeDTO {
	private Long expressCompanyId;
	private Long sendType;
	private String sendTypeName;
	
	public ExpressSendTypeDTO() {
	}
	public ExpressSendTypeDTO(Long expressCompanyId, Long sendType, String sendTypeName) {
		super();
		this.expressCompanyId = expressCompanyId;
		this.sendType = sendType;
		this.sendTypeName = sendTypeName;
	}
	public Long getExpressCompanyId() {
		return expressCompanyId;
	}
	public void setExpressCompanyId(Long expressCompanyId) {
		this.expressCompanyId = expressCompanyId;
	}
	public Long getSendType() {
		return sendType;
	}
	public void setSendType(Long sendType) {
		this.sendType = sendType;
	}
	public String getSendTypeName() {
		return sendTypeName;
	}
	public void setSendTypeName(String sendTypeName) {
		this.sendTypeName = sendTypeName;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
