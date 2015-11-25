package com.everhomes.techpark.expansion;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterpriseOpRequestsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseOpRequests;
import com.everhomes.server.schema.tables.records.EhEnterpriseDetailsRecord;
import com.everhomes.server.schema.tables.records.EhEnterpriseOpRequestsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class EnterpriseApplyEntryProviderImpl implements
		EnterpriseApplyEntryProvider {
	
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public List<EnterpriseDetail> listEnterpriseDetails(Long communityId,
			int offset, int pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		List<EnterpriseDetail> enterpriseDetails = new ArrayList<EnterpriseDetail>();
		context.select().from(Tables.EH_ENTERPRISE_COMMUNITY_MAP).leftOuterJoin(Tables.EH_ENTERPRISE_DETAILS)
						.on(Tables.EH_ENTERPRISE_COMMUNITY_MAP.MEMBER_ID.eq(Tables.EH_ENTERPRISE_DETAILS.ENTERPRISE_ID))
						.and(Tables.EH_ENTERPRISE_COMMUNITY_MAP.COMMUNITY_ID.eq(communityId))
						.limit(offset, pageSize)
						.fetch().map((r) -> {
							enterpriseDetails.add(ConvertHelper.convert(r, EnterpriseDetail.class));
							return null;
						});
		return enterpriseDetails;
	}

	@Override
	public EnterpriseDetail getEnterpriseDetailById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhEnterpriseDetailsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_DETAILS);
		query.addConditions(Tables.EH_ENTERPRISE_DETAILS.ENTERPRISE_ID.eq(id));
		query.fetchOne();
		return ConvertHelper.convert(query, EnterpriseDetail.class);
	}

	@Override
	public List<EnterpriseOpRequest> listApplyEntrys(Long communityId,
			int offset, int pageSize) {
		List<EnterpriseOpRequest> enterpriseOpRequests = new ArrayList<EnterpriseOpRequest>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhEnterpriseOpRequestsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_OP_REQUESTS);
		query.addConditions(Tables.EH_ENTERPRISE_OP_REQUESTS.COMMUNITY_ID.eq(communityId));
		query.addLimit(offset, pageSize);
		query.fetch().map((r) -> {
			enterpriseOpRequests.add(ConvertHelper.convert(r, EnterpriseOpRequest.class));
			return null;
		});
		return enterpriseOpRequests;
	}

	@Override
	public boolean createApplyEntry(EnterpriseOpRequest enterpriseOpRequest) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhEnterpriseOpRequests.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EnterpriseOpRequest.class));
		enterpriseOpRequest.setId(id);
		enterpriseOpRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
		EhEnterpriseOpRequestsDao dap = new EhEnterpriseOpRequestsDao(context.configuration());
		dap.insert(enterpriseOpRequest);
		return true;
	}

	@Override
	public EnterpriseOpRequest getEnterpriseOpRequestByBuildIdAndUserId(
			Long buildId, Long userId) {
		List<EnterpriseOpRequest> enterpriseOpRequests = new ArrayList<EnterpriseOpRequest>();
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		SelectQuery<EhEnterpriseOpRequestsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_OP_REQUESTS);
		query.addConditions(Tables.EH_ENTERPRISE_OP_REQUESTS.SOURCE_ID.eq(buildId));
		query.addConditions(Tables.EH_ENTERPRISE_OP_REQUESTS.APPLY_USER_ID.eq(userId));
		query.fetch().map((r) -> {
			enterpriseOpRequests.add(ConvertHelper.convert(r, EnterpriseOpRequest.class));
			return null;
		});
		
		if(0 == enterpriseOpRequests.size()){
			return null;
		}
		return enterpriseOpRequests.get(0);
	}

}
