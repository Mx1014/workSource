package com.everhomes.rest.equipment;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>equipmentId: 设备id</li>
 * <li>targetId: 标准所属管理处id</li>
 * <li>targetName: 标准所属管理处名</li>
 * <li>equipmentName: 设备名称</li>
 * <li>location: 设备位置</li>
 * <li>sequenceNo: 编号</li>
 * <li>status: 设备状态 参考{@link com.everhomes.rest.equipment.EquipmentStatus}</li>
 * <li>standardId: 关联标准id</li>
 * <li>standardName: 标准名称</li>
 * <li>repeatType: 标准周期类型  0:no repeat 1: by day 2:by week 3: by month 4:year</li>
 * <li>inspectionItems: 标准周期类型  参考{@link com.everhomes.rest.equipment.InspectionItemOfflineDTO}</li>
 * </ul>
 */
public class EquipmentStandardRelationOfflineDTO {

    private Long equipmentId;

    private String equipmentName;

    private String sequenceNo;

    private String location;

    private Byte status;

    private Long standardId;

    private String standardName;

    private Byte repeatType;
    @ItemType(InspectionItemOfflineDTO.class)
    private List<InspectionItemOfflineDTO> inspectionItems;

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public Byte getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(Byte repeatType) {
        this.repeatType = repeatType;
    }

    public List<InspectionItemOfflineDTO> getInspectionItems() {
        return inspectionItems;
    }

    public void setInspectionItems(List<InspectionItemOfflineDTO> inspectionItems) {
        this.inspectionItems = inspectionItems;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
