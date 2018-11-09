// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

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
public class OrganizationContactDTO implements Comparable<OrganizationContactDTO>{
    private Long id;
    private Long organizationId;
    private String contactName;
    private Byte contactType;
    private String contactToken;
    private Long creatorUid;
    private Timestamp createTime;

    private String initial;
    private String fullPinyin;
    private String fullInitial;
    private String nickName;
    private String avatar;
    private String employeeNo;
    private Byte gender;
    private Long targetId;

    private String targetType;

    //added by R 20120713 增加岗位信息
    private String jobPosition;
    private Long detailId;
    private Byte visibleFlag;

    private String departmentName;

    public OrganizationContactDTO() {
    }

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

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getFullPinyin() {
        return fullPinyin;
    }

    public void setFullPinyin(String fullPinyin) {
        this.fullPinyin = fullPinyin;
    }

    public String getFullInitial() {
        return fullInitial;
    }

    public void setFullInitial(String fullInitial) {
        this.fullInitial = fullInitial;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
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

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Byte getVisibleFlag() {
        return visibleFlag;
    }

    public void setVisibleFlag(Byte visibleFlag) {
        this.visibleFlag = visibleFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public int compareTo(OrganizationContactDTO organizationMember) {
        return this.initial.compareTo(organizationMember.getInitial());
    }
}
