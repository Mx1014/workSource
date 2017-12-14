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
import com.everhomes.server.schema.tables.daos.EhSocialSecurityDepartmentSummaryDao;
import com.everhomes.server.schema.tables.pojos.EhSocialSecurityDepartmentSummary;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SocialSecurityDepartmentSummaryProviderImpl implements SocialSecurityDepartmentSummaryProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSocialSecurityDepartmentSummary(SocialSecurityDepartmentSummary socialSecurityDepartmentSummary) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecurityDepartmentSummary.class));
		socialSecurityDepartmentSummary.setId(id);
		socialSecurityDepartmentSummary.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecurityDepartmentSummary.setCreatorUid(UserContext.current().getUser().getId());
		socialSecurityDepartmentSummary.setUpdateTime(socialSecurityDepartmentSummary.getCreateTime());
		socialSecurityDepartmentSummary.setOperatorUid(socialSecurityDepartmentSummary.getCreatorUid());
		getReadWriteDao().insert(socialSecurityDepartmentSummary);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSocialSecurityDepartmentSummary.class, null);
	}

	@Override
	public void updateSocialSecurityDepartmentSummary(SocialSecurityDepartmentSummary socialSecurityDepartmentSummary) {
		assert (socialSecurityDepartmentSummary.getId() != null);
		socialSecurityDepartmentSummary.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecurityDepartmentSummary.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(socialSecurityDepartmentSummary);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSocialSecurityDepartmentSummary.class, socialSecurityDepartmentSummary.getId());
	}

	@Override
	public SocialSecurityDepartmentSummary findSocialSecurityDepartmentSummaryById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SocialSecurityDepartmentSummary.class);
	}
	
	@Override
	public List<SocialSecurityDepartmentSummary> listSocialSecurityDepartmentSummary() {
		return getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_DEPARTMENT_SUMMARY)
				.orderBy(Tables.EH_SOCIAL_SECURITY_DEPARTMENT_SUMMARY.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SocialSecurityDepartmentSummary.class));
	}
	
	private EhSocialSecurityDepartmentSummaryDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSocialSecurityDepartmentSummaryDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSocialSecurityDepartmentSummaryDao getDao(DSLContext context) {
		return new EhSocialSecurityDepartmentSummaryDao(context.configuration());
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
