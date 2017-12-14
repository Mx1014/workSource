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
import com.everhomes.server.schema.tables.daos.EhSocialSecuritySummaryDao;
import com.everhomes.server.schema.tables.pojos.EhSocialSecuritySummary;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SocialSecuritySummaryProviderImpl implements SocialSecuritySummaryProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSocialSecuritySummary(SocialSecuritySummary socialSecuritySummary) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecuritySummary.class));
		socialSecuritySummary.setId(id);
		socialSecuritySummary.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecuritySummary.setCreatorUid(UserContext.current().getUser().getId());
		socialSecuritySummary.setUpdateTime(socialSecuritySummary.getCreateTime());
		socialSecuritySummary.setOperatorUid(socialSecuritySummary.getCreatorUid());
		getReadWriteDao().insert(socialSecuritySummary);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSocialSecuritySummary.class, null);
	}

	@Override
	public void updateSocialSecuritySummary(SocialSecuritySummary socialSecuritySummary) {
		assert (socialSecuritySummary.getId() != null);
		socialSecuritySummary.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecuritySummary.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(socialSecuritySummary);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSocialSecuritySummary.class, socialSecuritySummary.getId());
	}

	@Override
	public SocialSecuritySummary findSocialSecuritySummaryById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SocialSecuritySummary.class);
	}
	
	@Override
	public List<SocialSecuritySummary> listSocialSecuritySummary() {
		return getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_SUMMARY)
				.orderBy(Tables.EH_SOCIAL_SECURITY_SUMMARY.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SocialSecuritySummary.class));
	}
	
	private EhSocialSecuritySummaryDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSocialSecuritySummaryDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSocialSecuritySummaryDao getDao(DSLContext context) {
		return new EhSocialSecuritySummaryDao(context.configuration());
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
