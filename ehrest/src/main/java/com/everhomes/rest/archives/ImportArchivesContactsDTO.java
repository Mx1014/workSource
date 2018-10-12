package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>contactName: 姓名</li>
 * <li>account: 账号(唯一标识)</li>
 * <li>contactEnName: 英文名</li>
 * <li>gender: 性别</li>
 * <li>contactToken: 手机</li>
 * <li>contactShortToken: 短号</li>
 * <li>workEmail: 工作邮箱</li>
 * <li>department: 部门</li>
 * <li>jobPosition: 职务</li>
 * </ul>
 */
public class ImportArchivesContactsDTO {

    private String contactName;

    private String account;

    private String contactEnName;

    private String gender;

    private String contactToken;

    private String contactShortToken;

    private String workEmail;

    private String department;

    private String jobPosition;

    public ImportArchivesContactsDTO() {
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getContactEnName() {
        return contactEnName;
    }

    public void setContactEnName(String contactEnName) {
        this.contactEnName = contactEnName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
