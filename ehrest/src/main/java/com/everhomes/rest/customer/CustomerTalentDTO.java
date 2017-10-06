package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>customerType: 客户类型 参考{@link com.everhomes.rest.customer.CustomerType}</li>
 *     <li>customerId: 客户id</li>
 *     <li>name: 姓名</li>
 *     <li>gender: 性别</li>
 *     <li>genderName: 性别</li>
 *     <li>phone: 电话</li>
 *     <li>nationalityItemId: 国籍id</li>
 *     <li>nationalityItemName: 国籍</li>
 *     <li>degreeItemId: 最高学历id</li>
 *     <li>degreeItemName: 最高学历</li>
 *     <li>graduateSchool: 毕业学校</li>
 *     <li>major: 所属专业</li>
 *     <li>experience: 工作经验</li>
 *     <li>returneeFlag: 是否海归</li>
 *     <li>returneeFlagName: 是否海归</li>
 *     <li>abroadItemId: 留学国家id</li>
 *     <li>abroadItemName: 留学国家</li>
 *     <li>jobPosition: 聘任职务</li>
 *     <li>technicalTitleItemId: 技术职称</li>
 *     <li>individualEvaluationItemId: 个人评定id</li>
 *     <li>individualEvaluationItemName: 个人评定</li>
 *     <li>personalCertificate: 个人证书</li>
 *     <li>careerExperience: 主要职业经历</li>
 *     <li>remark: 备注</li>
 * </ul>
 * Created by ying.xiong on 2017/8/18.
 */
public class CustomerTalentDTO {
    private Long id;
    private Integer namespaceId;
    private Byte customerType;
    private Long customerId;
    private String name;
    private Long gender;
    private String genderName;
    private String phone;
    private Long nationalityItemId;
    private String nationalityItemName;
    private Long degreeItemId;
    private String degreeItemName;
    private String graduateSchool;
    private String major;
    private Integer experience;
    private Long returneeFlag;
    private String returneeFlagName;
    private Long abroadItemId;
    private String abroadItemName;
    private String jobPosition;
    private Long technicalTitleItemId;
    private String technicalTitleItemName;
    private Long individualEvaluationItemId;
    private String individualEvaluationItemName;
    private String personalCertificate;
    private String careerExperience;
    private String remark;

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getReturneeFlagName() {
        return returneeFlagName;
    }

    public void setReturneeFlagName(String returneeFlagName) {
        this.returneeFlagName = returneeFlagName;
    }

    public Long getAbroadItemId() {
        return abroadItemId;
    }

    public void setAbroadItemId(Long abroadItemId) {
        this.abroadItemId = abroadItemId;
    }

    public String getAbroadItemName() {
        return abroadItemName;
    }

    public void setAbroadItemName(String abroadItemName) {
        this.abroadItemName = abroadItemName;
    }

    public String getCareerExperience() {
        return careerExperience;
    }

    public void setCareerExperience(String careerExperience) {
        this.careerExperience = careerExperience;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Byte getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    public Long getDegreeItemId() {
        return degreeItemId;
    }

    public void setDegreeItemId(Long degreeItemId) {
        this.degreeItemId = degreeItemId;
    }

    public String getDegreeItemName() {
        return degreeItemName;
    }

    public void setDegreeItemName(String degreeItemName) {
        this.degreeItemName = degreeItemName;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Long getGender() {
        return gender;
    }

    public void setGender(Long gender) {
        this.gender = gender;
    }

    public void setReturneeFlag(Long returneeFlag) {
        this.returneeFlag = returneeFlag;
    }

    public String getGraduateSchool() {
        return graduateSchool;
    }

    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIndividualEvaluationItemId() {
        return individualEvaluationItemId;
    }

    public void setIndividualEvaluationItemId(Long individualEvaluationItemId) {
        this.individualEvaluationItemId = individualEvaluationItemId;
    }

    public String getIndividualEvaluationItemName() {
        return individualEvaluationItemName;
    }

    public void setIndividualEvaluationItemName(String individualEvaluationItemName) {
        this.individualEvaluationItemName = individualEvaluationItemName;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getNationalityItemId() {
        return nationalityItemId;
    }

    public void setNationalityItemId(Long nationalityItemId) {
        this.nationalityItemId = nationalityItemId;
    }

    public String getNationalityItemName() {
        return nationalityItemName;
    }

    public void setNationalityItemName(String nationalityItemName) {
        this.nationalityItemName = nationalityItemName;
    }

    public String getPersonalCertificate() {
        return personalCertificate;
    }

    public void setPersonalCertificate(String personalCertificate) {
        this.personalCertificate = personalCertificate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getReturneeFlag() {
        return returneeFlag;
    }

    public Long getTechnicalTitleItemId() {
        return technicalTitleItemId;
    }

    public void setTechnicalTitleItemId(Long technicalTitleItemId) {
        this.technicalTitleItemId = technicalTitleItemId;
    }

    public String getTechnicalTitleItemName() {
        return technicalTitleItemName;
    }

    public void setTechnicalTitleItemName(String technicalTitleItemName) {
        this.technicalTitleItemName = technicalTitleItemName;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
