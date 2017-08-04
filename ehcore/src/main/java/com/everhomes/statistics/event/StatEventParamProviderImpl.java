// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhStatEventParamsDao;
import com.everhomes.server.schema.tables.pojos.EhStatEventParams;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StatEventParamProviderImpl implements StatEventParamProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createStatEventParam(StatEventParam statEventParam) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhStatEventParams.class));
		statEventParam.setId(id);
		statEventParam.setCreateTime(DateUtils.currentTimestamp());
		// statEventParam.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(statEventParam);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhStatEventParams.class, id);
	}

	@Override
	public void updateStatEventParam(StatEventParam statEventParam) {
		// statEventParam.setUpdateTime(DateUtils.currentTimestamp());
		// statEventParam.setUpdateUid(UserContext.currentUserId());
        rwDao().update(statEventParam);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhStatEventParams.class, statEventParam.getId());
	}

	@Override
	public StatEventParam findStatEventParamById(Long id) {
		return ConvertHelper.convert(dao().findById(id), StatEventParam.class);
	}

    @Override
    public StatEventParam findStatEventParam(String eventName, String paramKey) {
        return context().selectFrom(Tables.EH_STAT_EVENT_PARAMS)
                .where(Tables.EH_STAT_EVENT_PARAMS.EVENT_NAME.eq(eventName))
                .and(Tables.EH_STAT_EVENT_PARAMS.PARAM_KEY.eq(paramKey))
                .fetchAnyInto(StatEventParam.class);
    }

    @Override
    public List<StatEventParam> listParam(String eventName, Integer eventVersion) {
        return context().selectFrom(Tables.EH_STAT_EVENT_PARAMS)
                .where(Tables.EH_STAT_EVENT_PARAMS.EVENT_NAME.eq(eventName))
                .and(Tables.EH_STAT_EVENT_PARAMS.EVENT_VERSION.eq(eventVersion))
                .fetchInto(StatEventParam.class);
    }

    private EhStatEventParamsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhStatEventParamsDao(context.configuration());
	}

	private EhStatEventParamsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhStatEventParamsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
