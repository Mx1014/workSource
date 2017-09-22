package com.everhomes.techpark.expansion;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.server.schema.tables.pojos.EhLeasePromotions;

public class LeasePromotion extends EhLeasePromotions {

	private static final long serialVersionUID = -5199936376319201329L;

	private Long communityId;
	private String posterUrl;

	private BigDecimal startRentArea;
	private BigDecimal endRentArea;
	private BigDecimal startRentAmount;
	private BigDecimal endRentAmount;

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public java.lang.String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(java.lang.String posterUrl) {
		this.posterUrl = posterUrl;
	}

	public BigDecimal getStartRentArea() {
		return startRentArea;
	}

	public void setStartRentArea(BigDecimal startRentArea) {
		this.startRentArea = startRentArea;
	}

	public BigDecimal getEndRentArea() {
		return endRentArea;
	}

	public void setEndRentArea(BigDecimal endRentArea) {
		this.endRentArea = endRentArea;
	}

	public BigDecimal getStartRentAmount() {
		return startRentAmount;
	}

	public void setStartRentAmount(BigDecimal startRentAmount) {
		this.startRentAmount = startRentAmount;
	}

	public BigDecimal getEndRentAmount() {
		return endRentAmount;
	}

	public void setEndRentAmount(BigDecimal endRentAmount) {
		this.endRentAmount = endRentAmount;
	}
}
