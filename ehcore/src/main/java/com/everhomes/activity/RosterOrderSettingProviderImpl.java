// @formatter:off
package com.everhomes.activity;

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
import com.everhomes.server.schema.tables.daos.EhRosterOrderSettingsDao;
import com.everhomes.server.schema.tables.pojos.EhRosterOrderSettings;
import com.everhomes.util.ConvertHelper;

@Component
public class RosterOrderSettingProviderImpl implements RosterOrderSettingProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;


	@Override
	public void createRosterOrderSetting(RosterOrderSetting rosterOrderSetting) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhRosterOrderSettings.class));
		rosterOrderSetting.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhRosterOrderSettingsDao dao = new EhRosterOrderSettingsDao(context.configuration());
        dao.insert(rosterOrderSetting);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhRosterOrderSettings.class, null);
	}

	@Override
	public void updateRosterOrderSetting(RosterOrderSetting rosterOrderSetting) {
		assert(rosterOrderSetting.getId() == null);

	    DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
	    EhRosterOrderSettingsDao dao = new EhRosterOrderSettingsDao(context.configuration());
	    dao.update(rosterOrderSetting);

	    DaoHelper.publishDaoAction(DaoAction.MODIFY,EhRosterOrderSettings.class, rosterOrderSetting.getId());
		
	}

	@Override
	public RosterOrderSetting findRosterOrderSettingById(Long id) {
		 DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		 EhRosterOrderSettingsDao dao = new EhRosterOrderSettingsDao(context.configuration());
		 return ConvertHelper.convert(dao.findById(id), RosterOrderSetting.class);
	}

	@Override
	public RosterOrderSetting findRosterOrderSettingByNamespace(Integer namespaceId, Long categoryId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

		RosterOrderSetting rosterOrderSetting = context.select()
				.from(Tables.EH_ROSTER_ORDER_SETTINGS)
				.where(Tables.EH_ROSTER_ORDER_SETTINGS.NAMESPACE_ID.eq(namespaceId))
				.and(Tables.EH_ROSTER_ORDER_SETTINGS.CATEGORY_ID.eq(categoryId))
				.fetchOneInto(RosterOrderSetting.class);
		return rosterOrderSetting;
	}
}
