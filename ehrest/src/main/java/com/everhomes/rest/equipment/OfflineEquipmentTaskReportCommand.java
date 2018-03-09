package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  <li>tasks: 离线上报任务列表</li>
 *  <li>ownerId: 任务所属的主体id</li>
 *  <li>ownerType: 任务所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>equipmentRepairReportDetail: 报修信息{@link com.everhomes.rest.equipment.CreateEquipmentRepairCommand}</li>
 *  <li>equipmentTaskReportDetails: 任务下设备检查细项 {@link com.everhomes.rest.equipment.EquipmentTaskReportDetail}</li>
 *  <li>namespaceId: namespaceId </li>
 * </ul>
 */

public class OfflineEquipmentTaskReportCommand {

    private Long ownerId;

    private String ownerType;

    @ItemType(EquipmentTaskDTO.class)
    private List<EquipmentTaskDTO> tasks;

    @ItemType(EquipmentTaskReportDetail.class)
    private List<EquipmentTaskReportDetail> equipmentTaskReportDetails;

    @ItemType(CreateEquipmentRepairCommand.class)
    private List<CreateEquipmentRepairCommand> equipmentRepairReportDetail;


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

    public List<EquipmentTaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<EquipmentTaskDTO> tasks) {
        this.tasks = tasks;
    }

    public List<EquipmentTaskReportDetail> getEquipmentTaskReportDetails() {
        return equipmentTaskReportDetails;
    }

    public void setEquipmentTaskReportDetails(List<EquipmentTaskReportDetail> equipmentTaskReportDetails) {
        this.equipmentTaskReportDetails = equipmentTaskReportDetails;
    }

    public List<CreateEquipmentRepairCommand> getEquipmentRepairReportDetail() {
        return equipmentRepairReportDetail;
    }

    public void setEquipmentRepairReportDetail(List<CreateEquipmentRepairCommand> equipmentRepairReportDetail) {
        this.equipmentRepairReportDetail = equipmentRepairReportDetail;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
