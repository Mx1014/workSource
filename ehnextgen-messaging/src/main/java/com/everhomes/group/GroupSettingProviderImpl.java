// @formatter:off
package com.everhomes.group;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.group.GroupSetting;
import com.everhomes.group.GroupSettingProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhGroupSettingsDao;
import com.everhomes.server.schema.tables.pojos.EhGroupSettings;
import com.everhomes.util.ConvertHelper;

@Component
public class GroupSettingProviderImpl implements GroupSettingProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createGroupSetting(GroupSetting groupSetting) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhGroupSettings.class));
		groupSetting.setId(id);
		getReadWriteDao().insert(groupSetting);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhGroupSettings.class, null);
	}

	@Override
	public void updateGroupSetting(GroupSetting groupSetting) {
		assert (groupSetting.getId() != null);
		getReadWriteDao().update(groupSetting);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhGroupSettings.class, groupSetting.getId());
	}

	@Override
	public GroupSetting findGroupSettingById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), GroupSetting.class);
	}
	
	@Override
	public List<GroupSetting> listGroupSetting() {
		return getReadOnlyContext().select().from(Tables.EH_GROUP_SETTINGS)
				.orderBy(Tables.EH_GROUP_SETTINGS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, GroupSetting.class));
	}
	
	@Override
	public GroupSetting findGroupSettingByNamespaceId(Integer namespaceId) {
		Record record = getReadOnlyContext().select().from(Tables.EH_GROUP_SETTINGS)
				.where(Tables.EH_GROUP_SETTINGS.NAMESPACE_ID.eq(namespaceId))
				.orderBy(Tables.EH_GROUP_SETTINGS.ID.asc())
				.fetchAny();
		
		if (record != null) {
			return ConvertHelper.convert(record, GroupSetting.class);
		}
		return null;
	}

	private EhGroupSettingsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhGroupSettingsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhGroupSettingsDao getDao(DSLContext context) {
		return new EhGroupSettingsDao(context.configuration());
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
