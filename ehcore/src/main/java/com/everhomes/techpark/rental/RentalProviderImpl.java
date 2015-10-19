package com.everhomes.techpark.rental;

import java.sql.Date;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhRentalRulesDao;
import com.everhomes.server.schema.tables.pojos.EhRentalRules;
import com.everhomes.server.schema.tables.pojos.EhRentalSiteItems;
import com.everhomes.server.schema.tables.pojos.EhRentalSiteRules;
import com.everhomes.server.schema.tables.pojos.EhRentalSites;
import com.everhomes.server.schema.tables.records.EhRentalSiteItemsRecord;
import com.everhomes.server.schema.tables.records.EhRentalSiteRulesRecord;
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
		List<String> result =step.fetch().map((r) -> {
			return ConvertHelper.convert(r, String.class);
		});
		return result;
	}
	
	@Override
	public RentalRule getRentalRule(Long enterpriseCommunityId, String siteType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_RULES);
		Condition condition = Tables.EH_RENTAL_RULES.ENTERPRISE_COMMUNITY_ID.equal(enterpriseCommunityId);
		 condition = condition.and(Tables.EH_RENTAL_RULES.SITE_TYPE.equal(siteType));
		step.where(condition);
		List<RentalRule> result = step.orderBy(Tables.EH_RENTAL_RULES.ID.desc())
				.fetch().map((r) -> {
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
		EhRentalRulesDao dao = new EhRentalRulesDao(
				context.configuration());
		dao.update(rentalRule);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRentalRules.class,
				rentalRule.getId());
	}

	@Override
	public void createRentalSite(RentalSite rentalsite) {
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

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalSites.class,
				null);
	}

	@Override
	public List<RentalSite> findRentalSites(Long enterpriseCommunityId,
			String siteType) { 
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_SITES);
		Condition condition = Tables.EH_RENTAL_SITES.ENTERPRISE_COMMUNITY_ID.equal(enterpriseCommunityId);
		condition = condition.and(Tables.EH_RENTAL_SITES.SITE_TYPE.equal(siteType));
		step.where(condition);
		List<RentalSite> result = step.orderBy(Tables.EH_RENTAL_SITES.ID.desc())
				.fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalSite.class);
				});
		 
		return result;
	}

	@Override
	public List<RentalSiteItem> findRentalSiteItems(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(
				Tables.EH_RENTAL_SITE_ITEMS);
		Condition condition = Tables.EH_RENTAL_SITE_ITEMS.RENTAL_SITE_ID.equal(id); 
		step.where(condition);
		List<RentalSiteItem> result = step.orderBy(Tables.EH_RENTAL_SITE_ITEMS.ID.desc())
				.fetch().map((r) -> {
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
			String ruleDate, Byte loopType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectJoinStep<Record> step = context.select().from(Tables.EH_RENTAL_SITE_RULES);
		Condition condition = Tables.EH_RENTAL_SITE_RULES.RENTAL_SITE_ID.equal(rentalSiteId); 
		if(null != ruleDate){
			condition = condition.and( Tables.EH_RENTAL_SITE_RULES.RULE_DATE.equal(Date.valueOf(ruleDate))); 
		}
		if(null != loopType){
			condition = condition.and( Tables.EH_RENTAL_SITE_RULES.LOOP_TYPE.equal(loopType)); 
		}
		step.where(condition);
		List<RentalSiteRule> result = step.orderBy(Tables.EH_RENTAL_SITE_RULES.ID.desc())
				.fetch().map((r) -> {
					return ConvertHelper.convert(r, RentalSiteRule.class);
				});
		 
		return result;
	}

}
