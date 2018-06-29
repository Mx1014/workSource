package com.everhomes.rest.techpark.punch;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>id: id</li> 
 * <li>punchMonth: YYYYMM</li> 
 * <li>filerName: 归档人</li> 
 * <li>fileTime: 归档时间</li>
 * </ul>
 */
public class PunchMonthReportDTO {
	private String punchMonth;
	private Long id;
    private Long creatorUid;
    private Timestamp createTime;
    private Long filerUid;
    private Timestamp fileTime;
    private String creatorName;
    private String filerName;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getPunchMonth() {
		return punchMonth;
	}

	public void setPunchMonth(String punchMonth) {
		this.punchMonth = punchMonth;
	}

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFilerUid() {
		return filerUid;
	}

	public void setFilerUid(Long filerUid) {
		this.filerUid = filerUid;
	}

	public Timestamp getFileTime() {
		return fileTime;
	}

	public void setFileTime(Timestamp fileTime) {
		this.fileTime = fileTime;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getFilerName() {
		return filerName;
	}

	public void setFilerName(String filerName) {
		this.filerName = filerName;
	}

}
