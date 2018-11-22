// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterprisePaymentAuthSceneLimitsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthSceneLimits;
import com.everhomes.server.schema.tables.records.EhEnterprisePaymentAuthSceneLimitsRecord;
import com.everhomes.user.UserContext;
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
public class EnterprisePaymentAuthSceneLimitProviderImpl implements EnterprisePaymentAuthSceneLimitProvider {
	private static final String FIND_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMIT = "FindEnterprisePaymentAuthSceneLimit";

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	@CacheEvict(value = FIND_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMIT,
			key = "{#enterprisePaymentAuthSceneLimit.namespaceId,#enterprisePaymentAuthSceneLimit.organizationId,#enterprisePaymentAuthSceneLimit.paymentSceneAppId}")
	public void createEnterprisePaymentAuthSceneLimit(EnterprisePaymentAuthSceneLimit enterprisePaymentAuthSceneLimit) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterprisePaymentAuthSceneLimits.class));
		enterprisePaymentAuthSceneLimit.setId(id);
		enterprisePaymentAuthSceneLimit.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		enterprisePaymentAuthSceneLimit.setCreatorUid(UserContext.currentUserId() != null ? UserContext.currentUserId() : 0);
		enterprisePaymentAuthSceneLimit.setOperatorUid(UserContext.currentUserId() != null ? UserContext.currentUserId() : 0);
		enterprisePaymentAuthSceneLimit.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().insert(enterprisePaymentAuthSceneLimit);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterprisePaymentAuthSceneLimits.class, null);
	}

	@Override
	@CacheEvict(value = FIND_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMIT,
			key = "{#enterprisePaymentAuthSceneLimit.namespaceId,#enterprisePaymentAuthSceneLimit.organizationId,#enterprisePaymentAuthSceneLimit.paymentSceneAppId}")
	public void updateEnterprisePaymentAuthSceneLimit(EnterprisePaymentAuthSceneLimit enterprisePaymentAuthSceneLimit) {
		assert (enterprisePaymentAuthSceneLimit.getId() != null);
		enterprisePaymentAuthSceneLimit.setOperatorUid(UserContext.currentUserId());
		enterprisePaymentAuthSceneLimit.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().update(enterprisePaymentAuthSceneLimit);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterprisePaymentAuthSceneLimits.class, enterprisePaymentAuthSceneLimit.getId());
	}

	@Override
	@CacheEvict(value = FIND_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMIT, key = "{#namespaceId,#organizationId,#paymentSceneAppId}")
	public void incrHistoricalTotalPayAmountAndPayCount(Integer namespaceId, Long organizationId, Long paymentSceneAppId, BigDecimal payAmount, Integer payCount) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		UpdateQuery<EhEnterprisePaymentAuthSceneLimitsRecord> updateQuery = context.updateQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMITS);
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMITS.NAMESPACE_ID.eq(namespaceId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMITS.ORGANIZATION_ID.eq(organizationId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMITS.PAYMENT_SCENE_APP_ID.eq(paymentSceneAppId));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMITS.HISTORICAL_TOTAL_PAY_AMOUNT, Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMITS.HISTORICAL_TOTAL_PAY_AMOUNT.add(payAmount));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMITS.HISTORICAL_PAY_COUNT, Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMITS.HISTORICAL_PAY_COUNT.add(payCount));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMITS.OPERATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()));
		updateQuery.execute();
	}

	@Override
	@Cacheable(value = FIND_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMIT, key = "{#namespaceId,#organizationId,#sceneAppId}", unless = "#result == null")
	public EnterprisePaymentAuthSceneLimit findEnterprisePaymentAuthSceneLimitByAppId(Long organizationId, Integer namespaceId, Long sceneAppId) {
		Record record = getReadOnlyContext().select().from(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMITS)
				.where(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMITS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMITS.ORGANIZATION_ID.eq(organizationId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMITS.PAYMENT_SCENE_APP_ID.eq(sceneAppId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMITS.IS_DELETE.eq((byte) 0))
				.orderBy(Tables.EH_ENTERPRISE_PAYMENT_AUTH_SCENE_LIMITS.ID.asc())
				.fetchAny();
		if (record == null || record.size() == 0) {
			return null;
		}
		return record.map(r -> ConvertHelper.convert(r, EnterprisePaymentAuthSceneLimit.class));
	}

	private EhEnterprisePaymentAuthSceneLimitsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhEnterprisePaymentAuthSceneLimitsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhEnterprisePaymentAuthSceneLimitsDao getDao(DSLContext context) {
		return new EhEnterprisePaymentAuthSceneLimitsDao(context.configuration());
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
