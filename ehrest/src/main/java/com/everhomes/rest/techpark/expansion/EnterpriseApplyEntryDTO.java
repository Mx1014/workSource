package com.everhomes.rest.techpark.expansion;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>id：id</li> 
 * <li>sourceType：类型{@link com.everhomes.rest.techpark.expansion.ApplyEntrySourceType}</li> 
 * <li>sourceId：来源id </li> 
 * <li>enterpriseName：公司名 </li> 
 * <li>enterpriseId： 公司id</li> 
 * <li>applyContact： 申请联系人</li> 
 * <li>applyUserId： 申请人id</li> 
 * <li>applyUserName： 申请人用户名</li> 
 * <li>applyType： 申请类型</li> 
 * <li>sizeUnit： 这啥</li> 
 * <li>status： 状态</li> 
 * <li>areaSize：申请空间大小(面积或者工位数) </li> 
 * <li>description： </li> 
 * <li>createTime： </li> 
 * <li>buildings： 楼栋列表 {@link com.everhomes.rest.community.BuildingDTO}</li>
 * <li>contract： 合同 {@link com.everhomes.rest.contract.ContractDTO}</li> 
 * <li>customerName:意向客户名称</li> 
 * </ul>
 */
public class EnterpriseApplyEntryDTO {
	
	private Long id;
	
	private String sourceType; //1:enterprise 2:chuangke space 
	
	private Long sourceId; 
	
	private String enterpriseName;
	
	private Long enterpriseId;
	
	private String applyContact;
	
	private Long applyUserId;
	
	private String applyUserName;

	private Byte sizeUnit;
	
	private Byte status;
	
	private Double areaSize;
	
	private String description;
	
	private Timestamp createTime;

//	private String sourceName;
	
	@ItemType(BuildingDTO.class)
	private List<BuildingDTO> buildings;

	private ContractDTO contract;

	private String issuerType;
	private Long buildingId;
	private String buildingName;
	private Long addressId;
	private String apartmentName;
	private Long flowcaseId;

	@ItemType(PostApprovalFormItem.class)
	private List<PostApprovalFormItem> formValues;

	private Long communityId;
	private String communityName;
	
	private String customerName;
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public List<PostApprovalFormItem> getFormValues() {
		return formValues;
	}

	public void setFormValues(List<PostApprovalFormItem> formValues) {
		this.formValues = formValues;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	public String getIssuerType() {
		return issuerType;
	}

	public void setIssuerType(String issuerType) {
		this.issuerType = issuerType;
	}

	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Long getFlowcaseId() {
		return flowcaseId;
	}

	public void setFlowcaseId(Long flowcaseId) {
		this.flowcaseId = flowcaseId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getApplyContact() {
		return applyContact;
	}

	public void setApplyContact(String applyContact) {
		this.applyContact = applyContact;
	}

	public Long getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(Long applyUserId) {
		this.applyUserId = applyUserId;
	}

	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public Byte getSizeUnit() {
		return sizeUnit;
	}

	public void setSizeUnit(Byte sizeUnit) {
		this.sizeUnit = sizeUnit;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Double getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public List<BuildingDTO> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<BuildingDTO> buildings) {
		this.buildings = buildings;
	}

	public ContractDTO getContract() {
		return contract;
	}

	public void setContract(ContractDTO contract) {
		this.contract = contract;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
