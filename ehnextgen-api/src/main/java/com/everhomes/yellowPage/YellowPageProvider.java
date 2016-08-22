package com.everhomes.yellowPage;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;

public interface YellowPageProvider {
 

	void populateYellowPagesAttachment(YellowPage yellowPage);


	YellowPage getYellowPageById(Long id);


	List<YellowPage> queryYellowPages(CrossShardListingLocator locator, int pageSize,
			String ownerType, Long ownerId, Long parentId, Byte type,
			String serviceType, String keywords);
	
	
	List<YellowPage> queryServiceAlliance(CrossShardListingLocator locator, int pageSize,
			String ownerType, Long ownerId, Long parentId, Long categoryId, String keywords);


	void createYellowPage(YellowPage yellowPage);


	void updateYellowPage(YellowPage yellowPage);


	void createYellowPageAttachments(YellowPageAttachment yellowPageAttachment);


	void deleteYellowPageAttachmentsByOwnerId(Long ownerId);


	void deleteYellowPageAttachment(YellowPageAttachment attachment);


	YellowPage queryYellowPageTopic(String ownerType, Long ownerId, Byte type);
	
	List<YellowPage> getYellowPagesByCategoryId(Long categoryId);

	YellowPage findYellowPageById(Long id, String ownerType, Long ownerId);
 
}
