// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>scene: 场景标识，参考{@link com.everhomes.rest.ui.SceneType}</li>
 * <li>entityType: 实体类型，{@link com.everhomes.rest.user.UserCurrentEntityType}</li>
 * <li>entityId: 实体ID</li>
 * <li>longitude: 请求人所在位置对应的经度</li>
 * <li>latitude: 请求人所在位置对应的纬度</li>
 * <li>conditionJson: 搜索条件</li>
 * <li>globalFlag: 是否全局搜索</li>
 * <li>pageOffset: 偏移量</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class SearchTopicV2Command {
    private String scene;    
    private String entityType;    
    private Long entityId;
    private Double longitude;
    private Double latitude;
    
    private String queryString;
    private Long forumId;
    private Long contentCategory;
    private Long actionCategory;
    
    private Integer searchFlag;
    private Long pageAnchor;
    
    private Integer pageSize;
    
    public SearchTopicV2Command() {
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getContentCategory() {
        return contentCategory;
    }

    public void setContentCategory(Long contentCategory) {
        this.contentCategory = contentCategory;
    }

    public Long getActionCategory() {
        return actionCategory;
    }

    public void setActionCategory(Long actionCategory) {
        this.actionCategory = actionCategory;
    }

    public Integer getSearchFlag() {
        return searchFlag;
    }

    public void setSearchFlag(Integer searchFlag) {
        this.searchFlag = searchFlag;
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
