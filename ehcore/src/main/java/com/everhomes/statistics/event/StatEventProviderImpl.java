// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhStatEventsDao;
import com.everhomes.server.schema.tables.pojos.EhStatEvents;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StatEventProviderImpl implements StatEventProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createStatEvent(StatEvent statEvent) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatEvents.class));
		statEvent.setId(id);
		statEvent.setCreateTime(DateUtils.currentTimestamp());
		// statEvent.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(statEvent);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhStatEvents.class, id);
	}

	@Override
	public void updateStatEvent(StatEvent statEvent) {
		// statEvent.setUpdateTime(DateUtils.currentTimestamp());
		// statEvent.setUpdateUid(UserContext.currentUserId());
        rwDao().update(statEvent);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhStatEvents.class, statEvent.getId());
	}

    @Override
    public List<StatEvent> listStatEvent() {
        return context().selectFrom(Tables.EH_STAT_EVENTS).fetchInto(StatEvent.class);
    }

    @Override
	public StatEvent findStatEventById(Long id) {
		return ConvertHelper.convert(dao().findById(id), StatEvent.class);
	}

    @Override
    public StatEvent findStatEventByName(String eventName) {
        return context().selectFrom(Tables.EH_STAT_EVENTS)
                .where(Tables.EH_STAT_EVENTS.EVENT_NAME.eq(eventName))
                .fetchAnyInto(StatEvent.class);
    }

	private EhStatEventsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhStatEventsDao(context.configuration());
	}

	private EhStatEventsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhStatEventsDao(context.configuration());
	}

	private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
