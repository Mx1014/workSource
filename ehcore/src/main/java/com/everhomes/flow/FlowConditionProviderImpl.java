// @formatter:off
package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFlowConditionsDao;
import com.everhomes.server.schema.tables.pojos.EhFlowConditions;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FlowConditionProviderImpl implements FlowConditionProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createFlowCondition(FlowCondition flowCondition) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowConditions.class));
		flowCondition.setId(id);
		flowCondition.setCreateTime(DateUtils.currentTimestamp());
		flowCondition.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(flowCondition);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhFlowConditions.class, id);
	}

	@Override
	public void updateFlowCondition(FlowCondition flowCondition) {
		flowCondition.setUpdateTime(DateUtils.currentTimestamp());
		flowCondition.setUpdateUid(UserContext.currentUserId());
        rwDao().update(flowCondition);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFlowConditions.class, flowCondition.getId());
	}

	@Override
	public FlowCondition findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), FlowCondition.class);
	}

    @Override
    public void deleteFlowCondition(Long flowMainId, Long flowNodeId, Integer flowVersion) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhFlowConditions t = Tables.EH_FLOW_CONDITIONS;
        context.delete(t)
                .where(t.FLOW_MAIN_ID.eq(flowMainId))
                .and(t.FLOW_VERSION.eq(flowVersion))
                .and(t.FLOW_NODE_ID.eq(flowNodeId))
                .execute();
    }

    @Override
    public List<FlowCondition> listFlowCondition(Long flowNodeId) {
        com.everhomes.server.schema.tables.EhFlowConditions t = Tables.EH_FLOW_CONDITIONS;
        return context().selectFrom(t)
                .where(t.FLOW_NODE_ID.eq(flowNodeId))
                .fetchInto(FlowCondition.class);
    }

    @Override
    public List<FlowCondition> listFlowCondition(Long flowMainId, Integer flowVersion) {
        com.everhomes.server.schema.tables.EhFlowConditions t = Tables.EH_FLOW_CONDITIONS;
        return context().selectFrom(t)
                .where(t.FLOW_MAIN_ID.eq(flowMainId))
                .and(t.FLOW_VERSION.eq(flowVersion))
                .fetchInto(FlowCondition.class);
    }

    private EhFlowConditionsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhFlowConditionsDao(context.configuration());
	}

	private EhFlowConditionsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhFlowConditionsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
