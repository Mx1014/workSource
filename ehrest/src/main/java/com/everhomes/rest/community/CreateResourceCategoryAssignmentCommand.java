// @formatter:off
package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>resourceType: 资源实体类型 小区园区：EhCommunities</li>
 * <li>resourceId: 资源实体id</li>
 * <li>resourceCategoryId: 类型id</li>
 * </ul>
 */
public class CreateResourceCategoryAssignmentCommand {

    private String resourceType;

    private Long resourceId;

    private Long resourceCategoryId;

    public CreateResourceCategoryAssignmentCommand() {
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Long getResourceCategoryId() {
        return resourceCategoryId;
    }

    public void setResourceCategoryId(Long resourceCategoryId) {
        this.resourceCategoryId = resourceCategoryId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
