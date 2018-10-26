package com.everhomes.group;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhGroupMemberLogsDao;
import com.everhomes.server.schema.tables.pojos.EhGroupMemberLogs;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RecordHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xq.tian on 2017/7/11.
 */
@Repository
public class GroupMemberLogProviderImpl implements GroupMemberLogProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

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
    public List<GroupMemberLog> listGroupMemberLogByGroupId(Long groupId, String keyword, Long from, int pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.select().from(Tables.EH_GROUP_MEMBER_LOGS).getQuery();
        query.addConditions(Tables.EH_GROUP_MEMBER_LOGS.GROUP_ID.eq(groupId));
        if(keyword != null){
            query.addConditions(Tables.EH_GROUP_MEMBER_LOGS.MEMBER_NICK_NAME.like("%" + keyword + "%"));
        }
        query.addOrderBy(Tables.EH_GROUP_MEMBER_LOGS.ID.desc());
        query.addLimit(from.intValue(), pageSize);
        List<GroupMemberLog> list = query.fetch().map(r -> ConvertHelper.convert(r, GroupMemberLog.class));

        return  list;
    }

    @Override
    public List<GroupMemberLog> listGroupMemberLogByUserId(Long userId, Byte status) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<Record> query = context.select().from(Tables.EH_GROUP_MEMBER_LOGS).getQuery();
        query.addConditions(Tables.EH_GROUP_MEMBER_LOGS.MEMBER_ID.eq(userId));
        if(status != null){
            query.addConditions(Tables.EH_GROUP_MEMBER_LOGS.MEMBER_STATUS.eq(status));
        }
        query.addOrderBy(Tables.EH_GROUP_MEMBER_LOGS.ID.desc());
        List<GroupMemberLog> list = query.fetch().map(r -> ConvertHelper.convert(r, GroupMemberLog.class));

        return  list;
    }

    @Override
    public void createGroupMemberLog(GroupMemberLog groupMemberLog) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhGroupMemberLogs.class));
        groupMemberLog.setId(id);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGroupMemberLogs.class, groupMemberLog.getId()));
        EhGroupMemberLogsDao dao = new EhGroupMemberLogsDao(context.configuration());
        dao.insert(groupMemberLog);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhGroupMemberLogs.class, id);
    }

    @Override
    public List<GroupMemberLog> queryGroupMemberLog(String userInfoKeyword, String identifierToken, String communityKeyword, List<Long> communityIds, Byte status, CrossShardListingLocator locator, int pageSize) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<Record> query = context.select(Tables.EH_GROUP_MEMBER_LOGS.fields())
                .from(Tables.EH_GROUP_MEMBER_LOGS).getQuery();

        query.addSelect(Tables.EH_USERS.NICK_NAME);
        query.addJoin(Tables.EH_USERS, JoinType.JOIN, Tables.EH_GROUP_MEMBER_LOGS.MEMBER_ID.eq(Tables.EH_USERS.ID));
        
        if (StringUtils.isNotBlank(identifierToken)) {
        	//联表EH_USER_IDENTIFIERS，由于用户认证分邮箱与手机号两种匹配方式，因为客户端暂只开放手机号，所以这里指定 IDENTIFIER_TYPE 为 0（手机号） ;add by moubinmo,18/10/25
        	query.addJoin(Tables.EH_USER_IDENTIFIERS, JoinType.JOIN, Tables.EH_USERS.ID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID).and(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TYPE.eq((byte)0)));        	
        }

        query.addJoin(Tables.EH_USER_IDENTIFIERS, JoinType.JOIN, Tables.EH_USERS.ID.eq(Tables.EH_USER_IDENTIFIERS.OWNER_UID));

        query.addSelect(Tables.EH_COMMUNITIES.NAME);
        query.addJoin(Tables.EH_COMMUNITIES, JoinType.JOIN, Tables.EH_GROUP_MEMBER_LOGS.COMMUNITY_ID.eq(Tables.EH_COMMUNITIES.ID));

        query.addConditions(Tables.EH_GROUP_MEMBER_LOGS.COMMUNITY_ID.in(communityIds));
        query.addConditions(Tables.EH_GROUP_MEMBER_LOGS.MEMBER_STATUS.eq(status));
        if (StringUtils.isNotBlank(userInfoKeyword)) {
            String keyword = "%" + userInfoKeyword + "%";
            query.addConditions(Tables.EH_USERS.NICK_NAME.like(keyword).or(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.like(keyword)));
        }
        if (StringUtils.isNotBlank(identifierToken)) {
            String keyword = "%" + identifierToken + "%";
            query.addConditions(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.like(keyword));
        }
        if (StringUtils.isNotBlank(communityKeyword)) {
            String keyword = "%" + communityKeyword + "%";
            query.addConditions(Tables.EH_COMMUNITIES.NAME.like(keyword));
        }

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_GROUP_MEMBER_LOGS.ID.lt(locator.getAnchor()));
        }

        query.addLimit(pageSize + 1);
        query.addOrderBy(Tables.EH_GROUP_MEMBER_LOGS.ID.desc());

        List<GroupMemberLog> logList = query.fetch().map((r) -> RecordHelper.convert(r, GroupMemberLog.class));

        if (logList != null && logList.size() > pageSize) {
            locator.setAnchor(logList.get(logList.size() - 1).getId());
            logList = logList.subList(0, pageSize);
        } else {
            locator.setAnchor(null);
        }
        return logList;
    }
}
