// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterprisePaymentAuthPayLogsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthPayLogs;
import com.everhomes.server.schema.tables.records.EhEnterprisePaymentAuthPayLogsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class EnterprisePaymentAuthPayLogProviderImpl implements EnterprisePaymentAuthPayLogProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createEnterprisePaymentAuthPayLog(EnterprisePaymentAuthPayLog enterprisePaymentAuthPayLog) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterprisePaymentAuthPayLogs.class));
		enterprisePaymentAuthPayLog.setId(id);
		enterprisePaymentAuthPayLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
		getReadWriteDao().insert(enterprisePaymentAuthPayLog);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterprisePaymentAuthPayLogs.class, null);
	}

	@Override
	public List<EnterprisePaymentAuthPayLog> listEnterprisePaymentAuthPayLogByDetailId(Integer namespaceId, Long organizationId, Long detailId, Integer offset, Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhEnterprisePaymentAuthPayLogsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_PAY_LOGS);
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_PAY_LOGS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_PAY_LOGS.ORGANIZATION_ID.eq(organizationId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_PAY_LOGS.DETAIL_ID.eq(detailId));
		query.addOrderBy(Tables.EH_ENTERPRISE_PAYMENT_AUTH_PAY_LOGS.PAY_TIME.desc());

		if (offset != null && pageSize != null) {
			query.addLimit(offset, pageSize);
		}

		Result<EhEnterprisePaymentAuthPayLogsRecord> result = query.fetch();

		if (result == null || result.size() == 0) {
			return new ArrayList<>();
		}
		return result.map(record -> {
			return ConvertHelper.convert(record, EnterprisePaymentAuthPayLog.class);
		});
	}

	@Override
	public List<EnterprisePaymentAuthPayLog> listEnterprisePaymentAuthPayLogs(Integer currentNamespaceId, Long organizationId, Long paymentSceneAppId, Timestamp paymentStartDate, Timestamp paymentEndDate, List<Long> detailIds, Long orderId, Integer offset, Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhEnterprisePaymentAuthPayLogsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_PAY_LOGS);
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_PAY_LOGS.NAMESPACE_ID.eq(currentNamespaceId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_PAY_LOGS.ORGANIZATION_ID.eq(organizationId));
		if (paymentStartDate != null && paymentEndDate != null) {
			query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_PAY_LOGS.PAY_TIME.between(paymentStartDate, paymentEndDate));
		}
		if (detailIds != null) {
			query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_PAY_LOGS.DETAIL_ID.in(detailIds));
		}
		if (orderId != null) {
			query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_PAY_LOGS.MERCHANT_ORDER_ID.eq(orderId));
		}
		if (paymentSceneAppId != null) {
			query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_PAY_LOGS.PAYMENT_SCENE_APP_ID.eq(paymentSceneAppId));
		}
		query.addOrderBy(Tables.EH_ENTERPRISE_PAYMENT_AUTH_PAY_LOGS.PAY_TIME.desc());

		if (offset != null && pageSize != null) {
			query.addLimit(offset, pageSize);
		}

		Result<EhEnterprisePaymentAuthPayLogsRecord> result = query.fetch();

		if (result == null || result.size() == 0) {
			return new ArrayList<>();
		}
		return result.map(record -> {
			return ConvertHelper.convert(record, EnterprisePaymentAuthPayLog.class);
		});
	}

	private EhEnterprisePaymentAuthPayLogsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhEnterprisePaymentAuthPayLogsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhEnterprisePaymentAuthPayLogsDao getDao(DSLContext context) {
		return new EhEnterprisePaymentAuthPayLogsDao(context.configuration());
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
