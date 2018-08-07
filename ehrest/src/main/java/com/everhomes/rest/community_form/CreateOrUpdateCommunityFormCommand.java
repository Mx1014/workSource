// @formatter:off
package com.everhomes.rest.community_form;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId:域空间ID</li>
 *     <li>communityId:项目ID</li>
 *     <li>formId:表单ID</li>
 *     <li>type:类型</li>
 * </ul>
 */
public class CreateOrUpdateCommunityFormCommand {
    private Integer namespaceId;
    private Long communityId;
    private Long formId;
    private String type;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
