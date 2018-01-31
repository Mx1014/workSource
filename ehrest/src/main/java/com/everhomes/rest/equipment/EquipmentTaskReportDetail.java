package com.everhomes.rest.equipment;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  <li>attachments: 上报内容图片     参考{@link  com.everhomes.rest.equipment.Attachment}</li>
 *  <li>message: 上报信息</li>
 *  <li>taskId: 任务id</li>
 *  <li>equipmentId: 巡检设备id</li>
 *  <li>standardId: 巡检标准id</li>
 *  <li>itemResults: 设备参数 参考{@link com.everhomes.rest.equipment.InspectionItemResult}</li>
 * </ul>
 */
public class EquipmentTaskReportDetail {

    @ItemType(Attachment.class)
    private List<Attachment> attachments;

    private String message;

    private Long taskId;

    private Long equipmentId;

    private Long standardId;

    @ItemType(InspectionItemResult.class)
    private List<InspectionItemResult> itemResults;

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public List<InspectionItemResult> getItemResults() {
        return itemResults;
    }

    public void setItemResults(List<InspectionItemResult> itemResults) {
        this.itemResults = itemResults;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
