package com.everhomes.rest.equipment;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *  <li>taskId: 任务id</li>
 *  <li>verificationResult: 上报结果  参考{@link com.everhomes.rest.equipment.EquipmentTaskResult}</li>
 *  <li>attachments: 上报内容图片   对应设备id   参考{@link  com.everhomes.rest.equipment.Attachment}</li>
 *  <li>itemResults: 设备参数 参考{@link com.everhomes.rest.equipment.InspectionItemResult}</li>
 *  <li>ownerId: 设备所属的主体id</li>
 *  <li>ownerType: 设备所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 * </ul>
 */
public class EquipmentTaskReportDetail {

    @NotNull
    @ItemType(Attachment.class)
    private List<Attachment> attachments;

    private String message;

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

    public List<InspectionItemResult> getItemResults() {
        return itemResults;
    }

    public void setItemResults(List<InspectionItemResult> itemResults) {
        this.itemResults = itemResults;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
