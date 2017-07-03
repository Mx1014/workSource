// @formatter:off
package com.everhomes.group;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.cache.CacheProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.group.GroupOpRequestStatus;
import com.everhomes.rest.group.GroupPrivacy;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.EhGroupMembersRecord;
import com.everhomes.server.schema.tables.records.EhGroupOpRequestsRecord;
import com.everhomes.server.schema.tables.records.EhGroupsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;
import com.everhomes.util.RecordHelper;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.everhomes.server.schema.Tables.EH_GROUP_MEMBERS;

@Component
public class GroupProviderImpl implements GroupProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private CacheProvider cacheProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;

    @Caching(evict={@CacheEvict(value="listGroupMessageMembers", allEntries=true)})
    @Override
    public void createGroup(Group group) {
        long id = this.shardingProvider.allocShardableContentId(EhGroups.class).second();
        group.setId(id);
        group.setUuid(UUID.randomUUID().toString());
        group.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        group.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, id));
        EhGroupsDao dao = new EhGroupsDao(context.configuration());
        dao.insert(group);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhGroups.class, null);
    }

    @Caching(evict={ @CacheEvict(value="Group", key="#group.id"),
            @CacheEvict(value="GroupByCreatorId", key="#group.creatorUid"),
            @CacheEvict(value="listGroupMessageMembers", allEntries=true),
            @CacheEvict(value="GroupByUuid", key="#group.uuid")})
    @Override
    public void updateGroup(Group group) {
        assert(group.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, group.getId()));
        EhGroupsDao dao = new EhGroupsDao(context.configuration());
        group.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(group);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhGroups.class, group.getId());
    }

    @Caching(evict={ @CacheEvict(value="Group", key="#group.id"),
        @CacheEvict(value="GroupByCreatorId", key="#group.creatorUid"),
        @CacheEvict(value="listGroupMessageMembers", allEntries=true),
        @CacheEvict(value="GroupByUuid", key="#group.uuid")})
    @Override
    public void deleteGroup(Group group) {
        assert(group.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, group.getId()));
        EhGroupsDao dao = new EhGroupsDao(context.configuration());
        dao.delete(group);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhGroups.class, group.getId());
    }

    public void deleteGroup(long id) {
        GroupProvider self = PlatformContext.getComponent(GroupProvider.class);
        Group group = self.findGroupById(id);
        if(group != null)
            self.deleteGroup(group);
    }

    /**
     * Also used by Enterprise
     */
    @Cacheable(value = "Group", key="#id", unless="#result == null")
    @Override
    public Group findGroupById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, id));
        EhGroupsDao dao = new EhGroupsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Group.class);
    }
    
    @Cacheable(value = "GroupByUuid", key="#uuid", unless="#result == null")
    @Override
    public Group findGroupByUuid(String uuid) {
        Group[] result = new Group[1];
        
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
            (DSLContext context, Object reducingContext) -> {
                result[0] = context.select().from(Tables.EH_GROUPS)
                    .where(Tables.EH_GROUPS.UUID.eq(uuid))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, Group.class);
                    });

                if (result[0] != null) {
                    return false;
                } else {
                    return true;
                }
            });
        
        return result[0];
    }
    
    @Cacheable(value = "GroupByCreatorId", key="#creatorId", unless="#result.size() == 0")
    @Override
    public List<Group> findGroupByCreatorId(long creatorId) {
        List<Group> groupList = new ArrayList<Group>();
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class),
			groupList, (DSLContext context, Object reducingContext) -> {
				List<Group> list = context.select().from(Tables.EH_GROUPS)
					.where(Tables.EH_GROUPS.CREATOR_UID.eq(creatorId))
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
    public List<Group> queryPulbicGroups(int maxCount, ListingQueryBuilderCallback callback) {
        List<Group> groupList = new ArrayList<Group>();
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class),
            groupList, (DSLContext context, Object reducingContext) -> {
                SelectQuery<EhGroupsRecord> query = context.selectQuery(Tables.EH_GROUPS);
                query.addConditions(Tables.EH_GROUPS.PRIVATE_FLAG.eq(GroupPrivacy.PUBLIC.getCode()));
                query.addConditions(Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode()));
                
                if(callback != null) {
                    callback.buildCondition(null, query);
                }
                
                query.addOrderBy(Tables.EH_GROUPS.MEMBER_COUNT.desc());
                query.addLimit(maxCount);
                
                query.fetch().map((r) -> {
                    groupList.add(ConvertHelper.convert(r, Group.class));
                    return null;
                });

                return true;
            });
        
        Collections.sort(groupList, new GroupMemberCountDescComparator());
        
        return groupList;
    }

    @Override
    public List<Group> queryGroups(CrossShardListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        final List<Group> groups = new ArrayList<>();
        
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhGroups.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhGroupsRecord> query = context.selectQuery(Tables.EH_GROUPS);

            if(callback != null)
                callback.buildCondition(locator, query);
                
            if(locator.getAnchor() != null)
                query.addConditions(Tables.EH_GROUPS.ID.gt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_GROUPS.ID.asc());
            query.addLimit(count - groups.size());
            
            query.fetch().map((r) -> {
                groups.add(ConvertHelper.convert(r, Group.class));
                return null;
            });
           
            if(groups.size() >= count) {
                locator.setAnchor(groups.get(groups.size() - 1).getId());
                return AfterAction.done;
            }
            return AfterAction.next;
        });

        if(groups.size() > 0) {
            locator.setAnchor(groups.get(groups.size() - 1).getId());
        }
        
        return groups;
    }
    
    @Override
    public void iterateGroups(int count, GroupDiscriminator discriminator, IterateGroupCallback callback) {
    	assert(count > 0);
    	assert(callback != null);
    	
    	List<Group> groupList = null;
    	int maxIndex = 0; // Max index of group list in loop
    	CrossShardListingLocator locator = new CrossShardListingLocator();
    	Long pageAnchor = null;
    	do {
            locator.setAnchor(pageAnchor);
    		
            // Query count+1 record, the plus one is used to indicate there are more data
            if(discriminator == null) {
            	groupList = queryGroups(locator, (count + 1), null);
            } else {
            	groupList = queryGroups(locator, (count + 1), (loc, query)-> {
            		query.addConditions(Tables.EH_GROUPS.DISCRIMINATOR.eq(discriminator.getCode()));
		            return query;
		        });
            }
            if(groupList == null || groupList.size() == 0) {
            	break;
            } else {
        		// if there are still more records in db
            	if(groupList.size() > count) {
            		maxIndex = groupList.size() - 2;
            		pageAnchor = groupList.get(groupList.size() - 2).getId();
            	} else {
            		// no more record in db
            		maxIndex = groupList.size() - 1;
            		pageAnchor = null;
            	}

                for(int i = 0; i <= maxIndex; i++) {
                	callback.process(groupList.get(i));
                }
            }
    	} while (pageAnchor != null);
    }
    
    @Caching(evict={ @CacheEvict(value="GroupMemberByInfo", key="{#groupMember.groupId, #groupMember.memberType, #groupMember.memberId}"),
            @CacheEvict(value="listGroupMessageMembers", allEntries=true),
            @CacheEvict(value="GroupMemberByGroupId", key="#groupMember.groupId")})
    public void createGroupMember(final GroupMember groupMember) {
        assert(groupMember.getGroupId() != null);
        Group group = findGroupById(groupMember.getGroupId());
        if(group == null)
            throw new InvalidParameterException("Missing group info in GroupMember parameter");
        
        
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhGroupMembers.class));
        groupMember.setId(id);
        groupMember.setUuid(UUID.randomUUID().toString());
        groupMember.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        groupMember.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
