// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterprisePaymentAuthEmployeePaySceneHistoriesDao;
import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthEmployeePaySceneHistories;
import com.everhomes.server.schema.tables.records.EhEnterprisePaymentAuthEmployeePaySceneHistoriesRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class EnterprisePaymentAuthEmployeePaySceneHistoryProviderImpl implements EnterprisePaymentAuthEmployeePaySceneHistoryProvider {
	private static final String FIND_EMPLOYEE_PAY_SCENE_HISTORY = "FindEnterprisePaymentAuthEmployeePaySceneHistory";

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	@CacheEvict(value = FIND_EMPLOYEE_PAY_SCENE_HISTORY,
			key = "{#paySceneHistory.namespaceId,#paySceneHistory.organizationId,#paySceneHistory.detailId,#paySceneHistory.paymentSceneAppId,#paySceneHistory.payMonth}")
	public void createEnterprisePaymentAuthEmployeePaySceneHistory(EnterprisePaymentAuthEmployeePaySceneHistory paySceneHistory) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterprisePaymentAuthEmployeePaySceneHistories.class));
		paySceneHistory.setId(id);
		paySceneHistory.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().insert(paySceneHistory);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterprisePaymentAuthEmployeePaySceneHistories.class, null);
	}

	@Override
	@CacheEvict(value = FIND_EMPLOYEE_PAY_SCENE_HISTORY,
			key = "{#paySceneHistory.namespaceId,#paySceneHistory.organizationId,#paySceneHistory.detailId,#paySceneHistory.paymentSceneAppId,#paySceneHistory.payMonth}")
	public void updateEnterprisePaymentAuthEmployeePaySceneHistory(EnterprisePaymentAuthEmployeePaySceneHistory paySceneHistory) {
		assert (paySceneHistory.getId() != null);
		paySceneHistory.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().update(paySceneHistory);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterprisePaymentAuthEmployeePaySceneHistories.class, paySceneHistory.getId());
	}

	@Override
	public List<EnterprisePaymentAuthEmployeePaySceneHistory> listEnterprisePaymentAuthEmployeePaySceneHistoryByDetailId(Integer namespaceId, Long organizationId, Long detailId, String payMonth) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhEnterprisePaymentAuthEmployeePaySceneHistoriesRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES);
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.ORGANIZATION_ID.eq(organizationId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.DETAIL_ID.eq(detailId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.PAY_MONTH.eq(payMonth));

		Result<EhEnterprisePaymentAuthEmployeePaySceneHistoriesRecord> result = query.fetch();
		if (result == null || result.size() == 0) {
			return new ArrayList<>();
		}
		return result.map(record -> {
			return ConvertHelper.convert(record, EnterprisePaymentAuthEmployeePaySceneHistory.class);
		});
	}

	@Override
	@Cacheable(value = FIND_EMPLOYEE_PAY_SCENE_HISTORY, key = "{#namespaceId,#organizationId,#detailId,#appId,#payMonth}", unless = "#result == null")
	public EnterprisePaymentAuthEmployeePaySceneHistory findEnterprisePaymentAuthEmployeePaySceneHistoryByDetailId(Integer namespaceId, Long organizationId, Long detailId, Long appId, String payMonth) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhEnterprisePaymentAuthEmployeePaySceneHistoriesRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES);
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.ORGANIZATION_ID.eq(organizationId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.DETAIL_ID.eq(detailId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.PAYMENT_SCENE_APP_ID.eq(appId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.PAY_MONTH.eq(payMonth));
		EhEnterprisePaymentAuthEmployeePaySceneHistoriesRecord record = query.fetchAny();
		if (record == null || record.size() == 0) {
			return null;
		}

		return record.map(r -> ConvertHelper.convert(r, EnterprisePaymentAuthEmployeePaySceneHistory.class));
	}

	@Override
	@CacheEvict(value = FIND_EMPLOYEE_PAY_SCENE_HISTORY, key = "{#namespaceId,#organizationId,#detailId,#paymentSceneAppId,#payMonth}")
	public void decrUsedAmountAndPayCount(Integer namespaceId, Long organizationId, Long detailId, Long paymentSceneAppId, BigDecimal payAmount, String payMonth) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		UpdateQuery<EhEnterprisePaymentAuthEmployeePaySceneHistoriesRecord> updateQuery = context.updateQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES);
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.NAMESPACE_ID.eq(namespaceId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.ORGANIZATION_ID.eq(organizationId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.DETAIL_ID.eq(detailId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.PAYMENT_SCENE_APP_ID.eq(paymentSceneAppId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.PAY_MONTH.eq(payMonth));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.USED_AMOUNT, Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.USED_AMOUNT.sub(payAmount));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.PAY_COUNT, Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.PAY_COUNT.sub(1));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_SCENE_HISTORIES.OPERATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()));
		updateQuery.execute();
	}

	private EhEnterprisePaymentAuthEmployeePaySceneHistoriesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhEnterprisePaymentAuthEmployeePaySceneHistoriesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhEnterprisePaymentAuthEmployeePaySceneHistoriesDao getDao(DSLContext context) {
		return new EhEnterprisePaymentAuthEmployeePaySceneHistoriesDao(context.configuration());
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
