package com.everhomes.techpark.rental;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteWhereStep;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectJoinStep;
import org.jooq.SelectOnConditionStep;
import org.jooq.UpdateConditionStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhRentalRulesDao;
import com.everhomes.server.schema.tables.daos.EhRentalSitesDao;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhRentalBills;
import com.everhomes.server.schema.tables.pojos.EhRentalItemsBills;
import com.everhomes.server.schema.tables.pojos.EhRentalRules;
import com.everhomes.server.schema.tables.pojos.EhRentalSiteItems;
import com.everhomes.server.schema.tables.pojos.EhRentalSiteRules;
import com.everhomes.server.schema.tables.pojos.EhRentalSites;
import com.everhomes.server.schema.tables.pojos.EhRentalSitesBills;
import com.everhomes.server.schema.tables.records.EhRentalBillsRecord;
import com.everhomes.server.schema.tables.records.EhRentalItemsBillsRecord;
import com.everhomes.server.schema.tables.records.EhRentalSiteItemsRecord;
import com.everhomes.server.schema.tables.records.EhRentalSiteRulesRecord;
import com.everhomes.server.schema.tables.records.EhRentalSitesBillsRecord;
import com.everhomes.server.schema.tables.records.EhRentalSitesRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class RentalProviderImpl implements RentalProvider {

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
	public RentalRule getRentalRule(Long enterpriseCommunityId, String siteType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_RULES);
		Condition condition = Tables.EH_RENTAL_RULES.ENTERPRISE_COMMUNITY_ID
				.equal(enterpriseCommunityId);
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
	public List<RentalSite> findRentalSites(Long enterpriseCommunityId,
			String siteType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_SITES);
		Condition condition = Tables.EH_RENTAL_SITES.ENTERPRISE_COMMUNITY_ID
				.equal(enterpriseCommunityId);
		condition = condition.and(Tables.EH_RENTAL_SITES.SITE_TYPE
				.equal(siteType));
		step.where(condition);
		List<RentalSite> result = step
				.orderBy(Tables.EH_RENTAL_SITES.ID.desc()).fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalSite.class);
				});

		return result;
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
			String ruleDate, Timestamp beginDate) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_SITE_RULES);
		Condition condition = Tables.EH_RENTAL_SITE_RULES.SITE_RENTAL_DATE
				.equal(Date.valueOf(ruleDate));
		if (null != rentalSiteId) {
			condition = condition
					.and(Tables.EH_RENTAL_SITE_RULES.RENTAL_SITE_ID
							.equal(rentalSiteId));
		}
		if (null != beginDate) {
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
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_SITES_BILLS);
		Condition condition = Tables.EH_RENTAL_SITES_BILLS.RENTAL_SITE_RULE_ID
				.equal(siteRuleId);

		step.where(condition);
		List<RentalSitesBill> result = step
				.orderBy(Tables.EH_RENTAL_SITES_BILLS.ID.desc()).fetch()
				.map((r) -> {
					return ConvertHelper.convert(r, RentalSitesBill.class);
				});

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
	public List<RentalBill> listRentalBills(ListingLocator locator, int count,
			Byte status) {
		final List<RentalBill> result = new ArrayList<RentalBill>();
		Condition condition = Tables.EH_RENTAL_BILLS.ID.lt(locator.getAnchor());
		if (null != status)
			condition = condition.and(Tables.EH_RENTAL_BILLS.STATUS.eq(status));
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
		// TODO Auto-generated method stub
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

	@Override
	public Integer countRentalSiteBills(Long rentalSiteId, Long beginDate,
			Long endDate) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectOnConditionStep<Record1<Integer>> step = context
				.selectCount().from(Tables.EH_RENTAL_SITE_RULES).join(Tables.EH_RENTAL_SITES_BILLS)
				.on(Tables.EH_RENTAL_SITE_RULES.ID.eq(Tables.EH_RENTAL_SITES_BILLS.RENTAL_SITE_RULE_ID))
				.join(Tables.EH_RENTAL_BILLS).on(Tables.EH_RENTAL_BILLS.ID.eq(Tables.EH_RENTAL_SITES_BILLS.RENTAL_BILL_ID));
		Condition condition = Tables.EH_RENTAL_SITE_RULES.RENTAL_SITE_ID
				.equal(rentalSiteId);
		condition = condition.and(Tables.EH_RENTAL_BILLS.STATUS.ne(SiteBillStatus.FAIL.getCode()));
		if (null != beginDate && null != endDate) {
			condition = condition
					.and(Tables.EH_RENTAL_SITE_RULES.SITE_RENTAL_DATE.between(
							new Date(beginDate), new Date(endDate)));
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
}
