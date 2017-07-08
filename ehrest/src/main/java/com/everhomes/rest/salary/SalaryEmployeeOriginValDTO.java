package com.everhomes.rest.salary;

/**
 * <ul>
 * <li>salaryGroupId: 批次id</li>
 * <li>userId: 用户id</li>
 * <li>userDetailId: 用户档案id</li>
 * <li>type: 字段类型:0-文本类 1-数值类</li>
 * <li>editableFlag: 是否可编辑(对文本类):0-否   1-是</li>
 * <li>numberType: 数值类型:0-普通数值 1-计算公式</li>
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

    private Byte type;

    private Byte editableFlag;

    private Byte numberType;

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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getEditableFlag() {
        return editableFlag;
    }

    public void setEditableFlag(Byte editableFlag) {
        this.editableFlag = editableFlag;
    }

    public Byte getNumberType() {
        return numberType;
    }

    public void setNumberType(Byte numberType) {
        this.numberType = numberType;
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
