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
import com.everhomes.server.schema.tables.daos.EhAccumulationFundSettingsDao;
import com.everhomes.server.schema.tables.pojos.EhAccumulationFundSettings;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class AccumulationFundSettingProviderImpl implements AccumulationFundSettingProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createAccumulationFundSetting(AccumulationFundSetting accumulationFundSetting) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhAccumulationFundSettings.class));
		accumulationFundSetting.setId(id);
		accumulationFundSetting.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		accumulationFundSetting.setCreatorUid(UserContext.current().getUser().getId());
		accumulationFundSetting.setUpdateTime(accumulationFundSetting.getCreateTime());
		accumulationFundSetting.setOperatorUid(accumulationFundSetting.getCreatorUid());
		getReadWriteDao().insert(accumulationFundSetting);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhAccumulationFundSettings.class, null);
	}

	@Override
	public void updateAccumulationFundSetting(AccumulationFundSetting accumulationFundSetting) {
		assert (accumulationFundSetting.getId() != null);
		accumulationFundSetting.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		accumulationFundSetting.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(accumulationFundSetting);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhAccumulationFundSettings.class, accumulationFundSetting.getId());
	}

	@Override
	public AccumulationFundSetting findAccumulationFundSettingById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), AccumulationFundSetting.class);
	}
	
	@Override
	public List<AccumulationFundSetting> listAccumulationFundSetting() {
		return getReadOnlyContext().select().from(Tables.EH_ACCUMULATION_FUND_SETTINGS)
				.orderBy(Tables.EH_ACCUMULATION_FUND_SETTINGS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, AccumulationFundSetting.class));
	}
	
	private EhAccumulationFundSettingsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhAccumulationFundSettingsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhAccumulationFundSettingsDao getDao(DSLContext context) {
		return new EhAccumulationFundSettingsDao(context.configuration());
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
