package com.everhomes.yellowPage;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.yellowPage.JumpModuleDTO;
import com.everhomes.rest.yellowPage.IdNameInfoDTO;
import com.everhomes.rest.yellowPage.standard.ConfigCommand;
import com.everhomes.rest.yellowPage.stat.ServiceAndTypeNameDTO;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;
import org.jooq.Condition;

public interface YellowPageProvider {
 

	void populateYellowPagesAttachment(YellowPage yellowPage);


	YellowPage getYellowPageById(Long id);


	List<YellowPage> queryYellowPages(CrossShardListingLocator locator, int pageSize,
			String ownerType, Long ownerId, Long parentId, Byte type,
			String serviceType, String keywords);
	
	
	List<ServiceAlliances> queryServiceAllianceAdmin(CrossShardListingLocator locator, int pageSize,
												String ownerType, Long ownerId, Long parentId, Long categoryId, List<Long> childTagIds, String keywords,  Byte displayFlag, ConfigCommand cmd);

	List<ServiceAlliances> queryServiceAllianceByScene(CrossShardListingLocator locator, int pageSize, String ownerType,
			Long ownerId, List<Long> authProjectIds, Long parentId, Long categoryId, List<Long> childTagIds, String keywords, ConfigCommand cmd);

	void createYellowPage(YellowPage yellowPage);


	void updateYellowPage(YellowPage yellowPage);


	void createYellowPageAttachments(YellowPageAttachment yellowPageAttachment);


	void deleteYellowPageAttachmentsByOwnerId(Long ownerId);


	void deleteYellowPageAttachment(YellowPageAttachment attachment);


	YellowPage queryYellowPageTopic(String ownerType, Long ownerId, Byte type);
	
	List<YellowPage> getYellowPagesByCategoryId(Long categoryId);

	YellowPage findYellowPageById(Long id, String ownerType, Long ownerId);
	
	ServiceAllianceCategories findCategoryById(Long id);
	ServiceAllianceCategories findMainCategory(String ownerTye, Long ownerId, Long type);
	ServiceAllianceCategories findCategoryByName(Integer namespaceId, String name);
	ServiceAllianceCategories findCategoryByEntryId(Integer namespaceId, Integer EntryId);
	void createCategory(ServiceAllianceCategories category);
	void updateCategory(ServiceAllianceCategories category);
	void createServiceAlliances(ServiceAlliances sa);
	void updateServiceAlliances(ServiceAlliances sa);
	void createServiceAllianceAttachments(ServiceAllianceAttachment attachment);
	void deleteServiceAllianceAttachmentsByOwnerId(String ownerType, Long ownerId);
	
	ServiceAlliances findServiceAllianceById(Long id, String ownerType, Long ownerId);
	void populateServiceAlliancesAttachment(ServiceAlliances sa, String ownerType);
	
	List<ServiceAllianceCategories> listCategories(CrossShardListingLocator locator, Integer pageSize, String ownerType, Long ownerId, Integer namespaceId, Long parentId, Long type, List<Byte> displayDestination, boolean queryAllChilds);

	void createNotifyTarget(ServiceAllianceNotifyTargets target);
	void updateNotifyTarget(ServiceAllianceNotifyTargets target);
	void deleteNotifyTarget(Long id);
	ServiceAllianceNotifyTargets findNotifyTarget(String ownerType, Long ownerId, Long id);
	ServiceAllianceNotifyTargets findNotifyTarget(String ownerType, Long ownerId, Long categoryId, Byte contactType, String contactToken);
	List<ServiceAllianceNotifyTargets> listNotifyTargets(Integer namespaceId, Byte contactType, 
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

	Long createInvestRequests(ServiceAllianceInvestRequests request);
	ServiceAllianceInvestRequests findInvestRequests(Long id);
	List<ServiceAllianceInvestRequests> listInvestRequests(CrossShardListingLocator locator, int pageSize);


	List<JumpModuleDTO> jumpModules(Integer namespaceId, String bizString);
	
	List<ServiceAllianceAttachment> listAttachments(CrossShardListingLocator locator, int count, String ownerType, Long ownerId);

	List<ServiceAllianceAttachment> listAttachments(String ownerType, Long ownerId, Byte attachmentType);

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

	List<Integer> listAscEntryIds(int namespaceId);


	void updateEntryIdNullByNamespaceId(Integer namespaceId);


	List<ServiceAlliances> findOldFormServiceAlliance();

	List<ServiceAndTypeNameDTO> listServiceNames(Long type, Long ownerId, Long categoryId);

	List<IdNameInfoDTO> listServiceTypeNames(Long type);


	Map<Long, Long> getServiceTypeOrders(List<Long> idList);


	void updateServiceTypeOrders(Long id, Long order);



	void deleteProjectMainConfig(Long projectId, Long type);

	void deleteProjectCategories(Long projectId, Long type);

	List<ServiceAllianceCategories> listChildCategories(Long parentId);


	void updateMainCategorysByType(Long type, Byte enableComment, Byte enableProvider, String name);


	List<IdNameInfoDTO> listServiceTypeNames(String ownerType, Long ownerId, Long type);


	void updateServiceAllianceOrder(Long itemId, Long defaultOrderId);




}
