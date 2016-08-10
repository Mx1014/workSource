package com.everhomes.rest.rentalv2;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.admin.AttachmentConfigDTO;
import com.everhomes.util.StringHelper;
/**
 * <ul> 
 * <li>siteItems：场所商品{@link com.everhomes.rest.rentalv2.SiteItemDTO}</li>
 * <li>attachments：场所附件{@link 
com.everhomes.rest.rentalv2.admin.AttachmentConfigDTO}</li>
 * </ul>
 */
public class FindRentalSiteItemsAndAttachmentsResponse {

    @ItemType(SiteItemDTO.class)
	private List<SiteItemDTO> siteItems;
    
    @ItemType(AttachmentConfigDTO.class)
	private List<AttachmentConfigDTO> attachments;
    
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public List<SiteItemDTO> getSiteItems() {
		return siteItems;
	}
	public void setSiteItems(List<SiteItemDTO> siteItems) {
		this.siteItems = siteItems;
	}
	public List<AttachmentConfigDTO> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<AttachmentConfigDTO> attachments) {
		this.attachments = attachments;
	}
}
