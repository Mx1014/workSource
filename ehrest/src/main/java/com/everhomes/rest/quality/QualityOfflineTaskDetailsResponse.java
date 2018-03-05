package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationMemberDTO;

import java.util.List;

/**
 * <ul>
 * <li>tasks:任务列表</li>
 * <li>specifications:specifications</li>
 * <li>organizations:组织架构</li>
 * <li>organizationMembers:人员</li>
 * <li>groupMaps:计划和group关系</li>
 * <li>taskGroupMap:任务执行group关系</li>
 * <li>nextPageAnchor:下一页锚点</li>
 * </ul>
 */

public class QualityOfflineTaskDetailsResponse {

    @ItemType(QualityInspectionTaskDTO.class)
    private List<QualityInspectionTaskDTO> tasks;

    @ItemType(QualityInspectionSpecificationDTO.class)
    private List<QualityInspectionSpecificationDTO> specifications;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> organizations;

    @ItemType(OrganizationMemberDTO.class)
    private List<OrganizationMemberDTO> organizationMembers;

    @ItemType(QualityInspectionStandardGroupMapDTO.class)
    List<QualityInspectionStandardGroupMapDTO> groupMaps;

    private Long nextPageAnchor;

    public List<QualityInspectionTaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<QualityInspectionTaskDTO> tasks) {
        this.tasks = tasks;
    }

    public List<QualityInspectionSpecificationDTO> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<QualityInspectionSpecificationDTO> specifications) {
        this.specifications = specifications;
    }

    public List<OrganizationDTO> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<OrganizationDTO> organizations) {
        this.organizations = organizations;
    }

    public List<OrganizationMemberDTO> getOrganizationMembers() {
        return organizationMembers;
    }

    public void setOrganizationMembers(List<OrganizationMemberDTO> organizationMembers) {
        this.organizationMembers = organizationMembers;
    }

    public List<QualityInspectionStandardGroupMapDTO> getGroupMaps() {
        return groupMaps;
    }

    public void setGroupMaps(List<QualityInspectionStandardGroupMapDTO> groupMaps) {
        this.groupMaps = groupMaps;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
}
