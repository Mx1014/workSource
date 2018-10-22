package com.everhomes.rentalv2;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;


import com.everhomes.rest.rentalv2.*;
import com.everhomes.rest.rentalv2.admin.ResourceTypeStatus;
import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.user.UserContext;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhRentalv2ResourceRanges;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RecordHelper;


@Component
public class Rentalv2ProviderImpl implements Rentalv2Provider {
    private static final Logger LOGGER = LoggerFactory.getLogger(Rentalv2ProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;
 

	@Override
	public void createRentalSite(RentalResource rentalsite) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2Resources.class));
		rentalsite.setId(id);
		rentalsite.setDefaultOrder(id);
		rentalsite.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		rentalsite.setCreatorUid(UserContext.currentUserId());

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2ResourcesRecord record = ConvertHelper.convert(rentalsite,
				EhRentalv2ResourcesRecord.class);
		InsertQuery<EhRentalv2ResourcesRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_RESOURCES);
		query.setRecord(record);
		query.execute();

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2Resources.class, null);
	}

	@Override
	public List<RentalItem> findRentalSiteItems(String sourceType,Long sourceId, String resourceType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ITEMS);
		Condition condition = Tables.EH_RENTALV2_ITEMS.SOURCE_ID
				.equal(sourceId);
		condition.and(Tables.EH_RENTALV2_ITEMS.SOURCE_TYPE.equal(sourceType));
		condition = condition.and(Tables.EH_RENTALV2_ITEMS.RESOURCE_TYPE.equal(resourceType));
		step.where(condition);
		List<RentalItem> result = step
				.orderBy(Tables.EH_RENTALV2_ITEMS.DEFAULT_ORDER.asc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalItem.class);
				});

		return result;

	} 
	@Override
	public void createRentalSiteItem(RentalItem siteItem) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2Items.class));
		siteItem.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2ItemsRecord record = ConvertHelper.convert(siteItem,
				EhRentalv2ItemsRecord.class);
		InsertQuery<EhRentalv2ItemsRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_ITEMS);
		query.setRecord(record);
		query.execute();

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2Items.class,
				null);

	}

	@Override
	public void createRentalSiteRule(RentalCell rsr) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2Cells.class));
		rsr.setCellId(rsr.getId());
		rsr.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2CellsRecord record = ConvertHelper.convert(rsr,
				EhRentalv2CellsRecord.class);
		InsertQuery<EhRentalv2CellsRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_CELLS);
		query.setRecord(record);
		query.execute();

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2Cells.class,
				null);

	}

	@Override
	public Long createRentalOrder(RentalOrder rentalBill) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2Orders.class));
		rentalBill.setId(id);
		rentalBill.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		rentalBill.setCreatorUid(UserContext.currentUserId());

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2OrdersRecord record = ConvertHelper.convert(rentalBill,
				EhRentalv2OrdersRecord.class);
		record.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		InsertQuery<EhRentalv2OrdersRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_ORDERS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2Orders.class, null);
		return id;
	}

	@Override
	public Long createRentalOrderStatistics(RentalOrderStatistics statistics) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2OrderStatistics.class));
		statistics.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2OrderStatisticsRecord record = ConvertHelper.convert(statistics,
			EhRentalv2OrderStatisticsRecord.class);
		InsertQuery<EhRentalv2OrderStatisticsRecord> query = context.insertQuery(Tables.EH_RENTALV2_ORDER_STATISTICS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE,EhRentalv2OrderStatistics.class,null);
		return id;
	}



	@Override
	public Long createRentalItemBill(RentalItemsOrder rib) {

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2ItemsOrders.class));
		rib.setId(id);
		rib.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		rib.setCreatorUid(UserContext.currentUserId());

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2ItemsOrdersRecord record = ConvertHelper.convert(rib,
				EhRentalv2ItemsOrdersRecord.class);
		InsertQuery<EhRentalv2ItemsOrdersRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_ITEMS_ORDERS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2ItemsOrders.class,
				null);
		return id;
	}

	@Override
	public Long createRentalSiteBill(RentalResourceOrder rsb) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2ResourceOrders.class));
		rsb.setId(id);
		rsb.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		rsb.setCreatorUid(UserContext.currentUserId());

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2ResourceOrdersRecord record = ConvertHelper.convert(rsb,
				EhRentalv2ResourceOrdersRecord.class);
		InsertQuery<EhRentalv2ResourceOrdersRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_RESOURCE_ORDERS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2ResourceOrders.class,
				null);
		return id;
	}

	@Override
	public List<RentalResourceOrder> findRentalSiteBillBySiteRuleId(Long siteRuleId, String resourceType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context
				.select()
				.from(Tables.EH_RENTALV2_RESOURCE_ORDERS)
				.join(Tables.EH_RENTALV2_ORDERS)
				.on(Tables.EH_RENTALV2_ORDERS.ID
						.eq(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_ORDER_ID));

		Condition condition = Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID
				.equal(siteRuleId);
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS
				.ne(SiteBillStatus.FAIL.getCode()));
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS
				.ne(SiteBillStatus.REFUNDED.getCode()));
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS
				.ne(SiteBillStatus.REFUNDING.getCode()));
		/*---start modify by sw----*/
		//修改以前线下订单只有一个状态
		//线下订单重新定义状态，产品定义在已支付节点之前，该资源状态是未预约，但是支付之后该资源就表示已预约
		//判断 待审批和待支付状态
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS
				.ne(SiteBillStatus.APPROVING.getCode()));
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS
				.ne(SiteBillStatus.PAYINGFINAL.getCode()));
		/*---end----*/
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS
				.ne(SiteBillStatus.INACTIVE.getCode()));
		step.where(condition);
		List<EhRentalv2ResourceOrdersRecord> resultRecord = step
				.orderBy(Tables.EH_RENTALV2_RESOURCE_ORDERS.ID.desc()).fetch()
				.map(new RentalResourceOrderRecordMapper());

		List<RentalResourceOrder> result = resultRecord.stream().map((r) -> {
			return ConvertHelper.convert(r, RentalResourceOrder.class);
		}).collect(Collectors.toList());

		return result;
	}
		
	@Override
	public Double countRentalSiteBillBySiteRuleId(Long cellId,RentalResource rentalResource,Byte rentalType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Record1<BigDecimal> result = context.select(DSL.sum(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_COUNT))
				.from(Tables.EH_RENTALV2_RESOURCE_ORDERS)
				.join(Tables.EH_RENTALV2_ORDERS)
				.on(Tables.EH_RENTALV2_ORDERS.ID.eq(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_ORDER_ID))
				.where(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID.equal(cellId))
				.and(Tables.EH_RENTALV2_ORDERS.STATUS.eq(SiteBillStatus.SUCCESS.getCode()).
						or(Tables.EH_RENTALV2_ORDERS.STATUS.eq(SiteBillStatus.IN_USING.getCode())))
				.and(Tables.EH_RENTALV2_ORDERS.RENTAL_RESOURCE_ID.eq(rentalResource.getId()))
				.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE.eq(rentalResource.getResourceType()))
				.and(Tables.EH_RENTALV2_ORDERS.RENTAL_TYPE.eq(rentalType))
				.fetchOne();

		return result == null ? 0D : result.getValue(DSL.sum(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_COUNT)) == null ? 0D: result.getValue(DSL.sum(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_COUNT)).doubleValue();
	}

	@Override
	public Double countRentalSiteBillOfAllScene(RentalResource rentalResource, RentalCell rentalCell, List<Rentalv2PriceRule> priceRules) {
		List<Byte> rentalTypes = priceRules.stream().map(Rentalv2PriceRule::getRentalType).collect(Collectors.toList());
		Field<BigDecimal> rentalCount = null;
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectConditionStep<?> step = context.select(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE, Tables.EH_RENTALV2_RESOURCE_ORDERS.AMORPM, 
				Tables.EH_RENTALV2_RESOURCE_ORDERS.BEGIN_TIME, Tables.EH_RENTALV2_RESOURCE_ORDERS.END_TIME,
				rentalCount = DSL.sum(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_COUNT).as("rental_count"))
			.from(Tables.EH_RENTALV2_RESOURCE_ORDERS)
			.join(Tables.EH_RENTALV2_ORDERS)
			.on(Tables.EH_RENTALV2_ORDERS.ID.eq(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_ORDER_ID))
			.where(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.FAIL.getCode()))
			.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.REFUNDED.getCode()))
			.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.REFUNDING.getCode()))
			.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.APPROVING.getCode()))
			.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.PAYINGFINAL.getCode()))
			.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.INACTIVE.getCode()))
                .and(Tables.EH_RENTALV2_ORDERS.RENTAL_RESOURCE_ID.eq(rentalResource.getId()))
                .and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE.eq(rentalResource.getResourceType()));
		if (!StringUtils.isBlank(rentalCell.getResourceNumber())){
			step.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_NUMBER.eq(rentalCell.getResourceNumber()));
		}
		if (RentalType.fromCode(rentalCell.getRentalType()) == RentalType.HOUR) {
			// 如果这个资源可以使用半天预约，要判断当前时间段在上午还是下午或者晚上
			Byte amorpm = calculateAmorpm(rentalResource, rentalCell);
			if ((rentalTypes.contains(RentalType.HALFDAY.getCode()) || rentalTypes.contains(RentalType.THREETIMEADAY.getCode())) &&
					amorpm != null) {
					step.and((Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(rentalCell.getRentalType())
									.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID.equal(rentalCell.getId())))
							.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(rentalCell.getResourceRentalDate())
									.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.DAY.getCode())))
							.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.AMORPM.eq(amorpm)
									.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(rentalCell.getResourceRentalDate())))
							.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.MONTH.getCode())
									.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(initToMonthFirstDay(rentalCell.getResourceRentalDate()))))
					        .or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.WEEK.getCode())
							        .and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(initToWeekFirstDay(rentalCell.getResourceRentalDate())))));

			}else {
				step.and((Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(rentalCell.getRentalType())
						.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID.equal(rentalCell.getId())))
						.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.DAY.getCode())
								.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(rentalCell.getResourceRentalDate())))
						.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.MONTH.getCode())
									.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(initToMonthFirstDay(rentalCell.getResourceRentalDate()))))
						.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.WEEK.getCode())
								.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(initToWeekFirstDay(rentalCell.getResourceRentalDate())))));
			}
		}else if (RentalType.fromCode(rentalCell.getRentalType()) == RentalType.HALFDAY || RentalType.fromCode(rentalCell.getRentalType()) == RentalType.THREETIMEADAY) {
			if (rentalTypes.contains(RentalType.HOUR.getCode())) {
				Timestamp[] beginEndTime = calculateHalfDayBeginEndTime(rentalResource, rentalCell);
				step.and((Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(rentalCell.getRentalType())
						.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID.equal(rentalCell.getId())))
						.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.DAY.getCode())
								.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(rentalCell.getResourceRentalDate())))
						.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.BEGIN_TIME.ge(beginEndTime[0]).and(Tables.EH_RENTALV2_RESOURCE_ORDERS.END_TIME.le(beginEndTime[1])))
						.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.MONTH.getCode())
									.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(initToMonthFirstDay(rentalCell.getResourceRentalDate()))))
						.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.WEEK.getCode())
								.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(initToWeekFirstDay(rentalCell.getResourceRentalDate())))));
			}else {
				step.and((Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(rentalCell.getRentalType())
						.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID.equal(rentalCell.getId())))
						.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.DAY.getCode())
								.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(rentalCell.getResourceRentalDate())))
						.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.MONTH.getCode())
									.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(initToMonthFirstDay(rentalCell.getResourceRentalDate()))))
						.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.WEEK.getCode())
								.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(initToWeekFirstDay(rentalCell.getResourceRentalDate())))));
			}
		}else if (RentalType.fromCode(rentalCell.getRentalType()) == RentalType.DAY) {
			step.and((Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(rentalCell.getRentalType())
					.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID.equal(rentalCell.getId())))
					.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(rentalCell.getResourceRentalDate())
							.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.in(Arrays.asList(RentalType.HOUR.getCode(), RentalType.HALFDAY.getCode(), RentalType.THREETIMEADAY.getCode()))))
					.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.MONTH.getCode())
							.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(initToMonthFirstDay(rentalCell.getResourceRentalDate()))))
					.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.WEEK.getCode())
							.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(initToWeekFirstDay(rentalCell.getResourceRentalDate())))));
		}else if (RentalType.fromCode(rentalCell.getRentalType()) == RentalType.MONTH) { //对于自然周跨月的处理 区间判断可以处理首尾跨界问题的其中一个 另一个用首日所属解决
			step.and((Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(rentalCell.getRentalType())
					.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID.equal(rentalCell.getId())))
					.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.ge(initToMonthFirstDay(rentalCell.getResourceRentalDate()))
							.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.le(initToMonthLastDay(rentalCell.getResourceRentalDate()))))
					.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.WEEK.getCode())
							.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(initToWeekFirstDay(rentalCell.getResourceRentalDate())))));
		}else if (RentalType.fromCode(rentalCell.getRentalType()) == RentalType.WEEK){
			step.and((Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(rentalCell.getRentalType())
					.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID.equal(rentalCell.getId())))
					.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.ge(initToWeekFirstDay(rentalCell.getResourceRentalDate()))
							.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.le(initToWeekLastDay(rentalCell.getResourceRentalDate()))))
					.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.MONTH.getCode())
							.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(initToMonthFirstDay(rentalCell.getResourceRentalDate())))));
		}
		
		Table<?> innerTable = step.groupBy(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE, Tables.EH_RENTALV2_RESOURCE_ORDERS.AMORPM, 
				Tables.EH_RENTALV2_RESOURCE_ORDERS.BEGIN_TIME, Tables.EH_RENTALV2_RESOURCE_ORDERS.END_TIME).asTable("inner_table");
		
		Field<BigDecimal> maxRentalCount = null;
		Table<?> middleTable = context.select(innerTable.field("rental_type"), maxRentalCount = DSL.max(rentalCount).as("max_rental_count")).from(innerTable).groupBy(innerTable.field("rental_type")).asTable("middle_table");
		
		SelectJoinStep<Record1<BigDecimal>> outer = context.select(DSL.sum(maxRentalCount)).from(middleTable);
		
