package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>员工授权每月额度信息</p>
 * <ul>
 * <li>userId:用户uid</li>
 * <li>detailId: 员工档案id  </li>
 * <li>contactName: 员工姓名</li>
 * <li>limitAmount: 每月额度</li>
 * <li>usedAmount: 本月已使用额度</li>
 * <li>currentMonthRemainAmount: 当月余额</li>
 * <li>paymentAuthSceneCount: 支付场景数量</li>
 * <li>paymentSceneLimitDTOS: 场景信息，参考{@link com.everhomes.rest.enterprisepaymentauth.EmployeePaymentSceneLimitDTO}</li>
 * </ul>
 */
public class GetEmployeePaymentAuthDetailResponse {
    private Long userId;
    private Long detailId;
    private String contactName;
    private BigDecimal limitAmount;
    private BigDecimal usedAmount;
    private BigDecimal currentMonthRemainAmount;
    private Integer paymentAuthSceneCount;

    private List<EmployeePaymentSceneLimitDTO> paymentSceneLimitDTOS;

    public GetEmployeePaymentAuthDetailResponse() {
        this.currentMonthRemainAmount = BigDecimal.ZERO;
        this.limitAmount = BigDecimal.ZERO;
        this.usedAmount = BigDecimal.ZERO;
        this.paymentAuthSceneCount = 0;
    }

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

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }

    public BigDecimal getCurrentMonthRemainAmount() {
        return currentMonthRemainAmount;
    }

    public void setCurrentMonthRemainAmount(BigDecimal currentMonthRemainAmount) {
        this.currentMonthRemainAmount = currentMonthRemainAmount;
    }

    public Integer getPaymentAuthSceneCount() {
        return paymentAuthSceneCount;
    }

    public void setPaymentAuthSceneCount(Integer paymentAuthSceneCount) {
        this.paymentAuthSceneCount = paymentAuthSceneCount;
    }

    public List<EmployeePaymentSceneLimitDTO> getPaymentSceneLimitDTOS() {
        return paymentSceneLimitDTOS;
    }

    public void setPaymentSceneLimitDTOS(List<EmployeePaymentSceneLimitDTO> paymentSceneLimitDTOS) {
        this.paymentSceneLimitDTOS = paymentSceneLimitDTOS;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
