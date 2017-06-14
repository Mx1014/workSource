package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowStatusType;
import com.everhomes.rest.flow.ListFlowCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFlowsDao;
import com.everhomes.server.schema.tables.pojos.EhFlows;
import com.everhomes.server.schema.tables.records.EhFlowsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class FlowProviderImpl implements FlowProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlow(Flow obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlows.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlows.class));
        obj.setId(id);
        prepareObj(obj);
        EhFlowsDao dao = new EhFlowsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateFlow(Flow obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlows.class));
        EhFlowsDao dao = new EhFlowsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlow(Flow obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlows.class));
        EhFlowsDao dao = new EhFlowsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public Flow getFlowById(Long id) {
        try {
        Flow[] result = new Flow[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlows.class));

        result[0] = context.select().from(Tables.EH_FLOWS)
            .where(Tables.EH_FLOWS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, Flow.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<Flow> queryFlows(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlows.class));

        SelectQuery<EhFlowsRecord> query = context.selectQuery(Tables.EH_FLOWS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOWS.ID.lt(locator.getAnchor()));
            }

        query.addLimit(count);
        query.addOrderBy(Tables.EH_FLOWS.ID.desc());
        List<Flow> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, Flow.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(Flow obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
        obj.setStopTime(obj.getCreateTime());
        // obj.setRunTime(new Timestamp(DateHelper.parseDataString("1997-01-01", "yyyy-MM-dd").getTime()));
        obj.setRunTime(new Timestamp(DateHelper.parseDataString("1997-01-01", "yyyy-MM-dd").getTime()));
        obj.setUpdateTime(obj.getCreateTime());
    }

	@Override
	public Flow findFlowByName(Integer namespaceId, Long moduleId,
			String moduleType, Long ownerId, String ownerType, String name) {
		ListingLocator locator = new ListingLocator();
		List<Flow> flows = this.queryFlows(locator, 1, new ListingQueryBuilderCallback() {

			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOWS.NAMESPACE_ID.eq(namespaceId));
				query.addConditions(Tables.EH_FLOWS.MODULE_ID.eq(moduleId));
				if(moduleType != null) {
					query.addConditions(Tables.EH_FLOWS.MODULE_TYPE.eq(moduleType));
				}
				query.addConditions(Tables.EH_FLOWS.OWNER_TYPE.eq(ownerType));
				query.addConditions(Tables.EH_FLOWS.OWNER_ID.eq(ownerId));
				query.addConditions(Tables.EH_FLOWS.FLOW_NAME.eq(name));
				query.addConditions(Tables.EH_FLOWS.STATUS.ne(FlowStatusType.INVALID.getCode()));
				query.addConditions(Tables.EH_FLOWS.FLOW_MAIN_ID.eq(0l)); // Got a main flow, not snapshot flow.
				return query;
			}
			
		});
		
		if(flows != null && flows.size() > 0) {
			return flows.get(0);
		}
		
		return null;
	}
	
	@Override
	public List<Flow> findFlowsByModule(ListingLocator locator, ListFlowCommand cmd) {
		if(locator.getAnchor() == null) {
			locator.setAnchor(cmd.getPageAnchor());	
		}
		
		if(cmd.getModuleType() == null) {
			cmd.setModuleType(FlowModuleType.NO_MODULE.getCode());
		}
		
		List<Flow> flows = this.queryFlows(locator, cmd.getPageSize(), new ListingQueryBuilderCallback() {

			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOWS.NAMESPACE_ID.eq(cmd.getNamespaceId()));
				if(cmd.getModuleId() != null) {
					query.addConditions(Tables.EH_FLOWS.MODULE_ID.eq(cmd.getModuleId()));
					query.addConditions(Tables.EH_FLOWS.MODULE_TYPE.eq(cmd.getModuleType()));
				}

				if(cmd.getOwnerId() != null) {
					query.addConditions(Tables.EH_FLOWS.OWNER_ID.eq(cmd.getOwnerId()));	
				}
				if(cmd.getOwnerType() != null) {
					query.addConditions(Tables.EH_FLOWS.OWNER_TYPE.eq(cmd.getOwnerType()));	
				}
				
				if(cmd.getProjectId() != null) {
					query.addConditions(Tables.EH_FLOWS.PROJECT_ID.eq(cmd.getProjectId()));	
				}
				if(cmd.getProjectType() != null) {
					query.addConditions(Tables.EH_FLOWS.PROJECT_TYPE.eq(cmd.getProjectType()));
				}
				
				query.addConditions(Tables.EH_FLOWS.STATUS.ne(FlowStatusType.INVALID.getCode()));
				
				if(cmd.getFlowVersion() != null) {
					// Got snapshot flow
					query.addConditions(Tables.EH_FLOWS.FLOW_MAIN_ID.eq(cmd.getFlowMainId()));
					query.addConditions(Tables.EH_FLOWS.FLOW_VERSION.eq(cmd.getFlowVersion()));
				} else {
					query.addConditions(Tables.EH_FLOWS.FLOW_MAIN_ID.eq(0l)); // Got a main flow, not snapshot flow.	
				}
				
				return query;
			}
			
		});
		
		return flows;
	}
	
	@Override
	public void flowMarkUpdated(Flow flow) {
		Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
		flow.setUpdateTime(now);
		if(!flow.getStatus().equals(FlowStatusType.CONFIG.getCode())) {
			//update new version
			flow.setFlowVersion(flow.getFlowVersion()+1);
			flow.setStatus(FlowStatusType.CONFIG.getCode());
		}
		updateFlow(flow);
	}
	
	@Override
	public Flow findSnapshotFlow(Long flowId, Integer flowVer) {
		ListingLocator locator = new ListingLocator();
		List<Flow> flows = this.queryFlows(locator, 1, new ListingQueryBuilderCallback() {

			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOWS.STATUS.ne(FlowStatusType.INVALID.getCode()));
				query.addConditions(Tables.EH_FLOWS.FLOW_MAIN_ID.eq(flowId));
				query.addConditions(Tables.EH_FLOWS.FLOW_VERSION.eq(flowVer));
				
				return query;
			}
			
		});
		
		if(flows == null || flows.size() == 0) {
			return null;
		}
		
		return flows.get(0);
	}
	
