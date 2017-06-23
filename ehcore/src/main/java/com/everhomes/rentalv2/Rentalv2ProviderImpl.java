package com.everhomes.rentalv2;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder.Case;

import com.everhomes.rest.rentalv2.admin.ResourceTypeStatus;
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
import com.everhomes.organization.Organization;
import com.everhomes.rest.rentalv2.DateLength;
import com.everhomes.rest.rentalv2.MaxMinPrice;
import com.everhomes.rest.rentalv2.RentalSiteStatus;
import com.everhomes.rest.rentalv2.RentalTimeIntervalOwnerType;
import com.everhomes.rest.rentalv2.RentalType;
import com.everhomes.rest.rentalv2.ResourceOrderStatus;
import com.everhomes.rest.rentalv2.SiteBillStatus;
import com.everhomes.rest.rentalv2.VisibleFlag;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhRentalv2ResourceRanges;
import com.everhomes.server.schema.tables.daos.EhRentalv2CellsDao;
import com.everhomes.server.schema.tables.daos.EhRentalv2DefaultRulesDao;
import com.everhomes.server.schema.tables.daos.EhRentalv2ItemsDao;
import com.everhomes.server.schema.tables.daos.EhRentalv2OrderPayorderMapDao;
import com.everhomes.server.schema.tables.daos.EhRentalv2OrdersDao;
import com.everhomes.server.schema.tables.daos.EhRentalv2RefundOrdersDao;
import com.everhomes.server.schema.tables.daos.EhRentalv2ResourceRangesDao;
import com.everhomes.server.schema.tables.daos.EhRentalv2ResourceTypesDao;
import com.everhomes.server.schema.tables.daos.EhRentalv2ResourcesDao;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Cells;
import com.everhomes.server.schema.tables.pojos.EhRentalv2CloseDates;
import com.everhomes.server.schema.tables.pojos.EhRentalv2ConfigAttachments;
import com.everhomes.server.schema.tables.pojos.EhRentalv2DefaultRules;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Items;
import com.everhomes.server.schema.tables.pojos.EhRentalv2ItemsOrders;
import com.everhomes.server.schema.tables.pojos.EhRentalv2OrderAttachments;
import com.everhomes.server.schema.tables.pojos.EhRentalv2OrderPayorderMap;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Orders;
import com.everhomes.server.schema.tables.pojos.EhRentalv2RefundOrders;
import com.everhomes.server.schema.tables.pojos.EhRentalv2ResourceNumbers;
import com.everhomes.server.schema.tables.pojos.EhRentalv2ResourceOrders;
import com.everhomes.server.schema.tables.pojos.EhRentalv2ResourcePics;
import com.everhomes.server.schema.tables.pojos.EhRentalv2ResourceTypes;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Resources;
import com.everhomes.server.schema.tables.pojos.EhRentalv2TimeInterval;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.records.EhRentalv2CellsRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2CloseDatesRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2ConfigAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2DefaultRulesRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2ItemsOrdersRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2ItemsRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2OrderAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2OrderPayorderMapRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2OrdersRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2RefundOrdersRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2ResourceNumbersRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2ResourceOrdersRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2ResourcePicsRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2ResourceRangesRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2ResourceTypesRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2ResourcesRecord;
import com.everhomes.server.schema.tables.records.EhRentalv2TimeIntervalRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RecordHelper;

import freemarker.core.ArithmeticEngine.BigDecimalEngine;

