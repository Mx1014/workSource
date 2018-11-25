// @formatter:off
package com.everhomes.enterprisepaymentauth;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterprisePaymentAuthOperateLogsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthOperateLogs;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class EnterprisePaymentAuthOperateLogProviderImpl implements EnterprisePaymentAuthOperateLogProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createEnterprisePaymentAuthOperateLog(EnterprisePaymentAuthOperateLog enterprisePaymentAuthOperateLog) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterprisePaymentAuthOperateLogs.class));
		enterprisePaymentAuthOperateLog.setId(id);
		enterprisePaymentAuthOperateLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		getReadWriteDao().insert(enterprisePaymentAuthOperateLog);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterprisePaymentAuthOperateLogs.class, null);
	}

	@Override
	public List<EnterprisePaymentAuthOperateLog> listEnterprisePaymentAuthOperateLogs(Integer currentNamespaceId, Long organizationId, int pageSize, int offset) {
		Result<Record> record = getReadOnlyContext().select().from(Tables.EH_ENTERPRISE_PAYMENT_AUTH_OPERATE_LOGS)
				.where(Tables.EH_ENTERPRISE_PAYMENT_AUTH_OPERATE_LOGS.NAMESPACE_ID.eq(currentNamespaceId))
				.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_OPERATE_LOGS.ORGANIZATION_ID.eq(organizationId))
				.orderBy(Tables.EH_ENTERPRISE_PAYMENT_AUTH_OPERATE_LOGS.OPERATE_TIME.desc())
				.limit(offset, pageSize)
				.fetch();
		if(record == null){
			return new ArrayList<>();
		}
		return record.map(r -> ConvertHelper.convert(r, EnterprisePaymentAuthOperateLog.class));
	}

	private EhEnterprisePaymentAuthOperateLogsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhEnterprisePaymentAuthOperateLogsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhEnterprisePaymentAuthOperateLogsDao getDao(DSLContext context) {
		return new EhEnterprisePaymentAuthOperateLogsDao(context.configuration());
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
