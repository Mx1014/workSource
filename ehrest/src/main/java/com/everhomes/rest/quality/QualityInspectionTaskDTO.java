package com.everhomes.rest.quality;

import java.sql.Timestamp;
import java.util.List;






import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 任务主键id</li>
 *  <li>standardId: 标准id</li>
 *  <li>parentId: 父任务id</li>
 *  <li>childCount: 子任务数量</li>
 *  <li>ownerType: 任务所属机构类型 com.everhomes.rest.quality.OwnerType</li>
 *  <li>ownerId: 任务所属机构id</li>
 *  <li>taskName: 任务名称</li>
 *  <li>taskNumber: 任务编号</li>
 *  <li>groupName: 业务组名称</li>
 *  <li>categoryName: 所属类型名</li>
 *  <li>executorId:核查人id</li>
 *  <li>executorName:核查人姓名</li>
 *  <li>operatorId:整改人id</li>
 *  <li>operatorName:整改人姓名</li>
 *  <li>reviewerId:审核人id</li>
 *  <li>reviewerName:审核人姓名</li>
 *  <li>executiveStartTime: 核查开始时间</li>
 *  <li>executiveExpireTime: 核查结束时间</li>
 *  <li>processExpireTime: 整改结束时间</li>
 *  <li>status: 核查执行状态 com.everhomes.rest.quality.QualityInspectionTaskStatus</li>
 *  <li>result: 核查执行结果 com.everhomes.rest.quality.QualityInspectionTaskResult</li>
 *  <li>processResult: 整改执行结果com.everhomes.rest.quality.QualityInspectionTaskResult</li>
 *  <li>reviewResult: 审阅结果com.everhomes.rest.quality.QualityInspectionTaskReviewResult</li>
 *  <li>record: 操作记录列表 com.everhomes.rest.quality.QualityInspectionTaskRecordsDTO</li>
 *  <li>executiveGroupId: 执行业务组id</li>
 *  <li>groupUsers: 参考com.everhomes.rest.quality.GroupUserDTO</li>
 *  <li>standardDescription: 标准内容</li>
 *  <li>categoryId: 类型id</li>
 *  <li>categoryDescription: 规范内容</li>
 *  <li>manualFlag : 是否手动添加 0：自动生成，1：手动添加</li>
 * </ul>
 */
public class QualityInspectionTaskDTO {
	
	private Long id;
	
	private Long standardId;
	
	private Long parentId;
	
	private Long childCount;

	private String ownerType;
	
	private Long ownerId;
	
	private String taskName;
	
	private String taskNumber;
	
	private String groupName;
	
	private String categoryName;
	
	private Long executiveGroupId;
	
	private Long executorId;
	
	private String executorName;

	private Long operatorId;
	
	private String operatorName;
	
	private Timestamp executiveStartTime;
	
	private Timestamp executiveExpireTime;
	
	private Timestamp processExpireTime;
	
	private Byte status;
	
	private Byte result;
	
	private Byte processResult;
	
	private Byte reviewResult;
	
	private Long reviewerId;
	
	private String reviewerName;
	
	private Long categoryId;
	
	@ItemType(QualityInspectionTaskRecordsDTO.class)
    private QualityInspectionTaskRecordsDTO record;
	
	@ItemType(GroupUserDTO.class)
	private List<GroupUserDTO> groupUsers;
	
	private Byte taskFlag;
	
	private Byte manualFlag;
	
	private String standardDescription;
	
	private String categoryDescription;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Byte getReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(Byte reviewResult) {
		this.reviewResult = reviewResult;
	}

	public Long getStandardId() {
		return standardId;
	}

	public void setStandardId(Long standardId) {
		this.standardId = standardId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getChildCount() {
		return childCount;
	}

	public void setChildCount(Long childCount) {
		this.childCount = childCount;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}

	public Long getExecutorId() {
		return executorId;
	}

	public void setExecutorId(Long executorId) {
		this.executorId = executorId;
	}

	public String getExecutorName() {
		return executorName;
	}

	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Timestamp getExecutiveStartTime() {
		return executiveStartTime;
	}

	public void setExecutiveStartTime(Timestamp executiveStartTime) {
		this.executiveStartTime = executiveStartTime;
	}

	public Timestamp getExecutiveExpireTime() {
		return executiveExpireTime;
	}

	public void setExecutiveExpireTime(Timestamp executiveExpireTime) {
		this.executiveExpireTime = executiveExpireTime;
	}

	public Timestamp getProcessExpireTime() {
		return processExpireTime;
	}

	public void setProcessExpireTime(Timestamp processExpireTime) {
		this.processExpireTime = processExpireTime;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getResult() {
		return result;
	}

	public void setResult(Byte result) {
		this.result = result;
	}

	public Byte getProcessResult() {
		return processResult;
	}

	public void setProcessResult(Byte processResult) {
		this.processResult = processResult;
	}

	public Long getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(Long reviewerId) {
		this.reviewerId = reviewerId;
	}

	public String getReviewerName() {
		return reviewerName;
	}

	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}

	public QualityInspectionTaskRecordsDTO getRecord() {
		return record;
	}

	public void setRecord(QualityInspectionTaskRecordsDTO record) {
		this.record = record;
	}

	public Long getExecutiveGroupId() {
		return executiveGroupId;
	}

	public void setExecutiveGroupId(Long executiveGroupId) {
		this.executiveGroupId = executiveGroupId;
	}

	public List<GroupUserDTO> getGroupUsers() {
		return groupUsers;
	}

	public void setGroupUsers(List<GroupUserDTO> groupUsers) {
		this.groupUsers = groupUsers;
	}

	public Byte getTaskFlag() {
		return taskFlag;
	}

	public void setTaskFlag(Byte taskFlag) {
		this.taskFlag = taskFlag;
	}

	public String getStandardDescription() {
		return standardDescription;
	}

	public void setStandardDescription(String standardDescription) {
		this.standardDescription = standardDescription;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Byte getManualFlag() {
		return manualFlag;
	}

	public void setManualFlag(Byte manualFlag) {
		this.manualFlag = manualFlag;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
