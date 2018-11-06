// @formatter:off
package com.everhomes.organization.pm;

import com.everhomes.address.AddressProperties;
import com.everhomes.community.Community;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationOwner;
import com.everhomes.organization.OrganizationTask;
import com.everhomes.rest.organization.OrganizationOwnerDTO;
import com.everhomes.rest.organization.pm.ListOrganizationOwnerStatisticDTO;
import com.everhomes.rest.organization.pm.ListPropInvitedUserCommandResponse;
import com.everhomes.rest.organization.pm.ReservationStatus;
import com.everhomes.server.schema.tables.pojos.EhParkingCardCategories;
import com.everhomes.server.schema.tables.pojos.EhPmResoucreReservations;

import org.jooq.Record;
import org.jooq.RecordMapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public interface PropertyMgrProvider {

	/**
	 * Query property members by specific <code>targetType</code> and <code>targetId</code>
	 * @param targetType {@link PmTargetType}
	 * @param targetId target user id if target_type is a user
	 * @return property members
	 */
	public List<CommunityPmMember> findPmMemberByTargetTypeAndId(String targetType, long targetId);
	
	/**
	 * Query property members by specific <code>communityId</code> and <code>targetType</code> and <code>targetId</code>
	 * @param communityId the community id where the property member managed
	 * @param targetType {@link PmTargetType}
	 * @param targetId target user id if target_type is a user
	 * @return property members
	 */
	public List<CommunityPmMember> listUserCommunityPmMembers(Long userId);
	public List<CommunityPmMember> findPmMemberByCommunityAndTarget(long communityId, String targetType, long targetId);

	public void createPropMember(CommunityPmMember communityPmMember);
	public void updatePropMember(CommunityPmMember communityPmMember);
	public void deletePropMember(CommunityPmMember communityPmMember);
	public void deletePropMember(long id);
	public CommunityPmMember findPropMemberById(long id);
	public List<CommunityPmMember> listCommunityPmMembers(Long communityId, String contactToken,Integer pageOffset,Integer pageSize);
	public List<CommunityPmMember> listCommunityPmMembers(Long communityId);
	
	public void createPropAddressMapping(CommunityAddressMapping communityAddressMapping);
	public void updatePropAddressMapping(CommunityAddressMapping communityAddressMapping);
	public void deletePropAddressMapping(CommunityAddressMapping communityAddressMapping);
	public void deletePropAddressMapping(long id);
	public CommunityAddressMapping findPropAddressMappingById(long id);
	public CommunityAddressMapping findPropAddressMappingByAddressId(Long communityId,Long addressId);
	public List<CommunityAddressMapping> listCommunityAddressMappings(Long communityId, Integer pageOffset,Integer pageSize);
	public List<CommunityAddressMapping> listCommunityAddressMappings(Long communityId);
	
	public void createPropBill(CommunityPmBill communityPmBill);
	public void updatePropBill(CommunityPmBill communityPmBill);
	public void deletePropBill(CommunityPmBill communityPmBill);
	public void deletePropBill(long id);
	public CommunityPmBill findPropBillById(long id);
	public List<CommunityPmBill> listCommunityPmBills(Long communityId, String dateStr,String address, Integer pageOffset,Integer pageSize);
	public List<CommunityPmBill> listCommunityPmBills(Long communityId, String dateStr);
	
	public void createPropBillItem(CommunityPmBillItem communityPmBillItem);
	public void updatePropBillItem(CommunityPmBillItem communityPmBillItem);
	public void deletePropBillItem(CommunityPmBillItem communityPmBillItem);
	public void deletePropBillItem(long id);
	public CommunityPmBillItem findPropBillItemById(long id);
	public List<CommunityPmBillItem> listCommunityPmBillItems(Long billId);
	
	public long createPropOwner(CommunityPmOwner communityPmOwner);
	public void updatePropOwner(CommunityPmOwner communityPmOwner);
	public void deletePropOwner(CommunityPmOwner communityPmOwner);
	public void deletePropOwner(long id);
	public CommunityPmOwner findPropOwnerById(long id);
	public List<CommunityPmOwner> listCommunityPmOwners(Long communityId, String address,String contactToken, Integer pageOffset,Integer pageSize);
	public List<CommunityPmOwner> listCommunityPmOwners(Long communityId, Long addressId);
	
	public List<CommunityPmTasks> listCommunityPmTasks(Long communityId, Long entityId, String entityType, 
            Long targetId, String targetType, String taskType,Byte status,Integer pageOffset,Integer pageSize);
	public List<CommunityPmTasks> findPmTaskEntityIdAndTargetId(Long communityId, Long entityId, String entityType, 
            Long targetId, String targetType, String taskType,Byte status);
    public void createPmTask(CommunityPmTasks task);
    public CommunityPmTasks findPmTaskById(long id);
	public CommunityPmTasks findPmTaskByEntityId(long communityId, long entityId, String entityType);
	public void updatePmTaskListStatus(List<CommunityPmTasks> tasks);
	public void updatePmTask(CommunityPmTasks task);
	
    public ListPropInvitedUserCommandResponse listInvitedUsers(Long communityId, String contactToken, Long pageOffset, Long pageSize);

	public List<String> listPropBillDateStr(Long communityId);
	
	public int countCommunityPmMembers(long communityId, String contactToken);
	public int countCommunityAddressMappings(Long organizationId, Long communityId, Byte livingStatus);
	public int countCommunityPmBills(long communityId, String dateStr, String address);
	public int countCommunityPmOwners(long communityId, String address, String contactToken);
	public int countCommunityPmTasks(Long communityId, Long entityId, String entityType, 
            Long targetId, String targetType, String taskType,Byte status);
	public int countCommunityPmTasks(Long communityId, String taskType,Byte status,String startTime,String endTime);

	public void createPropContact(CommunityPmContact communityPmContanct);
	public void updatePropContact(CommunityPmContact communityPmContanct);
	public void deletePropContact(CommunityPmContact communityPmContanct);
	public void deletePropContact(long id);
	public CommunityPmContact findPropContactById(long id);
	public List<CommunityPmContact> listCommunityPmContacts(Long communityId);

	public CommunityPmBill findNewestBillByAddressId(Long addressId);

	public List<CommunityPmBill> listNewestPmBillsByOrgId(Long organizationId, String address);

	public BigDecimal countPmYearIncomeByOrganizationId(Long organizationId, Integer resultCodeId);

	public CommunityPmBill findPmBillByAddressAndDate(Long integralTag1,java.sql.Date startDate, java.sql.Date endDate);

	public BigDecimal countFamilyPmBillDueAmountInYear(Long orgId, Long addressId);

	public CommunityPmBill findFamilyFirstPmBillInYear(Long orgId, Long addressId);

	public CommunityPmBill findPmBillByAddressIdAndTime(Long addressId,Date startDate, Date endDate);

	public OrganizationCommunity findPmCommunityByOrgId(Long organizationId);

	public CommunityAddressMapping findAddressMappingByAddressId(Long addressId);

	public List<CommunityAddressMapping> listAddressMappingsByOrgId(Long orgId);

	public List<CommunityPmBill> listPmBillsByOrgId(Long orgId,String address,java.sql.Date startDate, java.sql.Date endDate, long offset, int pageSize);

	public void updateOrganizationAddressMapping(CommunityAddressMapping mapping);

	public List<CommunityPmBillItem> listOrganizationBillItemsByBillId(Long billId);
	
	List<OrganizationTask> communityPmTaskLists(Long organizationId, Long communityId,String taskType,Byte status,String startTime,String endTime);

	List<CommunityPmOwner> listCommunityPmOwners(List<Long> ids);
	List<CommunityPmOwner> listCommunityPmOwnersByToken(Integer namespaceId, Long communityId, String contactToken);
	List<CommunityPmOwner> listCommunityPmOwnersByToken(Integer namespaceId, String contactToken);

    long createOrganizationOwnerBehavior(OrganizationOwnerBehavior behavior);

    OrganizationOwnerType findOrganizationOwnerTypeById(Long orgOwnerTypeId);

	/**
	 * 获取业主对应的ownerAddress
	 * @param namespaceId
	 * @param ownerId
	 * @return
	 */
	List<OrganizationOwnerAddress> listOrganizationOwnerAddressByOwnerId(Integer namespaceId, Long ownerId);

	/**
	 * 创建业主的附件记录
	 * @param attachment
	 * @return
	 */
    long createOrganizationOwnerAttachment(OrganizationOwnerAttachment attachment);

	/**
	 * 列出业主的附件列表
	 * @param namespaceId
	 * @param ownerId
	 * @return
	 */
	List<OrganizationOwnerAttachment> listOrganizationOwnerAttachments(Integer namespaceId, Long ownerId);

	/**
	 * 根据id 查询业主的附件记录
	 * @param namespaceId
	 * @param id
	 * @return
	 */
    OrganizationOwnerAttachment findOrganizationOwnerAttachment(Integer namespaceId, Long id);

	/**
	 * 删除业主的附件记录
	 * @param attachment
	 */
	void deleteOrganizationOwnerAttachment(OrganizationOwnerAttachment attachment);

    /**
     * 根据id获取业主行为记录
     * @param namespaceId
     * @param id
     * @return
     */
    OrganizationOwnerBehavior findOrganizationOwnerBehaviorById(Integer namespaceId, Long id);

    /**
     * 删除业主行为记录
     * @param behavior
     */
    void deleteOrganizationOwnerBehavior(OrganizationOwnerBehavior behavior);

    /**
     * 查询业主行为记录列表
     * @param namespaceId
     * @param ownerId
     * @return
     */
    List<OrganizationOwnerBehavior> listOrganizationOwnerBehaviors(Integer namespaceId, Long ownerId);

    List<OrganizationOwnerBehavior> listApartmentOrganizationOwnerBehaviors(Long addressId);

    /**
     * 创建地址与业主对应记录
     * @param ownerAddress
     */
    long createOrganizationOwnerAddress(OrganizationOwnerAddress ownerAddress);

    /**
     * 根据地址及业主获取业主与地址的映射记录
     * @param namespaceId
     * @param ownerId
     * @param addressId
     * @return
     */
    OrganizationOwnerAddress findOrganizationOwnerAddressByOwnerAndAddress(Integer namespaceId, Long ownerId, Long addressId);

    /**
     * 更新业主与地址的映射记录
     * @param ownerAddress
     */
    void updateOrganizationOwnerAddress(OrganizationOwnerAddress ownerAddress);

	/**
	 * 删除业主与地址的映射记录
	 * @param ownerAddress
	 */
    void deleteOrganizationOwnerAddress(OrganizationOwnerAddress ownerAddress);

	/**
	 * 删除业主与地址之间所有的关系记录
	 * @param namespaceId
	 * @param ownerId
     * @return 返回删除的记录数
	 */
    int deleteOrganizationOwnerAddressByOwnerId(Integer namespaceId, Long ownerId);

	/**
	 * 根据地址获取业主与地址之间的关系记录
	 * @param namespaceId
	 * @param addressId
	 * @return
	 */
	List<OrganizationOwnerDTO> listOrganizationOwnersByAddressId(Integer namespaceId, Long addressId, RecordMapper<Record, OrganizationOwnerDTO> mapper);

    OrganizationOwnerType findOrganizationOwnerTypeByDisplayName(String orgOwnerTypeName);

    /**
     * 根据小区查询业主列表
     * @param namespaceId
     * @param communityId
     * @return
     */
    List<CommunityPmOwner> listCommunityPmOwnersByCommunity(Integer namespaceId, Long communityId);

    /**
     * 创建车辆
     * @param car
     * @return
     */
    long createOrganizationOwnerCar(OrganizationOwnerCar car);

    /**
     * 根据小区id 及车牌号查询车辆
     *
     * @param namespaceId
     * @param communityId
     * @param plateNumber
     * @return
     */
    List<OrganizationOwnerCar> findOrganizationOwnerCarByCommunityIdAndPlateNumber(Integer namespaceId, Long communityId, String plateNumber);

    List<OrganizationOwnerCar> listOrganizationOwnerCarsByIds(List<Long> ids);

    List<OrganizationOwnerType> listOrganizationOwnerType();

    /**
     * 根据Id查询车辆
     *
     * @param namespaceId
     * @param id
     * @return
     */
    OrganizationOwnerCar findOrganizationOwnerCar(Integer namespaceId, Long id);

    /**
     * 更新车辆
     * @param car
     */
    void updateOrganizationOwnerCar(OrganizationOwnerCar car);

    List<CommunityPmOwner> listOrganizationOwners(Integer namespaceId, Long communityId, Long orgOwnerTypeId, String keyword, Long pageAnchor, Integer pageSize);

    /**
     * 创建车辆附件记录
     * @param attachment
     * @return
     */
    long createOrganizationOwnerCarAttachment(OrganizationOwnerCarAttachment attachment);

    /**
     * 根据车辆查询车辆对应的附件记录
     * @param namespaceId
     * @param carId
     * @return
     */
    List<OrganizationOwnerCarAttachment> listOrganizationOwnerCarAttachment(Integer namespaceId, Long carId);

    /**
     * 查询
     * @param namespaceId
     * @param id
     * @param carId
     * @return
     */
    OrganizationOwnerCarAttachment findOrganizationOwnerCarAttachment(Integer namespaceId, Long id);

    /**
     * 删除车辆附件
     * @param attachment
     */
    void deleteOrganizationOwnerCarAttachment(OrganizationOwnerCarAttachment attachment);

    /**
     * 创建车辆的使用者信息
     * @param ownerOwnerCar
     * @return
     */
    long createOrganizationOwnerOwnerCar(OrganizationOwnerOwnerCar ownerOwnerCar);

    /**
     * 列出车辆的使用者
     * @param namespaceId
     * @param carId
     * @param mapper
     * @return
     */
    <R> List<R> listOrganizationOwnersByCar(Integer namespaceId, Long carId, RecordMapper<Record, R> mapper);

    /**
     * 删除业主的附件记录
     * @param namespaceId   namespaceId
     * @param id    业主id
     * @return  返回删除的行数
     */
    int deleteOrganizationOwnerAttachmentByOwnerId(Integer namespaceId, Long id);

    /**
     * 删除业主与车辆之间的关联记录
     * 根据业主Id删除
     * @param namespaceId   namespaceId
     * @param id    业主id
     * @return  返回删除的行数
     */
    int deleteOrganizationOwnerOwnerCarByOwnerId(Integer namespaceId, Long id);

    /**
     * 删除业主与车辆之间的关联记录
     * 根据车辆id删除
     * @param namespaceId   namespaceId
     * @param id    车辆id
     * @return  返回删除的行数
     */
    int deleteOrganizationOwnerOwnerCarByCarId(Integer namespaceId, Long id);

    /**
     * 删除车辆的附件记录
     * @param namespaceId   namespaceId
     * @param id    车辆id
     * @return  返回删除的行数
     */
    int deleteOrganizationOwnerCarAttachmentByCarId(Integer namespaceId, Long id);

    /**
     * 删除用户及车辆之间的关联
     * 删除一条业主id与车辆id都符合条件的记录
     * @param namespaceId   namespaceId
     * @param ownerId   业主id
     * @param carId     车辆id
     */
    void deleteOrganizationOwnerOwnerCarByOwnerIdAndCarId(Integer namespaceId, Long ownerId, Long carId);

    /**
     * 根据业主id 及 车辆id 查询业主与车辆的对应记录
     * @param namespaceId
     * @param ownerId
     * @param carId
     * @return
     */
    OrganizationOwnerOwnerCar findOrganizationOwnerOwnerCarByOwnerIdAndCarId(Integer namespaceId, Long ownerId, Long carId);

    /**
     * 更新业主与车辆的记录
     * @param ownerOwnerCar
     */
    void updateOrganizationOwnerOwnerCar(OrganizationOwnerOwnerCar ownerOwnerCar);

    /**
     * 根据id获取业主与车辆关联的记录
     * @param namespaceId
     * @param id
     * @return
     */
    OrganizationOwnerOwnerCar findOrganizationOwnerOwnerCarById(Integer namespaceId, Long id);

    /**
     * 查询车辆的首要联系人
     * @param namespaceId
     * @param carId
     * @return
     */
    OrganizationOwnerOwnerCar findOrganizationOwnerCarPrimaryUser(Integer namespaceId, Long carId);

    /**
     * 查询该业主为使用者的车辆列表
     * @param namespaceId
     * @param ownerId
     * @return
     */
    List<OrganizationOwnerCar> listOrganizationOwnerCarByOwnerId(Integer namespaceId, Long ownerId);

    /**
     * 根据小区列出车辆
     * @param namespaceId
     * @param communityId
     * @return
     */
    List<OrganizationOwnerCar> listOrganizationOwnerCarsByCommunity(Integer namespaceId, Long communityId);

    /**
     * 根据性别统计业主数据
     *
     * @param communityId
     * @param livingStatus
     * @param orgOwnerTypeIds
     * @param mapper
     * @return
     */
    List<ListOrganizationOwnerStatisticDTO> listOrganizationOwnerStatisticByGender(
            Long communityId, Byte livingStatus, List<Long> orgOwnerTypeIds, RecordMapper<Record, ListOrganizationOwnerStatisticDTO> mapper);

    /**
     * 根据年龄统计业主数据
     * @param communityId
     * @param livingStatus
     * @param orgOwnerTypeIds
     * @param mapper
     * @return
     */
    List<ListOrganizationOwnerStatisticDTO> listOrganizationOwnerStatisticByAge(
            Long communityId, Byte livingStatus, List<Long> orgOwnerTypeIds, RecordMapper<Record, ListOrganizationOwnerStatisticDTO> mapper);

    /**
     * 查询楼栋门牌下的住户列表
     * @param namespaceId
     * @param addressId
     * @return
     */
    List<OrganizationOwnerAddress> listOrganizationOwnerAddressByAddressId(Integer namespaceId, Long addressId);

    /**
     * 根据小区及联系电话查询业主
     * @param namespaceId
     * @param communityId
     * @param contactToken
     * @return
     */
    CommunityPmOwner findOrganizationOwnerByCommunityIdAndContactToken(Integer namespaceId, Long communityId, String contactToken);

    /**
     * 停车类型列表
     * @return
     */
    List<EhParkingCardCategories> listParkingCardCategories();

    /**
     * 根据多个addressId查询业主与地址的关联记录
     * @param namespaceId
     * @param addressIds
     * @return
     */
    List<OrganizationOwnerAddress> listOrganizationOwnerAddressByAddressIds(Integer namespaceId, List<Long> addressIds);

    ParkingCardCategory findParkingCardCategory(Byte cardType);

	public Map<Long, Integer> mapOrganizationOwnerCountByAddressIds(Integer namespaceId, List<Long> addressIds);

	public Map<Long, CommunityAddressMapping> mapAddressMappingByAddressIds(List<Long> addressIds);

	Map<Long, CommunityAddressMapping> mapAddressMappingByAddressIds(List<Long> addressIds, Byte livingStatus);

	public Map<Long, CommunityPmBill> mapNewestBillByAddressIds(List<Long> addressIds);

	public OrganizationOwner findOrganizationOwnerById(Long organizationOwnerId);

	List<CommunityAddressMapping> listCommunityAddressMappingByAddressIds(List<Long> addressIds);

	List<OrganizationOwnerAddress> listOrganizationOwnerAuthAddressByAddressId(Integer namespaceId, Long addressId);

	List<CommunityPmOwner> listCommunityPmOwnersByTel(Integer currentNamespaceId, Long communityId, String tel);

	OrganizationOwnerBehavior findOrganizationOwnerBehaviorByOwnerAndAddressId(Long id, Long id1);

    void insertResourceReservation(PmResourceReservation resourceReservation);

    List<PmResourceReservation> listReservationsByAddresses(List<Long> ids);

    boolean isInvolvedWithReservation(Long addressId);

    List<PmResourceReservation> findReservationByAddress(Long addressId, ReservationStatus status);

	void updateReservation(Long reservationId, Timestamp startTime, Timestamp endTime,Long enterpriseCustomerId,Long addressId);

	Long changeReservationStatus(Long reservationId, Byte newStatus);

	List<ReservationInfo> listRunningReservations();

	Byte getReservationPreviousLivingStatusById(Long reservationId);


    List<CommunityPmOwner> listCommunityPmOwnersWithLocator(CrossShardListingLocator locator, Integer pageSize);

	//add by tangcen 2018年7月2日11:11:43
	PmResourceReservation findReservationById(Long reservationId);

	void updateReservation(PmResourceReservation oldReservation);

	void createAuthorizePrice(AddressProperties addressProperties);

	List<AddressProperties> listAuthorizePrices(Integer namespaceId, Long buildingId, Long communityId, Long pageAnchor, Integer pageSize);

	AddressProperties findAddressPropertiesById(Long id);

	void updateAuthorizePrice(AddressProperties addressProperties);

	void deleteAuthorizePrice(AddressProperties addressProperties);

	AddressProperties findAddressPropertiesByApartmentId(Community community, Long buildingId, Long address);

	CommunityPmOwner findOrganizationOwnerByContactToken(String contactToken, Integer namespaceId);

	CommunityPmOwner findOrganizationOwnerByContactExtraTels(String contactToken, Integer namespaceId);


}
