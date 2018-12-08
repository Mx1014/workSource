//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>namespaceId:域空间ID</li>
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
 * <li>sourceType:各个业务系统定义的唯一标识</li>
 * <li>sourceId:各个业务系统定义的唯一标识</li>
 * <li>sourceName:账单来源（如：停车缴费）</li>
 * <li>consumeUserId:记账人ID</li>
 * <li>consumeUserName:记账人名称</li>
 * <li>thirdBillId:各个业务系统定义的唯一账单标识</li>
 * <li>merchantOrderId:统一订单定义的唯一标识</li>
 * <li>deleteFlag:删除状态：0：已删除；1：正常使用</li>
 * <li>canDelete:0：不可删除；1：可删除</li>
 * <li>canModify:0：不可编辑；1：可编辑</li>
 * <li>canMergeBillItem:true：可以合并明细；false：可编辑</li>
 *</ul>
 */
public class CreateBillCommand {
	private Integer namespaceId;
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

    //新增账单来源信息
    private String sourceType;
    private Long sourceId;
    private String sourceName;
    private Long consumeUserId;
    private String consumeUserName;
    private String thirdBillId;
    //物业缴费V6.0 账单、费项表增加是否删除状态字段
    private Byte deleteFlag;
    //物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
    private Byte canDelete;
    private Byte canModify;
    //物业缴费V6.0 将“新增账单”改为“新增账单、批量导入”权限；
    private Long organizationId;
    //物业缴费V7.1 统一账单加入的：统一订单定义的唯一标识
    private String merchantOrderId;
    //物业缴费v7.4 瑞安对接合并同账期账单至明细
    private Boolean canMergeBillItem = false;
    
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

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public Long getConsumeUserId() {
		return consumeUserId;
	}

	public void setConsumeUserId(Long consumeUserId) {
		this.consumeUserId = consumeUserId;
	}

	public Byte getCanDelete() {
		return canDelete;
	}

	public void setCanDelete(Byte canDelete) {
		this.canDelete = canDelete;
	}

	public Byte getCanModify() {
		return canModify;
	}

	public void setCanModify(Byte canModify) {
		this.canModify = canModify;
	}

	public Byte getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Byte deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getThirdBillId() {
		return thirdBillId;
	}

	public void setThirdBillId(String thirdBillId) {
		this.thirdBillId = thirdBillId;
	}

	public String getMerchantOrderId() {
		return merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}

	public String getConsumeUserName() {
		return consumeUserName;
	}

	public void setConsumeUserName(String consumeUserName) {
		this.consumeUserName = consumeUserName;
	}

    public Boolean getCanMergeBillItem() {
        return canMergeBillItem;
    }

    public void setCanMergeBillItem(Boolean canMergeBillItem) {
        this.canMergeBillItem = canMergeBillItem;
    }
}
