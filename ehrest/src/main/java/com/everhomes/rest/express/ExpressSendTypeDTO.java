// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>expressCompanyId : 快递公司id</li>
 * <li>expressCompany : 快递公司名称</li>
 * <li>sendType : 寄件类型, 参考 {@link com.everhomes.rest.express.ExpressSendType}</li>
 * <li>sendTypeName : 寄件类型的名称</li>
 * <li>payType : 支付方式， 参考 {@link com.everhomes.rest.express.ExpressPayType}</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class ExpressSendTypeDTO {
	private Long expressCompanyId;
	private String expressCompany;
	private Byte sendType;
	private String sendTypeName;
	private Byte payType;
	
	public ExpressSendTypeDTO() {
	}
	
	public ExpressSendTypeDTO(Long expressCompanyId, String expressCompany, Byte sendType, String sendTypeName,
			Byte payType) {
		super();
		this.expressCompanyId = expressCompanyId;
		this.expressCompany = expressCompany;
		this.sendType = sendType;
		this.sendTypeName = sendTypeName;
		this.payType = payType;
	}

	public Long getExpressCompanyId() {
		return expressCompanyId;
	}

	public void setExpressCompanyId(Long expressCompanyId) {
		this.expressCompanyId = expressCompanyId;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public Byte getSendType() {
		return sendType;
	}

	public void setSendType(Byte sendType) {
		this.sendType = sendType;
	}

	public String getSendTypeName() {
		return sendTypeName;
	}

	public void setSendTypeName(String sendTypeName) {
		this.sendTypeName = sendTypeName;
	}

	public Byte getPayType() {
		return payType;
	}

	public void setPayType(Byte payType) {
		this.payType = payType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