//		if (LOGGER.isDebugEnabled()) {
//			LOGGER.debug(outer.getSQL());
//			LOGGER.debug(outer.getBindValues().toString());
//		}
		
		Record1<BigDecimal> record = outer.fetchOne();
		
		return record == null ? 0D : record.getValue(DSL.sum(maxRentalCount)) == null ? 0D : record.getValue(DSL.sum(maxRentalCount)).doubleValue();
	}

    @Override
    public List<RentalResourceOrder> findAllRentalSiteBillByTime(RentalResource rentalResource, Long beginTime, Long endTime) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<RentalResourceOrder> list = context.select(Tables.EH_RENTALV2_RESOURCE_ORDERS.fields())
				.from(Tables.EH_RENTALV2_RESOURCE_ORDERS)
				.join(Tables.EH_RENTALV2_ORDERS)
				.on(Tables.EH_RENTALV2_ORDERS.ID.eq(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_ORDER_ID))
				.and(Tables.EH_RENTALV2_ORDERS.RENTAL_RESOURCE_ID.eq(rentalResource.getId()))
				.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE.eq(rentalResource.getResourceType()))
				.where(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.FAIL.getCode()))
				.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.REFUNDED.getCode()))
				.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.REFUNDING.getCode()))
				.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.APPROVING.getCode()))
				.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.PAYINGFINAL.getCode()))
				.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.INACTIVE.getCode()))
				.fetch().map(r -> {
					RentalResourceOrder order = new RentalResourceOrder();
					order.setRentalType(r.getValue(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE));
					order.setBeginTime(r.getValue(Tables.EH_RENTALV2_RESOURCE_ORDERS.BEGIN_TIME));
					order.setEndTime(r.getValue(Tables.EH_RENTALV2_RESOURCE_ORDERS.END_TIME));
					order.setRentalCount(r.getValue(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_COUNT));
					order.setResourceNumber(r.getValue(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_NUMBER));
					return order;
				});
		return list;
    }

    @Override
	public boolean findOtherModeClosed(RentalResource rentalResource, RentalCell rentalCell, List<Rentalv2PriceRule> priceRules) {
		List<Byte> rentalTypes = priceRules.stream().map(Rentalv2PriceRule::getRentalType).collect(Collectors.toList());
		rentalTypes.remove(rentalCell.getRentalType());
		if (RentalType.fromCode(rentalCell.getRentalType()) == RentalType.HALFDAY) {
			rentalTypes.remove(new Byte(RentalType.THREETIMEADAY.getCode()));
		}
		if (RentalType.fromCode(rentalCell.getRentalType()) == RentalType.THREETIMEADAY) {
			rentalTypes.remove(new Byte(RentalType.HALFDAY.getCode()));
		}
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Record record = null;
		//此处如果用一条语句写效率比较低，反而不如像下面这样费事的写
		for (Byte rentalTypeByte : rentalTypes) {
			RentalType rentalType = RentalType.fromCode(rentalTypeByte);
			if (rentalType == RentalType.HOUR) {
				Timestamp[] beginEndTime = calculateBeginEndTime(rentalResource, rentalCell);
				record = getHourCloseRecord(context, rentalResource.getResourceType(),rentalResource.getId(), beginEndTime[0], beginEndTime[1], rentalCell.getResourceNumber());
			}else if (rentalType == RentalType.DAY) {
				Date[] beginEndDate = calculateBeginEndDate(rentalResource, rentalCell);
				record = getDayCloseRecord(context,rentalResource.getResourceType(), rentalResource.getId(), beginEndDate[0], beginEndDate[1], rentalCell.getResourceNumber());
			}else if (rentalType == RentalType.MONTH) { //需要处理月和周互相跨界的问题
				record = getMonthCloseRecord(context, rentalResource.getResourceType(),rentalResource.getId(), rentalCell.getResourceRentalDate(), rentalCell.getResourceNumber());
				if (record == null && rentalCell.getRentalType() == RentalType.WEEK.getCode()){
					Date[] beginEndDate = calculateBeginEndDate(rentalResource, rentalCell);
					record = getMonthCloseRecord(context,rentalResource.getResourceType(), rentalResource.getId(), beginEndDate[1], rentalCell.getResourceNumber());
				}
			}else if (rentalType == RentalType.WEEK) {
				record = getWeekCloseRecord(context,rentalResource.getResourceType(), rentalResource.getId(), rentalCell.getResourceRentalDate(), rentalCell.getResourceNumber());
				if (record == null && rentalCell.getRentalType() == RentalType.MONTH.getCode()){
					Date[] beginEndDate = calculateBeginEndDate(rentalResource, rentalCell);
					record = getWeekCloseRecord(context, rentalResource.getResourceType(),rentalResource.getId(), beginEndDate[1], rentalCell.getResourceNumber());
				}
			} else if (rentalType == RentalType.HALFDAY || rentalType == RentalType.THREETIMEADAY) {
				Byte amorpm = calculateAmorpm(rentalResource, rentalCell);
				Date[] beginEndDate = calculateBeginEndDate(rentalResource, rentalCell);
				record = getHalfDayCloseRecord(context,rentalResource.getResourceType(), rentalResource.getId(), beginEndDate[0], beginEndDate[1], rentalTypeByte, amorpm, RentalType.fromCode(rentalCell.getRentalType())==RentalType.HOUR, rentalCell.getResourceNumber());
			}
			if (record != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<RentalCell> findCellClosedByTimeInterval(String resourceType, Long rentalSiteId,Long startTime,Long endTime) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        List<RentalCell> map = context.select().from(Tables.EH_RENTALV2_CELLS)
                .where(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.eq(rentalSiteId))
                .and(Tables.EH_RENTALV2_CELLS.RESOURCE_TYPE.eq(resourceType))
                .and(Tables.EH_RENTALV2_CELLS.STATUS.eq((byte) -1))
                .and((Tables.EH_RENTALV2_CELLS.BEGIN_TIME.le(new Timestamp(startTime)).and(Tables.EH_RENTALV2_CELLS.END_TIME.gt(new Timestamp(startTime))))
                        .or(Tables.EH_RENTALV2_CELLS.BEGIN_TIME.gt(new Timestamp(startTime)).and(Tables.EH_RENTALV2_CELLS.BEGIN_TIME.lt(new Timestamp(endTime)))))
                .fetch().map(r -> {
                    RentalCell convert = ConvertHelper.convert(r, RentalCell.class);
                    Long tmp = convert.getId();
                    convert.setId(convert.getCellId());
                    convert.setCellId(tmp);
                    return convert;
                });
        return map;
	}

	private Date[] calculateBeginEndDate(RentalResource rentalResource, RentalCell rentalCell) {
		if (RentalType.fromCode(rentalCell.getRentalType()) == RentalType.MONTH) {
			return new Date[] {initToMonthFirstDay(rentalCell.getResourceRentalDate()), initToMonthLastDay(rentalCell.getResourceRentalDate())};
		}else if (RentalType.fromCode(rentalCell.getRentalType()) == RentalType.WEEK){
			return new Date[] {initToWeekFirstDay(rentalCell.getResourceRentalDate()), initToWeekLastDay(rentalCell.getResourceRentalDate())};
		}
		return new Date[] {rentalCell.getResourceRentalDate(), rentalCell.getResourceRentalDate()};
	}

	private Timestamp[] calculateBeginEndTime(RentalResource rentalResource, RentalCell rentalCell) {
		RentalType rentalType = RentalType.fromCode(rentalCell.getRentalType());
		if (rentalType == RentalType.HALFDAY || rentalType == RentalType.THREETIMEADAY) {
			return calculateHalfDayBeginEndTime(rentalResource, rentalCell);
		}else if (rentalType == RentalType.DAY) {
			Long currentTime = rentalCell.getResourceRentalDate().getTime();
			return new Timestamp[]{new Timestamp(currentTime), new Timestamp(currentTime + 24*60*60*1000 - 1000)};
		}else if (rentalType == RentalType.MONTH) {
			Long start = initToMonthFirstDay(rentalCell.getResourceRentalDate()).getTime();
			Long end = initToMonthLastDay(rentalCell.getResourceRentalDate()).getTime() + 24*60*60*1000 - 1000;
			return new Timestamp[]{new Timestamp(start), new Timestamp(end)};
		}else if (rentalType == RentalType.WEEK){
			Long start = initToWeekFirstDay(rentalCell.getResourceRentalDate()).getTime();
			Long end = initToWeekLastDay(rentalCell.getResourceRentalDate()).getTime() + 24*60*60*1000 - 1000;
			return new Timestamp[]{new Timestamp(start), new Timestamp(end)};
		}
		return new Timestamp[]{new Timestamp(rentalCell.getResourceRentalDate().getTime()), new Timestamp(rentalCell.getResourceRentalDate().getTime())};
	}

	private Record getHourCloseRecord(DSLContext context,String resourceType, Long resourceId, Timestamp begin, Timestamp end, String resourceNumber) {
		return context.select().from(Tables.EH_RENTALV2_CELLS)
				.where(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.eq(resourceId))
				.and(Tables.EH_RENTALV2_CELLS.RESOURCE_TYPE.eq(resourceType))
				.and(Tables.EH_RENTALV2_CELLS.RENTAL_TYPE.eq(RentalType.HOUR.getCode()))
				.and(resourceNumber == null?DSL.trueCondition():Tables.EH_RENTALV2_CELLS.RESOURCE_NUMBER.eq(resourceNumber))
				.and(Tables.EH_RENTALV2_CELLS.BEGIN_TIME.ge(begin))
				.and(Tables.EH_RENTALV2_CELLS.END_TIME.le(end))
				.and(Tables.EH_RENTALV2_CELLS.STATUS.eq((byte) -1))
				.fetchAny();
	}

	private Record getHalfDayCloseRecord(DSLContext context,String resourceType, Long resourceId, Date begin, Date end, Byte rentalType, Byte amorpm, boolean isHour, String resourceNumber) {
		if (isHour && amorpm == null) {
			return null;
		}
		return context.select().from(Tables.EH_RENTALV2_CELLS)
				.where(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.eq(resourceId))
				.and(Tables.EH_RENTALV2_CELLS.RESOURCE_TYPE.eq(resourceType))
				.and(Tables.EH_RENTALV2_CELLS.RENTAL_TYPE.eq(rentalType))
				.and(resourceNumber == null?DSL.trueCondition():Tables.EH_RENTALV2_CELLS.RESOURCE_NUMBER.eq(resourceNumber))
				.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE.ge(begin))
				.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE.le(end))
				.and(amorpm == null?DSL.trueCondition():Tables.EH_RENTALV2_CELLS.AMORPM.eq(amorpm))
				.and(Tables.EH_RENTALV2_CELLS.STATUS.eq((byte) -1))
				.fetchAny();
	}

	private Record getMonthCloseRecord(DSLContext context,String resourceType, Long resourceId, Date resourceRentalDate, String resourceNumber) {
		return context.select().from(Tables.EH_RENTALV2_CELLS)
				.where(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.eq(resourceId))
				.and(Tables.EH_RENTALV2_CELLS.RESOURCE_TYPE.eq(resourceType))
				.and(Tables.EH_RENTALV2_CELLS.RENTAL_TYPE.eq(RentalType.MONTH.getCode()))
				.and(resourceNumber == null?DSL.trueCondition():Tables.EH_RENTALV2_CELLS.RESOURCE_NUMBER.eq(resourceNumber))
				.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE.eq(initToMonthFirstDay(resourceRentalDate)))
				.and(Tables.EH_RENTALV2_CELLS.STATUS.eq((byte) -1))
				.fetchAny();
	}

	private Record getWeekCloseRecord(DSLContext context,String resourceType, Long resourceId, Date resourceRentalDate, String resourceNumber){
		return context.select().from(Tables.EH_RENTALV2_CELLS)
				.where(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.eq(resourceId))
				.and(Tables.EH_RENTALV2_CELLS.RESOURCE_TYPE.eq(resourceType))
				.and(Tables.EH_RENTALV2_CELLS.RENTAL_TYPE.eq(RentalType.WEEK.getCode()))
				.and(resourceNumber == null?DSL.trueCondition():Tables.EH_RENTALV2_CELLS.RESOURCE_NUMBER.eq(resourceNumber))
				.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE.eq(initToWeekFirstDay(resourceRentalDate)))
				.and(Tables.EH_RENTALV2_CELLS.STATUS.eq((byte) -1))
				.fetchAny();
	}

	private Record getDayCloseRecord(DSLContext context,String resourceType, Long resourceId, Date begin, Date end, String resourceNumber) {
		return context.select().from(Tables.EH_RENTALV2_CELLS)
				.where(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.eq(resourceId))
				.and(Tables.EH_RENTALV2_CELLS.RESOURCE_TYPE.eq(resourceType))
				.and(Tables.EH_RENTALV2_CELLS.RENTAL_TYPE.eq(RentalType.DAY.getCode()))
				.and(resourceNumber == null?DSL.trueCondition():Tables.EH_RENTALV2_CELLS.RESOURCE_NUMBER.eq(resourceNumber))
				.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE.ge(begin))
				.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE.le(end))
				.and(Tables.EH_RENTALV2_CELLS.STATUS.eq((byte) -1))
				.fetchAny();
	}

	private Date initToMonthLastDay(Date date) {
		Calendar temp = Calendar.getInstance();
		temp.setTime(date);
		temp.set(Calendar.DAY_OF_MONTH, temp.getActualMaximum(Calendar.DAY_OF_MONTH));
		temp.set(Calendar.HOUR_OF_DAY, 0);
		temp.set(Calendar.MINUTE, 0);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MILLISECOND, 0);
		return new Date(temp.getTimeInMillis());
	}

	private Date initToMonthFirstDay(Date date) {
		Calendar temp = Calendar.getInstance();
		temp.setTime(date);
		temp.set(Calendar.DAY_OF_MONTH, 1);
		temp.set(Calendar.HOUR_OF_DAY, 0);
		temp.set(Calendar.MINUTE, 0);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MILLISECOND, 0);
		return new Date(temp.getTimeInMillis());
	}

	private Date initToWeekFirstDay(Date date){
		Calendar temp = Calendar.getInstance();
		temp.setTime(date);
		if (temp.get(Calendar.DAY_OF_WEEK)==1)//默认周日是一周第一天
			temp.add(Calendar.DATE,-1);
		temp.set(Calendar.DAY_OF_WEEK,2);
		temp.set(Calendar.HOUR_OF_DAY, 0);
		temp.set(Calendar.MINUTE, 0);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MILLISECOND, 0);
		return new Date(temp.getTimeInMillis());

	}

	private Date initToWeekLastDay(Date date){
		Calendar temp = Calendar.getInstance();
		temp.setTime(date);
		if (temp.get(Calendar.DAY_OF_WEEK)==1)//默认周日是一周第一天
			temp.add(Calendar.DATE,-1);
		temp.set(Calendar.DAY_OF_WEEK,2);
		temp.set(Calendar.HOUR_OF_DAY, 0);
		temp.set(Calendar.MINUTE, 0);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MILLISECOND, 0);
		temp.add(Calendar.DATE,6);
		return new Date(temp.getTimeInMillis());

	}

	private Timestamp[] calculateHalfDayBeginEndTime(RentalResource rentalResource, RentalCell rentalCell) {
		List<RentalTimeInterval> rentalTimeIntervals = queryRentalTimeIntervalByOwner(rentalResource.getResourceType(),
				RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode(), rentalResource.getId());
		if (rentalCell.getAmorpm() != null && rentalTimeIntervals.size() > rentalCell.getAmorpm().intValue()) {
			RentalTimeInterval rentalTimeInterval = rentalTimeIntervals.get(rentalCell.getAmorpm().intValue());
			Timestamp beginTime = format(rentalCell.getResourceRentalDate(), rentalTimeInterval.getBeginTime());
			Timestamp endTime = format(rentalCell.getResourceRentalDate(), rentalTimeInterval.getEndTime());
			return new Timestamp[]{beginTime, endTime};
		}
		
		return new Timestamp[]{new Timestamp(rentalCell.getResourceRentalDate().getTime()), new Timestamp(rentalCell.getResourceRentalDate().getTime())};
	}

	private Timestamp format(Date date, Double time) {
		Timestamp result = new Timestamp(date.getTime() + (long)(time*60*60*1000));
//		result.setHours(time.intValue());
//		result.setMinutes((int) ((time.doubleValue() - time.intValue()) * 60));
		return result;
	}

	@SuppressWarnings("deprecation")
	private Byte calculateAmorpm(RentalResource rentalResource, RentalCell rentalCell) {
		if (rentalCell.getBeginTime() == null || rentalCell.getEndTime() == null) {
			return null;
		}
		List<RentalTimeInterval> halfTimeIntervals = queryRentalTimeIntervalByOwner(rentalResource.getResourceType(),
				RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode(), rentalResource.getId());
		if (halfTimeIntervals == null || halfTimeIntervals.isEmpty()) {
			return null;
		}
		
		for (int i = 0; i < halfTimeIntervals.size(); i++) {
			RentalTimeInterval rentalTimeInterval = halfTimeIntervals.get(i);
			if (rentalTimeInterval.getBeginTime() <= (rentalCell.getBeginTime().getHours()+rentalCell.getBeginTime().getMinutes()/60.0) 
					&& rentalTimeInterval.getEndTime() >= (rentalCell.getEndTime().getHours()+rentalCell.getEndTime().getMinutes()/60.0)) {
				// 麻蛋的，这个表里没有标识是上午下午还是晚上，只能根据这个i来返回了，0上午1下午2晚上
				return (byte) i;
			}
		}
		// TODO,这里有个问题，不支持一个时间段跨越了上下午的
		return null;
	}

	@Override
	public List<RentalOrder> findRentalSiteBillBySiteRuleIds(List<Long> siteRuleIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<Long> orderIds = new ArrayList<Long>();
		SelectConditionStep<Record1<Long>> step1 = context
				.selectDistinct(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_ORDER_ID)
				.from(Tables.EH_RENTALV2_RESOURCE_ORDERS ).where(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID
				.in(siteRuleIds));
		step1.fetch().map((r) -> {
			orderIds.add(r.value1());
			return null;
		});
		SelectJoinStep<Record> step = context
				.select()
				.from(Tables.EH_RENTALV2_ORDERS )   ;

		Condition condition = Tables.EH_RENTALV2_ORDERS.ID.in(orderIds);
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.FAIL.getCode()));
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.REFUNDED.getCode()));
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.REFUNDING.getCode()));
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.INACTIVE.getCode()));

		step.where(condition);
