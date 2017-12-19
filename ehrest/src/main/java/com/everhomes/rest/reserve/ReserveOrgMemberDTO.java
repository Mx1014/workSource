package com.everhomes.rest.reserve;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>contactPhone: 手机号</li>
 * <li>contactName: 姓名</li>
 * </ul>
 */
public class ReserveOrgMemberDTO {

    private Integer namespaceId;
    private String contactPhone;
    private String contactName;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
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
