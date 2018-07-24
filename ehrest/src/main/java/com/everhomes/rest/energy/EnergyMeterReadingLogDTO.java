package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *     <li>id: 读表记录id</li>
 *     <li>meterId: 表记id</li>
 *     <li>meterName: 表记名称</li>
 *     <li>meterNumber: 表记编号</li>
 *     <li>meterType: 表记分类</li>
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
    private Byte meterType;
    private String meterNumber;
    @ItemType(EnergyMeterAddressDTO.class)
    private List<EnergyMeterAddressDTO> meterAddress;
    private BigDecimal reading;
    private BigDecimal oldReading;
    private String operatorName;
    private Timestamp operateTime;
    private Byte resetMeterFlag;
    private Byte changeMeterFlag;

    private BigDecimal dayPrompt;
    private BigDecimal monthPrompt;
    private BigDecimal lastReading;
    private BigDecimal valueDifference;

    //离线的用
    private Long communityId;
    private Long organizationId;
    private Integer namespaceId;


    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<EnergyMeterAddressDTO> getMeterAddress() {
        return meterAddress;
    }

    public void setMeterAddress(List<EnergyMeterAddressDTO> meterAddress) {
        this.meterAddress = meterAddress;
    }

    public Byte getMeterType() {
        return meterType;
    }

    public void setMeterType(Byte meterType) {
        this.meterType = meterType;
    }

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

    public BigDecimal getLastReading() {
        return lastReading;
    }

    public void setLastReading(BigDecimal lastReading) {
        this.lastReading = lastReading;
    }

    public BigDecimal getValueDifference() {
        return valueDifference;
    }

    public void setValueDifference(BigDecimal valueDifference) {
        this.valueDifference = valueDifference;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
