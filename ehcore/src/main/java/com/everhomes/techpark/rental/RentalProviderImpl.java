package com.everhomes.techpark.rental;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteWhereStep;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectJoinStep;
import org.jooq.SelectOnConditionStep;
import org.jooq.UpdateConditionStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.techpark.rental.DateLength;
import com.everhomes.rest.techpark.rental.RentalType;
import com.everhomes.rest.techpark.rental.SiteBillStatus;
import com.everhomes.rest.techpark.rental.VisibleFlag;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhRentalBillsDao;
import com.everhomes.server.schema.tables.daos.EhRentalRulesDao;
import com.everhomes.server.schema.tables.daos.EhRentalSitesDao;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhRentalBillAttachments;
import com.everhomes.server.schema.tables.pojos.EhRentalBillPaybillMap;
import com.everhomes.server.schema.tables.pojos.EhRentalBills;
import com.everhomes.server.schema.tables.pojos.EhRentalCloseDates;
import com.everhomes.server.schema.tables.pojos.EhRentalConfigAttachments;
import com.everhomes.server.schema.tables.pojos.EhRentalDefaultRules;
import com.everhomes.server.schema.tables.pojos.EhRentalItemsBills;
import com.everhomes.server.schema.tables.pojos.EhRentalRules;
import com.everhomes.server.schema.tables.pojos.EhRentalSiteItems;
import com.everhomes.server.schema.tables.pojos.EhRentalSiteRules;
import com.everhomes.server.schema.tables.pojos.EhRentalSites;
import com.everhomes.server.schema.tables.pojos.EhRentalSitesBillNumbers;
import com.everhomes.server.schema.tables.pojos.EhRentalSitesBills;
import com.everhomes.server.schema.tables.pojos.EhRentalTimeInterval;
import com.everhomes.server.schema.tables.records.EhRentalBillAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhRentalBillPaybillMapRecord;
import com.everhomes.server.schema.tables.records.EhRentalBillsRecord;
import com.everhomes.server.schema.tables.records.EhRentalCloseDatesRecord;
import com.everhomes.server.schema.tables.records.EhRentalConfigAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhRentalDefaultRulesRecord;
import com.everhomes.server.schema.tables.records.EhRentalItemsBillsRecord;
import com.everhomes.server.schema.tables.records.EhRentalRulesRecord;
import com.everhomes.server.schema.tables.records.EhRentalSiteItemsRecord;
import com.everhomes.server.schema.tables.records.EhRentalSiteRulesRecord;
import com.everhomes.server.schema.tables.records.EhRentalSitesBillNumbersRecord;
import com.everhomes.server.schema.tables.records.EhRentalSitesBillsRecord;
import com.everhomes.server.schema.tables.records.EhRentalSitesRecord;
import com.everhomes.server.schema.tables.records.EhRentalTimeIntervalRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class RentalProviderImpl implements RentalProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(RentalProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public List<String> findRentalSiteTypes() {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record1<String>> step = context.selectDistinct(
				Tables.EH_RENTAL_RULES.SITE_TYPE).from(Tables.EH_RENTAL_RULES);
		List<String> result = step.fetch().getValues(
				Tables.EH_RENTAL_RULES.SITE_TYPE);
		return result;
	}

	@Override
	public RentalRule getRentalRule(Long ownerId,String ownerType,  String siteType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_RULES);
		Condition condition = Tables.EH_RENTAL_RULES.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTAL_RULES.OWNER_TYPE
				.equal(ownerType));
		condition = condition.and(Tables.EH_RENTAL_RULES.SITE_TYPE
				.equal(siteType));
		step.where(condition);
		List<RentalRule> result = step
				.orderBy(Tables.EH_RENTAL_RULES.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalRule.class);
				});
		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public void updateRentalRule(RentalRule rentalRule) {
		assert (rentalRule.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalRulesDao dao = new EhRentalRulesDao(context.configuration());
		dao.update(rentalRule);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRentalRules.class,
				rentalRule.getId());
	}

	@Override
	public Long createRentalSite(RentalSite rentalsite) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalSites.class));
		rentalsite.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalSitesRecord record = ConvertHelper.convert(rentalsite,
				EhRentalSitesRecord.class);
		InsertQuery<EhRentalSitesRecord> query = context
				.insertQuery(Tables.EH_RENTAL_SITES);
		query.setRecord(record);
		query.execute();

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalSites.class, null);
		return id;
	}

	@Override
	public List<RentalSiteItem> findRentalSiteItems(Long rentalSiteId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_SITE_ITEMS);
		Condition condition = Tables.EH_RENTAL_SITE_ITEMS.RENTAL_SITE_ID
				.equal(rentalSiteId);
		step.where(condition);
		List<RentalSiteItem> result = step
				.orderBy(Tables.EH_RENTAL_SITE_ITEMS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalSiteItem.class);
				});

		return result;

	}

	@Override
	public void createRentalSiteItem(RentalSiteItem siteItem) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalSiteItems.class));
		siteItem.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalSiteItemsRecord record = ConvertHelper.convert(siteItem,
				EhRentalSiteItemsRecord.class);
		InsertQuery<EhRentalSiteItemsRecord> query = context
				.insertQuery(Tables.EH_RENTAL_SITE_ITEMS);
		query.setRecord(record);
		query.execute();

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalSiteItems.class,
				null);

	}

	@Override
	public void createRentalSiteRule(RentalSiteRule rsr) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalSiteRules.class));
		rsr.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalSiteRulesRecord record = ConvertHelper.convert(rsr,
				EhRentalSiteRulesRecord.class);
		InsertQuery<EhRentalSiteRulesRecord> query = context
				.insertQuery(Tables.EH_RENTAL_SITE_RULES);
		query.setRecord(record);
		query.execute();

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalSiteRules.class,
				null);

	}

	@Override
	public List<RentalSiteRule> findRentalSiteRules(Long rentalSiteId,
			String ruleDate, Timestamp beginDate, Byte rentalType, Byte dateLength) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_SITE_RULES);
		Condition condition = Tables.EH_RENTAL_SITE_RULES.RENTAL_TYPE
				.eq(rentalType);
		if (null != ruleDate) {
			if (dateLength.equals(DateLength.DAY.getCode())) {
				condition = Tables.EH_RENTAL_SITE_RULES.SITE_RENTAL_DATE
						.equal(Date.valueOf(ruleDate));
			} else if (dateLength.equals(DateLength.MONTH.getCode())) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(Date.valueOf(ruleDate));
				// month begin
				calendar.set(Calendar.DAY_OF_MONTH,
						calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
				condition = condition
						.and(Tables.EH_RENTAL_SITE_RULES.SITE_RENTAL_DATE
								.greaterOrEqual(new java.sql.Date(calendar
										.getTime().getTime())));

				// month end
				calendar.set(Calendar.DAY_OF_MONTH,
						calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				condition = condition
						.and(Tables.EH_RENTAL_SITE_RULES.SITE_RENTAL_DATE
								.lessOrEqual(new java.sql.Date(calendar
										.getTime().getTime())));

			}else if (dateLength.equals(DateLength.WEEK.getCode())) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(Date.valueOf(ruleDate));
				// month begin
				calendar.set(Calendar.DAY_OF_WEEK,
						calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
				condition = condition
						.and(Tables.EH_RENTAL_SITE_RULES.SITE_RENTAL_DATE
								.greaterOrEqual(new java.sql.Date(calendar
										.getTime().getTime())));

				// month end
				calendar.set(Calendar.DAY_OF_WEEK,
						calendar.getActualMaximum(Calendar.DAY_OF_WEEK));
				condition = condition
						.and(Tables.EH_RENTAL_SITE_RULES.SITE_RENTAL_DATE
								.lessOrEqual(new java.sql.Date(calendar
										.getTime().getTime())));

			}
		}
		if (null != rentalSiteId) {
			condition = condition
					.and(Tables.EH_RENTAL_SITE_RULES.RENTAL_SITE_ID
							.equal(rentalSiteId));
		}

		if (null != beginDate && rentalType.equals(RentalType.HOUR.getCode())) {
//		if (null != beginDate) {
			condition = condition.and(Tables.EH_RENTAL_SITE_RULES.BEGIN_TIME
					.lessOrEqual(beginDate));

		}
		step.where(condition);
		List<RentalSiteRule> result = step
				.orderBy(Tables.EH_RENTAL_SITE_RULES.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalSiteRule.class);
				});

		return result;
	}

	@Override
	public RentalSiteRule findRentalSiteRuleById(Long siteRuleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_SITE_RULES);
		Condition condition = Tables.EH_RENTAL_SITE_RULES.ID.equal(siteRuleId);
		step.where(condition);
		List<RentalSiteRule> result = step
				.orderBy(Tables.EH_RENTAL_SITE_RULES.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalSiteRule.class);
				});
		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public Long createRentalBill(RentalBill rentalBill) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalBills.class));
		rentalBill.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalBillsRecord record = ConvertHelper.convert(rentalBill,
				EhRentalBillsRecord.class);
		InsertQuery<EhRentalBillsRecord> query = context
				.insertQuery(Tables.EH_RENTAL_BILLS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalBills.class, null);
		return id;
	}

	@Override
	public Long createRentalItemBill(RentalItemsBill rib) {

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalItemsBills.class));
		rib.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalItemsBillsRecord record = ConvertHelper.convert(rib,
				EhRentalItemsBillsRecord.class);
		InsertQuery<EhRentalItemsBillsRecord> query = context
				.insertQuery(Tables.EH_RENTAL_ITEMS_BILLS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalItemsBills.class,
				null);
		return id;
	}

	@Override
	public Long createRentalSiteBill(RentalSitesBill rsb) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalSitesBills.class));
		rsb.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalSitesBillsRecord record = ConvertHelper.convert(rsb,
				EhRentalSitesBillsRecord.class);
		InsertQuery<EhRentalSitesBillsRecord> query = context
				.insertQuery(Tables.EH_RENTAL_SITES_BILLS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalSitesBills.class,
				null);
		return id;
	}

	@Override
	public List<RentalSitesBill> findRentalSiteBillBySiteRuleId(Long siteRuleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context
				.select()
				.from(Tables.EH_RENTAL_SITES_BILLS)
				.join(Tables.EH_RENTAL_BILLS)
				.on(Tables.EH_RENTAL_BILLS.ID
						.eq(Tables.EH_RENTAL_SITES_BILLS.RENTAL_BILL_ID));

		Condition condition = Tables.EH_RENTAL_SITES_BILLS.RENTAL_SITE_RULE_ID
				.equal(siteRuleId);
		condition = condition.and(Tables.EH_RENTAL_BILLS.STATUS
				.ne(SiteBillStatus.FAIL.getCode()));
		step.where(condition);
		List<EhRentalSitesBillsRecord> resultRecord = step
				.orderBy(Tables.EH_RENTAL_SITES_BILLS.ID.desc()).fetch()
				.map(new RentalSitesBillRecordMapper());

		List<RentalSitesBill> result = resultRecord.stream().map((r) -> {
			return ConvertHelper.convert(r, RentalSitesBill.class);
		}).collect(Collectors.toList());

		return result;
	}

	@Override
	public List<RentalItemsBill> findRentalItemsBillByItemsId(Long siteItemId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_ITEMS_BILLS);
		Condition condition = Tables.EH_RENTAL_ITEMS_BILLS.RENTAL_SITE_ITEM_ID
				.equal(siteItemId);

		step.where(condition);
		List<RentalItemsBill> result = step
				.orderBy(Tables.EH_RENTAL_ITEMS_BILLS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalItemsBill.class);
				});

		return result;
	}

	@Override
	public Integer deleteRentalSiteRules(Long rentalSiteId, Long beginDate,
			Long endDate) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalSiteRulesRecord> step = context
				.delete(Tables.EH_RENTAL_SITE_RULES);
		Condition condition = Tables.EH_RENTAL_SITE_RULES.RENTAL_SITE_ID
				.equal(rentalSiteId);
		if (null != beginDate && null != endDate) {
			condition = condition
					.and(Tables.EH_RENTAL_SITE_RULES.SITE_RENTAL_DATE.between(
							new Date(beginDate), new Date(endDate)));
		}
		step.where(condition);
		Integer deleteCount = step.execute();
		return deleteCount;
	}

	@Override
	public List<RentalBill> listRentalBills(Long userId,Long launchPadItemId,
			ListingLocator locator, int count, Byte status) {
		final List<RentalBill> result = new ArrayList<RentalBill>();
		Condition condition = Tables.EH_RENTAL_BILLS.ID.lt(locator.getAnchor());
		//TODO:
		if(null!=launchPadItemId)
			condition = condition.and(Tables.EH_RENTAL_BILLS.LAUNCH_PAD_ITEM_ID
				.eq(launchPadItemId));
//		condition = condition.and(Tables.EH_RENTAL_BILLS.OWNER_TYPE
//				.eq(ownerType));
//		if (StringUtils.isNotEmpty(siteType))
//			condition = condition.and(Tables.EH_RENTAL_BILLS.SITE_TYPE
//					.eq(siteType));
		if (null != userId) {
			condition = condition.and(Tables.EH_RENTAL_BILLS.RENTAL_UID
					.eq(userId));
		}
		if (null != status)
			condition = condition.and(Tables.EH_RENTAL_BILLS.STATUS.eq(status));
		condition = condition.and(Tables.EH_RENTAL_BILLS.VISIBLE_FLAG
				.eq(VisibleFlag.VISIBLE.getCode()));
		final Condition condition2 = condition;
		this.dbProvider.mapReduce(
				AccessSpec.readOnlyWith(EhCommunities.class),
				null,
				(DSLContext context, Object reducingContext) -> {
					context.select()
							.from(Tables.EH_RENTAL_BILLS)
							.where(condition2)
							.orderBy(Tables.EH_RENTAL_BILLS.ID.desc())
							.limit(count)
							.fetch()
							.map((r) -> {
								result.add(ConvertHelper.convert(r,
										RentalBill.class));
								return null;
							});

					return true;
				});

		return result;
	}

	@Override
	public RentalSite getRentalSiteById(Long rentalSiteId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_SITES);
		Condition condition = Tables.EH_RENTAL_SITES.ID.equal(rentalSiteId);
		step.where(condition);
		List<RentalSite> result = step
				.orderBy(Tables.EH_RENTAL_SITES.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalSite.class);
				});
		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public Integer getSumSitePrice(Long rentalBillId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record1<BigDecimal>> step = context.select(
				Tables.EH_RENTAL_ITEMS_BILLS.TOTAL_MONEY.sum()).from(
				Tables.EH_RENTAL_ITEMS_BILLS);
		Condition condition = Tables.EH_RENTAL_ITEMS_BILLS.RENTAL_BILL_ID
				.equal(rentalBillId);
		step.where(condition);
		Integer result = step.fetchOne().value1().intValue();
		return result;
	}

	@Override
	public List<RentalItemsBill> findRentalItemsBillBySiteBillId(
			Long rentalBillId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_ITEMS_BILLS);
		Condition condition = Tables.EH_RENTAL_ITEMS_BILLS.RENTAL_BILL_ID
				.equal(rentalBillId);
		step.where(condition);
		List<RentalItemsBill> result = step
				.orderBy(Tables.EH_RENTAL_ITEMS_BILLS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalItemsBill.class);
				});

		if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query rental item bills by site bill id, sql=" + step.getSQL());
            LOGGER.debug("Query rental item bills by site bill id, bindValues=" + step.getBindValues());
        }
		return result;
	}

	@Override
	public RentalSiteItem findRentalSiteItemById(Long rentalSiteItemId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_SITE_ITEMS);
		Condition condition = Tables.EH_RENTAL_SITE_ITEMS.ID
				.equal(rentalSiteItemId);
		step.where(condition);
		List<RentalSiteItem> result = step
				.orderBy(Tables.EH_RENTAL_SITE_ITEMS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalSiteItem.class);
				});
		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public RentalItemsBill findRentalItemBill(Long rentalBillId,
			Long rentalSiteItemId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_ITEMS_BILLS);
		Condition condition = Tables.EH_RENTAL_ITEMS_BILLS.RENTAL_BILL_ID
				.equal(rentalBillId);
		condition = condition
				.and(Tables.EH_RENTAL_ITEMS_BILLS.RENTAL_SITE_ITEM_ID
						.equal(rentalSiteItemId));

		step.where(condition);
		List<RentalItemsBill> result = step
				.orderBy(Tables.EH_RENTAL_ITEMS_BILLS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalItemsBill.class);
				});

		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public RentalBill findRentalBillById(Long rentalBillId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_BILLS);
		Condition condition = Tables.EH_RENTAL_BILLS.ID.equal(rentalBillId);
		step.where(condition);
		List<RentalBill> result = step
				.orderBy(Tables.EH_RENTAL_BILLS.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalBill.class);
				});

		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public void cancelRentalBillById(Long rentalBillId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = Tables.EH_RENTAL_BILLS.ID.equal(rentalBillId);
		UpdateConditionStep<EhRentalBillsRecord> step = context
				.update(Tables.EH_RENTAL_BILLS)
				.set(Tables.EH_RENTAL_BILLS.STATUS,
						SiteBillStatus.FAIL.getCode()).where(condition);
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
				.from(Tables.EH_RENTAL_SITE_RULES)
				.join(Tables.EH_RENTAL_SITES_BILLS)
				.on(Tables.EH_RENTAL_SITE_RULES.ID
						.eq(Tables.EH_RENTAL_SITES_BILLS.RENTAL_SITE_RULE_ID))
				.join(Tables.EH_RENTAL_BILLS)
				.on(Tables.EH_RENTAL_BILLS.ID
						.eq(Tables.EH_RENTAL_SITES_BILLS.RENTAL_BILL_ID));
		Condition condition = Tables.EH_RENTAL_SITE_RULES.RENTAL_SITE_ID
				.equal(rentalSiteId);
		condition = condition.and(Tables.EH_RENTAL_BILLS.STATUS
				.ne(SiteBillStatus.FAIL.getCode()));
		if (null != beginDate && null != endDate) {
			condition = condition
					.and(Tables.EH_RENTAL_SITE_RULES.SITE_RENTAL_DATE.between(
							new Date(beginDate), new Date(endDate)));
		}
		if (null != beginTime && null != endTime) {
			//TODO: delete between two time in a day
//			Timestamp beginTimestamp = Timestamp.valueOf(s)
//			condition = condition
//					.and(Tables.EH_RENTAL_SITE_RULES.SITE_RENTAL_DATE.between(
//							new Date(beginDate), new Date(endDate)));
		}
		step.where(condition);
		Integer result = step.fetchOne().value1().intValue();
		return result;
	}

	@Override
	public void updateRentalSite(RentalSite rentalsite) {
		assert (rentalsite.getId() == null);

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalSitesDao dao = new EhRentalSitesDao(context.configuration());
		dao.update(rentalsite);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRentalSites.class,
				rentalsite.getId());
	}

	@Override
	public void deleteRentalSite(Long rentalSiteId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalSitesRecord> step = context
				.delete(Tables.EH_RENTAL_SITES);
		Condition condition = Tables.EH_RENTAL_SITES.ID.equal(rentalSiteId);

		step.where(condition);
		step.execute();
	}

	@Override
	public void updateRentalSiteStatus(Long rentalSiteId, byte status) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = Tables.EH_RENTAL_SITES.ID.equal(rentalSiteId);
		UpdateConditionStep<EhRentalSitesRecord> step = context
				.update(Tables.EH_RENTAL_SITES)
				.set(Tables.EH_RENTAL_SITES.STATUS, status).where(condition);
		step.execute();
	}

	@Override
	public int countRentalSites(Long  launchPadItemId,String keyword,List<Byte>  status,List<Long>  siteIds){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record1<Integer>> step = context.selectCount().from(
				Tables.EH_RENTAL_SITES);
		Condition condition = Tables.EH_RENTAL_SITES.LAUNCH_PAD_ITEM_ID
				.equal(launchPadItemId);
		
		if(null!= siteIds)
			condition = condition.and(Tables.EH_RENTAL_SITES.ID
				.in(siteIds));
		 
		if (!StringUtils.isEmpty(keyword)) {
			condition = condition.and(Tables.EH_RENTAL_SITES.ADDRESS
					.like("%" + keyword + "%")
					.or(Tables.EH_RENTAL_SITES.SITE_NAME.like("%" + keyword
							+ "%"))
					.or(Tables.EH_RENTAL_SITES.BUILDING_NAME.like("%" + keyword
							+ "%")));
		}
		if(null != status)
			condition = condition.and(Tables.EH_RENTAL_SITES.STATUS
				.in(status));
		return step.where(condition).fetchOneInto(Integer.class);
		 
	}

	@Override
	public Integer countRentalSiteItemBills(Long rentalSiteItemId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectOnConditionStep<Record1<Integer>> step = context
				.selectCount()
				.from(Tables.EH_RENTAL_ITEMS_BILLS)
				.join(Tables.EH_RENTAL_BILLS)
				.on(Tables.EH_RENTAL_BILLS.ID
						.eq(Tables.EH_RENTAL_ITEMS_BILLS.RENTAL_BILL_ID));
		Condition condition = Tables.EH_RENTAL_ITEMS_BILLS.RENTAL_SITE_ITEM_ID
				.equal(rentalSiteItemId);
		condition = condition.and(Tables.EH_RENTAL_BILLS.STATUS
				.ne(SiteBillStatus.FAIL.getCode()));

		return step.where(condition).fetchOneInto(Integer.class);
	}

	@Override
	public void deleteRentalSiteItemById(Long rentalSiteItemId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		DeleteWhereStep<EhRentalSiteItemsRecord> step = context
				.delete(Tables.EH_RENTAL_SITE_ITEMS);
		Condition condition = Tables.EH_RENTAL_SITE_ITEMS.ID
				.equal(rentalSiteItemId);
		step.where(condition);
		step.execute();
	}

	@Override
	public Double sumRentalRuleBillSumCounts(Long siteRuleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectOnConditionStep<Record1<BigDecimal>> step = context
				.select(Tables.EH_RENTAL_SITES_BILLS.RENTAL_COUNT.sum())
				.from(Tables.EH_RENTAL_SITES_BILLS)
				.join(Tables.EH_RENTAL_BILLS)
				.on(Tables.EH_RENTAL_BILLS.ID
						.eq(Tables.EH_RENTAL_SITES_BILLS.RENTAL_BILL_ID));
		Condition condition = Tables.EH_RENTAL_SITES_BILLS.RENTAL_SITE_RULE_ID
				.equal(siteRuleId);
		condition = condition.and(Tables.EH_RENTAL_BILLS.STATUS
				.ne(SiteBillStatus.FAIL.getCode()));

		return step.where(condition).fetchOneInto(Double.class);
	}

	@Override
	public int countRentalBills(Long ownerId,String ownerType, String siteType,
			Long rentalSiteId, Byte billStatus, Long startTime, Long endTime,
			Byte invoiceFlag,Long userId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record1<Integer>> step = context.selectCount().from(
				Tables.EH_RENTAL_BILLS);
		//TODO:
		Condition condition = Tables.EH_RENTAL_BILLS.LAUNCH_PAD_ITEM_ID
				.equal(ownerId);
//		condition = condition.and(Tables.EH_RENTAL_BILLS.OWNER_TYPE
//				.equal(ownerType));
//		if (StringUtils.isNotEmpty(siteType))
//			condition = condition.and(Tables.EH_RENTAL_BILLS.SITE_TYPE
//					.equal(siteType));
		if (null != rentalSiteId)
			condition = condition.and(Tables.EH_RENTAL_BILLS.RENTAL_SITE_ID
					.equal(rentalSiteId));
		if (null != userId)
			condition = condition.and(Tables.EH_RENTAL_BILLS.RENTAL_UID
								.equal(userId));
		if (null != startTime)
			condition = condition.and(Tables.EH_RENTAL_BILLS.START_TIME
					.lessThan(new Timestamp(endTime)));
		if (null != endTime)
			condition = condition.and(Tables.EH_RENTAL_BILLS.END_TIME
					.greaterThan(new Timestamp(startTime)));
		if (null != billStatus)
			condition = condition.and(Tables.EH_RENTAL_BILLS.STATUS
					.equal(billStatus));
		if (null != invoiceFlag) {
			condition = condition.and(Tables.EH_RENTAL_BILLS.INVOICE_FLAG
					.equal(invoiceFlag));
		}
		return step.where(condition).fetchOneInto(Integer.class);
	}

	@Override
	public List<RentalBill> listRentalBills(Long ownerId,String ownerType,
			String siteType, Long rentalSiteId, Byte billStatus,
			Integer pageOffset, Integer pageSize, Long startTime, Long endTime,
			Byte invoiceFlag,Long userId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_BILLS);
		//TODO
		Condition condition = Tables.EH_RENTAL_BILLS.LAUNCH_PAD_ITEM_ID
				.equal(ownerId);
