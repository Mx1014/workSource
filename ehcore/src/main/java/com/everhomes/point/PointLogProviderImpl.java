// @formatter:off
package com.everhomes.point;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.point.ListPointLogsCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPointLogsDao;
import com.everhomes.server.schema.tables.pojos.EhPointLogs;
import com.everhomes.server.schema.tables.records.EhPointLogsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class PointLogProviderImpl implements PointLogProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPointLog(PointLog pointLog) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPointLogs.class));
		pointLog.setId(id);
		pointLog.setCreateTime(DateUtils.currentTimestamp());
		// pointLog.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(pointLog);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPointLogs.class, id);
	}

	@Override
	public void updatePointLog(PointLog pointLog) {
		// pointLog.setUpdateTime(DateUtils.currentTimestamp());
		// pointLog.setUpdateUid(UserContext.currentUserId());
        rwDao().update(pointLog);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPointLogs.class, pointLog.getId());
	}

	@Override
	public PointLog findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PointLog.class);
	}

    @Override
    public List<PointLog> listPointLogs(ListPointLogsCommand cmd, ListingLocator locator) {
        com.everhomes.server.schema.tables.EhPointLogs t = Tables.EH_POINT_LOGS;
        SelectQuery<EhPointLogsRecord> query = context().selectFrom(t).getQuery();
        if (cmd.getSystemId() != null) {
            query.addConditions(t.SYSTEM_ID.eq(cmd.getSystemId()));
        }

        Optional.ofNullable(cmd.getUserId()).ifPresent(targetUid -> query.addConditions(t.TARGET_UID.eq(targetUid)));
        Optional.ofNullable(cmd.getOperateType()).ifPresent(operateType -> query.addConditions(t.OPERATE_TYPE.eq(operateType)));
        Optional.ofNullable(cmd.getPhone()).ifPresent(phone -> query.addConditions(t.TARGET_PHONE.eq(phone)));
        Optional.ofNullable(cmd.getStartTime()).ifPresent(startTime -> query.addConditions(t.CREATE_TIME.gt(new Timestamp(startTime))));
        Optional.ofNullable(cmd.getEndTime()).ifPresent(endTime -> query.addConditions(t.CREATE_TIME.le(new Timestamp(endTime))));

        Optional.ofNullable(locator.getAnchor()).ifPresent(pageAnchor -> query.addConditions(t.ID.le(pageAnchor)));
        Optional.ofNullable(cmd.getPageSize()).ifPresent(query::addLimit);

        List<PointLog> list = query.fetchInto(PointLog.class);
        if (list.size() > cmd.getPageSize()) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

    private EhPointLogsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhPointLogsDao(context.configuration());
	}

	private EhPointLogsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhPointLogsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
