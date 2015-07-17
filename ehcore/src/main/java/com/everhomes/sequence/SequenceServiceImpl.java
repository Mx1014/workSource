package com.everhomes.sequence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhForums;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.pojos.EhPolls;
import com.everhomes.server.schema.tables.pojos.EhUsers;

@Component
public class SequenceServiceImpl implements SequenceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SequenceServiceImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void syncSequence() {
        syncTableSequence(null, com.everhomes.schema.Tables.EH_ACLS.getName(), (dbContext) -> { 
            return dbContext.select(com.everhomes.schema.Tables.EH_ACLS.ID.max())
                .from(com.everhomes.schema.Tables.EH_ACLS).fetchOne().value1(); 
        });
        
        syncTableSequence(null, com.everhomes.schema.Tables.EH_SERVER_SHARD_MAP.getName(), (dbContext) -> { 
            Integer max = dbContext.select(com.everhomes.schema.Tables.EH_SERVER_SHARD_MAP.ID.max())
                .from(com.everhomes.schema.Tables.EH_SERVER_SHARD_MAP).fetchOne().value1();
            Long lmax = null;
            if(max != null) {
                lmax = Long.valueOf(max.longValue());
            }
            return lmax;
        });
        
        syncTableSequence(null, com.everhomes.schema.Tables.EH_CONTENT_SHARD_MAP.getName(), (dbContext) -> { 
            return dbContext.select(com.everhomes.schema.Tables.EH_CONTENT_SHARD_MAP.ID.max())
                .from(com.everhomes.schema.Tables.EH_CONTENT_SHARD_MAP).fetchOne().value1();
        });
        
