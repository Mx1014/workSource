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
import com.everhomes.server.schema.tables.daos.EhSocialSecuritySettingsDao;
import com.everhomes.server.schema.tables.pojos.EhSocialSecuritySettings;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SocialSecuritySettingProviderImpl implements SocialSecuritySettingProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSocialSecuritySetting(SocialSecuritySetting socialSecuritySetting) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSocialSecuritySettings.class));
		socialSecuritySetting.setId(id);
		socialSecuritySetting.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecuritySetting.setCreatorUid(UserContext.current().getUser().getId());
		socialSecuritySetting.setUpdateTime(socialSecuritySetting.getCreateTime());
		socialSecuritySetting.setOperatorUid(socialSecuritySetting.getCreatorUid());
		getReadWriteDao().insert(socialSecuritySetting);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSocialSecuritySettings.class, null);
	}

	@Override
	public void updateSocialSecuritySetting(SocialSecuritySetting socialSecuritySetting) {
		assert (socialSecuritySetting.getId() != null);
		socialSecuritySetting.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		socialSecuritySetting.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(socialSecuritySetting);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSocialSecuritySettings.class, socialSecuritySetting.getId());
	}

	@Override
	public SocialSecuritySetting findSocialSecuritySettingById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SocialSecuritySetting.class);
	}
	
	@Override
	public List<SocialSecuritySetting> listSocialSecuritySetting() {
		return getReadOnlyContext().select().from(Tables.EH_SOCIAL_SECURITY_SETTINGS)
				.orderBy(Tables.EH_SOCIAL_SECURITY_SETTINGS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SocialSecuritySetting.class));
	}
	
	private EhSocialSecuritySettingsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSocialSecuritySettingsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSocialSecuritySettingsDao getDao(DSLContext context) {
		return new EhSocialSecuritySettingsDao(context.configuration());
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
