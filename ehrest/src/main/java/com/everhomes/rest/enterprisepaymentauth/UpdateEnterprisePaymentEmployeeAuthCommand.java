// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import java.math.BigDecimal;


import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerId: 公司id</li>
 * <li>detailId: 员工detailId</li>
 * <li>employeeMonthLimitAmount: 人员每月限额 </li>
 * <li>sceneAtuhs: 场景授权列表 参考{@link com.everhomes.rest.enterprisepayment.ScenePaymentAuthDTO}</li>
 * </ul>
 */
public class UpdateEnterprisePaymentEmployeeAuthCommand {

	private Long ownerId;

	private Long detailId;

	private BigDecimal employeeMonthLimitAmount;

	@ItemType(PaymentAuthSceneDTO.class)
	private List<PaymentAuthSceneDTO> sceneAtuhs;

	public UpdateEnterprisePaymentEmployeeAuthCommand() {

	}

	public UpdateEnterprisePaymentEmployeeAuthCommand(Long ownerId, Long detailId, BigDecimal employeeMonthLimitAmount, List<PaymentAuthSceneDTO> sceneAtuhs) {
		super();
		this.ownerId = ownerId;
		this.detailId = detailId;
		this.employeeMonthLimitAmount = employeeMonthLimitAmount;
		this.sceneAtuhs = sceneAtuhs;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public BigDecimal getEmployeeMonthLimitAmount() {
		return employeeMonthLimitAmount;
	}

	public void setEmployeeMonthLimitAmount(BigDecimal employeeMonthLimitAmount) {
		this.employeeMonthLimitAmount = employeeMonthLimitAmount;
	}

	public List<PaymentAuthSceneDTO> getSceneAtuhs() {
		return sceneAtuhs;
	}

	public void setSceneAtuhs(List<PaymentAuthSceneDTO> sceneAtuhs) {
		this.sceneAtuhs = sceneAtuhs;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
