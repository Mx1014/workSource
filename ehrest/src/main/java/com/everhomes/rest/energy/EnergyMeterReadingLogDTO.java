package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: 读表记录id</li>
 *     <li>meterId: 表记id</li>
 *     <li>meterName: 表记名称</li>
 *     <li>meterNumber: 表记编号</li>
 *     <li>reading: 读数</li>
 *     <li>oldReading: 旧表读数(只有换表情况下才会有该字段)</li>
 *     <li>operatorName: 读表人名字</li>
 *     <li>operateTime: 读表时间</li>
 *     <li>resetMeterFlag: 是否为复始计量 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 *     <li>changeMeterFlag: 是否为换表 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 *     <li>dayPrompt: 日抄表提示</li>
 *     <li>monthPrompt: 月抄表提示</li>
 * </ul>
 */
public class EnergyMeterReadingLogDTO {

    private Long id;
    private Long meterId;
    private String meterName;
    private String meterNumber;
    private BigDecimal reading;
    private BigDecimal oldReading;
    private String operatorName;
    private Timestamp operateTime;
    private Byte resetMeterFlag;
    private Byte changeMeterFlag;

    private BigDecimal dayPrompt;
    private BigDecimal monthPrompt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeterId() {
        return meterId;
    }

    public BigDecimal getDayPrompt() {
        return dayPrompt;
    }

    public void setDayPrompt(BigDecimal dayPrompt) {
        this.dayPrompt = dayPrompt;
    }

    public BigDecimal getMonthPrompt() {
        return monthPrompt;
    }

    public void setMonthPrompt(BigDecimal monthPrompt) {
        this.monthPrompt = monthPrompt;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public String getMeterName() {
        return meterName;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public BigDecimal getReading() {
        return reading;
    }

    public BigDecimal getOldReading() {
        return oldReading;
    }

    public void setOldReading(BigDecimal oldReading) {
        this.oldReading = oldReading;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Timestamp getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Timestamp operateTime) {
        this.operateTime = operateTime;
    }

    public Byte getResetMeterFlag() {
        return resetMeterFlag;
    }

    public void setResetMeterFlag(Byte resetMeterFlag) {
        this.resetMeterFlag = resetMeterFlag;
    }

    public Byte getChangeMeterFlag() {
        return changeMeterFlag;
    }

    public void setChangeMeterFlag(Byte changeMeterFlag) {
        this.changeMeterFlag = changeMeterFlag;
    }

    public void setReading(BigDecimal reading) {
        this.reading = reading;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
