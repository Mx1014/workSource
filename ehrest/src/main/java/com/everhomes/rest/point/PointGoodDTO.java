package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>number: 商品编号</li>
 *     <li>displayName: 名称</li>
 *     <li>posterUri: 封面uri</li>
 *     <li>posterUrl: 封面url</li>
 *     <li>detailUrl: 商品url</li>
 *     <li>points: 可使用积分数量</li>
 *     <li>soldAmount: 销量</li>
 *     <li>originalPrice: 原始价格</li>
 *     <li>discountPrice: 折扣价格</li>
 *     <li>pointRuleDescription: 积分使用规则描述</li>
 *     <li>status: status</li>
 *     <li>topTime: topTime</li>
 *     <li>createTime: createTime</li>
 *     <li>updateTime: updateTime</li>
 * </ul>
 */
public class PointGoodDTO {
    private Long id;
    private Integer namespaceId;
    private String number;
    private String displayName;
    private String posterUri;
    private String posterUrl;
    private String detailUrl;
    private Long points;
    private Long soldAmount;
    private BigDecimal originalPrice;
    private BigDecimal discountPrice;
    private String pointRuleDescription;
    private Byte status;
    private Timestamp topTime;
    private Timestamp createTime;
    private Timestamp updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(String posterUri) {
        this.posterUri = posterUri;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Long getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(Long soldAmount) {
        this.soldAmount = soldAmount;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getTopTime() {
        return topTime;
    }

    public void setTopTime(Timestamp topTime) {
        this.topTime = topTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getPointRuleDescription() {
        return pointRuleDescription;
    }

    public void setPointRuleDescription(String pointRuleDescription) {
        this.pointRuleDescription = pointRuleDescription;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
