// @formatter:off
package com.everhomes.statistics.event;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhStatEventParamsDao;
import com.everhomes.server.schema.tables.pojos.EhStatEventParams;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DateUtils;

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
	
	// @Override
	// public List<StatEventParam> listStatEventParam() {
	// 	return getReadOnlyContext().select().from(Tables.EH_STAT_EVENT_PARAMS)
	//			.orderBy(Tables.EH_STAT_EVENT_PARAMS.ID.asc())
	//			.fetch().map(r -> ConvertHelper.convert(r, StatEventParam.class));
	// }
	
	private EhStatEventParamsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhStatEventParamsDao(context.configuration());
	}

	private EhStatEventParamsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhStatEventParamsDao(context.configuration());
	}
}