//	@Override
//	public Flow getEnabledSnapshotFlow(Integer namespaceId, Long moduleId, String moduleType, Long ownerId, String ownerType) {
//		List<Flow> flows = this.queryFlows(new ListingLocator(), 1, new ListingQueryBuilderCallback() {
//
//			@Override
//			public SelectQuery<? extends Record> buildCondition(
//					ListingLocator locator, SelectQuery<? extends Record> query) {
//				query.addConditions(Tables.EH_FLOWS.NAMESPACE_ID.eq(namespaceId));
//				query.addConditions(Tables.EH_FLOWS.MODULE_ID.eq(moduleId));
//				if(moduleType != null) {
//					query.addConditions(Tables.EH_FLOWS.MODULE_TYPE.eq(moduleType));	
//				}
//				query.addConditions(Tables.EH_FLOWS.OWNER_ID.eq(ownerId));	
//				query.addConditions(Tables.EH_FLOWS.OWNER_TYPE.eq(ownerType));	
//				query.addConditions(Tables.EH_FLOWS.STATUS.eq(FlowStatusType.RUNNING.getCode()));
//				query.addConditions(Tables.EH_FLOWS.FLOW_MAIN_ID.ne(0l));
//				query.addOrderBy(Tables.EH_FLOWS.FLOW_VERSION.desc());
//				
//				return query;
//			}
//			
//		});
//		
//		if(flows != null && flows.size() > 0) {
//			return flows.get(0);
//		}
//		
//		return null;
//	}
	
	@Override
	public Flow getEnabledConfigFlow(Integer namespaceId, Long moduleId, String moduleType, Long ownerId, String ownerType) {
		List<Flow> flows = this.queryFlows(new ListingLocator(), 1, new ListingQueryBuilderCallback() {

			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOWS.NAMESPACE_ID.eq(namespaceId));
				query.addConditions(Tables.EH_FLOWS.MODULE_ID.eq(moduleId));
				if(moduleType != null) {
					query.addConditions(Tables.EH_FLOWS.MODULE_TYPE.eq(moduleType));	
				}
				query.addConditions(Tables.EH_FLOWS.OWNER_ID.eq(ownerId));	
				query.addConditions(Tables.EH_FLOWS.OWNER_TYPE.eq(ownerType));	
				query.addConditions(Tables.EH_FLOWS.STATUS.eq(FlowStatusType.RUNNING.getCode())
						.or(Tables.EH_FLOWS.STATUS.eq(FlowStatusType.CONFIG.getCode())));
				query.addConditions(Tables.EH_FLOWS.FLOW_MAIN_ID.eq(0l));
				query.addOrderBy(Tables.EH_FLOWS.RUN_TIME.desc());
				
				return query;
			}
			
		});
		
		if(flows != null && flows.size() > 0) {
			return flows.get(0);
		}
		
		return null;
	}
	
	@Override
	public Flow getSnapshotFlowById(Long flowId) {
		List<Flow> flows = this.queryFlows(new ListingLocator(), 1, new ListingQueryBuilderCallback() {

			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOWS.FLOW_MAIN_ID.eq(flowId));
				query.addConditions(Tables.EH_FLOWS.STATUS.eq(FlowStatusType.RUNNING.getCode()));
				query.addOrderBy(Tables.EH_FLOWS.FLOW_VERSION.desc());
				
				return query;
			}
			
		});
		
		if(flows != null && flows.size() > 0) {
			return flows.get(0);
		}
		
		return null;
	}
}
