//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Wentian Wang on 2017/8/18.
 */
/**
 *<ul>
 * <li>billId:账单id</li>
 * <li>dateStr:账期</li>
 * <li>dateStrBegin:账单开始时间，参与排序</li>
 * <li>dateStrEnd:账单结束时间，参与排序</li>
 * <li>contractNum:合同编号</li>
 * <li>buildingName: 楼栋名称</li>
 * <li>apartmentName: 门牌名称</li>
 * <li>addresses:组装的多个楼栋门牌，如：金融基地/1205,金融基地/1206</li>
 * <li>noticeTel:催缴电话</li>
 * <li>customerTel:客户手机号</li>
 * <li>targetName:客户名称</li>
 * <li>targetId:客户id</li>
 * <li>targetType:客户type</li>
 * <li>amountReceivable:应收(元)</li>
 * <li>amountReceived:实收(元)</li>
 * <li>amoutExemption:减免(元)</li>
 * <li>amountSupplement:增收(元)</li>
 * <li>billGroupDTO:账单组，包括减免项和收费项目的集合，参考{@link com.everhomes.rest.asset.BillGroupDTO}</li>
 * <li>billGroupName:账单组名称</li>
 * <li>assetPaymentBillAttachmentList: 附件数据，参考{@link com.everhomes.rest.asset.AssetPaymentBillAttachment}</li>
 * <li>sourceType:各个业务系统定义的唯一标识</li>
 * <li>sourceId:各个业务系统定义的唯一标识</li>
 * <li>sourceName:账单来源（如：停车缴费）</li>
 * <li>consumeUserId:企业下面的某个人的ID</li>
 * <li>deleteFlag:删除状态：0：已删除；1：正常使用</li>
 * <li>canDelete:0：不可删除；1：可删除</li>
 * <li>canModify:0：不可编辑；1：可编辑</li>
 *</ul>
 */
public class ListBillDetailVO {
    private Long billId;
    private Long billGroupId;
    private String billGroupName;
    private String dateStr;
    private String dateStrBegin;
    private String dateStrEnd;
    private String noticeTel;
    private String customerTel;
    private String targetName;
    private String targetType;
    private String buildingName;
    private String apartmentName;
    private String addresses;
    private Long targetId;
    @ItemType(BillGroupDTO.class)
    private BillGroupDTO billGroupDTO;
    private String contractNum;
    private String invoiceNum;
    private Byte billStatus;
    //新增附件
    private List<AssetPaymentBillAttachment> assetPaymentBillAttachmentList;
    private Long contractId;//新增合同ID字段
    //新增账单来源信息
    private String sourceType;
    private Long sourceId;
    private String sourceName;
    private Long consumeUserId;
    //物业缴费V6.0 账单、费项表增加是否删除状态字段
    private Byte deleteFlag;
    //物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
    private Byte canDelete;
    private Byte canModify;
    //对接统一账单业务线的需求
    private Integer paymentType;
    //催缴手机号码列表
    private List<String> noticeTelList;
    
    public List<String> getNoticeTelList() {
		return noticeTelList;
	}

	public void setNoticeTelList(List<String> noticeTelList) {
		this.noticeTelList = noticeTelList;
	}

	public Byte getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(Byte billStatus) {
		this.billStatus = billStatus;
	}

    private BigDecimal amountReceivable;
    private BigDecimal amountReceived;
    private BigDecimal amoutExemption;
    private BigDecimal amountSupplement;
    
    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }


    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }


    public String getNoticeTel() {
        return noticeTel;
    }

    public void setNoticeTel(String noticeTel) {
        this.noticeTel = noticeTel;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
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

    public BillGroupDTO getBillGroupDTO() {
        return billGroupDTO;
    }

    public void setBillGroupDTO(BillGroupDTO billGroupDTO) {
        this.billGroupDTO = billGroupDTO;
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

	public BigDecimal getAmountReceivable() {
		return amountReceivable;
	}

	public void setAmountReceivable(BigDecimal amountReceivable) {
		this.amountReceivable = amountReceivable;
	}

	public BigDecimal getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(BigDecimal amountReceived) {
		this.amountReceived = amountReceived;
	}

	public BigDecimal getAmoutExemption() {
		return amoutExemption;
	}

	public void setAmoutExemption(BigDecimal amoutExemption) {
		this.amoutExemption = amoutExemption;
	}

	public BigDecimal getAmountSupplement() {
		return amountSupplement;
	}

	public void setAmountSupplement(BigDecimal amountSupplement) {
		this.amountSupplement = amountSupplement;
	}

	public String getBillGroupName() {
		return billGroupName;
	}

	public void setBillGroupName(String billGroupName) {
		this.billGroupName = billGroupName;
	}

	public String getAddresses() {
		return addresses;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
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

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
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

	public Byte getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Byte deleteFlag) {
		this.deleteFlag = deleteFlag;
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

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}
}