@Component
public class Rentalv2ProviderImpl implements Rentalv2Provider {
    private static final Logger LOGGER = LoggerFactory.getLogger(Rentalv2ProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;
 

	@Override
	public Long createRentalSite(RentalResource rentalsite) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2Resources.class));
		rentalsite.setId(id);
		rentalsite.setDefaultOrder(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2ResourcesRecord record = ConvertHelper.convert(rentalsite,
				EhRentalv2ResourcesRecord.class);
		InsertQuery<EhRentalv2ResourcesRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_RESOURCES);
		query.setRecord(record);
		query.execute();

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2Resources.class, null);
		return id;
	}

	@Override
	public List<RentalItem> findRentalSiteItems(Long rentalSiteId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ITEMS);
		Condition condition = Tables.EH_RENTALV2_ITEMS.RENTAL_RESOURCE_ID
				.equal(rentalSiteId);
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
//		long id = sequenceProvider.getNextSequence(NameMapper
//				.getSequenceDomainFromTablePojo(EhRentalv2Cells.class));
//		rsr.setId(id);
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
	public List<RentalCell> findRentalSiteRuleByDate(Long rentalSiteId,
			String siteNumber, Timestamp beginTime, Timestamp endTime,
			List<Byte> ampmList, String rentalDate) throws ParseException {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_CELLS);
		Condition condition = Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.equal(rentalSiteId);
		if (null != siteNumber) {
			condition = condition.and(Tables.EH_RENTALV2_CELLS.RESOURCE_NUMBER.equal(siteNumber));
		}

		if (null != beginTime) {
			condition = condition.and(Tables.EH_RENTALV2_CELLS.BEGIN_TIME.greaterOrEqual(beginTime));
		}
		if (null != endTime) {
			condition = condition.and(Tables.EH_RENTALV2_CELLS.END_TIME.lessOrEqual(endTime));
		}
		if(null!=ampmList){
			condition = condition.and(Tables.EH_RENTALV2_CELLS.AMORPM.in(ampmList));
		}
		if(null!=rentalDate){

			SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
			condition = condition.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE.eq(new Date(dateSF.parse(rentalDate).getTime())));
		}
		step.where(condition);
		List<RentalCell> result = step
				.orderBy(Tables.EH_RENTALV2_CELLS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalCell.class);
				});

		return result;
	}

	@Override
	public List<RentalCell> findRentalCellBetweenDates(Long rentalSiteId,String beginTime, String   endTime) throws ParseException {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_CELLS);
		Condition condition = Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.equal(rentalSiteId);
	

		if (null != beginTime) {
			condition = condition.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE.greaterOrEqual(new Date(dateSF.parse(beginTime).getTime())));
		}
		if (null != endTime) {
			condition = condition.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE.lessOrEqual(new Date(dateSF.parse(endTime).getTime())));
		} 
		step.where(condition);
		List<RentalCell> result = step
				.orderBy(Tables.EH_RENTALV2_CELLS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalCell.class);
				});

		if (null != result && result.size() > 0)
			return result;
		return null;
	}
	
	
	@Override
	public List<RentalCell> findRentalSiteRules(Long rentalSiteId,
			String ruleDate, Timestamp beginDate, Byte rentalType, Byte dateLength,Byte status) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_CELLS);
		Condition condition = Tables.EH_RENTALV2_CELLS.RENTAL_TYPE
				.eq(rentalType);
		if(null!=status)
			condition = condition
					.and(Tables.EH_RENTALV2_CELLS.STATUS
				.equal(status));
		else
			condition = condition
					.and(Tables.EH_RENTALV2_CELLS.STATUS
			.equal(RentalSiteStatus.NORMAL.getCode()));
		
		if (null != ruleDate) {
			if (dateLength.equals(DateLength.DAY.getCode())) {
				condition = condition
						.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE
						.equal(Date.valueOf(ruleDate)));
			} else if (dateLength.equals(DateLength.MONTH.getCode())) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(Date.valueOf(ruleDate));
				// month begin
				calendar.set(Calendar.DAY_OF_MONTH,
						calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
				condition = condition
						.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE
								.greaterOrEqual(new java.sql.Date(calendar
										.getTime().getTime())));

				// month end
				calendar.set(Calendar.DAY_OF_MONTH,
						calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				condition = condition
						.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE
								.lessOrEqual(new java.sql.Date(calendar
										.getTime().getTime())));

			}else if (dateLength.equals(DateLength.WEEK.getCode())) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(Date.valueOf(ruleDate));
				// month begin
				calendar.set(Calendar.DAY_OF_WEEK,
						calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
				condition = condition
						.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE
								.greaterOrEqual(new java.sql.Date(calendar
										.getTime().getTime())));

				// month end
				calendar.set(Calendar.DAY_OF_WEEK,
						calendar.getActualMaximum(Calendar.DAY_OF_WEEK));
				condition = condition
						.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE
								.lessOrEqual(new java.sql.Date(calendar
										.getTime().getTime())));

			}
		}
		if (null != rentalSiteId) {
			condition = condition
					.and(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID
							.equal(rentalSiteId));
		}

		if (null != beginDate && rentalType.equals(RentalType.HOUR.getCode())) {
//		if (null != beginDate) {
			condition = condition.and(Tables.EH_RENTALV2_CELLS.BEGIN_TIME
					.lessOrEqual(beginDate));

		}
		step.where(condition);
		List<RentalCell> result = step
				.orderBy(Tables.EH_RENTALV2_CELLS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalCell.class);
				});

		return result;
	}

	@Override
	public RentalCell findRentalSiteRuleById(Long siteRuleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_CELLS);
		Condition condition = Tables.EH_RENTALV2_CELLS.ID.equal(siteRuleId);
		step.where(condition);
		List<RentalCell> result = step
				.orderBy(Tables.EH_RENTALV2_CELLS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalCell.class);
				});
		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public Long createRentalOrder(RentalOrder rentalBill) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2Orders.class));
		rentalBill.setId(id);
		//生成订单编号
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
		String numberEnd = String.valueOf(id%1000);
		if(numberEnd.length()==1)
			numberEnd="00"+numberEnd;
		if(numberEnd.length()==2)
			numberEnd="0"+numberEnd;
		String orderNo = sdf.format(new java.util.Date())+numberEnd;
		rentalBill.setOrderNo(orderNo);
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
	public Long createRentalItemBill(RentalItemsOrder rib) {

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2ItemsOrders.class));
		rib.setId(id);
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
	public List<RentalResourceOrder> findRentalSiteBillBySiteRuleId(Long siteRuleId) {
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
	public Double countRentalSiteBillBySiteRuleId(Long cellId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Record1<BigDecimal> result = context.select(DSL.sum(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_COUNT))
			.from(Tables.EH_RENTALV2_RESOURCE_ORDERS)
			.join(Tables.EH_RENTALV2_ORDERS)
			.on(Tables.EH_RENTALV2_ORDERS.ID.eq(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_ORDER_ID))
			.where(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID.equal(cellId))
			.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.FAIL.getCode()))
			.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.REFUNDED.getCode()))
			.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.REFUNDING.getCode()))
			.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.APPROVING.getCode()))
			.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.PAYINGFINAL.getCode()))
			.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.INACTIVE.getCode()))
			.fetchOne();

		return result == null ? 0D : result.getValue(DSL.sum(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_COUNT)).doubleValue();
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
			.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.INACTIVE.getCode()));
		
		if (RentalType.fromCode(rentalCell.getRentalType()) == RentalType.HOUR) {
			// 如果这个资源可以使用半天预约，要判断当前时间段在上午还是下午或者晚上
			if (rentalTypes.contains(RentalType.HALFDAY) || rentalTypes.contains(RentalType.THREETIMEADAY)) {
				Byte amorpm = calculateAmorpm(rentalResource, rentalCell);
				if (amorpm != null) {
					step.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID.equal(rentalCell.getId())
							.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(rentalCell.getResourceRentalDate())
									.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.DAY.getCode())
											.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.AMORPM.eq(amorpm))))
							.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.MONTH.getCode())
									.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(initToMonthFirstDay(rentalCell.getResourceRentalDate())))));
				}
			}else {
				step.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID.equal(rentalCell.getId())
						.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.DAY.getCode())
								.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(rentalCell.getResourceRentalDate())))
						.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.MONTH.getCode())
									.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(initToMonthFirstDay(rentalCell.getResourceRentalDate())))));
			}
		}else if (RentalType.fromCode(rentalCell.getRentalType()) == RentalType.HALFDAY || RentalType.fromCode(rentalCell.getRentalType()) == RentalType.THREETIMEADAY) {
			if (rentalTypes.contains(RentalType.HOUR)) {
				Timestamp[] beginEndTime = calculateBeginEndTime(rentalResource, rentalCell);
				step.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID.equal(rentalCell.getId())
						.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.DAY.getCode())
								.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(rentalCell.getResourceRentalDate())))
						.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.BEGIN_TIME.ge(beginEndTime[0]).and(Tables.EH_RENTALV2_RESOURCE_ORDERS.END_TIME.le(beginEndTime[1])))
						.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.MONTH.getCode())
									.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(initToMonthFirstDay(rentalCell.getResourceRentalDate())))));
			}else {
				step.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID.equal(rentalCell.getId())
						.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.DAY.getCode())
								.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(rentalCell.getResourceRentalDate())))
						.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.MONTH.getCode())
									.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(initToMonthFirstDay(rentalCell.getResourceRentalDate())))));
			}
		}else if (RentalType.fromCode(rentalCell.getRentalType()) == RentalType.DAY) {
			step.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID.equal(rentalCell.getId())
					.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(rentalCell.getResourceRentalDate())
							.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.in(Arrays.asList(RentalType.HOUR, RentalType.HALFDAY, RentalType.THREETIMEADAY))))
					.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE.eq(RentalType.MONTH.getCode())
							.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.eq(initToMonthFirstDay(rentalCell.getResourceRentalDate())))));
		}else if (RentalType.fromCode(rentalCell.getRentalType()) == RentalType.MONTH) {
			step.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID.equal(rentalCell.getId())
					.or(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.ge(initToMonthFirstDay(rentalCell.getResourceRentalDate()))
							.and(Tables.EH_RENTALV2_RESOURCE_ORDERS.RESOURCE_RENTAL_DATE.le(initToMonthLastDay(rentalCell.getResourceRentalDate())))));
		}
		
		Table<?> innerTable = step.groupBy(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_TYPE, Tables.EH_RENTALV2_RESOURCE_ORDERS.AMORPM, 
				Tables.EH_RENTALV2_RESOURCE_ORDERS.BEGIN_TIME, Tables.EH_RENTALV2_RESOURCE_ORDERS.END_TIME).asTable("inner_table");
		
		Field<BigDecimal> maxRentalCount = null;
		Table<?> middleTable = context.select(innerTable.field("rental_type"), maxRentalCount = DSL.max(rentalCount)).from(innerTable).groupBy(innerTable.field("rental_type")).asTable("middle_table");
		
		SelectJoinStep<Record1<BigDecimal>> outer = context.select(DSL.sum(maxRentalCount)).from(middleTable);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(outer.getSQL());
		}
		
		Record1<BigDecimal> record = outer.fetchOne();
		
		return record == null ? 0D : record.getValue(DSL.sum(maxRentalCount)).doubleValue();
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

	private Timestamp[] calculateBeginEndTime(RentalResource rentalResource, RentalCell rentalCell) {
		return null;
	}

	@SuppressWarnings("deprecation")
	private Byte calculateAmorpm(RentalResource rentalResource, RentalCell rentalCell) {
		if (rentalCell.getBeginTime() == null || rentalCell.getEndTime() == null) {
			return null;
		}
		List<RentalTimeInterval> halfTimeIntervals = queryRentalTimeIntervalByOwner(RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode(), rentalResource.getId());
		if (halfTimeIntervals == null || halfTimeIntervals.isEmpty()) {
			return null;
		}
		
		for (int i = 0; i < halfTimeIntervals.size(); i++) {
			RentalTimeInterval rentalTimeInterval = halfTimeIntervals.get(i);
			if (rentalTimeInterval.getBeginTime() <= (rentalCell.getBeginTime().getHours()+rentalCell.getBeginTime().getMinutes()/60) 
					&& rentalTimeInterval.getEndTime() >= (rentalCell.getEndTime().getHours()+rentalCell.getEndTime().getMinutes()/60)) {
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
	public List<RentalItemsOrder> findRentalItemsBillByItemsId(Long siteItemId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ITEMS_ORDERS);
		Condition condition = Tables.EH_RENTALV2_ITEMS_ORDERS.RENTAL_RESOURCE_ITEM_ID
				.equal(siteItemId);

		step.where(condition);
		List<RentalItemsOrder> result = step
				.orderBy(Tables.EH_RENTALV2_ITEMS_ORDERS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalItemsOrder.class);
				});

		return result;
	}

	@Override
	public Integer deleteResourceCells(Long rentalSiteId, Long beginDate,
			Long endDate) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2CellsRecord> step = context
				.delete(Tables.EH_RENTALV2_CELLS);
		Condition condition = Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID
				.equal(rentalSiteId);
		if (null != beginDate && null != endDate) {
			condition = condition
					.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE.between(
							new Date(beginDate), new Date(endDate)));
		}
		step.where(condition);
		Integer deleteCount = step.execute();
		return deleteCount;
	}

	@Override
	public List<RentalOrder> listRentalBills(Long userId,Long resourceTypeId,
			ListingLocator locator, int count, List<Byte> status, Byte payMode) {
		final List<RentalOrder> result = new ArrayList<RentalOrder>();
		Condition condition = Tables.EH_RENTALV2_ORDERS.ID.lt(locator.getAnchor());
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.INACTIVE.getCode()));

		//TODO:
		if(null!=resourceTypeId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE_ID
				.eq(resourceTypeId));