//		List<EhRentalv2ResourceOrdersRecord> resultRecord = step
//				.orderBy(Tables.EH_RENTALV2_RESOURCE_ORDERS.ID.desc()).fetch()
//				.map(new RentalResourceOrderRecordMapper());
//
//		List<RentalOrder> result = resultRecord.stream().map((r) -> {
//			return ConvertHelper.convert(r, RentalOrder.class);
//		}).collect(Collectors.toList());
		List<RentalOrder> result = step
				.orderBy(Tables.EH_RENTALV2_ORDERS.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalOrder.class);
					});
		return result;
	}

	@Override
	public List<RentalOrder> listRentalBills(Long id, Long userId,Long rentalSiteId, String resourceType, Long resourceTypeId,
			ListingLocator locator, int count, List<Byte> status, Byte payMode) {
		final List<RentalOrder> result = new ArrayList<>();
		Condition condition = Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.INACTIVE.getCode());

		if (null != locator.getAnchor()) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.ID.lt(locator.getAnchor()));
		}

		if(null != resourceTypeId) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE_ID.eq(resourceTypeId));
		}
		if (StringUtils.isNotBlank(resourceType)) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE.eq(resourceType));
		}
		if (null != rentalSiteId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_RESOURCE_ID.eq(rentalSiteId));
		if (null != id)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.ID.eq(id));
		if (null != payMode) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.PAY_MODE.eq(payMode));
		}
		if (null != userId) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_UID.eq(userId));
		}
		if (null != status) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS.in(status));
		}
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.VISIBLE_FLAG.eq(VisibleFlag.VISIBLE.getCode()));
		final Condition condition2 = condition;
		this.dbProvider.mapReduce(
				AccessSpec.readOnlyWith(EhCommunities.class),
				null,
				(DSLContext context, Object reducingContext) -> {
					context.select()
							.from(Tables.EH_RENTALV2_ORDERS)
							.where(condition2)
							.orderBy(Tables.EH_RENTALV2_ORDERS.ID.desc())
							.limit(count)
							.fetch()
							.map((r) -> {
								result.add(ConvertHelper.convert(r,
										RentalOrder.class));
								return null;
							});

					return true;
				});

		return result;
	}

	@Override
	public RentalResource getRentalSiteById(Long rentalSiteId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_RESOURCES);
		Condition condition = Tables.EH_RENTALV2_RESOURCES.ID.equal(rentalSiteId);
		step.where(condition);
		List<RentalResource> result = step
				.orderBy(Tables.EH_RENTALV2_RESOURCES.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalResource.class);
				});
		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public List<RentalItemsOrder> findRentalItemsBillBySiteBillId(Long rentalBillId, String resourceType) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ITEMS_ORDERS);
		Condition condition = Tables.EH_RENTALV2_ITEMS_ORDERS.RENTAL_ORDER_ID
				.equal(rentalBillId);

		condition = condition.and(Tables.EH_RENTALV2_ITEMS_ORDERS.RESOURCE_TYPE.equal(resourceType));

		step.where(condition);
		List<RentalItemsOrder> result = step
				.orderBy(Tables.EH_RENTALV2_ITEMS_ORDERS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalItemsOrder.class);
				});

		if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query rental item bills by site bill id, sql=" + step.getSQL());
            LOGGER.debug("Query rental item bills by site bill id, bindValues=" + step.getBindValues());
        }
		if (null != result && result.size() > 0)
			return result;
		return null;
	}

	@Override
	public RentalItem findRentalSiteItemById(Long rentalSiteItemId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ITEMS);
		Condition condition = Tables.EH_RENTALV2_ITEMS.ID
				.equal(rentalSiteItemId);
		step.where(condition);
		List<RentalItem> result = step
				.orderBy(Tables.EH_RENTALV2_ITEMS.DEFAULT_ORDER.asc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalItem.class);
				});
		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public RentalItemsOrder findRentalItemBill(Long rentalBillId,
			Long rentalSiteItemId, String resourceType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ITEMS_ORDERS);
		Condition condition = Tables.EH_RENTALV2_ITEMS_ORDERS.RENTAL_ORDER_ID
				.equal(rentalBillId);
		condition = condition
				.and(Tables.EH_RENTALV2_ITEMS_ORDERS.RENTAL_RESOURCE_ITEM_ID
						.equal(rentalSiteItemId));

		condition = condition
				.and(Tables.EH_RENTALV2_ITEMS_ORDERS.RESOURCE_TYPE
						.equal(resourceType));

		step.where(condition);
		List<RentalItemsOrder> result = step
				.orderBy(Tables.EH_RENTALV2_ITEMS_ORDERS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalItemsOrder.class);
				});

		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public RentalOrder findRentalBillById(Long rentalBillId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ORDERS);
		Condition condition = Tables.EH_RENTALV2_ORDERS.ID.equal(rentalBillId);
		step.where(condition);
		List<RentalOrder> result = step
				.orderBy(Tables.EH_RENTALV2_ORDERS.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalOrder.class);
				});

		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public RentalOrder findRentalBillByOrderNo(String orderNo) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ORDERS);
		Condition condition = Tables.EH_RENTALV2_ORDERS.ORDER_NO.equal(orderNo);
		step.where(condition);
		List<RentalOrder> result = step.fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalOrder.class);
				});

		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public void updateRentalSite(RentalResource rentalsite) {
		assert (rentalsite.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2ResourcesDao dao = new EhRentalv2ResourcesDao(context.configuration());
		dao.update(rentalsite);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRentalv2Resources.class,
				rentalsite.getId());
	}

	@Override
	public void deleteRentalSiteItemById(Long rentalSiteItemId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2ItemsRecord> step = context
				.delete(Tables.EH_RENTALV2_ITEMS);
		Condition condition = Tables.EH_RENTALV2_ITEMS.ID
				.equal(rentalSiteItemId);
		step.where(condition);
		step.execute();
	}

	@Override
	public void deleteRentalOrderStatisticsByOrderId(Long orderId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2OrderStatisticsRecord> step = context.delete(Tables.EH_RENTALV2_ORDER_STATISTICS);
		Condition condition = Tables.EH_RENTALV2_ORDER_STATISTICS.ORDER_ID.eq(orderId);
		step.where(condition);
		step.execute();
	}

	@Override
	public List<RentalOrder> listRentalBills(Long resourceTypeId, Long organizationId,Long communityId, Long rentalSiteId,
											 ListingLocator locator, Byte billStatus, String vendorType , Integer pageSize,
											 Long startTime, Long endTime, Byte invoiceFlag,Long userId){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_RENTALV2_ORDERS);


		Condition condition = Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.INACTIVE.getCode());
		if (null != organizationId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.ORGANIZATION_ID.equal( organizationId));
		if (null != communityId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.COMMUNITY_ID.equal( communityId));
		if (StringUtils.isNotEmpty(vendorType))
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.VENDOR_TYPE.equal(vendorType));
		if(null!=resourceTypeId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE_ID.equal(resourceTypeId));
		if (null != rentalSiteId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_RESOURCE_ID.equal(rentalSiteId));

		if (null != startTime) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.gt(new Timestamp(startTime)));
		}
		if (null != endTime) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.lt(new Timestamp(endTime)));
		}
		if (null != billStatus)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS.equal(billStatus));
		if (null != invoiceFlag) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.INVOICE_FLAG.equal(invoiceFlag));
		}
		if (null != userId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_UID.equal(userId));
		if(null!=locator && locator.getAnchor() != null)
			condition=condition.and(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.lt(new Timestamp(locator.getAnchor())));

		step.orderBy(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.desc());
		step.limit(pageSize);
		step.where(condition);
		List<RentalOrder> result = step
				.orderBy(Tables.EH_RENTALV2_ORDERS.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalOrder.class);
				});

		return result;
	}

	@Override
	public List<RentalOrder> listRentalBillsByUserOrgId(Long organizationId, ListingLocator locator, Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_RENTALV2_ORDERS);
		Condition condition = Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.INACTIVE.getCode());
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.USER_ENTERPRISE_ID.equal(organizationId));
		if(null!=locator && locator.getAnchor() != null)
			condition=condition.and(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.lt(new Timestamp(locator.getAnchor())));
		step.orderBy(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.desc());
		step.limit(pageSize);
		step.where(condition);
		List<RentalOrder> result = step
				.orderBy(Tables.EH_RENTALV2_ORDERS.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalOrder.class);
				});
		return result;
	}

	@Override
	public List<RentalOrder> listActiveBills(Long rentalSiteId, ListingLocator locator, Integer pageSize, Long startTime, Long endTime) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_RENTALV2_ORDERS);
		Condition condition = Tables.EH_RENTALV2_ORDERS.STATUS.in(SiteBillStatus.SUCCESS.getCode(),SiteBillStatus.REFUNDING.getCode(),
				SiteBillStatus.PAYINGFINAL.getCode(),SiteBillStatus.APPROVING.getCode(),SiteBillStatus.IN_USING.getCode());
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_RESOURCE_ID.equal(rentalSiteId));
		if (null != startTime) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.START_TIME.gt(new Timestamp(startTime)));
		}
		if (null != endTime) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.START_TIME.lt(new Timestamp(endTime)));
		}
		if(null!=locator && locator.getAnchor() != null)
			condition=condition.and(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.lt(new Timestamp(locator.getAnchor())));
		step.orderBy(Tables.EH_RENTALV2_ORDERS.START_TIME);
		step.limit(pageSize);
		step.where(condition);
		List<RentalOrder> result = step
				.orderBy(Tables.EH_RENTALV2_ORDERS.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalOrder.class);
				});

		return result;
	}

	@Override
	public List<RentalOrder> listActiveBillsByInterval(Long rentalSiteId, Long startTime, Long endTime) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_RENTALV2_ORDERS);
		Condition condition = Tables.EH_RENTALV2_ORDERS.STATUS.in(SiteBillStatus.SUCCESS.getCode(),
				SiteBillStatus.IN_USING.getCode());
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_RESOURCE_ID.equal(rentalSiteId));
		if (null != startTime && null != endTime) {
			condition = condition.and((Tables.EH_RENTALV2_ORDERS.START_TIME.le(new Timestamp(startTime)).and(Tables.EH_RENTALV2_ORDERS.END_TIME.gt(new Timestamp(startTime))))
					.or(Tables.EH_RENTALV2_ORDERS.START_TIME.gt(new Timestamp(startTime)).and(Tables.EH_RENTALV2_ORDERS.START_TIME.lt(new Timestamp(endTime)))));

		}
		step.where(condition);

		List<RentalOrder> result = step
				.orderBy(Tables.EH_RENTALV2_ORDERS.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalOrder.class);
				});

		return result;
	}

	@Override
	public List<RentalOrder> searchRentalOrders(Long resourceTypeId, String resourceType, Long rentalSiteId, Byte billStatus,
												Long startTime, Long endTime, String tag1, String tag2,String vendorType,String keyword, Long pageAnchor ,
												Integer pageSize){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_RENTALV2_ORDERS);

		Condition condition = Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.INACTIVE.getCode());

		if (StringUtils.isNotBlank(resourceType))
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE.equal(resourceType));
		if (StringUtils.isNotBlank(tag1))
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.STRING_TAG1.equal(tag1));
		if (StringUtils.isNotBlank(tag2))
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.STRING_TAG2.equal(tag2));
		if(null != resourceTypeId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE_ID.equal(resourceTypeId));
		if (null != rentalSiteId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_RESOURCE_ID.equal(rentalSiteId));
		if (null != startTime) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.START_TIME.gt(new Timestamp(startTime)));
		}
		if (null != endTime) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.END_TIME.lt(new Timestamp(endTime)));
		}
		if (null != billStatus)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS.equal(billStatus));
		if (null != vendorType)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.VENDOR_TYPE.equal(vendorType));
		if (null != keyword)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.CUSTOM_OBJECT.like("%"+keyword+"%").
					or(Tables.EH_RENTALV2_ORDERS.USER_NAME.eq(keyword)).or(Tables.EH_RENTALV2_ORDERS.USER_PHONE.eq(keyword)));

		if(null != pageAnchor && pageAnchor != 0)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.lt(new Timestamp(pageAnchor)));

		step.orderBy(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.desc());
		if (pageSize != null)
			step.limit(pageSize);
		step.where(condition);

		return step.fetch().map((r) -> ConvertHelper.convert(r, RentalOrder.class));
	}

	@Override
	public BigDecimal getRentalOrdersTotalAmount(Long resourceTypeId, String resourceType, Long rentalSiteId,
												 Byte billStatus, Long startTime, Long endTime, String tag1, String tag2, String keyword) {
		BigDecimal count = new BigDecimal(0);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record1<BigDecimal>> step = context.select(Tables.EH_RENTALV2_ORDERS.PAY_TOTAL_MONEY.sum()).from(Tables.EH_RENTALV2_ORDERS);
		Condition condition = Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.INACTIVE.getCode());
		if (StringUtils.isNotBlank(resourceType))
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE.equal(resourceType));
		if (StringUtils.isNotBlank(tag1))
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.STRING_TAG1.equal(tag1));
		if (StringUtils.isNotBlank(tag2))
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.STRING_TAG2.equal(tag2));
		if(null != resourceTypeId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE_ID.equal(resourceTypeId));
		if (null != rentalSiteId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_RESOURCE_ID.equal(rentalSiteId));
		if (null != startTime) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.gt(new Timestamp(startTime)));
		}
		if (null != endTime) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.lt(new Timestamp(endTime)));
		}
		if (null != billStatus)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS.equal(billStatus));
		if (null != keyword)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.CUSTOM_OBJECT.like("%"+keyword+"%").
					or(Tables.EH_RENTALV2_ORDERS.USER_NAME.eq(keyword)).or(Tables.EH_RENTALV2_ORDERS.USER_PHONE.eq(keyword)));

		count = step.where(condition).fetchOneInto(BigDecimal.class);

		return count;
	}

	@Override
	public BigDecimal countRentalBillAmount(String resourceType,Long resourceTypeId,Long communityId, Long startTime, Long endTime, Long rentalSiteId, Long orgId) {
		BigDecimal count = new BigDecimal(0);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record1<BigDecimal>> step = context.select(Tables.EH_RENTALV2_ORDERS.PAY_TOTAL_MONEY.sum()).from(Tables.EH_RENTALV2_ORDERS);
		Condition condition = Tables.EH_RENTALV2_ORDERS.STATUS.in(SiteBillStatus.COMPLETE.getCode(),SiteBillStatus.SUCCESS.getCode(),SiteBillStatus.IN_USING.getCode());
		if (StringUtils.isNotBlank(resourceType))
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE.equal(resourceType));
		if (null!=resourceTypeId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE_ID.eq(resourceTypeId));
		if (null!=communityId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.COMMUNITY_ID.equal(communityId));
		if (null != startTime) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.gt(new Timestamp(startTime)));
		}
		if (null != endTime) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.lt(new Timestamp(endTime)));
		}
		if (null != rentalSiteId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_RESOURCE_ID.equal(rentalSiteId));
		if (null != orgId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.USER_ENTERPRISE_ID.equal(orgId));
		count = step.where(condition).fetchOneInto(BigDecimal.class);

		return count;
	}

	@Override
	public List<RentalStatisticsDTO> listRentalBillAmountByOrgId(String resourceType, Long resourceTypeId,Long communityId,
																 Long startTime, Long endTime,Integer order){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record2<BigDecimal,Long>> step = context.select(Tables.EH_RENTALV2_ORDERS.PAY_TOTAL_MONEY.sum(),
				Tables.EH_RENTALV2_ORDERS.USER_ENTERPRISE_ID).from(Tables.EH_RENTALV2_ORDERS);
		Condition condition = Tables.EH_RENTALV2_ORDERS.STATUS.in(SiteBillStatus.COMPLETE.getCode(),SiteBillStatus.SUCCESS.getCode(),SiteBillStatus.IN_USING.getCode());
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.USER_ENTERPRISE_ID.isNotNull());
		if (StringUtils.isNotBlank(resourceType))
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE.equal(resourceType));
		if (null!=resourceTypeId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE_ID.eq(resourceTypeId));
		if (null!=communityId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.COMMUNITY_ID.equal(communityId));
		if (null != startTime) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.gt(new Timestamp(startTime)));
		}
		if (null != endTime) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.lt(new Timestamp(endTime)));
		}
		SortField<BigDecimal> sortOrder = Tables.EH_RENTALV2_ORDERS.PAY_TOTAL_MONEY.sum().asc();
		if (order != null && order<0)
			sortOrder = Tables.EH_RENTALV2_ORDERS.PAY_TOTAL_MONEY.sum().desc();

		return step.where(condition).groupBy(Tables.EH_RENTALV2_ORDERS.USER_ENTERPRISE_ID).orderBy(sortOrder).fetch()
				.map(r->{
					RentalStatisticsDTO dto = new RentalStatisticsDTO();
					dto.setAmount(r.getValue(Tables.EH_RENTALV2_ORDERS.PAY_TOTAL_MONEY.sum()));
					dto.setEnterpriseId(r.getValue(Tables.EH_RENTALV2_ORDERS.USER_ENTERPRISE_ID));
					return dto;
				});
	}

	@Override
	public Integer countRentalBillNum(String resourceType, Long resourceTypeId,Long communityId, Long startTime, Long endTime, Long rentalSiteId, Long orgId) {
		Integer count = 0;
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record1<Integer>> step = context.selectCount().from(Tables.EH_RENTALV2_ORDERS);
		Condition condition = Tables.EH_RENTALV2_ORDERS.STATUS.in(SiteBillStatus.COMPLETE.getCode(),SiteBillStatus.SUCCESS.getCode(),SiteBillStatus.IN_USING.getCode());
		if (StringUtils.isNotBlank(resourceType))
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE.equal(resourceType));
		if (null!=resourceTypeId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE_ID.eq(resourceTypeId));
		if (null!=communityId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.COMMUNITY_ID.equal(communityId));
		if (null != startTime) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.gt(new Timestamp(startTime)));
		}
		if (null != endTime) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.lt(new Timestamp(endTime)));
		}
		if (null != rentalSiteId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_RESOURCE_ID.equal(rentalSiteId));
		if (null != orgId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.USER_ENTERPRISE_ID.equal(orgId));
		count = step.where(condition).fetchOneInto(Integer.class);
		return count;
	}

	@Override
	public List<RentalStatisticsDTO> listRentalBillNumByOrgId(String resourceType, Long resourceTypeId,Long communityId,
															  Long startTime, Long endTime,Integer order){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record2<Integer,Long>> step = context.select(DSL.count(),Tables.EH_RENTALV2_ORDERS.USER_ENTERPRISE_ID).from(Tables.EH_RENTALV2_ORDERS);
		Condition condition = Tables.EH_RENTALV2_ORDERS.STATUS.in(SiteBillStatus.COMPLETE.getCode(),SiteBillStatus.SUCCESS.getCode(),SiteBillStatus.IN_USING.getCode());
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.USER_ENTERPRISE_ID.isNotNull());
		if (StringUtils.isNotBlank(resourceType))
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE.equal(resourceType));
		if (null!=resourceTypeId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE_ID.eq(resourceTypeId));
		if (null != startTime) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.gt(new Timestamp(startTime)));
		}
		if (null != endTime) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESERVE_TIME.lt(new Timestamp(endTime)));
		}
		if (null!=communityId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.COMMUNITY_ID.equal(communityId));
		SortField<Integer> sortOrder = DSL.count().asc();
		if (order != null && order<0)
			sortOrder = DSL.count().desc();

		return step.where(condition).groupBy(Tables.EH_RENTALV2_ORDERS.USER_ENTERPRISE_ID).orderBy(sortOrder).fetch().map(r->{
			RentalStatisticsDTO dto = new RentalStatisticsDTO();
			dto.setOrderCount(r.getValue(DSL.count()));
			dto.setEnterpriseId(r.getValue(Tables.EH_RENTALV2_ORDERS.USER_ENTERPRISE_ID));
			return dto;
		});
	}

	@Override
	public Long countRentalBillValidTime(String resourceType, Long resourceTypeId,Long communityId, Long startTime, Long endTime, Long rentalSiteId, Long orgId) {
		Long count = 0l;
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record1<BigDecimal>> step = context.select(Tables.EH_RENTALV2_ORDER_STATISTICS.VALID_TIME_LONG.sum()).from(Tables.EH_RENTALV2_ORDER_STATISTICS);
		Condition condition = Tables.EH_RENTALV2_ORDER_STATISTICS.RESOURCE_TYPE.eq(resourceType);
		if (null!=resourceTypeId)
			condition = condition.and(Tables.EH_RENTALV2_ORDER_STATISTICS.RESOURCE_TYPE_ID.eq(resourceTypeId));
		if (null!=communityId)
			condition = condition.and(Tables.EH_RENTALV2_ORDER_STATISTICS.COMMUNITY_ID.eq(communityId));
		if (null!=startTime)
			condition = condition.and(Tables.EH_RENTALV2_ORDER_STATISTICS.RENTAL_DATE.ge(new Date(startTime)));
		if (null!=endTime)
			condition = condition.and(Tables.EH_RENTALV2_ORDER_STATISTICS.RENTAL_DATE.le(new Date(endTime)));
		if (null != rentalSiteId)
			condition = condition.and(Tables.EH_RENTALV2_ORDER_STATISTICS.RENTAL_RESOURCE_ID.eq(rentalSiteId));
		if (null!=orgId)
			condition = condition.and(Tables.EH_RENTALV2_ORDER_STATISTICS.USER_ENTERPRISE_ID.eq(orgId));
		count = step.where(condition).fetchOneInto(Long.class);
		return count;
	}

	@Override
	public List<RentalStatisticsDTO> listRentalBillValidTimeByOrgId(String resourceType, Long resourceTypeId,Long communityId,
																	Long startTime, Long endTime,Integer order){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record2<BigDecimal,Long>> step = context.select(Tables.EH_RENTALV2_ORDER_STATISTICS.VALID_TIME_LONG.sum(),
				Tables.EH_RENTALV2_ORDER_STATISTICS.USER_ENTERPRISE_ID).from(Tables.EH_RENTALV2_ORDER_STATISTICS);
		Condition condition = Tables.EH_RENTALV2_ORDER_STATISTICS.USER_ENTERPRISE_ID.isNotNull();
		if (null != resourceType)
			condition = condition.and(Tables.EH_RENTALV2_ORDER_STATISTICS.RESOURCE_TYPE.eq(resourceType));
		if (null!=resourceTypeId)
			condition = condition.and(Tables.EH_RENTALV2_ORDER_STATISTICS.RESOURCE_TYPE_ID.eq(resourceTypeId));
		if (null!=communityId)
			condition = condition.and(Tables.EH_RENTALV2_ORDER_STATISTICS.COMMUNITY_ID.eq(communityId));
		if (null!=startTime)
			condition = condition.and(Tables.EH_RENTALV2_ORDER_STATISTICS.RENTAL_DATE.ge(new Date(startTime)));
		if (null!=endTime)
			condition = condition.and(Tables.EH_RENTALV2_ORDER_STATISTICS.RENTAL_DATE.le(new Date(endTime)));
		SortField<BigDecimal> sortOrder = Tables.EH_RENTALV2_ORDER_STATISTICS.VALID_TIME_LONG.sum().asc();
		if (order != null && order<0)
			sortOrder = Tables.EH_RENTALV2_ORDER_STATISTICS.VALID_TIME_LONG.sum().desc();
		return step.where(condition).groupBy(Tables.EH_RENTALV2_ORDER_STATISTICS.USER_ENTERPRISE_ID).orderBy(sortOrder).
				fetch().map(r->{
			RentalStatisticsDTO dto = new RentalStatisticsDTO();
			dto.setUsedTime(r.getValue(Tables.EH_RENTALV2_ORDER_STATISTICS.VALID_TIME_LONG.sum()).longValue());
			dto.setEnterpriseId(r.getValue(Tables.EH_RENTALV2_ORDER_STATISTICS.USER_ENTERPRISE_ID));
			return dto;
		});

	}

	@Override
	public List<RentalResource> findRentalSites(Long  resourceTypeId, String keyword, ListingLocator locator,
			Integer pageSize, Byte status,List<Long> siteIds,Long communityId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_RESOURCES);
		//TODO
		Condition condition = Tables.EH_RENTALV2_RESOURCES.RESOURCE_TYPE_ID
				.equal(resourceTypeId);
		if(null!=siteIds )
			condition= condition.and( Tables.EH_RENTALV2_RESOURCES.ID.in(siteIds));
