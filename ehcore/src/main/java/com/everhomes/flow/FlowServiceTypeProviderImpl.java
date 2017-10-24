// @formatter:off
package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFlowServiceTypesDao;
import com.everhomes.server.schema.tables.pojos.EhFlowServiceTypes;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FlowServiceTypeProviderImpl implements FlowServiceTypeProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createFlowServiceType(FlowServiceType flowServiceType) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowServiceTypes.class));
		flowServiceType.setId(id);
		// flowServiceType.setCreateTime(DateUtils.currentTimestamp());
		// flowServiceType.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(flowServiceType);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhFlowServiceTypes.class, id);
	}

	@Override
	public void updateFlowServiceType(FlowServiceType flowServiceType) {
		// flowServiceType.setUpdateTime(DateUtils.currentTimestamp());
		// flowServiceType.setUpdateUid(UserContext.currentUserId());
        rwDao().update(flowServiceType);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFlowServiceTypes.class, flowServiceType.getId());
	}

	@Override
	public FlowServiceType findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), FlowServiceType.class);
	}

    @Override
    public <T> List<T> listFlowServiceType(Integer namespaceId, Class<T> clazz) {
        com.everhomes.server.schema.tables.EhFlowServiceTypes t = Tables.EH_FLOW_SERVICE_TYPES;
        return context().selectFrom(t).where(t.NAMESPACE_ID.eq(namespaceId)).fetchInto(clazz);
    }

    private EhFlowServiceTypesDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhFlowServiceTypesDao(context.configuration());
	}

	private EhFlowServiceTypesDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhFlowServiceTypesDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
