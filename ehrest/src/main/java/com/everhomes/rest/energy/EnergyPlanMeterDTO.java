package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>planId: 计划id</li>
 *     <li>meterId: 表计id</li>
 *     <li>meterType: 表计类型</li>
 *     <li>meterNumber: 表计号码</li>
 *     <li>meterName: 表计名称</li>
 *     <li>defaultOrder: 排序</li>
 *     <li>addresses: 表记所属楼栋门牌 参考{@link com.everhomes.rest.energy.EnergyMeterAddressDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/10/19.
 */
public class EnergyPlanMeterDTO {
    private Long id;
    private Long planId;
    private Long meterId;
    private Byte meterType;
    private String meterNumber;
    private String meterName;
    private Integer defaultOrder;
    private String status;
    @ItemType(EnergyMeterAddressDTO.class)
    private List<EnergyMeterAddressDTO> addresses;

    public List<EnergyMeterAddressDTO> getAddresses() {
        return addresses;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAddresses(List<EnergyMeterAddressDTO> addresses) {
        this.addresses = addresses;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
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

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public Byte getMeterType() {
        return meterType;
    }

    public void setMeterType(Byte meterType) {
        this.meterType = meterType;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
