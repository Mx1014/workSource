package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>displayName: 积分系统名称</li>
 *     <li>pointName: 积分名称</li>
 *     <li>pointExchangeFlag: 积分兑换flag{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 *     <li>exchangePoint: 兑换积分数</li>
 *     <li>exchangeCash: 兑换现金数</li>
 *     <li>userAgreement: 用户须知</li>
 *     <li>status: status {@link com.everhomes.rest.point.PointCommonStatus}</li>
 *     <li>createTime: createTime</li>
 * </ul>
 */
public class PointSystemDTO {
    private Long id;
    private Integer namespaceId;
    private String displayName;
    private String pointName;
    private Byte pointExchangeFlag;
    private Integer exchangePoint;
    private Integer exchangeCash;
    private String userAgreement;
    private Byte status;
    private Timestamp createTime;

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public Byte getPointExchangeFlag() {
        return pointExchangeFlag;
    }

    public void setPointExchangeFlag(Byte pointExchangeFlag) {
        this.pointExchangeFlag = pointExchangeFlag;
    }

    public Integer getExchangePoint() {
        return exchangePoint;
    }

    public void setExchangePoint(Integer exchangePoint) {
        this.exchangePoint = exchangePoint;
    }

    public Integer getExchangeCash() {
        return exchangeCash;
    }

    public void setExchangeCash(Integer exchangeCash) {
        this.exchangeCash = exchangeCash;
    }

    public String getUserAgreement() {
        return userAgreement;
    }

    public void setUserAgreement(String userAgreement) {
        this.userAgreement = userAgreement;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
