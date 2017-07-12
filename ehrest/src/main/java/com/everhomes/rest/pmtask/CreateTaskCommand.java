package com.everhomes.rest.pmtask;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>categoryId: 子类型ID</li>
 * <li>address: 服务地点</li>
 * <li>content: 内容</li>
 * <li>organizationId: 公司id</li>
 * <li>taskCategoryId: 服务类型id</li>
 * <li>addressId: 门牌id</li>
 * <li>priority: 客户反映</li>
 * <li>reserveTime: 预约时间</li>
 * <li>sourceType: 报事来源</li>
 * <li>requestorName: 联系人名称</li>
 * <li>requestorPhone: 联系方式</li>
 * <li>addressType: 地址类型   1:小区家庭门牌地址 2: 园区公司地址 {@link com.everhomes.rest.pmtask.PmTaskAddressType }</li>
 * </ul>
 */
public class CreateTaskCommand {
	private String ownerType;
    private Long ownerId;
	private Long categoryId;
	private String address;
	private String content;
	private Long organizationId;
	
	private Long taskCategoryId;
	private Long addressId;
	private Byte priority;
	private String sourceType;
	private Long reserveTime;
	private String requestorName;
	private String requestorPhone;
	
	private Byte addressType;
	private Long addressOrgId;
	private String buildingName;
	private Long flowOrganizationId;
	private Integer namespaceId;
	@ItemType(AttachmentDescriptor.class)
	private List<AttachmentDescriptor> attachments;

	public Long getFlowOrganizationId() {
		return flowOrganizationId;
	}

	public void setFlowOrganizationId(Long flowOrganizationId) {
		this.flowOrganizationId = flowOrganizationId;
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
	public List<AttachmentDescriptor> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<AttachmentDescriptor> attachments) {
		this.attachments = attachments;
	}
	public Long getTaskCategoryId() {
		return taskCategoryId;
	}
	public void setTaskCategoryId(Long taskCategoryId) {
		this.taskCategoryId = taskCategoryId;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public Long getReserveTime() {
		return reserveTime;
	}
	public void setReserveTime(Long reserveTime) {
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
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public Byte getAddressType() {
		return addressType;
	}
	public void setAddressType(Byte addressType) {
		this.addressType = addressType;
	}
	public Long getAddressOrgId() {
		return addressOrgId;
	}
	public void setAddressOrgId(Long addressOrgId) {
		this.addressOrgId = addressOrgId;
	}
	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
