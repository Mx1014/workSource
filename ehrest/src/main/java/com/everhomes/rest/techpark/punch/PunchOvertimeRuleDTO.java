package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 主键id</li>
 * <li>type: 规则类型，参考{@link com.everhomes.rest.techpark.punch.PunchOvertimeRuleType}，必填</li>
 * <li>name: 加班规则名称，必填</li>
 * <li>overtimeApprovalType: 加班方式，参考{@link com.everhomes.rest.techpark.punch.PunchOvertimeApprovalType}，必填</li>
 * <li>validInterval: 加班起算时间，必填</li>
 * <li>overtimeMinUnit: 最小加班时长，必填</li>
 * <li>overtimeMax: 最大加班时长，必填</li>
 * </ul>
 */
public class PunchOvertimeRuleDTO {
    private Long id;
    private String type;
    private String name;
    private Byte overtimeApprovalType;
    private Integer validInterval;
    private Integer overtimeMinUnit;
    private Integer overtimeMax;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getOvertimeApprovalType() {
        return overtimeApprovalType;
    }

    public void setOvertimeApprovalType(Byte overtimeApprovalType) {
        this.overtimeApprovalType = overtimeApprovalType;
    }

    public Integer getValidInterval() {
        return validInterval;
    }

    public void setValidInterval(Integer validInterval) {
        this.validInterval = validInterval;
    }

    public Integer getOvertimeMinUnit() {
        return overtimeMinUnit;
    }

    public void setOvertimeMinUnit(Integer overtimeMinUnit) {
        this.overtimeMinUnit = overtimeMinUnit;
    }

    public Integer getOvertimeMax() {
        return overtimeMax;
    }

    public void setOvertimeMax(Integer overtimeMax) {
        this.overtimeMax = overtimeMax;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
