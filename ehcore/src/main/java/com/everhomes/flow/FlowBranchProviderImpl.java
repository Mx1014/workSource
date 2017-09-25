// @formatter:off
package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhFlowBranches;
import com.everhomes.server.schema.tables.daos.EhFlowBranchesDao;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FlowBranchProviderImpl implements FlowBranchProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createFlowBranch(FlowBranch flowBranch) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowBranches.class));
		flowBranch.setId(id);
		flowBranch.setCreateTime(DateUtils.currentTimestamp());
		flowBranch.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(flowBranch);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhFlowBranches.class, id);
	}

	@Override
	public void updateFlowBranch(FlowBranch flowBranch) {
		flowBranch.setUpdateTime(DateUtils.currentTimestamp());
		flowBranch.setUpdateUid(UserContext.currentUserId());
        rwDao().update(flowBranch);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFlowBranches.class, flowBranch.getId());
	}

	@Override
	public FlowBranch findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), FlowBranch.class);
	}

    @Override
    public void deleteFlowBranch(Long flowMainId, Integer flowVersion) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhFlowBranches t = Tables.EH_FLOW_BRANCHES;
        context.delete(t)
                .where(t.FLOW_MAIN_ID.eq(flowMainId))
                .and(t.FLOW_VERSION.eq(flowVersion))
                .execute();
    }

    @Override
    public FlowBranch findBranch(Long originalNodeId) {
        com.everhomes.server.schema.tables.EhFlowBranches t = Tables.EH_FLOW_BRANCHES;
        return context()
                .selectFrom(t)
                .where(t.ORIGINAL_NODE_ID.eq(originalNodeId))
                .fetchAnyInto(FlowBranch.class);
    }

    @Override
    public List<FlowBranch> findByFlowId(Long flowId, Integer flowVersion) {
        com.everhomes.server.schema.tables.EhFlowBranches t = Tables.EH_FLOW_BRANCHES;
        return context()
                .selectFrom(t)
                .where(t.FLOW_MAIN_ID.eq(flowId))
                .and(t.FLOW_VERSION.eq(flowVersion))
                .fetchInto(FlowBranch.class);
    }

    private EhFlowBranchesDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhFlowBranchesDao(context.configuration());
	}

	private EhFlowBranchesDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhFlowBranchesDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