//		condition = condition.and(Tables.EH_RENTALV2_ORDERS.OWNER_TYPE
//				.eq(ownerType));
//		if (StringUtils.isNotEmpty(siteType))
//			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE
//					.eq(siteType));
		if (null != payMode) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.PAY_MODE.eq(payMode));
		}
		if (null != userId) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_UID
					.eq(userId));
		}
		if (null != status)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS.in(status));
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.VISIBLE_FLAG
				.eq(VisibleFlag.VISIBLE.getCode()));
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
	public Integer getSumSitePrice(Long rentalBillId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record1<BigDecimal>> step = context.select(
				Tables.EH_RENTALV2_ITEMS_ORDERS.TOTAL_MONEY.sum()).from(
				Tables.EH_RENTALV2_ITEMS_ORDERS);
		Condition condition = Tables.EH_RENTALV2_ITEMS_ORDERS.RENTAL_ORDER_ID
				.equal(rentalBillId);
		step.where(condition);
		Integer result = step.fetchOne().value1().intValue();
		return result;
	}

	@Override
	public List<RentalItemsOrder> findRentalItemsBillBySiteBillId(
			Long rentalBillId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ITEMS_ORDERS);
		Condition condition = Tables.EH_RENTALV2_ITEMS_ORDERS.RENTAL_ORDER_ID
				.equal(rentalBillId);
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
			Long rentalSiteItemId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ITEMS_ORDERS);
		Condition condition = Tables.EH_RENTALV2_ITEMS_ORDERS.RENTAL_ORDER_ID
				.equal(rentalBillId);
		condition = condition
				.and(Tables.EH_RENTALV2_ITEMS_ORDERS.RENTAL_RESOURCE_ITEM_ID
						.equal(rentalSiteItemId));

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
	public void cancelRentalBillById(Long rentalBillId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = Tables.EH_RENTALV2_ORDERS.ID.equal(rentalBillId);
		UpdateConditionStep<EhRentalv2OrdersRecord> step = context
				.update(Tables.EH_RENTALV2_ORDERS)
				.set(Tables.EH_RENTALV2_ORDERS.STATUS,
						SiteBillStatus.FAIL.getCode())
				.set(Tables.EH_RENTALV2_ORDERS.OPERATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()))
				.where(condition);
		step.execute();

	}

	SimpleDateFormat timeSF = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public Integer countRentalSiteBills(Long rentalSiteId, Long beginDate,
			Long endDate,Time beginTime,Time endTime) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectOnConditionStep<Record1<Integer>> step = context
				.selectCount()
				.from(Tables.EH_RENTALV2_CELLS)
				.join(Tables.EH_RENTALV2_RESOURCE_ORDERS)
				.on(Tables.EH_RENTALV2_CELLS.ID
						.eq(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_RESOURCE_RULE_ID))
				.join(Tables.EH_RENTALV2_ORDERS)
				.on(Tables.EH_RENTALV2_ORDERS.ID
						.eq(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_ORDER_ID));
		Condition condition = Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID
				.equal(rentalSiteId);
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS
				.ne(SiteBillStatus.FAIL.getCode()));
		if (null != beginDate && null != endDate) {
			condition = condition
					.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE.between(
							new Date(beginDate), new Date(endDate)));
		}
		if (null != beginTime && null != endTime) {
			//TODO: delete between two time in a day
//			Timestamp beginTimestamp = Timestamp.valueOf(s)
//			condition = condition
//					.and(Tables.EH_RENTALV2_CELLS.RESOURCE_RENTAL_DATE.between(
//							new Date(beginDate), new Date(endDate)));
		}
		step.where(condition);
		Integer result = step.fetchOne().value1().intValue();
		return result;
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
	public void deleteResource(Long rentalSiteId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2ResourcesRecord> step = context
				.delete(Tables.EH_RENTALV2_RESOURCES);
		Condition condition = Tables.EH_RENTALV2_RESOURCES.ID.equal(rentalSiteId);

		step.where(condition);
		step.execute();
	}

	@Override
	public void updateRentalSiteStatus(Long rentalSiteId, byte status) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = Tables.EH_RENTALV2_RESOURCES.ID.equal(rentalSiteId);
		UpdateConditionStep<EhRentalv2ResourcesRecord> step = context
				.update(Tables.EH_RENTALV2_RESOURCES)
				.set(Tables.EH_RENTALV2_RESOURCES.STATUS, status).where(condition);
		step.execute();
	}

	@Override
	public int countRentalSites(Long  resourceTypeId,String keyword,List<Byte>  status,List<Long>  siteIds){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record1<Integer>> step = context.selectCount().from(
				Tables.EH_RENTALV2_RESOURCES);
		Condition condition = Tables.EH_RENTALV2_RESOURCES.RESOURCE_TYPE_ID
				.equal(resourceTypeId);
		
		if(null!= siteIds)
			condition = condition.and(Tables.EH_RENTALV2_RESOURCES.ID
				.in(siteIds));
		 
		if (!StringUtils.isEmpty(keyword)) {
			condition = condition.and(Tables.EH_RENTALV2_RESOURCES.ADDRESS
					.like("%" + keyword + "%")
					.or(Tables.EH_RENTALV2_RESOURCES.RESOURCE_NAME.like("%" + keyword
							+ "%"))
					.or(Tables.EH_RENTALV2_RESOURCES.BUILDING_NAME.like("%" + keyword
							+ "%")));
		}
		if(null != status)
			condition = condition.and(Tables.EH_RENTALV2_RESOURCES.STATUS
				.in(status));
		return step.where(condition).fetchOneInto(Integer.class);
		 
	}

	@Override
	public Integer countRentalSiteItemBills(Long rentalSiteItemId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectOnConditionStep<Record1<Integer>> step = context
				.selectCount()
				.from(Tables.EH_RENTALV2_ITEMS_ORDERS)
				.join(Tables.EH_RENTALV2_ORDERS)
				.on(Tables.EH_RENTALV2_ORDERS.ID
						.eq(Tables.EH_RENTALV2_ITEMS_ORDERS.RENTAL_ORDER_ID));
		Condition condition = Tables.EH_RENTALV2_ITEMS_ORDERS.RENTAL_RESOURCE_ITEM_ID
				.equal(rentalSiteItemId);
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS
				.ne(SiteBillStatus.FAIL.getCode()));

		return step.where(condition).fetchOneInto(Integer.class);
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
	public Double sumRentalRuleBillSumCounts(Long siteRuleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectOnConditionStep<Record1<BigDecimal>> step = context
				.select(Tables.EH_RENTALV2_RESOURCE_ORDERS.RENTAL_COUNT.sum())
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
		//		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS
//				.ne(SiteBillStatus.OFFLINE_PAY.getCode()));
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS
				.ne(SiteBillStatus.APPROVING.getCode()));
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS
				.ne(SiteBillStatus.PAYINGFINAL.getCode()));
		/*---end----*/
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS
				.ne(SiteBillStatus.INACTIVE.getCode()));

		return step.where(condition).fetchOneInto(Double.class);
	}

	@Override
	public int countRentalBills(Long ownerId,String ownerType, String siteType,
			Long rentalSiteId, Byte billStatus, Long startTime, Long endTime,
			Byte invoiceFlag,Long userId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record1<Integer>> step = context.selectCount().from(
				Tables.EH_RENTALV2_ORDERS);
		//TODO:
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
		if (null != userId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_UID
								.equal(userId));
		if (null != startTime)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.START_TIME
					.lessThan(new Timestamp(endTime)));
		if (null != endTime)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.END_TIME
					.greaterThan(new Timestamp(startTime)));
		if (null != billStatus)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS
					.equal(billStatus));
		if (null != invoiceFlag) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.INVOICE_FLAG
					.equal(invoiceFlag));
		}
		return step.where(condition).fetchOneInto(Integer.class);
	}

	@Override
	public List<RentalOrder> listRentalBills(Long resourceTypeId, Long organizationId, Long rentalSiteId, ListingLocator locator, Byte billStatus,
			String vendorType , Integer pageSize, Long startTime, Long endTime,
			Byte invoiceFlag,Long userId){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ORDERS);
		//TODO
		Condition condition = Tables.EH_RENTALV2_ORDERS.ORGANIZATION_ID
				.equal( organizationId);
		condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS.ne(SiteBillStatus.INACTIVE.getCode()));
