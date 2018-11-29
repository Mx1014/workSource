// @formatter:off
package com.everhomes.enterprisepaymentauth;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.everhomes.user.UserContext;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.enterprisepaymentauth.EmployeePaymentLimitChangeLogsGroupDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterprisePaymentAuthEmployeeLimitChangeLogsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthEmployeeLimitChangeLogs;
import com.everhomes.server.schema.tables.records.EhEnterprisePaymentAuthEmployeeLimitChangeLogsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class EnterprisePaymentAuthEmployeeLimitChangeLogProviderImpl implements EnterprisePaymentAuthEmployeeLimitChangeLogProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public List<EmployeePaymentLimitChangeLogsGroupDTO> listEmployeePaymentLimitChangeLogsGroups(Integer namespaceId, Long organizationId, Long detailId, Integer offset, Integer pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectConditionStep<Record3<Long, Timestamp, Long>> query = context.select(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_CHANGE_LOGS.OPERATOR_UID,
				Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_CHANGE_LOGS.OPERATE_TIME, Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_CHANGE_LOGS.OPERATE_NO).from(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_CHANGE_LOGS)
				.where(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_CHANGE_LOGS.NAMESPACE_ID.eq(namespaceId)
						.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_CHANGE_LOGS.ORGANIZATION_ID.eq(organizationId))
						.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_CHANGE_LOGS.DETAIL_ID.eq(detailId)));

		query.groupBy(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_CHANGE_LOGS.OPERATE_NO);
		query.orderBy(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_CHANGE_LOGS.OPERATE_NO.desc());
		if (offset != null && pageSize != null) {
			query.limit(offset, pageSize);
		}
		Result<Record3<Long, Timestamp, Long>> result = query.fetch();

		if (result == null || result.size() == 0) {
			return new ArrayList<>();
		}
		return result.map(record -> {
			EmployeePaymentLimitChangeLogsGroupDTO dto = new EmployeePaymentLimitChangeLogsGroupDTO();
			dto.setOperatorUid(record.value1());
			dto.setOperateTime(record.value2().getTime());
			dto.setOperateNo(record.value3());
			return dto;
		});
	}

	@Override
	public List<EnterprisePaymentAuthEmployeeLimitChangeLog> listEnterprisePaymentAuthEmployeeLimitChangeLog(Integer namespaceId, Long organizationId, Long detailId, List<Long> operateNoList) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhEnterprisePaymentAuthEmployeeLimitChangeLogsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_CHANGE_LOGS);
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_CHANGE_LOGS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_CHANGE_LOGS.ORGANIZATION_ID.eq(organizationId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_CHANGE_LOGS.DETAIL_ID.eq(detailId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_CHANGE_LOGS.OPERATE_NO.in(operateNoList));

		query.addOrderBy(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_CHANGE_LOGS.OPERATE_TIME.desc());

		Result<EhEnterprisePaymentAuthEmployeeLimitChangeLogsRecord> result = query.fetch();
		if (result == null || result.size() == 0) {
			return new ArrayList<>();
		}
		return result.map(record -> {
			return ConvertHelper.convert(record, EnterprisePaymentAuthEmployeeLimitChangeLog.class);
		});
	}

	@Override
	public void batchCreateEnterprisePaymentAuthEmployeeLimitChangeLog(List<EnterprisePaymentAuthEmployeeLimitChangeLog> changeLogList) {
		if (CollectionUtils.isEmpty(changeLogList)) {
			return;
		}
		Long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhEnterprisePaymentAuthEmployeeLimitChangeLogs.class), changeLogList.size());
		Long operateNo = id + 1;
		Timestamp createTime = new Timestamp(DateHelper.currentGMTTime().getTime());
		List<EhEnterprisePaymentAuthEmployeeLimitChangeLogs> logs = new ArrayList<>();
		for (EnterprisePaymentAuthEmployeeLimitChangeLog changeLog : changeLogList) {
			EhEnterprisePaymentAuthEmployeeLimitChangeLogs log = ConvertHelper.convert(changeLog, EhEnterprisePaymentAuthEmployeeLimitChangeLogs.class);
			log.setId(++id);
			log.setOperateNo(operateNo);
			log.setOperatorUid(UserContext.currentUserId());
			log.setOperateTime(createTime);
			logs.add(log);
		}
		getReadWriteDao().insert(logs);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterprisePaymentAuthEmployeeLimitChangeLogs.class, null);

	}

	private EhEnterprisePaymentAuthEmployeeLimitChangeLogsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhEnterprisePaymentAuthEmployeeLimitChangeLogsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhEnterprisePaymentAuthEmployeeLimitChangeLogsDao getDao(DSLContext context) {
		return new EhEnterprisePaymentAuthEmployeeLimitChangeLogsDao(context.configuration());
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
