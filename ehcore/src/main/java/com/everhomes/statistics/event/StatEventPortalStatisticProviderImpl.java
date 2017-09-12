// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhStatEventPortalStatisticsDao;
import com.everhomes.server.schema.tables.pojos.EhStatEventPortalStatistics;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.SelectHavingStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class StatEventPortalStatisticProviderImpl implements StatEventPortalStatisticProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createStatEventPortalStatistic(StatEventPortalStatistic statEventPortalStatistic) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatEventPortalStatistics.class));
		statEventPortalStatistic.setId(id);
		statEventPortalStatistic.setCreateTime(DateUtils.currentTimestamp());
		// statEventPortalStatistic.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(statEventPortalStatistic);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhStatEventPortalStatistics.class, id);
	}

	@Override
	public void updateStatEventPortalStatistic(StatEventPortalStatistic statEventPortalStatistic) {
		// statEventPortalStatistic.setUpdateTime(DateUtils.currentTimestamp());
		// statEventPortalStatistic.setUpdateUid(UserContext.currentUserId());
        rwDao().update(statEventPortalStatistic);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhStatEventPortalStatistics.class, statEventPortalStatistic.getId());
	}

	@Override
	public StatEventPortalStatistic findStatEventPortalStatisticById(Long id) {
		return ConvertHelper.convert(dao().findById(id), StatEventPortalStatistic.class);
	}

    @Override
    public List<StatEventPortalStatistic> listEventPortalStatByZeroParentId(Integer namespaceId, Byte statType, Date startDate, Date endDate) {
        SelectHavingStep<Record1<Long>> lastRecordIdCondition = context().select(DSL.max(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.ID))
                .from(Tables.EH_STAT_EVENT_PORTAL_STATISTICS)
                .where(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.PARENT_ID.eq(0L))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.STAT_TYPE.eq(statType))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.STAT_DATE.between(startDate, endDate))
                .groupBy(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.OWNER_TYPE, Tables.EH_STAT_EVENT_PORTAL_STATISTICS.OWNER_ID);

        return context().selectFrom(Tables.EH_STAT_EVENT_PORTAL_STATISTICS)
                .where(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.ID.in(lastRecordIdCondition))
                .fetchInto(StatEventPortalStatistic.class);
    }

    @Override
    public List<StatEventPortalStatistic> listEventPortalStatByParentId(Integer namespaceId, Long parentId, Byte statType, Date startDate, Date endDate) {

        SelectConditionStep<Record1<Long>> ownerIdCondition = context()
                .select(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.OWNER_ID)
                .from(Tables.EH_STAT_EVENT_PORTAL_STATISTICS)
                .where(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.ID.eq(parentId));

        SelectConditionStep<Record1<Long>> idCondition = context()
                .select(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.ID)
                .from(Tables.EH_STAT_EVENT_PORTAL_STATISTICS)
                .where(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.OWNER_ID.in(ownerIdCondition));

        SelectHavingStep<Record1<Long>> lastRecordIdCondition = context()
                .select(DSL.max(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.ID))
                .from(Tables.EH_STAT_EVENT_PORTAL_STATISTICS)
                .where(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.PARENT_ID.in(idCondition))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.STAT_TYPE.eq(statType))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.STAT_DATE.between(startDate, endDate))
                .groupBy(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.OWNER_TYPE, Tables.EH_STAT_EVENT_PORTAL_STATISTICS.OWNER_ID);

        return context().selectFrom(Tables.EH_STAT_EVENT_PORTAL_STATISTICS)
                .where(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.ID.in(lastRecordIdCondition))
                .fetchInto(StatEventPortalStatistic.class);
    }

    @Override
    public List<Long> listEventPortalStatByIdentifier(Integer namespaceId, Long parentId, String identifier, Date startDate, Date endDate) {
        return context().select(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.ID)
                .from(Tables.EH_STAT_EVENT_PORTAL_STATISTICS)
                .where(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.PARENT_ID.eq(parentId))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.IDENTIFIER.eq(identifier))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.STAT_DATE.between(startDate, endDate))
                // .groupBy(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.OWNER_ID)
                .fetchInto(Long.class);
    }

    @Override
    public StatEventPortalStatistic findStatEventPortalStatistic(Integer namespaceId, byte statType, String configName, Date statDate) {
        return context().select()
                .from(Tables.EH_STAT_EVENT_PORTAL_STATISTICS)
                .where(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.STAT_TYPE.eq(statType))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.NAME.eq(configName))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.STAT_DATE.eq(statDate))
                .fetchAnyInto(StatEventPortalStatistic.class);
    }

    @Override
    public StatEventPortalStatistic findStatEventPortalStatistic(Integer namespaceId, byte statType, String ownerType, Long ownerId, Date statDate) {
        return context().select()
                .from(Tables.EH_STAT_EVENT_PORTAL_STATISTICS)
                .where(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.STAT_TYPE.eq(statType))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.OWNER_ID.eq(ownerId))
                .and(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.STAT_DATE.eq(statDate))
                .fetchAnyInto(StatEventPortalStatistic.class);
    }

    @Override
    public void deleteEventPortalStatByDate(Date date) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        context.delete(Tables.EH_STAT_EVENT_PORTAL_STATISTICS)
                .where(Tables.EH_STAT_EVENT_PORTAL_STATISTICS.STAT_DATE.eq(date))
                .execute();
    }

    private EhStatEventPortalStatisticsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhStatEventPortalStatisticsDao(context.configuration());
	}

	private EhStatEventPortalStatisticsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhStatEventPortalStatisticsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
