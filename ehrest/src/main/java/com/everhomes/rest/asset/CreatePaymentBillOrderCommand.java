//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.order.PaymentParamsDTO;
import com.everhomes.util.StringHelper;

import java.util.List;
/**
 *<ul>
 * <li>namespaceId: 域空间</li>
 * <li>communityId: 小区ID</li>
 * <li>clientAppName: 客户端名称</li>
 * <li>billGroupId: 帐单组ID</li>
 * <li>contactNumber: 合同编号</li>
 * <li>amount: 缴纳金额，单位元</li>
 * <li>businessOrderType: 订单类型，参考统一订单系统里的定义{@link com.everhomes.rest.promotion.order.BusinessOrderType}</li>
 * <li>businessPayerType: 付款方的类型，参考统一订单系统里的定义{@link com.everhomes.rest.promotion.order.BusinessPayerType}</li>
 * <li>businessPayerId: 付款方的id，如个人ID、企业ID</li>
 * <li>businessPayerName: 付款方名称</li>
 * <li>paymentType: 支付方式，微信公众号支付方式必填，9-公众号支付，参考{@link com.everhomes.rest.order.PaymentType}</li>
 * <li>sourceType: 支付来源，如手机或PC之类，参考{@link com.everhomes.pay.order.SourceType}</li>
 * <li>extendInfo: 扩展信息，传给支付系统而支付系统会原样返回</li>
 * <li>billIds: 账单id列表</li>
 * <li>pmsyOrderId: eh_pmsy_orders表的ID</li>
 *</ul>
 */
public class CreatePaymentBillOrderCommand {
    private Integer namespaceId;
    private Long communityId;
    private String clientAppName;
    private Long billGroupId;
    private String contactNumber;
    private String amount;
    private String businessOrderType;
    private String businessPayerType;
    private String businessPayerId;
    private String payerName;
    private Integer sourceType;
    private Integer paymentType;
    private String extendInfo;
    private PaymentParamsDTO paymentParams;
    
    @ItemType(BillIdAndAmount.class)
    private List<BillIdAndAmount> bills;
    
    private String pmsyOrderId;
    
    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getClientAppName() {
        return clientAppName;
    }

    public void setClientAppName(String clientAppName) {
        this.clientAppName = clientAppName;
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBusinessOrderType() {
        return businessOrderType;
    }

    public void setBusinessOrderType(String businessOrderType) {
        this.businessOrderType = businessOrderType;
    }

    public String getBusinessPayerType() {
        return businessPayerType;
    }

    public void setBusinessPayerType(String businessPayerType) {
        this.businessPayerType = businessPayerType;
    }

    public String getBusinessPayerId() {
        return businessPayerId;
    }

    public void setBusinessPayerId(String businessPayerId) {
        this.businessPayerId = businessPayerId;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }

    public PaymentParamsDTO getPaymentParams() {
        return paymentParams;
    }

    public void setPaymentParams(PaymentParamsDTO paymentParams) {
        this.paymentParams = paymentParams;
    }

    public List<BillIdAndAmount> getBills() {
        return bills;
    }

    public void setBills(List<BillIdAndAmount> bills) {
        this.bills = bills;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getPmsyOrderId() {
		return pmsyOrderId;
	}

	public void setPmsyOrderId(String pmsyOrderId) {
		this.pmsyOrderId = pmsyOrderId;
	}
}