//		condition = condition.and(Tables.EH_RENTAL_BILLS.OWNER_TYPE
//				.equal(ownerType));
//		if (StringUtils.isNotEmpty(siteType))
//			condition = condition.and(Tables.EH_RENTAL_BILLS.SITE_TYPE
//					.equal(siteType));
		if (null != rentalSiteId)
			condition = condition.and(Tables.EH_RENTAL_BILLS.RENTAL_SITE_ID
					.equal(rentalSiteId));
		if (null != startTime)
			condition = condition.and(Tables.EH_RENTAL_BILLS.START_TIME
					.lessThan(new Timestamp(endTime)));
		if (null != endTime)
			condition = condition.and(Tables.EH_RENTAL_BILLS.END_TIME
					.greaterThan(new Timestamp(startTime)));
		 
		if (null != billStatus)
			condition = condition.and(Tables.EH_RENTAL_BILLS.STATUS
					.equal(billStatus));
		if (null != invoiceFlag) {
			condition = condition.and(Tables.EH_RENTAL_BILLS.INVOICE_FLAG
					.equal(invoiceFlag));
		}
		if (null != userId)
			condition = condition.and(Tables.EH_RENTAL_BILLS.RENTAL_UID
								.equal(userId));
		Integer offset = pageOffset == null ? 1 : (pageOffset - 1) * pageSize;
		step.limit(offset, pageSize);
		step.where(condition);
		List<RentalBill> result = step
				.orderBy(Tables.EH_RENTAL_BILLS.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalBill.class);
				});
		
		if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query rental bills, sql=" + step.getSQL());
            LOGGER.debug("Query rental bills, bindValues=" + step.getBindValues());
        }

		return result;
	}

	@Override
	public List<RentalSite> findRentalSites(Long  launchPadItemId, String keyword, ListingLocator locator,
			Integer pageSize,List<Byte>  status,List<Long>  siteIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_SITES);
		//TODO
		Condition condition = Tables.EH_RENTAL_SITES.LAUNCH_PAD_ITEM_ID
				.equal(launchPadItemId);
		if(null!=siteIds)
			condition= condition.and( Tables.EH_RENTAL_SITES.ID.in(siteIds));