//		condition = condition.and(Tables.EH_RENTALV2_RESOURCES.OWNER_TYPE
//				.equal(ownerType));
//		condition = condition.and(Tables.EH_RENTALV2_RESOURCES.RESOURCE_TYPE
//				.equal(siteType));
		if (!StringUtils.isEmpty(keyword)) {
			condition = condition.and(Tables.EH_RENTALV2_RESOURCES.ADDRESS
					.like("%" + keyword + "%")
					.or(Tables.EH_RENTALV2_RESOURCES.RESOURCE_NAME.like("%" + keyword
							+ "%"))
					.or(Tables.EH_RENTALV2_RESOURCES.BUILDING_NAME.like("%" + keyword
							+ "%")));
		}

        if(locator.getAnchor() != null)
        	condition=condition.and(Tables.EH_RENTALV2_RESOURCES.ID.gt(locator.getAnchor()));

        if(communityId  != null)
        	condition=condition.and(Tables.EH_RENTALV2_RESOURCES.COMMUNITY_ID.eq(communityId));
		if(null != status) {
			condition = condition.and(Tables.EH_RENTALV2_RESOURCES.STATUS.eq(status));
		}else {
			condition = condition.and(Tables.EH_RENTALV2_RESOURCES.STATUS.ne(RentalSiteStatus.DELETED.getCode()));
		}

		return step.where(condition).orderBy(Tables.EH_RENTALV2_RESOURCES.DEFAULT_ORDER.asc())
				.limit(pageSize).fetch().map((r) -> ConvertHelper.convert(r, RentalResource.class));
	}

	@Override
	public List<RentalResource> findRentalSitesByCommunityId(String resouceType,Long resourceTypeId, Long communityId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_RESOURCES);
		Condition condition = Tables.EH_RENTALV2_RESOURCES.STATUS.ne(RentalSiteStatus.DELETED.getCode());
		if (null!=resourceTypeId)
			condition=condition.and(Tables.EH_RENTALV2_RESOURCES.RESOURCE_TYPE_ID.eq(resourceTypeId));
		if(communityId  != null)
			condition=condition.and(Tables.EH_RENTALV2_RESOURCES.COMMUNITY_ID.eq(communityId));
		if (!StringUtils.isEmpty(resouceType))
			condition=condition.and(Tables.EH_RENTALV2_RESOURCES.RESOURCE_TYPE.eq(resouceType));

		return step.where(condition).fetch().map((r) -> ConvertHelper.convert(r, RentalResource.class));
	}

	@Override
	public void updateRentalBill(RentalOrder bill) {
		assert (bill.getId() == null);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2OrdersDao dao = new EhRentalv2OrdersDao(context.configuration());
		dao.update(bill);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRentalv2Orders.class,
				bill.getId());
	}


	@Override
	public Long createRentalBillAttachment(RentalOrderAttachment rba) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2OrderAttachments.class));
		rba.setId(id);
		rba.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		rba.setCreatorUid(UserContext.currentUserId());

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2OrderAttachmentsRecord record = ConvertHelper.convert(rba,
				EhRentalv2OrderAttachmentsRecord.class);
		InsertQuery<EhRentalv2OrderAttachmentsRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_ORDER_ATTACHMENTS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE,
				EhRentalv2OrderAttachments.class, null);
		return id;
	}

