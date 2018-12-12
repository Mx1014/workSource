// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.rest.visitorsys.VisitorsysApprovalFormItem;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysVisitors;
import com.everhomes.util.StringHelper;

import java.util.List;

public class VisitorSysVisitor extends EhVisitorSysVisitors {
	
	private static final long serialVersionUID = -1090547867158368768L;
	private List<VisitorsysApprovalFormItem> communityFormValues;
	private List<VisitorsysApprovalFormItem> enterpriseFormValues;
	private String statsDate;
	private Integer statsHour;
	private Integer statsWeek;

	private Long doorAccessEndTime;

	//  扫码登记或ipad自助登记标志
	private Byte fromDevice;

	public List<VisitorsysApprovalFormItem> getCommunityFormValues() {
		return communityFormValues;
	}

	public void setCommunityFormValues(List<VisitorsysApprovalFormItem> communityFormValues) {
		this.communityFormValues = communityFormValues;
	}

	public List<VisitorsysApprovalFormItem> getEnterpriseFormValues() {
		return enterpriseFormValues;
	}

	public void setEnterpriseFormValues(List<VisitorsysApprovalFormItem> enterpriseFormValues) {
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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getDoorAccessEndTime() {
		return doorAccessEndTime;
	}

	public void setDoorAccessEndTime(Long doorAccessEndTime) {
		this.doorAccessEndTime = doorAccessEndTime;
	}

	public Byte getFromDevice() {
		return fromDevice;
	}

	public void setFromDevice(Byte fromDevice) {
		this.fromDevice = fromDevice;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}