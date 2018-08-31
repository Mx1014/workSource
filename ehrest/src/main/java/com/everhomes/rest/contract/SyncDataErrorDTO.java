package com.everhomes.rest.contract;

import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>moduleId: moduleId</li>
 *     <li>syncType: "contract","customer"</li>
 *     <li>ownerId: ownerId</li>
 *     <li>ownerType: ownerType</li>
 *     <li>errorMessage: errorMessage</li>
 *     <li>taskId: taskId</li>
 *     <li>contract: contract {@link com.everhomes.rest.contract.ContractDetailDTO}</li>
 *     <li>customerDTO: customerDTO {@link com.everhomes.rest.customer.EnterpriseCustomerDTO}</li>
 * </ul>
 */
public class SyncDataErrorDTO {
    private Long id;
    private Integer namespaceId;
    private Long moduleId;
    private String syncType;
    private Long ownerId;
    private String ownerType;
    private String errorMessage;
    private Long taskId;

    private ContractDetailDTO contract;

    private EnterpriseCustomerDTO customerDTO;

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

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public ContractDetailDTO getContract() {
        return contract;
    }

    public void setContract(ContractDetailDTO contract) {
        this.contract = contract;
    }

    public EnterpriseCustomerDTO getCustomerDTO() {
        return customerDTO;
    }

    public void setCustomerDTO(EnterpriseCustomerDTO customerDTO) {
        this.customerDTO = customerDTO;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
