//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.Date;
import java.util.List;

/**
 *<ul>
 * <li>ownerType:所属者type</li>
 * <li>ownerId:所属者id</li>
 * <li>dateStr:账期</li>
 * <li>targetType:客户类别</li>
 * <li>targetId:客户id</li>
 * <li>contractId:合同id</li>
 * <li>contractNum:合同编号</li>
 * <li>noticeTel:催缴电话</li>
 * <li>targetName:客户名称</li>
 * <li>billGroupDTO:账单组数据，参考{@link com.everhomes.rest.asset.BillGroupDTO}</li>
 * <li>isSettled:是否是已出账单,1:新增已出账单;0:新增未出账单</li>
 * <li>invoiceNum:发票单号</li>
 * <li>dateStrBegin:计费开始</li>
 * <li>dateStrEnd:计费结束</li>
 * <li>isOwed:是否欠费，0：正常；1：欠费</li>
 * <li>customerTel:客户手机号，用来滞后定位用户</li>
 * <li>assetPaymentBillAttachmentList: 附件数据，参考{@link com.everhomes.rest.asset.AssetPaymentBillAttachment}</li>
 *</ul>
 */
public class CreateBillCommand {
    private String ownerType;
    private Long ownerId;
    //private String noticeTel;
    private String targetName;
    private String targetType;
    private Long targetId;
    private Long contractId;
    private String contractNum;
    private String dateStr;
    @ItemType(BillGroupDTO.class)
    private BillGroupDTO billGroupDTO;
    private Byte isSettled;
    private String invoiceNum;
    private Long categoryId;
    //催缴手机号码列表
    private List<String> noticeTelList;
    //新增附件
    private List<AssetPaymentBillAttachment> assetPaymentBillAttachmentList;

    public List<String> getNoticeTelList() {
		return noticeTelList;
	}

	public void setNoticeTelList(List<String> noticeTelList) {
		this.noticeTelList = noticeTelList;
	}

	public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }
    private String dateStrBegin;
    private String dateStrEnd;
    private Byte isOwed;
    private String customerTel;
    private String addresses;

    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    public Byte getIsOwed() {
        return isOwed;
    }

    public void setIsOwed(Byte isOwed) {
        this.isOwed = isOwed;
    }

    public String getDateStrBegin() {
        return dateStrBegin;
    }

    public void setDateStrBegin(String dateStrBegin) {
        this.dateStrBegin = dateStrBegin;
    }

    public String getDateStrEnd() {
        return dateStrEnd;
    }

    public void setDateStrEnd(String dateStrEnd) {
        this.dateStrEnd = dateStrEnd;
    }

    public CreateBillCommand() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Byte getIsSettled() {
        return isSettled;
    }

    public void setIsSettled(Byte isSettled) {
        this.isSettled = isSettled;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public BillGroupDTO getBillGroupDTO() {
        return billGroupDTO;
    }

    public void setBillGroupDTO(BillGroupDTO billGroupDTO) {
        this.billGroupDTO = billGroupDTO;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

	public String getAddresses() {
		return addresses;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}

	public List<AssetPaymentBillAttachment> getAssetPaymentBillAttachmentList() {
		return assetPaymentBillAttachmentList;
	}

	public void setAssetPaymentBillAttachmentList(List<AssetPaymentBillAttachment> assetPaymentBillAttachmentList) {
		this.assetPaymentBillAttachmentList = assetPaymentBillAttachmentList;
	}

}
