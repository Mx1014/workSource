// @formatter:off
package com.everhomes.enterprisepaymentauth;

import ch.qos.logback.classic.Logger;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.organization.EmployeeStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterprisePaymentAuthEmployeeLimitDetailsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthEmployeeLimitDetails;
import com.everhomes.server.schema.tables.records.EhEnterprisePaymentAuthEmployeeLimitDetailsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.hp.hpl.sparta.xpath.Step;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.jooq.SelectSeekStep1;
import org.jooq.UpdateQuery;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class EnterprisePaymentAuthEmployeeLimitDetailProviderImpl implements EnterprisePaymentAuthEmployeeLimitDetailProvider {
	private static final String FIND_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAIL = "FindEnterprisePaymentAuthEmployeeLimitDetailByDetailId";
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	@CacheEvict(value = FIND_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAIL,
			key = "{#limitDetail.namespaceId,#limitDetail.organizationId,#limitDetail.detailId,#limitDetail.paymentSceneAppId}")
	public void createEnterprisePaymentAuthEmployeeLimitDetail(EnterprisePaymentAuthEmployeeLimitDetail limitDetail) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterprisePaymentAuthEmployeeLimitDetails.class));
		limitDetail.setId(id);
		limitDetail.setCreatorUid(UserContext.currentUserId());
		limitDetail.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().insert(limitDetail);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterprisePaymentAuthEmployeeLimitDetails.class, null);
	}

	@Override
	@CacheEvict(value = FIND_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAIL,
			key = "{#limitDetail.namespaceId,#limitDetail.organizationId,#limitDetail.detailId,#limitDetail.paymentSceneAppId}")
	public void updateEnterprisePaymentAuthEmployeeLimitDetail(EnterprisePaymentAuthEmployeeLimitDetail limitDetail) {
		assert (limitDetail.getId() != null);
		getReadWriteDao().update(limitDetail);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhEnterprisePaymentAuthEmployeeLimitDetails.class, limitDetail.getId());
	}

	@Override
	@CacheEvict(value = FIND_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAIL, key = "{#namespaceId,#organizationId,#detailId,#paymentSceneAppId}")
    public void incrHistoricalTotalPayAmountAndPayCount(Integer namespaceId, Long organizationId, Long detailId, Long paymentSceneAppId, BigDecimal payAmount, Integer payCount) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        UpdateQuery<EhEnterprisePaymentAuthEmployeeLimitDetailsRecord> updateQuery = context.updateQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS);
        updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.NAMESPACE_ID.eq(namespaceId));
        updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.ORGANIZATION_ID.eq(organizationId));
        updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.DETAIL_ID.eq(detailId));
        updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.PAYMENT_SCENE_APP_ID.eq(paymentSceneAppId));
        updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.HISTORICAL_TOTAL_PAY_AMOUNT, Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.HISTORICAL_TOTAL_PAY_AMOUNT.add(payAmount));
        updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.HISTORICAL_PAY_COUNT, Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.HISTORICAL_PAY_COUNT.add(payCount));
        updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.OPERATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()));
        updateQuery.execute();
    }

	@Override
	public List<EnterprisePaymentAuthEmployeeLimitDetail> listEnterprisePaymentAuthEmployeeLimitDetail(Integer namespaceId, Long organizationId, Long detailId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhEnterprisePaymentAuthEmployeeLimitDetailsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS);
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.ORGANIZATION_ID.eq(organizationId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.DETAIL_ID.eq(detailId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.IS_DELETE.eq((byte) 0));

		Result<EhEnterprisePaymentAuthEmployeeLimitDetailsRecord> records = query.fetch();
		if (records == null || records.size() == 0) {
			return new ArrayList<>();
		}
		return records.map(record -> {
			return ConvertHelper.convert(record, EnterprisePaymentAuthEmployeeLimitDetail.class);
		});
	}

	@Override
	public Integer countSceneEmployeeCount(Long organizationId, Integer currentNamespaceId, Long sceneAppId) {
		return getReadOnlyContext().selectDistinct(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.DETAIL_ID)
				.from(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS)
				.where(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.NAMESPACE_ID.eq(currentNamespaceId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.ORGANIZATION_ID.eq(organizationId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.PAYMENT_SCENE_APP_ID.eq(sceneAppId))
				//增加对离职员工的判断
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.WAIT_AUTO_DELETE_MONTH.isNull())
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.IS_DELETE.eq((byte) 0))
				.fetchCount();
	}

	@Override
	@CacheEvict(value = FIND_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAIL, allEntries = true)
	public void autoDeleteDismissEmployeePaymentAuthLimitDetail(String shouldDeleteMonth) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterprisePaymentAuthEmployeeLimitDetails.class));
		UpdateQuery<EhEnterprisePaymentAuthEmployeeLimitDetailsRecord> updateQuery = context.updateQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS);
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.WAIT_AUTO_DELETE_MONTH.eq(shouldDeleteMonth));
		updateQuery.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.IS_DELETE.eq((byte) 0));
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.IS_DELETE, (byte) 1);
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.DELETE_REASON, "employee dismiss");
		updateQuery.addValue(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.OPERATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()));
		updateQuery.execute();
	}
	@Override
	public List<EnterprisePaymentAuthEmployeeLimitDetail> listEnterprisePaymentAuthEmployeeLimitDetailByScene(Integer currentNamespaceId, Long organizationId, Long sceneAppId, int pageSize, int pageOffSet) {
		SelectSeekStep1<Record, Long> step = getReadOnlyContext().select().from(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS)
				.where(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.NAMESPACE_ID.eq(currentNamespaceId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.ORGANIZATION_ID.eq(organizationId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.PAYMENT_SCENE_APP_ID.eq(sceneAppId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.IS_DELETE.eq((byte) 0))
				//增加对离职员工的判断
				.and(DSL.notExists(DSL.select(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID)
						.from(Tables.EH_ORGANIZATION_MEMBER_DETAILS)
						.where(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.DETAIL_ID.eq(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID))
						.and(Tables.EH_ORGANIZATION_MEMBER_DETAILS.EMPLOYEE_STATUS.eq(EmployeeStatus.DISMISSAL.getCode()))))
				.orderBy(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.ID.asc());
		Result<Record> record = step.limit(pageOffSet, pageSize)
				.fetch();
		 if(record == null){
			return new ArrayList<>();
		}
		return  record.map(r -> ConvertHelper.convert(r, EnterprisePaymentAuthEmployeeLimitDetail.class));
	}

	@Override
	@Cacheable(value = FIND_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAIL, key = "{#namespaceId,#organizationId,#detailId,#sceneAppId}", unless = "#result == null")
	public EnterprisePaymentAuthEmployeeLimitDetail findEnterprisePaymentAuthEmployeeLimitDetailByDetailId(Integer namespaceId, Long organizationId, Long detailId, Long sceneAppId) {
		Record record = getReadOnlyContext().select().from(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS)
				.where(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.ORGANIZATION_ID.eq(organizationId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.DETAIL_ID.eq(detailId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.PAYMENT_SCENE_APP_ID.eq(sceneAppId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.IS_DELETE.eq((byte) 0))
				.fetchAny();
		if (record == null) {
			return null;
		}
		return record.map(r -> ConvertHelper.convert(r, EnterprisePaymentAuthEmployeeLimitDetail.class));
	}

	@Override
	@CacheEvict(value = FIND_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAIL, allEntries = true)
	public void deleteEnterprisePaymentAuthEmployeeLimitDetailsByDetailIdAndAppId(Integer namespaceId, Long organizationId, Long detailId, List<Long> deleteAppIds) {
		getReadWriteContext().update(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS)
				.set(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.IS_DELETE, (byte) 1)
				.set(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.DELETE_REASON, "admin set")
				.set(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.OPERATE_TIME, new Timestamp(DateHelper.currentGMTTime().getTime()))
				.where(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.ORGANIZATION_ID.eq(organizationId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.DETAIL_ID.eq(detailId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.PAYMENT_SCENE_APP_ID.in(deleteAppIds))
				.execute();
	}

	private EhEnterprisePaymentAuthEmployeeLimitDetailsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhEnterprisePaymentAuthEmployeeLimitDetailsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhEnterprisePaymentAuthEmployeeLimitDetailsDao getDao(DSLContext context) {
		return new EhEnterprisePaymentAuthEmployeeLimitDetailsDao(context.configuration());
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
