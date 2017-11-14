package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: id</li>
 *  <li>standardId: 标准id</li>
 *  <li>standardName: 标准名称</li>
 *  <li>repeatType: 标准周期类型 0:no repeat 1: by day 2:by week 3: by month 4:year</li>
 *  <li>equipmentId: 设备id</li>
 *  <li>equipmentName: 设备名称</li>
 *  <li>sequenceNo: 设备编号</li>
 *  <li>location: 设备位置</li>
 * </ul>
 */
public class EquipmentStandardMapDTO {

	private Long id;
	
	private Long standardId;
	
	private String standardName;
	
	private Long equipmentId;

	private  String  equipmentName;

	private String sequenceNo;

	private  String location;

	@Deprecated
	private Byte reviewStatus;

	@Deprecated
	private Byte reviewResult;

	@Deprecated
	private Long reviewerUid;

	@Deprecated
	private String reviewerName;

	@Deprecated
	private Long reviewTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStandardId() {
		return standardId;
	}

	public void setStandardId(Long standardId) {
		this.standardId = standardId;
	}

	public String getStandardName() {
		return standardName;
	}

	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Byte getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(Byte reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public Byte getReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(Byte reviewResult) {
		this.reviewResult = reviewResult;
	}

	public Long getReviewerUid() {
		return reviewerUid;
	}

	public void setReviewerUid(Long reviewerUid) {
		this.reviewerUid = reviewerUid;
	}

	public String getReviewerName() {
		return reviewerName;
	}

	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}

	public Long getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Long reviewTime) {
		this.reviewTime = reviewTime;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
