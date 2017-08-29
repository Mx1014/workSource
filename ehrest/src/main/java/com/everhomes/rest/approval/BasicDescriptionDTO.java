// @formatter:off
package com.everhomes.rest.approval;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>timeTotal: 请假总时长</li>
 * <li>categoryName: 请假类型</li>
 * <li>timeRangeList: 请假时间列表，参考{@link com.everhomes.rest.approval.TimeRange}</li>
 * <li>punchDate: 打卡日期</li>
 * <li>punchIntevalNo: 当天第几个班次</li>
 * <li>punchDetail: 打卡详情</li>
 * <li>punchStatusName: 打卡状态名称</li>
 * <li>hourLength: 时长 -请假</li>  
 * </ul>
 */
public class BasicDescriptionDTO {
	private String timeTotal;
	private String categoryName;
	@ItemType(TimeRange.class)
	private List<TimeRange> timeRangeList;

	private Timestamp punchDate;
	private Integer punchIntevalNo;
	private String punchDetail;
	private String punchStatusName;
	
	private Double hourLength;
	
	public Timestamp getPunchDate() {
		return punchDate;
	}

	public void setPunchDate(Timestamp punchDate) {
		this.punchDate = punchDate;
	}

	public String getPunchDetail() {
		return punchDetail;
	}

	public void setPunchDetail(String punchDetail) {
		this.punchDetail = punchDetail;
	}

	public String getPunchStatusName() {
		return punchStatusName;
	}

	public void setPunchStatusName(String punchStatusName) {
		this.punchStatusName = punchStatusName;
	}

	public Double getHourLength() {
		return hourLength;
	}

	public void setHourLength(Double hourLength) {
		this.hourLength = hourLength;
	}

	public String getTimeTotal() {
		return timeTotal;
	}

	public void setTimeTotal(String timeTotal) {
		this.timeTotal = timeTotal;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<TimeRange> getTimeRangeList() {
		return timeRangeList;
	}

	public void setTimeRangeList(List<TimeRange> timeRangeList) {
		this.timeRangeList = timeRangeList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Integer getPunchIntevalNo() {
		return punchIntevalNo;
	}

	public void setPunchIntevalNo(Integer punchIntevalNo) {
		this.punchIntevalNo = punchIntevalNo;
	}
}
