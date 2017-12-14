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
import com.everhomes.server.schema.tables.daos.EhSocialSecurityReportDao;
import com.everhomes.server.schema.tables.pojos.EhSocialSecurityReport;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SocialSecurityReportProviderImpl implements SocialSecurityReportProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSocialSecurityReport(SocialSecurityReport socialSecurityReport) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecurityReport.class));
		socialSecurityReport.setId(id);
		socialSecurityReport.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecurityReport.setCreatorUid(UserContext.current().getUser().getId());
		socialSecurityReport.setUpdateTime(socialSecurityReport.getCreateTime());
		socialSecurityReport.setOperatorUid(socialSecurityReport.getCreatorUid());
		getReadWriteDao().insert(socialSecurityReport);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSocialSecurityReport.class, null);
	}

	@Override
	public void updateSocialSecurityReport(SocialSecurityReport socialSecurityReport) {
		assert (socialSecurityReport.getId() != null);
		socialSecurityReport.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecurityReport.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(socialSecurityReport);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSocialSecurityReport.class, socialSecurityReport.getId());
	}

	@Override
	public SocialSecurityReport findSocialSecurityReportById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SocialSecurityReport.class);
	}
	
	@Override
	public List<SocialSecurityReport> listSocialSecurityReport() {
		return getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_REPORT)
				.orderBy(Tables.EH_SOCIAL_SECURITY_REPORT.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SocialSecurityReport.class));
	}
	
	private EhSocialSecurityReportDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSocialSecurityReportDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSocialSecurityReportDao getDao(DSLContext context) {
		return new EhSocialSecurityReportDao(context.configuration());
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
