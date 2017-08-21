package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>detailId: 成员 detailId</li>
 * <li>contactName: 姓名</li>
 * <li>jobPositions: 职务 {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>departments: 部门 {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>contactToken: 手机号</li>
 * <li>email: 邮箱</li>
 * <li>stick: 置顶状态: 0-未置顶 1-置顶</li>
 * </ul>
 */
public class ArchivesContactDTO {

    private Long detailId;

    private String contactName;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> jobPositions;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> departments;

    private String contactToken;

    private String email;

    private String stick;

    public ArchivesContactDTO() {
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public List<OrganizationDTO> getJobPositions() {
        return jobPositions;
    }

    public void setJobPositions(List<OrganizationDTO> jobPositions) {
        this.jobPositions = jobPositions;
    }

    public List<OrganizationDTO> getDepartments() {
        return departments;
    }

    public void setDepartments(List<OrganizationDTO> departments) {
        this.departments = departments;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStick() {
        return stick;
    }

    public void setStick(String stick) {
        this.stick = stick;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
