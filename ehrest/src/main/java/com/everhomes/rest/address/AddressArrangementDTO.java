// @formatter:off
package com.everhomes.rest.address;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.organization.OrganizationOwnerDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 拆分/合并计划id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>operationType: 操作类型：拆分（0），合并（1）</li>
 * <li>dateBegin: 拆分/合并计划的生效日期</li>
 * <li>addressId: 要执行拆分/合并计划的房源id</li>
 * <li>apartments: 拆分后、或者待合并的房源信息，参考{@link com.everhomes.rest.address.ArrangementApartmentDTO}</li>
 * <li>enterpriseCustomers: 和拆分出的房源或待合并的房源相关联的企业客户信息，参考{@link com.everhomes.rest.customer.EnterpriseCustomerDTO}</li>
 * <li>individualCustomers: 和拆分出的房源或待合并的房源相关联的个人客户信息，参考{@link com.everhomes.rest.organization.OrganizationOwnerDTO}</li>
 * <li>contracts: 和拆分出的房源或待合并的房源相关联的合同信息，参考{@link com.everhomes.rest.contract.ContractDTO}</li>
 * </ul>
 */
public class AddressArrangementDTO {
	
	private Long id;
	private Integer namespaceId;
	private Byte operationType;
	private Long dateBegin;
	private Long addressId;
	@ItemType(ArrangementApartmentDTO.class)
    private List<ArrangementApartmentDTO> apartments;
	@ItemType(EnterpriseCustomerDTO.class)
    private List<EnterpriseCustomerDTO> enterpriseCustomers;
	@ItemType(OrganizationOwnerDTO.class)
    private List<OrganizationOwnerDTO> individualCustomers;
	@ItemType(ContractDTO.class)
    private List<ContractDTO> contracts;
	
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
	public Byte getOperationType() {
		return operationType;
	}
	public void setOperationType(Byte operationType) {
		this.operationType = operationType;
	}
	public Long getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Long dateBegin) {
		this.dateBegin = dateBegin;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public List<ArrangementApartmentDTO> getApartments() {
		return apartments;
	}
	public void setApartments(List<ArrangementApartmentDTO> apartments) {
		this.apartments = apartments;
	}
	public List<EnterpriseCustomerDTO> getEnterpriseCustomers() {
		return enterpriseCustomers;
	}
	public void setEnterpriseCustomers(List<EnterpriseCustomerDTO> enterpriseCustomers) {
		this.enterpriseCustomers = enterpriseCustomers;
	}
	public List<OrganizationOwnerDTO> getIndividualCustomers() {
		return individualCustomers;
	}
	public void setIndividualCustomers(List<OrganizationOwnerDTO> individualCustomers) {
		this.individualCustomers = individualCustomers;
	}
	public List<ContractDTO> getContracts() {
		return contracts;
	}
	public void setContracts(List<ContractDTO> contracts) {
		this.contracts = contracts;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
