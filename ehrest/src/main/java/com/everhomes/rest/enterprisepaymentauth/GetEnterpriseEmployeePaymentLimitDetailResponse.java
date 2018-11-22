// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 * <li>userId: 用户id</li>
 * <li>detailId: 用户detailId</li>
 * <li>contactAvatar: 头像url</li>
 * <li>contactName: 姓名</li>
 * <li>departmentId: 部门id</li>
 * <li>departmentName: 部门</li>
 * <li>currentMonthRemainAmount: 本月余额 </li>
 * <li>limitAmount: 每月总额 </li>
 * <li>historicalTotalPayAmount: 历史累计支付金额</li>
 * <li>historicalPayCount: 历史累计支付笔数</li>
 * <li>paymentSceneCount: 已授权支付场景数</li>
 * <li>sceneAuths: 场景授权列表 参考{@link com.everhomes.rest.enterprisepaymentauth.PaymentAuthSceneDTO}</li>
 * </ul>
 */
public class GetEnterpriseEmployeePaymentLimitDetailResponse {
    private Long userId;
    private Long detailId;
    private String contactName;
    private String contactAvatar;
    private Long departmentId;
    private String departmentName;
    private BigDecimal currentMonthRemainAmount;
    private BigDecimal limitAmount;
    private BigDecimal historicalTotalPayAmount;
    private Integer historicalPayCount;
    private Integer paymentSceneCount;
    private List<PaymentAuthSceneDTO> sceneAuths;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactAvatar() {
        return contactAvatar;
    }

    public void setContactAvatar(String contactAvatar) {
        this.contactAvatar = contactAvatar;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public BigDecimal getCurrentMonthRemainAmount() {
        return currentMonthRemainAmount;
    }

    public void setCurrentMonthRemainAmount(BigDecimal currentMonthRemainAmount) {
        this.currentMonthRemainAmount = currentMonthRemainAmount;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public BigDecimal getHistoricalTotalPayAmount() {
        return historicalTotalPayAmount;
    }

    public void setHistoricalTotalPayAmount(BigDecimal historicalTotalPayAmount) {
        this.historicalTotalPayAmount = historicalTotalPayAmount;
    }

    public Integer getHistoricalPayCount() {
        return historicalPayCount;
    }

    public void setHistoricalPayCount(Integer historicalPayCount) {
        this.historicalPayCount = historicalPayCount;
    }

    public Integer getPaymentSceneCount() {
        return paymentSceneCount;
    }

    public void setPaymentSceneCount(Integer paymentSceneCount) {
        this.paymentSceneCount = paymentSceneCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<PaymentAuthSceneDTO> getSceneAuths() {
        return sceneAuths;
    }

    public void setSceneAuths(List<PaymentAuthSceneDTO> sceneAuths) {
        this.sceneAuths = sceneAuths;
    }
}
