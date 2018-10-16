// @formatter:off
package com.everhomes.organization;

import com.everhomes.community.Community;
import com.everhomes.enterprise.EnterpriseAddress;
import com.everhomes.group.GroupMemberCaches;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.CommunityPmBill;
import com.everhomes.organization.pm.CommunityPmOwner;
import com.everhomes.rest.asset.NoticeMemberIdAndContact;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.organization.*;
import com.everhomes.userOrganization.UserOrganizations;

import org.jooq.Condition;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OrganizationProvider {
    void createOrganization(Organization organization);

    void updateOrganization(Organization organization);

    void updateUserOrganization(UserOrganizations userOrganization);

    void updateOrganization(List<Long> ids, Byte status, Long uid, Timestamp now);

    void deleteOrganization(Organization organization);

    void deleteOrganizationById(Long id);

    Organization findOrganizationById(Long id);

    /**
     * 获取指定小区对应的政府机构。
     * 每个政府机构管辖着一定量的小区，如物业、业委管一个小区，居委和公安管理一个片区里的小区；
     *
     * @param communityId 小区ID
     * @return 小区列表
     */
    List<Organization> findOrganizationByCommunityId(Long communityId);

    List<Organization> findOrganizationByPath(String path);

    List<Organization> listOrganizations(String organizationType, String name, Integer pageOffset, Integer pageSize);

    List<Organization> listOrganizations(String organizationType, Integer namespaceId, Long parentId, Long pageAnchor, Integer pageSize);

    void createOrganizationMember(OrganizationMember organizationMember);

    void updateOrganizationMember(OrganizationMember organizationMember);

    void updateOrganizationMemberByOrgPaths(String path, Byte status, Long uid, Timestamp now);

    void deleteOrganizationMemberById(Long id);

    OrganizationMember findOrganizationMemberById(Long id);

    List<OrganizationMember> listOrganizationMembers(Long organizationId, Long memberUid, Long offset, Integer pageSize);

    List<OrganizationMember> listOrganizationMembers(Long memberUid);


    void createOrganizationCommunity(OrganizationCommunity organizationCommunity);

    void updateOrganizationCommunity(OrganizationCommunity organizationCommunity);

    void deleteOrganizationCommunity(OrganizationCommunity organizationCommunity);

    void deleteOrganizationCommunityById(Long id);

    void deleteOrganizationCommunityByOrgIds(List<Long> organizationIds);

    OrganizationCommunity findOrganizationCommunityById(Long id);

    OrganizationCommunity findOrganizationCommunityByOrgIdAndCmmtyId(Long orgId, Long cmmtyId);

    //组织-小区 的查询逻辑
    List<OrganizationCommunity> listOrganizationCommunities(Long organizationId, Integer pageOffset, Integer pageSize);

    List<OrganizationCommunity> listOrganizationCommunities(Long organizationId);

    OrganizationCommunity findOrganizationProperty(Long communityId);

    OrganizationCommunity findOrganizationPropertyCommunity(Long organizationId);

    List<OrganizationCommunity> listOrganizationByCommunityId(Long communityId);

    int countOrganizations(String type, String name);

    int countOrganizationMembers(Long organnizationId, Long memberUid);

    int countOrganizationCommunitys(Long organizationId);

    List<OrganizationCommunityDTO> findOrganizationCommunityByCommunityId(Long communityId);

    OrganizationDTO findOrganizationByIdAndOrgType(Long organizationId, String organizationType);

    OrganizationMember findOrganizationMemberByUIdAndOrgId(Long userId, Long organizationId);

    List<OrganizationMember> findOrganizationMemberByOrgIdAndUIdWithoutAllStatus(Long organizationId, Long userId);

    List<OrganizationMember> findOrganizationMembersByOrgIdAndUId(Long userId, Long organizationId);

    OrganizationMember findOrganizationMemberByOrgIdAndToken(String contactPhone, Long organizationId);

    OrganizationMember findMemberDepartmentByDetailId(Long detailId);

    List<OrganizationMember> findMemberJobPositionByDetailId(Long detailId);

    OrganizationMember findMemberJobLevelByDetailId(Long detailId);

    List<OrganizationMember> listOrganizationMembersByPhones(List<String> phones, Long departmentId);

    void createOrganizationTask(OrganizationTask task);

    OrganizationTask findOrganizationTaskById(Long taskId);

    Organization findOrganizationByCommunityIdAndOrgType(Long communityId, String organizationType);

    List<OrganizationBillingTransactions> listOrganizationBillingTransactions(Condition condition, long offset, int pageSize);

    List<CommunityPmOwner> listOrganizationOwnerByAddressIdAndOrgId(Long addressId, Long organizationId);

    OrganizationBillingAccount findOrganizationBillingAccount(Long organizationId);

    void createOrganizationBillingAccount(OrganizationBillingAccount oAccount);

    void createOrganizationBillingTransaction(OrganizationBillingTransactions orgTx);

    void updateOrganizationBillingAccount(OrganizationBillingAccount oAccount);

    List<Organization> listOrganizationByCondition(Condition condition);

    List<CommunityPmOwner> listOrgOwnerByOrgIdAndAddressId(Long organizationId, Long addressId);

    CommunityPmBill findOranizationBillById(Long id);

    List<OrganizationBillingTransactionDTO> listOrgBillTxByOrgId(Long orgId, int resultCode, Timestamp startTime, Timestamp endTime, String address, long offset, int i);

    List<OrganizationOwner> listOrganizationOwnersByOrgIdAndAddressId(Long organizationId, Long addressId);

    CommunityAddressMapping findOrganizationAddressMappingByOrgIdAndAddress(Long organizationId, String address);

    void deleteOrganizationBillById(Long id);

    void updateOrganizationBill(CommunityPmBill communBill);

    List<CommunityPmBill> listOrganizationBillsByAddressId(Long addreddId, long offset, int pageSize);

    OrganizationCommunity findOrganizationCommunityByOrgId(Long orgId);

    void updateOrganizationTask(OrganizationTask task);

    void deleteOrganizationMember(OrganizationMember organizationMember);

    List<OrganizationTask> listOrganizationTasksByOrgIdAndType(Long organizationId, String topicType, Byte taskType, int pageSize, long offset);

    OrganizationTask findOrgTaskByOrgIdAndEntityId(Long orgId, Long topicId);

    Organization findOrganizationByName(String name);

    List<OrganizationMember> listOrganizationMembersByOrgId(Long orgId);

    void createOrganizationOrder(OrganizationOrder order);

    List<OrganizationOrder> listOrgOrdersByBillIdAndStatus(Long billId, Byte status);

    OrganizationOrder findOrganizationOrderById(Long orderId);

    void updateOrganizationOrder(OrganizationOrder order);

    List<OrganizationOrder> listOrgOrdersByStatus(Byte code);

    CommunityAddressMapping findOrganizationAddressMappingByAddressId(Long addressId);

    List<Organization> listOrganizationByName(String orgName, String orgType);


    void addPmBuilding(OrganizationAssignedScopes pmBuilding);

    void deletePmBuildingById(Long id);

    void deletePmBuildingByOrganizationId(Long organizationId);

    List<OrganizationAssignedScopes> findPmBuildingId(Long orgId);

    List<OrganizationAssignedScopes> findUnassignedBuildingId(Long orgId);

    List<Organization> listPmManagements(ListingLocator locator, int count, Long orgId, Long communityId);

    List<OrganizationTask> listOrganizationTasksByOperatorUid(Long operatorUid, String taskType, int pageSize, long offset);

    int countDepartments(String superiorPath);

    List<Organization> listDepartments(String superiorPath, Integer pageOffset, Integer pageSize);

    List<OrganizationMember> listParentOrganizationMembers(String superiorPath, List<String> groupTypes, CrossShardListingLocator locator, Integer pageSize);

    boolean updateOrganizationMemberByIds(List<Long> ids, Organization org);

    List<Organization> listOrganizationByGroupTypes(String superiorPath, List<String> groupTypes);

    List<Organization> listOrganizationByGroupTypes(Long parentId, List<String> groupTypes);

    List<Organization> listOrganizationByGroupTypes(Long parentId, List<String> groupTypes, String keyworks);

    List<OrganizationMember> listOrganizationPersonnels(Integer namespaceId, String keywords, Organization orgCommoand, Byte contactSignedupStatus, VisibleFlag visibleFlag, CrossShardListingLocator locator, Integer pageSize);

    List<OrganizationMember> listOrganizationPersonnels(Integer namespaceId, String keywords, String identifierToken, Organization orgCommoand, Byte contactSignedupStatus, VisibleFlag visibleFlag, CrossShardListingLocator locator, Integer pageSize);

    Integer countOrganizationPersonnels(Integer namespaceId, Organization orgCommoand, Byte contactSignedupStatus, VisibleFlag visibleFlag);

    List<OrganizationMember> listOrganizationPersonnels(String keywords, Organization orgCommoand, Byte contactSignedupStatus, VisibleFlag visibleFlag, CrossShardListingLocator locator, Integer pageSize, ListOrganizationContactCommand listCommand);

    OrganizationMember findOrganizationPersonnelByPhone(Long id, String phone);

    OrganizationMemberDetails findOrganizationPersonnelByWorkEmail(Long orgId, String workEmail);

    /**
     * Create enterprise details
     *
     * @param organizationDetail
     */
    void createOrganizationDetail(OrganizationDetail organizationDetail);

    /**
     * Update enterprise details
     *
     * @param organizationDetail
     */
    void updateOrganizationDetail(OrganizationDetail organizationDetail);

    /**
     * Query enterprise information by organizationId
     *
     * @param organizationId
     * @return
     */
    OrganizationDetail findOrganizationDetailByOrganizationId(Long organizationId);

    void createOrganizationCommunityRequest(OrganizationCommunityRequest organizationCommunityRequest);

    void updateOrganizationCommunityRequest(OrganizationCommunityRequest organizationCommunityRequest);

    void updateOrganizationCommunityRequestByOrgIds(List<Long> orgIds, Byte status, Long uid, Timestamp now);

    List<OrganizationCommunityRequest> queryOrganizationCommunityRequestByCommunityId(ListingLocator locator, Long comunityId
            , int count, ListingQueryBuilderCallback queryBuilderCallback);

    OrganizationCommunityRequest findOrganizationCommunityRequestByOrganizationId(Long communityId, Long organizationId);

    List<OrganizationCommunityRequest> queryOrganizationCommunityRequests(CrossShardListingLocator locator, int count,
                                                                          ListingQueryBuilderCallback queryBuilderCallback);

    OrganizationAddress findOrganizationAddressByAddressId(Long addressId);

    void updateOrganizationAddress(OrganizationAddress oa);

    void deleteOrganizationAddressById(Long id);

    void deleteOrganizationAddress(OrganizationAddress address);

    List<OrganizationAddress> findOrganizationAddressByOrganizationId(
            Long organizationId);

    void deleteOrganizationAddressByOrganizationId(long organizationId);

    void deleteOrganizationAttachmentsByOrganizationId(long organizationId);

    Organization findOrganizationByAddressId(long addressId);

    List<OrganizationAttachment> listOrganizationAttachments(long organizationId);

    void createOrganizationAddress(OrganizationAddress address);

    void createOrganizationAttachment(OrganizationAttachment attachment);

    OrganizationCommunityRequest getOrganizationCommunityRequestByOrganizationId(Long organizationId);

    List<OrganizationAddress> listOrganizationAddressByBuildingId(Long buildingId, Integer pageSize, CrossShardListingLocator locator);

    List<Organization> listEnterpriseByNamespaceIds(Integer namespaceId, String organizationType, CrossShardListingLocator locator, Integer pageSize);

    List<Organization> listEnterpriseByNamespaceIds(Integer namespaceId, String organizationType,
                                                    Byte setAdminFlag, String keywords, CrossShardListingLocator locator, int pageSize);

    List<OrganizationMember> listOrganizationMembersByPhone(String phone);

    List<OrganizationAddress> listOrganizationAddressByBuildingName(String buildingName);

    Organization getOrganizationByGoupId(Long groupId);

    Long createOrUpdateOrganizationMemberDetail(OrganizationMemberDetails organizationMemberDetails);

    List<OrganizationMember> listOrganizationMembersByOrgIdAndMemberGroup(Long orgId, String memberGroup);

    List<OrganizationMember> getOrganizationMemberByOrgIds(List<Long> ids, Condition cond);

    List<OrganizationTask> listOrganizationTasksByTypeOrStatus(CrossShardListingLocator locator, List<Long> organizationIds, Long targetId, String taskType, Byte taskStatus, Byte visibleRegionType, Long visibleRegionId, int pageSize);

    List<OrganizationTaskTarget> listOrganizationTaskTargetsByOwner(String ownerType, Long ownerId, String taskType);

    List<OrganizationMember> listOrganizationMembersByUId(Long uId);

    List<OrganizationMember> listAllOrganizationMembersByUID(List<Long> uIds);

    List<OrganizationMember> listOrganizationMembersTargetIdExist();

    void createOrganizationOwner(OrganizationOwner owner);

    OrganizationOwner getOrganizationOwnerByTokenOraddressId(String contactToken, Long addressId);

    void deleteOrganizationOwnerById(Long id);

    void updateOrganizationOwner(OrganizationOwner organizationOwner);

    List<OrganizationOwner> listOrganizationOwnerByCommunityId(Long communityId, ListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback);

    List<OrganizationOwner> findOrganizationOwnerByTokenOrNamespaceId(String contactToken, Integer namespaceId);

    Organization findOrganizationByGroupId(Long groupId);

    List<Organization> listOrganizationByName(ListingLocator locator, int count, Integer namespaceId, String name);

    List<OrganizationMember> listOrganizationMemberByOrganizationIds(ListingLocator locator, int pageSize, Condition cond, List<Long> organizationIds);

    List<OrganizationMember> listOrganizationMemberByTokens(String contactPhone, List<Long> organizationIds);

    Organization findOrganizationByParentAndName(Long parentId, String name);

    //根据第三方机构token获取organization
    Organization findOrganizationByOrganizationToken(String organizationToken);

    List<Organization> listOrganizationsByIds(List<Long> ids);

    List<Organization> listOrganizationsByIds(Long... ids);

    List<Organization> listOrganizationsByIds(Set<Long> ids);

    Organization findOrganizationByNameAndNamespaceId(String name, Integer namespaceId);

    List<OrganizationMember> listOrganizationPersonnels(String keywords, List<Long> orgIds, Byte memberStatus,
                                                        Byte contactSignedupStatus, CrossShardListingLocator locator, Integer pageSize);

    GroupMemberCaches listGroupMessageMembers(Integer namespaceId, Long groupId, int pageSize);

    void evictGroupMessageMembers(Integer namespaceId, Long groupId, int pageSize);

    List<Organization> listOrganizationByEmailDomainAndNamespace(Integer namesapceId, String emailDomain, Long communityId);


    List<OrganizationMember> listOrganizationMembers(Long orgId, List<Long> memberUids);

    List<OrganizationCommunityRequest> listOrganizationCommunityRequests(Long communityId);

    List<OrganizationCommunityRequest> listOrganizationCommunityRequestsByOrganizationId(Long organizationId);

    void createOrganizationMemberLog(OrganizationMemberLog orgLog);

    List<OrganizationMemberLog> listOrganizationMemberLogs(Long id);


    List<OrganizationJobPositionMap> listOrganizationJobPositionMaps(Long organizationId);

    void deleteOrganizationJobPositionMapById(Long id);

    void createOrganizationJobPositionMap(OrganizationJobPositionMap organizationJobPositionMap);

    void createOrganizationJobPosition(OrganizationJobPosition organizationJobPosition);

    void updateOrganizationJobPosition(OrganizationJobPosition organizationJobPosition);

    OrganizationJobPosition findOrganizationJobPositionById(Long id);

    OrganizationJobPosition findOrganizationJobPositionByName(String ownerType, Long ownerId, String name);

    List<OrganizationJobPosition> listOrganizationJobPositions(String ownerType, Long ownerId, String keyword,
                                                               Long pageAnchor, Integer pageSize);

    List<Organization> listOrganizationByGroupTypes(Long parentId, List<String> groupTypes, String keyword, Long pageAnchor, Integer pageSize);

    List<Organization> listOrganizationByGroupTypesAndPath(String path, List<String> groupTypes, String keyword, Long pageAnchor, Integer pageSize);

    List<OrganizationCommunityRequest> listOrganizationCommunityRequests(List<Long> communityIds);

    OrganizationMember getOrganizationMemberByContactToken(Integer currentNamespaceId, String email);

    Organization findOrganizationByName(String name, Integer namespaceId);

    Integer getSignupCount(Long organizationId);

    OrganizationAddress findOrganizationAddress(Long organizationId, Long addressId, Long buildingId);

    void createOrganizationAddressMapping(CommunityAddressMapping addressMapping);

    void updateOrganizationAddressMapping(CommunityAddressMapping addressMapping);

    CommunityAddressMapping findOrganizationAddressMapping(Long organizationId, Long communityId, Long addressId);

    List<OrganizationJobPositionMap> listOrganizationJobPositionMapsByJobPositionId(Long jobPositionId);

    List<OrganizationMember> getOrganizationMemberByOrgIds(List<Long> ids, ListingQueryBuilderCallback queryBuilderCallback);

    OrganizationJobPositionMap getOrganizationJobPositionMapByOrgIdAndJobPostionId(Long organizationId, Long jobPostionId);

    List<Organization> listOrganizationByNamespaceType(Integer namespaceId, String namespaceType);

    List<OrganizationAddress> listOrganizationAddressByOrganizationId(Long organizationId);

    List<CommunityAddressMapping> listOrganizationAddressMappingByOrganizationIdAndCommunityId(Long organizationId,
                                                                                               Long communityId);

    List<EnterpriseAddress> listEnterpriseAddressByOrganization(Long organizationId);

    OrganizationAddress findOrganizationAddressByOrganizationIdAndAddressId(Long organizationId, Long addressId);

    List<CommunityAddressMapping> listOrganizationAddressMappingByNamespaceType(Long superOrganizationId,
                                                                                Long communityId, String namespaceType);

    void deleteOrganizationAddressMapping(CommunityAddressMapping organizationAddressMapping);

    Organization findOrganizationByNameAndNamespaceIdForJindie(String name, Integer namespaceId, String namespaceToken,
                                                               String namespaceType);

    List<OrganizationMember> listOrganizationMemberByPath(String keywords, String path, List<String> groupTypes, VisibleFlag visibleFlag, CrossShardListingLocator locator, Integer pageSize);

    List<OrganizationMember> listOrganizationMemberByPath(String keywords, String path, List<String> groupTypes, Byte contactSignedupStatus, VisibleFlag visibleFlag, CrossShardListingLocator locator, Integer pageSize);

    Integer countOrganizationMemberByPath(String keywords, String path, List<String> groupTypes, Byte contactSignedupStatus, VisibleFlag visibleFlag);


    List<Organization> listOrganizationByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor,
                                                             int pageSize);

    List<Organization> listOrganizationByUpdateTime(Integer namespaceId, Long timestamp, int pageSize);

    List<CommunityAddressMapping> listCsthomerelByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor, int pageSize);

    List<CommunityAddressMapping> listCsthomerelByUpdateTime(Integer namespaceId, Long timestamp, int pageSize);

    OrganizationMember findAnyOrganizationMemberByNamespaceIdAndUserId(Integer namespaceId, Long userId, String groupType);

    OrganizationAddress findActiveOrganizationAddressByAddressId(Long addressId);

    OrganizationMember findActiveOrganizationMemberByOrgIdAndUId(Long userId, Long organizationId);


    List<OrganizationMember> listOrganizationMembers(CrossShardListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback);

    List<OrganizationMember> listOrganizationMemberByPath(String path, List<String> groupTypes, String contactToken);

    Organization findOrganizationByName(String name, String groupType, Long parentId, Integer namespaceId);

    List listOrganizationByName(String name, String groupType, Long parentId, Integer namespaceId);

    List listOrganizationByName(String name, String groupType, Long parentId, Integer namespaceId, Long enterpriseId);

    void createImportFileTask(ImportFileTask importFileTask);

    void updateImportFileTask(ImportFileTask importFileTask);

    ImportFileTask findImportFileTaskById(Long id);

    Map<Long, BigDecimal> mapOrgOrdersByBillIdAndStatus(List<Long> billIds, byte organizationOrderStatus);

    /**
     * 查询企业下的用户
     *
     * @param locator
     * @param pageSize
     * @param queryBuilderCallback
     * @return
     */
    List<OrganizationMember> listUsersOfEnterprise(CrossShardListingLocator locator, int pageSize, ListingQueryBuilderCallback queryBuilderCallback);

    Integer countUsersOfEnterprise(CrossShardListingLocator locator, ListingQueryBuilderCallback queryBuilderCallback);

    List<OrganizationMember> convertMemberListAsDetailList(List<OrganizationMember> old_list);

    /**
     * New
     **/
    List<OrganizationMemberDetails> findDetailInfoListByIdIn(List<Long> detailIds);

    OrganizationMemberDetails findOrganizationMemberDetailsByDetailId(Long detailId);

    Long createOrganizationMemberDetails(OrganizationMemberDetails memberDetails);

    void updateOrganizationMemberDetails(OrganizationMemberDetails organizationMemberDetails, Long detailId);

    void deleteOrganizationMemberDetails(OrganizationMemberDetails organizationMemberDetails);

    OrganizationMemberDetails findOrganizationMemberDetailsByOrganizationIdAndContactToken(Long organizationId, String contactToken);

    Long createOrUpdateOrganizationMemberDetail(OrganizationMemberDetails organizationMemberDetails, Boolean needUpdate);

    Organization findUnderOrganizationByParentOrgId(Long parentOrgId);

    List<Long> listOrganizationIdByBuildingId(Long buildingId, byte setAdminFlag, int pageSize, CrossShardListingLocator locator);

    List<Long> listOrganizationIdByCommunityId(Long communityId, byte setAdminFlag, int pageSize, CrossShardListingLocator locator);

    List<Organization> listEnterpriseByNamespaceIds(Integer namespaceId, String organizationType, Byte setAdminFlag,
                                                    CrossShardListingLocator locator, int pageSize);

    List<OrganizationAddress> findOrganizationAddressByOrganizationIdAndBuildingId(
            Long organizationId, Long buildId);

    List<Organization> listEnterpriseByNamespaceIds(Integer namespaceId, String keywords, String organizationType, CrossShardListingLocator locator, Integer pageSize);

    List<OrganizationMember> listOrganizationMembersByOrgIdWithAllStatus(Long organizaitonId);

    List<OrganizationMemberLog> listOrganizationMemberLogs(List<Long> organizationIds, String userInfoKeyword, String keywords, CrossShardListingLocator locator, int pageSize);

	List<OrganizationMemberLog> listOrganizationMemberLogs(List<Long> organizationIds, String userInfoKeyword,String identifierToken, String keywords, CrossShardListingLocator locator, int pageSize);List<OrganizationMemberLog> listOrganizationMemberLogs(Long userId, List<Long> organizationIds,
														   Byte operationType);

    List<OrganizationMember> listOrganizationPersonnels(String userInfoKeyword, String identifierToken, String orgNameKeyword, List<Long> orgIds,
                                                        Byte memberStatus, Byte contactSignedupStatus, CrossShardListingLocator locator, int pageSize);

    List<UserOrganizations> listUserOrganizations(CrossShardListingLocator locator, int pageSize, ListingQueryBuilderCallback callback);

    Set<Long> listMemberDetailIdWithExclude(Integer namespaceId, String big_path, List<String> small_path);

    List<Long> listMemberDetailIdWithExclude(String keywords, Integer namespaceId, String big_path, List<String> small_path, Timestamp checkinTimeStart, Timestamp checkinTimeEnd, Timestamp dissmissTimeStart, Timestamp dissmissTimeEnd, CrossShardListingLocator locator, Integer pageSize, List<Long> notinDetails, List<Long> inDetails, List<String> groupTypes);

    boolean checkIfLastOnNode(Integer namespaceId, Long organizationId, String contactToken, String path);

    Map<Long, String> listOrganizationsOfDetail(Integer namespaceId, Long detailId, String organizationGroupType);

    // 根据人事档案id获取members，可能会获取到同一域空间下不同总公司的人
    List<OrganizationMember> listOrganizationMembersByDetailId(Long detailId, List<String> groupTypes);

    // 根据人事档案id和orgId获取members，只会获取到该公司下的人
    List<OrganizationMember> listOrganizationMembersByDetailIdAndOrgId(Long detailId, Long orgId, List<String> groupTypes);

    List<OrganizationMember> listOrganizationMembersByDetailIdAndPath(Long detailId, String path, List<String> groupTypes);

    Integer countUserOrganization(Integer namespaceId, Long communityId, Byte userOrganizationStatus, String namespaceUserType, Byte gender);

    Integer countUserOrganization(Integer namespaceId, Long communityId, Byte userOrganizationStatus);

    Integer countUserOrganization(Integer namespaceId, Long communityId);

    //	根据 group_type 查找薪酬组 added by R 20170630
    List<Organization> listOrganizationsByGroupType(String groupType, Long organizationId);

    //查询组织下内有效的人数
    Integer countOrganizationMemberDetailsByOrgId(Integer namespaceId, Long organizationId);

    OrganizationMemberDetails findOrganizationMemberDetailsByTargetId(Long targetId);

    //查询所有总公司
    List<Organization> listHeadEnterprises();

    /**
     * 查询非离职状态下所有员工的 detailId
     * added by R, 20170719
     */
    List<Organization> listOrganizationsByGroupType(String groupType, Long organizationId,
                                                    List<Long> orgIds, String groupName, Long creatorUid, CrossShardListingLocator locator, Integer pageSize);

    List listOrganizationMembersGroupByToken();

    List listOrganizationMemberByToken(String token);

    String getOrganizationNameById(Long targetId);

    List<TargetDTO> findOrganizationIdByNameAndAddressId(String targetName, List<Long> ids);

    List<UserOrganizations> listUserOrganizationByUserId(Long userId);

    List<OrganizationMember> listOrganizationPersonnelsWithDownStream(String keywords, Byte contactSignedupStatus, CrossShardListingLocator locator, Integer pageSize, ListOrganizationContactCommand listCommand, String filterScopeType, List<String> groupTypes);

    List<OrganizationMember> queryOrganizationPersonnelsWithDownStream(ListingLocator locator, Long organizationId, ListingQueryBuilderCallback queryBuilderCallback);

    List<Long> queryOrganizationPersonnelDetailIds(ListingLocator locator, Long organizationId, ListingQueryBuilderCallback queryBuilderCallback);

    List<Long> queryOrganizationPersonnelTargetIds(ListingLocator locator, Long organizationId, ListingQueryBuilderCallback queryBuilderCallback);

    Integer queryOrganizationPersonnelCounts(ListingLocator locator, Long organizationId, ListingQueryBuilderCallback queryBuilderCallback);

    // path查询接口
    List<OrganizationMember> listOrganizationMemberByPath(String path, List<String> groupTypes, List<String> tokens);

    void updateOrganizationMemberByDetailId(Long detailId, String contactToken, String contactName, Byte gender);

    List<OrganizationMember> listOrganizationMembersByOrganizationIdAndMemberGroup(Long organizationId, String memberGroup, String targetType);

    List<OrganizationMember> listOrganizationMembersByOrganizationIdAndMemberGroup(Long organizationId, String memberGroup, String targetType, Long targetId, int pageSize, ListingLocator locator);

    List<OrganizationMember> listOrganizationMembersByOrganizationIdAndMemberGroup(Long organizationId, String memberGroup, String targetType, Integer pageSize, ListingLocator locator);

    List<OrganizationMember> listOrganizationMembersByOrganizationIdAndMemberGroup(String memberGroup, String targetType, Long targetId);

    UserOrganizations findActiveAndWaitUserOrganizationByUserIdAndOrgId(Long userId, Long orgId);

    UserOrganizations findUserOrganizationById(Long id);

    List<OrganizationMember> listOrganizationMembersByIds(List<Long> ids);

    List<OrganizationMember> listOrganizationMembersByOrgIdAndMemberGroup(
            Long orgId, String memberGroup, Long userId);

    List<Organization> findNamespaceUnifiedSocialCreditCode(String unifiedSocialCreditCode, Integer namespaceId);

    void deleteOrganizationPersonelByJobPositionIdsAndDetailIds(List<Long> jobPositionIds, List<Long> detailIds);

    void deleteOrganizationMembersByGroupTypeWithDetailIds(Integer namespaceId, List<Long> detailIds, String groupType);

    List<OrganizationMember> listOrganizationPersonnels(String keywords, Organization orgCommoand, Byte contactSignedupStatus, VisibleFlag visibleFlag, CrossShardListingLocator locator, Integer pageSize);

    void updateOrganizationDefaultOrder(Integer namespaceId, Long orgId, Integer order);

    List listLapseOrganizations(Integer namespaceId);

    Integer updateOrganizationMembersToInactiveByPath(String path, Timestamp now);

    OrganizationMember findDepartmentMemberByTargetIdAndOrgId(Long userId, Long organizationId);

    CommunityOrganizationDetailDisplay findOrganizationDetailFlag(Integer namespaceId, Long communityId);

    void createCommunityOrganizationDetailDisplay(CommunityOrganizationDetailDisplay detailDisplay);

    void updateCommunityOrganizationDetailDisplay(CommunityOrganizationDetailDisplay detailDisplay);

    List checkOrgExistInOrgOrPaths(Integer namespaceId, Long organizationId, List<Long> orgIds, List<String> orgPaths);

    List<OrganizationMemberDetails> listOrganizationMemberDetails(Long ownerId);

    Integer queryOrganizationMemberDetailCounts(ListingLocator locator, Long organizationId, ListingQueryBuilderCallback queryBuilderCallback);

    List<OrganizationMemberDetails> queryOrganizationMemberDetails(ListingLocator locator, Long organizationId, ListingQueryBuilderCallback queryBuilderCallback);

    List<Long> listOrganizationPersonnelDetailIdsByDepartmentIds(List<Long> departmentIds);


    List<Organization> listPMOrganizations(Integer namespaceId);

    Organization findOrganizationByName(String groupType, String name, Long directlyEnterpriseId, Long groupId);

    OrganizationCommunityRequest getOrganizationRequest(Long organizationId);

    void deleteAllOrganizationAddressById(Long organizationId);

    List<NoticeMemberIdAndContact> findActiveUidsByTargetTypeAndOrgId(Long noticeObjId, String... targetType);

    OrganizationMemberDetails findOrganizationMemberDetailsByTargetIdAndOrgId(Long targetId, Long orgId);

    Integer getUserOrgAmount(Long targetId);

    OrganizationMemberDetails findOrganizationMemberDetailsByTargetId(
            Long targetId, Long organizationId);
    OrganizationMember findOrganizationMemberByContactTokenAndOrgId(
            Long organizationId, String contactToken);
    
	OrganizationMember findOrganizationMemberByOrgIdAndToken(
			String contactPhone, Long organizationId, String memberGroup);
 /**
	 * 通过项目ID 与 认证状态来查询项目下的用户
	 * @param namespaceId
	 * @param communityIds
	 * @param authStatus
	 * @param locator
	 * @param pageSize
	 * @return
	 */
	List<UserOrganizations>  findUserByCommunityIDAndAuthStatus(Integer namespaceId ,
			List<Long> communityIds , List<Integer> authStatus ,CrossShardListingLocator locator , int pageSize);

	List<OrganizationAddress> findOrganizationAddressByOrganizationIds(
			List<Long> organizationIds);

    OrganizationMember findMemberByType(Long aLong, String groupPath, String code);

    List<OrganizationMemberDetails>  listOrganizationMemberDetails(Long ownerId, String userName);
	TargetDTO findUserContactByUserId(Integer namespaceId, Long userId);

	Integer countOrganizationMemberDetails(Long orgId, Long departmentId);
}
