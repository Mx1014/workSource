package com.everhomes.rest.techpark.expansion;

/**
 * @author sw on 2017/10/17.
 */
public class GetLeaseProjectByIdCommand {
    private Long projectId;
    private Long categoryId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
