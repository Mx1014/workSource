package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>number: number</li>
 *     <li>displayName: displayName</li>
 *     <li>posterUri: posterUri</li>
 *     <li>posterUrl: posterUrl</li>
 *     <li>detailUrl: detailUrl</li>
 *     <li>points: points</li>
 *     <li>soldAmount: soldAmount</li>
 *     <li>originalPrice: originalPrice</li>
 *     <li>discountPrice: discountPrice</li>
 *     <li>pointRule: pointRule</li>
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
    private Integer points;
    private Long soldAmount;
    private Long originalPrice;
    private Long discountPrice;
    private String pointRule;
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

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Long getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(Long soldAmount) {
        this.soldAmount = soldAmount;
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Long originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Long getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Long discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getPointRule() {
        return pointRule;
    }

    public void setPointRule(String pointRule) {
        this.pointRule = pointRule;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
