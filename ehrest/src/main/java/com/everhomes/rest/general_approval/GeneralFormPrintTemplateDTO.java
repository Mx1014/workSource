// @formatter:off
package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 表单打印模板ID</li>
 *     <li>namespaceId:域空间ID</li>
 *     <li>ownerId:表单打印模板所属ID，即表单ID</li>
 *     <li>ownerType:表单打印模板所属类型，默认为EhGeneralForm</li>
 *     <li>name:打印模板名称</li>
 *     <li>contents:打印模板内容</li>
 *     <li>lastCommit:最近提交的ID</li>
 *     <li>creatorUid:创建人ID</li>
 *     <li>creatorName:创建人名称</li>
 *     <li>creatorDate:创建时间</li>
 * </ul>
 */
public class GeneralFormPrintTemplateDTO {

    private Long id;
    private Integer namespaceId;
    private Long ownerId;
    private String ownerType;
    private String name;
    private String contents;
    private String lastCommit;
    private Long creatorUid;
    private String creatorName;
    private String creatorDate;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getLastCommit() {
        return lastCommit;
    }

    public void setLastCommit(String lastCommit) {
        this.lastCommit = lastCommit;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorDate() {
        return creatorDate;
    }

    public void setCreatorDate(String creatorDate) {
        this.creatorDate = creatorDate;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String gogsPath() {
        return this.getName() + ".txt";
    }
}
