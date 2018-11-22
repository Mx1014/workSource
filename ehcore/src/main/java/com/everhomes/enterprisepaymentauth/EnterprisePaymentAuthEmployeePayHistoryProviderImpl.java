// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterprisePaymentAuthEmployeePayHistoriesDao;
import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthEmployeePayHistories;
import com.everhomes.server.schema.tables.records.EhEnterprisePaymentAuthEmployeePayHistoriesRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
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
public class EnterprisePaymentAuthEmployeePayHistoryProviderImpl implements EnterprisePaymentAuthEmployeePayHistoryProvider {
	private static final String GET_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORY ="GetEnterprisePaymentAuthEmployeePayHistory";

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	@CacheEvict(value = GET_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORY,
			key = "{#employeePayHistory.namespaceId,#employeePayHistory.organizationId,#employeePayHistory.detailId,#employeePayHistory.payMonth}")
	public void createEnterprisePaymentAuthEmployeePayHistory(EnterprisePaymentAuthEmployeePayHistory employeePayHistory) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterprisePaymentAuthEmployeePayHistories.class));
		employeePayHistory.setId(id);
		employeePayHistory.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().insert(employeePayHistory);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterprisePaymentAuthEmployeePayHistories.class, null);
	}

	@Override
	@CacheEvict(value = GET_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORY,
			key = "{#employeePayHistory.namespaceId,#employeePayHistory.organizationId,#employeePayHistory.detailId,#employeePayHistory.payMonth}")
	public void updateEnterprisePaymentAuthEmployeePayHistory(EnterprisePaymentAuthEmployeePayHistory employeePayHistory) {
		assert (employeePayHistory.getId() != null);
		employeePayHistory.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().update(employeePayHistory);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterprisePaymentAuthEmployeePayHistories.class, employeePayHistory.getId());
	}

	@Override
	@Cacheable(value = GET_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORY, key = "{#namespaceId,#organizationId,#detailId,#payMonth}", unless = "#result == null")
	public EnterprisePaymentAuthEmployeePayHistory findEnterprisePaymentAuthEmployeePayHistoryByDetailId(Integer namespaceId, Long organizationId, Long detailId, String payMonth) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhEnterprisePaymentAuthEmployeePayHistoriesRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES);
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.ORGANIZATION_ID.eq(organizationId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.DETAIL_ID.eq(detailId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.PAY_MONTH.eq(payMonth));
		query.addLimit(1);

		EhEnterprisePaymentAuthEmployeePayHistoriesRecord record = query.fetchOne();
		return ConvertHelper.convert(record, EnterprisePaymentAuthEmployeePayHistory.class);
	}

	@Override
	public List<EnterprisePaymentAuthEmployeePayHistory> listEnterprisePaymentAuthEmployeePayHistory(Integer namespaceId, Long organizationId, String month) {
		Result<Record> record = getReadOnlyContext().select().from(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES)
				.where(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.ORGANIZATION_ID.eq(organizationId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.PAY_MONTH.eq(month))
				.orderBy(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.ID.asc())
				.fetch();
		if(record == null){
			return new ArrayList<>();
		}
		return  record.map(r -> ConvertHelper.convert(r, EnterprisePaymentAuthEmployeePayHistory.class));
	}

	@Override
	@CacheEvict(value = GET_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORY, key = "{#namespaceId,#organizationId,#detailId,#payMonth}")
	public void decrUsedAmountAndPayCount(Integer namespaceId, Long organizationId, Long detailId, BigDecimal payAmount, String payMonth) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		UpdateQuery<EhEnterprisePaymentAuthEmployeePayHistoriesRecord> updateQuery = context.updateQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES);
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.NAMESPACE_ID.eq(namespaceId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.ORGANIZATION_ID.eq(organizationId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.DETAIL_ID.eq(detailId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.PAY_MONTH.eq(payMonth));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.USED_AMOUNT, Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.USED_AMOUNT.sub(payAmount));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.PAY_COUNT, Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.PAY_COUNT.sub(1));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.OPERATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()));
		updateQuery.execute();
	}

	private EhEnterprisePaymentAuthEmployeePayHistoriesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhEnterprisePaymentAuthEmployeePayHistoriesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhEnterprisePaymentAuthEmployeePayHistoriesDao getDao(DSLContext context) {
		return new EhEnterprisePaymentAuthEmployeePayHistoriesDao(context.configuration());
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
