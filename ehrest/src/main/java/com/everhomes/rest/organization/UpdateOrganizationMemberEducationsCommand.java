package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 教育信息编号</li>
 * <li>schoolName：学校名称</li>
 * <li>degree: 学位</li>
 * <li>major: 专业名称</li>
 * <li>enrollmentTime: 入学日期</li>
 * <li>graduationTime: 毕业日期</li>
 * </ul>
 */
public class UpdateOrganizationMemberEducationsCommand {

    private Long id;

    private String schoolName;

    private String degree;

    private String major;

    private String enrollmentTime;

    private String graduationTime;

    public UpdateOrganizationMemberEducationsCommand() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEnrollmentTime() {
        return enrollmentTime;
    }

    public void setEnrollmentTime(String enrollmentTime) {
        this.enrollmentTime = enrollmentTime;
    }

    public String getGraduationTime() {
        return graduationTime;
    }

    public void setGraduationTime(String graduationTime) {
        this.graduationTime = graduationTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}