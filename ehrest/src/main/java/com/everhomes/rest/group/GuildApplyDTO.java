// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;


/**
 * <ul>
 *     <li>id: id</li>
 *     <li>uuid: uuid</li>
 *     <li>namespace_id: namespace_id</li>
 *     <li>group_id: group_id</li>
 *     <li>applicant_uid: applicant_uid</li>
 *     <li>name: name</li>
 *     <li>phone: phone</li>
 *     <li>email: email</li>
 *     <li>organization_name: organization_name</li>
 *     <li>registered_capital: registered_capital</li>
 *     <li>industryType: industryType</li>
 *     <li>create_time: create_time</li>
 *     <li>update_time: update_time</li>
 *     <li>update_uid: update_uid</li>
 *     <li>status: 0-applying,1-reject,2-agree</li>
 * </ul>
 */
public class GuildApplyDTO {
    private Long id;
    private String uuid;
    private Integer namespace_id;
    private Long group_id;
    private Long applicant_uid;
    private String name;
    private String phone;
    private String email;
    private String organization_name;
    private String registered_capital;
    private String industryType;
    private Timestamp create_time;
    private Timestamp update_time;
    private Long update_uid;
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

    public Integer getNamespace_id() {
        return namespace_id;
    }

    public void setNamespace_id(Integer namespace_id) {
        this.namespace_id = namespace_id;
    }

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }

    public Long getApplicant_uid() {
        return applicant_uid;
    }

    public void setApplicant_uid(Long applicant_uid) {
        this.applicant_uid = applicant_uid;
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

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getRegistered_capital() {
        return registered_capital;
    }

    public void setRegistered_capital(String registered_capital) {
        this.registered_capital = registered_capital;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }

    public Long getUpdate_uid() {
        return update_uid;
    }

    public void setUpdate_uid(Long update_uid) {
        this.update_uid = update_uid;
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
