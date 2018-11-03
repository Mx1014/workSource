package com.everhomes.rest.pmtask;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 任务log Id</li>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>taskCategoryId: 服务类型id</li>
 * <li>taskCategoryName: 服务类型名称</li>	
 * <li>categoryId: 分类id</li>
 * <li>categoryName: 分类名称</li>
 * <li>address: 服务地点</li>
 * <li>content: 内容</li>
 * <li>status: 状态 1: 待处理  2: 处理中 3: 已完成  4: 已关闭  5:已回访{@link com.everhomes.rest.pmtask.PmTaskStatus}</li>
 * <li>star: 评价分数</li>
 * <li>unprocessedTime: 未处理</li>
 * <li>processingTime: 处理中时间</li>
 * <li>processedTime: 完成时间</li>
 * <li>closedTime: 关闭时间</li>
 * <li>creatorUid: 创建人id</li>
 * <li>createTime: 创建时间</li>
 * <li>requestorName: 联系人名称</li>
 * <li>requestorPhone: 联系方式</li>
 * <li>reserveTime: 预约时间</li>
 * <li>priority: 客户反映</li>
 * <li>sourceType: 报事来源</li>
 * <li>organizationId: 机构id</li>
 * <li>addressId: 门牌id</li>
 * <li>revisitContent: 回访内容</li>
 * <li>revisitTime: 回访时间</li>
 * <li>operatorStar: 操作人员评价分数</li>
 * <li>flowCaseId: 工作流flowCaseId</li>
 * <li>remarkSource: 处理意见来源</li>
 * <li>remark: 处理意见</li>
 * <li>organizationUid: 代发人ID</li>
 * <li>attachments: 附件，参考{@link com.everhomes.rest.pmtask.AttachmentDescriptor}</li>
 * <li>organizationName: 公司名称</li>
 *
 * <li>enterpriseId: 企业id（app场景获取）</li>
 * <li>enterpriseName: 企业名称</li>
 * <li>enterpriseAddress: 楼栋门牌</li>
 *
 * <li>amount: 费用金额</li>
 * <li>feeModel: 是否开启费用清单</li>
 * </ul>
 */
public class PmTaskDTO {
	private Long id;
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private Long taskCategoryId;
	private String taskCategoryName;
	private Long categoryId;
	private String categoryName;
	private String address;
	private String content;
	private Byte status;
	private String star;
	private Timestamp unprocessedTime;
	private Timestamp processingTime;
	private Timestamp processedTime;
	private Timestamp closedTime;
	private Long creatorUid;
	private Timestamp createTime;
	private String requestorName;
	private String requestorPhone;
	
	private Timestamp reserveTime;
	private Byte priority;
	private String sourceType;
	private Long organizationId;
	private Long addressId;
	private String revisitContent;
	private Timestamp revisitTime;
	private Long organizationUid;
	
	private Byte operatorStar;
	
	private Long flowCaseId;
	
	private String buildingName;

	private String remarkSource;

	private String remark;
	
	@ItemType(PmTaskAttachmentDTO.class)
	private List<PmTaskAttachmentDTO> attachments;
	
	@ItemType(PmTaskLogDTO.class)
	private List<PmTaskLogDTO> taskLogs;
	
	private String organizationName;

	private Long enterpriseId;
	private String enterpriseName;
	private String enterpriseAddress;

	private Long amount;

	private String feeModel;

	private Long appId;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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
	
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}
	public Timestamp getUnprocessedTime() {
		return unprocessedTime;
	}
	public void setUnprocessedTime(Timestamp unprocessedTime) {
		this.unprocessedTime = unprocessedTime;
	}
	public Timestamp getProcessingTime() {
		return processingTime;
	}
	public void setProcessingTime(Timestamp processingTime) {
		this.processingTime = processingTime;
	}
	public Timestamp getProcessedTime() {
		return processedTime;
	}
	public void setProcessedTime(Timestamp processedTime) {
		this.processedTime = processedTime;
	}
	public Timestamp getClosedTime() {
		return closedTime;
	}
	public void setClosedTime(Timestamp closedTime) {
		this.closedTime = closedTime;
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
	public List<PmTaskLogDTO> getTaskLogs() {
		return taskLogs;
	}
	public void setTaskLogs(List<PmTaskLogDTO> taskLogs) {
		this.taskLogs = taskLogs;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public List<PmTaskAttachmentDTO> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<PmTaskAttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	public Long getTaskCategoryId() {
		return taskCategoryId;
	}
	public void setTaskCategoryId(Long taskCategoryId) {
		this.taskCategoryId = taskCategoryId;
	}
	public String getTaskCategoryName() {
		return taskCategoryName;
	}
	public void setTaskCategoryName(String taskCategoryName) {
		this.taskCategoryName = taskCategoryName;
	}
	public String getRequestorName() {
		return requestorName;
	}
	public void setRequestorName(String requestorName) {
		this.requestorName = requestorName;
	}
	public String getRequestorPhone() {
		return requestorPhone;
	}
	public void setRequestorPhone(String requestorPhone) {
		this.requestorPhone = requestorPhone;
	}
	public Timestamp getReserveTime() {
		return reserveTime;
	}
	public void setReserveTime(Timestamp reserveTime) {
		this.reserveTime = reserveTime;
	}
	public Byte getPriority() {
		return priority;
	}
	public void setPriority(Byte priority) {
		this.priority = priority;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public String getRevisitContent() {
		return revisitContent;
	}
	public void setRevisitContent(String revisitContent) {
		this.revisitContent = revisitContent;
	}
	public Timestamp getRevisitTime() {
		return revisitTime;
	}
	public void setRevisitTime(Timestamp revisitTime) {
		this.revisitTime = revisitTime;
	}
	public Byte getOperatorStar() {
		return operatorStar;
	}
	public void setOperatorStar(Byte operatorStar) {
		this.operatorStar = operatorStar;
	}

	public Long getOrganizationUid() {
		return organizationUid;
	}

	public void setOrganizationUid(Long organizationUid) {
		this.organizationUid = organizationUid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemarkSource() {
		return remarkSource;
	}

	public void setRemarkSource(String remarkSource) {
		this.remarkSource = remarkSource;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public Long getFlowCaseId() {
		return flowCaseId;
	}
	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}


	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getEnterpriseAddress() {
		return enterpriseAddress;
	}

	public void setEnterpriseAddress(String enterpriseAddress) {
		this.enterpriseAddress = enterpriseAddress;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getFeeModel() {
		return feeModel;
	}

	public void setFeeModel(String feeModel) {
		this.feeModel = feeModel;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	@Override
	public boolean equals(Object obj){
		if (obj == null) return false ;
		else{
			if (obj instanceof PmTaskDTO){
				PmTaskDTO o = (PmTaskDTO) obj;
				if(o.id.equals(this.id)){
					return true ;
				}
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
}