//	@Override
//	public Long createRentalBillPaybillMap(RentalOrderPayorderMap billmap) {
//		long id = sequenceProvider.getNextSequence(NameMapper
//				.getSequenceDomainFromTablePojo(EhRentalv2OrderPayorderMap.class));
//		billmap.setId(id);
//		billmap.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		billmap.setCreatorUid(UserContext.currentUserId());
//
//		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
//		EhRentalv2OrderPayorderMapRecord record = ConvertHelper.convert(billmap,
//				EhRentalv2OrderPayorderMapRecord.class);
//		InsertQuery<EhRentalv2OrderPayorderMapRecord> query = context
//				.insertQuery(Tables.EH_RENTALV2_ORDER_PAYORDER_MAP);
//		query.setRecord(record);
//		query.execute();
//		DaoHelper.publishDaoAction(DaoAction.CREATE,
//				EhRentalv2OrderPayorderMap.class, null);
//		return id;
//	}

	@Override
	public List<RentalResourceOrder> findRentalResourceOrderByOrderId(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context
				.select()
				.from(Tables.EH_RENTALV2_RESOURCE_ORDERS);

		Condition condition = Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_ORDER_ID
				.equal(id);
		condition = condition.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.STATUS
				.ne(ResourceOrderStatus.DISPLOY.getCode()));
		step.where(condition);
		List<RentalResourceOrder> result  = step
				.orderBy(Tables.EH_RENTALV2_RESOURCE_ORDERS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalResourceOrder.class);
				});


		return result;
	}

	@Override
	public List<RentalOrderAttachment> findRentalBillAttachmentByBillId(
			Long rentalBillId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ORDER_ATTACHMENTS);
		Condition condition = Tables.EH_RENTALV2_ORDER_ATTACHMENTS.RENTAL_ORDER_ID
				.eq(rentalBillId);

		step.where(condition);
		List<RentalOrderAttachment> result = step
				.orderBy(Tables.EH_RENTALV2_ORDER_ATTACHMENTS.ID.desc())
				.fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalOrderAttachment.class);
				});

		return result;
	}

//	@Override
//	public RentalOrderPayorderMap findRentalBillPaybillMapByOrderNo(String orderNo) {
//		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//		SelectJoinStep<Record> step = context.select().from(
//				Tables.EH_RENTALV2_ORDER_PAYORDER_MAP);
//		Condition condition = Tables.EH_RENTALV2_ORDER_PAYORDER_MAP.ORDER_NO
//				.equal(Long.valueOf(orderNo));
//		step.where(condition);
//		List<RentalOrderPayorderMap> result = step
//				.orderBy(Tables.EH_RENTALV2_ORDER_PAYORDER_MAP.ID.desc()).fetch()
//				.map((r) -> {
//					return ConvertHelper.convert(r, RentalOrderPayorderMap.class);
//				});
//		if (null != result && result.size() > 0)
//			return result.get(0);
//		return null;
//	}
//	public List<RentalOrderPayorderMap> findRentalBillPaybillMapByBillId(Long id){
//		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//		SelectJoinStep<Record> step = context.select().from(
//				Tables.EH_RENTALV2_ORDER_PAYORDER_MAP);
//		Condition condition = Tables.EH_RENTALV2_ORDER_PAYORDER_MAP.ORDER_ID
//				.equal(id);
//		step.where(condition);
//		List<RentalOrderPayorderMap> result = step
//				.orderBy(Tables.EH_RENTALV2_ORDER_PAYORDER_MAP.ID.desc()).fetch()
//				.map((r) -> {
//					return ConvertHelper.convert(r, RentalOrderPayorderMap.class);
//				});
//		if (null != result && result.size() > 0)
//			return result;
//		return null;
//	}
	@Override
	public List<RentalOrder> listRentalBills(Long ownerId, String ownerType,
			String siteType, Long rentalSiteId, Long beginDate, Long endDate) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ORDERS);
		//TODO：
		Condition condition = Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE_ID
				.equal(ownerId);
//		condition = condition.and(Tables.EH_RENTALV2_ORDERS.OWNER_TYPE
//				.equal(ownerType));
//		if (StringUtils.isNotEmpty(siteType))
//			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE
//					.equal(siteType));
		if (null != rentalSiteId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_RESOURCE_ID
					.equal(rentalSiteId));
		if (null != beginDate)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_DATE
					.greaterOrEqual(new Date(beginDate)));
		if (null != endDate)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_DATE
					.lessOrEqual(new Date(endDate)));
		step.where(condition);
		List<RentalOrder> result = step
				.orderBy(Tables.EH_RENTALV2_ORDERS.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalOrder.class);
				});
		if (null != result && result.size() > 0)
			return result ;
		return null;
	}
	@Override
	public List<RentalOrder> listTargetRentalBills(Byte status) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ORDERS);
		//TODO：
		Condition condition = Tables.EH_RENTALV2_ORDERS.STATUS
				.eq(status);
		step.where(condition);
		List<RentalOrder> result = step
				.orderBy(Tables.EH_RENTALV2_ORDERS.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalOrder.class);
				});
		if (null != result && result.size() > 0)
			return result ;
		return null;
	}
	
//
//	@Override
//	public void createRentalRule(RentalRule rentalRule) {
//		long id = sequenceProvider.getNextSequence(NameMapper
//				.getSequenceDomainFromTablePojo(EhRentalRules.class));
//		rentalRule.setId(id);
//		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
//		EhRentalRulesRecord record = ConvertHelper.convert(rentalRule,
//				EhRentalRulesRecord.class);
//		InsertQuery<EhRentalRulesRecord> query = context
//				.insertQuery(Tables.EH_RENTALV2_RULES);
//		query.setRecord(record);
//		query.execute();
//
//		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalRules.class,
//				null);
//		
//	}

	@Override
	public List<RentalSiteRange> findRentalSiteOwnersByOwnerTypeAndId(String resourceType, String ownerType,Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_RESOURCE_RANGES);
		Condition condition = Tables.EH_RENTALV2_RESOURCE_RANGES.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTALV2_RESOURCE_RANGES.OWNER_TYPE
				.equal(ownerType));
		condition = condition.and(Tables.EH_RENTALV2_RESOURCE_RANGES.RESOURCE_TYPE
				.equal(resourceType));
		step.where(condition);
		List<RentalSiteRange> result = step
				.orderBy(Tables.EH_RENTALV2_RESOURCE_RANGES.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalSiteRange.class);
				});
		if (null != result && result.size() > 0)
			return result ;
		return null;

	}


	@Override
	public List<RentalResourcePic> findRentalSitePicsByOwnerTypeAndId(String resourceType, String ownerType,Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_SITE_RESOURCES);
		Condition condition = Tables.EH_RENTALV2_SITE_RESOURCES.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTALV2_SITE_RESOURCES.OWNER_TYPE
				.equal(ownerType));
		condition = condition.and(Tables.EH_RENTALV2_SITE_RESOURCES.RESOURCE_TYPE
				.equal(resourceType));
		condition = condition.and(Tables.EH_RENTALV2_SITE_RESOURCES.TYPE.eq("pic"));
		step.where(condition);
		List<RentalResourcePic> result = step
				.orderBy(Tables.EH_RENTALV2_SITE_RESOURCES.ID.asc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalResourcePic.class);
				});
		if (null != result && result.size() > 0)
			return result ;
		return null;

	}

	@Override
	public List<RentalResourceFile> findRentalSiteFilesByOwnerTypeAndId(String resourceType, String ownerType, Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_SITE_RESOURCES);
		Condition condition = Tables.EH_RENTALV2_SITE_RESOURCES.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTALV2_SITE_RESOURCES.OWNER_TYPE
				.equal(ownerType));
		condition = condition.and(Tables.EH_RENTALV2_SITE_RESOURCES.RESOURCE_TYPE
				.equal(resourceType));
		condition = condition.and(Tables.EH_RENTALV2_SITE_RESOURCES.TYPE.eq("file"));
		step.where(condition);
		List<RentalResourceFile> result = step
				.orderBy(Tables.EH_RENTALV2_SITE_RESOURCES.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalResourceFile.class);
				});
		if (null != result && result.size() > 0)
			return result ;
		return null;
	}

	@Override
	public RentalItem getRentalSiteItemById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ITEMS);
		Condition condition = Tables.EH_RENTALV2_ITEMS.ID.equal(id);
		step.where(condition);
		List<RentalItem> result = step
				.orderBy(Tables.EH_RENTALV2_ITEMS.DEFAULT_ORDER.asc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalItem.class);
				});
		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}

