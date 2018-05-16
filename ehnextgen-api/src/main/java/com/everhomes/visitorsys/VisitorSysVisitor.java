// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysVisitors;
import com.everhomes.util.StringHelper;

import java.util.List;

public class VisitorSysVisitor extends EhVisitorSysVisitors {
	
	private static final long serialVersionUID = -1090547867158368768L;
	private Long enterpriseId;
	private List<PostApprovalFormItem> communityFormValues;
	private List<PostApprovalFormItem> enterpriseFormValues;
	private String statsDate;
	private Integer statsHour;
	private Integer statsWeek;
	private Long communityId;

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public List<PostApprovalFormItem> getCommunityFormValues() {
		return communityFormValues;
	}

	public void setCommunityFormValues(List<PostApprovalFormItem> communityFormValues) {
		this.communityFormValues = communityFormValues;
	}

	public List<PostApprovalFormItem> getEnterpriseFormValues() {
		return enterpriseFormValues;
	}

	public void setEnterpriseFormValues(List<PostApprovalFormItem> enterpriseFormValues) {
		this.enterpriseFormValues = enterpriseFormValues;
	}

	public String getStatsDate() {
		return statsDate;
	}

	public void setStatsDate(String statsDate) {
		this.statsDate = statsDate;
	}

	public Integer getStatsHour() {
		return statsHour;
	}

	public void setStatsHour(Integer statsHour) {
		this.statsHour = statsHour;
	}

	public Integer getStatsWeek() {
		return statsWeek;
	}

	public void setStatsWeek(Integer statsWeek) {
		this.statsWeek = statsWeek;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}