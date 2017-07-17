// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhStatEventStatisticsDao;
import com.everhomes.server.schema.tables.pojos.EhStatEventStatistics;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StatEventStatisticProviderImpl implements StatEventStatisticProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createStatEventStatistic(StatEventStatistic statEventStatistic) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatEventStatistics.class));
		statEventStatistic.setId(id);
		statEventStatistic.setCreateTime(DateUtils.currentTimestamp());
		// statEventStatistic.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(statEventStatistic);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhStatEventStatistics.class, id);
	}

	@Override
	public void updateStatEventStatistic(StatEventStatistic statEventStatistic) {
		// statEventStatistic.setUpdateTime(DateUtils.currentTimestamp());
		// statEventStatistic.setUpdateUid(UserContext.currentUserId());
        rwDao().update(statEventStatistic);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhStatEventStatistics.class, statEventStatistic.getId());
	}

	@Override
	public StatEventStatistic findStatEventStatisticById(Long id) {
		return ConvertHelper.convert(dao().findById(id), StatEventStatistic.class);
	}

    @Override
    public List<Long> listEventStatIdByPortalStatIds(List<Long> portalStatIdList) {
        return context().select(Tables.EH_STAT_EVENT_STATISTICS.ID)
                .from(Tables.EH_STAT_EVENT_STATISTICS)
                .where(Tables.EH_STAT_EVENT_STATISTICS.EVENT_PORTAL_STAT_ID.in(portalStatIdList))
                .fetchInto(Long.class);
    }

    @Override
    public List<StatEventStatistic> listEventStatByPortalStatIds(List<Long> portalStatIdList) {
        return context().select().from(Tables.EH_STAT_EVENT_STATISTICS)
                .where(Tables.EH_STAT_EVENT_STATISTICS.EVENT_PORTAL_STAT_ID.in(portalStatIdList))
                .fetchInto(StatEventStatistic.class);
    }

    private EhStatEventStatisticsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhStatEventStatisticsDao(context.configuration());
	}

	private EhStatEventStatisticsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhStatEventStatisticsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
