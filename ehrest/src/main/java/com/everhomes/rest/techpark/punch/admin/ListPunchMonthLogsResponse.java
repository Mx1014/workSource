package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchLogsDay;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 
 * <li>userName：用户姓名</li>
 * <li>deptName：部门名称</li>   
 * <li>punchLogsDayList: 每天的打卡状态  {@link com.everhomes.rest.techpark.punch.PunchLogsDay} </li>
 * <li>nextPageAnchor: 下页anchor</li>
 * </ul>
 */
public class ListPunchMonthLogsResponse {

	private String userName;
	private String deptName;
	@ItemType(PunchLogsDay.class)
	private List<PunchLogsDay> punchLogsDayList;

	private Long nextPageAnchor;


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public List<PunchLogsDay> getPunchLogsDayList() {
		return punchLogsDayList;
	}

	public void setPunchLogsDayList(List<PunchLogsDay> punchLogsDayList) {
		this.punchLogsDayList = punchLogsDayList;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	
}
