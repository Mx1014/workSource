package com.everhomes.yellowPage;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.yellowPage.JumpModuleDTO;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;
import org.jooq.Condition;

public interface YellowPageProvider {
 

	void populateYellowPagesAttachment(YellowPage yellowPage);


	YellowPage getYellowPageById(Long id);


	List<YellowPage> queryYellowPages(CrossShardListingLocator locator, int pageSize,
			String ownerType, Long ownerId, Long parentId, Byte type,
			String serviceType, String keywords);
	
	
	List<ServiceAlliances> queryServiceAlliance(CrossShardListingLocator locator, int pageSize,
												String ownerType, Long ownerId, Long parentId, Long categoryId, String keywords);

	/**
	 * add by dengs,20170428 不仅查小区，也查询物业公司下的 服务联盟机构
	 */
	List<ServiceAlliances> queryServiceAlliance(CrossShardListingLocator locator, int pageSize,
			String ownerType, Long ownerId, Long parentId, Long categoryId, String keywords,Long organizationId,String organizationType);

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
	
	List<ServiceAllianceCategories> listChildCategories(String ownerType, Long ownerId, Integer namespaceId, Long parentId, CategoryAdminStatus status, List<Byte> displayDestination);
 
	void createNotifyTarget(ServiceAllianceNotifyTargets target);
	void updateNotifyTarget(ServiceAllianceNotifyTargets target);
	void deleteNotifyTarget(Long id);
	ServiceAllianceNotifyTargets findNotifyTarget(String ownerType, Long ownerId, Long id);
	ServiceAllianceNotifyTargets findNotifyTarget(String ownerType, Long ownerId, Long categoryId, Byte contactType, String contactToken);
	List<ServiceAllianceNotifyTargets> listNotifyTargets(String ownerType, Long ownerId, Byte contactType, 
			Long categoryId, CrossShardListingLocator locator, int pageSize);
	
	Long createServiceAllianceRequests(ServiceAllianceRequests request);
	ServiceAllianceRequests findServiceAllianceRequests(Long id);
	List<ServiceAllianceRequests> listServiceAllianceRequests(CrossShardListingLocator locator, int pageSize);
	
	Long createSettleRequests(SettleRequests request);
	SettleRequests findSettleRequests(Long id);
	List<SettleRequests> listSettleRequests(CrossShardListingLocator locator, int pageSize);
	
	Long createReservationRequests(ReservationRequests request);
	ReservationRequests findReservationRequests(Long id);
	List<ReservationRequests> listReservationRequests(CrossShardListingLocator locator, int pageSize);
	
	Long createApartmentRequests(ServiceAllianceApartmentRequests request);
	ServiceAllianceApartmentRequests findApartmentRequests(Long id);
	List<ServiceAllianceApartmentRequests> listApartmentRequests(CrossShardListingLocator locator, int pageSize);
	
	ServiceAllianceSkipRule getCateorySkipRule(Long categoryId);

	Long createInvestRequests(ServiceAllianceInvestRequests request);
	ServiceAllianceInvestRequests findInvestRequests(Long id);
	List<ServiceAllianceInvestRequests> listInvestRequests(CrossShardListingLocator locator, int pageSize);


	List<JumpModuleDTO> jumpModules(Integer namespaceId);
	
	List<ServiceAllianceAttachment> listAttachments(CrossShardListingLocator locator, int count, Long ownerId);

	Long createGolfRequest(ServiceAllianceGolfRequest request);

	Long createGymRequest(ServiceAllianceGymRequest request);

	Long createServerRequest(ServiceAllianceServerRequest request);

	ServiceAllianceServerRequest findServerRequest(Long id);

	ServiceAllianceGymRequest findGymRequest(Long id);

	ServiceAllianceGolfRequest findGolfRequest(Long id);


	/**
	 * 获取顺序
	 */
	List<ServiceAlliances> listServiceAllianceSortOrders(List<Long> idList);


	/**
	 * 更新顺序
	 */
	void updateOrderServiceAllianceDefaultOrder(List<ServiceAlliances> ServiceAllianceList);


	/**
	 * 更新是否在app显示的字段 
	 */
	void updateServiceAlliancesDisplayFlag(Long id, Byte showFlag);

	void createServiceAllianceCategory(ServiceAllianceCategories serviceAllianceCategories);

	void updateServiceAllianceCategory(ServiceAllianceCategories serviceAllianceCategories);

	void createServiceAllianceSkipRule(ServiceAllianceSkipRule serviceAllianceSkipRule);

	void deleteServiceAllianceSkipRule(Long id);

	ServiceAllianceSkipRule getCateorySkipRule(Long categoryId, Integer namespaceId);
}
