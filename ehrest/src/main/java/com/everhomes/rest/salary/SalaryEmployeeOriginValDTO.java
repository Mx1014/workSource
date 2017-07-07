package com.everhomes.rest.salary;

/**
 * <ul>
 * <li>salaryGroupId: 批次id</li>
 * <li>userId: 用户id</li>
 * <li>userDetailId: 用户档案id</li>
 * <li>groupEntityId: 项目字段id</li>
 * <li>originEntityId: 项目字段原始id</li>
 * <li>groupEntityName: 项目字段名称</li>
 * <li>salaryValue: 项目字段对应值</li>
 * </ul>
 */
public class SalaryEmployeeOriginValDTO {

    private Long id;

    private Long salaryGroupId;

    private Long userId;

    private Long userDetailId;

    private Long groupEntityId;

    private Long originEntityId;

    private String groupEntityName;

    private String salaryValue;

    public SalaryEmployeeOriginValDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSalaryGroupId() {
        return salaryGroupId;
    }

    public void setSalaryGroupId(Long salaryGroupId) {
        this.salaryGroupId = salaryGroupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserDetailId() {
        return userDetailId;
    }

    public void setUserDetailId(Long userDetailId) {
        this.userDetailId = userDetailId;
    }

    public Long getGroupEntityId() {
        return groupEntityId;
    }

    public void setGroupEntityId(Long groupEntityId) {
        this.groupEntityId = groupEntityId;
    }

    public Long getOriginEntityId() {
        return originEntityId;
    }

    public void setOriginEntityId(Long originEntityId) {
        this.originEntityId = originEntityId;
    }

    public String getGroupEntityName() {
        return groupEntityName;
    }

    public void setGroupEntityName(String groupEntityName) {
        this.groupEntityName = groupEntityName;
    }

    public String getSalaryValue() {
        return salaryValue;
    }

    public void setSalaryValue(String salaryValue) {
        this.salaryValue = salaryValue;
    }
}
