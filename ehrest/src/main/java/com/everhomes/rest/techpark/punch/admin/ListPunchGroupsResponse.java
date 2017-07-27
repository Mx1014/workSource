package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.techpark.punch.PunchRuleMapDTO;
import com.everhomes.rest.techpark.punch.PunchTimeRuleDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出资源列表返回值(根据图标和园区)
 * <li>nextPageAnchor: 分页，下一页锚点</li>
 * <li>punchGroups: 结果{@link com.everhomes.rest.techpark.punch.admin.PunchGroupDTO}</li>
 * <li>unjoinPunchGroupEmployees: 未设置考勤的员工列表 {@link com.everhomes.rest.organization.OrganizationMemberDTO}</li>
 * <li>allEmployeeCount: 总人数</li>
 * </ul>
 */
public class ListPunchGroupsResponse {
	private Long nextPageAnchor;

	@ItemType(PunchGroupDTO.class)
	private List<PunchGroupDTO> punchGroups;

	@ItemType(OrganizationMemberDTO.class)
	private List<OrganizationMemberDTO> unjoinPunchGroupEmployees;
	
	private Integer allEmployeeCount;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<PunchGroupDTO> getPunchGroups() {
		return punchGroups;
	}

	public void setPunchGroups(List<PunchGroupDTO> punchGroups) {
		this.punchGroups = punchGroups;
	}

	public Integer getAllEmployeeCount() {
		return allEmployeeCount;
	}

	public void setAllEmployeeCount(Integer allEmployeeCount) {
		this.allEmployeeCount = allEmployeeCount;
	}

	public List<OrganizationMemberDTO> getUnjoinPunchGroupEmployees() {
		return unjoinPunchGroupEmployees;
	}

	public void setUnjoinPunchGroupEmployees(List<OrganizationMemberDTO> unjoinPunchGroupEmployees) {
		this.unjoinPunchGroupEmployees = unjoinPunchGroupEmployees;
	}
 
 

}
