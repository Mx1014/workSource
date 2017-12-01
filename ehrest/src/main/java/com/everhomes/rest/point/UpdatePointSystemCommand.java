package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>displayName: 积分系统名称</li>
 *     <li>pointName: 积分名称</li>
 *     <li>pointExchangeFlag: 积分兑换flag{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 *     <li>exchangePoint: 兑换积分数</li>
 *     <li>exchangeCash: 兑换现金数</li>
 *     <li>userAgreement: 用户须知</li>
 * </ul>
 */
public class UpdatePointSystemCommand {

    @NotNull
    private Long id;
    private String displayName;
    private String pointName;
    private Byte pointExchangeFlag;
    private Integer exchangePoint;
    private Integer exchangeCash;
    private String userAgreement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
