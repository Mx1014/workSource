package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.energy.PriceCalculationType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnergyDateStatisticsDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyDateStatistics;
import com.everhomes.server.schema.tables.records.EhEnergyDateStatisticsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Component
public class EnergyDateStatisticProviderImpl implements EnergyDateStatisticProvider {
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private ShardingProvider shardingProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public Long createEnergyDateStatistic(EnergyDateStatistic obj) {
		String key = NameMapper.getSequenceDomainFromTablePojo(EhEnergyDateStatistics.class);
		long id = sequenceProvider.getNextSequence(key);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		obj.setId(id);
		prepareObj(obj);
		EhEnergyDateStatisticsDao dao = new EhEnergyDateStatisticsDao(context.configuration());
		dao.insert(obj);
		return id;
	}

	@Override
	public void updateEnergyDateStatistic(EnergyDateStatistic obj) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhEnergyDateStatisticsDao dao = new EhEnergyDateStatisticsDao(context.configuration());
		dao.update(obj);
	}

	@Override
	public void deleteEnergyDateStatistic(EnergyDateStatistic obj) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhEnergyDateStatisticsDao dao = new EhEnergyDateStatisticsDao(context.configuration());
		dao.deleteById(obj.getId());
	}

	@Override
	public EnergyDateStatistic getEnergyDateStatisticById(Long id) {
		try {
			EnergyDateStatistic[] result = new EnergyDateStatistic[1];
			DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

			result[0] = context.select().from(Tables.EH_ENERGY_DATE_STATISTICS).where(Tables.EH_ENERGY_DATE_STATISTICS.ID.eq(id))
					.fetchAny().map((r) -> {
						return ConvertHelper.convert(r, EnergyDateStatistic.class);
					});

			return result[0];
		} catch (Exception ex) {
			// fetchAny() maybe return null
			return null;
		}
	}

	@Override
	public List<EnergyDateStatistic> queryEnergyDateStatistics(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

		SelectQuery<EhEnergyDateStatisticsRecord> query = context.selectQuery(Tables.EH_ENERGY_DATE_STATISTICS);
		if (queryBuilderCallback != null)
			queryBuilderCallback.buildCondition(locator, query);

		if (null != locator && locator.getAnchor() != null) {
			query.addConditions(Tables.EH_ENERGY_DATE_STATISTICS.ID.gt(locator.getAnchor()));
		}

		query.addLimit(count);
		List<EnergyDateStatistic> objs = query.fetch().map((r) -> {
			return ConvertHelper.convert(r, EnergyDateStatistic.class);
		});

		return objs;
	}

	private void prepareObj(EnergyDateStatistic obj) {
	}

	@Override
	public void deleteEnergyDateStatisticByDate(Long meterId, Date date) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		context.delete(Tables.EH_ENERGY_DATE_STATISTICS).where(Tables.EH_ENERGY_DATE_STATISTICS.STAT_DATE.eq(date))
				.and(Tables.EH_ENERGY_DATE_STATISTICS.METER_ID.eq(meterId)).execute();
	}

	@Override
	public EnergyDateStatistic getEnergyDateStatisticByStatDate(Long meterId,Date statDate) {
		try {
			EnergyDateStatistic[] result = new EnergyDateStatistic[1];
			DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

			result[0] = context.select().from(Tables.EH_ENERGY_DATE_STATISTICS).where(Tables.EH_ENERGY_DATE_STATISTICS.STAT_DATE.eq(statDate))
					.and(Tables.EH_ENERGY_DATE_STATISTICS.METER_ID.eq(meterId))
					.orderBy(Tables.EH_ENERGY_DATE_STATISTICS.CREATE_TIME.desc())
					.fetchAny().map((r) -> {
						return ConvertHelper.convert(r, EnergyDateStatistic.class);
					});

			return result[0];
		} catch (Exception ex) {
			// fetchAny() maybe return null
			return null;
		}
	}

	@Override
	public List<BigDecimal> getBlockSumAmountBetweenDate(Long meterId, Date begin, Date end) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

		List<BigDecimal> result  = context.select(Tables.EH_ENERGY_DATE_STATISTICS.CURRENT_AMOUNT.sum()).from(Tables.EH_ENERGY_DATE_STATISTICS)
				.where(Tables.EH_ENERGY_DATE_STATISTICS.STAT_DATE.between(begin, end))
				.and(Tables.EH_ENERGY_DATE_STATISTICS.METER_ID.eq(meterId))
				.and(Tables.EH_ENERGY_DATE_STATISTICS.CALCULATION_TYPE.eq(PriceCalculationType.BLOCK_TARIFF.getCode()))
				.groupBy(Tables.EH_ENERGY_DATE_STATISTICS.CONFIG_ID)
				.orderBy(Tables.EH_ENERGY_DATE_STATISTICS.CREATE_TIME.desc())
				.fetchInto(BigDecimal.class);