//		condition = condition.and(Tables.EH_RENTAL_SITES.OWNER_TYPE
//				.equal(ownerType));
//		condition = condition.and(Tables.EH_RENTAL_SITES.SITE_TYPE
//				.equal(siteType));
		if (!StringUtils.isEmpty(keyword)) {
			condition = condition.and(Tables.EH_RENTAL_SITES.ADDRESS
					.like("%" + keyword + "%")
					.or(Tables.EH_RENTAL_SITES.SITE_NAME.like("%" + keyword
							+ "%"))
					.or(Tables.EH_RENTAL_SITES.BUILDING_NAME.like("%" + keyword
							+ "%")));
		}

        if(locator.getAnchor() != null)
        	condition=condition.and(Tables.EH_COMMUNITIES.ID.lt(locator.getAnchor()));
		if(null!= status)
			condition = condition.and(Tables.EH_RENTAL_SITES.STATUS
					.in(status));
		step.where(condition);

		List<RentalSite> result = step
				.orderBy(Tables.EH_RENTAL_SITES.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalSite.class);
				});
		if(result.size()==0)
			return null;
		return result;
	}

	@Override
	public Integer updateBillInvoice(Long rentalBillId, Byte invoiceFlag) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = Tables.EH_RENTAL_BILLS.ID.equal(rentalBillId);
		UpdateConditionStep<EhRentalBillsRecord> step = context
				.update(Tables.EH_RENTAL_BILLS)
				.set(Tables.EH_RENTAL_BILLS.INVOICE_FLAG, invoiceFlag)
				.where(condition);

		return step.execute();
	}

	@Override
	public void updateRentalBill(RentalBill bill) {
		assert (bill.getId() == null);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalBillsDao dao = new EhRentalBillsDao(context.configuration());
		dao.update(bill);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRentalBills.class,
				bill.getId());
	}

	@Override
	public void deleteRentalBillById(Long rentalBillId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = Tables.EH_RENTAL_BILLS.ID.equal(rentalBillId);
		UpdateConditionStep<EhRentalBillsRecord> step = context
				.update(Tables.EH_RENTAL_BILLS)
				.set(Tables.EH_RENTAL_BILLS.VISIBLE_FLAG,
						VisibleFlag.UNVISIBLE.getCode()).where(condition);
		step.execute();
	}

	@Override
	public Long createRentalBillAttachment(RentalBillAttachment rba) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalBillAttachments.class));
		rba.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalBillAttachmentsRecord record = ConvertHelper.convert(rba,
				EhRentalBillAttachmentsRecord.class);
		InsertQuery<EhRentalBillAttachmentsRecord> query = context
				.insertQuery(Tables.EH_RENTAL_BILL_ATTACHMENTS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE,
				EhRentalBillAttachments.class, null);
		return id;
	}

	@Override
	public Long createRentalBillPaybillMap(RentalBillPaybillMap billmap) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalBillPaybillMap.class));
		billmap.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalBillPaybillMapRecord record = ConvertHelper.convert(billmap,
				EhRentalBillPaybillMapRecord.class);
		InsertQuery<EhRentalBillPaybillMapRecord> query = context
				.insertQuery(Tables.EH_RENTAL_BILL_PAYBILL_MAP);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE,
				EhRentalBillPaybillMap.class, null);
		return id;
	}

	@Override
	public List<RentalSitesBill> findRentalSitesBillByBillId(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context
				.select()
				.from(Tables.EH_RENTAL_SITES_BILLS)
				.join(Tables.EH_RENTAL_BILLS)
				.on(Tables.EH_RENTAL_BILLS.ID
						.eq(Tables.EH_RENTAL_SITES_BILLS.RENTAL_BILL_ID));

		Condition condition = Tables.EH_RENTAL_SITES_BILLS.RENTAL_BILL_ID
				.equal(id);
//		condition = condition.and(Tables.EH_RENTAL_BILLS.STATUS
//				.ne(SiteBillStatus.FAIL.getCode()));
		step.where(condition);
		List<EhRentalSitesBillsRecord> resultRecord = step
				.orderBy(Tables.EH_RENTAL_SITES_BILLS.ID.desc()).fetch()
				.map(new RentalSitesBillRecordMapper());

		List<RentalSitesBill> result = resultRecord.stream().map((r) -> {
			return ConvertHelper.convert(r, RentalSitesBill.class);
		}).collect(Collectors.toList());

		return result;
	}

	@Override
	public List<RentalSiteRule> findRentalSiteRulesByRuleIds(
			List<Long> siteRuleIds) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_SITE_RULES);
		Condition condition = Tables.EH_RENTAL_SITE_RULES.ID.in(siteRuleIds);

		step.where(condition);
		List<RentalSiteRule> result = step
				.orderBy(Tables.EH_RENTAL_SITE_RULES.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalSiteRule.class);
				});

		return result;
	}

	@Override
	public List<RentalBillAttachment> findRentalBillAttachmentByBillId(
			Long rentalBillId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_BILL_ATTACHMENTS);
		Condition condition = Tables.EH_RENTAL_BILL_ATTACHMENTS.RENTAL_BILL_ID
				.eq(rentalBillId);

		step.where(condition);
		List<RentalBillAttachment> result = step
				.orderBy(Tables.EH_RENTAL_BILL_ATTACHMENTS.ID.desc())
				.fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalBillAttachment.class);
				});

		return result;
	}

	@Override
	public RentalBillPaybillMap findRentalBillPaybillMapByOrderNo(String orderNo) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_BILL_PAYBILL_MAP);
		Condition condition = Tables.EH_RENTAL_BILL_PAYBILL_MAP.ONLINE_PAY_BILL_ID
				.equal(Long.valueOf(orderNo));
		step.where(condition);
		List<RentalBillPaybillMap> result = step
				.orderBy(Tables.EH_RENTAL_BILL_PAYBILL_MAP.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalBillPaybillMap.class);
				});
		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public List<RentalBill> listRentalBills(Long ownerId, String ownerType,
			String siteType, Long rentalSiteId, Long beginDate, Long endDate) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_BILLS);
		//TODO：
		Condition condition = Tables.EH_RENTAL_BILLS.LAUNCH_PAD_ITEM_ID
				.equal(ownerId);
