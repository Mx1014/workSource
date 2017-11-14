package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>ownerType: 所有者类型 如：PM</li>
 *     <li>ownerId: 管理机构id</li>
 *     <li>targetType: 关联类型 如 community</li>
 *     <li>targetId: 关联id communityId</li>
 *     <li>name: 计划名称</li>
 *     <li>repeat: 周期 参考{@link com.everhomes.rest.repeat.RepeatSettingsDTO}</li>
 *     <li>notifyTickMinutes: 任务结束前提前提醒分钟</li>
 *     <li>notifyTickUnit: 单位 分钟、小时、天等</li>
 *     <li>groups: 执行组列表 参考{@link com.everhomes.rest.energy.EnergyPlanGroupDTO}</li>
 *     <li>meters: 计划关联表计列表 参考{@link com.everhomes.rest.energy.EnergyPlanMeterDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/10/19.
 */
public class EnergyPlanDTO {
    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String targetType;
    private Long targetId;
    private String name;
    @ItemType(RepeatSettingsDTO.class)
    private RepeatSettingsDTO repeat;
    private Integer notifyTickMinutes;
    private Byte notifyTickUnit;
    @ItemType(EnergyPlanGroupDTO.class)
    private List<EnergyPlanGroupDTO> groups;
    @ItemType(EnergyPlanMeterDTO.class)
    private List<EnergyPlanMeterDTO> meters;

    public List<EnergyPlanGroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<EnergyPlanGroupDTO> groups) {
        this.groups = groups;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<EnergyPlanMeterDTO> getMeters() {
        return meters;
    }

    public void setMeters(List<EnergyPlanMeterDTO> meters) {
        this.meters = meters;
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

    public Integer getNotifyTickMinutes() {
        return notifyTickMinutes;
    }

    public void setNotifyTickMinutes(Integer notifyTickMinutes) {
        this.notifyTickMinutes = notifyTickMinutes;
    }

    public Byte getNotifyTickUnit() {
        return notifyTickUnit;
    }

    public void setNotifyTickUnit(Byte notifyTickUnit) {
        this.notifyTickUnit = notifyTickUnit;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public RepeatSettingsDTO getRepeat() {
        return repeat;
    }

    public void setRepeat(RepeatSettingsDTO repeat) {
        this.repeat = repeat;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
