package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>detailId: 成员 detailId</li>
 * <li>organizationId: 公司或部门 id</li>
 * <li>targetId: 用户id</li>
 * <li>targetType: 用户类型</li>
 * <li>contactName: 姓名</li>
 * <li>contactEnName: 英文名</li>
 * <li>gender: 性别: 1-男, 2-女</li>
 * <li>jobPositions: 职务</li>
 * <li>departments: 部门 {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>jobLevels: 岗位 {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>regionCode: 区号</li>
 * <li>contactToken: 手机号</li>
 * <li>contactShortToken: 短号</li>
 * <li>workEmail: 工作邮箱</li>
 * <li>stick: 置顶状态: 0-未置顶 1-置顶</li>
 * <li>account: 账号(唯一标识)</li>
 * </ul>
 */
public class ArchivesContactDTO {

    private Long detailId;

    private Long organizationId;

    private Long targetId;

    private String targetType;

    private String contactName;

    private String contactEnName;

    private Byte gender;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> departments;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> jobPositions;

    private List<OrganizationDTO> jobLevels;

    private String regionCode;

    private String contactToken;

    private String contactShortToken;

    private String workEmail;

    private String stick;

    private Byte visibleFlag;

    private String account;

    public ArchivesContactDTO() {
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEnName() {
        return contactEnName;
    }

    public void setContactEnName(String contactEnName) {
        this.contactEnName = contactEnName;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
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

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    public String getContactShortToken() {
        return contactShortToken;
    }

    public void setContactShortToken(String contactShortToken) {
        this.contactShortToken = contactShortToken;
    }

    public String getWorkEmail() {
        return workEmail;
    }

    public void setWorkEmail(String workEmail) {
        this.workEmail = workEmail;
    }

    public String getStick() {
        return stick;
    }

    public void setStick(String stick) {
        this.stick = stick;
    }

    public Byte getVisibleFlag() {
        return visibleFlag;
    }

    public void setVisibleFlag(Byte visibleFlag) {
        this.visibleFlag = visibleFlag;
    }

    public List<OrganizationDTO> getJobLevels() {
        return jobLevels;
    }

    public void setJobLevels(List<OrganizationDTO> jobLevels) {
        this.jobLevels = jobLevels;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
