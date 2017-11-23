// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhStatEventUploadStrategiesDao;
import com.everhomes.server.schema.tables.pojos.EhStatEventUploadStrategies;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StatEventUploadStrategyProviderImpl implements StatEventUploadStrategyProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createStatEventUploadStrategy(StatEventUploadStrategy statEventUploadStrategy) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatEventUploadStrategies.class));
		statEventUploadStrategy.setId(id);
		statEventUploadStrategy.setCreateTime(DateUtils.currentTimestamp());
		// statEventUploadStrategy.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(statEventUploadStrategy);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhStatEventUploadStrategies.class, id);
	}

	@Override
	public void updateStatEventUploadStrategy(StatEventUploadStrategy statEventUploadStrategy) {
		// statEventUploadStrategy.setUpdateTime(DateUtils.currentTimestamp());
		// statEventUploadStrategy.setUpdateUid(UserContext.currentUserId());
        rwDao().update(statEventUploadStrategy);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhStatEventUploadStrategies.class, statEventUploadStrategy.getId());
	}

	@Override
	public StatEventUploadStrategy findStatEventUploadStrategyById(Long id) {
		return ConvertHelper.convert(dao().findById(id), StatEventUploadStrategy.class);
	}

    @Override
    public List<StatEventUploadStrategy> listUploadStrategyByOwner(String ownerType, Long ownerId) {
        return context().selectFrom(Tables.EH_STAT_EVENT_UPLOAD_STRATEGIES)
                .where(Tables.EH_STAT_EVENT_UPLOAD_STRATEGIES.OWNER_TYPE.eq(ownerType))
                .and(Tables.EH_STAT_EVENT_UPLOAD_STRATEGIES.OWNER_ID.eq(ownerId))
                .fetchInto(StatEventUploadStrategy.class);
    }

    @Override
    public List<StatEventUploadStrategy> listUploadStrategyByNamespace(Integer namespaceId) {
        return context().selectFrom(Tables.EH_STAT_EVENT_UPLOAD_STRATEGIES)
                .where(Tables.EH_STAT_EVENT_UPLOAD_STRATEGIES.NAMESPACE_ID.eq(namespaceId))
                .fetchInto(StatEventUploadStrategy.class);
    }
	
	private EhStatEventUploadStrategiesDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhStatEventUploadStrategiesDao(context.configuration());
	}

	private EhStatEventUploadStrategiesDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhStatEventUploadStrategiesDao(context.configuration());
	}

	private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
