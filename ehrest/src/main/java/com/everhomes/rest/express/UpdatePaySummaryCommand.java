// @formatter:off
package com.everhomes.rest.express;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>id: 快递id</li>
 * <li>paySummary: 付费总计</li>
 * </ul>
 */
public class UpdatePaySummaryCommand {

	private String ownerType;

	private Long ownerId;

	private Long id;

	private BigDecimal paySummary;

	public UpdatePaySummaryCommand() {

	}

	public UpdatePaySummaryCommand(String ownerType, Long ownerId, Long id, BigDecimal paySummary) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.id = id;
		this.paySummary = paySummary;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getPaySummary() {
		return paySummary;
	}

	public void setPaySummary(BigDecimal paySummary) {
		this.paySummary = paySummary;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
