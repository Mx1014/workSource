// @formatter:off
package com.everhomes.rest.ui.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 *     <li>publishPrivilege: 发布权限，0开放模式，1管理员发布模式，如果是1，则调用/org/checkOfficalPrivilegeByScene检查是否具有官方权限</li>
 *     <li>livePrivilege: 是否只要直播活动</li>
 *     <li>categoryId: 活动分类id</li>
 *     <li>pageSize: 每页的数量</li>
 *     <li>pageAnchor: 下一页锚点</li>
 * </ul>
 */
public class ListActivityPromotionEntitiesBySceneCommand {

    private String sceneToken;
    private Byte publishPrivilege;
    private Byte livePrivilege;
    private Long categoryId;
    private Integer pageSize;
    private Long pageAnchor;

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }

    public Byte getPublishPrivilege() {
        return publishPrivilege;
    }

    public void setPublishPrivilege(Byte publishPrivilege) {
        this.publishPrivilege = publishPrivilege;
    }

    public Byte getLivePrivilege() {
        return livePrivilege;
    }

    public void setLivePrivilege(Byte livePrivilege) {
        this.livePrivilege = livePrivilege;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
