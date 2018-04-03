// @formatter:off
package com.everhomes.rest.organization;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>organizationId: 组织id</li>
 * <li>groupTypes：机构类型</li>
 * <li>contactName：姓名</li>
 * </ul>
 */
public class ListAllChildOrganizationPersonnelCommand {

    @NotNull
    private Long organizationId;

    @NotNull
    @ItemType(String.class)
    private List<String> groupTypes;

    @NotNull
    private String contactName;


    public Long getOrganizationId() {
        return organizationId;
    }


    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }


    public List<String> getGroupTypes() {
        return groupTypes;
    }


    public void setGroupTypes(List<String> groupTypes) {
        this.groupTypes = groupTypes;
    }


    public String getContactName() {
        return contactName;
    }


    public void setContactName(String contactName) {
        this.contactName = contactName;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
