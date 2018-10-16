// @formatter:off
package com.everhomes.rest.contract;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 
 * <ul>
 * <li>id: id</li>
 * <li>partyAId: 甲方id</li>
 * <li>contractNumber: 合同编号</li>
 * <li>name: 合同名称</li>
 * <li>contractStartDate: 合同开始日期</li>
 * <li>contractEndDate: 合同到期日期</li>
 * <li>organizationName: 客户名称</li>
 * <li>customerName: 客户名称</li>
 * <li>adminMembers: 管理员列表</li>
 * <li>contactor: 业务联系人名称</li>
 * <li>contract: 业务联系人电话</li>
 * <li>signupCount: 注册人数</li>
 * <li>serviceUserId: 客服人员id</li>
 * <li>serviceUserName: 客服人员名称</li>
 * <li>serviceUserPhone: 客服人员电话</li>
 * <li>buildings: 楼栋门牌信息{@link com.everhomes.rest.contract.BuildingApartmentDTO}</li>
 * <li>status: 合同状态 参考{@link com.everhomes.rest.contract.ContractStatus}</li>
 * <li>contractType: 合同属性 参考{@link com.everhomes.rest.contract.ContractType}</li>
 * <li>rent: 合同总金额</li>
 * <li>categoryId: 合同类型categoryId，用于多入口</li>ContractTemplates
 * <li>contractTemplate: 合同模板信息{@link com.everhomes.rest.contract.ContractTemplateDTO}</li>
 * <li>contractApplicationScene: 合同场景信息{@link com.everhomes.rest.contract.ContractApplicationScene}</li>
 * </ul>
 */
public class ContractDTO {
	private Long id;
	private Long partyAId;
	private String partyAName;
	private String contractNumber;
	private String name;
	private Timestamp contractStartDate;
	private Timestamp contractEndDate;
	private String organizationName;
	private String customerName;
	private Long categoryId;
	private Long configId;
	// dynamic field special name
	private String contractTypeName;
	private String syncErrorMsg;
	private Long categoryItemId;
	private String categoryItemName;
	private String contactor;
	private String contract;
	private Integer signupCount;
    private Long serviceUserId;
    private String serviceUserName;
    private String serviceUserPhone;
    private String settled;
	private Byte contractType;
	private Byte status;
	private BigDecimal rent;
	private String namespaceContractType;
	private String namespaceContractToken;
	private Byte paymentFlag;
	private Byte contractApplicationScene;
	
	@ItemType(OrganizationContactDTO.class)
	private List<OrganizationContactDTO> adminMembers;
	
	@ItemType(BuildingApartmentDTO.class)
    private List<BuildingApartmentDTO> buildings;
	
	@ItemType(ContractTemplateDTO.class)
    private ContractTemplateDTO contractTemplate;
	
	private String sponsorName;
	
	private Long sponsorUid;
	

	public Long getSponsorUid() {
		return sponsorUid;
	}

	public void setSponsorUid(Long sponsorUid) {
		this.sponsorUid = sponsorUid;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPartyAId() {
		return partyAId;
	}

	public void setPartyAId(Long partyAId) {
		this.partyAId = partyAId;
	}

	public String getPartyAName() {
		return partyAName;
	}

	public void setPartyAName(String partyAName) {
		this.partyAName = partyAName;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(Timestamp contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public Timestamp getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Timestamp contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getConfigId() {
		return configId;
	}

	public void setConfigId(Long configId) {
		this.configId = configId;
	}

	public String getContractTypeName() {
		return contractTypeName;
	}

	public void setContractTypeName(String contractTypeName) {
		this.contractTypeName = contractTypeName;
	}

	public String getSyncErrorMsg() {
		return syncErrorMsg;
	}

	public void setSyncErrorMsg(String syncErrorMsg) {
		this.syncErrorMsg = syncErrorMsg;
	}

	public Long getCategoryItemId() {
		return categoryItemId;
	}

	public void setCategoryItemId(Long categoryItemId) {
		this.categoryItemId = categoryItemId;
	}

	public String getCategoryItemName() {
		return categoryItemName;
	}

	public void setCategoryItemName(String categoryItemName) {
		this.categoryItemName = categoryItemName;
	}

	public String getContactor() {
		return contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	public Integer getSignupCount() {
		return signupCount;
	}

	public void setSignupCount(Integer signupCount) {
		this.signupCount = signupCount;
	}

	public Long getServiceUserId() {
		return serviceUserId;
	}

	public void setServiceUserId(Long serviceUserId) {
		this.serviceUserId = serviceUserId;
	}

	public String getServiceUserName() {
		return serviceUserName;
	}

	public void setServiceUserName(String serviceUserName) {
		this.serviceUserName = serviceUserName;
	}

	public String getServiceUserPhone() {
		return serviceUserPhone;
	}

	public void setServiceUserPhone(String serviceUserPhone) {
		this.serviceUserPhone = serviceUserPhone;
	}

	public String getSettled() {
		return settled;
	}

	public void setSettled(String settled) {
		this.settled = settled;
	}

	public Byte getContractType() {
		return contractType;
	}

	public void setContractType(Byte contractType) {
		this.contractType = contractType;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public BigDecimal getRent() {
		return rent;
	}

	public void setRent(BigDecimal rent) {
		this.rent = rent;
	}

	public String getNamespaceContractType() {
		return namespaceContractType;
	}

	public void setNamespaceContractType(String namespaceContractType) {
		this.namespaceContractType = namespaceContractType;
	}

	public String getNamespaceContractToken() {
		return namespaceContractToken;
	}

	public void setNamespaceContractToken(String namespaceContractToken) {
		this.namespaceContractToken = namespaceContractToken;
	}

	public Byte getPaymentFlag() {
		return paymentFlag;
	}

	public void setPaymentFlag(Byte paymentFlag) {
		this.paymentFlag = paymentFlag;
	}

	public Byte getContractApplicationScene() {
		return contractApplicationScene;
	}

	public void setContractApplicationScene(Byte contractApplicationScene) {
		this.contractApplicationScene = contractApplicationScene;
	}

	public List<OrganizationContactDTO> getAdminMembers() {
		return adminMembers;
	}

	public void setAdminMembers(List<OrganizationContactDTO> adminMembers) {
		this.adminMembers = adminMembers;
	}

	public List<BuildingApartmentDTO> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<BuildingApartmentDTO> buildings) {
		this.buildings = buildings;
	}

	public ContractTemplateDTO getContractTemplate() {
		return contractTemplate;
	}

	public void setContractTemplate(ContractTemplateDTO contractTemplate) {
		this.contractTemplate = contractTemplate;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
