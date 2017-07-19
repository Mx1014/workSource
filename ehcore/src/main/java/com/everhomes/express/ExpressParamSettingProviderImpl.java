// @formatter:off
package com.everhomes.express;

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
import com.everhomes.server.schema.tables.daos.EhExpressParamSettingsDao;
import com.everhomes.server.schema.tables.pojos.EhExpressParamSettings;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class ExpressParamSettingProviderImpl implements ExpressParamSettingProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createExpressParamSetting(ExpressParamSetting expressParamSetting) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhExpressParamSettings.class));
		expressParamSetting.setId(id);
		expressParamSetting.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressParamSetting.setCreatorUid(UserContext.current().getUser().getId());
		expressParamSetting.setUpdateTime(expressParamSetting.getCreateTime());
		expressParamSetting.setOperatorUid(expressParamSetting.getCreatorUid());
		getReadWriteDao().insert(expressParamSetting);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhExpressParamSettings.class, null);
	}

	@Override
	public void updateExpressParamSetting(ExpressParamSetting expressParamSetting) {
		assert (expressParamSetting.getId() != null);
		expressParamSetting.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		expressParamSetting.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(expressParamSetting);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhExpressParamSettings.class, expressParamSetting.getId());
	}

	@Override
	public ExpressParamSetting findExpressParamSettingById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ExpressParamSetting.class);
	}
	
	@Override
	public List<ExpressParamSetting> listExpressParamSetting() {
		return getReadOnlyContext().select().from(Tables.EH_EXPRESS_PARAM_SETTINGS)
				.orderBy(Tables.EH_EXPRESS_PARAM_SETTINGS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ExpressParamSetting.class));
	}
	
	private EhExpressParamSettingsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhExpressParamSettingsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhExpressParamSettingsDao getDao(DSLContext context) {
		return new EhExpressParamSettingsDao(context.configuration());
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