//		condition = condition.and(Tables.EH_RENTAL_BILLS.OWNER_TYPE
//				.equal(ownerType));
//		if (StringUtils.isNotEmpty(siteType))
//			condition = condition.and(Tables.EH_RENTAL_BILLS.SITE_TYPE
//					.equal(siteType));
		if (null != rentalSiteId)
			condition = condition.and(Tables.EH_RENTAL_BILLS.RENTAL_SITE_ID
					.equal(rentalSiteId));
		if (null != beginDate)
			condition = condition.and(Tables.EH_RENTAL_BILLS.RENTAL_DATE
					.greaterOrEqual(new Date(beginDate)));
		if (null != endDate)
			condition = condition.and(Tables.EH_RENTAL_BILLS.RENTAL_DATE
					.lessOrEqual(new Date(endDate)));
		step.where(condition);
		List<RentalBill> result = step
				.orderBy(Tables.EH_RENTAL_BILLS.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalBill.class);
				});
		if (null != result && result.size() > 0)
			return result ;
		return null;
	}

	@Override
	public void createRentalRule(RentalRule rentalRule) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalRules.class));
		rentalRule.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalRulesRecord record = ConvertHelper.convert(rentalRule,
				EhRentalRulesRecord.class);
		InsertQuery<EhRentalRulesRecord> query = context
				.insertQuery(Tables.EH_RENTAL_RULES);
		query.setRecord(record);
		query.execute();

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalRules.class,
				null);
		
	}

	@Override
	public List<RentalSiteOwner> findRentalSiteOwnersByOwnerTypeAndId(String ownerType,Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_SITE_OWNERS);
		Condition condition = Tables.EH_RENTAL_SITE_OWNERS.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTAL_SITE_OWNERS.OWNER_TYPE
				.equal(ownerType));
		step.where(condition);
		List<RentalSiteOwner> result = step
				.orderBy(Tables.EH_RENTAL_SITE_OWNERS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalSiteOwner.class);
				});
		if (null != result && result.size() > 0)
			return result ;
		return null;

	}


	@Override
	public List<RentalSitePic> findRentalSitePicsByOwnerTypeAndId(String ownerType,Long ownerId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_SITE_PICS);
		Condition condition = Tables.EH_RENTAL_SITE_PICS.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTAL_SITE_PICS.OWNER_TYPE
				.equal(ownerType));
		step.where(condition);
		List<RentalSitePic> result = step
				.orderBy(Tables.EH_RENTAL_SITE_OWNERS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalSitePic.class);
				});
		if (null != result && result.size() > 0)
			return result ;
		return null;

	}

	@Override
	public RentalSiteItem getRentalSiteItemById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_SITE_ITEMS);
		Condition condition = Tables.EH_RENTAL_SITE_ITEMS.ID.equal(id);
		step.where(condition);
		List<RentalSiteItem> result = step
				.orderBy(Tables.EH_RENTAL_SITE_ITEMS.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalSiteItem.class);
				});
		if (null != result && result.size() > 0)
			return result.get(0);
		return null;
	}

	@Override
	public List<RentalSitesBillNumber> findSitesBillNumbersBySiteId(
			Long siteRuleId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_SITES_BILL_NUMBERS);
		Condition condition = Tables.EH_RENTAL_SITES_BILL_NUMBERS.RENTAL_SITE_RULE_ID.equal(siteRuleId);
		step.where(condition);
		List<RentalSitesBillNumber> result = step
				.orderBy(Tables.EH_RENTAL_SITES_BILL_NUMBERS.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalSitesBillNumber.class);
				});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}
	@Override
	public List<RentalSitesBillNumber> findSitesBillNumbersByBillId(
			Long siteBillId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_SITES_BILL_NUMBERS);
		Condition condition = Tables.EH_RENTAL_SITES_BILL_NUMBERS.RENTAL_SITE_BILL_ID.equal(siteBillId);
		step.where(condition);
		List<RentalSitesBillNumber> result = step
				.orderBy(Tables.EH_RENTAL_SITES_BILL_NUMBERS.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalSitesBillNumber.class);
				});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}
	@Override
	public void createRentalSitesBillNumber(RentalSitesBillNumber sitesBillNumber) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalSitesBillNumbers.class));
		sitesBillNumber.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalSitesBillNumbersRecord record = ConvertHelper.convert(sitesBillNumber,
				EhRentalSitesBillNumbersRecord.class);
		InsertQuery<EhRentalSitesBillNumbersRecord> query = context
				.insertQuery(Tables.EH_RENTAL_SITES_BILL_NUMBERS);
		query.setRecord(record); 
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalSitesBillNumbers.class, null);
		 
	}

	@Override
	public void createRentalDefaultRule(RentalDefaultRule defaultRule) {

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalDefaultRules.class));
		defaultRule.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalDefaultRulesRecord record = ConvertHelper.convert(defaultRule,
				EhRentalDefaultRulesRecord.class);
		InsertQuery<EhRentalDefaultRulesRecord> query = context
				.insertQuery(Tables.EH_RENTAL_DEFAULT_RULES);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalDefaultRules.class,
				null); 
		
	}

	@Override
	public void createTimeInterval(RentalTimeInterval timeInterval) {

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalTimeInterval.class));
		timeInterval.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalTimeIntervalRecord record = ConvertHelper.convert(timeInterval,
				EhRentalTimeIntervalRecord.class);
		InsertQuery<EhRentalTimeIntervalRecord> query = context
				.insertQuery(Tables.EH_RENTAL_TIME_INTERVAL);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalTimeInterval.class,
				null); 
	}

	@Override
	public void createRentalCloseDate(RentalCloseDate rcd) {

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalCloseDates.class));
		rcd.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalCloseDatesRecord record = ConvertHelper.convert(rcd,
				EhRentalCloseDatesRecord.class);
		InsertQuery<EhRentalCloseDatesRecord> query = context
				.insertQuery(Tables.EH_RENTAL_CLOSE_DATES);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalCloseDates.class,
				null); 
	}

	@Override
	public void createRentalConfigAttachment(RentalConfigAttachment rca) {

		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhRentalConfigAttachments.class));
		rca.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRentalConfigAttachmentsRecord record = ConvertHelper.convert(rca,
				EhRentalConfigAttachmentsRecord.class);
		InsertQuery<EhRentalConfigAttachmentsRecord> query = context
				.insertQuery(Tables.EH_RENTAL_CONFIG_ATTACHMENTS);
		query.setRecord(record);
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalConfigAttachments.class,
				null); 
	}

	@Override
	public RentalDefaultRule getRentalDefaultRule(String ownerType,
			Long ownerId, Long launchPadItemId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_DEFAULT_RULES);
		Condition condition = Tables.EH_RENTAL_DEFAULT_RULES.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTAL_DEFAULT_RULES.OWNER_TYPE
				.equal(ownerType));
		condition = condition.and(Tables.EH_RENTAL_DEFAULT_RULES.LAUNCH_PAD_ITEM_ID
				.equal(launchPadItemId));
		step.where(condition);
		List<RentalDefaultRule> result = step
				.orderBy(Tables.EH_RENTAL_DEFAULT_RULES.ID.desc()).fetch().map((r) -> {
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
				Tables.EH_RENTAL_TIME_INTERVAL);
		Condition condition = Tables.EH_RENTAL_TIME_INTERVAL.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTAL_TIME_INTERVAL.OWNER_TYPE
				.equal(ownerType));
		step.where(condition);
		List<RentalTimeInterval> result = step
				.orderBy(Tables.EH_RENTAL_TIME_INTERVAL.ID.desc()).fetch().map((r) -> {
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
				Tables.EH_RENTAL_TIME_INTERVAL);
		Condition condition = Tables.EH_RENTAL_TIME_INTERVAL.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTAL_TIME_INTERVAL.OWNER_TYPE
				.equal(ownerType));
		step.where(condition);
		List<RentalCloseDate> result = step
				.orderBy(Tables.EH_RENTAL_TIME_INTERVAL.ID.desc()).fetch().map((r) -> {
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
				Tables.EH_RENTAL_TIME_INTERVAL);
		Condition condition = Tables.EH_RENTAL_TIME_INTERVAL.OWNER_ID
				.equal(ownerId);
		condition = condition.and(Tables.EH_RENTAL_TIME_INTERVAL.OWNER_TYPE
				.equal(ownerType));
		step.where(condition);
		List<RentalConfigAttachment> result = step
				.orderBy(Tables.EH_RENTAL_TIME_INTERVAL.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalConfigAttachment.class);
				});
		if (null != result && result.size() > 0)
			return result;
		return null;
	}

	
}
