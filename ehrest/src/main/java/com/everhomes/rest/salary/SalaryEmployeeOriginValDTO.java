package com.everhomes.rest.salary;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>salaryGroupId: 批次id</li>
 * <li>userId: 用户id</li>
 * <li>groupEntityId: 项目字段id</li>
 * <li>originEntityId: 项目字段原始id</li>
 * <li>entityName: 项目字段名称</li>
 * <li>salaryValue: 项目字段对应值</li>
 * </ul>
 */
public class SalaryEmployeeOriginValDTO {

    private Long id;

    private Long salaryGroupId;

    private Long userId;

    private Long groupEntityId;

    private Long originEntityId;

    private String entityName;

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

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getSalaryValue() {
        return salaryValue;
    }

    public void setSalaryValue(String salaryValue) {
        this.salaryValue = salaryValue;
    }
}
