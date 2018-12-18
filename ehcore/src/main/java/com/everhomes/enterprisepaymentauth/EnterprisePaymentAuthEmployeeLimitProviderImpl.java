// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.enterprisepaymentauth.GetEnterprisePaymentAuthInfoResponse;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterprisePaymentAuthEmployeeLimitsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthEmployeeLimits;
import com.everhomes.server.schema.tables.records.EhEnterprisePaymentAuthEmployeeLimitsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class EnterprisePaymentAuthEmployeeLimitProviderImpl implements EnterprisePaymentAuthEmployeeLimitProvider {
	private static final String FIND_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_BY_DETAIL_ID = "FindEnterprisePaymentAuthEmployeeLimitByDetailId";
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	@CacheEvict(value = FIND_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_BY_DETAIL_ID, key = "{#employeeLimit.namespaceId,#employeeLimit.organizationId,#employeeLimit.detailId}")
	public void createEnterprisePaymentAuthEmployeeLimit(EnterprisePaymentAuthEmployeeLimit employeeLimit) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterprisePaymentAuthEmployeeLimits.class));
		employeeLimit.setId(id);
		employeeLimit.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().insert(employeeLimit);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterprisePaymentAuthEmployeeLimits.class, null);
	}

	@Override
	@CacheEvict(value = FIND_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_BY_DETAIL_ID, key = "{#employeeLimit.namespaceId,#employeeLimit.organizationId,#employeeLimit.detailId}")
	public void updateEnterprisePaymentAuthEmployeeLimit(EnterprisePaymentAuthEmployeeLimit employeeLimit) {
		assert (employeeLimit.getId() != null);
		getReadWriteDao().update(employeeLimit);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterprisePaymentAuthEmployeeLimits.class, employeeLimit.getId());
	}

	@Override
	@Cacheable(value = FIND_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_BY_DETAIL_ID, key = "{#namespaceId,#organizationId,#detailId}", unless = "#result == null")
	public EnterprisePaymentAuthEmployeeLimit findEnterprisePaymentAuthEmployeeLimitByDetailId(Integer namespaceId, Long organizationId, Long detailId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhEnterprisePaymentAuthEmployeeLimitsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS);
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.ORGANIZATION_ID.eq(organizationId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.DETAIL_ID.eq(detailId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.IS_DELETE.eq((byte) 0));
		query.addLimit(1);

		EhEnterprisePaymentAuthEmployeeLimitsRecord record = query.fetchOne();
		return ConvertHelper.convert(record, EnterprisePaymentAuthEmployeeLimit.class);
	}

	@Override
	public GetEnterprisePaymentAuthInfoResponse countEnterpriseAuthInfo(Long organizationId) {
		SimpleDateFormat monthSF = new SimpleDateFormat("YYYYMM");
		GetEnterprisePaymentAuthInfoResponse response = new GetEnterprisePaymentAuthInfoResponse();
        Record3<BigDecimal, BigDecimal, BigDecimal> records = getReadOnlyContext()
                .select(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.LIMIT_AMOUNT.sum(),
                        Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.HISTORICAL_TOTAL_PAY_AMOUNT.sum(),
                        Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.HISTORICAL_PAY_COUNT.sum())
                .from(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS)
                .where(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()))
                .and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.ORGANIZATION_ID.eq(organizationId)).fetchAny();
        response.setLimitAmount(records.value1());
        response.setHistoricalTotalPayAmount(records.value2());
        response.setHistoricalPayCount(records.value3() == null ? 0 : records.value3().intValue());
        Record1<BigDecimal> record1 = getReadOnlyContext().select(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.USED_AMOUNT.sum())
                .from(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES)
                .where(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()))
                .and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.ORGANIZATION_ID.eq(organizationId))
                .and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_PAY_HISTORIES.PAY_MONTH.eq(monthSF.format(DateHelper.currentGMTTime())))
                .fetchAny();
        if(response.getLimitAmount() == null ){
        	response.setCurrentMonthRemainAmount(null);
        }else if(record1 == null || record1.value1() == null){
        	response.setCurrentMonthRemainAmount(response.getLimitAmount());
        }else{        	
        	response.setCurrentMonthRemainAmount(response.getLimitAmount().subtract(record1.value1()));
        }
		return response;
	}

	@Override
	public List<EnterprisePaymentAuthEmployeeLimit> listEnterprisePaymentAuthEmployeeLimit(Integer currentNamespaceId, Long organizationId) {

		Result<Record> record = getReadOnlyContext().select().from(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS)
				.where(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.NAMESPACE_ID.eq(currentNamespaceId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.ORGANIZATION_ID.eq(organizationId))
				.orderBy(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.ID.asc())
				.fetch();
		if(record == null){
			return new ArrayList<>();
		}
		return  record.map(r -> ConvertHelper.convert(r, EnterprisePaymentAuthEmployeeLimit.class));
	}

	@Override
	@CacheEvict(value = FIND_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_BY_DETAIL_ID, allEntries = true)
	public void autoDeleteDismissEmployeePaymentAuthLimit(String shouldDeleteMonth) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterprisePaymentAuthEmployeeLimits.class));
		UpdateQuery<EhEnterprisePaymentAuthEmployeeLimitsRecord> updateQuery = context.updateQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS);
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.WAIT_AUTO_DELETE_MONTH.eq(shouldDeleteMonth));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.IS_DELETE.eq((byte) 0));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.IS_DELETE, (byte) 1);
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.DELETE_REASON, "employee dismiss");
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.OPERATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()));
		updateQuery.execute();
	}

	@Override
	public void markAutoDeleteDismissEmployeePaymentAuthLimit(Integer namespaceId, Long organizationId, Long detailId, String waitAutoDeleteMonth) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterprisePaymentAuthEmployeeLimits.class));
		UpdateQuery<EhEnterprisePaymentAuthEmployeeLimitsRecord> updateQuery = context.updateQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS);
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.NAMESPACE_ID.eq(namespaceId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.ORGANIZATION_ID.eq(organizationId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.DETAIL_ID.eq(detailId));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.WAIT_AUTO_DELETE_MONTH, waitAutoDeleteMonth);
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.OPERATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()));
		updateQuery.execute();
	}

	@Override
	@CacheEvict(value = FIND_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_BY_DETAIL_ID, key = "{#namespaceId,#organizationId,#detailId}")
	public void incrHistoricalTotalPayAmountAndPayCount(Integer namespaceId, Long organizationId, Long detailId, BigDecimal payAmount, Integer payCount) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		UpdateQuery<EhEnterprisePaymentAuthEmployeeLimitsRecord> updateQuery = context.updateQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS);
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.NAMESPACE_ID.eq(namespaceId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.ORGANIZATION_ID.eq(organizationId));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.DETAIL_ID.eq(detailId));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.HISTORICAL_TOTAL_PAY_AMOUNT, Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.HISTORICAL_TOTAL_PAY_AMOUNT.add(payAmount));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.HISTORICAL_PAY_COUNT, Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.HISTORICAL_PAY_COUNT.add(payCount));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMITS.OPERATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()));
		updateQuery.execute();
	}

	private EhEnterprisePaymentAuthEmployeeLimitsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhEnterprisePaymentAuthEmployeeLimitsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhEnterprisePaymentAuthEmployeeLimitsDao getDao(DSLContext context) {
		return new EhEnterprisePaymentAuthEmployeeLimitsDao(context.configuration());
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
