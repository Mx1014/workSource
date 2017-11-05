// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;


/**
 * <ul>
 *     <li>id: id</li>
 *     <li>uuid: uuid</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>groupId: groupId</li>
 *     <li>applicantUid: applicantUid</li>
 *     <li>name: name</li>
 *     <li>phone: phone</li>
 *     <li>email: email</li>
 *     <li>organizationName: organizationName</li>
 *     <li>registeredCapital: registeredCapital</li>
 *     <li>industryType: industryType</li>
 *     <li>createTime: createTime</li>
 *     <li>updateTime: updateTime</li>
 *     <li>updateUid: updateUid</li>
 *     <li>status: 申请状态 0-applying,1-reject,2-agree 参考 {@link GuildApplyStatus}</li>
 * </ul>
 */
public class GuildApplyDTO {
    private Long id;
    private String uuid;
    private Integer namespaceId;
    private Long groupId;
    private Long applicantUid;
    private String name;
    private String phone;
    private String email;
    private String organizationName;
    private String registeredCapital;
    private String industryType;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Long updateUid;
    private Byte status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getApplicantUid() {
        return applicantUid;
    }

    public void setApplicantUid(Long applicantUid) {
        this.applicantUid = applicantUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUid() {
        return updateUid;
    }

    public void setUpdateUid(Long updateUid) {
        this.updateUid = updateUid;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
