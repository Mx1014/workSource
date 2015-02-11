// @formatter:off
package com.everhomes.group;

import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.ReflectionUtils;

import com.everhomes.cache.CacheAccessor;
import com.everhomes.cache.CacheProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.tables.daos.EhGroupMembersDao;
import com.everhomes.server.schema.tables.daos.EhGroupsDao;
import com.everhomes.server.schema.tables.pojos.EhGroupMembers;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.records.EhGroupMembersRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

import static com.everhomes.server.schema.Tables.*;

@Component
public class GroupServiceProviderImpl implements GroupServiceProvider {

    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private CacheProvider cacheProvider;
    
    @Override
    public void createGroup(Group group) {
        long id = this.shardingProvider.allocShardableContentId(EhGroups.class).second();
        group.setId(id);
        group.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, id));
        EhGroupsDao dao = new EhGroupsDao(context.configuration());
        dao.insert(group);
    }

    @Override
    @CacheEvict(value = "Group", key="#group.id")
    public void updateGroup(Group group) {
        assert(group.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, group.getId()));
        EhGroupsDao dao = new EhGroupsDao(context.configuration());
        dao.update(group);
    }

    @Override
    @CacheEvict(value = "Group", key="#group.id")
    public void deleteGroup(Group group) {
        assert(group.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, group.getId()));
        EhGroupsDao dao = new EhGroupsDao(context.configuration());
        dao.delete(group);
    }

    @Override
    @CacheEvict(value = "Group", key="#id")
    public void deleteGroup(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, id));
        EhGroupsDao dao = new EhGroupsDao(context.configuration());
        dao.deleteById(id);
    }

    @Override
    @Cacheable(value = "Group", key="#id")
    public Group findGroupById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroups.class, id));
        EhGroupsDao dao = new EhGroupsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Group.class);
    }
    
    public void createGroupMember(final GroupMember groupMember) {
        final long id = this.shardingProvider.allocShardableContentId(EhGroupMembers.class).second();
        groupMember.setId(id);
        groupMember.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        this.dbProvider.execute((TransactionStatus status) -> {
            Group group = findGroupById(groupMember.getGroupId());
            if(group == null)
                throw new InvalidParameterException("Missing group info in GroupMember parameter");
            
            DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroupMembers.class, id));
            EhGroupMembersDao dao = new EhGroupMembersDao(context.configuration());
            dao.insert(groupMember);
            
            group.setMemberCount(1 + group.getMemberCount().longValue());
            updateGroup(group);
            
            CacheAccessor accessor = this.cacheProvider.getCacheAccessor("GroupMemberList-" + groupMember.getGroupId());
            accessor.clear();
            
            return null;
        });
    }
    
    public void updateGroupMember(GroupMember groupMember) {
        assert(groupMember.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroupMembers.class, groupMember.getId()));
        EhGroupMembersDao dao = new EhGroupMembersDao(context.configuration());
        dao.update(groupMember);
        
        CacheAccessor accessor = this.cacheProvider.getCacheAccessor("GroupMember");
        
        // clear id cache
        Method method = ReflectionUtils.findMethod(GroupServiceProviderImpl.class, "findGroupMemberById");
        accessor.evict(this, method, groupMember.getId());
        
        method = ReflectionUtils.findMethod(GroupServiceProviderImpl.class, "findGroupMemberByMemberInfo");
        accessor.evict(this, method, groupMember.getGroupId(), groupMember.getMemberType(), groupMember.getMemberId());
        
        accessor = this.cacheProvider.getCacheAccessor("GroupMemberList-" + groupMember.getGroupId());
        accessor.clear();
    }
    
    public void deleteGroupMember(final GroupMember groupMember) {
        assert(groupMember.getId() != null);
        deleteGroupMemberById(groupMember.getId());
    }
    
    public void deleteGroupMemberById(final long id) {
        boolean[] found = new boolean[1];
        GroupMember[] foundMember = new GroupMember[1];
        
        this.dbProvider.execute((TransactionStatus status)-> {
            GroupMember member = findGroupMemberById(id);
            
            // make it idempotent
            if(member != null) {
                Group group = findGroupById(member.getGroupId());
                
                DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroupMembers.class, id));
                EhGroupMembersDao dao = new EhGroupMembersDao(context.configuration());
                dao.delete(member);
                
                found[0] = true;
                foundMember[0] = member;
                
                if(group != null) {
                    group.setMemberCount(group.getMemberCount().longValue() - 1);
                    updateGroup(group);
                }
            }

            return null;
        });
        
        if(found[0]) {
            CacheAccessor accessor = this.cacheProvider.getCacheAccessor("GroupMember");
            
            // clear id cache
            Method method = ReflectionUtils.findMethod(GroupServiceProviderImpl.class, "findGroupMemberById");
            accessor.evict(this, method, id);
            
            method = ReflectionUtils.findMethod(GroupServiceProviderImpl.class, "findGroupMemberByMemberInfo");
            accessor.evict(this, method, foundMember[0].getGroupId(), foundMember[0].getMemberType(), foundMember[0].getMemberId());
            
            accessor = this.cacheProvider.getCacheAccessor("GroupMemberList-" + foundMember[0].getGroupId());
            accessor.clear();
        }
    }
    
    @Cacheable(value = "GroupMember", key="#id")
    public GroupMember findGroupMemberById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroupMembers.class, id));
        EhGroupMembersDao dao = new EhGroupMembersDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), GroupMember.class);
    }
    
    @Cacheable(value = "GroupMember", key="{#groupId, #memberType, #memberId}")
    public GroupMember findGroupMemberByMemberInfo(final long groupId, final String memberType, final long memberId) {
        final GroupMember[] result = new GroupMember[1];

        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroupMembers.class), result, 
            (DSLContext context, Object reducingContext) -> {
                EhGroupMembersRecord record = (EhGroupMembersRecord)context.select().from(EH_GROUP_MEMBERS)
                .where(EH_GROUP_MEMBERS.GROUP_ID.eq(groupId))
                .and(EH_GROUP_MEMBERS.MEMBER_TYPE.eq(memberType))
                .and(EH_GROUP_MEMBERS.MEMBER_ID.eq(memberId))
                .fetchOne();
            
                if(record != null) {
                    result[0] = ConvertHelper.convert(record, GroupMember.class);
                    return false;
                }
                
                return true;
            });
        
        return result[0];
    }

    public List<GroupMember> listGroupMembers(final GroupMemberLocator locator, final int count) {
        CacheAccessor cacheAccessor = this.cacheProvider.getCacheAccessor("GroupMemberList-" + locator.getGroupId());
        String cacheKey = locator.getHashString() + count;
        return cacheAccessor.get(cacheKey, () -> {
            final List<GroupMember> members = new ArrayList<GroupMember>();
    
            if(locator.getShardIterator() == null) {
                AccessSpec accessSpec = AccessSpec.readOnlyWith(EhGroupMembers.class);
                ShardIterator shardIterator = new ShardIterator(accessSpec);
                
                locator.setShardIterator(shardIterator);
            }
            
            this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
                SelectConditionStep<Record> query = context.select()
                        .from(EH_GROUP_MEMBERS)
                        .where(EH_GROUP_MEMBERS.GROUP_ID.eq(locator.getGroupId()));
                if(locator.getAnchor() != null)
                    query = query.and(EH_GROUP_MEMBERS.ID.gt(locator.getAnchor().longValue()));
                query.orderBy(EH_GROUP_MEMBERS.GROUP_ID.asc()).limit(count - members.size());
                query.fetch().map((Record arg) -> {
                        GroupMember member = ConvertHelper.convert(arg, GroupMember.class);
                        members.add(member);
                        return null;
                });
    
                if(members.size() >= count) {
                    locator.setAnchor(members.get(members.size() -1).getId());
                    return AfterAction.done;
                }
                
                return AfterAction.next;
            });
    
            if(members.size() > 0) {
                locator.setAnchor(members.get(members.size() -1).getId());
            }
            return members;
        });
    }
}
