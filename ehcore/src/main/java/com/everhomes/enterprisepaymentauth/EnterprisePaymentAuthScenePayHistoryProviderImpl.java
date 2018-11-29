// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterprisePaymentAuthScenePayHistoriesDao;
import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthScenePayHistories;
import com.everhomes.server.schema.tables.records.EhEnterprisePaymentAuthScenePayHistoriesRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.UpdateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Component
public class EnterprisePaymentAuthScenePayHistoryProviderImpl implements EnterprisePaymentAuthScenePayHistoryProvider {
	private static final String FIND_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORY = "FindEnterprisePaymentAuthScenePayHistory";

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	@CacheEvict(value = FIND_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORY,
			key = "{#scenePayHistory.namespaceId,#scenePayHistory.organizationId,#scenePayHistory.paymentSceneAppId,#scenePayHistory.payMonth}")
	public void createEnterprisePaymentAuthScenePayHistory(EnterprisePaymentAuthScenePayHistory scenePayHistory) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterprisePaymentAuthScenePayHistories.class));
		scenePayHistory.setId(id);
		scenePayHistory.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().insert(scenePayHistory);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterprisePaymentAuthScenePayHistories.class, null);
	}

	@Override
	@CacheEvict(value = FIND_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORY,
			key = "{#scenePayHistory.namespaceId,#scenePayHistory.organizationId,#scenePayHistory.paymentSceneAppId,#scenePayHistory.payMonth}")
	public void updateEnterprisePaymentAuthScenePayHistory(EnterprisePaymentAuthScenePayHistory scenePayHistory) {
		assert (scenePayHistory.getId() != null);
		scenePayHistory.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().update(scenePayHistory);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterprisePaymentAuthScenePayHistories.class, scenePayHistory.getId());
	}

	@Override
	@Cacheable(value = FIND_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORY, key = "{#namespaceId,#organizationId,#sceneAppId,#month}", unless = "#result == null")
	public EnterprisePaymentAuthScenePayHistory findEnterprisePaymentAuthScenePayHistoryByAppId(Long organizationId, Integer namespaceId, Long sceneAppId, String month) {
		Record record = getReadOnlyContext().select().from(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORIES)
				.where(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORIES.ORGANIZATION_ID.eq(organizationId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORIES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORIES.PAY_MONTH.eq(month))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORIES.PAYMENT_SCENE_APP_ID.eq(sceneAppId))
				.orderBy(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORIES.ID.asc())
				.fetchAny();
		if (record == null || record.size() == 0) {
			return null;
		}
		return record.map(r -> ConvertHelper.convert(r, EnterprisePaymentAuthScenePayHistory.class));
	}

	@Override
	@CacheEvict(value = FIND_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORY, key = "{#namespaceId,#organizationId,#sceneAppId,#payMonth}")
	public void decrUsedAmountAndPayCount(Integer namespaceId, Long organizationId, Long sceneAppId, BigDecimal payAmount, String payMonth) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		UpdateQuery<EhEnterprisePaymentAuthScenePayHistoriesRecord> updateQuery = context.updateQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORIES);
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORIES.NAMESPACE_ID.eq(namespaceId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORIES.ORGANIZATION_ID.eq(organizationId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORIES.PAYMENT_SCENE_APP_ID.eq(sceneAppId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORIES.PAY_MONTH.eq(payMonth));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORIES.USED_AMOUNT, Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORIES.USED_AMOUNT.sub(payAmount));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORIES.PAY_COUNT, Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORIES.PAY_COUNT.sub(1));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_PAY_HISTORIES.OPERATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()));
		updateQuery.execute();
	}

	private EhEnterprisePaymentAuthScenePayHistoriesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhEnterprisePaymentAuthScenePayHistoriesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhEnterprisePaymentAuthScenePayHistoriesDao getDao(DSLContext context) {
		return new EhEnterprisePaymentAuthScenePayHistoriesDao(context.configuration());
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
