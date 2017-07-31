package com.everhomes.rest.ui.user;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.util.StringHelper;

 
import java.rmi.MarshalledObject;
import java.util.List;
import java.util.Map;

/**
 * <ul>
 * <li>userId: 通讯录关联的用户ID</li>
 * <li>detailId: 联系人档案id</li>
 * <li>contactAvatar: 联系人头像</li>
 * <li>enterpriseName: 公司名称</li>
 * <li>contactName: 联系人名字</li>
 * <li>contactEnglishName: 联系人英文名字</li>
 * <li>gender: 联系人性别：0-保密, 1-男性, 2-女性</li>
 * <li>jobPosition: 联系人岗位 {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>departments: 联系人部门 {@link com.everhomes.rest.organization.OrganizationDTO}</li>
 * <li>contactToken: 联系人号码</li>
 * <li>email: 联系人邮箱</li>
 * </ul>
 */
public class SceneContactV2DTO {

    private Long userId;

    private Long detailId;

    private String contactAvatar;

    private String enterpriseName;

    private String contactName;

    private String contactEnglishName;

    private Byte gender;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> jobPosition;

    @ItemType(OrganizationDTO.class)
    private List<OrganizationDTO> departments;

    private String contactToken;

    private String email;


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

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public List<OrganizationDTO> getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(List<OrganizationDTO> jobPosition) {
        this.jobPosition = jobPosition;
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


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
