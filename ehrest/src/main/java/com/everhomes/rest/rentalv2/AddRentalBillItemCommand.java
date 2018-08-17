package com.everhomes.rest.rentalv2;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchExceptionRequestDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>rentalSiteId：场所id</li>   
 * <li>rentalType：类型</li>   
 * <li>rentalBillId：订单id</li>
 * <li>rentalItems：商品列表，参考{@link com.everhomes.rest.rentalv2.SiteItemDTO}</li>
 * <li>rentalAttachments：附件列表，参考{@link com.everhomes.rest.rentalv2.AttachmentDTO}</li>
 * <li>fileUris：文件列表，参考{@link com.everhomes.rest.rentalv2.RentalSiteFileDTO}</li>
 * <li>paymentType: 支付方式，微信公众号支付方式必填，9-公众号支付 参考{@link com.everhomes.rest.order.PaymentType}</li>
 * </ul>
 */
public class AddRentalBillItemCommand {

	@NotNull
	private Long rentalBillId; 
	private Byte rentalType;
	@ItemType(SiteItemDTO.class)
	private List<SiteItemDTO> rentalItems;
	@ItemType(AttachmentDTO.class)
	private List<AttachmentDTO> rentalAttachments;
	private List<RentalSiteFileDTO> fileUris;

	private String clientAppName;
	private Integer paymentType;
	private String customObject;

	private Long userEnterpriseId;
	private String userEnterpriseName;
	private String userPhone;
	private String userName;
	private Long addressId;

	public Long getUserEnterpriseId() {
		return userEnterpriseId;
	}

	public void setUserEnterpriseId(Long userEnterpriseId) {
		this.userEnterpriseId = userEnterpriseId;
	}

	public String getUserEnterpriseName() {
		return userEnterpriseName;
	}

	public void setUserEnterpriseName(String userEnterpriseName) {
		this.userEnterpriseName = userEnterpriseName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getCustomObject() {
		return customObject;
	}

	public void setCustomObject(String customObject) {
		this.customObject = customObject;
	}

	public String getClientAppName() {
		return clientAppName;
	}

	public void setClientAppName(String clientAppName) {
		this.clientAppName = clientAppName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getRentalBillId() {
		return rentalBillId;
	}

	public Byte getRentalType() {
		return rentalType;
	}

	public void setRentalType(Byte rentalType) {
		this.rentalType = rentalType;
	}

	public void setRentalBillId(Long rentalBillId) {
		this.rentalBillId = rentalBillId;
	}

	public List<SiteItemDTO> getRentalItems() {
		return rentalItems;
	}

	public void setRentalItems(List<SiteItemDTO> rentalItems) {
		this.rentalItems = rentalItems;
	}
 

	public List<AttachmentDTO> getRentalAttachments() {
		return rentalAttachments;
	}

	public void setRentalAttachments(List<AttachmentDTO> rentalAttachments) {
		this.rentalAttachments = rentalAttachments;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public List<RentalSiteFileDTO> getFileUris() {
		return fileUris;
	}

	public void setFileUris(List<RentalSiteFileDTO> fileUris) {
		this.fileUris = fileUris;
	}
}
