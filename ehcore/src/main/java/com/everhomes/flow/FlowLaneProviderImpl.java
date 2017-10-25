// @formatter:off
package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFlowLanesDao;
import com.everhomes.server.schema.tables.pojos.EhFlowLanes;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FlowLaneProviderImpl implements FlowLaneProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createFlowLane(FlowLane flowLane) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowLanes.class));
		flowLane.setId(id);
		flowLane.setCreateTime(DateUtils.currentTimestamp());
		flowLane.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(flowLane);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhFlowLanes.class, id);
	}

	@Override
	public void updateFlowLane(FlowLane flowLane) {
		flowLane.setUpdateTime(DateUtils.currentTimestamp());
		flowLane.setUpdateUid(UserContext.currentUserId());
        rwDao().update(flowLane);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFlowLanes.class, flowLane.getId());
	}

	@Override
	public FlowLane findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), FlowLane.class);
	}

    @Override
    public List<FlowLane> listFlowLane(Long flowMainId, Integer flowVersion) {
        com.everhomes.server.schema.tables.EhFlowLanes t = Tables.EH_FLOW_LANES;
        return context().selectFrom(t)
                .where(t.FLOW_MAIN_ID.eq(flowMainId))
                .and(t.FLOW_VERSION.eq(flowVersion))
                .fetchInto(FlowLane.class);
    }

    @Override
    public void deleteFlowLane(List<Long> deleteIdList) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhFlowLanes t = Tables.EH_FLOW_LANES;
        context.delete(t)
                .where(t.ID.in(deleteIdList))
                .execute();
    }

    @Override
    public void deleteFlowLane(Long flowMainId, Integer flowVersion, List<Long> retainIdList) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhFlowLanes t = Tables.EH_FLOW_LANES;
        context.delete(t)
                .where(t.FLOW_MAIN_ID.eq(flowMainId))
                .and(t.FLOW_VERSION.eq(flowVersion))
                .and(t.ID.notIn(retainIdList))
                .execute();
    }

    private EhFlowLanesDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhFlowLanesDao(context.configuration());
	}

	private EhFlowLanesDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhFlowLanesDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