//		condition = condition.and(Tables.EH_RENTALV2_ORDERS.OWNER_TYPE
//				.equal(ownerType));
		if (StringUtils.isNotEmpty(vendorType))
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.VENDOR_TYPE
					.equal(vendorType));
		if(null!=resourceTypeId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RESOURCE_TYPE_ID 
					.equal(resourceTypeId));
		if (null != rentalSiteId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_RESOURCE_ID
					.equal(rentalSiteId));
		if (null != endTime)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.START_TIME
					.lessThan(new Timestamp(endTime)));
		if (null !=  startTime)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.END_TIME
					.greaterThan(new Timestamp(startTime)));
		 
		if (null != billStatus)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.STATUS
					.equal(billStatus));
		if (null != invoiceFlag) {
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.INVOICE_FLAG
					.equal(invoiceFlag));
		}
		if (null != userId)
			condition = condition.and(Tables.EH_RENTALV2_ORDERS.RENTAL_UID
								.equal(userId)); 
		if(null!=locator && locator.getAnchor() != null)
			condition=condition.and(Tables.EH_RENTALV2_ORDERS.ID.lt(locator.getAnchor()));
								
		step.limit(pageSize);
		step.where(condition);
		List<RentalOrder> result = step
				.orderBy(Tables.EH_RENTALV2_ORDERS.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalOrder.class);
				});
		
		if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query rental bills, sql=" + step.getSQL());
            LOGGER.debug("Query rental bills, bindValues=" + step.getBindValues());
        }

		return result;
	}

	@Override
	public List<RentalResource> findRentalSites(Long  resourceTypeId, String keyword, ListingLocator locator,
			Integer pageSize,List<Byte>  status,List<Long>  siteIds,Long communityId) {
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
		if(null!= status&&status.size()!=0)
			condition = condition.and(Tables.EH_RENTALV2_RESOURCES.STATUS.in(status));
		else
			condition = condition.and(Tables.EH_RENTALV2_RESOURCES.STATUS.ne(RentalSiteStatus.DISABLE.getCode()));

		List<RentalResource> result = step.where(condition)
				.orderBy(Tables.EH_RENTALV2_RESOURCES.DEFAULT_ORDER.asc()).limit(pageSize).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalResource.class);
				});
		if(result.size()==0)
			return null;
		return result;
	}

	@Override
	public Integer updateBillInvoice(Long rentalBillId, Byte invoiceFlag) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = Tables.EH_RENTALV2_ORDERS.ID.equal(rentalBillId);
		UpdateConditionStep<EhRentalv2OrdersRecord> step = context
				.update(Tables.EH_RENTALV2_ORDERS)
				.set(Tables.EH_RENTALV2_ORDERS.INVOICE_FLAG, invoiceFlag)
				.set(Tables.EH_RENTALV2_ORDERS.OPERATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()))
				.where(condition);

		return step.execute();
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
	public void updateRentalOrderPayorderMap(RentalOrderPayorderMap ordeMap) {
		assert (ordeMap.getId() == null);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2OrderPayorderMapDao dao = new EhRentalv2OrderPayorderMapDao(context.configuration());
		
		dao.update(ordeMap);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRentalv2OrderPayorderMap.class,
				ordeMap.getId());
	}

	@Override
	public void deleteRentalBillById(Long rentalBillId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = Tables.EH_RENTALV2_ORDERS.ID.equal(rentalBillId);
		UpdateConditionStep<EhRentalv2OrdersRecord> step = context
				.update(Tables.EH_RENTALV2_ORDERS)
				.set(Tables.EH_RENTALV2_ORDERS.VISIBLE_FLAG,
						VisibleFlag.UNVISIBLE.getCode())
				.set(Tables.EH_RENTALV2_ORDERS.OPERATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()))
				.where(condition);
		step.execute();
	}

	@Override
	public Long createRentalBillAttachment(RentalOrderAttachment rba) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2OrderAttachments.class));
		rba.setId(id);
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

	@Override
	public Long createRentalBillPaybillMap(RentalOrderPayorderMap billmap) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2OrderPayorderMap.class));
		billmap.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2OrderPayorderMapRecord record = ConvertHelper.convert(billmap,
				EhRentalv2OrderPayorderMapRecord.class);
		InsertQuery<EhRentalv2OrderPayorderMapRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_ORDER_PAYORDER_MAP);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE,
				EhRentalv2OrderPayorderMap.class, null);
		return id;
	}

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
				});;
 

		return result;
	}

	@Override
	public List<RentalCell> findRentalSiteRulesByRuleIds(
			List<Long> siteRuleIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_CELLS);
		Condition condition = Tables.EH_RENTALV2_CELLS.ID.in(siteRuleIds);

		step.where(condition);
		List<RentalCell> result = step
				.orderBy(Tables.EH_RENTALV2_CELLS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalCell.class);
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

	@Override
	public RentalOrderPayorderMap findRentalBillPaybillMapByOrderNo(String orderNo) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ORDER_PAYORDER_MAP);
		Condition condition = Tables.EH_RENTALV2_ORDER_PAYORDER_MAP.ORDER_NO
				.equal(Long.valueOf(orderNo));
		step.where(condition);
		List<RentalOrderPayorderMap> result = step
				.orderBy(Tables.EH_RENTALV2_ORDER_PAYORDER_MAP.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalOrderPayorderMap.class);
				});
		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}
	public List<RentalOrderPayorderMap> findRentalBillPaybillMapByBillId(Long id){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ORDER_PAYORDER_MAP);
		Condition condition = Tables.EH_RENTALV2_ORDER_PAYORDER_MAP.ORDER_ID
				.equal(id);
		step.where(condition);
		List<RentalOrderPayorderMap> result = step
				.orderBy(Tables.EH_RENTALV2_ORDER_PAYORDER_MAP.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalOrderPayorderMap.class);
				});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}
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
	public List<RentalOrder> listSuccessRentalBills() {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_ORDERS);
		//TODO：
		Condition condition = Tables.EH_RENTALV2_ORDERS.STATUS
				.eq(SiteBillStatus.SUCCESS.getCode()); 
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
	public List<RentalSiteRange> findRentalSiteOwnersByOwnerTypeAndId(String ownerType,Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_RESOURCE_RANGES);
		Condition condition = Tables.EH_RENTALV2_RESOURCE_RANGES.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTALV2_RESOURCE_RANGES.OWNER_TYPE
				.equal(ownerType));
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
	public List<RentalResourcePic> findRentalSitePicsByOwnerTypeAndId(String ownerType,Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_RESOURCE_PICS);
		Condition condition = Tables.EH_RENTALV2_RESOURCE_PICS.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTALV2_RESOURCE_PICS.OWNER_TYPE
				.equal(ownerType));
		step.where(condition);
		List<RentalResourcePic> result = step
				.orderBy(Tables.EH_RENTALV2_RESOURCE_PICS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalResourcePic.class);
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
	public RentalDefaultRule getRentalDefaultRule(String ownerType,
			Long ownerId, Long resourceTypeId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_DEFAULT_RULES);
		Condition condition = Tables.EH_RENTALV2_DEFAULT_RULES.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTALV2_DEFAULT_RULES.OWNER_TYPE
				.equal(ownerType));
		condition = condition.and(Tables.EH_RENTALV2_DEFAULT_RULES.RESOURCE_TYPE_ID
				.equal(resourceTypeId));
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
			String ownerType, Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_TIME_INTERVAL);
		Condition condition = Tables.EH_RENTALV2_TIME_INTERVAL.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTALV2_TIME_INTERVAL.OWNER_TYPE
				.equal(ownerType));
		step.where(condition);
		List<RentalTimeInterval> result = step
				.orderBy(Tables.EH_RENTALV2_TIME_INTERVAL.ID.asc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalTimeInterval.class);
				});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}

	@Override
	public List<RentalCloseDate> queryRentalCloseDateByOwner(String ownerType,
			Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_CLOSE_DATES);
		Condition condition = Tables.EH_RENTALV2_CLOSE_DATES.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTALV2_CLOSE_DATES.OWNER_TYPE
				.equal(ownerType));
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
	public List<RentalConfigAttachment> queryRentalConfigAttachmentByOwner(
			String ownerType, Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_CONFIG_ATTACHMENTS);
		Condition condition = Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.OWNER_TYPE
				.equal(ownerType));
		step.where(condition);
		List<RentalConfigAttachment> result = step
				.orderBy(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.ID.desc()).fetch().map((r) -> {
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
				.getSequenceDomainFromTablePojo(EhRentalv2ResourcePics.class));
		detailPic.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2ResourcePicsRecord record = ConvertHelper.convert(detailPic,
				EhRentalv2ResourcePicsRecord.class);
		InsertQuery<EhRentalv2ResourcePicsRecord> query = context
				.insertQuery(Tables.EH_RENTALV2_RESOURCE_PICS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2ResourcePics.class, null);
		
	}

	@Override
	public void deleteRentalSitePicsBySiteId(Long siteId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2ResourcePicsRecord> step = context
				.delete(Tables.EH_RENTALV2_RESOURCE_PICS);
		Condition condition = Tables.EH_RENTALV2_RESOURCE_PICS.OWNER_ID
				.equal(siteId);
		condition = condition.and(Tables.EH_RENTALV2_RESOURCE_PICS.OWNER_TYPE
				.equal(EhRentalv2Resources.class.getSimpleName()));
		step.where(condition);
		step.execute();
	}

	@Override
	public void deleteRentalSiteOwnersBySiteId(Long siteId) { 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2ResourceRangesRecord> step = context
				.delete(Tables.EH_RENTALV2_RESOURCE_RANGES);
		Condition condition = Tables.EH_RENTALV2_RESOURCE_RANGES.RENTAL_RESOURCE_ID
				.equal(siteId);
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

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2CellsDao dao = new EhRentalv2CellsDao(context.configuration());
		dao.update(rsr);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRentalv2Cells.class,
				rsr.getId());
	}

	@Override
	public Integer deleteTimeIntervalsByOwnerId(String ownerType, Long id) { 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2TimeIntervalRecord> step = context
				.delete(Tables.EH_RENTALV2_TIME_INTERVAL);
		Condition condition = Tables.EH_RENTALV2_TIME_INTERVAL.OWNER_TYPE
				.equal(ownerType).and(Tables.EH_RENTALV2_TIME_INTERVAL.OWNER_ID
				.equal(id));
		 
		step.where(condition);
		Integer deleteCount = step.execute();
		return deleteCount;
	}

	@Override
	public Integer deleteRentalCloseDatesByOwnerId(String ownerType, Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2CloseDatesRecord> step = context
				.delete(Tables.EH_RENTALV2_CLOSE_DATES);
		Condition condition = Tables.EH_RENTALV2_CLOSE_DATES.OWNER_TYPE
				.equal(ownerType).and(Tables.EH_RENTALV2_CLOSE_DATES.OWNER_ID
				.equal(id));
		 
		step.where(condition);
		Integer deleteCount = step.execute();
		return deleteCount;
		
	}

	@Override
	public Integer deleteRentalConfigAttachmentsByOwnerId(String ownerType,
			Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2ConfigAttachmentsRecord> step = context
				.delete(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS);
		Condition condition = Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.OWNER_TYPE
				.equal(ownerType).and(Tables.EH_RENTALV2_CONFIG_ATTACHMENTS.OWNER_ID
				.equal(id));
		 
		step.where(condition);
		Integer deleteCount = step.execute();
		return deleteCount;
		
	}

	@Override
	public List<RentalSiteRange> findRentalSiteOwnersBySiteId(Long siteId) {
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
	public RentalCell findRentalSiteRulesByRuleId(Long rentalSiteRuleId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_CELLS);
		Condition condition = Tables.EH_RENTALV2_CELLS.ID.eq(rentalSiteRuleId);

		step.where(condition);
		List<RentalCell> result = step
				.orderBy(Tables.EH_RENTALV2_CELLS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalCell.class);
				});

		if (null != result && result.size() > 0)
			return result.get(0) ;
		return null;
	}



	@Override
	public Long createRentalRefundOrder(RentalRefundOrder rentalRefundOrder) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalv2ResourceRanges.class));
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
	public void deleteRentalRefundOrder(RentalRefundOrder rentalRefundOrder) {
		 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite()); 
		EhRentalv2ResourceRangesDao dao = new EhRentalv2ResourceRangesDao(context.configuration());
		dao.deleteById(rentalRefundOrder.getId());

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRentalv2ResourceRanges.class,rentalRefundOrder.getId());
	}
	@Override
	public RentalRefundOrder getRentalRefundOrderById(Long rentalRefundOrderId) {
		 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite()); 
		EhRentalv2RefundOrdersDao dao = new EhRentalv2RefundOrdersDao(context.configuration());
		EhRentalv2RefundOrders order = dao.findById(rentalRefundOrderId);
		return ConvertHelper.convert(order, RentalRefundOrder.class);
	}
	
	@Override
	public void deleteRentalRefundOrder(Long rentalRefundOrderId) {
		 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite()); 
		EhRentalv2ResourceRangesDao dao = new EhRentalv2ResourceRangesDao(context.configuration());
		dao.deleteById(rentalRefundOrderId);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRentalv2ResourceRanges.class,rentalRefundOrderId);
	}
	

	@Override
	public void updateRentalRefundOrder(RentalRefundOrder rentalRefundOrder) {
		 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite()); 
		EhRentalv2RefundOrdersDao dao = new EhRentalv2RefundOrdersDao(context.configuration());
		dao.update(rentalRefundOrder); 

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRentalv2RefundOrders.class,rentalRefundOrder.getId());
	}

	@Override
	public List<RentalRefundOrder> getRefundOrderList(Long resourceTypeId,
			CrossShardListingLocator locator, Byte status, String styleNo,
			int pageSize, Long startTime, Long endTime) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_REFUND_ORDERS); 
		Condition condition = Tables.EH_RENTALV2_REFUND_ORDERS.ID.greaterOrEqual(0L); 
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
	public RentalRefundOrder getRentalRefundOrderByRefoundNo(
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
	public RentalResourceType getRentalResourceTypeById(Long rentalResourceTypeId) {
		 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite()); 
		EhRentalv2ResourceTypesDao dao = new EhRentalv2ResourceTypesDao(context.configuration());
		EhRentalv2ResourceTypes order = dao.findById(rentalResourceTypeId);
		return ConvertHelper.convert(order, RentalResourceType.class);
	}

	@Override
	public RentalCell getRentalCellById(Long cellId) {
		 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite()); 
		EhRentalv2CellsDao dao = new EhRentalv2CellsDao(context.configuration());
		EhRentalv2Cells order = dao.findById(cellId);
		return ConvertHelper.convert(order, RentalCell.class);
	}

	@Override
	public List<RentalCell> getRentalCellsByIds(List<Long> cellIds) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
