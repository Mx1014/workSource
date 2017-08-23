// @formatter:off
package com.everhomes.user;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhUserAppealLogsDao;
import com.everhomes.server.schema.tables.pojos.EhUserAppealLogs;
import com.everhomes.server.schema.tables.records.EhUserAppealLogsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by xq.tian on 2017/6/27.
 */
@Repository
public class UserAppealLogProviderImpl implements UserAppealLogProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createUserAppealLog(UserAppealLog userAppealLog) {
        userAppealLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        userAppealLog.setCreatorUid(UserContext.current().getUser().getId());
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserAppealLogs.class));
        userAppealLog.setId(id);
        rwDao().insert(userAppealLog);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhUserAppealLogs.class, id);
    }

    @Override
    public void updateUserAppealLog(UserAppealLog userAppealLog) {
        userAppealLog.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        userAppealLog.setUpdateUid(UserContext.current().getUser().getId());
        rwDao().update(userAppealLog);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUserAppealLogs.class, userAppealLog.getId());
    }

    @Override
    public UserAppealLog findUserAppealLogById(Long id) {
        return ConvertHelper.convert(dao().findById(id), UserAppealLog.class);
    }

    @Override
    public List<UserAppealLog> listUserAppealLog(ListingLocator locator, Byte status, int pageSize) {
        SelectQuery<EhUserAppealLogsRecord> query = context().selectFrom(Tables.EH_USER_APPEAL_LOGS).getQuery();
        if (status != null) {
            query.addConditions(Tables.EH_USER_APPEAL_LOGS.STATUS.eq(status));
        }
        if (locator.getAnchor() != null) {
            query.addConditions(Tables.EH_USER_APPEAL_LOGS.ID.le(locator.getAnchor()));
        }
        query.addOrderBy(Tables.EH_USER_APPEAL_LOGS.ID.desc());
        query.addLimit(pageSize + 1);

        List<UserAppealLog> logList = query.fetchInto(UserAppealLog.class);

        if (logList != null && logList.size() > pageSize) {
            locator.setAnchor(logList.get(logList.size() - 1).getId());
            logList = logList.subList(0, pageSize);
        } else {
            locator.setAnchor(null);
        }
        return logList;
    }
    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    private EhUserAppealLogsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhUserAppealLogsDao(context.configuration());
    }

    private EhUserAppealLogsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhUserAppealLogsDao(context.configuration());
    }
}
