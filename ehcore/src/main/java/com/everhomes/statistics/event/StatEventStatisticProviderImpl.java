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
import com.everhomes.server.schema.tables.pojos.EhStatEventLogs;
import com.everhomes.server.schema.tables.pojos.EhStatEventStatistics;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
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
    public void insertEventStatList(List<StatEventStatistic> statList) {
        statList.forEach(this::prepare);
        rwDao().insert(statList.toArray(new StatEventStatistic[statList.size()]));
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhStatEventStatistics.class, null);
    }

    private void prepare(StatEventStatistic stat) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatEventLogs.class));
        stat.setId(id);
        stat.setCreateTime(DateUtils.currentTimestamp());
    }

    @Override
    public List<StatEventStatistic> countAndListEventStat(Integer namespaceId, Long parentId, String identifier, Date startDate, Date endDate) {
        DSLContext context = context();

        // 根据identifier拿到所有门户统计，不用去重
        SelectConditionStep<Record1<Long>> portalStatIdList = context.
                select(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.ID)
                .from(Tables.EH_STAT_EVENT_PORTAL_STATISTICS)
                .where(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.PARENT_ID.eq(parentId))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.IDENTIFIER.eq(identifier))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.STAT_DATE.between(startDate, endDate));

        // 再去拿到所有事件统计
        List<Field<?>> fields = new ArrayList<>();
        fields.add(DSL.sum(Tables.EH_STAT_EVENT_STATISTICS.TOTAL_COUNT).as("totalCountTimes"));
        fields.addAll(Arrays.asList(Tables.EH_STAT_EVENT_STATISTICS.fields()));

        List<StatEventStatistic> statList = context
                .select(fields)
                .from(Tables.EH_STAT_EVENT_STATISTICS)
                .where(Tables.EH_STAT_EVENT_STATISTICS.EVENT_PORTAL_STAT_ID.in(portalStatIdList))
                .groupBy(Tables.EH_STAT_EVENT_STATISTICS.OWNER_TYPE, Tables.EH_STAT_EVENT_STATISTICS.OWNER_ID)
                .fetch().map(r -> {
                    StatEventStatistic stat = r.into(StatEventStatistic.class);
                    BigDecimal totalCountTimes = r.getValue(DSL.fieldByName(BigDecimal.class, "totalCountTimes"));
                    stat.setTotalCount(totalCountTimes.longValue());
                    return stat;
                });
        return statList;
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
