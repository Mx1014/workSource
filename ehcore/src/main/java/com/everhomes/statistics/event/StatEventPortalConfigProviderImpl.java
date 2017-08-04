// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.server.schema.Tables;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhStatEventPortalConfigsDao;
import com.everhomes.server.schema.tables.pojos.EhStatEventPortalConfigs;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;

import java.util.List;

@Repository
public class StatEventPortalConfigProviderImpl implements StatEventPortalConfigProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createStatEventPortalConfig(StatEventPortalConfig statEventPortalConfig) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatEventPortalConfigs.class));
		statEventPortalConfig.setId(id);
		statEventPortalConfig.setCreateTime(DateUtils.currentTimestamp());
		// statEventPortalConfig.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(statEventPortalConfig);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhStatEventPortalConfigs.class, id);
	}

	@Override
	public void updateStatEventPortalConfig(StatEventPortalConfig statEventPortalConfig) {
		// statEventPortalConfig.setUpdateTime(DateUtils.currentTimestamp());
		// statEventPortalConfig.setUpdateUid(UserContext.currentUserId());
        rwDao().update(statEventPortalConfig);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhStatEventPortalConfigs.class, statEventPortalConfig.getId());
	}

	@Override
	public StatEventPortalConfig findStatEventPortalConfigById(Long id) {
		return ConvertHelper.convert(dao().findById(id), StatEventPortalConfig.class);
	}

    @Override
    public List<StatEventPortalConfig> listPortalTopNavigationBarByStatus(byte configType, byte status) {
        return context().selectFrom(Tables.EH_STAT_EVENT_PORTAL_CONFIGS)
                .where(Tables.EH_STAT_EVENT_PORTAL_CONFIGS.CONFIG_TYPE.eq(configType))
                .and(Tables.EH_STAT_EVENT_PORTAL_CONFIGS.STATUS.eq(status))
                .fetchInto(StatEventPortalConfig.class);
    }

    @Override
    public StatEventPortalConfig findPortalConfig(Integer namespaceId, byte configType, String identifier) {
        return context().selectFrom(Tables.EH_STAT_EVENT_PORTAL_CONFIGS)
                .where(Tables.EH_STAT_EVENT_PORTAL_CONFIGS.NAMESPACE_ID.eq(namespaceId))
                .and(Tables.EH_STAT_EVENT_PORTAL_CONFIGS.CONFIG_TYPE.eq(configType))
                .and(Tables.EH_STAT_EVENT_PORTAL_CONFIGS.IDENTIFIER.eq(identifier))
                .fetchAnyInto(StatEventPortalConfig.class);
    }

    // @Override
	// public List<StatEventPortalConfig> listStatEventPortalConfig() {
	// 	return getReadOnlyContext().select().from(Tables.EH_STAT_EVENT_PORTAL_CONFIGS)
	//			.orderBy(Tables.EH_STAT_EVENT_PORTAL_CONFIGS.ID.asc())
	//			.fetch().map(r -> ConvertHelper.convert(r, StatEventPortalConfig.class));
	// }
	
	private EhStatEventPortalConfigsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhStatEventPortalConfigsDao(context.configuration());
	}

	private EhStatEventPortalConfigsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhStatEventPortalConfigsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
