// @formatter:off
package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFlowPredefinedParamsDao;
import com.everhomes.server.schema.tables.pojos.EhFlowPredefinedParams;
import com.everhomes.server.schema.tables.records.EhFlowPredefinedParamsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FlowPredefinedParamProviderImpl implements FlowPredefinedParamProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createFlowPredefinedParam(FlowPredefinedParam flowPredefinedParam) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowPredefinedParams.class));
		flowPredefinedParam.setId(id);
		flowPredefinedParam.setCreateTime(DateUtils.currentTimestamp());
		// flowPredefinedParam.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(flowPredefinedParam);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhFlowPredefinedParams.class, id);
	}

	@Override
	public void updateFlowPredefinedParam(FlowPredefinedParam flowPredefinedParam) {
		// flowPredefinedParam.setUpdateTime(DateUtils.currentTimestamp());
		// flowPredefinedParam.setUpdateUid(UserContext.currentUserId());
        rwDao().update(flowPredefinedParam);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFlowPredefinedParams.class, flowPredefinedParam.getId());
	}

	@Override
	public FlowPredefinedParam findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), FlowPredefinedParam.class);
	}

    @Override
    public List<FlowPredefinedParam> listPredefinedParam(String moduleType, Long moduleId, String ownerType, Long ownerId, String entityType) {
        com.everhomes.server.schema.tables.EhFlowPredefinedParams t = Tables.EH_FLOW_PREDEFINED_PARAMS;

        SelectQuery<EhFlowPredefinedParamsRecord> query = context().selectQuery(t);
        if (moduleType != null && moduleId != null) {
            query.addConditions(t.MODULE_TYPE.eq(moduleType));
            query.addConditions(t.MODULE_ID.eq(moduleId));
        }
        if (ownerType != null && ownerId != null) {
            query.addConditions(t.OWNER_TYPE.eq(ownerType));
            query.addConditions(t.OWNER_ID.eq(ownerId));
        }
        if (entityType != null) {
            query.addConditions(t.ENTITY_TYPE.eq(entityType));
        }
        return query.fetchInto(FlowPredefinedParam.class);
    }

    private EhFlowPredefinedParamsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhFlowPredefinedParamsDao(context.configuration());
	}

	private EhFlowPredefinedParamsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhFlowPredefinedParamsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
