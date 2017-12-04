// @formatter:off
package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFlowConditionExpressionsDao;
import com.everhomes.server.schema.tables.pojos.EhFlowConditionExpressions;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class FlowConditionExpressionProviderImpl implements FlowConditionExpressionProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createFlowConditionExpression(FlowConditionExpression flowConditionExpression) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowConditionExpressions.class));
		flowConditionExpression.setId(id);
		flowConditionExpression.setCreateTime(DateUtils.currentTimestamp());
		flowConditionExpression.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(flowConditionExpression);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhFlowConditionExpressions.class, id);
	}

	@Override
	public void updateFlowConditionExpression(FlowConditionExpression flowConditionExpression) {
		flowConditionExpression.setUpdateTime(DateUtils.currentTimestamp());
		flowConditionExpression.setUpdateUid(UserContext.currentUserId());
        rwDao().update(flowConditionExpression);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFlowConditionExpressions.class, flowConditionExpression.getId());
	}

	@Override
	public FlowConditionExpression findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), FlowConditionExpression.class);
	}

    @Override
    public void deleteFlowConditionExpression(Long flowMainId, Integer flowVersion) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhFlowConditionExpressions t = Tables.EH_FLOW_CONDITION_EXPRESSIONS;
        context.delete(t)
                .where(t.FLOW_MAIN_ID.eq(flowMainId))
                .and(t.FLOW_VERSION.eq(flowVersion))
                .execute();
    }

    @Override
    public List<FlowConditionExpression> listFlowConditionExpression(Long conditionId) {
        com.everhomes.server.schema.tables.EhFlowConditionExpressions t = Tables.EH_FLOW_CONDITION_EXPRESSIONS;
        return context().selectFrom(t).where(t.FLOW_CONDITION_ID.eq(conditionId)).fetchInto(FlowConditionExpression.class);
    }

    @Override
    public void updateFlowConditionExpressions(List<FlowConditionExpression> expressions) {
        Long updateUid = UserContext.currentUserId();
        Timestamp updateTime = DateUtils.currentTimestamp();
        for (FlowConditionExpression expression : expressions) {
            expression.setUpdateTime(updateTime);
            expression.setUpdateUid(updateUid);
        }
        rwDao().update(expressions.toArray(new EhFlowConditionExpressions[expressions.size()]));
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFlowConditionExpressions.class, null);
    }

    @Override
    public List<FlowConditionExpression> listFlowConditionExpressionByFlow(Long flowId, Integer flowVersion) {
        com.everhomes.server.schema.tables.EhFlowConditionExpressions t = Tables.EH_FLOW_CONDITION_EXPRESSIONS;
        return context()
                .selectFrom(t)
                .where(t.FLOW_MAIN_ID.eq(flowId))
                .and(t.FLOW_VERSION.eq(flowVersion))
                .fetchInto(FlowConditionExpression.class);
    }

    private EhFlowConditionExpressionsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhFlowConditionExpressionsDao(context.configuration());
	}

	private EhFlowConditionExpressionsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhFlowConditionExpressionsDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