//	@Override
//	public List<RentalResourceOrderNumber> findSitesBillNumbersBySiteId(
//			Long siteRuleId) {
//		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//		SelectJoinStep<Record> step = context.select().from(
//				Tables.EH_RENTALV2_RESOURCES_ORDER_NUMBERS);
//		Condition condition = Tables.EH_RENTALV2_RESOURCES_ORDER_NUMBERS.RENTAL_RESOURCE_RULE_ID.equal(siteRuleId);
//		step.where(condition);
//		List<RentalResourceOrderNumber> result = step
//				.orderBy(Tables.EH_RENTALV2_RESOURCES_ORDER_NUMBERS.ID.desc()).fetch().map((r) -> {
//					return ConvertHelper.convert(r, RentalResourceOrderNumber.class);
//				});
//		if (null != result && result.size() > 0)
//			return result;
//		return null;
//	}
//	@Override
//	public List<RentalResourceOrderNumber> findSitesBillNumbersByBillId(
//			Long siteBillId) {
//		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
//		SelectJoinStep<Record> step = context.select().from(
//				Tables.EH_RENTALV2_RESOURCES_ORDER_NUMBERS);
//		Condition condition = Tables.EH_RENTALV2_RESOURCES_ORDER_NUMBERS.RENTAL_RESOURCE_ORDER_ID.equal(siteBillId);
//		step.where(condition);
//		List<RentalResourceOrderNumber> result = step
//				.orderBy(Tables.EH_RENTALV2_RESOURCES_ORDER_NUMBERS.ID.desc()).fetch().map((r) -> {
//					return ConvertHelper.convert(r, RentalResourceOrderNumber.class);
//				});
//		if (null != result && result.size() > 0)
//			return result;
//		return null;
//	}
//	@Override
//	public void createRentalResourceOrderNumber(RentalResourceOrderNumber sitesBillNumber) {
//		long id = sequenceProvider.getNextSequence(NameMapper
//				.getSequenceDomainFromTablePojo(EhRentalv2ResourcesBillNumbers.class));
//		sitesBillNumber.setId(id);
//		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
//		EhRentalv2ResourcesBillNumbersRecord record = ConvertHelper.convert(sitesBillNumber,
//				EhRentalv2ResourcesBillNumbersRecord.class);
//		InsertQuery<EhRentalv2ResourcesBillNumbersRecord> query = context
//				.insertQuery(Tables.EH_RENTALV2_RESOURCES_ORDER_NUMBERS);
//		query.setRecord(record); 
//		query.execute();
//		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2ResourcesBillNumbers.class, null);
//		 
//	}

	@Override
	public void createRentalDefaultRule(RentalDefaultRule defaultRule) {

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2DefaultRules.class));
		defaultRule.setId(id);
		defaultRule.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		defaultRule.setCreatorUid(UserContext.currentUserId());

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2DefaultRulesRecord record = ConvertHelper.convert(defaultRule,
				EhRentalv2DefaultRulesRecord.class);
		InsertQuery<EhRentalv2DefaultRulesRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_DEFAULT_RULES);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2DefaultRules.class,
				null); 
		
	}

	@Override
	public void updateRentalDefaultRule(RentalDefaultRule newDefaultRule) {
		assert (newDefaultRule.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2DefaultRulesDao dao = new EhRentalv2DefaultRulesDao(context.configuration());
		dao.update(newDefaultRule);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRentalv2DefaultRules.class,
				newDefaultRule.getId());
	}
	@Override
	public void createTimeInterval(RentalTimeInterval timeInterval) {

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2TimeInterval.class));
		timeInterval.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2TimeIntervalRecord record = ConvertHelper.convert(timeInterval,
				EhRentalv2TimeIntervalRecord.class);
		InsertQuery<EhRentalv2TimeIntervalRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_TIME_INTERVAL);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2TimeInterval.class,
				null); 
	}

	@Override
	public void createRentalCloseDate(RentalCloseDate rcd) {

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2CloseDates.class));
		rcd.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2CloseDatesRecord record = ConvertHelper.convert(rcd,
				EhRentalv2CloseDatesRecord.class);
		InsertQuery<EhRentalv2CloseDatesRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_CLOSE_DATES);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2CloseDates.class,
				null); 
	}

	@Override
	public void createRentalDayopenTime(RentalDayopenTime rdt) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2DayopenTime.class));
		rdt.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2DayopenTimeRecord record = ConvertHelper.convert(rdt,
				EhRentalv2DayopenTimeRecord.class);
		InsertQuery<EhRentalv2DayopenTimeRecord> query = context.insertQuery(Tables.EH_RENTALV2_DAYOPEN_TIME);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE,EhRentalv2DayopenTime.class,null);

	}

	@Override
	public void createRentalConfigAttachment(RentalConfigAttachment rca) {

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2ConfigAttachments.class));
		rca.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2ConfigAttachmentsRecord record = ConvertHelper.convert(rca,
				EhRentalv2ConfigAttachmentsRecord.class);
		InsertQuery<EhRentalv2ConfigAttachmentsRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2ConfigAttachments.class,
				null); 
	}

	@Override
	public RentalDefaultRule getRentalDefaultRule(String ownerType, Long ownerId, String resourceType,
												  Long resourceTypeId, String sourceType, Long sourceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_DEFAULT_RULES);
		Condition condition = Tables.EH_RENTALV2_DEFAULT_RULES.RESOURCE_TYPE_ID
				.equal(resourceTypeId);
		if (StringUtils.isNotBlank(ownerType)) {
			condition = condition.and(Tables.EH_RENTALV2_DEFAULT_RULES.OWNER_TYPE
					.equal(ownerType));
		}
		if (null != ownerId) {
			condition = condition.and(Tables.EH_RENTALV2_DEFAULT_RULES.OWNER_ID
					.equal(ownerId));
		}
		if (StringUtils.isNotBlank(resourceType)) {
			condition = condition.and(Tables.EH_RENTALV2_DEFAULT_RULES.RESOURCE_TYPE
					.equal(resourceType));
		}
		if (StringUtils.isNotBlank(sourceType)) {
			condition = condition.and(Tables.EH_RENTALV2_DEFAULT_RULES.SOURCE_TYPE
					.equal(sourceType));
		}
		if (null != sourceId) {
			condition = condition.and(Tables.EH_RENTALV2_DEFAULT_RULES.SOURCE_ID
					.equal(sourceId));
		}

		step.where(condition);
		List<RentalDefaultRule> result = step
				.orderBy(Tables.EH_RENTALV2_DEFAULT_RULES.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalDefaultRule.class);
				});
		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public List<RentalTimeInterval> queryRentalTimeIntervalByOwner(
			String resourceType, String ownerType, Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_TIME_INTERVAL);
		Condition condition = Tables.EH_RENTALV2_TIME_INTERVAL.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTALV2_TIME_INTERVAL.OWNER_TYPE
				.equal(ownerType));
		condition = condition.and(Tables.EH_RENTALV2_TIME_INTERVAL.RESOURCE_TYPE
				.equal(resourceType));
		step.where(condition);
		List<RentalTimeInterval> result = step
				.orderBy(Tables.EH_RENTALV2_TIME_INTERVAL.BEGIN_TIME.asc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalTimeInterval.class);
				});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}

	@Override
	public List<RentalCloseDate> queryRentalCloseDateByOwner(String resourceType, String ownerType,
			Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_CLOSE_DATES);
		Condition condition = Tables.EH_RENTALV2_CLOSE_DATES.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTALV2_CLOSE_DATES.OWNER_TYPE
				.equal(ownerType));
		condition = condition.and(Tables.EH_RENTALV2_CLOSE_DATES.RESOURCE_TYPE
				.equal(resourceType));
		step.where(condition);
		List<RentalCloseDate> result = step
				.orderBy(Tables.EH_RENTALV2_CLOSE_DATES.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalCloseDate.class);
				});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}

	@Override
	public List<RentalConfigAttachment> queryRentalConfigAttachmentByOwner(String resourceType,
																		   String ownerType, Long ownerId,Byte attachmentType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_CONFIG_ATTACHMENTS);
		Condition condition = Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.OWNER_TYPE
				.equal(ownerType));

		condition = condition.and(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.RESOURCE_TYPE
				.equal(resourceType));

		if (attachmentType!=null)
			condition = condition.and(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.ATTACHMENT_TYPE
					.equal(attachmentType));
		step.where(condition);
		List<RentalConfigAttachment> result = step
				.orderBy(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.DEFAULT_ORDER).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalConfigAttachment.class);
				});

		return result;
	}

	@Override
	public List<RentalConfigAttachment> queryRentalConfigAttachmentByIds(List<Long> ids) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_CONFIG_ATTACHMENTS);
		Condition condition = Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.ID
				.in(ids);

		step.where(condition);
		List<RentalConfigAttachment> result = step
				.orderBy(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalConfigAttachment.class);
				});

		return result;
	}

	@Override
	public List<RentalDayopenTime> queryRentalDayopenTimeByOwner(String resourceType, String ownerType, Long ownerId,Byte rentalType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_DAYOPEN_TIME);
		Condition condition = Tables.EH_RENTALV2_DAYOPEN_TIME.RESOURCE_TYPE.eq(resourceType);
		condition = condition.and(Tables.EH_RENTALV2_DAYOPEN_TIME.OWNER_TYPE.eq(ownerType));
		condition = condition.and(Tables.EH_RENTALV2_DAYOPEN_TIME.OWNER_ID.eq(ownerId));
		if (null != rentalType)
			condition = condition.and(Tables.EH_RENTALV2_DAYOPEN_TIME.RENTAL_TYPE.eq(rentalType));
		step.where(condition);
		List<RentalDayopenTime> result = step.fetch().map(r->{
			return ConvertHelper.convert(r,RentalDayopenTime.class);
		});
		return result;
	}

	@Override
	public void createRentalSiteOwner(RentalSiteRange siteOwner) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2ResourceRanges.class));
		siteOwner.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2ResourceRangesRecord record = ConvertHelper.convert(siteOwner,
				EhRentalv2ResourceRangesRecord.class);
		InsertQuery<EhRentalv2ResourceRangesRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_RESOURCE_RANGES);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2ResourceRanges.class, null);
	}

	@Override
	public void createRentalSitePic(RentalResourcePic detailPic) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2SiteResources.class));
		detailPic.setId(id);
		detailPic.setType("pic");
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2SiteResourcesRecord record = ConvertHelper.convert(detailPic,
				EhRentalv2SiteResourcesRecord.class);
		InsertQuery<EhRentalv2SiteResourcesRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_SITE_RESOURCES);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2SiteResources.class, null);
		
	}

	@Override
	public void createRentalSiteFile(RentalResourceFile file) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2SiteResources.class));
		file.setId(id);
		file.setType("file");
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2SiteResourcesRecord record = ConvertHelper.convert(file,
				EhRentalv2SiteResourcesRecord.class);
		InsertQuery<EhRentalv2SiteResourcesRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_SITE_RESOURCES);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2SiteResources.class, null);
	}

	@Override
	public void deleteRentalSitePicsBySiteId(String resourceType, Long siteId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2SiteResourcesRecord> step = context
				.delete(Tables.EH_RENTALV2_SITE_RESOURCES);
		Condition condition = Tables.EH_RENTALV2_SITE_RESOURCES.OWNER_ID
				.equal(siteId);
		condition = condition.and(Tables.EH_RENTALV2_SITE_RESOURCES.OWNER_TYPE
				.equal(EhRentalv2Resources.class.getSimpleName()));
		condition = condition.and(Tables.EH_RENTALV2_SITE_RESOURCES.RESOURCE_TYPE
				.equal(resourceType));
		condition = condition.and(Tables.EH_RENTALV2_SITE_RESOURCES.TYPE.eq("pic"));
		step.where(condition);
		step.execute();
	}

	@Override
	public void deleteRentalSiteFilesBySiteId(String resourceType, Long siteId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2SiteResourcesRecord> step = context
				.delete(Tables.EH_RENTALV2_SITE_RESOURCES);
		Condition condition = Tables.EH_RENTALV2_SITE_RESOURCES.OWNER_ID
				.equal(siteId);
		condition = condition.and(Tables.EH_RENTALV2_SITE_RESOURCES.OWNER_TYPE
				.equal(EhRentalv2Resources.class.getSimpleName()));
		condition = condition.and(Tables.EH_RENTALV2_SITE_RESOURCES.RESOURCE_TYPE
				.equal(resourceType));
		condition = condition.and(Tables.EH_RENTALV2_SITE_RESOURCES.TYPE.eq("file"));
		step.where(condition);
		step.execute();
	}

	@Override
	public void deleteRentalSiteOwnersBySiteId(String resourceType, Long siteId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2ResourceRangesRecord> step = context
				.delete(Tables.EH_RENTALV2_RESOURCE_RANGES);
		Condition condition = Tables.EH_RENTALV2_RESOURCE_RANGES.RENTAL_RESOURCE_ID
				.equal(siteId);
		condition = condition.and(Tables.EH_RENTALV2_RESOURCE_RANGES.RESOURCE_TYPE
				.equal(resourceType));
		step.where(condition);
		step.execute();
	}

	@Override
	public void updateRentalSiteItem(RentalItem siteItem) {
		assert(siteItem.getId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhUsers.class, siteItem.getId().longValue()));
        EhRentalv2ItemsDao dao = new EhRentalv2ItemsDao(context.configuration());
        dao.update(siteItem);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhUsers.class, siteItem.getId());
	}

	@Override
	public Integer countRentalSiteItemSoldCount(Long itemId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record1<BigDecimal>> step = context
				.select(Tables.EH_RENTALV2_ITEMS_ORDERS.RENTAL_COUNT.sum())
				.from(Tables.EH_RENTALV2_ITEMS_ORDERS);
				 
		Condition condition = Tables.EH_RENTALV2_ITEMS_ORDERS.RENTAL_RESOURCE_ITEM_ID
				.equal(itemId);
		 
		step.where(condition);
		Record1<BigDecimal> record1 = step.fetchOne();
		if(null == record1||null == record1.value1())
			return 0;
		Integer result = record1.value1().intValue();
		return result;
	}


	@Override
	public Integer countRentalSiteItemRentalCount(List<Long> rentalBillIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record1<BigDecimal>> step = context
				.select(Tables.EH_RENTALV2_ITEMS_ORDERS.RENTAL_COUNT.sum())
				.from(Tables.EH_RENTALV2_ITEMS_ORDERS);
				 
		Condition condition = Tables.EH_RENTALV2_ITEMS_ORDERS.RENTAL_ORDER_ID.in(rentalBillIds);
		 
		step.where(condition);
		Record1<BigDecimal> record1 = step.fetchOne();
		if(null == record1||null == record1.value1())
			return 0;
		Integer result = record1.value1().intValue();
		return result;
	}
	@Override
	public void updateRentalSiteRule(RentalCell rsr) {
		assert (rsr.getId() == null);
		Long tmp = rsr.getId();
		rsr.setId(rsr.getCellId());
		rsr.setCellId(tmp);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2CellsDao dao = new EhRentalv2CellsDao(context.configuration());
		dao.update(rsr);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRentalv2Cells.class,
				rsr.getId());
	}

	@Override
	public Integer deleteTimeIntervalsByOwnerId(String resourceType, String ownerType, Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2TimeIntervalRecord> step = context
				.delete(Tables.EH_RENTALV2_TIME_INTERVAL);
		Condition condition = Tables.EH_RENTALV2_TIME_INTERVAL.OWNER_TYPE
				.equal(ownerType).and(Tables.EH_RENTALV2_TIME_INTERVAL.OWNER_ID
				.equal(id)).and(Tables.EH_RENTALV2_TIME_INTERVAL.RESOURCE_TYPE
						.equal(resourceType));
		 
		step.where(condition);
		Integer deleteCount = step.execute();
		return deleteCount;
	}

	@Override
	public Integer deleteRentalCloseDatesByOwnerId(String resourceType, String ownerType, Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2CloseDatesRecord> step = context
				.delete(Tables.EH_RENTALV2_CLOSE_DATES);
		Condition condition = Tables.EH_RENTALV2_CLOSE_DATES.OWNER_TYPE
				.equal(ownerType).and(Tables.EH_RENTALV2_CLOSE_DATES.OWNER_ID
				.equal(id)).and(Tables.EH_RENTALV2_CLOSE_DATES.RESOURCE_TYPE
						.equal(resourceType));
		 
		step.where(condition);
		Integer deleteCount = step.execute();
		return deleteCount;
		
	}

	@Override
	public Integer deleteRentalConfigAttachmentsByOwnerId(String resourceType, String ownerType,
			Long id,Byte attachmentType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2ConfigAttachmentsRecord> step = context
				.delete(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS);
		Condition condition = Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.OWNER_TYPE
				.equal(ownerType).and(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.OWNER_ID
				.equal(id)).and(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.RESOURCE_TYPE
						.equal(resourceType));
		 if (attachmentType!=null)
		 	condition = condition.and(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.ATTACHMENT_TYPE.equal(attachmentType));
		step.where(condition);
		Integer deleteCount = step.execute();
		return deleteCount;
		
	}

	@Override
	public Integer deleteRentalDayopenTimeByOwnerId(String resourceType, String ownerType, Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2DayopenTimeRecord > step = context.delete(Tables.EH_RENTALV2_DAYOPEN_TIME);
		Condition condition = Tables.EH_RENTALV2_DAYOPEN_TIME.OWNER_TYPE.eq(ownerType)
				.and(Tables.EH_RENTALV2_DAYOPEN_TIME.OWNER_ID.eq(id)).and(Tables.EH_RENTALV2_DAYOPEN_TIME.RESOURCE_TYPE.eq(resourceType));
		step.where(condition);
		Integer deleteCount = step.execute();
		return deleteCount;
	}

	@Override
	public List<RentalSiteRange> findRentalSiteOwnersBySiteId(String resourceType, Long siteId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_RESOURCE_RANGES);
		Condition condition = Tables.EH_RENTALV2_RESOURCE_RANGES.RENTAL_RESOURCE_ID
				.equal(siteId); 
		step.where(condition);
		List<RentalSiteRange> result = step
				.orderBy(Tables.EH_RENTALV2_RESOURCE_RANGES.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalSiteRange.class);
				});
		if (null != result && result.size() > 0)
			return result ;
		return null;
	}

	@Override
	public Long createRentalRefundOrder(RentalRefundOrder rentalRefundOrder) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2RefundOrders.class));
		rentalRefundOrder.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2RefundOrdersRecord record = ConvertHelper.convert(rentalRefundOrder,
				EhRentalv2RefundOrdersRecord.class);
		InsertQuery<EhRentalv2RefundOrdersRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_REFUND_ORDERS);
		query.setRecord(record);
		query.execute();

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2ResourceRanges.class, null);
		return id;
	}

	@Override
	public RentalRefundOrder getRentalRefundOrderById(Long rentalRefundOrderId) {
		 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite()); 
		EhRentalv2RefundOrdersDao dao = new EhRentalv2RefundOrdersDao(context.configuration());
		EhRentalv2RefundOrders order = dao.findById(rentalRefundOrderId);
		return ConvertHelper.convert(order, RentalRefundOrder.class);
	}

	@Override
	public void updateRentalRefundOrder(RentalRefundOrder rentalRefundOrder) {
		 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite()); 
		EhRentalv2RefundOrdersDao dao = new EhRentalv2RefundOrdersDao(context.configuration());
		dao.update(rentalRefundOrder); 

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRentalv2RefundOrders.class,rentalRefundOrder.getId());
	}

	@Override
	public List<RentalRefundOrder> getRefundOrderList(String resourceType, Long resourceTypeId,
			CrossShardListingLocator locator, Byte status, String styleNo,
			int pageSize, Long startTime, Long endTime) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_REFUND_ORDERS); 
		Condition condition = Tables.EH_RENTALV2_REFUND_ORDERS.ID.greaterOrEqual(0L);
		condition = condition.and(Tables.EH_RENTALV2_REFUND_ORDERS.RESOURCE_TYPE
				.equal(resourceType));
		if (StringUtils.isNotEmpty(styleNo))
			condition = condition.and(Tables.EH_RENTALV2_REFUND_ORDERS.ONLINE_PAY_STYLE_NO
					.equal(styleNo));
		if(null!=resourceTypeId)
			condition = condition.and(Tables.EH_RENTALV2_REFUND_ORDERS.RESOURCE_TYPE_ID 
					.equal(resourceTypeId)); 
		if (null != endTime)
			condition = condition.and(Tables.EH_RENTALV2_REFUND_ORDERS.CREATE_TIME
					.lessThan(new Timestamp(endTime)));
		if (null != startTime)
			condition = condition.and(Tables.EH_RENTALV2_REFUND_ORDERS.CREATE_TIME
					.greaterThan(new Timestamp(startTime)));
		 
		if (null != status)
			condition = condition.and(Tables.EH_RENTALV2_REFUND_ORDERS.STATUS
					.equal(status));    
		if(null!=locator && locator.getAnchor() != null)
			condition=condition.and(Tables.EH_RENTALV2_REFUND_ORDERS.ID.lt(locator.getAnchor()));
								
		step.limit(pageSize);
		step.where(condition);
		List<RentalRefundOrder> result = step
				.orderBy(Tables.EH_RENTALV2_REFUND_ORDERS.CREATE_TIME.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalRefundOrder.class);
				});
		
		if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query rental refund orders, sql=" + step.getSQL());
            LOGGER.debug("Query rental refund orders, bindValues=" + step.getBindValues());
        }
		if(result==null || result.size()==0)
			return null;
		return result;
	}

	@Override
	public RentalRefundOrder getRentalRefundOrderByRefundNo(
			String refundOrderNo) {
		 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_REFUND_ORDERS); 
		Condition condition = Tables.EH_RENTALV2_REFUND_ORDERS.REFUND_ORDER_NO.eq(Long.valueOf(refundOrderNo)); 
		step.where(condition);
		RentalRefundOrder result = step
				.orderBy(Tables.EH_RENTALV2_REFUND_ORDERS.ID.desc()).fetchOne().map((r) -> {
					return ConvertHelper.convert(r, RentalRefundOrder.class);
				});
		return result;
		
	}

    @Override
    public RentalRefundOrder getRentalRefundOrderByOrderNo(String orderNo) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectJoinStep<Record> step = context.select().from(
                Tables.EH_RENTALV2_REFUND_ORDERS);
        Condition condition = Tables.EH_RENTALV2_REFUND_ORDERS.ORDER_NO.eq(Long.valueOf(orderNo));
        step.where(condition);
        RentalRefundOrder result = step
                .orderBy(Tables.EH_RENTALV2_REFUND_ORDERS.ID.desc()).fetchOne().map((r) -> {
                    return ConvertHelper.convert(r, RentalRefundOrder.class);
                });
        return result;
    }

    @Override
	public RentalResourceType getRentalResourceTypeById(Long rentalResourceTypeId) {
		 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite()); 
		EhRentalv2ResourceTypesDao dao = new EhRentalv2ResourceTypesDao(context.configuration());
		EhRentalv2ResourceTypes order = dao.findById(rentalResourceTypeId);
		return ConvertHelper.convert(order, RentalResourceType.class);
	}

	@Override
	public RentalCell getRentalCellById(Long cellId,Long rentalSiteId,Byte rentalType,String resourceType) {
		 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhRentalv2CellsRecord> query = context.selectQuery(Tables.EH_RENTALV2_CELLS);
		query.addConditions(Tables.EH_RENTALV2_CELLS.CELL_ID.eq(cellId).and(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.eq(rentalSiteId))
		.and(Tables.EH_RENTALV2_CELLS.RENTAL_TYPE.eq(rentalType)).and(Tables.EH_RENTALV2_CELLS.RESOURCE_TYPE.eq(resourceType)));
		EhRentalv2CellsRecord record = query.fetchOne();
		if (record == null)
			return null;
		RentalCell cell = ConvertHelper.convert(record, RentalCell.class);
		Long tmp = cell.getId();
		cell.setId(cell.getCellId());
		cell.setCellId(tmp);
		return cell;
	}

	@Override
	public List<RentalCell> getRentalCellsByIds(String resourceType,List<Long> cellIds, Long rentalSiteId,Byte rentalType) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
