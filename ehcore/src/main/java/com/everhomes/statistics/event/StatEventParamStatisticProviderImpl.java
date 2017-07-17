// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhStatEventParamStatisticsDao;
import com.everhomes.server.schema.tables.pojos.EhStatEventParamStatistics;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StatEventParamStatisticProviderImpl implements StatEventParamStatisticProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createStatEventParamStatistic(StatEventParamStatistic statEventParamStatistic) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatEventParamStatistics.class));
		statEventParamStatistic.setId(id);
		statEventParamStatistic.setCreateTime(DateUtils.currentTimestamp());
		// statEventParamStatistic.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(statEventParamStatistic);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhStatEventParamStatistics.class, id);
	}

	@Override
	public void updateStatEventParamStatistic(StatEventParamStatistic statEventParamStatistic) {
		// statEventParamStatistic.setUpdateTime(DateUtils.currentTimestamp());
		// statEventParamStatistic.setUpdateUid(UserContext.currentUserId());
        rwDao().update(statEventParamStatistic);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhStatEventParamStatistics.class, statEventParamStatistic.getId());
	}

	@Override
	public StatEventParamStatistic findStatEventParamStatisticById(Long id) {
		return ConvertHelper.convert(dao().findById(id), StatEventParamStatistic.class);
	}

    @Override
    public List<StatEventParamStatistic> listEventParamStat(List<Long> eventStatIdList) {
        return context().selectFrom(Tables.EH_STAT_EVENT_PARAM_STATISTICS)
                .where(Tables.EH_STAT_EVENT_PARAM_STATISTICS.EVENT_STAT_ID.in(eventStatIdList))
                .fetchInto(StatEventParamStatistic.class);
    }

    // @Override
	// public List<StatEventParamStatistic> listStatEventParamStatistic() {
	// 	return getReadOnlyContext().select().from(Tables.EH_STAT_EVENT_PARAM_STATISTICS)
	//			.orderBy(Tables.EH_STAT_EVENT_PARAM_STATISTICS.ID.asc())
	//			.fetch().map(r -> ConvertHelper.convert(r, StatEventParamStatistic.class));
	// }
	
	private EhStatEventParamStatisticsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhStatEventParamStatisticsDao(context.configuration());
	}

	private EhStatEventParamStatisticsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhStatEventParamStatisticsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
