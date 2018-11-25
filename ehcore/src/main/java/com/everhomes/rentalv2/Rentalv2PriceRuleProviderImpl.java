// @formatter:off
package com.everhomes.rentalv2;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.server.schema.tables.daos.EhRentalv2PriceClassificationDao;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.rentalv2.admin.PriceRuleDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhRentalv2PriceRulesDao;
import com.everhomes.server.schema.tables.pojos.EhRentalv2PriceRules;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class Rentalv2PriceRuleProviderImpl implements Rentalv2PriceRuleProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createRentalv2PriceRule(Rentalv2PriceRule rentalv2PriceRule) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhRentalv2PriceRules.class));
		rentalv2PriceRule.setId(id);
		rentalv2PriceRule.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		rentalv2PriceRule.setCreatorUid(UserContext.current().getUser().getId());
		if (rentalv2PriceRule.getCellBeginId() == null) {
			rentalv2PriceRule.setCellBeginId(0L);
		}
		if (rentalv2PriceRule.getCellEndId() == null) {
			rentalv2PriceRule.setCellEndId(0L);
		}
		getReadWriteDao().insert(rentalv2PriceRule);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRentalv2PriceRules.class, null);
	}

	@Override
	public void createRentalv2PriceClassification(RentalPriceClassification classification) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(RentalPriceClassification.class));
		classification.setId(id);
		EhRentalv2PriceClassificationDao dao = new EhRentalv2PriceClassificationDao(getReadWriteContext().configuration());
		dao.insert(classification);
		DaoHelper.publishDaoAction(DaoAction.CREATE, RentalPriceClassification.class, null);
	}

	@Override
	public void updateRentalv2PriceRule(Rentalv2PriceRule rentalv2PriceRule) {
		assert (rentalv2PriceRule.getId() != null);
		getReadWriteDao().update(rentalv2PriceRule);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhRentalv2PriceRules.class, rentalv2PriceRule.getId());
	}

	@Override
	public void deletePriceRuleByOwnerId(String resourceType, String ownerType, Long ownerId) {
		getReadWriteContext().delete(Tables.EH_RENTALV2_PRICE_RULES)
			.where(Tables.EH_RENTALV2_PRICE_RULES.OWNER_TYPE.eq(ownerType))
			.and(Tables.EH_RENTALV2_PRICE_RULES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_RENTALV2_PRICE_RULES.RESOURCE_TYPE.eq(resourceType))
			.execute();
	}

	@Override
	public Rentalv2PriceRule findRentalv2PriceRuleById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), Rentalv2PriceRule.class);
	}
	
	@Override
	public Rentalv2PriceRule findRentalv2PriceRuleByOwner(String resourceType, String ownerType, Long ownerId, Byte rentalType) {
		Record record = getReadOnlyContext().select().from(Tables.EH_RENTALV2_PRICE_RULES)
				.where(Tables.EH_RENTALV2_PRICE_RULES.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_RENTALV2_PRICE_RULES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_RENTALV2_PRICE_RULES.RENTAL_TYPE.eq(rentalType))
				.and(Tables.EH_RENTALV2_PRICE_RULES.RESOURCE_TYPE.eq(resourceType))
				.fetchOne();
		return record == null ? null : ConvertHelper.convert(record, Rentalv2PriceRule.class);
	}

	@Override
	public List<Rentalv2PriceRule> listRentalv2PriceRule() {
		return getReadOnlyContext().select().from(Tables.EH_RENTALV2_PRICE_RULES)
				.orderBy(Tables.EH_RENTALV2_PRICE_RULES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, Rentalv2PriceRule.class));
	}
	
	@Override
	public List<Rentalv2PriceRule> listPriceRuleByOwner(String resourceType, String ownerType, Long ownerId) {
		return getReadOnlyContext().select().from(Tables.EH_RENTALV2_PRICE_RULES)
				.where(Tables.EH_RENTALV2_PRICE_RULES.OWNER_TYPE.eq(ownerType))
				.and(Tables.EH_RENTALV2_PRICE_RULES.OWNER_ID.eq(ownerId))
				.and(Tables.EH_RENTALV2_PRICE_RULES.RESOURCE_TYPE.eq(resourceType))
				.orderBy(Tables.EH_RENTALV2_PRICE_RULES.RENTAL_TYPE.asc())
				.fetch().map(r -> ConvertHelper.convert(r, Rentalv2PriceRule.class));
	}

	private EhRentalv2PriceRulesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhRentalv2PriceRulesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhRentalv2PriceRulesDao getDao(DSLContext context) {
		return new EhRentalv2PriceRulesDao(context.configuration());
	}

	private DSLContext getReadWriteContext() {
		return getContext(AccessSpec.readWrite());
	}

	private DSLContext getReadOnlyContext() {
		return getContext(AccessSpec.readOnly());
	}

	private DSLContext getContext(AccessSpec accessSpec) {
		return dbProvider.getDslContext(accessSpec);
	}
}