//				.fetchOne().value1() ;
		return result;
	}

	@Override
	public BigDecimal getSumAmountBetweenDate(Long meterId,Date begin, Date end) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

		BigDecimal result  = context.select(Tables.EH_ENERGY_DATE_STATISTICS.CURRENT_AMOUNT.sum()).from(Tables.EH_ENERGY_DATE_STATISTICS)
				.where(Tables.EH_ENERGY_DATE_STATISTICS.STAT_DATE.between(begin, end))
				.and(Tables.EH_ENERGY_DATE_STATISTICS.METER_ID.eq(meterId)) 
				.orderBy(Tables.EH_ENERGY_DATE_STATISTICS.CREATE_TIME.desc())
				.fetchOne().value1() ;
		return result;
		
	}

	@Override
	public BigDecimal getSumCostBetweenDate(Long meterId ,Date begin, Date end) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

		BigDecimal result  = context.select(Tables.EH_ENERGY_DATE_STATISTICS.CURRENT_COST.sum()).from(Tables.EH_ENERGY_DATE_STATISTICS)
				.where(Tables.EH_ENERGY_DATE_STATISTICS.STAT_DATE.between(begin, end))
				.and(Tables.EH_ENERGY_DATE_STATISTICS.METER_ID.eq(meterId))
				.and(Tables.EH_ENERGY_DATE_STATISTICS.CALCULATION_TYPE.eq(PriceCalculationType.STANDING_CHARGE_TARIFF.getCode()))
				.orderBy(Tables.EH_ENERGY_DATE_STATISTICS.CREATE_TIME.desc())
				.fetchOne().value1() ;
		return result;
	}

	@Override
	public List<EnergyDateStatistic> listEnergyDateStatistics(Byte meterType, Long communityId, List<Long> billCategoryIds,
			List<Long> serviceCategoryIds, Date startDate, Date endDate) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_ENERGY_DATE_STATISTICS);
		Condition condition = Tables.EH_ENERGY_DATE_STATISTICS.METER_TYPE.eq(meterType);
		if (null != communityId)
			condition = condition.and(Tables.EH_ENERGY_DATE_STATISTICS.COMMUNITY_ID.eq(communityId)); 
		if (null != billCategoryIds)
			condition = condition.and(Tables.EH_ENERGY_DATE_STATISTICS.BILL_CATEGORY_ID.in(billCategoryIds)); 
		if (null != serviceCategoryIds)
			condition = condition.and(Tables.EH_ENERGY_DATE_STATISTICS.SERVICE_CATEGORY_ID.in(serviceCategoryIds)); 
		if (null != startDate)
			condition = condition.and(Tables.EH_ENERGY_DATE_STATISTICS.STAT_DATE.greaterOrEqual(startDate) ); 
		if (null != endDate)
			condition = condition.and(Tables.EH_ENERGY_DATE_STATISTICS.STAT_DATE.lessOrEqual(endDate)); 
		step.where(condition);
		List<EnergyDateStatistic> result = step.orderBy(Tables.EH_ENERGY_DATE_STATISTICS.STAT_DATE.asc()).fetch().map((r) -> {
			return ConvertHelper.convert(r, EnergyDateStatistic.class);
		});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}

    @Override
    public EnergyDateStatistic findByMeterAndDate(Integer namespaceId, Long meterId, Date date) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_ENERGY_DATE_STATISTICS)
                // .where(Tables.EH_ENERGY_DATE_STATISTICS.NAMESPACE_ID.eq(namespaceId))
                .where(Tables.EH_ENERGY_DATE_STATISTICS.METER_ID.eq(meterId))
                .and(Tables.EH_ENERGY_DATE_STATISTICS.STAT_DATE.eq(date))
                .fetchAnyInto(EnergyDateStatistic.class);
    }
}
