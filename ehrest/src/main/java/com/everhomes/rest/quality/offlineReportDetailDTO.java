package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  <li>taskId: 任务id</li>
 *  <li>attachments: 核查上报内容图片</li>
 *  <li>message: 核查上报内容文字</li>
 *  <li>itemResults: 规范事项  参考{@link com.everhomes.rest.quality.ReportSpecificationItemResultsDTO}</li>
 * </ul>
 */

public class offlineReportDetailDTO {

    private Long taskId;

    private String message;

    private Integer namespaceId;

    @ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;

    @ItemType(ReportSpecificationItemResultsDTO.class)
    private List<ReportSpecificationItemResultsDTO> itemResults;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public List<AttachmentDescriptor> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDescriptor> attachments) {
        this.attachments = attachments;
    }

    public List<ReportSpecificationItemResultsDTO> getItemResults() {
        return itemResults;
    }

    public void setItemResults(List<ReportSpecificationItemResultsDTO> itemResults) {
        this.itemResults = itemResults;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
