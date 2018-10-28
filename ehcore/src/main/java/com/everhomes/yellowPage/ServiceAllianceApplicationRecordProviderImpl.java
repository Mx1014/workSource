// @formatter:off
package com.everhomes.yellowPage;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SelectJoinStep;
import org.jooq.SelectQuery;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhServiceAllianceApplicationRecordsDao;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceApplicationRecords;
import com.everhomes.server.schema.tables.records.EhAllianceOperateServicesRecord;
import com.everhomes.server.schema.tables.records.EhServiceAllianceApplicationRecordsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.yellowPage.faq.GetLatestServiceStateCommand;

@Component
public class ServiceAllianceApplicationRecordProviderImpl implements ServiceAllianceApplicationRecordProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createServiceAllianceApplicationRecord(ServiceAllianceApplicationRecord serviceAllianceApplicationRecord) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceApplicationRecords.class));
		serviceAllianceApplicationRecord.setId(id);
		serviceAllianceApplicationRecord.setCreatorUid(UserContext.current().getUser().getId());
		serviceAllianceApplicationRecord.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
		getReadWriteDao().insert(serviceAllianceApplicationRecord);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceAllianceApplicationRecords.class, null);
	}

	@Override
	public void updateServiceAllianceApplicationRecord(ServiceAllianceApplicationRecord serviceAllianceApplicationRecord) {
		assert (serviceAllianceApplicationRecord.getId() != null);
		serviceAllianceApplicationRecord.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
		getReadWriteDao().update(serviceAllianceApplicationRecord);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceAllianceApplicationRecords.class, serviceAllianceApplicationRecord.getId());
	}

	@Override
	public ServiceAllianceApplicationRecord findServiceAllianceApplicationRecordById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), ServiceAllianceApplicationRecord.class);
	}
	
	@Override
	public List<ServiceAllianceApplicationRecord> listServiceAllianceApplicationRecord(Long pageAnchor, Integer pageSize) {
		return getReadOnlyContext().select().from(Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS)
				.where(Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS.ID.gt(pageAnchor))
				.orderBy(Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS.ID.asc()).limit(pageSize)
				.fetch().map(r -> ConvertHelper.convert(r, ServiceAllianceApplicationRecord.class));
	}
	
	
	@Override
	public List<ServiceAllianceApplicationRecord> listServiceAllianceApplicationRecordByEnterpriseId(Long enterpriseId,Long pageAnchor, Integer pageSize) {
		return getReadOnlyContext().select().from(Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS)
				.where(Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS.ID.gt(pageAnchor))
				.and(Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS.CREATOR_ORGANIZATION_ID.eq(enterpriseId))
				.orderBy(Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS.ID.asc()).limit(pageSize)
				.fetch().map(r -> ConvertHelper.convert(r, ServiceAllianceApplicationRecord.class));
	}
	
	private EhServiceAllianceApplicationRecordsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhServiceAllianceApplicationRecordsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhServiceAllianceApplicationRecordsDao getDao(DSLContext context) {
		return new EhServiceAllianceApplicationRecordsDao(context.configuration());
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

	@Override
	public ServiceAllianceApplicationRecord findServiceAllianceApplicationRecordByFlowCaseId(Long flowCaseId) {

		List<ServiceAllianceApplicationRecord> records = getReadOnlyContext().select()
				.from(Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS)
				.where(Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS.FLOW_CASE_ID.eq(flowCaseId)).fetch()
				.map(r -> ConvertHelper.convert(r, ServiceAllianceApplicationRecord.class));

		if (CollectionUtils.isEmpty(records)) {
			return null;
		}

		return records.get(0);
	}
	
	private com.everhomes.server.schema.tables.EhServiceAllianceApplicationRecords TABLE = Tables.EH_SERVICE_ALLIANCE_APPLICATION_RECORDS;
	
	private Class<ServiceAllianceApplicationRecord> CLASS = ServiceAllianceApplicationRecord.class;
	
	private List<ServiceAllianceApplicationRecord> listTool(Integer pageSize, ListingLocator locator,
			ListingQueryBuilderCallback callback) {
		
        DSLContext context =  this.dbProvider.getDslContext(AccessSpec.readOnly());
        int realPageSize = null == pageSize ? 0 : pageSize;
        
        SelectQuery<EhServiceAllianceApplicationRecordsRecord> query = context.selectQuery(TABLE);
        if(callback != null)
        	callback.buildCondition(locator, query);

		if (null != locator && locator.getAnchor() != null) {
			query.addConditions(TABLE.ID.ge(locator.getAnchor()));
		}
        
		if (realPageSize > 0) {
			query.addLimit(realPageSize + 1);
		}
		
		query.addOrderBy(TABLE.CREATE_TIME.desc());
		query.addOrderBy(TABLE.ID.asc());
        
        List<ServiceAllianceApplicationRecord> list = query.fetchInto(CLASS);
        
        // 设置锚点
        if (null != locator && null != list) {
    		if (realPageSize > 0 && list.size() > realPageSize) {
    			locator.setAnchor(list.get(list.size() - 1).getId());
    			list.remove(list.size() - 1);
    		} else {
    			locator.setAnchor(null);
    		}
        }

        return list;
    }
	
	
	@Override
	public List<ServiceAllianceApplicationRecord> listServiceAllianceApplicationRecord(AllianceCommonCommand cmd,
			Integer pageSize, ListingLocator locator, Long userId, List<Byte> workFlowStatusList) {
		return listTool(pageSize, locator, (l, q) -> {
			q.addConditions(TABLE.NAMESPACE_ID
					.eq(cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId()));
			q.addConditions(TABLE.OWNER_TYPE.eq(cmd.getOwnerType()));
			q.addConditions(TABLE.OWNER_ID.eq(cmd.getOwnerId()));
			q.addConditions(TABLE.TYPE.eq(cmd.getType()));
			if (null != userId) {
				q.addConditions(TABLE.CREATOR_UID.eq(userId));
			}
			
			if (!CollectionUtils.isEmpty(workFlowStatusList)) {
				q.addConditions(TABLE.WORKFLOW_STATUS.in(workFlowStatusList));
			}
			return null;
		});
	}
	
	@Override
	public Integer listRecordCounts(AllianceCommonCommand cmd, Long userId, List<Byte> workFlowStatusList) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Condition conds = DSL.trueCondition();
		conds = conds.and(TABLE.NAMESPACE_ID
				.eq(cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId()));
		conds = conds.and(TABLE.OWNER_TYPE.eq(cmd.getOwnerType()));
		conds = conds.and(TABLE.OWNER_ID.eq(cmd.getOwnerId()));
		conds = conds.and(TABLE.TYPE.eq(cmd.getType()));
		if (null != userId) {
			conds = conds.and(TABLE.CREATOR_UID.eq(userId));
		}
		
		if (!CollectionUtils.isEmpty(workFlowStatusList)) {
			conds = conds.and(TABLE.WORKFLOW_STATUS.in(workFlowStatusList));
		}
		
		return context.selectCount().from(TABLE).where(conds).fetchOneInto(Integer.class);
	}

	@Override
	public ServiceAllianceApplicationRecord listLatestRecord(GetLatestServiceStateCommand cmd, Long userId,
			List<Byte> workFlowStatusList) {
		
		Integer pageSize = 1;
		List<ServiceAllianceApplicationRecord> records = listTool(pageSize, null, (l, q) -> {
			q.addConditions(TABLE.NAMESPACE_ID
					.eq(cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId()));
			q.addConditions(TABLE.OWNER_TYPE.eq(cmd.getOwnerType()));
			q.addConditions(TABLE.OWNER_ID.eq(cmd.getOwnerId()));
			q.addConditions(TABLE.TYPE.eq(cmd.getType()));
			if (null != userId) {
				q.addConditions(TABLE.CREATOR_UID.eq(userId));
			}

			if (!CollectionUtils.isEmpty(workFlowStatusList)) {
				q.addConditions(TABLE.WORKFLOW_STATUS.in(workFlowStatusList));
			}
			
			q.addOrderBy(TABLE.UPDATE_TIME.desc());
			return null;
		});
		
		return records.size() > 0 ? records.get(0) : null;
	}
}
