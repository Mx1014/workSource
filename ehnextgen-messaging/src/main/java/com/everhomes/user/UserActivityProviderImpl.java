package com.everhomes.user;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.group.GroupAdminStatus;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.organization.OrganizationCommunityRequestType;
import com.everhomes.rest.organization.UserOrganizationStatus;
import com.everhomes.rest.user.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserActivityProviderImpl implements UserActivityProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserActivityProviderImpl.class);
    
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public List<User> listUsers(List<Long> uids) {
        List<User> users = new ArrayList<User>();
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context, obj) -> {
            List<User> ret = context.select().from(Tables.EH_USERS).where(Tables.EH_USERS.ID.in(uids)).fetch().stream()
                    .map(r -> ConvertHelper.convert(r, User.class)).collect(Collectors.toList());
            users.addAll(ret);
            return true;
        });
        return users;
    }

    @Override
    public List<UserContact> listContactByUid(Long uid, ListingLocator locator, int count) {
        if (locator.getAnchor() == null) {
            locator.setAnchor(0L);
        }
        List<UserContact> contacts = new ArrayList<UserContact>();
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, uid));
        cxt.select().from(Tables.EH_USER_CONTACTS).where(Tables.EH_USER_CONTACTS.UID.eq(uid))
                .and(Tables.EH_USER_CONTACTS.CONTACT_ID.ne(0L)).and(Tables.EH_USER_CONTACTS.ID.gt(locator.getAnchor()))
                .limit(count + 1).fetch().forEach(contact -> {
                    UserContact c = ConvertHelper.convert(contact, UserContact.class);
                    contacts.add(c);
                });
        if (contacts.size() > 0) {
            locator.setAnchor(contacts.get(contacts.size() - 1).getId());
        }
        return contacts;
    }

    @Override
    public List<UserContact> listContacts(Long uid) {
        List<UserContact> contacts = new ArrayList<UserContact>();
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, uid));
        cxt.select().from(Tables.EH_USER_CONTACTS).where(Tables.EH_USER_CONTACTS.UID.eq(uid)).fetch()
                .forEach(contact -> {
                    UserContact c = ConvertHelper.convert(contact, UserContact.class);
                    contacts.add(c);
                });
        return contacts;
    }

    @Override
    public List<UserInvitation> listInvitationByUid(Long uid, CrossShardListingLocator locator, int count) {
        return listInvitationsByConditions(locator, count, Tables.EH_USER_INVITATIONS.OWNER_UID.eq(uid),
                Tables.EH_USER_INVITATIONS.STATUS.ne(InvitationStatus.inactive.getCode()));
    }

    @Override
    public List<UserInvitationRoster> listInvitationRoster(Set<Long> invitationIds) {

        List<UserInvitationRoster> invitations = new ArrayList<UserInvitationRoster>();
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context, object) -> {
            EhUserInvitationRosterDao rosterDao = new EhUserInvitationRosterDao(context.configuration());
            rosterDao.fetchByInviteId(invitationIds.toArray(new Long[invitationIds.size()])).forEach(roster -> {
                invitations.add(ConvertHelper.convert(roster, UserInvitationRoster.class));
            });
            return true;
        });
        return invitations;
    }

    @Override
    public void addContacts(List<UserContact> contacts, Long uid) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, uid));
        EhUserContactsDao dao = new EhUserContactsDao(context.configuration());
        dao.insert(contacts.toArray(new EhUserContacts[contacts.size()]));
    }

    @CacheEvict(allEntries = true, value = "listContactByUid")
    @Override
    public void updateContact(List<UserContact> contacts, Long uid) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, uid));
        EhUserContactsDao dao = new EhUserContactsDao(context.configuration());
        dao.update(contacts.toArray(new EhUserContacts[contacts.size()]));
    }

    @Override
    public void addInstalledApp(List<UserInstalledApp> insApps, Long uid) {
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, uid));
        EhUserInstalledAppsDao dao = new EhUserInstalledAppsDao(cxt.configuration());
        dao.insert(insApps.toArray(new EhUserInstalledApps[insApps.size()]));
    }

    @Override
    public void addBehavior(UserBehavior behavior, Long uid) {
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, uid));
        EhUserBehaviorsDao dao = new EhUserBehaviorsDao(cxt.configuration());
        dao.insert(behavior);
    }

    @Override
    public void addActivity(UserActivity activity, Long uid) {
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, uid));
        EhUserActivitiesDao dao = new EhUserActivitiesDao(cxt.configuration());
        dao.insert(activity);
    }

    @Override
    public void addLocation(UserLocation location, Long uid) {
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, uid));
        EhUserLocationsDao dao = new EhUserLocationsDao(cxt.configuration());
        dao.insert(location);

    }
    
    @Override
    public List<UserLocation> findLocation(Long uid) {
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, uid));
        return cxt.select().from(Tables.EH_USER_LOCATIONS).where(Tables.EH_USER_LOCATIONS.UID.eq(uid))
        .orderBy(Tables.EH_USER_LOCATIONS.CREATE_TIME.desc()).fetch().stream().map(r ->
            ConvertHelper.convert(r, UserLocation.class)).collect(Collectors.toList());
    }

    @Override
    public List<UserContact> listRetainUserContactByUid(Long uid) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, uid));
        Map<Long, UserContact> cache = new HashMap<Long, UserContact>();
        EhUserContactsDao dao = new EhUserContactsDao(context.configuration());
        dao.fetchByContactId(uid).forEach(item -> {
            cache.put(item.getUid(), ConvertHelper.convert(item, UserContact.class));
        });
        List<UserIdentifier> identifiers = listUserIndetifiersByUid(new ArrayList<Long>(cache.keySet()));
        identifiers.forEach(item -> {
            if (item.getIdentifierType().equals(IdentifierType.MOBILE.getCode())) {
                cache.get(item.getOwnerUid()).setContactPhone(item.getIdentifierToken());
                try{
                    cache.get(item.getOwnerUid()).setContactName(cache.get(item.getOwnerUid()).getContactName()); 
                }catch(Exception e){
                    
                }
                
            }
        });
        return new ArrayList<UserContact>(cache.values());
    }

    @Override
    public List<UserIdentifier> listUserIdentifiers(List<String> indentifierTokens) {
        List<UserIdentifier> userIdentifiers = new ArrayList<UserIdentifier>();
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUserIdentifiers.class), null, (context, object) -> {
            EhUserIdentifiersDao identifierDao = new EhUserIdentifiersDao(context.configuration());
            identifierDao.fetchByIdentifierToken(indentifierTokens.toArray(new String[indentifierTokens.size()]))
                    .forEach(identifier -> {
                        userIdentifiers.add(ConvertHelper.convert(identifier, UserIdentifier.class));
                    });
            return true;
        });
        return userIdentifiers;
    }

    private List<UserIdentifier> listUserIndetifiersByUid(List<Long> uids) {
        List<UserIdentifier> userIdentifiers = new ArrayList<UserIdentifier>();
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, (context, object) -> {
            EhUserIdentifiersDao identifierDao = new EhUserIdentifiersDao(context.configuration());
            identifierDao.fetchByOwnerUid(uids.toArray(new Long[uids.size()])).forEach(identifier -> {
                userIdentifiers.add(ConvertHelper.convert(identifier, UserIdentifier.class));
            });
            return true;
        });
        return userIdentifiers;
    }

    @Override
    public void addUserProfile(UserProfile userProfile) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserProfiles.class));
        userProfile.setId(id);
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, userProfile.getOwnerId()));
        EhUserProfilesDao dao = new EhUserProfilesDao(cxt.configuration());
        dao.insert(userProfile);

    }

    @Override
    public List<UserProfile> findProfileByUid(Long uid) {
        List<UserProfile> result = new ArrayList<UserProfile>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, uid));
        EhUserProfilesDao dao = new EhUserProfilesDao(context.configuration());
        result.addAll(dao.fetchByOwnerId(uid).stream().map(r -> ConvertHelper.convert(r, UserProfile.class))
                .collect(Collectors.toList()));
        return result;
    }

    @Override
    public List<User> listUnAuthUsersByProfileCommunityId(Integer namespaceId, Long communityId, Long anchor, int pagesize, Byte communityType, Byte userSourceType, String keywords) {
        return listUnAuthUsersByProfileCommunityId(namespaceId, communityId, anchor, pagesize, communityType, userSourceType, keywords, null, null);
    }

    @Override
    public List<User> listUnAuthUsersByProfileCommunityId(Integer namespaceId, Long communityId, Long anchor, int pagesize, Byte communityType, Byte userSourceType, String keywords, Long startTime, Long endTime) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class));

        SelectQuery<Record> query = context.select(Tables.EH_USERS.fields()).from(Tables.EH_USERS).getQuery();
        query.addJoin(Tables.EH_USER_PROFILES, JoinType.JOIN, Tables.EH_USERS.ID.eq(Tables.EH_USER_PROFILES.OWNER_ID));
        query.addConditions(Tables.EH_USER_PROFILES.ITEM_NAME.in(UserCurrentEntityType.COMMUNITY_COMMERCIAL.getUserProfileKey(), UserCurrentEntityType.COMMUNITY_RESIDENTIAL.getUserProfileKey()));

        if(communityId != null){
            query.addConditions(Tables.EH_USER_PROFILES.ITEM_VALUE.eq(String.valueOf(communityId)));
        }
        if(namespaceId != null){
            query.addConditions(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId));
        }

        if(UserSourceType.WEIXIN == UserSourceType.fromCode(userSourceType)){
            query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.eq(NamespaceUserType.WX.getCode()));
        }else if(UserSourceType.APP == UserSourceType.fromCode(userSourceType)){
            query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.isNull());
        }else if(UserSourceType.ALIPAY == UserSourceType.fromCode(userSourceType)){
            query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.eq(NamespaceUserType.ALIPAY.getCode()));
        }

        excludeAuthUser(context, query, namespaceId, communityType);

        if (anchor != null){
            query.addConditions(Tables.EH_USERS.ID.lt(anchor));
        }

        if(StringUtils.isNotEmpty(keywords)){
            query.addJoin(Tables.EH_USER_IDENTIFIERS, JoinType.LEFT_OUTER_JOIN, Tables.EH_USERS.ID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID));
            Condition cond = Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.like("%" + keywords + "%");
            cond = cond.or(Tables.EH_USERS.NICK_NAME.like("%" + keywords + "%"));
            query.addConditions(cond);
        }

        if (startTime != null && endTime != null) {
            query.addConditions(Tables.EH_USERS.CREATE_TIME.gt(new Timestamp(startTime)));
            query.addConditions(Tables.EH_USERS.CREATE_TIME.le(new Timestamp(endTime)));
        }

        query.addOrderBy(Tables.EH_USERS.ID.desc());
        query.addLimit(pagesize);

        return query.fetch().map((Record record) -> record.into(User.class));
    }

    private void excludeAuthUser(DSLContext context, SelectQuery<Record> query, Integer namespaceId, Byte communityType){
        if(CommunityType.fromCode(communityType) == CommunityType.COMMERCIAL){
            //子查询的条件
            Condition subQueryCondition =Tables.EH_USER_ORGANIZATIONS.STATUS.in(UserOrganizationStatus.ACTIVE.getCode(), UserOrganizationStatus.WAITING_FOR_APPROVAL.getCode())
                    .and(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_TYPE.eq(OrganizationCommunityRequestType.Organization.getCode()));
            if(namespaceId != null){
                subQueryCondition = subQueryCondition.and(Tables.EH_USER_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId));
            }
            //子查询排除已认证的
            query.addConditions(Tables.EH_USERS.ID.notIn(
                    context.select(Tables.EH_USER_ORGANIZATIONS.USER_ID).from(Tables.EH_USER_ORGANIZATIONS)
                            .join(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS)
                            .on(Tables.EH_USER_ORGANIZATIONS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.MEMBER_ID))
                            .where(subQueryCondition))
            );
        }else {

            //子查询的条件
            Condition subQueryCondition = Tables.EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode())
                    .and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.notIn(GroupMemberStatus.INACTIVE.getCode(), GroupMemberStatus.REJECT.getCode()))
                    .and(Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode()));

            if(namespaceId != null){
                subQueryCondition = subQueryCondition.and(Tables.EH_GROUPS.NAMESPACE_ID.eq(namespaceId));
            }

            //子查询排除已认证、认证中的
            query.addConditions(Tables.EH_USERS.ID.notIn(
                    context.select(Tables.EH_GROUP_MEMBERS.MEMBER_ID).from(Tables.EH_GROUP_MEMBERS)
                            .join(Tables.EH_GROUPS)
                            .on(Tables.EH_GROUP_MEMBERS.GROUP_ID.eq(Tables.EH_GROUPS.ID))
                            .where(subQueryCondition))
            );

        }

    }

    @Override
    public void updateUserProfile(UserProfile userProfile) {
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, userProfile.getOwnerId()));
        EhUserProfilesDao dao = new EhUserProfilesDao(cxt.configuration());
        dao.update(userProfile);
    }

    @Override
    public void deleteProfile(UserProfile userProfile) {
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, userProfile.getOwnerId()));
        EhUserProfilesDao dao = new EhUserProfilesDao(cxt.configuration());
        dao.delete(userProfile);
    }

    public List<UserInvitation> listInvitationsByConditions(CrossShardListingLocator locator, int count,
            Condition... conditons) {
        List<UserInvitation> userInvitations = new ArrayList<UserInvitation>();
        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhUserIdentifiers.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhUserInvitationsRecord> query = context.selectQuery(Tables.EH_USER_INVITATIONS);

            if (locator.getAnchor() != null)
                query.addConditions(Tables.EH_USER_INVITATIONS.ID.gt(locator.getAnchor()));
            if (conditons != null) {
                query.addConditions(conditons);
            }
            query.addLimit(count - userInvitations.size());

            query.fetch().map((r) -> {
                userInvitations.add(ConvertHelper.convert(r, UserInvitation.class));
                return null;
            });

            if (userInvitations.size() >= count) {
                locator.setAnchor(userInvitations.get(userInvitations.size() - 1).getId());
                return AfterAction.done;
            }
            return AfterAction.next;
        });

        if (userInvitations.size() > 0) {
            locator.setAnchor(userInvitations.get(userInvitations.size() - 1).getId());
        }

        return userInvitations;
    }

    @Override
    public UserProfile findUserProfileBySpecialKey(Long uid, String key) {
        UserProfile profie = null;
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, uid));

        Record result = context.select().from(Tables.EH_USER_PROFILES).where(Tables.EH_USER_PROFILES.OWNER_ID.eq(uid))
                .and(Tables.EH_USER_PROFILES.ITEM_NAME.eq(key)).fetchAny();
        if (result != null)
            profie = ConvertHelper.convert(result, UserProfile.class);
        return profie;
    }

    @Override
    public void updateUserProfile(Long uid, String key, String... content) {
        UserProfile profile = findUserProfileBySpecialKey(uid, key);
        if (profile == null) {
            profile = new UserProfile();
            profile.setAppId(17L);
            profile.setItemName(key);
            profile.setItemKind((byte) 1);
            profile.setItemValue(StringUtils.join(content, ","));
            profile.setOwnerId(uid);
            addUserProfile(profile);
            return;
        }
        profile.setItemValue(StringUtils.join(content, ","));
        updateUserProfile(profile);
    }
    
    @Override
    public void updateUserCurrentEntityProfile(Long uid, String key, Long entityId, Long timestemp, Integer namespaceId) {
        UserProfile profile = findUserProfileBySpecialKey(uid, key);
        if (profile == null) {
            profile = new UserProfile();
            profile.setAppId(17L);
            profile.setItemName(key);
            profile.setItemKind((byte) 1);
            profile.setItemValue(String.valueOf(entityId));
            profile.setIntegralTag1(timestemp);
            profile.setIntegralTag2((long)namespaceId);
            profile.setOwnerId(uid);
            addUserProfile(profile);
        } else {
            profile.setItemValue(String.valueOf(entityId));
            profile.setIntegralTag1(timestemp);
            updateUserProfile(profile);
        }
    }

    @Override
    public void addFeedback(Feedback feedback, Long uid) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, uid));
        EhFeedbacksDao dao = new EhFeedbacksDao(context.configuration());
        dao.insert(feedback);
    }

    @Override
	public List<Feedback> ListFeedbacks(CrossShardListingLocator locator, Integer namespaceId, Byte targetType,  Byte status, int pageSize) {
    	final List<Feedback> feedbacks = new ArrayList<>();

        if(locator.getShardIterator() == null) {
		    AccessSpec accessSpec = AccessSpec.readOnlyWith(EhUsers.class);
		    ShardIterator shardIterator = new ShardIterator(accessSpec);
		    locator.setShardIterator(shardIterator);
        }

        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
        	SelectQuery<EhFeedbacksRecord> query = context.selectQuery(Tables.EH_FEEDBACKS);
        	Condition condition = Tables.EH_FEEDBACKS.NAMESPACE_ID.eq(namespaceId);
        	if(targetType != null){
        		condition = condition.and(Tables.EH_FEEDBACKS.TARGET_TYPE.eq(targetType));
        	}
        	if(status != null){
        		condition = condition.and(Tables.EH_FEEDBACKS.STATUS.eq(status));
        	}
        	if(locator.getAnchor() != null){
        		condition = condition.and(Tables.EH_FEEDBACKS.ID.lt(locator.getAnchor()));
        	}
        	query.addConditions(condition);
        	query.addOrderBy(Tables.EH_FEEDBACKS.ID.desc());
        	query.addLimit(pageSize);
        	query.fetch().map((r) -> {
        		feedbacks.add(ConvertHelper.convert(r, Feedback.class));
        		return null;
        	});
        	if(feedbacks.size() >= pageSize) {
        		locator.setAnchor(feedbacks.get(feedbacks.size() - 1).getId());
        		return AfterAction.done;
        	}
        	return AfterAction.next;
        });

        return feedbacks;
	}

    @Override
    public List<Feedback> ListFeedbacksByNamespaceId(Integer namespaceId) {
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWriteWith(EhFeedbacks.class));
        return cxt.select().from(Tables.EH_FEEDBACKS).where(Tables.EH_FEEDBACKS.NAMESPACE_ID.eq(namespaceId))
                .orderBy(Tables.EH_FEEDBACKS.CREATE_TIME.desc()).fetch().stream().map(r ->
                        ConvertHelper.convert(r, Feedback.class)).collect(Collectors.toList());
    }

    @Override
	public void updateFeedback(Feedback feedback) {
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, feedback.getOwnerUid()));
    	EhFeedbacksDao dao = new EhFeedbacksDao(context.configuration());
    	dao.update(feedback);
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFeedbacksDao.class, feedback.getId());
	}
    

    @Override
    public void updateOtherFeedback(Long targetId, Long feedbackId, Byte verifyType, Byte handleType) {
    	AccessSpec accessSpec = AccessSpec.readOnlyWith(EhUsers.class);
    	ShardIterator shardIterator = new ShardIterator(accessSpec);

    	this.dbProvider.iterationMapReduce(shardIterator, null, (DSLContext context, Object reducingContext) -> {
    		context.update(Tables.EH_FEEDBACKS)
    		.set(Tables.EH_FEEDBACKS.STATUS, (byte)1)
    		.set(Tables.EH_FEEDBACKS.VERIFY_TYPE, verifyType)
    		.set(Tables.EH_FEEDBACKS.HANDLE_TYPE, handleType)
    		.where(Tables.EH_FEEDBACKS.TARGET_ID.eq(targetId).and(Tables.EH_FEEDBACKS.ID.ne(feedbackId)))
    		.execute();
    		return AfterAction.next;
    	});
    }
    
    @Override
    public Feedback findFeedbackById(Long id) {
    	List<Feedback> feedbackList = new ArrayList<Feedback>();
    	dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), feedbackList, (DSLContext context, Object reducingContext) -> {
    		List<Feedback> list = context.select().from(Tables.EH_FEEDBACKS)
    				.where(Tables.EH_FEEDBACKS.ID.eq(id))
    				.fetch().map((r) -> {
    					return ConvertHelper.convert(r, Feedback.class);
    				});

    		if (list != null && !list.isEmpty()) {
    			feedbackList.add(list.get(0));
    		}
    		return true;
    	});

    	if(feedbackList.size() > 0){
    		return feedbackList.get(0);
    	}
    	return null;
    }
    
    @Override
    public List<UserFavoriteDTO> findFavorite(Long uid) {
        //TODO
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, uid));
        return context.select().from(Tables.EH_USER_FAVORITES).where(Tables.EH_USER_FAVORITES.OWNER_UID.eq(uid))
                .orderBy(Tables.EH_USER_FAVORITES.CREATE_TIME.desc()).fetch().stream().map(r -> ConvertHelper.convert(r, UserFavoriteDTO.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<UserFavoriteDTO> findFavorite(Long uid, String targetType, Long targetId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, uid));
        return context.select().from(Tables.EH_USER_FAVORITES).where(Tables.EH_USER_FAVORITES.OWNER_UID.eq(uid)
            .and(Tables.EH_USER_FAVORITES.TARGET_TYPE.eq(targetType).and(Tables.EH_USER_FAVORITES.TARGET_ID.eq(targetId))))
                .fetch().stream().map(r -> ConvertHelper.convert(r, UserFavoriteDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void addUserFavorite(UserFavorite userFavorite) {
    	Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserFavorites.class));
    	userFavorite.setId(id);
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class,
                userFavorite.getOwnerUid()));
        EhUserFavoritesDao dao = new EhUserFavoritesDao(context.configuration());
        dao.insert(userFavorite);
        if (userFavorite.getTargetType() != null && userFavorite.getTargetType().equalsIgnoreCase("topic")) {
            updateProfileIfNotExist(userFavorite.getOwnerUid(), UserProfileContstant.FAVOTITE_TOPIC_COUNT, 1);
            return;
        }
        if (userFavorite.getTargetType() != null && userFavorite.getTargetType().equalsIgnoreCase("biz")) {
            updateProfileIfNotExist(userFavorite.getOwnerUid(), UserProfileContstant.FAVOTITE_BIZ_COUNT, 1);
        }

    }

    @Override
    public void updateProfileIfNotExist(Long uid, String key, Integer val) {
        UserProfile userProfile = null;
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, uid));
        Record result = context.select().from(Tables.EH_USER_PROFILES).where(Tables.EH_USER_PROFILES.OWNER_ID.eq(uid))
                .and(Tables.EH_USER_PROFILES.ITEM_NAME.eq(key)).fetchAny();
        if (result != null) {
            userProfile = ConvertHelper.convert(result, UserProfile.class);
        }

        if (userProfile == null) {
            long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserProfiles.class));
            userProfile = new UserProfile();
            userProfile.setOwnerId(uid);
            userProfile.setItemName(key);
            userProfile.setItemKind((byte) 1);
            userProfile.setItemValue(val + "");
            userProfile.setId(id);
            EhUserProfilesDao dao = new EhUserProfilesDao(context.configuration());
            dao.insert(userProfile);
            return;
        }
        EhUserProfilesDao dao = new EhUserProfilesDao(context.configuration());
        userProfile.setItemValue(convert(userProfile, val) + "");
        dao.update(userProfile);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserProfiles.class, userProfile.getId());

    }

    private int convert(UserProfile userProfile, int val) {
        int value = NumberUtils.toInt(userProfile.getItemValue(), 0);
        return value + val;
    }

    @Override
    public void addPostedTopic(Long ownerId, String targetType, Long postId) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserPosts.class));
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Get user posted post table sequence, ownerId=" + ownerId + ", postId=" + postId + ", sequence=" + id);
        }
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, ownerId));
        EhUserPostsDao dao = new EhUserPostsDao(context.configuration());
        UserPost post = new UserPost();
        post.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        post.setId(id);
        post.setOwnerUid(ownerId);
        post.setTargetType(targetType);
        post.setTargetId(postId);
        dao.insert(post);

    }
    
    @Override
    public int deletePostedTopic(Long ownerId, Long postId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, ownerId));
        DeleteQuery<EhUserPostsRecord> query = context.deleteQuery(Tables.EH_USER_POSTS);
        query.addConditions(Tables.EH_USER_POSTS.OWNER_UID.eq(ownerId));
        query.addConditions(Tables.EH_USER_POSTS.TARGET_ID.eq(postId));
        return query.execute();
    }

    @Override
    public List<UserPost> listPostedTopics(Long uid) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, uid));
        List<UserPost> posts = new ArrayList<UserPost>();
        EhUserPostsDao dao = new EhUserPostsDao(context.configuration());
        posts.addAll(dao.fetchByOwnerUid(uid).stream().map(r -> ConvertHelper.convert(r, UserPost.class))
                .collect(Collectors.toList()));
        return posts;
    }

    @Override
    public void deleteFavorite(Long uid, Long targetId, String type) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, uid));
        UserFavorite favorite = new UserFavorite();
        favorite.setTargetId(targetId);
        favorite.setTargetType(type);
        favorite.setOwnerUid(uid);
        //user sql delete
        context.delete(Tables.EH_USER_FAVORITES).where(Tables.EH_USER_FAVORITES.OWNER_UID.eq(uid))
                .and(Tables.EH_USER_FAVORITES.TARGET_ID.eq(targetId))
                .and(Tables.EH_USER_FAVORITES.TARGET_TYPE.eq(type)).execute();
        if (favorite.getTargetType() != null && (favorite.getTargetType().equalsIgnoreCase(UserFavoriteTargetType.TOPIC.getCode()) 
        		|| favorite.getTargetType().equalsIgnoreCase(UserFavoriteTargetType.ACTIVITY.getCode()))) {
            updateProfileIfNotExist(favorite.getOwnerUid(), UserProfileContstant.FAVOTITE_TOPIC_COUNT, -1);
            return;
        }
        if (favorite.getTargetType() != null && favorite.getTargetType().equalsIgnoreCase(UserFavoriteTargetType.BIZ.getCode())) {
            updateProfileIfNotExist(favorite.getOwnerUid(), UserProfileContstant.FAVOTITE_BIZ_COUNT, -1);
        }

    }

    @Override
    public void addUserServiceAddress(UserServiceAddress serviceAddress) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(UserServiceAddress.class));
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, serviceAddress.getOwnerUid()));
        EhUserServiceAddressesDao dao = new EhUserServiceAddressesDao(context.configuration());
        serviceAddress.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        serviceAddress.setId(id);
        dao.insert(serviceAddress);
    }
    
    @Override
    public void deleteUserServieAddress(Long addressId,Long uid) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, uid));
        EhUserServiceAddressesDao dao = new EhUserServiceAddressesDao(context.configuration());
        List<EhUserServiceAddresses> list =  dao.fetchByAddressId(addressId);
        dao.delete(list);
        
    }
    
    @Override
    public List<UserServiceAddress> findUserRelateServiceAddresses(long uid){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, uid));
        EhUserServiceAddressesDao dao = new EhUserServiceAddressesDao(context.configuration());
        return dao.fetchByOwnerUid(uid).stream().map(r -> {
            return ConvertHelper.convert(r, UserServiceAddress.class);
            }).collect(Collectors.toList());
    }

	@Override
	public void addUserShop(Long uid) {
		updateShop(uid, UserProfileContstant.IS_APPLIED_SHOP, 1);
		
	}

	@Override
	public void deleteShop(Long uid) {
		updateShop(uid, UserProfileContstant.IS_APPLIED_SHOP, 0);
		
	}
	
	private void updateShop(Long uid, String key, Integer val) {
        UserProfile userProfile = null;
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, uid));
        Record result = context.select().from(Tables.EH_USER_PROFILES).where(Tables.EH_USER_PROFILES.OWNER_ID.eq(uid))
                .and(Tables.EH_USER_PROFILES.ITEM_NAME.eq(key)).fetchAny();
        if (result != null) {
            userProfile = ConvertHelper.convert(result, UserProfile.class);
        }

        if (userProfile == null) {
            long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserProfiles.class));
            userProfile = new UserProfile();
            userProfile.setOwnerId(uid);
            userProfile.setItemName(key);
            userProfile.setItemKind((byte) 1);
            userProfile.setItemValue(val + "");
            userProfile.setId(id);
            EhUserProfilesDao dao = new EhUserProfilesDao(context.configuration());
            dao.insert(userProfile);
            return;
        }
        EhUserProfilesDao dao = new EhUserProfilesDao(context.configuration());
        userProfile.setItemValue(val + "");
        dao.update(userProfile);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserProfiles.class, userProfile.getId());

    }

	@Override
	public List<UserFavoriteDTO> findFavorite(Long uid, String targetType,
			CrossShardListingLocator locator, int count) {
		
		if (locator.getAnchor() == null) {
            locator.setAnchor(0L);
        }
		
        List<UserFavoriteDTO> favorites = new ArrayList<UserFavoriteDTO>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, uid));
        context.select().from(Tables.EH_USER_FAVORITES).where(Tables.EH_USER_FAVORITES.OWNER_UID.eq(uid))
                .and(Tables.EH_USER_FAVORITES.TARGET_TYPE.eq(targetType)).and(Tables.EH_USER_FAVORITES.ID.ge(locator.getAnchor()))
                .limit(count).fetch().forEach(fav -> {
                	UserFavoriteDTO f = ConvertHelper.convert(fav, UserFavoriteDTO.class);
                	favorites.add(f);
                });
        
        return favorites;
        
	}

	@Override
	public List<UserPost> listPostedTopics(Long uid, String targetType, 
			CrossShardListingLocator locator, int count) {
		
	    // 由于每次都是从前往后取，取完之后再倒排，导致回去的anchor一直都是最小值，
	    // 也就是下一页和第一页取得的结果是一样的，需要倒排着查 by lqs 20160928
//		if (locator.getAnchor() == null) {
//            locator.setAnchor(0L);
//        }
//		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, uid));
//        List<UserPost> posts = new ArrayList<UserPost>();
//        
//        context.select().from(Tables.EH_USER_POSTS).where(Tables.EH_USER_POSTS.OWNER_UID.eq(uid))
//        .and(Tables.EH_USER_POSTS.TARGET_TYPE.eq(targetType)).and(Tables.EH_USER_POSTS.ID.ge(locator.getAnchor()))
//        .limit(count).fetch().forEach(p -> {
//        	UserPost post = ConvertHelper.convert(p, UserPost.class);
//        	posts.add(post);
//        });
        
	    DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, uid));
        SelectQuery<EhUserPostsRecord> query = context.selectQuery(Tables.EH_USER_POSTS);
        query.addConditions(Tables.EH_USER_POSTS.OWNER_UID.eq(uid));
        query.addConditions(Tables.EH_USER_POSTS.TARGET_TYPE.eq(targetType));
        
        if(locator.getAnchor() != null) {
        	//修改EH_FORUM_POSTS为EH_USER_POSTS，by tt, 20160930
            query.addConditions(Tables.EH_USER_POSTS.CREATE_TIME.lt(new Timestamp(locator.getAnchor())));
        }
        
        query.addOrderBy(Tables.EH_USER_POSTS.CREATE_TIME.desc());
        query.addLimit(count);
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query user posted topics by count, sql=" + query.getSQL());
            LOGGER.debug("Query user posted topics, bindValues=" + query.getBindValues());
        }
        
        List<UserPost> posts = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, UserPost.class);
        });
        
        if(posts.size() == count) {
            locator.setAnchor(posts.get(count -2).getCreateTime().getTime());
        }else {
            locator.setAnchor(null);
        }
        
        return posts;
	}

	@Override
	public void updateViewedActivityProfileIfNotExist(Long uid, String key,
			Long lastViewedTime, List<Long> ids) {
		
		UserProfile userProfile = null;
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUsers.class, uid));
        Record result = context.select().from(Tables.EH_USER_PROFILES).where(Tables.EH_USER_PROFILES.OWNER_ID.eq(uid))
                .and(Tables.EH_USER_PROFILES.ITEM_NAME.eq(key)).fetchAny();
        if (result != null) {
            userProfile = ConvertHelper.convert(result, UserProfile.class);
        }

        if (userProfile == null) {
            long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserProfiles.class));
            userProfile = new UserProfile();
            userProfile.setOwnerId(uid);
            userProfile.setItemName(key);
            userProfile.setItemKind((byte) 1);
            userProfile.setItemValue(lastViewedTime + "");
            userProfile.setStringTag1(ids.toString());
            userProfile.setId(id);
            EhUserProfilesDao dao = new EhUserProfilesDao(context.configuration());
            dao.insert(userProfile);
        }
        EhUserProfilesDao dao = new EhUserProfilesDao(context.configuration());
        userProfile.setItemValue(lastViewedTime + "");
        userProfile.setStringTag1(ids.toString());
        dao.update(userProfile);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserProfiles.class, userProfile.getId());
		
	}

	@Override
	public List<SearchTypes> listByNamespaceId(Integer namespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhSearchTypesRecord> query = context.selectQuery(Tables.EH_SEARCH_TYPES);
		query.addConditions(Tables.EH_SEARCH_TYPES.NAMESPACE_ID.eq(namespaceId));
		
		query.addConditions(Tables.EH_SEARCH_TYPES.STATUS.eq(SearchTypesStatus.ACTIVE.getCode()));
		query.addOrderBy(Tables.EH_SEARCH_TYPES.ORDER.asc());
		List<SearchTypes> result = new ArrayList<SearchTypes>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, SearchTypes.class));
			return null;
		});
		if(result.size()==0)
			return null;

		
		return result;
	}

	@Override
	public RequestTemplates getCustomRequestTemplate(String templateType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhRequestTemplatesRecord> query = context.selectQuery(Tables.EH_REQUEST_TEMPLATES);
		query.addConditions(Tables.EH_REQUEST_TEMPLATES.TEMPLATE_TYPE.eq(templateType));
		query.addConditions(Tables.EH_REQUEST_TEMPLATES.STATUS.eq((byte) 1));
		 
		List<RequestTemplates> result = new ArrayList<RequestTemplates>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, RequestTemplates.class));

			return null;
		});
		if(result.size()==0)
			return null;

		
		return result.get(0);
	}

	@Override
	public SearchTypes findByContentAndNamespaceId(Integer namespaceId,
			String contentType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhSearchTypesRecord> query = context.selectQuery(Tables.EH_SEARCH_TYPES);
		query.addConditions(Tables.EH_SEARCH_TYPES.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_SEARCH_TYPES.CONTENT_TYPE.eq(contentType));
		
		query.addConditions(Tables.EH_SEARCH_TYPES.STATUS.eq(SearchTypesStatus.ACTIVE.getCode()));
		 
		List<SearchTypes> result = new ArrayList<SearchTypes>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, SearchTypes.class));
			return null;
		});

		if(result.size() == 0) {
			return null;
		}
		
		return result.get(0);
	}

	@Override
	public RequestTemplates getCustomRequestTemplate(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhRequestTemplatesRecord> query = context.selectQuery(Tables.EH_REQUEST_TEMPLATES);
		query.addConditions(Tables.EH_REQUEST_TEMPLATES.ID.eq(id));
		query.addConditions(Tables.EH_REQUEST_TEMPLATES.STATUS.eq((byte) 1));
		 
		List<RequestTemplates> result = new ArrayList<RequestTemplates>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, RequestTemplates.class));

			return null;
		});
		if(result.size()==0)
			return null;

		return result.get(0);
	}

	@Override
	public void createSearchTypes(SearchTypes searchType) {

		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSearchTypes.class));
		
		searchType.setId(id);
		searchType.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		searchType.setStatus(SearchTypesStatus.ACTIVE.getCode());
		LOGGER.info("createSearchTypes: " + searchType);
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSearchTypes.class, id));
		EhSearchTypesDao dao = new EhSearchTypesDao(context.configuration());
        dao.insert(searchType);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhSearchTypes.class, null);
		
	}

	@Override
	public void deleteSearchTypes(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhSearchTypes.class));
		EhSearchTypesDao dao = new EhSearchTypesDao(context.configuration());
		dao.deleteById(id);
	}

	@Override
	public List<RequestTemplatesNamespaceMapping> getRequestTemplatesNamespaceMappings(
			Integer namespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhRequestTemplatesNamespaceMappingRecord> query = context.selectQuery(Tables.EH_REQUEST_TEMPLATES_NAMESPACE_MAPPING);
		query.addConditions(Tables.EH_REQUEST_TEMPLATES_NAMESPACE_MAPPING.NAMESPACE_ID.eq(namespaceId));
		 
		List<RequestTemplatesNamespaceMapping> result = new ArrayList<RequestTemplatesNamespaceMapping>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, RequestTemplatesNamespaceMapping.class));
			return null;
		});
		
		return result;
	}

	@Override
	public List<RequestTemplates> listCustomRequestTemplates() {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhRequestTemplatesRecord> query = context.selectQuery(Tables.EH_REQUEST_TEMPLATES);
		query.addConditions(Tables.EH_REQUEST_TEMPLATES.STATUS.eq((byte) 1));
		 
		List<RequestTemplates> result = new ArrayList<RequestTemplates>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, RequestTemplates.class));
			return null;
		});
		if(result.size()==0)
			return null;
		return result;
	}

	@Override
	public List<RequestAttachments> listRequestAttachments(String ownerType,
			Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhRequestAttachmentsRecord> query = context.selectQuery(Tables.EH_REQUEST_ATTACHMENTS);
		query.addConditions(Tables.EH_REQUEST_ATTACHMENTS.OWNER_TYPE.eq(ownerType));
		query.addConditions(Tables.EH_REQUEST_ATTACHMENTS.OWNER_ID.eq(ownerId));
		
		Map<String, RequestAttachments> map = new HashMap<String, RequestAttachments>();
		List<RequestAttachments> result = new ArrayList<RequestAttachments>();
		query.fetch().map((r) -> {
			if(map.get(r.getTargetFieldName()) != null) {
				RequestAttachments attachment = map.get(r.getTargetFieldName());
				attachment.setContentUri(attachment.getContentUri() + "," + r.getContentUri());
				map.put(r.getTargetFieldName(), attachment);
			} else {
				map.put(r.getTargetFieldName(), ConvertHelper.convert(r, RequestAttachments.class));
			}
			return null;
		});
		
		Iterator it = map.entrySet().iterator();
		while(it.hasNext()){
			java.util.Map.Entry entry = (java.util.Map.Entry)it.next();
			RequestAttachments attachment = (RequestAttachments) entry.getValue();
			result.add(attachment);
		}
		
		return result;
	}

	@Override
	public void createRequestAttachments(RequestAttachments attachment) {

		assert(attachment.getOwnerId() != null);
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhRequestAttachments.class));
        attachment.setId(id);
        attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        EhRequestAttachmentsDao dao = new EhRequestAttachmentsDao(context.configuration());
        dao.insert(attachment);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhRequestAttachments.class, null);
		
	}

	@Override
	public List<StatActiveUser> listActiveStats(Long beginDate, Long endDate, Integer namespaceId, CrossShardListingLocator locator, int pageSize) {
		 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectQuery<EhStatActiveUsersRecord> query = context
				.selectQuery(Tables.EH_STAT_ACTIVE_USERS);
		 
		Condition condition = Tables.EH_STAT_ACTIVE_USERS.ID.ne(-1L);
		if (null != namespaceId)
			condition = condition.and(Tables.EH_STAT_ACTIVE_USERS.NAMESPACE_ID.eq(namespaceId));
		if (null != beginDate)
			condition = condition.and(Tables.EH_STAT_ACTIVE_USERS.STAT_DATE.gt(new Date(beginDate)));
		if (null != endDate)
			condition = condition.and(Tables.EH_STAT_ACTIVE_USERS.STAT_DATE.lt(new Date(endDate))); 
		if (null != locator && locator != null && locator.getAnchor() != null)
			condition = condition.and(Tables.EH_STAT_ACTIVE_USERS.ID.gt(locator.getAnchor()));
		query.addConditions(condition);
		query.addLimit(pageSize);
		query.addOrderBy(Tables.EH_STAT_ACTIVE_USERS.ID.asc());
		List<StatActiveUser> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, StatActiveUser.class));
			return null;
		});
		if (null != result && result.size() > 0)
			return result ;
		return null;
	}

	@Override
	public StatActiveUser findStatActiveUserByDate(Date date, Integer namespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		SelectQuery<EhStatActiveUsersRecord> query = context
				.selectQuery(Tables.EH_STAT_ACTIVE_USERS);
		 
		Condition condition = Tables.EH_STAT_ACTIVE_USERS.NAMESPACE_ID.eq(namespaceId);
		if (null != date)
			condition = condition.and(Tables.EH_STAT_ACTIVE_USERS.STAT_DATE.eq(date));  
		query.addConditions(condition);
		query.addOrderBy(Tables.EH_STAT_ACTIVE_USERS.ID.asc());
		List<StatActiveUser> result = new ArrayList<>();
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, StatActiveUser.class));
			return null;
		});
		if (null != result && result.size() > 0)
			return result.get(0) ;
		return null;
	}

	@Override
	public void createStatActiveUser(StatActiveUser stat) { 
		String key = NameMapper.getSequenceDomainFromTablePojo(EhStatActiveUsers.class);
		long id = sequenceProvider.getNextSequence(key);
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readWrite());
        stat.setId(id); 
        EhStatActiveUsersDao dao = new EhStatActiveUsersDao(context.configuration());
        dao.insert(stat);
	}

    @Override
    public List<UserActivity> listUserActivetys(ListingLocator locator, Integer count, ListingQueryBuilderCallback callback) {
        com.everhomes.server.schema.tables.EhUserActivities t = Tables.EH_USER_ACTIVITIES;

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhUserActivitiesRecord> query = context.selectQuery(t);

        if(null != callback) {
            callback.buildCondition(locator, query);
        }
        if(null != locator.getAnchor()) {
            query.addConditions(t.ID.le(locator.getAnchor()));
        }
        if (count > 0) {
            query.addLimit(count + 1);
        }
        query.addOrderBy(t.ID.desc());

        List<UserActivity> list = query.fetchInto(UserActivity.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

    @Override
    public List<UserActivity> listUserActivetys(Long userId, Integer pageSize) {
        List<UserActivity> results = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhUserActivitiesRecord> query = context.selectQuery(Tables.EH_USER_ACTIVITIES);
        query.addConditions(Tables.EH_USER_ACTIVITIES.UID.eq(userId));
        query.addOrderBy(Tables.EH_USER_ACTIVITIES.CREATE_TIME.desc());

        if (null != pageSize) {
            query.addLimit(pageSize);
        }
        query.fetch().map((r) -> {
            results.add(ConvertHelper.convert(r, UserActivity.class));
            return null;
        });
        return results;
    }

    @Override
    public UserActivity findLastUserActivity(Long uid) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_USER_ACTIVITIES)
                .where(Tables.EH_USER_ACTIVITIES.UID.eq(uid))
                .orderBy(Tables.EH_USER_ACTIVITIES.CREATE_TIME.desc())
                .fetchAnyInto(UserActivity.class);
    }

    @Override
    public List<User> listNotInUserActivityUsers(Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        List<String> ids = context
                .selectDistinct(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.IMEI_NUMBER)
                .from(Tables.EH_TERMINAL_APP_VERSION_ACTIVES)
                .where(Tables.EH_TERMINAL_APP_VERSION_ACTIVES.NAMESPACE_ID.eq(namespaceId))
                .fetchInto(String.class);

        if (ids.size() == 0) {
            ids.add("0");
        }

        List<Long> id1s = new ArrayList<>(ids.size());
        for (String id : ids) {
            try {
                id1s.add(Long.valueOf(id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return context.select(Tables.EH_USERS.ID, Tables.EH_USERS.NAMESPACE_ID, Tables.EH_USERS.CREATE_TIME)
                .from(Tables.EH_USERS)
                .where(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_USERS.STATUS.eq(UserStatus.ACTIVE.getCode()))
                .and(Tables.EH_USERS.NAMESPACE_USER_TYPE.isNull())
                .and(Tables.EH_USERS.NAMESPACE_USER_TOKEN.eq(""))
                .and(Tables.EH_USERS.ID.notIn(id1s))
                .fetchInto(User.class);
    }

    @Override
    public void addActivities(List<UserActivity> activityList) {
        DSLContext cxt = dbProvider.getDslContext(AccessSpec.readWrite());
        EhUserActivitiesDao dao = new EhUserActivitiesDao(cxt.configuration());
        for (UserActivity activity : activityList) {
            dao.insert(activity);
        }
    }

    @Override
    public void deleteUserActivity(UserActivity activity) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhUserActivitiesDao dao = new EhUserActivitiesDao(context.configuration());
        dao.delete(activity);
    }

    @Override
    public List<VipPriority> listVipPriorityByNamespaceId(Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_VIP_PRIORITY)
                .where(Tables.EH_VIP_PRIORITY.NAMESPACE_ID.eq(namespaceId))
                .fetchInto(VipPriority.class);
    }
}
