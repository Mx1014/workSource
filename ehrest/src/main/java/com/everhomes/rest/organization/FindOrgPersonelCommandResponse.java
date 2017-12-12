package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>members: 人员 参考{@link com.everhomes.rest.organization.OrganizationMemberDTO}</li>
 * <li>departments: 部门 参考{@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>departJobPoisitions: 部门岗位，参考{@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>jobPositions: 通用岗位，参考{@link OrganizationJobPositionDTO}</li>
 * </ul>
 */
public class FindOrgPersonelCommandResponse {
    @ItemType(OrganizationMemberDTO.class)
    private List<OrganizationMemberDTO> members;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> departments;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> departJobPoisitions;

    @ItemType(OrganizationJobPositionDTO.class)
    private List<OrganizationJobPositionDTO> jobPositions;

    public FindOrgPersonelCommandResponse() {
    }

    public List<OrganizationMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<OrganizationMemberDTO> members) {
        this.members = members;
    }

    public List<OrganizationDTO> getDepartments() {
        return departments;
    }

    public void setDepartments(List<OrganizationDTO> departments) {
        this.departments = departments;
    }

    public List<OrganizationDTO> getDepartJobPoisitions() {
        return departJobPoisitions;
    }

    public void setDepartJobPoisitions(List<OrganizationDTO> departJobPoisitions) {
        this.departJobPoisitions = departJobPoisitions;
    }

    public List<OrganizationJobPositionDTO> getJobPositions() {
        return jobPositions;
    }

    public void setJobPositions(List<OrganizationJobPositionDTO> jobPositions) {
        this.jobPositions = jobPositions;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
