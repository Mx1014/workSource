// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>communityIds: 小区id列表</li>
 * <li>organizationIds: 公司id列表</li>
 * <li>pmAdminIds: 管理员id列表</li>
 * <li>message: 消息内容</li>
 * <li>messageBodyType: 消息类型</li>
 * <li>imgUri: 图片uri</li>
 * </ul>
 */
public class SendNoticeToPmAdminCommand {

    @ItemType(Long.class)
    private List<Long> communityIds;
    @ItemType(Long.class)
    private List<Long> organizationIds;
    @ItemType(Long.class)
    private List<Long> pmAdminIds;

    private String message;
    private String messageBodyType;
    private String imgUri;

    public List<Long> getCommunityIds() {
        return communityIds;
    }

    public void setCommunityIds(List<Long> communityIds) {
        this.communityIds = communityIds;
    }

    public List<Long> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(List<Long> organizationIds) {
        this.organizationIds = organizationIds;
    }

    public List<Long> getPmAdminIds() {
        return pmAdminIds;
    }

    public void setPmAdminIds(List<Long> pmAdminIds) {
        this.pmAdminIds = pmAdminIds;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageBodyType() {
        return messageBodyType;
    }

    public void setMessageBodyType(String messageBodyType) {
        this.messageBodyType = messageBodyType;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
