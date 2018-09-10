// @formatter:off
package com.everhomes.rest.sensitiveWord.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间ID</li>
 *     <li>sensitiveWords: 敏感词</li>
 *     <li>moduleId: 应用ID</li>
 *     <li>moduleName: 应用名称</li>
 *     <li>creatorUid: 发布人ID</li>
 *     <li>creatorName：发布人名称</li>
 *     <li>phone: 发布人手机号码</li>
 *     <li>publishTime: 发布日期</li>
 *     <li>text: 文本内容</li>
 * </ul>
 */
public class SensitiveFilterRecordAdminDTO {

    private Long id;
    private Long namespaceId;
    private String sensitiveWords;
    private Long moduleId;
    private String moduleName;
    private Long creatorUid;
    private String creatorName;
    private String phone;
    private String publishTime;
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Long namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getSensitiveWords() {
        return sensitiveWords;
    }

    public void setSensitiveWords(String sensitiveWords) {
        this.sensitiveWords = sensitiveWords;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
