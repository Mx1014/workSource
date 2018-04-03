package com.everhomes.rest.contract;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>chargingItemId: 收费项id</li>
 *     <li>chargingItemName: 收费项名称</li>
 *     <li>changeType: 计划类型 参考{@link com.everhomes.rest.contract.ChangeType}</li>
 *     <li>changeMethod: 调整类型 参考{@link com.everhomes.rest.contract.ChangeMethod}</li>
 *     <li>changePeriod: 调整时间</li>
 *     <li>periodUnit: 调整时间单位 参考{@link com.everhomes.rest.contract.PeriodUnit}</li>
 *     <li>changeRange: 调整幅度</li>
 *     <li>changeStartTime: 执行开始日期</li>
 *     <li>changeExpiredTime: 执行结束日期</li>
 *     <li>remark: 备注</li>
 *     <li>apartments: 计划适用资产列表 参考{@link com.everhomes.rest.contract.BuildingApartmentDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/10/10.
 */
public class ContractChargingChangeDTO {
    private Long id;
    private Integer namespaceId;
    private Long chargingItemId;
    private String chargingItemName;
    private Byte changeType;
    private Byte changeMethod;
    private Integer changePeriod;
    private Byte periodUnit;
    private BigDecimal changeRange;
    private Long changeStartTime;
    private Long changeExpiredTime;
    @ItemType(BuildingApartmentDTO.class)
    private List<BuildingApartmentDTO> apartments;
    private String remark;

    public List<BuildingApartmentDTO> getApartments() {
        return apartments;
    }

    public void setApartments(List<BuildingApartmentDTO> apartments) {
        this.apartments = apartments;
    }

    public Long getChangeExpiredTime() {
        return changeExpiredTime;
    }

    public void setChangeExpiredTime(Long changeExpiredTime) {
        this.changeExpiredTime = changeExpiredTime;
    }

    public Byte getChangeMethod() {
        return changeMethod;
    }

    public void setChangeMethod(Byte changeMethod) {
        this.changeMethod = changeMethod;
    }

    public Integer getChangePeriod() {
        return changePeriod;
    }

    public void setChangePeriod(Integer changePeriod) {
        this.changePeriod = changePeriod;
    }

    public BigDecimal getChangeRange() {
        return changeRange;
    }

    public void setChangeRange(BigDecimal changeRange) {
        this.changeRange = changeRange;
    }

    public Long getChangeStartTime() {
        return changeStartTime;
    }

    public void setChangeStartTime(Long changeStartTime) {
        this.changeStartTime = changeStartTime;
    }

    public Byte getChangeType() {
        return changeType;
    }

    public void setChangeType(Byte changeType) {
        this.changeType = changeType;
    }

    public Long getChargingItemId() {
        return chargingItemId;
    }

    public void setChargingItemId(Long chargingItemId) {
        this.chargingItemId = chargingItemId;
    }

    public String getChargingItemName() {
        return chargingItemName;
    }

    public void setChargingItemName(String chargingItemName) {
        this.chargingItemName = chargingItemName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(Byte periodUnit) {
        this.periodUnit = periodUnit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
