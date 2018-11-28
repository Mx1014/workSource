package com.everhomes.rest.techpark.punch;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>punchCountList：考勤统计信息列表，参考{@link com.everhomes.rest.techpark.punch.PunchCountDTO}</li>
 * <li>nextPageAnchor: 下页anchor</li>
 * <li>extColumns: String list 扩展列的列名集合</li>
 * <li>dateList: String list 日期的集合</li>
 * <li>process: 创建更新进度百分比</li>
 * <li>errorInfo: 如果更新出错这里显示错误信息</li>
 * <li>status: 状态0-创建更新中 1-创建完成 2-已归档</li>
 * <li>punchMonth: YYYYMM</li>
 * <li>punchMemberNumber: 考勤人数</li>
 * <li>updateTime: 更新时间-数据截止时间</li>
 * <li>filerName: 归档人</li> 
 * <li>fileTime: 归档时间</li>
 * </ul>
 */

public class ListPunchCountCommandResponse{
 


	private Long nextPageAnchor;

    @ItemType(PunchCountDTO.class)
    private List<PunchCountDTO> punchCountList;

    @ItemType(String.class)
    private List<String> extColumns;
	private List<Long> departmentIds;
    private List<String> dateList;

    private Integer process;
    private String errorInfo;
    private Byte status;
    private String punchMonth;
	private Long updateTime;

    private Timestamp fileTime;
    private String filerName;
	private Integer punchMemberNumber;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<PunchCountDTO> getPunchCountList() {
		return punchCountList;
	}

	public void setPunchCountList(List<PunchCountDTO> punchCountList) {
		this.punchCountList = punchCountList;
	}

	public List<String> getExtColumns() {
		return extColumns;
	}

	public void setExtColumns(List<String> extColumns) {
		this.extColumns = extColumns;
	}

	public List<Long> getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(List<Long> departmentIds) {
		this.departmentIds = departmentIds;
	}

	public List<String> getDateList() {
		return dateList;
	}

	public void setDateList(List<String> dateList) {
		this.dateList = dateList;
	}

	public Integer getProcess() {
		return process;
	}

	public void setProcess(Integer process) {
		this.process = process;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getPunchMonth() {
		return punchMonth;
	}

	public void setPunchMonth(String punchMonth) {
		this.punchMonth = punchMonth;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Timestamp getFileTime() {
		return fileTime;
	}

	public void setFileTime(Timestamp fileTime) {
		this.fileTime = fileTime;
	}

	public String getFilerName() {
		return filerName;
	}

	public void setFilerName(String filerName) {
		this.filerName = filerName;
	}

	public Integer getPunchMemberNumber() {
		return punchMemberNumber;
	}

	public void setPunchMemberNumber(Integer punchMemberNumber) {
		this.punchMemberNumber = punchMemberNumber;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
