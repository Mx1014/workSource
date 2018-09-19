// @formatter:off
package com.everhomes.family;


import com.everhomes.acl.Role;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.core.AppConfig;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.group.Group;
import com.everhomes.group.GroupAdminStatus;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.region.RegionScope;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFamilyBillingAccountsDao;
import com.everhomes.server.schema.tables.daos.EhFamilyBillingTransactionsDao;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhFamilyBillingAccounts;
import com.everhomes.server.schema.tables.pojos.EhFamilyBillingTransactions;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.records.EhFamilyBillingAccountsRecord;
import com.everhomes.server.schema.tables.records.EhGroupMembersRecord;
import com.everhomes.user.UserGroup;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.*;
import com.mysql.jdbc.log.Log;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Family inherits from Group for family member management. GroupDiscriminator.FAMILY
 * distinguishes it from other group objects 
 * 
 */
@Component
public class FamilyProviderImpl implements FamilyProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(FamilyProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private CoordinationProvider coordinationProvider;

	@Autowired
	private GroupProvider groupProvider;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private RegionProvider regionProvider;

	@Autowired
	private AddressProvider addressProvider;

	@Autowired
	private CommunityProvider communityProvider;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private ContentServerService contentServerService;

	@Autowired
	private LocaleTemplateService localeTemplateService;

	//@Cacheable(value="Family", key="#addressId" ,unless="#result==null")
	@Override
	public Family findFamilyByAddressId(long addressId) {
		final Family[] result = new Family[1];
		dbProvider.mapReduce(AccessSpec.readWriteWith(EhGroups.class), result, 
				(DSLContext context, Object reducingContext) -> {
					List<Family> list = context.select().from(Tables.EH_GROUPS)
							.where(Tables.EH_GROUPS.INTEGRAL_TAG1.eq(addressId))
							.and(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode()))
							.fetch().map((r) -> {
								return ConvertHelper.convert(r, Family.class);
							});

					if(list != null && !list.isEmpty()){
						result[0] = list.get(0);
						return false;
					}

					return true;
				});

		return result[0];
	}
	
	@Override
	public Map<Long, Family> mapFamilyByAddressIds(List<Long> aptIdList) {
		 Map<Long, Family> result = new HashMap<>();
		dbProvider.mapReduce(AccessSpec.readWriteWith(EhGroups.class), result, 
				(DSLContext context, Object reducingContext) -> {
					context.select().from(Tables.EH_GROUPS)
						.where(Tables.EH_GROUPS.INTEGRAL_TAG1.in(aptIdList))
						.and(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode()))
						.fetch().map((r) -> {
							Family family = ConvertHelper.convert(r, Family.class);
							result.put(family.getAddressId(), family);
							return null;
						});
					return true;
				});

		return result;
	}

	//@Caching(evict = { @CacheEvict(value="Family", key="#group.integralTag1")} )
    @Override
    public void updateFamily(Group group) {
        this.groupProvider.updateGroup(group);
    }

	//@Caching(evict = { @CacheEvict(value="Family", key="#address.id")} )
	@Override
	public void leaveFamilyAtAddress(Address address, UserGroup userGroup) {
		this.coordinationProvider.getNamedLock(CoordinationLocks.LEAVE_FAMILY.getCode()).enter(()-> {

			this.dbProvider.execute((TransactionStatus status) -> {
				Family family = findFamilyByAddressId(address.getId());

				GroupMember m = this.groupProvider.findGroupMemberByMemberInfo(family.getId(), 
						EntityType.USER.getCode(), userGroup.getOwnerUid());
				assert(m != null);
				if(m != null) {
					// retrieve family info after membership changes
					this.groupProvider.deleteGroupMember(m);
					
					family = findFamilyByAddressId(address.getId());
					List<GroupMember> groupMembers = this.groupProvider.findGroupMemberByGroupId(family.getId());
					if(groupMembers == null || groupMembers.isEmpty()) {
						this.groupProvider.deleteGroup(family);
					} else {
						if(m.getMemberRole() == Role.ResourceCreator) {
							// reassign resource creator to other member
							GroupMember newCreator = pickOneMemberToPromote(family);
							if(newCreator != null){
								newCreator.setMemberRole(Role.ResourceCreator);
								this.groupProvider.updateGroupMember(newCreator);

								family.setCreatorUid(newCreator.getMemberId());
							}
						}

						//上面的this.groupProvider.deleteGroupMember(m);已经减过人数啦，这里不能再减啦 [大哭]   edit by yanjun 20170808
//						//删除正常家庭成员，成员数-1
//	                    if(m.getMemberStatus() == GroupMemberStatus.ACTIVE.getCode()){
//                            long memberCount = family.getMemberCount() - 1;
//                            family.setMemberCount(memberCount >= 0 ? memberCount : 0);
//	                    }

						this.groupProvider.updateGroup(family);
					}
				}

				this.userProvider.deleteUserGroup(userGroup);
				return null;
			});

			return null;
		});

	}
    

	private GroupMember pickOneMemberToPromote(Family family) {
		CrossShardListingLocator locator = new CrossShardListingLocator(family.getId());

		List<GroupMember> members = this.groupProvider.listGroupMembers(locator, Integer.MAX_VALUE);
		if(members == null || members.isEmpty())
			return null;
		return members.get(0);
	}

	//接口暂时不用
	@Override
	public Tuple<Integer, List<FamilyDTO>> findFamilByKeyword(String keyword) {
		if(StringUtils.isEmpty(keyword))
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid keyword parameter");
		List<FamilyDTO> results = new ArrayList<FamilyDTO>();
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhAddresses.class), null, 
				(DSLContext context, Object reducingContext)-> {

					String likeVal = keyword + "%";
					context.selectDistinct(Tables.EH_GROUPS.ID, Tables.EH_ADDRESSES.ADDRESS, Tables.EH_ADDRESSES.COMMUNITY_ID,
							Tables.EH_ADDRESSES.CITY_ID,Tables.EH_COMMUNITIES.NAME,Tables.EH_COMMUNITIES.CITY_NAME)
							.from(Tables.EH_GROUPS)
							.leftOuterJoin(Tables.EH_ADDRESSES)
							.on(Tables.EH_GROUPS.INTEGRAL_TAG1.eq(Tables.EH_ADDRESSES.ID))
							.leftOuterJoin(Tables.EH_COMMUNITIES)
							.on(Tables.EH_ADDRESSES.COMMUNITY_ID.equal(Tables.EH_COMMUNITIES.ID))
							.where((Tables.EH_GROUPS.NAME.like(likeVal)
									.or(Tables.EH_GROUPS.DISPLAY_NAME.like(likeVal)))
									.and(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode())))
									.fetch().map((r) -> {
										FamilyDTO family = new FamilyDTO();
										family.setId(r.getValue(Tables.EH_GROUPS.ID));
										family.setName(r.getValue(Tables.EH_GROUPS.NAME));
										family.setAddress(r.getValue(Tables.EH_ADDRESSES.ADDRESS));
										family.setCommunityId(r.getValue(Tables.EH_ADDRESSES.COMMUNITY_ID));
										family.setCommunityName(r.getValue(Tables.EH_COMMUNITIES.NAME));
										family.setCityId(r.getValue(Tables.EH_ADDRESSES.CITY_ID));
										family.setCityName(r.getValue(Tables.EH_COMMUNITIES.CITY_NAME));
										results.add(family);
										return null;
									});

					return true;
				});

		return new Tuple<Integer, List<FamilyDTO>>(ErrorCodes.SUCCESS, results);
	}


	private List<FamilyDTO> getUserOwningFamiliesByIds(List<Long> familyIds,Long userId){
		if(familyIds == null || familyIds.isEmpty())
			return null;
		List<FamilyDTO> familyList = new ArrayList<FamilyDTO>();

		for(Long familyId : familyIds){
			Group group = this.groupProvider.findGroupById(familyId);
			if(group == null || !group.getDiscriminator().equals(GroupDiscriminator.FAMILY.getCode())){
				LOGGER.error("Family is not exits or group is not family with the id.familyId=" + familyId);
				return null;
			}
			FamilyDTO family = ConvertHelper.convert(group,FamilyDTO.class);
			family.setAvatarUrl(parserUri(group.getAvatar(),EntityType.FAMILY.getCode(),group.getCreatorUid()));
			family.setAvatarUri(group.getAvatar());
			family.setAddressId(group.getIntegralTag1());
			long communityId = group.getIntegralTag2();
			Community community = this.communityProvider.findCommunityById(communityId);
			if(community != null){
				family.setCommunityId(communityId);
				family.setCommunityName(community.getName());
				family.setCommunityAliasName(community.getAliasName());
				family.setCityId(community.getCityId());
				family.setCityName(community.getCityName()+community.getAreaName());
				family.setCommunityType(community.getCommunityType());
				family.setDefaultForumId(community.getDefaultForumId());
				family.setFeedbackForumId(community.getFeedbackForumId());
				LOGGER.debug("community is :" + community.getName());
			}
			if(group.getCreatorUid().longValue() == userId.longValue())
				family.setAdminStatus(GroupAdminStatus.ACTIVE.getCode());

			GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(family.getId(), 
					EntityType.USER.getCode(), userId);
			if(member != null){
				family.setMemberNickName(member.getMemberNickName());
				family.setMembershipStatus(member.getMemberStatus());
				family.setMemberAvatarUrl(parserUri(member.getMemberAvatar(), EntityType.USER.getCode(), member.getCreatorUid()));
				family.setMemberAvatarUri(member.getMemberAvatar());
				family.setProofResourceUri(member.getProofResourceUri());
				family.setProofResourceUrl(parserUri(member.getProofResourceUri(), EntityType.USER.getCode(), member.getCreatorUid()));
			}

			Address address = this.addressProvider.findAddressById(group.getIntegralTag1());
			if(address != null){
				//用address字段覆盖family name
				family.setName(address.getAddress());
				family.setBuildingName(address.getBuildingName());
				family.setApartmentName(address.getApartmentName());
				family.setAddressStatus(address.getStatus());
				if (community == null) {
				    community = new Community();
                }
				String addrStr = FamilyUtils.joinDisplayName(community.getCityName(),community.getAreaName(), community.getName(), 
						address.getBuildingName(), address.getApartmentName());
				family.setDisplayName(addrStr);
				family.setAddress(addrStr);
			}
			familyList.add(family);
		}

		return familyList;
	}
	
	// @Cacheable(value="FamiliesOfUser", key="#userId", unless="#result.size() == 0")
	@Override
	public List<FamilyDTO> getUserFamiliesByUserId(long userId) {
		LOGGER.debug("userId :" + userId);

		List<UserGroup> list = this.userProvider.listUserGroups(userId, GroupDiscriminator.FAMILY.getCode());

		if(list == null || list.isEmpty())
			return null;
		LOGGER.info("Get user families by userId,userGroups size=" + list.size());
		List<Long> familyIds = new ArrayList<Long>();
		for(UserGroup u : list){
			GroupMember groupMember = this.groupProvider.findGroupMemberByMemberInfo(u.getGroupId(), EntityType.USER.getCode(), userId);
			if(groupMember != null){
				familyIds.add(u.getGroupId());
				LOGGER.debug("groupId is :" + u.getGroupId());
			}
		}
		LOGGER.info("Get user families by userId,familyIds size=" + familyIds.size());
		return getUserOwningFamiliesByIds(familyIds, userId);
	}


	@Override
	public List<GroupMember> listFamilyRequests(Long userId, Long familyId ,Long pageOffset) {

		final long pageSize = this.configurationProvider.getIntValue("pagination.page.size", 
				AppConfig.DEFAULT_PAGINATION_PAGE_SIZE);
		long offset = PaginationHelper.offsetFromPageOffset(pageOffset, pageSize);

		List<GroupMember> results = new ArrayList<GroupMember>();

		//查询待处理的审核，别人主动加入（WAITING_FOR_APPROVAL），被人拉入（WAITING_FOR_ACCEPTANCE）
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
				(DSLContext context, Object reducingContext)-> {

					context.select().from(Tables.EH_GROUP_MEMBERS)
					.where((Tables.EH_GROUP_MEMBERS.GROUP_ID.eq(familyId)
							.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS
									.eq(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode())))
									.or((Tables.EH_GROUP_MEMBERS.MEMBER_ID.eq(userId)
											.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS
													.eq(GroupMemberStatus.WAITING_FOR_ACCEPTANCE.getCode()))))
							)
							.limit((int)pageSize).offset((int)offset)
							.fetch().map((r) -> {
								results.add(ConvertHelper.convert(r,GroupMember.class));
								return null;
							});

					return true;
				});

		return results;
	}
	@Override
	public List<GroupMember> listFamilyMembers(ListingLocator locator, int count, 
			ListingQueryBuilderCallback queryBuilderCallback) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, locator.getEntityId()));

		final List<GroupMember> members = new ArrayList<GroupMember>();
		SelectQuery<EhGroupMembersRecord> query = context.selectQuery(Tables.EH_GROUP_MEMBERS);

		if(queryBuilderCallback != null)
			queryBuilderCallback.buildCondition(locator, query);

		if(locator.getAnchor() != null)
			query.addConditions(Tables.EH_GROUP_MEMBERS.ID.gt(locator.getAnchor()));
		query.addOrderBy(Tables.EH_GROUP_MEMBERS.ID.asc());
		query.addLimit(count);

		query.fetch().map((r) -> {
			members.add(ConvertHelper.convert(r, GroupMember.class));
			return null;
		});

		if(members.size() > 0) {
			locator.setAnchor(members.get(members.size() -1).getId());
		}

		return members;
	}


	@Override
	public List<FamilyDTO> listWaitApproveFamily(Long comunityId, Long offset, Long pageSize) {

		List<FamilyDTO> results = new ArrayList<FamilyDTO>();
		long size = pageSize;
		long offset_ = offset;
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
				(DSLContext context, Object reducingContext)-> {
					SelectConditionStep<Record> step = context.select().from(Tables.EH_GROUP_MEMBERS)
							.leftOuterJoin(Tables.EH_GROUPS)
							.on(Tables.EH_GROUPS.ID.eq(Tables.EH_GROUP_MEMBERS.GROUP_ID))
							.where(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS
									.eq(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode()))
									.and(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode()));

					if(comunityId != null && comunityId != 0){
						step.and(Tables.EH_GROUPS.INTEGRAL_TAG2.eq(comunityId));
					}
					step.orderBy(Tables.EH_GROUP_MEMBERS.PROOF_RESOURCE_URI.desc())
					.limit((int)size).offset((int) offset_)
					.fetch().map((r) ->{

						Address address = this.addressProvider.findAddressById(r.getValue(Tables.EH_GROUPS.INTEGRAL_TAG1));
						if (address == null) {
						    LOGGER.error("Address is not found,addressId=" + r.getValue(Tables.EH_GROUPS.INTEGRAL_TAG1));
						}
						Community community = null;
						if(address != null){
						    community = this.communityProvider.findCommunityById(address.getCommunityId());
	                        if(community == null) {
	                            LOGGER.error("Community is not found,communityId=" + address.getCommunityId());
	                        }
						}
						
						FamilyDTO f = new FamilyDTO();
						if(address != null){
    						f.setAddress(address.getAddress());
    						f.setAddressId(address.getId());
    						f.setApartmentName(address.getApartmentName());
    						f.setBuildingName(address.getBuildingName());
    						f.setAddressStatus(address.getStatus());
						}
						if(community != null){
						    f.setCityName(community.getCityName());
	                        f.setAreaName(community.getAreaName());
	                        f.setCommunityId(community.getId());
	                        f.setCommunityAliasName(community.getAliasName());
	                        f.setCommunityName(community.getName());
	                        f.setCommunityType(community.getCommunityType());
	                        f.setDefaultForumId(community.getDefaultForumId());
	                        f.setFeedbackForumId(community.getFeedbackForumId());
						}
						f.setId(r.getValue(Tables.EH_GROUPS.ID));
						f.setMemberCount(r.getValue(Tables.EH_GROUPS.MEMBER_COUNT));
						f.setMemberUid(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_ID));
						f.setMemberNickName(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_NICK_NAME));
						f.setMembershipStatus(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS));
						f.setProofResourceUri(r.getValue(Tables.EH_GROUP_MEMBERS.PROOF_RESOURCE_URI));
						f.setProofResourceUrl(parserUri(r.getValue(Tables.EH_GROUP_MEMBERS.PROOF_RESOURCE_URI),EntityType.FAMILY.getCode(),
								r.getValue(Tables.EH_GROUP_MEMBERS.CREATOR_UID)));
						f.setCreateTime(r.getValue(Tables.EH_GROUP_MEMBERS.CREATE_TIME));
						List<UserIdentifier> userIdentifiers = this.userProvider.listUserIdentifiersOfUser(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_ID));
						if(userIdentifiers != null && !userIdentifiers.isEmpty()){
							userIdentifiers.forEach((u) ->{
								if(u.getIdentifierType().byteValue() == IdentifierType.MOBILE.getCode()){
									f.setCellPhone(u.getIdentifierToken());
								}
							});
						}
						results.add(f);
						return null;
					});

					return true;
				});
		return results;
	}

	@Override
	public int countWaitApproveFamily(Long comunityId) {
	    final Integer[] count = new Integer[1];
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class),
                null, (DSLContext context, Object reducingContext) -> {
                    Integer c = context.selectCount().from(Tables.EH_GROUP_MEMBERS)
                    .leftOuterJoin(Tables.EH_GROUPS)
                    .on(Tables.EH_GROUPS.ID.eq(Tables.EH_GROUP_MEMBERS.GROUP_ID))
                    .where(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS
                            .eq(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode()))
                    .and(Tables.EH_GROUPS.INTEGRAL_TAG2.eq(comunityId))
                    .fetchOne(0,Integer.class);
                    count[0] = c;
                    return true;
                });
        return count[0];
