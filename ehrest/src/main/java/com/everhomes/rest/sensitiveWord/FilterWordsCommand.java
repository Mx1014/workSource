// @formatter:off
package com.everhomes.rest.sensitiveWord;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: 域空间ID</li>
 *     <li>moduleType: 模块类型/li>
 *     <li>communityId: 项目ID</li>
 *     <li>creatorUid: 发布人的userId</li>
 *     <li>publishTime: 发布时间</li>
 *     <li>textList: 需要检测的文本内容</li>
 * </ul>
 */
public class FilterWordsCommand {

    private Integer namespaceId;
    private Byte moduleType;
    private Long communityId;
    private Long creatorUid;
    private String publishTime;
    private List<String> textList;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getModuleType() {
        return moduleType;
    }

    public void setModuleType(Byte moduleType) {
        this.moduleType = moduleType;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public List<String> getTextList() {
        return textList;
    }

    public void setTextList(List<String> textList) {
        this.textList = textList;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
