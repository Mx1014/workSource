package com.everhomes.rest.yellowPage;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>attachments: 附件列表 参考{@link com.everhomes.rest.yellowPage.AttachmentDTO}</li>
 *     <li>nextPageAnchor: 下一页的锚点</li>
 * </ul>
 */
public class ListAttachmentsResponse {
	
	private Long nextPageAnchor;
	
	@ItemType(AttachmentDTO.class)
	private List<AttachmentDTO> attachments;
	
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<AttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
