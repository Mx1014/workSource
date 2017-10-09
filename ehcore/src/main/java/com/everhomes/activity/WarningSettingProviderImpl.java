// @formatter:off
package com.everhomes.activity;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhWarningSettingsDao;
import com.everhomes.server.schema.tables.pojos.EhWarningSettings;
import com.everhomes.util.ConvertHelper;

@Component
public class WarningSettingProviderImpl implements WarningSettingProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createWarningSetting(WarningSetting warningSetting) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWarningSettings.class));
		warningSetting.setId(id);
		getReadWriteDao().insert(warningSetting);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhWarningSettings.class, null);
	}

	@Override
	public void updateWarningSetting(WarningSetting warningSetting) {
		assert (warningSetting.getId() != null);
		getReadWriteDao().update(warningSetting);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhWarningSettings.class, warningSetting.getId());
	}

	@Override
	public WarningSetting findWarningSettingById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), WarningSetting.class);
	}
	
	@Override
	public List<WarningSetting> listWarningSetting() {
		return getReadOnlyContext().select().from(Tables.EH_WARNING_SETTINGS)
				.orderBy(Tables.EH_WARNING_SETTINGS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, WarningSetting.class));
	}
	
	@Override
	public WarningSetting findWarningSettingByNamespaceAndType(Integer namespaceId, Long categoryId, String type) {
		Record record = getReadOnlyContext().select().from(Tables.EH_WARNING_SETTINGS)
				.where(Tables.EH_WARNING_SETTINGS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_WARNING_SETTINGS.CATEGORY_ID.eq(categoryId))
				.and(Tables.EH_WARNING_SETTINGS.TYPE.eq(type))
				.fetchOne();
		if (record != null) {
			return ConvertHelper.convert(record, WarningSetting.class);
		}
		return null;
	}

	private EhWarningSettingsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhWarningSettingsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhWarningSettingsDao getDao(DSLContext context) {
		return new EhWarningSettingsDao(context.configuration());
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
