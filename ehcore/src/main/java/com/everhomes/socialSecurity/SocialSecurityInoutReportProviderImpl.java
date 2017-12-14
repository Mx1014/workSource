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
import com.everhomes.server.schema.tables.daos.EhSocialSecurityInoutReportDao;
import com.everhomes.server.schema.tables.pojos.EhSocialSecurityInoutReport;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SocialSecurityInoutReportProviderImpl implements SocialSecurityInoutReportProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSocialSecurityInoutReport(SocialSecurityInoutReport socialSecurityInoutReport) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecurityInoutReport.class));
		socialSecurityInoutReport.setId(id);
		socialSecurityInoutReport.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecurityInoutReport.setCreatorUid(UserContext.current().getUser().getId());
		socialSecurityInoutReport.setUpdateTime(socialSecurityInoutReport.getCreateTime());
		socialSecurityInoutReport.setOperatorUid(socialSecurityInoutReport.getCreatorUid());
		getReadWriteDao().insert(socialSecurityInoutReport);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSocialSecurityInoutReport.class, null);
	}

	@Override
	public void updateSocialSecurityInoutReport(SocialSecurityInoutReport socialSecurityInoutReport) {
		assert (socialSecurityInoutReport.getId() != null);
		socialSecurityInoutReport.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecurityInoutReport.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(socialSecurityInoutReport);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSocialSecurityInoutReport.class, socialSecurityInoutReport.getId());
	}

	@Override
	public SocialSecurityInoutReport findSocialSecurityInoutReportById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SocialSecurityInoutReport.class);
	}
	
	@Override
	public List<SocialSecurityInoutReport> listSocialSecurityInoutReport() {
		return getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_INOUT_REPORT)
				.orderBy(Tables.EH_SOCIAL_SECURITY_INOUT_REPORT.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SocialSecurityInoutReport.class));
	}
	
	private EhSocialSecurityInoutReportDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSocialSecurityInoutReportDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSocialSecurityInoutReportDao getDao(DSLContext context) {
		return new EhSocialSecurityInoutReportDao(context.configuration());
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
