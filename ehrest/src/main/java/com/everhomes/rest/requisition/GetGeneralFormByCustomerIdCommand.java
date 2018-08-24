package com.everhomes.rest.requisition;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>customerId: customerId</li>
 *     <li>communityId: communityId</li>
 *     <li>moduleId: moduleId</li>
 * </ul>
 */
public class GetGeneralFormByCustomerIdCommand {

    private Integer namespaceId;
    private Long customerId;
    private Long moduleId;
    private Long communityId;


    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }


}