//		EhRentalv2CellsDao dao = new EhRentalv2CellsDao(context.configuration());
//		EhRentalv2Cells order = dao.findById(cellId);
		SelectQuery<EhRentalv2CellsRecord> query = context.selectQuery(Tables.EH_RENTALV2_CELLS);
		query.addConditions(Tables.EH_RENTALV2_CELLS.CELL_ID.in(cellIds).and(Tables.EH_RENTALV2_CELLS.RESOURCE_TYPE.eq(resourceType))
		.and(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.eq(rentalSiteId).and(Tables.EH_RENTALV2_CELLS.RENTAL_TYPE.eq(rentalType))));


		return query.fetch().map(r -> { //cell 的id不再作为绝对值 改为根据资源id区分的相对值
			RentalCell convert = ConvertHelper.convert(r, RentalCell.class);
			Long tmp = convert.getId();
			convert.setId(convert.getCellId());
			convert.setCellId(tmp);
			return convert;
		});
	}

	@Override
	public List<RentalCell> getRentalCellsByRange(String resourceType,Long minId,Long maxId, Long rentalSiteId,Byte rentalType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		SelectQuery<EhRentalv2CellsRecord> query = context.selectQuery(Tables.EH_RENTALV2_CELLS);
		query.addConditions(Tables.EH_RENTALV2_CELLS.CELL_ID.ge(minId));
		query.addConditions(Tables.EH_RENTALV2_CELLS.CELL_ID.le(maxId));
		query.addConditions(Tables.EH_RENTALV2_CELLS.RESOURCE_TYPE.eq(resourceType)
				.and(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.eq(rentalSiteId).and(Tables.EH_RENTALV2_CELLS.RENTAL_TYPE.eq(rentalType))));
		return query.fetch().map(r -> {
			RentalCell convert = ConvertHelper.convert(r, RentalCell.class);
			Long tmp = convert.getId();
			convert.setId(convert.getCellId());
			convert.setCellId(tmp);
			return convert;
		});
	}

	@Override
	public void createRentalResourceType(RentalResourceType rentalResourceType) {
		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhRentalv2ResourceTypes.class));
		rentalResourceType.setId(id); 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2ResourceTypesRecord record = ConvertHelper.convert(rentalResourceType,
				EhRentalv2ResourceTypesRecord.class);
		InsertQuery<EhRentalv2ResourceTypesRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_RESOURCE_TYPES);
		query.setRecord(record);
		query.execute();

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2ResourceTypes.class, null);
		 
		 
	}

	@Override
	public void updateRentalResourceType(RentalResourceType resourceType) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite()); 
		EhRentalv2ResourceTypesDao dao = new EhRentalv2ResourceTypesDao(context.configuration());
		dao.update(resourceType); 

		DaoHelper.publishDaoAction(DaoAction.MODIFY, RentalResourceType.class,resourceType.getId());
	}

	@Override
	public RentalResourceType findRentalResourceTypeById (Long resourceTypeId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhRentalv2ResourceTypesDao dao = new EhRentalv2ResourceTypesDao(context.configuration());
		return ConvertHelper.convert(dao.findById(resourceTypeId), RentalResourceType.class);
	}

	@Override
	public List<RentalResourceType> findRentalResourceTypes(Integer namespaceId, Byte menuType, String resourceType,
															ListingLocator locator) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_RESOURCE_TYPES);

		Condition condition = Tables.EH_RENTALV2_RESOURCE_TYPES.STATUS.equal(ResourceTypeStatus.NORMAL.getCode());

		if(null != namespaceId){
			condition = condition.and(Tables.EH_RENTALV2_RESOURCE_TYPES.NAMESPACE_ID.equal(namespaceId));
		}

		if (StringUtils.isNotBlank(resourceType)) {
			condition = condition.and(Tables.EH_RENTALV2_RESOURCE_TYPES.IDENTIFY.equal(resourceType));
		}

		if (null != menuType) {
			condition = condition.and(Tables.EH_RENTALV2_RESOURCE_TYPES.MENU_TYPE.equal(menuType));
		}

		step.where(condition);

		List<RentalResourceType> result = step
				.orderBy(Tables.EH_RENTALV2_RESOURCE_TYPES.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalResourceType.class);
				});
		if (null != result && result.size() > 0)
			return result ;
		return null;

	}

	@Override
	public RentalResourceType findRentalResourceTypes(Integer namespaceId, String resourceType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_RESOURCE_TYPES);

		Condition condition = Tables.EH_RENTALV2_RESOURCE_TYPES.STATUS.equal(ResourceTypeStatus.NORMAL.getCode());

		if(null != namespaceId){
			condition = condition.and(Tables.EH_RENTALV2_RESOURCE_TYPES.NAMESPACE_ID.equal(namespaceId));
		}

		if (StringUtils.isNotBlank(resourceType)) {
			condition = condition.and(Tables.EH_RENTALV2_RESOURCE_TYPES.IDENTIFY.equal(resourceType));
		}

		step.where(condition);
		Record record = step.fetchAny();
		if (record == null)
			return null;

		return ConvertHelper.convert(record, RentalResourceType.class);

	}

	@Override
	public void createRentalResourceNumber(RentalResourceNumber resourceNumber) {

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2ResourceNumbers.class));
		resourceNumber.setId(id);	 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2ResourceNumbersRecord record = ConvertHelper.convert(resourceNumber,
				EhRentalv2ResourceNumbersRecord.class);
		InsertQuery<EhRentalv2ResourceNumbersRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_RESOURCE_NUMBERS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2ResourceNumbers.class,
				null); 
	}

	@Override
	public Integer deleteRentalResourceNumbersByOwnerId(String resourceType, String ownerType, Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2ResourceNumbersRecord> step = context
				.delete(Tables.EH_RENTALV2_RESOURCE_NUMBERS);
		Condition condition = Tables.EH_RENTALV2_RESOURCE_NUMBERS.OWNER_TYPE
				.equal(ownerType).and(Tables.EH_RENTALV2_RESOURCE_NUMBERS.OWNER_ID
						.equal(ownerId)).and(Tables.EH_RENTALV2_RESOURCE_NUMBERS.RESOURCE_TYPE
						.equal(resourceType));
		 
		step.where(condition);
		Integer deleteCount = step.execute();
		return deleteCount;
	}

	@Override
	public List<RentalResourceNumber> queryRentalResourceNumbersByOwner(
			String resourceType, String ownerType, Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_RESOURCE_NUMBERS);
		Condition condition = Tables.EH_RENTALV2_RESOURCE_NUMBERS.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTALV2_RESOURCE_NUMBERS.OWNER_TYPE
				.equal(ownerType));
		condition = condition.and(Tables.EH_RENTALV2_RESOURCE_NUMBERS.RESOURCE_TYPE
				.equal(resourceType));
		step.where(condition);
		List<RentalResourceNumber> result = step
				.orderBy(Tables.EH_RENTALV2_RESOURCE_NUMBERS.ID.asc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalResourceNumber.class);
				});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}
 
	@Override
	public void deleteRentalCellsByResourceId(String resourceType, Long rentalSiteId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2CellsRecord> step = context
				.delete(Tables.EH_RENTALV2_CELLS);
		Condition condition = Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.equal(rentalSiteId)
								.and(Tables.EH_RENTALV2_CELLS.RESOURCE_TYPE.equal(resourceType));
		step.where(condition);
		step.execute();
	} 

	/**
	 * 金地同步数据使用
	 */
	@Override
	public List<RentalOrder> listSiteRentalByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor,
			int pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> result = context.select(Tables.EH_RENTALV2_ORDERS.fields()).from(Tables.EH_RENTALV2_ORDERS)
			.join(Tables.EH_ORGANIZATIONS)
			.on(Tables.EH_RENTALV2_ORDERS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATIONS.ID))
			.and(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId))
			.where(Tables.EH_RENTALV2_ORDERS.OPERATE_TIME.eq(new Timestamp(timestamp)))
			.and(Tables.EH_RENTALV2_ORDERS.STATUS.in(SiteBillStatus.SUCCESS.getCode(), SiteBillStatus.COMPLETE.getCode(), SiteBillStatus.OVERTIME.getCode()))
			.and(Tables.EH_RENTALV2_ORDERS.ID.gt(pageAnchor))
			.orderBy(Tables.EH_RENTALV2_ORDERS.ID.asc())
			.limit(pageSize)
			.fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->RecordHelper.convert(r, RentalOrder.class));
		}
		return new ArrayList<RentalOrder>();
	}

	/**
	 * 金地同步数据使用
	 */
	@Override
	public List<RentalOrder> listSiteRentalByUpdateTime(Integer namespaceId, Long timestamp, int pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> result = context.select(Tables.EH_RENTALV2_ORDERS.fields()).from(Tables.EH_RENTALV2_ORDERS)
			.join(Tables.EH_ORGANIZATIONS)
			.on(Tables.EH_RENTALV2_ORDERS.ORGANIZATION_ID.eq(Tables.EH_ORGANIZATIONS.ID))
			.and(Tables.EH_ORGANIZATIONS.NAMESPACE_ID.eq(namespaceId))
			.where(Tables.EH_RENTALV2_ORDERS.OPERATE_TIME.gt(new Timestamp(timestamp)))
			.and(Tables.EH_RENTALV2_ORDERS.STATUS.in(SiteBillStatus.SUCCESS.getCode(), SiteBillStatus.COMPLETE.getCode(), SiteBillStatus.OVERTIME.getCode()))
			.orderBy(Tables.EH_RENTALV2_ORDERS.OPERATE_TIME.asc(), Tables.EH_RENTALV2_ORDERS.ID.asc())
			.limit(pageSize)
			.fetch();
			
		if (result != null && result.isNotEmpty()) {
			return result.map(r->RecordHelper.convert(r, RentalOrder.class));
		}
		return new ArrayList<RentalOrder>();
	}

	@Override
	public MaxMinPrice findMaxMinPrice(String resourceType,Long resourceId, Byte rentalType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Record record = context.select(DSL.max(Tables.EH_RENTALV2_CELLS.PRICE), DSL.min(Tables.EH_RENTALV2_CELLS.PRICE),
				DSL.max(Tables.EH_RENTALV2_CELLS.ORG_MEMBER_PRICE), DSL.min(Tables.EH_RENTALV2_CELLS.ORG_MEMBER_PRICE),
				DSL.max(Tables.EH_RENTALV2_CELLS.APPROVING_USER_PRICE), DSL.min(Tables.EH_RENTALV2_CELLS.APPROVING_USER_PRICE)
				)
			.from(Tables.EH_RENTALV2_CELLS)
			.where(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.eq(resourceId))
			.and(Tables.EH_RENTALV2_CELLS.RENTAL_TYPE.eq(rentalType))
				.and(Tables.EH_RENTALV2_CELLS.RESOURCE_TYPE.eq(resourceType))
			.and(Tables.EH_RENTALV2_CELLS.STATUS.eq(RentalSiteStatus.NORMAL.getCode()))
			.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE.ge(new Date(new java.util.Date().getTime())))
			.fetchOne();
		if (record != null) {
			BigDecimal maxPrice = record.getValue(DSL.max(Tables.EH_RENTALV2_CELLS.PRICE));
			BigDecimal minPrice = maxPrice;
			BigDecimal maxOrgMemberPrice = record.getValue(DSL.max(Tables.EH_RENTALV2_CELLS.ORG_MEMBER_PRICE));
			BigDecimal minOrgMemberPrice = maxOrgMemberPrice;
			BigDecimal maxApprovingUserPrice = record.getValue(DSL.max(Tables.EH_RENTALV2_CELLS.APPROVING_USER_PRICE));
			BigDecimal minApprovingUserPrice = maxApprovingUserPrice;
			return new MaxMinPrice(maxPrice, minPrice, maxOrgMemberPrice, minOrgMemberPrice, maxApprovingUserPrice, minApprovingUserPrice);
		}
		return null;
	}

	@Override
	public List<Long> listCellPackageId(String resourceType, Long resourceId, Byte rentalType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		return context.select(Tables.EH_RENTALV2_CELLS.PRICE_PACKAGE_ID).from(Tables.EH_RENTALV2_CELLS)
				.where(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.eq(resourceId))
				.and(Tables.EH_RENTALV2_CELLS.RESOURCE_TYPE.eq(resourceType))
				.and(Tables.EH_RENTALV2_CELLS.RENTAL_TYPE.eq(rentalType))
				.and(Tables.EH_RENTALV2_CELLS.STATUS.eq(RentalSiteStatus.NORMAL.getCode()))
				.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE.ge(new Date(new java.util.Date().getTime())))
				.fetch().map(r->r.value1());
	}

	@Override
	public void setAuthDoorId(Long orderId, String AuthDoorId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		context.update(Tables.EH_RENTALV2_ORDERS).set(Tables.EH_RENTALV2_ORDERS.DOOR_AUTH_ID,AuthDoorId).where(
				Tables.EH_RENTALV2_ORDERS.ID.eq(orderId)
		).execute();
	}
	private BigDecimal max(BigDecimal ... b) {
		BigDecimal max = new BigDecimal(Integer.MIN_VALUE);
		for (BigDecimal bigDecimal : b) {
			max = maxBig(max, bigDecimal);
		}
		return max;
	}
	
	private BigDecimal maxBig(BigDecimal b1, BigDecimal b2) {
		if (b2 != null && b2.compareTo(b1) > 0) {
			return b2;
		}
		return b1;
	}

	private BigDecimal min(BigDecimal ... b) {
		BigDecimal min = new BigDecimal(Integer.MAX_VALUE);
		for (BigDecimal bigDecimal : b) {
			min = minBig(min, bigDecimal);
		}
		return min;
	}
	
	private BigDecimal minBig(BigDecimal b1, BigDecimal b2) {
		if (b2 != null && b2.compareTo(b1) < 0) {
			return b2;
		}
		return b1;
	}


	@Override
	public void createRentalOrderRule(RentalOrderRule rule) {

		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhRentalv2OrderRules.class));

		rule.setId(id);
		rule.setCreateTime(new Timestamp(System.currentTimeMillis()));
		rule.setCreatorUid(UserContext.currentUserId());

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2OrderRulesDao dao = new EhRentalv2OrderRulesDao(context.configuration());
		dao.insert(rule);

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2OrderRules.class,null);

	}

	@Override
	public void deleteRentalOrderRules(String resourceType, String ownerType, Long ownerId, Byte handleType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		context.delete(Tables.EH_RENTALV2_ORDER_RULES)
				.where(Tables.EH_RENTALV2_ORDER_RULES.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_RENTALV2_ORDER_RULES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_RENTALV2_ORDER_RULES.HANDLE_TYPE.eq(handleType))
				.and(Tables.EH_RENTALV2_ORDER_RULES.RESOURCE_TYPE.eq(resourceType))
				.execute();
	}

	@Override
	public List<RentalOrderRule> listRentalOrderRules(String resourceType, String ownerType, Long ownerId, Byte handleType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		return context.select().from(Tables.EH_RENTALV2_ORDER_RULES)
				.where(Tables.EH_RENTALV2_ORDER_RULES.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_RENTALV2_ORDER_RULES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_RENTALV2_ORDER_RULES.HANDLE_TYPE.eq(handleType))
				.and(Tables.EH_RENTALV2_ORDER_RULES.RESOURCE_TYPE.eq(resourceType))
				.orderBy(Tables.EH_RENTALV2_ORDER_RULES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, RentalOrderRule.class));
	}

	@Override
	public List<String> listOverTimeSpaces(Integer namespaceId, Long resourceTypeId, String resourceType,
											 Long rentalSiteId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record1<String>> query = context.select(Tables.EH_RENTALV2_ORDERS.STRING_TAG1).from(
				Tables.EH_RENTALV2_ORDERS);

		Condition condition = Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE.equal(resourceType);
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_RESOURCE_ID.equal(rentalSiteId));
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.NAMESPACE_ID.eq(namespaceId));
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE_ID.eq(resourceTypeId));
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS.eq(SiteBillStatus.IN_USING.getCode()));
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.END_TIME.lt(new Timestamp(System.currentTimeMillis())));

		return query.where(condition).fetchInto(String.class);
	}

	@Override
	public List<String> listParkingNoInUsed(Integer namespaceId, Long resourceTypeId, String resourceType,
											Long rentalSiteId,List<Long> cellIds){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record1<String>> query = context.select(Tables.EH_RENTALV2_ORDERS.STRING_TAG1).from(Tables.EH_RENTALV2_ORDERS).
				join(Tables.EH_RENTALV2_RESOURCE_ORDERS).on(Tables.EH_RENTALV2_ORDERS.ID.eq(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_ORDER_ID));
		Condition condition = Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE.equal(resourceType);
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_RESOURCE_ID.equal(rentalSiteId));
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.NAMESPACE_ID.eq(namespaceId));
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE_ID.eq(resourceTypeId));
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS.eq(SiteBillStatus.IN_USING.getCode()).
				or(Tables.EH_RENTALV2_ORDERS.STATUS.eq(SiteBillStatus.SUCCESS.getCode())));
		condition = condition.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID.in(cellIds));

		return query.where(condition).fetchInto(String.class);

	}


	@Override
	public List<RentalOrder> listOverTimeRentalOrders(Integer namespaceId, Long resourceTypeId, String resourceType,
													  Long rentalSiteId, String spaceNo){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_RENTALV2_ORDERS);

		Condition condition = Tables.EH_RENTALV2_ORDERS.STATUS.eq(SiteBillStatus.IN_USING.getCode());

		if (StringUtils.isNotBlank(resourceType))
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE.equal(resourceType));
		if (StringUtils.isNotBlank(spaceNo))
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.STRING_TAG1.equal(spaceNo));
		if(null != resourceTypeId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE_ID.equal(resourceTypeId));
		if (null != rentalSiteId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_RESOURCE_ID.equal(rentalSiteId));

		step.where(condition);

		return step.fetch().map((r) -> ConvertHelper.convert(r, RentalOrder.class));
	}

	@Override
	public String getHolidayCloseDate(Byte holidayType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_RENTALV2_HOLIDAY);
		Condition condition = Tables.EH_RENTALV2_HOLIDAY.HOLIDAY_TYPE.eq(holidayType);
		Record record = step.where(condition).fetchOne();
		if (record == null)
			return  null;
		return record.getValue("close_date",String.class);
	}

	@Override
	public void createRefundTip(RentalRefundTip tip) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2RefundTips.class));
		tip.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2RefundTipsRecord record = ConvertHelper.convert(tip,
				EhRentalv2RefundTipsRecord.class);
		InsertQuery<EhRentalv2RefundTipsRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_REFUND_TIPS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2RefundTips.class,
				null);
	}

	@Override
	public void deleteRefundTip(String resourceType, String sourceType, Long sourceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());

		context.delete(Tables.EH_RENTALV2_REFUND_TIPS)
				.where(Tables.EH_RENTALV2_REFUND_TIPS.SOURCE_TYPE.eq(sourceType))
				.and(Tables.EH_RENTALV2_REFUND_TIPS.SOURCE_ID.eq(sourceId))
				.and(Tables.EH_RENTALV2_REFUND_TIPS.RESOURCE_TYPE.eq(resourceType))
				.execute();
	}

	@Override
	public List<RentalRefundTip> listRefundTips(String resourceType, String sourceType, Long sourceId, Byte refundStrategy) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_RENTALV2_REFUND_TIPS);
		Condition condition = Tables.EH_RENTALV2_REFUND_TIPS.RESOURCE_TYPE.eq(resourceType);
		if (!StringUtils.isBlank(sourceType))
			condition = condition.and(Tables.EH_RENTALV2_REFUND_TIPS.SOURCE_TYPE.eq(sourceType));
		if (sourceId != null)
			condition = condition.and(Tables.EH_RENTALV2_REFUND_TIPS.SOURCE_ID.eq(sourceId));
		if (refundStrategy != null)
			condition = condition.and(Tables.EH_RENTALV2_REFUND_TIPS.REFUND_STRATEGY.eq(refundStrategy));
		step.where(condition);

		return step.fetch().map(r-> ConvertHelper.convert(r,RentalRefundTip.class));
	}

	@Override
	public List<RentalStructure> listRentalStructures(String sourceType, Long sourceId, String resourceType,
													  ListingLocator locator, Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_RENTALV2_STRUCTURES);
		Condition condition = Tables.EH_RENTALV2_STRUCTURES.RESOURCE_TYPE.eq(resourceType);
		if (!StringUtils.isBlank(sourceType))
			condition = condition.and(Tables.EH_RENTALV2_STRUCTURES.SOURCE_TYPE.eq(sourceType));
		if (sourceId != null)
			condition = condition.and(Tables.EH_RENTALV2_STRUCTURES.SOURCE_ID.eq(sourceId));
		if (locator !=null && locator.getAnchor() != null)
			condition.and(Tables.EH_RENTALV2_STRUCTURES.DEFAULT_ORDER.gt(locator.getAnchor()));
		step.where(condition).orderBy(Tables.EH_RENTALV2_STRUCTURES.DEFAULT_ORDER);
		if (pageSize != null)
			step.limit(pageSize);

		return step.fetch().map(r->ConvertHelper.convert(r,RentalStructure.class));
	}

	@Override
	public List<RentalStructureTemplate> listRentalStructureTemplates() {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_RENTALV2_STRUCTURE_TEMPLATE);
		return step.fetch().map(r->ConvertHelper.convert(r,RentalStructureTemplate.class));
	}

	@Override
	public RentalStructure getRentalStructureById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_RENTALV2_STRUCTURES);
		return step.where(Tables.EH_RENTALV2_STRUCTURES.ID.eq(id)).fetchAny().map(r->ConvertHelper.convert(r,RentalStructure.class));
	}

	@Override
	public void createRentalStructure(RentalStructure rentalStructure) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2Structures.class));
		rentalStructure.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2StructuresRecord record = ConvertHelper.convert(rentalStructure,
				EhRentalv2StructuresRecord.class);
		InsertQuery<EhRentalv2StructuresRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_STRUCTURES);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, RentalStructure.class,
				null);
	}

	@Override
	public void updateRentalStructure(RentalStructure rentalStructure) {
		assert (rentalStructure.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2StructuresDao dao = new EhRentalv2StructuresDao(context.configuration());
		dao.update(rentalStructure);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, RentalStructure.class,
				rentalStructure.getId());
	}
}