        syncTableSequence(EhUsers.class, Tables.EH_USERS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_USERS.ID.max()).from(Tables.EH_USERS).fetchOne().value1(); 
        });
        
        syncTableSequence(EhUsers.class, Tables.EH_USER_GROUPS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_USER_GROUPS.ID.max()).from(Tables.EH_USER_GROUPS).fetchOne().value1(); 
        });
        
        syncTableSequence(EhUsers.class, Tables.EH_USER_INVITATIONS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_USER_INVITATIONS.ID.max()).from(Tables.EH_USER_INVITATIONS).fetchOne().value1(); 
        });
        
        syncTableSequence(EhUsers.class, Tables.EH_USER_IDENTIFIERS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_USER_IDENTIFIERS.ID.max()).from(Tables.EH_USER_IDENTIFIERS).fetchOne().value1(); 
        });
        
        syncTableSequence(EhUsers.class, Tables.EH_USER_INVITATION_ROSTER.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_USER_INVITATION_ROSTER.ID.max()).from(Tables.EH_USER_INVITATION_ROSTER).fetchOne().value1(); 
        });
        
        syncTableSequence(EhUsers.class, Tables.EH_USER_POSTS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_USER_POSTS.ID.max()).from(Tables.EH_USER_POSTS).fetchOne().value1(); 
        });
        
        syncTableSequence(EhUsers.class, Tables.EH_USER_LIKES.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_USER_LIKES.ID.max()).from(Tables.EH_USER_LIKES).fetchOne().value1(); 
        });
        
        syncTableSequence(EhUsers.class, Tables.EH_USER_FAVORITES.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_USER_FAVORITES.ID.max()).from(Tables.EH_USER_FAVORITES).fetchOne().value1(); 
        });
        
        syncTableSequence(EhUsers.class, Tables.EH_USER_PROFILES.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_USER_PROFILES.ID.max()).from(Tables.EH_USER_PROFILES).fetchOne().value1(); 
        });
        
        syncTableSequence(EhForums.class, Tables.EH_FORUMS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_FORUMS.ID.max()).from(Tables.EH_FORUMS).fetchOne().value1(); 
        });
        
        syncTableSequence(EhAddresses.class, Tables.EH_ADDRESSES.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_ADDRESSES.ID.max()).from(Tables.EH_ADDRESSES).fetchOne().value1(); 
        });
        
        syncTableSequence(null, Tables.EH_CONTENT_SERVER_RESOURCES.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_CONTENT_SERVER_RESOURCES.ID.max())
                .from(Tables.EH_CONTENT_SERVER_RESOURCES).fetchOne().value1(); 
        });
        
        syncTableSequence(EhGroups.class, Tables.EH_GROUPS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_GROUPS.ID.max()).from(Tables.EH_GROUPS).fetchOne().value1(); 
        });
        
        syncTableSequence(EhGroups.class, Tables.EH_GROUP_MEMBERS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_GROUP_MEMBERS.ID.max()).from(Tables.EH_GROUP_MEMBERS).fetchOne().value1(); 
        });
        
        syncTableSequence(EhGroups.class, Tables.EH_GROUP_OP_REQUESTS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_GROUP_OP_REQUESTS.ID.max()).from(Tables.EH_GROUP_OP_REQUESTS).fetchOne().value1(); 
        });
        
        syncTableSequence(EhForums.class, Tables.EH_FORUM_POSTS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_FORUM_POSTS.ID.max()).from(Tables.EH_FORUM_POSTS).fetchOne().value1(); 
        });
        
        syncTableSequence(EhForums.class, Tables.EH_FORUM_ASSIGNED_SCOPES.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_FORUM_ASSIGNED_SCOPES.ID.max()).from(Tables.EH_FORUM_ASSIGNED_SCOPES).fetchOne().value1(); 
        });
        
        syncTableSequence(EhForums.class, Tables.EH_FORUM_ATTACHMENTS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_FORUM_ATTACHMENTS.ID.max()).from(Tables.EH_FORUM_ATTACHMENTS).fetchOne().value1(); 
        });
        
        syncTableSequence(EhCommunities.class, Tables.EH_COMMUNITIES.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_COMMUNITIES.ID.max()).from(Tables.EH_COMMUNITIES).fetchOne().value1(); 
        });
        
        syncTableSequence(EhCommunities.class, Tables.EH_COMMUNITY_GEOPOINTS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_COMMUNITY_GEOPOINTS.ID.max()).from(Tables.EH_COMMUNITY_GEOPOINTS).fetchOne().value1(); 
        });
        
        syncTableSequence(EhActivities.class, Tables.EH_ACTIVITIES.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_ACTIVITIES.ID.max()).from(Tables.EH_ACTIVITIES).fetchOne().value1(); 
        });
        
        syncTableSequence(EhActivities.class, Tables.EH_ACTIVITY_ROSTER.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_ACTIVITY_ROSTER.ID.max()).from(Tables.EH_ACTIVITY_ROSTER).fetchOne().value1(); 
        });
        
        syncTableSequence(EhPolls.class, Tables.EH_POLLS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_POLLS.ID.max()).from(Tables.EH_POLLS).fetchOne().value1(); 
        });
        
        syncTableSequence(EhPolls.class, Tables.EH_POLL_ITEMS.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_POLL_ITEMS.ID.max()).from(Tables.EH_POLL_ITEMS).fetchOne().value1(); 
        });
        
        syncTableSequence(EhPolls.class, Tables.EH_POLL_VOTES.getName(), (dbContext) -> { 
            return dbContext.select(Tables.EH_POLL_VOTES.ID.max()).from(Tables.EH_POLL_VOTES).fetchOne().value1(); 
        });
    }
    
    private void syncTableSequence(Class keytableCls, String tableName, SequenceQueryCallback callback) {
        AccessSpec spec = null;
        if(keytableCls == null) {
            spec = AccessSpec.readOnly();
        } else {
            spec = AccessSpec.readOnlyWith(keytableCls);
        }
        
        Long result[] = new Long[1];
        result[0] = 0L;
        dbProvider.mapReduce(spec, null, (dbContext, contextObj) -> {
            //Long max = dbContext.select(Tables.EH_USERS.ID.max()).from(Tables.EH_USERS).fetchOne().value1();
            Long max = callback.maxSequence(dbContext);

            if(max != null && max.longValue() > result[0].longValue()) {
                result[0] = max;
            }
            return true;
        });
        long orgNextSequence = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUsers.class));
        sequenceProvider.resetSequence(NameMapper.getSequenceDomainFromTablePojo(EhUsers.class), result[0].longValue() + 1);
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Syn table sequence, tableName=" + tableName + ", orgNextSequence=" + orgNextSequence 
                + ", newSequence=" + (result[0].longValue() + 1));
        }
    }
}
