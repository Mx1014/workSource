package com.everhomes.rest.activity;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>attachments: 附件列表 参考{@link com.everhomes.rest.activity.ActivityAttachmentDTO}</li>
 *     <li>nextPageAnchor: 下一页的锚点</li>
 * </ul>
 */
public class ListActivityAttachmentsResponse {

    @ItemType(ActivityAttachmentDTO.class)
    private List<ActivityAttachmentDTO> attachments;

    private Long nextPageAnchor;

    public List<ActivityAttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ActivityAttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
