package com.everhomes.rest.relocation;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/11/20.
 */
public class RequestRelocationCommand {

    private Integer namespaceId;
    private Long requestorEnterpriseId;
    private String requestorEnterpriseName;
    private String requestorEnterpriseAddress;
    private String requestorName;
    private String contactPhone;
    private Long relocationDate;

    @ItemType(RelocationRequestItemDTO.class)
    private List<RelocationRequestItemDTO> items;

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getRequestorEnterpriseId() {
        return requestorEnterpriseId;
    }

    public void setRequestorEnterpriseId(Long requestorEnterpriseId) {
        this.requestorEnterpriseId = requestorEnterpriseId;
    }

    public String getRequestorEnterpriseName() {
        return requestorEnterpriseName;
    }

    public void setRequestorEnterpriseName(String requestorEnterpriseName) {
        this.requestorEnterpriseName = requestorEnterpriseName;
    }

    public String getRequestorEnterpriseAddress() {
        return requestorEnterpriseAddress;
    }

    public void setRequestorEnterpriseAddress(String requestorEnterpriseAddress) {
        this.requestorEnterpriseAddress = requestorEnterpriseAddress;
    }

    public String getRequestorName() {
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public Long getRelocationDate() {
        return relocationDate;
    }

    public void setRelocationDate(Long relocationDate) {
        this.relocationDate = relocationDate;
    }

    public List<RelocationRequestItemDTO> getItems() {
        return items;
    }

    public void setItems(List<RelocationRequestItemDTO> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
