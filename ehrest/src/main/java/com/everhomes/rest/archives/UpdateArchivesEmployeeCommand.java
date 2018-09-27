package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>detailId: 员工id</li>
 * <li>organizationId: 公司id</li>
 * <li>avatar: 员工头像</li>
 * <li>values: (List)被修改的字段及值 {@link com.everhomes.rest.general_approval.PostApprovalFormItem}</li>
 * </ul>
 */
public class UpdateArchivesEmployeeCommand {

    private Integer namespaceId;

    private Long detailId;

    private Long organizationId;

    private String contactToken;

    private String avatar;

    @ItemType(PostApprovalFormItem.class)
    List<PostApprovalFormItem> values;

    public UpdateArchivesEmployeeCommand() {
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<PostApprovalFormItem> getValues() {
        return values;
    }

    public void setValues(List<PostApprovalFormItem> values) {
        this.values = values;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }
}
