package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>orderId: 订单号</li>
 *  <li>taxpayerType: 发票抬头 0-公司 1-个人 </li>
 *  <li>vatType: 纳税人类型 0-一般纳税人 1-非一般纳税人 </li>
 *  <li>expenseType: 费用类型  0-CONF </li>
 *  <li>companyName: 企业名称</li>
 *  <li>vatCode: 税务登记证号</li>
 *  <li>vatAddress: 税务登记地址</li>
 *  <li>vatPhone: 税务登记电话</li>
 *  <li>vatBankname: 开户银行名称 </li>
 *  <li>vatBankaccount: 银行账号</li>
 *  <li>address: 地址</li>
 *  <li>zipCode: 邮编</li>
 *  <li>consignee: 收件人姓名</li>
 *  <li>contact: 联系方式</li>
 *  <li>contractFlag: 是否需要合同 0-不需要 1-需要</li>
 * </ul>
 *
 */
public class UpdateInvoiceCommand {

	private Long orderId;
	
	private Byte taxpayerType;
	
	private Byte vatType;
	
	private Byte expenseType;
	
	private String companyName;
	
	private String vatCode;
	
	private String vatAddress;
	
	private String vatPhone;
	
	private String vatBankname;
	
	private String vatBankaccount;
	
	private String address;
	
	private String zipCode;
	
	private String consignee;
	
	private String contact;
	
	private Byte contractFlag;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Byte getTaxpayerType() {
		return taxpayerType;
	}

	public void setTaxpayerType(Byte taxpayerType) {
		this.taxpayerType = taxpayerType;
	}

	public Byte getVatType() {
		return vatType;
	}

	public void setVatType(Byte vatType) {
		this.vatType = vatType;
	}

	public Byte getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(Byte expenseType) {
		this.expenseType = expenseType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getVatCode() {
		return vatCode;
	}

	public void setVatCode(String vatCode) {
		this.vatCode = vatCode;
	}

	public String getVatAddress() {
		return vatAddress;
	}

	public void setVatAddress(String vatAddress) {
		this.vatAddress = vatAddress;
	}

	public String getVatPhone() {
		return vatPhone;
	}

	public void setVatPhone(String vatPhone) {
		this.vatPhone = vatPhone;
	}

	public String getVatBankname() {
		return vatBankname;
	}

	public void setVatBankname(String vatBankname) {
		this.vatBankname = vatBankname;
	}

	public String getVatBankaccount() {
		return vatBankaccount;
	}

	public void setVatBankaccount(String vatBankaccount) {
		this.vatBankaccount = vatBankaccount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Byte getContractFlag() {
		return contractFlag;
	}

	public void setContractFlag(Byte contractFlag) {
		this.contractFlag = contractFlag;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
