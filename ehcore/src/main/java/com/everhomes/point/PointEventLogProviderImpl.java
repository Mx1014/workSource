// @formatter:off
package com.everhomes.point;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhPointEventLogsDao;
import com.everhomes.server.schema.tables.pojos.EhPointEventLogs;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;

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
	public PointEventLog findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), PointEventLog.class);
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
