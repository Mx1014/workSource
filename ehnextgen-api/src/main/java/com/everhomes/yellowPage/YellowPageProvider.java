package com.everhomes.yellowPage;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

public interface YellowPageProvider {
 

	void populateYellowPagesAttachment(YellowPage yellowPage);


	YellowPage getYellowPageById(Long id);


	List<YellowPage> queryYellowPages(CrossShardListingLocator locator, int pageSize,
			String ownerType, Long ownerId, Long parentId, Byte type,
			String serviceType, String keywords);
	
	
	List<ServiceAlliances> queryServiceAlliance(CrossShardListingLocator locator, int pageSize,
			String ownerType, Long ownerId, Long parentId, Long categoryId, String keywords);


	void createYellowPage(YellowPage yellowPage);


	void updateYellowPage(YellowPage yellowPage);


	void createYellowPageAttachments(YellowPageAttachment yellowPageAttachment);


	void deleteYellowPageAttachmentsByOwnerId(Long ownerId);


	void deleteYellowPageAttachment(YellowPageAttachment attachment);


	YellowPage queryYellowPageTopic(String ownerType, Long ownerId, Byte type);
	
	List<YellowPage> getYellowPagesByCategoryId(Long categoryId);

	YellowPage findYellowPageById(Long id, String ownerType, Long ownerId);
	
	ServiceAllianceCategories findCategoryById(Long id);
	ServiceAllianceCategories findCategoryByName(Integer namespaceId, String name);
	void createCategory(ServiceAllianceCategories category);
	void updateCategory(ServiceAllianceCategories category);
	void createServiceAlliances(ServiceAlliances sa);
	void updateServiceAlliances(ServiceAlliances sa);
	void createServiceAllianceAttachments(ServiceAllianceAttachment attachment);
	void deleteServiceAllianceAttachmentsByOwnerId(Long ownerId);
	
	ServiceAlliances queryServiceAllianceTopic(String ownerType, Long ownerId, Long type);
	ServiceAlliances findServiceAllianceById(Long id, String ownerType, Long ownerId);
	void populateServiceAlliancesAttachment(ServiceAlliances sa);
	
	List<ServiceAllianceCategories> listChildCategories(Integer namespaceId, Long parentId, CategoryAdminStatus status);
 
}
