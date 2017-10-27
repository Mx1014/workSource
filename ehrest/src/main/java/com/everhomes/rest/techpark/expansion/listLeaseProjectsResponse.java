package com.everhomes.rest.techpark.expansion;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * @author sw on 2017/10/17.
 */
public class listLeaseProjectsResponse {
    private Long nextPageAnchor;

    @ItemType(LeaseProjectDTO.class)
    private List<LeaseProjectDTO> projects;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<LeaseProjectDTO> getProjects() {
        return projects;
    }

    public void setProjects(List<LeaseProjectDTO> projects) {
        this.projects = projects;
    }
}
