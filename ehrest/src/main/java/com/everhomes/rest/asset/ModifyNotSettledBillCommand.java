//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>billId:账单id</li>
 * <li>billGroupId:账单组id</li>
 * <li>targetId:客户id</li>
 * <li>targetType:客户type</li>
 * <li>targetName:客户名称</li>
 * <li>billGroupDTO:账单组，参考{@link com.everhomes.rest.asset.BillGroupDTO}</li>
 * <li>invoiceNum:发票编号</li>
 * <li>noticeTel:催缴联系号码</li>
 * <li>customerTel:客户手机号</li>
 * <li>categoryId:多应用入口区分标识</li>
 * <li>ownerType:所属者类型</li>
 * <li>ownerId:所属者ID</li>
 * <li>assetPaymentBillAttachmentList: 附件数据，参考{@link com.everhomes.rest.asset.AssetPaymentBillAttachment}</li>
 * <li>contractNum: 合同编号</li>
 * <li>contractId: 合同ID</li>
 * <li>dateStrBegin:计费开始</li>
 * <li>dateStrEnd:计费结束</li>
 * <li>organizationId：当前登陆的企业ID</li>
 *</ul>
 */
public class ModifyNotSettledBillCommand {
    private Long billId;
    private Long billGroupId;
    private Long targetId;
    private String targetType;
    private String targetName;
    @ItemType(BillGroupDTO.class)
    private BillGroupDTO billGroupDTO;
    private String invoiceNum;
    //private String noticeTel;
    private String customerTel;
    private Long categoryId;
    private String ownerType;
    private Long ownerId;
    //新增附件
    private List<AssetPaymentBillAttachment> assetPaymentBillAttachmentList;
    private String contractNum;
    private Long contractId;
    private String dateStrBegin;
    private String dateStrEnd;
    //物业缴费V6.0 新增权限
    private Long organizationId;
    //催缴手机号码列表
    private List<String> noticeTelList;

	public List<String> getNoticeTelList() {
		return noticeTelList;
	}

	public void setNoticeTelList(List<String> noticeTelList) {
		this.noticeTelList = noticeTelList;
	}

	public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


    public Long getBillGroupId() {
        return billGroupId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public BillGroupDTO getBillGroupDTO() {
        return billGroupDTO;
    }

    public void setBillGroupDTO(BillGroupDTO billGroupDTO) {
        this.billGroupDTO = billGroupDTO;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public ModifyNotSettledBillCommand() {
    }

	public String getCustomerTel() {
		return customerTel;
	}

	public void setCustomerTel(String customerTel) {
		this.customerTel = customerTel;
	}

	public List<AssetPaymentBillAttachment> getAssetPaymentBillAttachmentList() {
		return assetPaymentBillAttachmentList;
	}

	public void setAssetPaymentBillAttachmentList(List<AssetPaymentBillAttachment> assetPaymentBillAttachmentList) {
		this.assetPaymentBillAttachmentList = assetPaymentBillAttachmentList;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
}