//		EhRentalv2CellsDao dao = new EhRentalv2CellsDao(context.configuration());
//		EhRentalv2Cells order = dao.findById(cellId);
		SelectQuery<EhRentalv2CellsRecord> query = context.selectQuery(Tables.EH_RENTALV2_CELLS);
		query.addConditions(Tables.EH_RENTALV2_CELLS.ID.in(cellIds));

		return query.fetch().map(r -> ConvertHelper.convert(r, RentalCell.class));
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
	public void deleteRentalResourceType (Long resoureceTypeId) {
		 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite()); 
		EhRentalv2ResourceTypesDao dao = new EhRentalv2ResourceTypesDao(context.configuration());
		dao.deleteById(resoureceTypeId);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, RentalResourceType.class,resoureceTypeId);
	}
	

	@Override
	public void updateRentalResourceType(RentalResourceType resourceType) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite()); 
		EhRentalv2ResourceTypesDao dao = new EhRentalv2ResourceTypesDao(context.configuration());
		dao.update(resourceType); 

		DaoHelper.publishDaoAction(DaoAction.MODIFY, RentalResourceType.class,resourceType.getId());
	}
	
	

	@Override
	public List<RentalResourceType> findRentalResourceTypes(Integer namespaceId, Byte status, ListingLocator locator) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_RESOURCE_TYPES);
		Condition condition = Tables.EH_RENTALV2_RESOURCE_TYPES.STATUS
				.equal(status);
		if(null!=namespaceId){
			condition = condition.and(Tables.EH_RENTALV2_RESOURCE_TYPES.NAMESPACE_ID
					.equal(namespaceId));
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
	public Integer deleteRentalResourceNumbersByOwnerId(String ownerType, Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2ResourceNumbersRecord> step = context
				.delete(Tables.EH_RENTALV2_RESOURCE_NUMBERS);
		Condition condition = Tables.EH_RENTALV2_RESOURCE_NUMBERS.OWNER_TYPE
				.equal(ownerType).and(Tables.EH_RENTALV2_RESOURCE_NUMBERS.OWNER_ID
				.equal(id));
		 
		step.where(condition);
		Integer deleteCount = step.execute();
		return deleteCount;
	}

	@Override
	public List<RentalResourceNumber> queryRentalResourceNumbersByOwner(
			String ownerType, Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTALV2_RESOURCE_NUMBERS);
		Condition condition = Tables.EH_RENTALV2_RESOURCE_NUMBERS.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTALV2_RESOURCE_NUMBERS.OWNER_TYPE
				.equal(ownerType));
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
	public void batchCreateRentalCells(List<EhRentalv2Cells> list) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalv2CellsDao dao = new EhRentalv2CellsDao(context.configuration());
		dao.insert(list);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2Cells.class,
				 null);
	}

	@Override
	public String getPriceStringByResourceId(Long rentalSiteId) {
		// TODO Auto-generated method stub
		BigDecimal minPrice = new BigDecimal(0);
		BigDecimal maxPrice = new BigDecimal(0);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		Record4<BigDecimal, BigDecimal, Byte, Double> record = context.select(Tables.EH_RENTALV2_RESOURCES.WEEKEND_PRICE,
				Tables.EH_RENTALV2_RESOURCES.WORKDAY_PRICE , Tables.EH_RENTALV2_RESOURCES.RENTAL_TYPE,
				Tables.EH_RENTALV2_RESOURCES.TIME_STEP)
				.from(Tables.EH_RENTALV2_RESOURCES).where(Tables.EH_RENTALV2_RESOURCES.ID.eq(rentalSiteId)).fetchOne();
		BigDecimal weekendPrice = record.value1();
		BigDecimal workdayPrice = record.value2();
		Byte rentalType = record.value3();
		Double timeStep = record.value4();
		int compareValue = workdayPrice.compareTo(weekendPrice);
		switch(compareValue){
			case -1:
				minPrice = workdayPrice;
				maxPrice = weekendPrice;
				break;
			case 0:
				minPrice = weekendPrice;
				maxPrice = weekendPrice;
				break;
			case 1:
				minPrice = weekendPrice;
				maxPrice = workdayPrice;
				break;
		}
		BigDecimal min2 = context.select(Tables.EH_RENTALV2_CELLS.PRICE.min())
		.from(Tables.EH_RENTALV2_CELLS).where(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.eq(rentalSiteId)).fetchOne().value1();
		if(null!=min2 && minPrice.compareTo(min2) == 1)
			minPrice = min2;
		BigDecimal max2 = context.select(Tables.EH_RENTALV2_CELLS.PRICE.max())
		.from(Tables.EH_RENTALV2_CELLS).where(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.eq(rentalSiteId)).fetchOne().value1();
		if(null!=max2 &&  maxPrice.compareTo(max2) == -1)
			maxPrice = max2;
		
		if(minPrice.compareTo(maxPrice) == 0){
			return priceToString(minPrice,rentalType,timeStep);
		}
		return priceToString(minPrice,rentalType,timeStep)+"~" +priceToString(maxPrice,rentalType,timeStep);
	}
 
	private boolean isInteger(double d){
		double eps = 0.0001;
		return Math.abs(d - (double)((int)d)) < eps;
	}
	private String priceToString(BigDecimal price, Byte rentalType, Double timeStep) {
		if(price.compareTo(new BigDecimal(0)) == 0)
			return "免费";
		if(rentalType.equals(RentalType.DAY.getCode()))
			return "￥"+price.toString()+"/天";
		if(rentalType.equals(RentalType.HALFDAY.getCode()))
			return "￥"+price.toString()+"/半天";
		if(rentalType.equals(RentalType.THREETIMEADAY.getCode()))
			return "￥"+price.toString()+"/半天";
		if(rentalType.equals(RentalType.HOUR.getCode()))
			return "￥"+price.toString()+"/"+(isInteger(timeStep.doubleValue())?timeStep.intValue():timeStep)+"小时";
		return "";
	}
 
	@Override
	public void deleteRentalCellsByResourceId(Long rentalSiteId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalv2CellsRecord> step = context
				.delete(Tables.EH_RENTALV2_CELLS);
		Condition condition = Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID
				.equal(rentalSiteId);
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
	public MaxMinPrice findMaxMinPrice(Long resourceId, Byte rentalType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Record record = context.select(DSL.max(Tables.EH_RENTALV2_CELLS.PRICE), DSL.min(Tables.EH_RENTALV2_CELLS.PRICE),
				DSL.max(Tables.EH_RENTALV2_CELLS.ORIGINAL_PRICE), DSL.min(Tables.EH_RENTALV2_CELLS.ORIGINAL_PRICE),
				DSL.max(Tables.EH_RENTALV2_CELLS.HALFRESOURCE_PRICE), DSL.min(Tables.EH_RENTALV2_CELLS.HALFRESOURCE_PRICE),
				DSL.max(Tables.EH_RENTALV2_CELLS.HALFRESOURCE_ORIGINAL_PRICE), DSL.min(Tables.EH_RENTALV2_CELLS.HALFRESOURCE_ORIGINAL_PRICE),
				DSL.max(Tables.EH_RENTALV2_CELLS.ORG_MEMBER_PRICE), DSL.min(Tables.EH_RENTALV2_CELLS.ORG_MEMBER_PRICE),
				DSL.max(Tables.EH_RENTALV2_CELLS.ORG_MEMBER_ORIGINAL_PRICE), DSL.min(Tables.EH_RENTALV2_CELLS.ORG_MEMBER_ORIGINAL_PRICE),
				DSL.max(Tables.EH_RENTALV2_CELLS.HALF_ORG_MEMBER_PRICE), DSL.min(Tables.EH_RENTALV2_CELLS.HALF_ORG_MEMBER_PRICE),
				DSL.max(Tables.EH_RENTALV2_CELLS.HALF_ORG_MEMBER_ORIGINAL_PRICE), DSL.min(Tables.EH_RENTALV2_CELLS.HALF_ORG_MEMBER_ORIGINAL_PRICE),
				DSL.max(Tables.EH_RENTALV2_CELLS.APPROVING_USER_PRICE), DSL.min(Tables.EH_RENTALV2_CELLS.APPROVING_USER_PRICE),
				DSL.max(Tables.EH_RENTALV2_CELLS.APPROVING_USER_ORIGINAL_PRICE), DSL.min(Tables.EH_RENTALV2_CELLS.APPROVING_USER_ORIGINAL_PRICE),
				DSL.max(Tables.EH_RENTALV2_CELLS.HALF_APPROVING_USER_PRICE), DSL.min(Tables.EH_RENTALV2_CELLS.HALF_APPROVING_USER_PRICE),
				DSL.max(Tables.EH_RENTALV2_CELLS.HALF_APPROVING_USER_ORIGINAL_PRICE), DSL.min(Tables.EH_RENTALV2_CELLS.HALF_APPROVING_USER_ORIGINAL_PRICE)
				)
			.from(Tables.EH_RENTALV2_CELLS)
			.where(Tables.EH_RENTALV2_CELLS.RENTAL_RESOURCE_ID.eq(resourceId))
			.and(Tables.EH_RENTALV2_CELLS.RENTAL_TYPE.eq(rentalType))
			.and(Tables.EH_RENTALV2_CELLS.STATUS.eq(RentalSiteStatus.NORMAL.getCode()))
			.fetchOne();
		if (record != null) {
			BigDecimal maxPrice = max(record.getValue(DSL.max(Tables.EH_RENTALV2_CELLS.PRICE)),
					record.getValue(DSL.max(Tables.EH_RENTALV2_CELLS.ORIGINAL_PRICE)),
					record.getValue(DSL.max(Tables.EH_RENTALV2_CELLS.HALFRESOURCE_PRICE)),
					record.getValue(DSL.max(Tables.EH_RENTALV2_CELLS.HALFRESOURCE_ORIGINAL_PRICE)));
			BigDecimal minPrice = min(record.getValue(DSL.min(Tables.EH_RENTALV2_CELLS.PRICE)),
					record.getValue(DSL.min(Tables.EH_RENTALV2_CELLS.ORIGINAL_PRICE)),
					record.getValue(DSL.min(Tables.EH_RENTALV2_CELLS.HALFRESOURCE_PRICE)),
					record.getValue(DSL.min(Tables.EH_RENTALV2_CELLS.HALFRESOURCE_ORIGINAL_PRICE)));
			BigDecimal maxOrgMemberPrice = max(record.getValue(DSL.max(Tables.EH_RENTALV2_CELLS.ORG_MEMBER_PRICE)),
					record.getValue(DSL.max(Tables.EH_RENTALV2_CELLS.ORG_MEMBER_ORIGINAL_PRICE)),
					record.getValue(DSL.max(Tables.EH_RENTALV2_CELLS.HALF_ORG_MEMBER_PRICE)),
					record.getValue(DSL.max(Tables.EH_RENTALV2_CELLS.HALF_ORG_MEMBER_ORIGINAL_PRICE)));
			BigDecimal minOrgMemberPrice = min(record.getValue(DSL.min(Tables.EH_RENTALV2_CELLS.ORG_MEMBER_PRICE)),
					record.getValue(DSL.min(Tables.EH_RENTALV2_CELLS.ORG_MEMBER_ORIGINAL_PRICE)),
					record.getValue(DSL.min(Tables.EH_RENTALV2_CELLS.HALF_ORG_MEMBER_PRICE)),
					record.getValue(DSL.min(Tables.EH_RENTALV2_CELLS.HALF_ORG_MEMBER_ORIGINAL_PRICE)));
			BigDecimal maxApprovingUserPrice = max(record.getValue(DSL.max(Tables.EH_RENTALV2_CELLS.APPROVING_USER_PRICE)),
					record.getValue(DSL.max(Tables.EH_RENTALV2_CELLS.APPROVING_USER_ORIGINAL_PRICE)),
					record.getValue(DSL.max(Tables.EH_RENTALV2_CELLS.HALF_APPROVING_USER_PRICE)),
					record.getValue(DSL.max(Tables.EH_RENTALV2_CELLS.HALF_APPROVING_USER_ORIGINAL_PRICE)));
			BigDecimal minApprovingUserPrice = min(record.getValue(DSL.min(Tables.EH_RENTALV2_CELLS.APPROVING_USER_PRICE)),
					record.getValue(DSL.min(Tables.EH_RENTALV2_CELLS.APPROVING_USER_ORIGINAL_PRICE)),
					record.getValue(DSL.min(Tables.EH_RENTALV2_CELLS.HALF_APPROVING_USER_PRICE)),
					record.getValue(DSL.min(Tables.EH_RENTALV2_CELLS.HALF_APPROVING_USER_ORIGINAL_PRICE)));
			return new MaxMinPrice(maxPrice, minPrice, maxOrgMemberPrice, minOrgMemberPrice, maxApprovingUserPrice, minApprovingUserPrice);
		}
		return null;
	}
	
	private BigDecimal max(BigDecimal ... b) {
		BigDecimal max = new BigDecimal(0);
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
		BigDecimal min = new BigDecimal(0);
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
	
}
