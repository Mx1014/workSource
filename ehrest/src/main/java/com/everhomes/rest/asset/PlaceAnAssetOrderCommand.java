//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/12.
 */
/**
 *<ul>
 * <li>clientAppName:客户端名称</li>
 * <li>billIds:账单id列表</li>
 * <li>contactNum:合同号</li>
 * <li>amountOwed:缴纳金额，单位元</li>
 * <li>communityId:园区id</li>
 * <li>payerType:支付者的类型，eh_user为个人，eh_organization为企业</li>
 * <li>payerId:支付者的id</li>
 * <li>payerName:支付者的名称</li>
 * <li>ownerType:所属者类型，通常为community</li>
 * <li>openid:微信标识</li>
 *</ul>
 */
public class PlaceAnAssetOrderCommand {
    private String clientAppName;
    @ItemType(String.class)
    private List<String> billIds;
    private String contactNum;
    private String amountOwed;
    private Long communityId;
    private String payerType;
    private String payerId;
    private String payerName;
    private String ownerType;
    private String openid;

    public String getClientAppName() {
        return clientAppName;
    }

    public void setClientAppName(String clientAppName) {
        this.clientAppName = clientAppName;
    }

    public List<String> getBillIds() {
        return billIds;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setBillIds(List<String> billIds) {
        this.billIds = billIds;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(String amountOwed) {
        this.amountOwed = amountOwed;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getPayerType() {
        return payerType;
    }

    public void setPayerType(String payerType) {
        this.payerType = payerType;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }
}
