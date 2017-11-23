package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import java.sql.Date;

/**
 * <ul>
 * <id>id: 教育信息标识号</id>
 * <li>detailId: 员工标识号</li>
 * <li>schoolName：学校名称</li>
 * <li>degree: 学位</li>
 * <li>major: 专业名称</li>
 * <li>enrollmentTime: 入学日期</li>
 * <li>graduationTime: 毕业日期</li>
 * </ul>
 */

public class OrganizationMemberEducationsDTO {

    private Long id;

    private Long detailId;

    private String schoolName;

    private String degree;

    private String major;

    private Date enrollmentTime;

    private Date graduationTime;

    public OrganizationMemberEducationsDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Date getEnrollmentTime() {
        return enrollmentTime;
    }

    public void setEnrollmentTime(Date enrollmentTime) {
        this.enrollmentTime = enrollmentTime;
    }

    public Date getGraduationTime() {
        return graduationTime;
    }

    public void setGraduationTime(Date graduationTime) {
        this.graduationTime = graduationTime;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
