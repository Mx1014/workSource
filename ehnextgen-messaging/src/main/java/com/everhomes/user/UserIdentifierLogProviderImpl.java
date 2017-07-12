// @formatter:off
package com.everhomes.user;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhUserIdentifierLogsDao;
import com.everhomes.server.schema.tables.pojos.EhUserAppealLogs;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifierLogs;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by xq.tian on 2017/6/27.
 */
@Repository
public class UserIdentifierLogProviderImpl implements UserIdentifierLogProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createUserIdentifierLog(UserIdentifierLog userIdentifierLog) {
        userIdentifierLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserAppealLogs.class));
        userIdentifierLog.setId(id);
        rwDao().insert(userIdentifierLog);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUserIdentifierLogs.class, id);
    }

    @Override
    public void updateUserIdentifierLog(UserIdentifierLog userIdentifierLog) {
        rwDao().update(userIdentifierLog);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserIdentifierLogs.class, userIdentifierLog.getId());
    }

    @Override
    public UserIdentifierLog findUserIdentifierLogById(Long id) {
        return ConvertHelper.convert(dao().findById(id), UserIdentifierLog.class);
    }

    @Override
    public List<UserIdentifierLog> listUserIdentifierLog() {
        throw new RuntimeException("listUserIdentifierLog not implements");
    }

    @Override
    public UserIdentifierLog findByUserIdAndIdentifier(Long uid, Integer regionCode, String identifier) {
        return context().selectFrom(Tables.EH_USER_IDENTIFIER_LOGS)
                .where(Tables.EH_USER_IDENTIFIER_LOGS.OWNER_UID.eq(uid))
                .and(Tables.EH_USER_IDENTIFIER_LOGS.REGION_CODE.eq(regionCode))
                .and(Tables.EH_USER_IDENTIFIER_LOGS.IDENTIFIER_TOKEN.eq(identifier))
                .orderBy(Tables.EH_USER_IDENTIFIER_LOGS.ID.desc())
                .fetchAnyInto(UserIdentifierLog.class);
    }

    @Override
    public UserIdentifierLog findByUserId(Long uid) {
        return context().selectFrom(Tables.EH_USER_IDENTIFIER_LOGS)
                .where(Tables.EH_USER_IDENTIFIER_LOGS.OWNER_UID.eq(uid))
                .orderBy(Tables.EH_USER_IDENTIFIER_LOGS.ID.desc())
                .fetchAnyInto(UserIdentifierLog.class);
    }

    @Override
    public UserIdentifierLog findByIdentifier(String identifier) {
        return context().selectFrom(Tables.EH_USER_IDENTIFIER_LOGS)
                .where(Tables.EH_USER_IDENTIFIER_LOGS.IDENTIFIER_TOKEN.eq(identifier))
                .orderBy(Tables.EH_USER_IDENTIFIER_LOGS.ID.desc())
                .fetchAnyInto(UserIdentifierLog.class);
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    private EhUserIdentifierLogsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhUserIdentifierLogsDao(context.configuration());
    }

    private EhUserIdentifierLogsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhUserIdentifierLogsDao(context.configuration());
    }
}
