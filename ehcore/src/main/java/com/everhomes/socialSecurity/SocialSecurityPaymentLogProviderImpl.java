// @formatter:off
package com.everhomes.socialSecurity;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSocialSecurityPaymentLogsDao;
import com.everhomes.server.schema.tables.pojos.EhSocialSecurityPaymentLogs;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SocialSecurityPaymentLogProviderImpl implements SocialSecurityPaymentLogProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSocialSecurityPaymentLog(SocialSecurityPaymentLog socialSecurityPaymentLog) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecurityPaymentLogs.class));
		socialSecurityPaymentLog.setId(id);
		socialSecurityPaymentLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecurityPaymentLog.setCreatorUid(UserContext.current().getUser().getId());
		socialSecurityPaymentLog.setUpdateTime(socialSecurityPaymentLog.getCreateTime());
		socialSecurityPaymentLog.setOperatorUid(socialSecurityPaymentLog.getCreatorUid());
		getReadWriteDao().insert(socialSecurityPaymentLog);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSocialSecurityPaymentLogs.class, null);
	}

	@Override
	public void updateSocialSecurityPaymentLog(SocialSecurityPaymentLog socialSecurityPaymentLog) {
		assert (socialSecurityPaymentLog.getId() != null);
		socialSecurityPaymentLog.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecurityPaymentLog.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(socialSecurityPaymentLog);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSocialSecurityPaymentLogs.class, socialSecurityPaymentLog.getId());
	}

	@Override
	public SocialSecurityPaymentLog findSocialSecurityPaymentLogById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SocialSecurityPaymentLog.class);
	}
	
	@Override
	public List<SocialSecurityPaymentLog> listSocialSecurityPaymentLog() {
		return getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_PAYMENT_LOGS)
				.orderBy(Tables.EH_SOCIAL_SECURITY_PAYMENT_LOGS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SocialSecurityPaymentLog.class));
	}
	
	private EhSocialSecurityPaymentLogsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSocialSecurityPaymentLogsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSocialSecurityPaymentLogsDao getDao(DSLContext context) {
		return new EhSocialSecurityPaymentLogsDao(context.configuration());
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
