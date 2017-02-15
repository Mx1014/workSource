// @formatter:off
package com.everhomes.rest.contract;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id: id</li>
 * <li>contractNumber: 合同编号</li>
 * <li>contractEndDate: 合同到期日期</li>
 * <li>organizationName: 客户名称</li>
 * <li>adminMembers: 管理员列表</li>
 * <li>contactor: 业务联系人名称</li>
 * <li>contract: 业务联系人电话</li>
 * <li>signupCount: 注册人数</li>
 * <li>serviceUserId: 客服人员id</li>
 * <li>serviceUserName: 客服人员名称</li>
 * <li>serviceUserPhone: 客服人员电话</li>
 * <li>buildings: 楼栋门牌信息{@link com.everhomes.rest.organization.OrganizationContactDTO}</li>
 * </ul>
 */
public class ContractDTO {
	private Long id;
	
	private String contractNumber;
	
	private Timestamp contractEndDate;
	
	private String organizationName;
	
	@ItemType(OrganizationContactDTO.class)
	private List<OrganizationContactDTO> adminMembers;
	
	private String contactor;
	
	private String contract;
	
	private Integer signupCount;

    private Long serviceUserId;
    
    private String serviceUserName;
    
    private String serviceUserPhone;
    
    @ItemType(BuildingApartmentDTO.class)
    private List<BuildingApartmentDTO> buildings;
	
	public List<BuildingApartmentDTO> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<BuildingApartmentDTO> buildings) {
		this.buildings = buildings;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
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

	public List<OrganizationContactDTO> getAdminMembers() {
		return adminMembers;
	}

	public void setAdminMembers(List<OrganizationContactDTO> adminMembers) {
		this.adminMembers = adminMembers;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
