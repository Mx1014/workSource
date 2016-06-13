package com.everhomes.rest.banner;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: order id</li>
 * <li>bannerId: banner Id</li>
 * <li>uid: 用户id</li>
 * <li>vendorOrderTag: 左邻系统或第三方服务</li>
 * <li>clickCount: 点击次数</li>
 * <li>amount: 总价</li>
 * <li>description: 描述</li>
 * <li>purchaseTime: 购买时间</li>
 * <li>createTime: 创建时间</li>
 * </ul>
 */
public class BannerOrderDTO {
    private Long     id;
    private Long     bannerId;
    private Long     uid;
    private String   vendorOrderTag;
    private Long     amount;
    private String   description;
    private Timestamp purchaseTime;
    private Timestamp createTime;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getBannerId() {
        return bannerId;
    }
    public void setBannerId(Long bannerId) {
        this.bannerId = bannerId;
    }
    public Long getUid() {
        return uid;
    }
    public void setUid(Long uid) {
        this.uid = uid;
    }
    public String getVendorOrderTag() {
        return vendorOrderTag;
    }
    public void setVendorOrderTag(String vendorOrderTag) {
        this.vendorOrderTag = vendorOrderTag;
    }
    public Long getAmount() {
        return amount;
    }
    public void setAmount(Long amount) {
        this.amount = amount;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Timestamp getPurchaseTime() {
        return purchaseTime;
    }
    public void setPurchaseTime(Timestamp purchaseTime) {
        this.purchaseTime = purchaseTime;
    }
    public Timestamp getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
    @Override
    public String toString(){
        return StringHelper.toJsonString(this);
    }
}
