// @formatter:off
package com.everhomes.flow;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhFlowWaysDao;
import com.everhomes.server.schema.tables.pojos.EhFlowWays;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;

@Repository
public class FlowWayProviderImpl implements FlowWayProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createFlowWay(FlowWay flowWay) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowWays.class));
		flowWay.setId(id);
		flowWay.setCreateTime(DateUtils.currentTimestamp());
		// flowWay.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(flowWay);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhFlowWays.class, id);
	}

	@Override
	public void updateFlowWay(FlowWay flowWay) {
		// flowWay.setUpdateTime(DateUtils.currentTimestamp());
		// flowWay.setUpdateUid(UserContext.currentUserId());
        rwDao().update(flowWay);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFlowWays.class, flowWay.getId());
	}

	@Override
	public FlowWay findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), FlowWay.class);
	}

	private EhFlowWaysDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhFlowWaysDao(context.configuration());
	}

	private EhFlowWaysDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhFlowWaysDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
