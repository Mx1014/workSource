// @formatter:off
package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：主键id</li>
 * <li>organizationId：组织id。</li>
 * <li>contactName：联系名称</li>
 * <li>contactType：联系类型。0-手机号，1-邮箱 {@link com.everhomes.rest.user.IdentifierType}</li>
 * <li>contactToken：联系方式:手机号/邮箱</li>
 * <li>creatorUid：创建者uId</li>
 * <li>createTime：创建时间</li>
 * <li>jobPosition: 岗位</li>
 * <li>detailId: 用户档案id</li>
 * <li>visibleFlag: 隐私保护: 0-显示(不受保护) 1-隐藏(保护) 参考{@link com.everhomes.rest.organization.VisibleFlag} </li>
 * <li>departmentName: 部门名字</li>
 * </ul>
 */
public class OfflineJobPositionDTO {
    private Long id;
    private Long organizationId;
    private String contactName;
    private Byte contactType;
    private String contactToken;
    private String nickName;
    private Long targetId;
    private String targetType;
    private String jobPosition;
    private String departmentName;
    private Long standardId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Byte getContactType() {
        return contactType;
    }

    public void setContactType(Byte contactType) {
        this.contactType = contactType;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
