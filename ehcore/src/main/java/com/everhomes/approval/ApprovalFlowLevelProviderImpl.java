// @formatter:off
package com.everhomes.approval;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhApprovalFlowLevelsDao;
import com.everhomes.server.schema.tables.pojos.EhApprovalFlowLevels;
import com.everhomes.util.ConvertHelper;

@Component
public class ApprovalFlowLevelProviderImpl implements ApprovalFlowLevelProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createApprovalFlowLevel(ApprovalFlowLevel approvalFlowLevel) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhApprovalFlowLevels.class));
		approvalFlowLevel.setId(id);
		getReadWriteDao().insert(approvalFlowLevel);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhApprovalFlowLevels.class, null);
	}
	
//	@Override
//	public void createApprovalFlowLevelList(List<ApprovalFlowLevel> approvalFlowLevelList) {
//		List<EhApprovalFlowLevels> list = approvalFlowLevelList.stream().map(a->{
//												a.setId(sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhApprovalFlowLevels.class)));
//												return ConvertHelper.convert(a, EhApprovalFlowLevels.class);
//											}).collect(Collectors.toList());
//		getReadWriteDao().insert(list);
//		DaoHelper.publishDaoAction(DaoAction.CREATE, EhApprovalFlowLevels.class, null);
//	}

	@Override
	public void updateApprovalFlowLevel(ApprovalFlowLevel approvalFlowLevel) {
		assert (approvalFlowLevel.getId() != null);
		getReadWriteDao().update(approvalFlowLevel);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhApprovalFlowLevels.class, approvalFlowLevel.getId());
	}

	@Override
	public ApprovalFlowLevel findApprovalFlowLevelById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ApprovalFlowLevel.class);
	}
	
	@Override
	public List<ApprovalFlowLevel> listApprovalFlowLevel() {
		return getReadOnlyContext().select().from(Tables.EH_APPROVAL_FLOW_LEVELS)
				.orderBy(Tables.EH_APPROVAL_FLOW_LEVELS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, ApprovalFlowLevel.class));
	}
	
	@Override
	public List<ApprovalFlowLevel> listApprovalFlowLevel(Long flowId, Byte level) {
		Result<Record> result = getReadOnlyContext().select().from(Tables.EH_APPROVAL_FLOW_LEVELS)
									.where(Tables.EH_APPROVAL_FLOW_LEVELS.FLOW_ID.eq(flowId))
									.and(Tables.EH_APPROVAL_FLOW_LEVELS.LEVEL.eq(level))
									.orderBy(Tables.EH_APPROVAL_FLOW_LEVELS.ID.asc())
									.fetch();
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, ApprovalFlowLevel.class));
		}
		
		return new ArrayList<ApprovalFlowLevel>();
	}

	@Override
	public void deleteApprovalLevels(Long flowId, Byte level) {
		getReadWriteContext().delete(Tables.EH_APPROVAL_FLOW_LEVELS)
			.where(Tables.EH_APPROVAL_FLOW_LEVELS.FLOW_ID.eq(flowId))
			.and(Tables.EH_APPROVAL_FLOW_LEVELS.LEVEL.eq(level))
			.execute();
	}

	@Override
	public List<ApprovalFlowLevel> listApprovalFlowLevelByFlowIds(List<Long> flowIdList) {
		Result<Record> result = getReadOnlyContext().select().from(Tables.EH_APPROVAL_FLOW_LEVELS)
									.where(Tables.EH_APPROVAL_FLOW_LEVELS.FLOW_ID.in(flowIdList))
									.orderBy(Tables.EH_APPROVAL_FLOW_LEVELS.LEVEL.asc(), Tables.EH_APPROVAL_FLOW_LEVELS.ID.asc())
									.fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->ConvertHelper.convert(r, ApprovalFlowLevel.class));
		}
		
		return new ArrayList<ApprovalFlowLevel>();
	}

	private EhApprovalFlowLevelsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhApprovalFlowLevelsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhApprovalFlowLevelsDao getDao(DSLContext context) {
		return new EhApprovalFlowLevelsDao(context.configuration());
	}

	private DSLContext getReadWriteContext() {
		return getContext(AccessSpec.readWrite());
	}

	private DSLContext getReadOnlyContext() {
		return getContext(AccessSpec.readOnly());
	}

	private DSLContext getContext(AccessSpec accessSpec) {
		return dbProvider.getDslContext(accessSpec);
	}
}
