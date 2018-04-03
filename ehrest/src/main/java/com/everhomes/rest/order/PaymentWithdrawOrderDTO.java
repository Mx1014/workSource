package com.everhomes.rest.order;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间ID</li>
 *     <li>ownerType: 帐号类型，如EhOrganizations, EhUsers，{@link com.everhomes.rest.order.OwnerType}</li>
 *     <li>ownerId: 帐号ID， 如企业ID、用户ID</li>
 *     <li>orderNo: 提现订单号</li>
 *     <li>amount: 提现金额，单位分</li>
 *     <li>status: 订单状态，{@link com.everhomes.rest.order.PaymentWithdrawOrderStatus}</li>
 *     <li>enterpriseName：企业名称</li>
 *     <li>operatorUid：提现人ID</li>
 *     <li>operatorName：提现人名称</li>
 *     <li>operatorPhone：提现人手机号</li>
 *     <li>operateTime：提现时间</li>
 *     <li>callbackTime：提现完成时间</li>
 * </ul>
 */
public class PaymentWithdrawOrderDTO {
    private Integer namespaceId;
    
    private String ownerType;
    
    private Long ownerId;
    
    private Long orderNo;
    
    private Long amount;
    
    private Byte status;
    
    private String enterpriseName;
    
    private String operatorUid;
    
    private String operatorName;
    
    private String operatorPhone;
    
    private Timestamp operateTime;
    
    private Timestamp callbackTime;
    
    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(String operatorUid) {
        this.operatorUid = operatorUid;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorPhone() {
        return operatorPhone;
    }

    public void setOperatorPhone(String operatorPhone) {
        this.operatorPhone = operatorPhone;
    }

    public Timestamp getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Timestamp operateTime) {
        this.operateTime = operateTime;
    }

    public Timestamp getCallbackTime() {
        return callbackTime;
    }

    public void setCallbackTime(Timestamp callbackTime) {
        this.callbackTime = callbackTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
