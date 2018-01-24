//@formatter:off
package com.everhomes.rest.Requisition;

import com.everhomes.util.StringHelper;

/**
 * Created by Wentian Wang on 2018/1/20.
 */
/**
 * <ul>
 * <li>identity: 请求编号</li>
 * <li>theme: 请求主题</li>
 * <li>applicantName: 请示人姓名</li>
 * <li>applicantDepartment: 请示人部门</li>
 * <li>applicantTime: 请示时间</li>
 * <li>type: 请示类型</li>
 * <li>status: 审批状态</li>
 * </ul>
 */
public class ListRequisitionsDTO {
    private String identity;
    private String theme;
    private String applicantName;
    private String applicantDepartment;
    private String applicantTime;
    private String type;
    private String status;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantDepartment() {
        return applicantDepartment;
    }

    public void setApplicantDepartment(String applicantDepartment) {
        this.applicantDepartment = applicantDepartment;
    }

    public String getApplicantTime() {
        return applicantTime;
    }

    public void setApplicantTime(String applicantTime) {
        this.applicantTime = applicantTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
