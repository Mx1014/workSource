// @formatter:off
package com.everhomes.rest.visitorsys.ui;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>invalidTime: (根据配置选填/必填)有效期</li>
 * <li>plateNo: (根据配置选填/必填)车牌号</li>
 * <li>idNumber: (根据配置选填/必填)证件号码</li>
 * <li>visitFloor: (根据配置选填/必填)到访楼层</li>
 * <li>visitAddresses: (根据配置选填/必填)到访门牌</li>
 * <li>formJsonValue: (选填)表单提交json值</li>
 * </ul>
 */
public class BaseFormDTO {
    private Timestamp invalidTime;
    private String plateNo;
    private String idNumber;
    private String visitFloor;
    private String visitAddresses;
    private String formJsonValue;
    public Timestamp getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Timestamp invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getVisitFloor() {
        return visitFloor;
    }

    public void setVisitFloor(String visitFloor) {
        this.visitFloor = visitFloor;
    }

    public String getVisitAddresses() {
        return visitAddresses;
    }

    public void setVisitAddresses(String visitAddresses) {
        this.visitAddresses = visitAddresses;
    }

    public String getFormJsonValue() {
        return formJsonValue;
    }

    public void setFormJsonValue(String formJsonValue) {
        this.formJsonValue = formJsonValue;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
