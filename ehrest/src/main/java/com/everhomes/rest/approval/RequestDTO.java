package com.everhomes.rest.approval;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>requestId: 申请ID</li>
 * <li>nickName: 姓名</li>
 * <li>categoryName: 请假类型名称</li>
 * <li>reason: 申请理由</li>
 * <li>timeRangeList: 请假时间列表</li>
 * <li>timeTotal: 时间总计</li>
 * <li>punchDate: 打卡日期</li>
 * <li>punchIntevalNo: 第几次班</li>
 * <li>punchStatusName: 打卡状态</li>
 * <li>requestInfo: 申请信息(对于加班是"加班日期和时长")</li>
 * <li>punchDetail: 打卡详情</li>
 * </ul>
 */
public class RequestDTO {
	private Long requestId;
	private String nickName;
	private String categoryName;
	private String reason;
	@ItemType(TimeRange.class)
	private List<TimeRange> timeRangeList;
	private String timeTotal; 
	private Timestamp punchDate;
	private Integer punchIntevalNo;
	private String punchStatusName;
	private String requestInfo;
	private String punchDetail;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<TimeRange> getTimeRangeList() {
		return timeRangeList;
	}

	public void setTimeRangeList(List<TimeRange> timeRangeList) {
		this.timeRangeList = timeRangeList;
	}

	public String getTimeTotal() {
		return timeTotal;
	}

	public void setTimeTotal(String timeTotal) {
		this.timeTotal = timeTotal;
	}

	public Timestamp getPunchDate() {
		return punchDate;
	}

	public void setPunchDate(Timestamp punchDate) {
		this.punchDate = punchDate;
	}

	public String getPunchStatusName() {
		return punchStatusName;
	}

	public void setPunchStatusName(String punchStatusName) {
		this.punchStatusName = punchStatusName;
	}

	public String getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(String requestInfo) {
		this.requestInfo = requestInfo;
	}

	public String getPunchDetail() {
		return punchDetail;
	}

	public void setPunchDetail(String punchDetail) {
		this.punchDetail = punchDetail;
	}

	public Integer getPunchIntevalNo() {
		return punchIntevalNo;
	}

	public void setPunchIntevalNo(Integer punchIntevalNo) {
		this.punchIntevalNo = punchIntevalNo;
	}
}
