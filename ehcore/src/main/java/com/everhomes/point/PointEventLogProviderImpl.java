// @formatter:off
package com.everhomes.point;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhPointEventLogsRecord;
import com.everhomes.server.schema.tables.daos.EhPointEventLogsDao;
import com.everhomes.server.schema.tables.pojos.EhPointEventLogs;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PointEventLogProviderImpl implements PointEventLogProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPointEventLog(PointEventLog pointEventLog) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointEventLogs.class));
		pointEventLog.setId(id);
		pointEventLog.setCreateTime(DateUtils.currentTimestamp());
		// pointEventLog.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(pointEventLog);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointEventLogs.class, id);
	}

	@Override
	public void updatePointEventLog(PointEventLog pointEventLog) {
		// pointEventLog.setUpdateTime(DateUtils.currentTimestamp());
		// pointEventLog.setUpdateUid(UserContext.currentUserId());
        rwDao().update(pointEventLog);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointEventLogs.class, pointEventLog.getId());
	}

    @Override
    public List<PointEventLog> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback) {
        com.everhomes.server.schema.tables.EhPointEventLogs t = Tables.EH_POINT_EVENT_LOGS;

        SelectQuery<EhPointEventLogsRecord> query = context().selectQuery(t);
        if (callback != null) {
            callback.buildCondition(locator, query);
        }
        if (locator.getAnchor() != null) {
            query.addConditions(t.ID.gt(locator.getAnchor()));
        }

        if (count > 0) {
            query.addLimit(count + 1);
        }
        query.addOrderBy(t.ID.asc());

        List<PointEventLog> list = query.fetchInto(PointEventLog.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

    @Override
    public Long getNextEventLogId() {
        return sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointEventLogs.class));
    }

    @Override
    public void createPointEventLogsWithId(List<PointEventLog> removeLogs) {
        rwDao().insert(removeLogs.toArray(new EhPointEventLogs[removeLogs.size()]));
    }

	@Override
	public PointEventLog findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PointEventLog.class);
	}

    @Override
    public List<PointEventLog> listEventLog(Long categoryId, Byte status, int pageSize, ListingLocator locator) {
        com.everhomes.server.schema.tables.EhPointEventLogs t = Tables.EH_POINT_EVENT_LOGS;
        return this.query(locator, pageSize, (locator1, query) -> {
            query.addConditions(t.CATEGORY_ID.eq(categoryId));
            query.addConditions(t.STATUS.eq(status));
            return query;
        });
    }

    @Override
    public void updatePointEventLogStatus(List<Long> idList, Byte status) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhPointEventLogs t = Tables.EH_POINT_EVENT_LOGS;

        context.update(t)
                .set(t.STATUS, status)
                .where(t.ID.in(idList))
                .execute();
    }

    private EhPointEventLogsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPointEventLogsDao(context.configuration());
	}

	private EhPointEventLogsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPointEventLogsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
