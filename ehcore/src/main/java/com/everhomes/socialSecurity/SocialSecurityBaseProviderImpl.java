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
import com.everhomes.server.schema.tables.daos.EhSocialSecurityBasesDao;
import com.everhomes.server.schema.tables.pojos.EhSocialSecurityBases;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SocialSecurityBaseProviderImpl implements SocialSecurityBaseProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSocialSecurityBase(SocialSecurityBase socialSecurityBase) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecurityBases.class));
		socialSecurityBase.setId(id);
		socialSecurityBase.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecurityBase.setCreatorUid(UserContext.current().getUser().getId());
		socialSecurityBase.setUpdateTime(socialSecurityBase.getCreateTime());
		socialSecurityBase.setOperatorUid(socialSecurityBase.getCreatorUid());
		getReadWriteDao().insert(socialSecurityBase);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSocialSecurityBases.class, null);
	}

	@Override
	public void updateSocialSecurityBase(SocialSecurityBase socialSecurityBase) {
		assert (socialSecurityBase.getId() != null);
		socialSecurityBase.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecurityBase.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(socialSecurityBase);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSocialSecurityBases.class, socialSecurityBase.getId());
	}

	@Override
	public SocialSecurityBase findSocialSecurityBaseById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SocialSecurityBase.class);
	}
	
	@Override
	public List<SocialSecurityBase> listSocialSecurityBase() {
		return getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_BASES)
				.orderBy(Tables.EH_SOCIAL_SECURITY_BASES.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SocialSecurityBase.class));
	}
	
	private EhSocialSecurityBasesDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSocialSecurityBasesDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSocialSecurityBasesDao getDao(DSLContext context) {
		return new EhSocialSecurityBasesDao(context.configuration());
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
