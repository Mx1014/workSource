//@formatter:off
package com.everhomes.rest.asset;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * @author change by yangcx
 * @date 2018年5月19日----下午8:18:48
 */
/**
 *<ul>
 * <li>userId:用户ID</li>
 * <li>orderNo: 支付流水号，如：WUF00000000000001098</li> 
 * <li>paymentOrderNum:订单编号，如：954650447962984448，订单编号为缴费中交易明细与电商系统中交易明细串联起来的唯一标识。</li>
 * <li>payTime:交易时间（缴费时间）</li>
 * <li>paymentType:支付方式，微信/支付宝/对公转账</li>
 * <li>transactionType:交易类型，如：手续费/充值/提现/退款等</li>
 * <li>orderAmount:交易金额</li>
 * <li>amount:入账金额</li>
 * <li>feeAmount:手续费</li>
 * <li>paymentStatus:支付状态: 0未支付,1支付成功,2挂起,3失败
 * 		转换为业务系统的订单状态：已完成/订单异常</li>
 * <li>settlementStatus:结算状态：已结算/待结算</li> 
 * <li>orderRemark1:业务系统自定义字段（要求传订单来源）：wuyeCode</li>
 * <li>orderRemark2:业务系统自定义字段（对应eh_asset_payment_order表的id）</li>
 * <li>paymentOrderId:</li>
 * <li>payerName:缴费人</li>
 * <li>payerTel:缴费人电话</li>
 * <li>billId:账单id</li>
 * <li>dateStrBegin:账单开始时间，参与排序</li>
 * <li>dateStrEnd:账单结束时间，参与排序</li>
 * <li>billGroupName:账单组名称</li>
 * <li>targetName:客户名称</li>
 * <li>targetType:客户类型</li>
 * <li>amountReceivable:应收(元),应收=应收-减免+增收</li>
 * <li>amountReceived:实收(元)</li>
 * <li>amountExemption:减免(元)</li>
 * <li>amountSupplement:增收(元)</li>
 * <li>buildingName:楼栋名称</li>
 * <li>apartmentName:门牌名称</li>
 * <li>addresses:组装的多个楼栋门牌，如：金融基地/1205,金融基地/1206</li>
 * <li>billItemDTOList:账单组收费项目的集合，参考{@link com.everhomes.rest.asset.BillItemDTO}</li>
 * <li>exemptionItemDTOList:减免项集合，参考{@link com.everhomes.rest.asset.ExemptionItemDTO}</li>
 * <li>state:前端触发事件使用，1是外层，2是内层(children代表内层)</li>
 *</ul>
 */
public class PaymentOrderBillDTO {
    private Long userId;
    private String orderNo;
    private String paymentOrderNum;
    private String payTime;
    private Integer paymentType;
    private Integer transactionType;
    private BigDecimal orderAmount;
    private BigDecimal amount;
    private BigDecimal feeAmount;
    private Integer paymentStatus;
    private Integer settlementStatus;
    private String orderRemark1;
    private Long paymentOrderId;
    private String orderRemark2;
    private String orderSource;
    private String payerName;
    private String payerTel;
    
    private Long billId;
    private String dateStrBegin;
    private String dateStrEnd;
    private String billGroupName;
    private String targetName;
    private String targetType;
    private BigDecimal amountReceivable;
    private BigDecimal amountReceived;
    private BigDecimal amountExemption;
    private BigDecimal amountSupplement;
    private String buildingName;
    private String apartmentName;
    private String addresses;
    @ItemType(BillItemDTO.class)
    private List<BillItemDTO> billItemDTOList;
    @ItemType(ExemptionItemDTO.class)
    private List<ExemptionItemDTO> exemptionItemDTOList;
    
    private Byte state;
    
    public String getPayerTel() {
        return payerTel;
    }

    public void setPayerTel(String payerTel) {
        this.payerTel = payerTel;
    }

    public String getOrderRemark2() {
        return orderRemark2;
    }

    public void setOrderRemark2(String orderRemark2) {
        this.orderRemark2 = orderRemark2;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getPaymentOrderNum() {
        return paymentOrderNum;
    }
    public void setPaymentOrderNum(String paymentOrderNum) {
        this.paymentOrderNum = paymentOrderNum;
    }
    public Integer getPaymentType() {
        return paymentType;
    }
    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }
    public Integer getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }
    public BigDecimal getOrderAmount() {
        return orderAmount;
    }
    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public BigDecimal getFeeAmount() {
        return feeAmount;
    }
    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }
    public Integer getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    public Integer getSettlementStatus() {
        return settlementStatus;
    }
    public void setSettlementStatus(Integer settlementStatus) {
        this.settlementStatus = settlementStatus;
    }
    public String getOrderRemark1() {
        return orderRemark1;
    }
    public void setOrderRemark1(String orderRemark1) {
        this.orderRemark1 = orderRemark1;
    }
    public Long getPaymentOrderId() {
        return paymentOrderId;
    }
    public void setPaymentOrderId(Long paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
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

	public String getBillGroupName() {
		return billGroupName;
	}

	public void setBillGroupName(String billGroupName) {
		this.billGroupName = billGroupName;
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
		this.amountReceivable = amountReceivable;
	}

	public BigDecimal getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(BigDecimal amountReceived) {
		this.amountReceived = amountReceived;
	}

	public String getBuildingName() {
		return buildingName;
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

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public List<BillItemDTO> getBillItemDTOList() {
		return billItemDTOList;
	}

	public void setBillItemDTOList(List<BillItemDTO> billItemDTOList) {
		this.billItemDTOList = billItemDTOList;
	}

	public List<ExemptionItemDTO> getExemptionItemDTOList() {
		return exemptionItemDTOList;
	}

	public void setExemptionItemDTOList(List<ExemptionItemDTO> exemptionItemDTOList) {
		this.exemptionItemDTOList = exemptionItemDTOList;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public BigDecimal getAmountSupplement() {
		return amountSupplement;
	}

	public void setAmountSupplement(BigDecimal amountSupplement) {
		this.amountSupplement = amountSupplement;
	}

	public String getAddresses() {
		return addresses;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}

	public BigDecimal getAmountExemption() {
		return amountExemption;
	}

	public void setAmountExemption(BigDecimal amountExemption) {
		this.amountExemption = amountExemption;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}
}