//		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));
//
//		SelectConditionStep<Record1<Integer>> step = context.selectCount().from(Tables.EH_GROUP_MEMBERS)
//				.leftOuterJoin(Tables.EH_GROUPS)
//				.on(Tables.EH_GROUPS.ID.eq(Tables.EH_GROUP_MEMBERS.GROUP_ID))
//				.where(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS
//						.eq(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode()));
//
//		if(comunityId != null){
//			step.and(Tables.EH_GROUPS.INTEGRAL_TAG2.eq(comunityId));
//		}
//
//		return step.fetchOneInto(Integer.class);
	}

	private String parserUri(String uri,String ownerType, Long ownerId){
		try {
			if(!org.apache.commons.lang.StringUtils.isEmpty(uri))
				return contentServerService.parserUri(uri,ownerType,ownerId);

		} catch (Exception e) {
			LOGGER.error("Parser uri is error." + e.getMessage());
		}
		return null;

	}

	public List<Group> listCommunityFamily(Long communityId){
		List<Group> groupList = new ArrayList<Group>();
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class),
				groupList, (DSLContext context, Object reducingContext) -> {
					List<Group> list = context.select().from(Tables.EH_GROUPS)
							.where(Tables.EH_GROUPS.INTEGRAL_TAG2.eq(communityId))
							.and(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode()))
							.fetch().map((r) -> {
								return ConvertHelper.convert(r, Group.class);
							});

					if (list != null && !list.isEmpty()) {
						groupList.addAll(list);
					}

					return true;
				});

		return groupList;
	}

	@Override
	public int countUserByCommunityId(long communityId){
		final Integer[] count = new Integer[1];
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class),
				null, (DSLContext context, Object reducingContext) -> {
                    SelectQuery<Record1<Long>> query = context.select(Tables.EH_USER_GROUPS.OWNER_UID)
                            .from(Tables.EH_USER_GROUPS)
                            .where(Tables.EH_USER_GROUPS.REGION_SCOPE_ID.eq(communityId))
                            .and(Tables.EH_USER_GROUPS.REGION_SCOPE.eq(RegionScope.COMMUNITY.getCode()))
                            .and(Tables.EH_USER_GROUPS.GROUP_DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode()))
                            .and(Tables.EH_USER_GROUPS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode()))
                            .groupBy(Tables.EH_USER_GROUPS.OWNER_UID).getQuery();

                    Integer c = context.selectCount().from(query).fetchOneInto(Integer.class);
					count[0] = c;
					return true;
				});
		return count[0];
	}

	@Override
	public int countFamiliesByCommunityId(long communityId){
		final Integer[] count = new Integer[1];
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class),
				null, (DSLContext context, Object reducingContext) -> {
					Integer c = context.selectCount().from(Tables.EH_GROUPS)
							.where(Tables.EH_GROUPS.INTEGRAL_TAG2.eq(communityId))
							.and(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode()))
							.and(Tables.EH_GROUPS.MEMBER_COUNT.gt(0L))
							.and(Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode()))
							.fetchOne(0,Integer.class);
					count[0] = c;
					return true;
				});
		return count[0];
	}

	//@Cacheable(value="FamilyOfId", key="#familyId", unless="#result == null")
	@Override
	public FamilyDTO getFamilyById(Long familyId) {

		Group group = this.groupProvider.findGroupById(familyId);

		FamilyDTO family = null;
		if(group != null){
			family = ConvertHelper.convert(group,FamilyDTO.class);
			family.setAddressId(group.getIntegralTag1());
			Long communityId = group.getIntegralTag2();
			if(communityId != null) {
			    Community community = this.communityProvider.findCommunityById(communityId);
	            if(community != null){
	                family.setCommunityId(communityId);
	                family.setCommunityName(community.getName());
	                family.setCommunityAliasName(community.getAliasName());
	                family.setCityId(community.getCityId());
	                family.setCityName(community.getCityName());
	            }    
			}
		}

		return family;
	}

	@Override
	public List<GroupMember> listFamilyMembersByCityId(long cityId, int offset, int pageSize) {

		List<GroupMember> members = new ArrayList<GroupMember>();
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
				(DSLContext context, Object reducingContext) -> {

					context.select(Tables.EH_GROUP_MEMBERS.fields()).from(Tables.EH_GROUP_MEMBERS)
					.leftOuterJoin(Tables.EH_GROUPS)
					.on(Tables.EH_GROUP_MEMBERS.GROUP_ID.eq(Tables.EH_GROUPS.ID))
					.where(Tables.EH_GROUPS.INTEGRAL_TAG3.eq(cityId))
					.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode()))
					.and(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode()))
					.limit(pageSize).offset(offset)
					.fetch().map((r) -> {
						GroupMember member = new GroupMember();
						member.setId(r.getValue(Tables.EH_GROUP_MEMBERS.ID));
						member.setGroupId(r.getValue(Tables.EH_GROUP_MEMBERS.GROUP_ID));
						member.setMemberId(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_ID));
						member.setMemberNickName(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_NICK_NAME));
						member.setMemberAvatar(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_AVATAR));
						members.add(member);
						return null;
					});

					return true;
				});

		return members;
	}

	@Override
	public List<GroupMember> listFamilyMembersByCommunityId(long communityId, int offset, int pageSize) {

		List<GroupMember> members = new ArrayList<GroupMember>();
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
				(DSLContext context, Object reducingContext) -> {

					context.select(Tables.EH_GROUP_MEMBERS.fields()).from(Tables.EH_GROUP_MEMBERS)
					.leftOuterJoin(Tables.EH_GROUPS)
					.on(Tables.EH_GROUP_MEMBERS.GROUP_ID.eq(Tables.EH_GROUPS.ID))
					.where(Tables.EH_GROUPS.INTEGRAL_TAG2.eq(communityId))
					.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode()))
					.and(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode()))
					.limit(pageSize).offset(offset)
					.fetch().map((r) -> {
						GroupMember member = new GroupMember();
						member.setId(r.getValue(Tables.EH_GROUP_MEMBERS.ID));
						member.setGroupId(r.getValue(Tables.EH_GROUP_MEMBERS.GROUP_ID));
						member.setMemberId(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_ID));
						member.setMemberNickName(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_NICK_NAME));
						member.setMemberAvatar(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_AVATAR));
						members.add(member);
						return null;
					});

					return true;
				});

		return members;
	}

	@Override
	public List<GroupMember> listFamilyMembersByFamilyId(long groupId, int offset, int pageSize) {

		List<GroupMember> members = new ArrayList<GroupMember>();
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
				(DSLContext context, Object reducingContext) -> {

					context.select(Tables.EH_GROUP_MEMBERS.fields()).from(Tables.EH_GROUP_MEMBERS)
					.leftOuterJoin(Tables.EH_GROUPS)
					.on(Tables.EH_GROUP_MEMBERS.GROUP_ID.eq(Tables.EH_GROUPS.ID))
					.where(Tables.EH_GROUPS.ID.eq(groupId))
					.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode()))
					.limit(pageSize).offset(offset)
					.fetch().map((r) -> {
						GroupMember member = new GroupMember();
						member.setId(r.getValue(Tables.EH_GROUP_MEMBERS.ID));
						member.setGroupId(r.getValue(Tables.EH_GROUP_MEMBERS.GROUP_ID));
						member.setMemberId(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_ID));
						member.setMemberNickName(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_NICK_NAME));
						member.setMemberAvatar(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_AVATAR));
						members.add(member);
						return null;
					});

					return true;
				});

		return members;
	}

	@Override
	public List<GroupMember> listAllFamilyMembers(int offset, int pageSize) {

		List<GroupMember> members = new ArrayList<GroupMember>();
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
				(DSLContext context, Object reducingContext) -> {

					context.select(Tables.EH_GROUP_MEMBERS.fields()).from(Tables.EH_GROUP_MEMBERS)
					.leftOuterJoin(Tables.EH_GROUPS)
					.on(Tables.EH_GROUP_MEMBERS.GROUP_ID.eq(Tables.EH_GROUPS.ID))
					.where(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode()))
					.limit(pageSize).offset(offset)
					.fetch().map((r) -> {
						GroupMember member = new GroupMember();
						member.setId(r.getValue(Tables.EH_GROUP_MEMBERS.ID));
						member.setGroupId(r.getValue(Tables.EH_GROUP_MEMBERS.GROUP_ID));
						member.setMemberId(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_ID));
						member.setMemberNickName(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_NICK_NAME));
						member.setMemberAvatar(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_AVATAR));
						member.setMemberStatus(r.getValue(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS));
						member.setCreateTime(r.getValue(Tables.EH_GROUP_MEMBERS.CREATE_TIME));
						members.add(member);
						return null;
					});

					return true;
				});

		return members;
	}

	@Override
	public List<FamilyBillingTransactions> listFBillTx(int resultCodeId,Long addresssId,int pageSize, long offset) {
		List<FamilyBillingTransactions> list = new ArrayList<FamilyBillingTransactions>();
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null,
				(DSLContext context , Object object) -> {
					Result<Record> records = context.select().from(Tables.EH_FAMILY_BILLING_TRANSACTIONS)
							.where(Tables.EH_FAMILY_BILLING_TRANSACTIONS.OWNER_ID.eq(addresssId)
									.and(Tables.EH_FAMILY_BILLING_TRANSACTIONS.RESULT_CODE_ID.eq(resultCodeId)))
							.orderBy(Tables.EH_FAMILY_BILLING_TRANSACTIONS.CREATE_TIME.desc())
							.limit(pageSize).offset((int)offset)
							.fetch();
					if(records != null && !records.isEmpty()){
						records.stream().map(r -> {
							list.add(ConvertHelper.convert(r, FamilyBillingTransactions.class));
							return null;
						}).toArray();
					}

					return true;
				});

		return list;
	}

	@Override
	public FamilyBillingTransactions findFamilyBillTxByOrderId(Long orderId,Long familyId) {
		List<FamilyBillingTransactions> list = new ArrayList<FamilyBillingTransactions>();

		if(familyId != null){
			DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class,familyId));
			context.select().from(Tables.EH_FAMILY_BILLING_TRANSACTIONS)
			.where(Tables.EH_FAMILY_BILLING_TRANSACTIONS.ORDER_ID.eq(orderId))
			.fetch().map(r -> {
				list.add(ConvertHelper.convert(r, FamilyBillingTransactions.class));
				return null;
			});
		}
		else{
			this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null,(DSLContext context , Object object) -> {
				context.select().from(Tables.EH_FAMILY_BILLING_TRANSACTIONS)
				.where(Tables.EH_FAMILY_BILLING_TRANSACTIONS.ORDER_ID.eq(orderId))
				.fetch().map(r -> {
					list.add(ConvertHelper.convert(r, FamilyBillingTransactions.class));
					return null;
				});
				return true;
			});
		}
		
		if(list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	@Override
	public FamilyBillingAccount findFamilyBillingAccountByOwnerId(Long ownerId) {

		List<FamilyBillingAccount> list = new ArrayList<FamilyBillingAccount>();

		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
				(DSLContext context, Object object) -> {
					Result<Record> records = context.select().from(Tables.EH_FAMILY_BILLING_ACCOUNTS)
							.where(Tables.EH_FAMILY_BILLING_ACCOUNTS.OWNER_ID.eq(ownerId))
							.fetch();

					if(records != null && !records.isEmpty()){
						list.add(ConvertHelper.convert(records.get(0),FamilyBillingAccount.class));
					}

					return true;
				});

		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void createFamilyBillingAccount(FamilyBillingAccount fAccount) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class));

		InsertQuery<EhFamilyBillingAccountsRecord> query = context.insertQuery(Tables.EH_FAMILY_BILLING_ACCOUNTS);
		query.setRecord(ConvertHelper.convert(fAccount, EhFamilyBillingAccountsRecord.class));
		query.setReturning(Tables.EH_FAMILY_BILLING_ACCOUNTS.ID);
		query.execute();

		fAccount.setId(query.getReturnedRecord().getId());
		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhFamilyBillingAccounts.class, null);
	}

	@Override
	public void createFamilyBillingTransaction(
			FamilyBillingTransactions familyTx) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class));

		EhFamilyBillingTransactionsDao dao = new EhFamilyBillingTransactionsDao(context.configuration());
		dao.insert(ConvertHelper.convert(familyTx, EhFamilyBillingTransactions.class));

		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhFamilyBillingTransactions.class, null);

	}

	@Override
	public void updateFamilyBillingAccount(FamilyBillingAccount fAccount) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class));

		EhFamilyBillingAccountsDao dao = new EhFamilyBillingAccountsDao(context.configuration());
		dao.update(ConvertHelper.convert(fAccount, EhFamilyBillingAccounts.class));

		DaoHelper.publishDaoAction(DaoAction.MODIFY,  EhFamilyBillingAccounts.class, null);

	}

	@Override
	public FamilyBillingAccount findFamilyBillingAccountById(
			Long id) {
		List<FamilyBillingAccount> list = new ArrayList<FamilyBillingAccount>();
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
				(DSLContext context, Object object) -> {
					EhFamilyBillingAccountsDao dao = new EhFamilyBillingAccountsDao(context.configuration());
					EhFamilyBillingAccounts account = dao.findById(id);
					if(account != null)
						list.add(ConvertHelper.convert(account, FamilyBillingAccount.class));
					return true;
				});

		if(list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	@Override
	public BigDecimal countFamilyBillTxChargeAmountInYear(Long addressId) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.MONTH, cal.getActualMaximum(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
		Timestamp endStampInYear = new Timestamp(cal.getTime().getTime());
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 0);
		Timestamp startStampInYear = new Timestamp(cal.getTime().getTime());

		BigDecimal [] totalChargeAmount = new BigDecimal [1];

		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
				(DSLContext context , Object object) -> {
					Result<Record1<BigDecimal>> records = context.select(Tables.EH_FAMILY_BILLING_TRANSACTIONS.CHARGE_AMOUNT.sum()).from(Tables.EH_FAMILY_BILLING_TRANSACTIONS)
							.where(Tables.EH_FAMILY_BILLING_TRANSACTIONS.OWNER_ID.eq(addressId)
									.and(Tables.EH_FAMILY_BILLING_TRANSACTIONS.CREATE_TIME.greaterOrEqual(startStampInYear))
									.and(Tables.EH_FAMILY_BILLING_TRANSACTIONS.CREATE_TIME.lessOrEqual(endStampInYear)))
									.fetch();
					if(records != null && !records.isEmpty()){
						totalChargeAmount[0] = records.get(0).value1();
					}
					return true;
				});

		if(totalChargeAmount[0] == null)
			return BigDecimal.ZERO;
		return totalChargeAmount[0];
	}


	@Override
	public List<Family> listFamilByCommunityIdAndUid(Long communityId, Long uid) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class));

		SelectQuery<Record> query = context.select(Tables.EH_GROUPS.fields()).from(Tables.EH_GROUPS).getQuery();
		query.addJoin(Tables.EH_GROUP_MEMBERS, JoinType.JOIN, Tables.EH_GROUP_MEMBERS.GROUP_ID.eq(Tables.EH_GROUPS.ID));
		query.addConditions(Tables.EH_GROUPS.INTEGRAL_TAG2.eq(communityId));
		query.addConditions(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode()));
		query.addConditions(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.FAMILY.getCode()));
		query.addConditions(Tables.EH_GROUP_MEMBERS.MEMBER_ID.eq(uid));

		List<Group> groups = query.fetch().map(r -> RecordHelper.convert(r, Group.class));

		List<Family> families = new ArrayList<>();
		if(groups != null){
			for (Group group:  groups){
				families.add(ConvertHelper.convert(group, Family.class));
			}
		}

		return families;
	}


}
