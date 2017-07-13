package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId:通讯录关联的用户ID</li>
 * <li>detailId: 联系人档案id</li>
 * <li>enterpriseName: 公司名称</li>
 * <li>contactName: 联系人名字</li>
 * <li>contactAvatar: 联系人头像</li>
 * <li>contactEnglishName: 联系人英文名字</li>
 * <li>jobPosition: 联系人岗位</li>
 * <li>contactToken: 联系人号码</li>
 * <li>email: 联系人邮箱</li>
 * <li>departments: 联系人部门</li>
 * </ul>
 */
public class SceneContactV2DTO {

    private Long userId;

    private Long detailId;

    private String contactAvatar;

    private String enterpriseName;

    private String contactName;

    private String contactEnglishName;

    private String jobPosition;

    private String contactToken;

    private String email;

    private String departments;

    public SceneContactV2DTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactAvatar() {
        return contactAvatar;
    }

    public void setContactAvatar(String contactAvatar) {
        this.contactAvatar = contactAvatar;
    }

    public String getContactEnglishName() {
        return contactEnglishName;
    }

    public void setContactEnglishName(String contactEnglishName) {
        this.contactEnglishName = contactEnglishName;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
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

    public String getDepartments() {
        return departments;
    }

    public void setDepartments(String departments) {
        this.departments = departments;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
