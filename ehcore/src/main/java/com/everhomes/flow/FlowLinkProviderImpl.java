// @formatter:off
package com.everhomes.flow;

import com.everhomes.server.schema.Tables;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhFlowLinksDao;
import com.everhomes.server.schema.tables.pojos.EhFlowLinks;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;

import java.util.List;

@Repository
public class FlowLinkProviderImpl implements FlowLinkProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createFlowLink(FlowLink flowLink) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowLinks.class));
		flowLink.setId(id);
		flowLink.setCreateTime(DateUtils.currentTimestamp());
		// flowLink.setCreatorUid(UserContext.currentUserId());
		rwDao().insert(flowLink);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhFlowLinks.class, id);
	}

	@Override
	public void updateFlowLink(FlowLink flowLink) {
		// flowLink.setUpdateTime(DateUtils.currentTimestamp());
		// flowLink.setUpdateUid(UserContext.currentUserId());
        rwDao().update(flowLink);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFlowLinks.class, flowLink.getId());
	}

	@Override
	public FlowLink findById(Long id) {
		return ConvertHelper.convert(dao().findById(id), FlowLink.class);
	}

    @Override
    public void deleteFlowLink(Long flowMainId, Integer flowVersion) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        com.everhomes.server.schema.tables.EhFlowLinks t = Tables.EH_FLOW_LINKS;
        context.delete(t)
                .where(t.FLOW_MAIN_ID.eq(flowMainId))
                .and(t.FLOW_VERSION.eq(flowVersion))
                .execute();
    }

    @Override
    public List<FlowLink> listFlowLink(Long flowMainId, Integer flowVersion) {
        com.everhomes.server.schema.tables.EhFlowLinks t = Tables.EH_FLOW_LINKS;
        return context().selectFrom(t)
                .where(t.FLOW_MAIN_ID.eq(flowMainId))
                .and(t.FLOW_VERSION.eq(flowVersion))
                .fetchInto(FlowLink.class);
    }

    private EhFlowLinksDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhFlowLinksDao(context.configuration());
	}

	private EhFlowLinksDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhFlowLinksDao(context.configuration());
	}

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
