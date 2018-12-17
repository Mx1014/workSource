//@formatter:off
package com.everhomes.rest.asset.bill;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.asset.BillItemDTO;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;
/**
 *<ul>
 * <li>defaultOrder:排序数字</li>
 * <li>dateStr:账期，格式为201706，参与排序</li>
 * <li>billId:账单id</li>
 * <li>billGroupName:账单组名称</li>
 * <li>targetName:客户名称</li>
 * <li>targetId:客户id</li>
 * <li>targetType:客户类型</li>
 * <li>contractId:合同id</li>
 * <li>contractNum:合同编号</li>
 * <li>buildingName:楼栋名称</li>
 * <li>apartmentName:门牌名称</li>
 * <li>noticeTel:催缴联系号码</li>
 * <li>amountReceivable:应收(元)</li>
 * <li>amountReceived:实收(元)</li>
 * <li>amountOwed:欠收(元)</li>+
 * <li>taxAmount:税额(元)</li>
 * <li>billStatus:账单状态，0:待缴;1:已缴</li>
 * <li>noticeTimes:已催缴次数</li>
 * <li>ownerId:所属者id</li>
 * <li>ownerType:所属者类型</li>
 * <li>payStatus:清账信息</li>
 * <li>paymentType:支付方式（0-线下缴费，1-微信支付，2-对公转账，8-支付宝支付）</li>
 * <li>invoiceNum:发票编号</li>
 * <li>dateStrBegin:账单开始时间，参与排序</li>
 * <li>dateStrEnd:账单结束时间，参与排序</li>
 * <li>customerTel:客户手机号</li>
 * <li>addresses:组装的多个楼栋门牌，如：金融基地/1205,金融基地/1206</li>
 * <li>dueDayCount:欠费天数</li>
 * <li>sourceType:各个业务系统定义的唯一标识</li>
 * <li>sourceId:各个业务系统定义的唯一标识</li>
 * <li>sourceName:账单来源（如：停车缴费）</li>
 * <li>consumeUserId:企业下面的某个人的ID</li>
 * <li>deleteFlag:删除状态：0：已删除；1：正常使用</li>
 * <li>canDelete:0：不可删除；1：可删除</li>
 * <li>canModify:0：不可编辑；1：可编辑</li>
 * <li>isReadOnly:只读状态：0：非只读；1：只读</li>
 * <li>billItemDTOList:账单组收费项目的集合，参考{@link com.everhomes.rest.asset.BillItemDTO}</li>
 * <li>accountId:物业缴费V7.4(瑞安项目-资产管理对接CM系统) : CM的客户ID</li>
 *</ul>
 */
public class ListBillsDTO {
    private Integer defaultOrder;
    private String dateStr;
    private String billId;
    private String billGroupName;
    private String targetName;
    private String targetId;
    private String contractId;
    private String contractNum;
    private String targetType;
    private String buildingName;
    private String apartmentName;
    private BigDecimal amountReceivable;
    private BigDecimal amountReceived;
    private BigDecimal amountOwed;
    private BigDecimal taxAmount;
    private Byte billStatus;
    private Integer noticeTimes;
    private String ownerId;
    private String ownerType;
    private String payStatus;
    // introduce invoice num for bill display. by wentian 2018/4/16 on earth
    private String invoiceNum;
    private Integer paymentType;

    private String dateStrBegin;
    private String dateStrEnd;
    private String customerTel;
    private String addresses;
    
    private Long dueDayCount;//欠费天数
    
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
    //瑞安CM对接 账单、费项表增加是否是只读字段
    private Byte isReadOnly;
    //催缴手机号码列表
    private List<String> noticeTelList;
    @ItemType(BillItemDTO.class)
    private List<BillItemDTO> billItemDTOList;
    private String accountId;//物业缴费V7.4(瑞安项目-资产管理对接CM系统) : CM的客户ID
    private String propertyID;//物业缴费V7.4(瑞安项目-资产管理对接CM系统) : CM的项目ID
    private String thirdErrorDescription;//瑞安项目专用、同瑞安CM系统服务账单同步失败的账单展示

    public List<String> getNoticeTelList() {
		return noticeTelList;
	}

	public void setNoticeTelList(List<String> noticeTelList) {
		this.noticeTelList = noticeTelList;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }



    public String getBillGroupName() {
        return billGroupName;
    }

    public void setBillGroupName(String billGroupName) {
        this.billGroupName = billGroupName;
    }

    public Byte getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Byte billStatus) {
        this.billStatus = billStatus;
    }

    public Integer getNoticeTimes() {
        return noticeTimes;
    }

    public void setNoticeTimes(Integer noticeTimes) {
        this.noticeTimes = noticeTimes;
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

    public BigDecimal getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(BigDecimal amountReceivable) {
        amountReceivable = amountReceivable.setScale(2,BigDecimal.ROUND_CEILING);
        this.amountReceivable = amountReceivable;
    }

    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(BigDecimal amountReceived) {
        amountReceived = amountReceived.setScale(2,BigDecimal.ROUND_CEILING);
        this.amountReceived = amountReceived;
    }

    public BigDecimal getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(BigDecimal amountOwed) {
        amountOwed = amountOwed.setScale(2,BigDecimal.ROUND_CEILING);
        this.amountOwed = amountOwed;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public ListBillsDTO() {

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

	public String getCustomerTel() {
		return customerTel;
	}

	public void setCustomerTel(String customerTel) {
		this.customerTel = customerTel;
	}

	public String getAddresses() {
		return addresses;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}

	public Long getDueDayCount() {
		return dueDayCount;
	}

	public void setDueDayCount(Long dueDayCount) {
		this.dueDayCount = dueDayCount;
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

	public Byte getIsReadOnly() {
		return isReadOnly;
	}

	public void setIsReadOnly(Byte isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public List<BillItemDTO> getBillItemDTOList() {
		return billItemDTOList;
	}

	public void setBillItemDTOList(List<BillItemDTO> billItemDTOList) {
		this.billItemDTOList = billItemDTOList;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getPropertyID() {
		return propertyID;
	}

	public void setPropertyID(String propertyID) {
		this.propertyID = propertyID;
	}

	public String getThirdErrorDescription() {
		return thirdErrorDescription;
	}

	public void setThirdErrorDescription(String thirdErrorDescription) {
		this.thirdErrorDescription = thirdErrorDescription;
	}

}
