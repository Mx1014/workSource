// @formatter:off
package com.everhomes.rest.community_form;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id:id</li>
 *     <li>namespaceId:域空间ID</li>
 *     <li>communityId:项目ID</li>
 *     <Li>communityName: 项目名称</Li>
 *     <li>formOriginId:表单formOriginID</li>
 *     <li>formName:表单名称</li>
 *     <li>type:类型</li>
 * </ul>
 */
public class CommunityGeneralFormDTO {
    private Long id;
    private Integer namespaceId;
    private Long communityId;
    private String communityName;
    private Long formOriginId;
    private String formName;
    private String type;

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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
