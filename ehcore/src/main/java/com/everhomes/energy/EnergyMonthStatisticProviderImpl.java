package com.everhomes.energy;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.energy.EnergyStatisticType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnergyMonthStatisticsDao;
import com.everhomes.server.schema.tables.pojos.EhEnergyMonthStatistics;
import com.everhomes.server.schema.tables.records.EhEnergyMonthStatisticsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class EnergyMonthStatisticProviderImpl implements EnergyMonthStatisticProvider {
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private ShardingProvider shardingProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public Long createEnergyMonthStatistic(EnergyMonthStatistic obj) {
		String key = NameMapper.getSequenceDomainFromTablePojo(EhEnergyMonthStatistics.class);
		long id = sequenceProvider.getNextSequence(key);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		obj.setId(id);
		prepareObj(obj);
		EhEnergyMonthStatisticsDao dao = new EhEnergyMonthStatisticsDao(context.configuration());
		dao.insert(obj);
		return id;
	}

	@Override
	public void updateEnergyMonthStatistic(EnergyMonthStatistic obj) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhEnergyMonthStatisticsDao dao = new EhEnergyMonthStatisticsDao(context.configuration());
		dao.update(obj);
	}

	@Override
	public void deleteEnergyMonthStatistic(EnergyMonthStatistic obj) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhEnergyMonthStatisticsDao dao = new EhEnergyMonthStatisticsDao(context.configuration());
		dao.deleteById(obj.getId());
	}

	@Override
	public EnergyMonthStatistic getEnergyMonthStatisticById(Long id) {
		try {
			EnergyMonthStatistic[] result = new EnergyMonthStatistic[1];
			DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

			result[0] = context.select().from(Tables.EH_ENERGY_MONTH_STATISTICS).where(Tables.EH_ENERGY_MONTH_STATISTICS.ID.eq(id))
					.fetchAny().map((r) -> {
						return ConvertHelper.convert(r, EnergyMonthStatistic.class);
					});

			return result[0];
		} catch (Exception ex) {
			// fetchAny() maybe return null
			return null;
		}
	}

	@Override
	public List<EnergyMonthStatistic> queryEnergyMonthStatistics(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

		SelectQuery<EhEnergyMonthStatisticsRecord> query = context.selectQuery(Tables.EH_ENERGY_MONTH_STATISTICS);
		if (queryBuilderCallback != null)
			queryBuilderCallback.buildCondition(locator, query);

		if (null != locator && locator.getAnchor() != null) {
			query.addConditions(Tables.EH_ENERGY_MONTH_STATISTICS.ID.gt(locator.getAnchor()));
		}

		query.addLimit(count);
		List<EnergyMonthStatistic> objs = query.fetch().map((r) -> {
			return ConvertHelper.convert(r, EnergyMonthStatistic.class);
		});

		return objs;
	}

	private void prepareObj(EnergyMonthStatistic obj) {
	}

	@Override
	public void deleteEnergyMonthStatisticByDate(Long meterId, String monthStr) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		context.delete(Tables.EH_ENERGY_MONTH_STATISTICS).where(Tables.EH_ENERGY_MONTH_STATISTICS.DATE_STR.eq(monthStr))
				.and(Tables.EH_ENERGY_MONTH_STATISTICS.METER_ID.eq(meterId)).execute();
	}

	@Override
	public List<EnergyCountStatistic> listEnergyCountStatistic(String monthStr) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		List<EnergyCountStatistic> result = new ArrayList<EnergyCountStatistic>();
		SelectHavingStep<Record8<Integer, Long, Byte, String, Long, Long, BigDecimal, BigDecimal>> step = context
				.select(Tables.EH_ENERGY_MONTH_STATISTICS.NAMESPACE_ID, Tables.EH_ENERGY_MONTH_STATISTICS.COMMUNITY_ID,
						Tables.EH_ENERGY_MONTH_STATISTICS.METER_TYPE, Tables.EH_ENERGY_MONTH_STATISTICS.DATE_STR,
						Tables.EH_ENERGY_MONTH_STATISTICS.BILL_CATEGORY_ID, Tables.EH_ENERGY_MONTH_STATISTICS.SERVICE_CATEGORY_ID,
						Tables.EH_ENERGY_MONTH_STATISTICS.CURRENT_AMOUNT.sum(), Tables.EH_ENERGY_MONTH_STATISTICS.CURRENT_COST.sum())
				.from(Tables.EH_ENERGY_MONTH_STATISTICS)
				.where(Tables.EH_ENERGY_MONTH_STATISTICS.DATE_STR.eq(monthStr))
				.groupBy(Tables.EH_ENERGY_MONTH_STATISTICS.NAMESPACE_ID, Tables.EH_ENERGY_MONTH_STATISTICS.COMMUNITY_ID,
						Tables.EH_ENERGY_MONTH_STATISTICS.METER_TYPE, Tables.EH_ENERGY_MONTH_STATISTICS.DATE_STR,
						Tables.EH_ENERGY_MONTH_STATISTICS.BILL_CATEGORY_ID, Tables.EH_ENERGY_MONTH_STATISTICS.SERVICE_CATEGORY_ID);
		step.fetch().map((r) -> {
			EnergyCountStatistic e = new EnergyCountStatistic();
			e.setNamespaceId((Integer) r.getValue(0));
			e.setCommunityId((Long) r.getValue(1));
			e.setMeterType((Byte) r.getValue(2));
			e.setDateStr((String) r.getValue(3));
			e.setBillCategoryId((Long) r.getValue(4));
			e.setServiceCategoryId((Long) r.getValue(5));
			e.setAmount((BigDecimal) r.getValue(6));
			e.setCost((BigDecimal) r.getValue(7));
			e.setStatisticType(EnergyStatisticType.SERVICE.getCode());
			result.add(e);
			return null;
		});
		SelectHavingStep<Record7<Integer, Long, Byte, String, Long, BigDecimal, BigDecimal>> step2 = context
				.select(Tables.EH_ENERGY_MONTH_STATISTICS.NAMESPACE_ID, Tables.EH_ENERGY_MONTH_STATISTICS.COMMUNITY_ID,
						Tables.EH_ENERGY_MONTH_STATISTICS.METER_TYPE, Tables.EH_ENERGY_MONTH_STATISTICS.DATE_STR,
						Tables.EH_ENERGY_MONTH_STATISTICS.BILL_CATEGORY_ID,
						Tables.EH_ENERGY_MONTH_STATISTICS.CURRENT_AMOUNT.sum(), Tables.EH_ENERGY_MONTH_STATISTICS.CURRENT_COST.sum())
				.from(Tables.EH_ENERGY_MONTH_STATISTICS)
				.where(Tables.EH_ENERGY_MONTH_STATISTICS.DATE_STR.eq(monthStr))
				.groupBy(Tables.EH_ENERGY_MONTH_STATISTICS.NAMESPACE_ID, Tables.EH_ENERGY_MONTH_STATISTICS.COMMUNITY_ID,
						Tables.EH_ENERGY_MONTH_STATISTICS.METER_TYPE, Tables.EH_ENERGY_MONTH_STATISTICS.DATE_STR,
						Tables.EH_ENERGY_MONTH_STATISTICS.BILL_CATEGORY_ID);
		step2.fetch().map((r) -> {
			EnergyCountStatistic e = new EnergyCountStatistic();
			e.setNamespaceId((Integer) r.getValue(0));
			e.setCommunityId((Long) r.getValue(1));
			e.setMeterType((Byte) r.getValue(2));
			e.setDateStr((String) r.getValue(3));
			e.setBillCategoryId((Long) r.getValue(4)); 
			e.setAmount((BigDecimal) r.getValue(5));
			e.setCost((BigDecimal) r.getValue(6));
			e.setStatisticType(EnergyStatisticType.BILL.getCode());
			result.add(e);
			return null;
		});
		if(result.size() == 0)
			return null;
		return result;
	}

	@Override
	public List<EnergyMonthStatistic> listEnergyMonthStatistics(Byte meterType, Long communityId, List<Long> billCategoryIds,
			List<Long> serviceCategoryIds, String yearStr) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_ENERGY_MONTH_STATISTICS);
		Condition condition = Tables.EH_ENERGY_MONTH_STATISTICS.METER_TYPE.eq(meterType);
		if (null != communityId)
			condition = condition.and(Tables.EH_ENERGY_MONTH_STATISTICS.COMMUNITY_ID.eq(communityId)); 
		if (null != billCategoryIds)
			condition = condition.and(Tables.EH_ENERGY_MONTH_STATISTICS.BILL_CATEGORY_ID.in(billCategoryIds)); 
		if (null != serviceCategoryIds)
			condition = condition.and(Tables.EH_ENERGY_MONTH_STATISTICS.SERVICE_CATEGORY_ID.in(serviceCategoryIds)); 
		if (null != yearStr){
			//取今年所有和去年12月
			condition = condition.and(Tables.EH_ENERGY_MONTH_STATISTICS.DATE_STR.like(yearStr+"%")
					.or(Tables.EH_ENERGY_MONTH_STATISTICS.DATE_STR.like((Integer.valueOf(yearStr)-1)+"12"))  );  
		}
		step.where(condition);
		List<EnergyMonthStatistic> result = step.orderBy(Tables.EH_ENERGY_MONTH_STATISTICS.DATE_STR.asc()).fetch().map((r) -> {
			return ConvertHelper.convert(r, EnergyMonthStatistic.class);
		});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}

    @Override
    public EnergyMonthStatistic findByMeterAndDate(Integer namespaceId, Long meterId, String dateStr) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_ENERGY_MONTH_STATISTICS)
                // .where(Tables.EH_ENERGY_DATE_STATISTICS.NAMESPACE_ID.eq(namespaceId))
                .where(Tables.EH_ENERGY_MONTH_STATISTICS.METER_ID.eq(meterId))
                .and(Tables.EH_ENERGY_MONTH_STATISTICS.DATE_STR.eq(dateStr))
                .fetchAnyInto(EnergyMonthStatistic.class);
    }
}