//      由于在创建成员的时候，有可能成员还是待审核状态，此时不能把其算在memberCount里；
//      另外，更新memberCount时需要加锁，故把更新memberCount的代码移到Service中实现；
//        this.dbProvider.execute((status) -> {
//            Group group = findGroupById(groupMember.getGroupId());
//            if(group == null)
//                throw new InvalidParameterException("Missing group info in GroupMember parameter");
//
//            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, groupMember.getGroupId()));
//            EhGroupMembersDao dao = new EhGroupMembersDao(context.configuration());
//            dao.insert(groupMember);
//            
//            DaoHelper.publishDaoAction(DaoAction.CREATE, EhGroupMembers.class, null);
//            
//            group.setMemberCount(1 + group.getMemberCount().longValue());
//            updateGroup(group);
//            
//            return null;
//        });
        

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, groupMember.getGroupId()));
        EhGroupMembersDao dao = new EhGroupMembersDao(context.configuration());
        dao.insert(groupMember);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhGroupMembers.class, null);
    }
    
    @Caching(evict={ @CacheEvict(value="GroupMember", key="#groupMember.id"),
            @CacheEvict(value="GroupMemberByInfo", key="{#groupMember.groupId, #groupMember.memberType, #groupMember.memberId}"),
            @CacheEvict(value="GroupMemberByGroupId", key="#groupMember.groupId"),
            @CacheEvict(value="listGroupMessageMembers", allEntries=true),
            @CacheEvict(value="GroupMemberByUuid", key="#groupMember.uuid")})
    public void updateGroupMember(GroupMember groupMember) {
        assert(groupMember.getId() != null);
        assert(groupMember.getGroupId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, groupMember.getGroupId()));
        EhGroupMembersDao dao = new EhGroupMembersDao(context.configuration());
        dao.update(groupMember);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhGroupMembers.class, groupMember.getId());
    }
    
    @Caching(evict={ @CacheEvict(value="GroupMember", key="#groupMember.id"),
            @CacheEvict(value="GroupMemberByInfo", key="{#groupMember.groupId, #groupMember.memberType, #groupMember.memberId}"),
            @CacheEvict(value="GroupMemberByGroupId", key="#groupMember.groupId"),
            @CacheEvict(value="listGroupMessageMembers", allEntries=true),
            @CacheEvict(value="GroupMemberByUuid", key="#groupMember.uuid")})
    public void deleteGroupMember(final GroupMember groupMember) {
        assert(groupMember.getId() != null);
        assert(groupMember.getGroupId() != null);

        this.dbProvider.execute((status) -> {
           
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, groupMember.getGroupId()));
            EhGroupMembersDao dao = new EhGroupMembersDao(context.configuration());
            dao.delete(groupMember);
            
            DaoHelper.publishDaoAction(DaoAction.MODIFY, EhGroupMembers.class, groupMember.getId());
                       
            Group group = this.findGroupById(groupMember.getGroupId());
            if(group != null) {
                long memberCount = group.getMemberCount().longValue() - 1;
                memberCount = (memberCount < 0) ? 0L : memberCount;
                group.setMemberCount(memberCount);
                this.updateGroup(group);
            }
            
            return null;
        });
    }

    public void deleteGroupMemberById(long id) {
        GroupProvider self = PlatformContext.getComponent(GroupProvider.class);
        GroupMember groupMember = self.findGroupMemberById(id);
        if(groupMember != null)
            self.deleteGroupMember(groupMember);
    }
    
    @Cacheable(value = "GroupMember", key="#id", unless="#result == null")
    @Override
    public GroupMember findGroupMemberById(long id) {
        GroupMember[] result = new GroupMember[1];
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
                (DSLContext context, Object reducingContext) -> {
            
            EhGroupMembersDao dao = new EhGroupMembersDao(context.configuration());
            result[0] = ConvertHelper.convert(dao.findById(id), GroupMember.class);
            if(result[0] != null) {
                return false;
            }
                    
            return true;
        });

        return result[0];
    }
    
    @Cacheable(value = "GroupMemberByUuid", key="#uuid", unless="#result == null")
    @Override
    public GroupMember findGroupMemberByUuid(String uuid) {
        GroupMember[] result = new GroupMember[1];
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
                (DSLContext context, Object reducingContext) -> {
            result[0] = context.select().from(Tables.EH_GROUPS)
                .where(Tables.EH_GROUPS.UUID.eq(uuid))
                .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, GroupMember.class);
            });
            
            if(result[0] != null) {
                return false;
            } else {
                return true;
            }
        });

        return result[0];
    }
    
    @Cacheable(value = "GroupMemberByInfo", key="{#groupId, #memberType, #memberId}", unless="#result == null")
    public GroupMember findGroupMemberByMemberInfo(final long groupId, final String memberType, final long memberId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, groupId));
        EhGroupMembersRecord record = (EhGroupMembersRecord)context.select().from(EH_GROUP_MEMBERS)
            .where(EH_GROUP_MEMBERS.GROUP_ID.eq(groupId))
            .and(EH_GROUP_MEMBERS.MEMBER_TYPE.eq(memberType))
            .and(EH_GROUP_MEMBERS.MEMBER_ID.eq(memberId))
            .fetchAny();
        return ConvertHelper.convert(record, GroupMember.class);
    }
    
    @Cacheable(value = "GroupMemberByGroupId", key="#groupId", unless="#result.size() == 0")
    @Override
    public List<GroupMember> findGroupMemberByGroupId(long groupId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, groupId));

        List<GroupMember> list = context.select().from(Tables.EH_GROUP_MEMBERS)
        	.where(EH_GROUP_MEMBERS.GROUP_ID.eq(groupId))
			.fetch().map((r) -> {
				return ConvertHelper.convert(r, GroupMember.class);
			});
        return list;
    }

    @Override
    public List<GroupMember> listGroupMembers(ListingLocator locator, int count) {
        return queryGroupMembers(locator, count, null); 
    }
    
    @Override
    public List<GroupMember> queryGroupMembers(ListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback) {
        assert(locator.getEntityId() != 0);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, locator.getEntityId()));
        
        final List<GroupMember> members = new ArrayList<GroupMember>();
        SelectQuery<EhGroupMembersRecord> query = context.selectQuery(Tables.EH_GROUP_MEMBERS);
        query.addConditions(EH_GROUP_MEMBERS.GROUP_ID.eq(locator.getEntityId()));

        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
            
        if(locator.getAnchor() != null)
            query.addConditions(Tables.EH_GROUP_MEMBERS.ID.lt(locator.getAnchor()));
        query.addOrderBy(Tables.EH_GROUP_MEMBERS.ID.desc());
        query.addLimit(count);
        
        query.fetch().map((r) -> {
            members.add(ConvertHelper.convert(r, GroupMember.class));
            return null;
        });
        
        if(members.size() > 0) {
            locator.setAnchor(members.get(members.size() -1).getId());
        }
        
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Query group members, entityId=" + locator.getEntityId() + ", expectedCount=" + count 
                + ", resultCount=" + members.size() + ", query=" + query);
        }
        
        return members;
    }
    
    @Override
    public void iterateGroupMembers(int count, long groupId, ListingQueryBuilderCallback queryBuilderCallback, 
        IterateGroupMemberCallback callback) {
        if(count <= 0 || callback == null) {
            return;
        }
        
        List<GroupMember> memberList = null;
        int maxIndex = 0; // Max index of group list in loop
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setEntityId(groupId);
        Long pageAnchor = null;
        do {
            locator.setAnchor(pageAnchor);
            
            memberList = queryGroupMembers(locator, count + 1, queryBuilderCallback);
            
            if(memberList == null || memberList.size() == 0) {
                break;
            } else {
                // if there are still more records in db
                if(memberList.size() > count) {
                    maxIndex = memberList.size() - 2;
                    pageAnchor = memberList.get(memberList.size() - 2).getId();
                } else {
                    // no more record in db
                    maxIndex = memberList.size() - 1;
                    pageAnchor = null;
                }

                for(int i = 0; i <= maxIndex; i++) {
                    callback.process(memberList.get(i));
                }
            }
        } while (pageAnchor != null);
    }
    
    @Caching(evict = { @CacheEvict(value="GroupVisibleList", key="#scope.ownerId") })
    @Override
    public void createGroupVisibleScope(GroupVisibilityScope scope) {
        assert(scope.getOwnerId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(
            AccessSpec.readWriteWith(EhGroups.class, scope.getOwnerId()));
        
        long id = this.sequenceProvider.getNextSequence(
                NameMapper.getSequenceDomainFromTablePojo(EhGroupVisibleScopes.class));
        scope.setId(id);
        
        EhGroupVisibleScopesDao dao = new EhGroupVisibleScopesDao(context.configuration());
        dao.insert(scope);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhGroupVisibleScopes.class, null);
    }

    @Caching(evict = { @CacheEvict(value="GroupVisible", key="#scope.id"), 
            @CacheEvict(value="GroupVisibleList", key="#scope.ownerId") })
    @Override
    public void updateGroupVisibleScope(GroupVisibilityScope scope) {
        assert(scope.getOwnerId() != null);
        assert(scope.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(
                AccessSpec.readWriteWith(EhGroups.class, scope.getOwnerId()));
        EhGroupVisibleScopesDao dao = new EhGroupVisibleScopesDao(context.configuration());
        dao.update(scope);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhGroupVisibleScopes.class, scope.getId());
    }

    @Caching(evict = { @CacheEvict(value="GroupVisible", key="#scope.id"), 
            @CacheEvict(value="GroupVisibleList", key="#scope.ownerId") })
    @Override
    public void deleteGroupVisibleScope(GroupVisibilityScope scope) {
        assert(scope.getOwnerId() != null);
        assert(scope.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(
                AccessSpec.readWriteWith(EhGroups.class, scope.getOwnerId()));
        EhGroupVisibleScopesDao dao = new EhGroupVisibleScopesDao(context.configuration());
        dao.delete(scope);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhGroupVisibleScopes.class, scope.getId());
    }

    @Override
    public void deleteGroupVisibleScopeById(long id) {
        GroupProvider self = PlatformContext.getComponent(GroupProvider.class);
        
        GroupVisibilityScope scope = self.findGroupVisibleScopeById(id);
        if(scope != null)
            self.deleteGroupVisibleScope(scope);
    }
    
    @Cacheable(value = "GroupVisible", key="#id", unless="#result == null")
    @Override
    public GroupVisibilityScope findGroupVisibleScopeById(long id) {
        final GroupVisibilityScope[] result = new GroupVisibilityScope[1];
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
            (DSLContext context, Object reducingContext) -> {
            
            EhGroupVisibleScopesDao dao = new EhGroupVisibleScopesDao(context.configuration());
            result[0] = ConvertHelper.convert(dao.findById(id), GroupVisibilityScope.class);
            if(result[0] != null)
                return false;
            
            return true;
        });
        
        return result[0];
    }

    @Cacheable(value = "GroupVisibleList", key="#groupId", unless="#result.size() == 0")
    @Override
    public List<GroupVisibilityScope> listGroupVisibilityScopes(long groupId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, groupId));
        
        return context.select().from(Tables.EH_GROUP_VISIBLE_SCOPES)
            .where(Tables.EH_GROUP_VISIBLE_SCOPES.OWNER_ID.eq(groupId))
            .fetch().map((r)-> {
                return ConvertHelper.convert(r, GroupVisibilityScope.class);
            });
    }

    @Override
    public void createGroupOpRequest(GroupOpRequest request) {
        assert(request.getGroupId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(
            AccessSpec.readWriteWith(EhGroups.class, request.getGroupId()));
        
        long id = this.sequenceProvider.getNextSequence(
                NameMapper.getSequenceDomainFromTablePojo(EhGroupOpRequests.class));
        request.setId(id);
        
        EhGroupOpRequestsDao dao = new EhGroupOpRequestsDao(context.configuration());
        dao.insert(request);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhGroupOpRequests.class, null);
    }

    @Caching(evict = { @CacheEvict(value="findGroupOpRequestById", key="#request.id"),
            @CacheEvict(value="listGroupMessageMembers", allEntries=true),
            @CacheEvict(value="findGroupOpRequestByRequestor", key="{#request.groupId, #request.requestorUid}")})
    @Override
    public void updateGroupOpRequest(GroupOpRequest request) {
        assert(request.getId() != null);
        assert(request.getGroupId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(
                AccessSpec.readWriteWith(EhGroups.class, request.getGroupId()));
        
        EhGroupOpRequestsDao dao = new EhGroupOpRequestsDao(context.configuration());
        dao.update(request);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhGroupOpRequests.class, request.getId());
    }

    @Caching(evict = { @CacheEvict(value="findGroupOpRequestById", key="#request.id"),
            @CacheEvict(value="listGroupMessageMembers", allEntries=true),
            @CacheEvict(value="findGroupOpRequestByRequestor", key="{#request.groupId, #request.requestorUid}")})
    @Override
    public void deleteGroupOpRequest(GroupOpRequest request) {
        assert(request.getId() != null);
        assert(request.getGroupId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(
                AccessSpec.readWriteWith(EhGroups.class, request.getGroupId()));
        
        EhGroupOpRequestsDao dao = new EhGroupOpRequestsDao(context.configuration());
        dao.delete(request);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhGroupOpRequests.class, request.getId());
    }
    
    public void deleteGroupOpRequestById(long id) {
        GroupProvider self = PlatformContext.getComponent(GroupProvider.class);
        GroupOpRequest request = self.findGroupOpRequestById(id);
        if(request != null)
            self.deleteGroupOpRequest(request);
    }
    
    @Cacheable(value="findGroupOpRequestById", key="#id", unless="#result == null")
    @Override
    public GroupOpRequest findGroupOpRequestById(long id) {
        final GroupOpRequest[] result = new GroupOpRequest[1];
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
            (DSLContext context, Object reducingContext) -> {
            
            EhGroupOpRequestsDao dao = new EhGroupOpRequestsDao (context.configuration());
            result[0] = ConvertHelper.convert(dao.findById(id), GroupOpRequest.class);
            if(result[0] != null)
                return false;
            
            return true;
        });
        
        return result[0];
    }
    
    @Cacheable(value="findGroupOpRequestByRequestor", key="{#groupId, #requestorUid}", unless="#result == null")
    @Override
    public GroupOpRequest findGroupOpRequestByRequestor(long groupId, long requestorUid) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, groupId));
        
        EhGroupOpRequestsRecord record = (EhGroupOpRequestsRecord)context.select().from(Tables.EH_GROUP_OP_REQUESTS)
            .where(Tables.EH_GROUP_OP_REQUESTS.GROUP_ID.eq(groupId))
            .and(Tables.EH_GROUP_OP_REQUESTS.REQUESTOR_UID.eq(requestorUid))
            .and(Tables.EH_GROUP_OP_REQUESTS.STATUS.ne(GroupOpRequestStatus.REJECTED.getCode()))
            .fetchAny();
        
        return ConvertHelper.convert(record, GroupOpRequest.class);
    }

    @Override
    public List<GroupOpRequest> queryGroupOpRequests(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback) {
        
        assert(locator.getEntityId() != 0);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, locator.getEntityId()));
        
        final List<GroupOpRequest> requests = new ArrayList<>();
        SelectQuery<EhGroupOpRequestsRecord> query = context.selectQuery(Tables.EH_GROUP_OP_REQUESTS);
        query.addConditions(Tables.EH_GROUP_OP_REQUESTS.GROUP_ID.eq(locator.getEntityId()));

        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);
            
        if(locator.getAnchor() != null)
            query.addConditions(Tables.EH_GROUP_OP_REQUESTS.ID.gt(locator.getAnchor()));
        query.addOrderBy(Tables.EH_GROUP_OP_REQUESTS.ID.asc());
        query.addLimit(count);
        
        query.fetch().map((r) -> {
            requests.add(ConvertHelper.convert(r, GroupOpRequest.class));
            return null;
        });
        
        if(requests.size() > 0) {
            locator.setAnchor(requests.get(requests.size() -1).getId());
        }
        
        return requests;
    }
    
    @Override
    public List<GroupOpRequest> queryGroupOpRequestsByMapReduce(ListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback) {
        List<GroupOpRequest> requests = new ArrayList<GroupOpRequest>();
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), requests, 
            (DSLContext context, Object reducingContext) -> {
                SelectQuery<EhGroupOpRequestsRecord> query = context.selectQuery(Tables.EH_GROUP_OP_REQUESTS);
                if(queryBuilderCallback != null) {
                    queryBuilderCallback.buildCondition(null, query);
                }
                
                if(locator.getAnchor() != null) {
                    query.addConditions(Tables.EH_GROUP_OP_REQUESTS.ID.gt(locator.getAnchor()));
                }
                query.addOrderBy(Tables.EH_GROUP_OP_REQUESTS.ID.asc());
                query.addLimit(count);
                
                query.fetch().map((r) -> {
                    requests.add(ConvertHelper.convert(r, GroupOpRequest.class));
                    return null;
                });
                
                if(requests.size() >= count) {
                    return false;
                } else {
                    return true;
                }
            });
        
        return requests;
    }
    
    @Override
    public List<Group> listGroupByCommunityId(Long communityId, ListingQueryBuilderCallback queryBuilderCallback){
    	List<Group> groupList = new ArrayList<Group>();
    	
    	dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class),
    			groupList, (DSLContext context, Object reducingContext) -> {
    				SelectQuery<EhGroupsRecord> query = context.selectQuery(Tables.EH_GROUPS);
    				 if(queryBuilderCallback != null) {
    	                    queryBuilderCallback.buildCondition(null, query);
    	             }
    				 query.addConditions(Tables.EH_GROUPS.INTEGRAL_TAG2.eq(communityId));
    				 
    				 query.fetch().map((r) -> {
    					 groupList.add(ConvertHelper.convert(r, Group.class));
    	                 return null;
    	             });
    				return true;
    	});
    	
    	return groupList;
    }

    @Override
    public List<Group> listGroupByCommunityIds(List<Long> communityIds, ListingQueryBuilderCallback queryBuilderCallback){
    	List<Group> groupList = new ArrayList<>();

    	dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class),
            groupList, (DSLContext context, Object reducingContext) -> {
                SelectQuery<EhGroupsRecord> query = context.selectQuery(Tables.EH_GROUPS);
                 if(queryBuilderCallback != null) {
                        queryBuilderCallback.buildCondition(null, query);
                 }
                 query.addConditions(Tables.EH_GROUPS.INTEGRAL_TAG2.in(communityIds));

                 query.fetch().map((r) -> {
                     groupList.add(ConvertHelper.convert(r, Group.class));
                     return null;
                 });
            return true;
    	});
    	return groupList;
    }

    @Override
    public List<GroupMember> listGroupMemberByGroupIds(List<Long> groupIds, ListingLocator locator, Integer pageSize,
                                                       ListingQueryBuilderCallback queryBuilderCallback) {
    	List<List<GroupMember>> groupMembers = new ArrayList<>();
    	
    	int count = null == pageSize ? 0 : pageSize + 1;
    	
    	dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class),
    			groupMembers, (DSLContext context, Object reducingContext) -> {
    				SelectQuery<Record> query = context.select(Tables.EH_GROUP_MEMBERS.fields())
                            .from(Tables.EH_GROUP_MEMBERS).getQuery();
    				 if(queryBuilderCallback != null) {
    	                    queryBuilderCallback.buildCondition(locator, query);
    	             }
    				 query.addConditions(Tables.EH_GROUP_MEMBERS.GROUP_ID.in(groupIds));
    				 if(null != pageSize){
    					 query.addLimit(count);
    				 }
    				
    				 List<GroupMember> groupList = query.fetch().map((r) -> {
    					 return RecordHelper.convert(r, GroupMember.class);
    	             });
    				 
    				 locator.setAnchor(null);
    			    	
    				 if(null != pageSize){
    					 if(groupList.size() > pageSize){
        					 groupList = groupList.subList(0, pageSize);
        					locator.setAnchor(groupList.get(pageSize-1).getMemberId());
        				 }
    				 }
    				
    				 groupMembers.add(groupList);
    				return true;
    	});
    	
    	return groupMembers.get(0);
    }

    @Override
    public GroupMember findGroupMemberTopOne(Long groupId){
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, groupId));
        
    	try{
	    	return context.select().from(EH_GROUP_MEMBERS)
	    			.where(EH_GROUP_MEMBERS.GROUP_ID.eq(groupId))
		    		.and(EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode()))
		    		.and(EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode()))
		    		.orderBy(EH_GROUP_MEMBERS.MEMBER_ROLE.asc(), EH_GROUP_MEMBERS.ID.asc())  //按角色、id排序，角色：创建者4、管理员5、普通成员7，这样可取出第一个管理员
		    		.limit(1)
		    		.fetchOne()
		    		.map(r->ConvertHelper.convert(r, GroupMember.class));
    	}catch(NullPointerException e){
    		return null;
    	}
    }
    
    @Cacheable(value="listGroupMessageMembers", key="{#namespaceId, #locator, #pageSize}", unless="#result.getSize() == 0")
    @Override
    public GroupMemberCaches listGroupMessageMembers(Integer namespaceId, ListingLocator locator, int pageSize) {
        List<GroupMember> members = listGroupMembers(locator, pageSize);
//        List<GroupMember> members = new ArrayList<GroupMember>();
//        members.add(new GroupMember());
        
        GroupMemberCaches caches = new GroupMemberCaches();
        caches.setMembers(members);
        caches.setTick(System.currentTimeMillis());
        
        return caches;
    }
    
    @Caching(evict={@CacheEvict(value="listGroupMessageMembers", key="{#namespaceId, #locator, #pageSize}")})
    @Override
    public void evictGroupMessageMembers(Integer namespaceId, ListingLocator locator, int pageSize) {
    }

	@Override
	public List<GroupMember> listPublicGroupMembersByStatus(Long groupId, String keyword, Byte status, Long from, int pageSize,
			boolean includeCreator, Long creatorId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhGroups.class, groupId));
		SelectConditionStep<Record> step = context
				.select()
				.from(EH_GROUP_MEMBERS)
				.where(EH_GROUP_MEMBERS.GROUP_ID.eq(groupId));
				
		if (status != null) {
			step = step.and(EH_GROUP_MEMBERS.MEMBER_STATUS.eq(status));
		}
        if (keyword != null) {
            step = step.and(EH_GROUP_MEMBERS.MEMBER_NICK_NAME.like(DSL.concat("%", keyword, "%")));
        }
		if (!includeCreator) {
			step = step.and(EH_GROUP_MEMBERS.MEMBER_ID.ne(creatorId));
		}
		
		Result<Record> result =step.orderBy(EH_GROUP_MEMBERS.MEMBER_ROLE.asc(), EH_GROUP_MEMBERS.ID.desc())
			.limit(from.intValue(), pageSize)
			.fetch();
		
		if (result != null) {
			return result.map(r->ConvertHelper.convert(r, GroupMember.class));
		}
		return new ArrayList<>();
	}

    @Override
    public List<GroupMember> listPublicGroupMembersByStatus(Long groupId, Byte status, Long from, int pageSize,
                                                            boolean includeCreator, Long creatorId) {
        return listPublicGroupMembersByStatus(groupId, null, status, from, pageSize, includeCreator, creatorId);
    }

	@Override
	public List<GroupMember> searchPublicGroupMembersByStatus(Long groupId, String keyword, Byte status, Long from, int pageSize) {
		return listPublicGroupMembersByStatus(groupId, keyword, status, from, pageSize, true, 0L);
	}

	@Override
	public GroupMemberLog findGroupMemberLogByGroupMemberId(Long groupMemberId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> records = context.select().from(Tables.EH_GROUP_MEMBER_LOGS)
			.where(Tables.EH_GROUP_MEMBER_LOGS.GROUP_MEMBER_ID.eq(groupMemberId))
			.orderBy(Tables.EH_GROUP_MEMBER_LOGS.ID.desc())
			.limit(1)
			.fetch();
		if (records != null && records.size() > 0) {
			return ConvertHelper.convert(records.get(0), GroupMemberLog.class);
		}
		return null;
	}

	@Override
	public void createGroupMemberLog(GroupMemberLog groupMemberLog) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhGroupMemberLogs.class));
		groupMemberLog.setId(id);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroupMemberLogs.class, groupMemberLog.getId()));
        EhGroupMemberLogsDao dao = new EhGroupMemberLogsDao(context.configuration());
        dao.insert(groupMemberLog);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhGroupMemberLogs.class, null);
	}
	
	
}
