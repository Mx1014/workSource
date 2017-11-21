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
    private String requestorEntperiseName;
    private String requestorEntperiseAddress;
    private String requestorName;
    private Long relocationDate;
    private String contactPhone;

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

    public String getRequestorEntperiseName() {
        return requestorEntperiseName;
    }

    public void setRequestorEntperiseName(String requestorEntperiseName) {
        this.requestorEntperiseName = requestorEntperiseName;
    }

    public String getRequestorEntperiseAddress() {
        return requestorEntperiseAddress;
    }

    public void setRequestorEntperiseAddress(String requestorEntperiseAddress) {
        this.requestorEntperiseAddress = requestorEntperiseAddress;
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
