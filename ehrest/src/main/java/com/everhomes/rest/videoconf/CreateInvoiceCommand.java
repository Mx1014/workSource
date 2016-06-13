package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *  <li>orderId: eh_conf_orders的主键id</li>
 *  <li>taxpayerType: 发票抬头 0-公司 1-个人 </li>
 *  <li>vatType: 纳税人类型 0-一般纳税人 1-非一般纳税人 </li>
 *  <li>expenseType: 费用类型 0-电话/数据会议费</li>
 *  <li>companyName: 公司名称</li>
 *  <li>taxRegCertificateNum: 税务登记证号</li>
 *  <li>taxRegAddress: 税务登记地址</li>
 *  <li>taxRegPhone: 税务登记电话</li>
 *  <li>bankName: 银行名称</li>
 *  <li>bankAccount: 银行账号</li>
 *  <li>address: 寄送地址</li>
 *  <li>zipCode: 邮政编码</li>
 *  <li>consignee: 收件人姓名</li>
 *  <li>contact: 联系电话</li>
 *  <li>contractFlag: 是否需要合同 0-不需要 1-需要</li>
 * </ul>
 *
 */
public class CreateInvoiceCommand {
	
	private Long orderId;
	
	private Byte taxpayerType;
	
	private Byte vatType;
	
	private Byte expenseType;
	
	private String companyName;
	
	private String taxRegCertificateNum;
	
	private String taxRegAddress;
	
	private String taxRegPhone;
	
	private String bankName;
	
	private String bankAccount;
	
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

	public String getTaxRegCertificateNum() {
		return taxRegCertificateNum;
	}

	public void setTaxRegCertificateNum(String taxRegCertificateNum) {
		this.taxRegCertificateNum = taxRegCertificateNum;
	}

	public String getTaxRegAddress() {
		return taxRegAddress;
	}

	public void setTaxRegAddress(String taxRegAddress) {
		this.taxRegAddress = taxRegAddress;
	}

	public String getTaxRegPhone() {
		return taxRegPhone;
	}

	public void setTaxRegPhone(String taxRegPhone) {
		this.taxRegPhone = taxRegPhone;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
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
