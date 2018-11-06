package com.everhomes.rest.pmtask;

import com.everhomes.discover.ItemType;

import java.util.List;


/**
 *<ul>
 * <li>clientAppName:客户端名称</li>
 * <li>taskId:物业报修任务Id</li>
 * <li>orderId:订单Id</li>
 * <li>paymentType:支付方式 {@link com.everhomes.pay.order.PaymentType}</li>
 * <li>payerType:支付者的类型，eh_user为个人，eh_organization为企业</li>
 * <li>payerId:支付者的id</li>
 * <li>payerName:支付者的名称</li>
 * <li>openid:微信标识</li>
 * <li>namespaceId：域空间</li>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 *</ul>
 */
public class CreatePmTaskOrderCommand {

    private Long taskId;
    private Long orderId;
    private String clientAppName;
    private Integer paymentType;
    private String payerType;
    private Long payerId;
    private String payerName;
    private String openid;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;

    private Long appId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getClientAppName() {
        return clientAppName;
    }

    public void setClientAppName(String clientAppName) {
        this.clientAppName = clientAppName;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public String getPayerType() {
        return payerType;
    }

    public void setPayerType(String payerType) {
        this.payerType = payerType;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

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

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
