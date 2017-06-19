package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.flow.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFlowEventLogsDao;
import com.everhomes.server.schema.tables.pojos.EhFlowCases;
import com.everhomes.server.schema.tables.pojos.EhFlowEventLogs;
import com.everhomes.server.schema.tables.records.EhFlowEventLogsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class FlowEventLogProviderImpl implements FlowEventLogProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createFlowEventLog(FlowEventLog obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEventLogs.class));
        if(obj.getId() == null || obj.getId() <= 0) {
        	long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowEventLogs.class));
         obj.setId(id);	
        }
        
        prepareObj(obj);
        EhFlowEventLogsDao dao = new EhFlowEventLogsDao(context.configuration());
        dao.insert(obj);
        return obj.getId();
    }

    @Override
    public void updateFlowEventLog(FlowEventLog obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEventLogs.class));
        EhFlowEventLogsDao dao = new EhFlowEventLogsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlowEventLog(FlowEventLog obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEventLogs.class));
        EhFlowEventLogsDao dao = new EhFlowEventLogsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public FlowEventLog getFlowEventLogById(Long id) {
        try {
        FlowEventLog[] result = new FlowEventLog[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEventLogs.class));

        result[0] = context.select().from(Tables.EH_FLOW_EVENT_LOGS)
            .where(Tables.EH_FLOW_EVENT_LOGS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, FlowEventLog.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<FlowEventLog> queryFlowEventLogs(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEventLogs.class));

        SelectQuery<EhFlowEventLogsRecord> query = context.selectQuery(Tables.EH_FLOW_EVENT_LOGS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.ID.gt(locator.getAnchor()));
            }
        query.addConditions(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.ge(0l));

        query.addLimit(count);
        List<FlowEventLog> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FlowEventLog.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(FlowEventLog obj) {
    	Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
    	obj.setCreateTime(now);
    }
    
    @Override
    public Long getNextId() {
    	return this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowEventLogs.class));
    }
    
    @Override
    public void createFlowEventLogs(List<FlowEventLog> objs) {
    	if(objs.size() == 0) {
    		return;
    	}
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEventLogs.class));
    	EhFlowEventLogsDao dao = new EhFlowEventLogsDao(context.configuration());
    	
    	for(FlowEventLog obj : objs) {
    		prepareObj(obj);	
    	}
        
        dao.insert(objs.toArray(new FlowEventLog[objs.size()]));
    }
    
    /**
     * 获取 待处理/处理中/督办 的 FlowCase
     */
    @Override
    public List<FlowCaseDetail> findProcessorFlowCases(ListingLocator locator, int count, SearchFlowCaseCommand cmd) {
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhFlowCases.class));
    	Condition cond = Tables.EH_FLOW_CASES.STATUS.ne(FlowCaseStatus.INVALID.getCode())
    			.and(Tables.EH_FLOW_CASES.NAMESPACE_ID.eq(cmd.getNamespaceId()));
    	
    	if(locator.getAnchor() == null) {
    		locator.setAnchor(cmd.getPageAnchor());
    	}

        FlowCaseSearchType searchType = FlowCaseSearchType.fromCode(cmd.getFlowCaseSearchType());
        if(FlowCaseSearchType.TODO_LIST.equals(searchType)) {
            cond = cond.and(Tables.EH_FLOW_CASES.STATUS.in(FlowCaseStatus.INITIAL.getCode(), FlowCaseStatus.PROCESS.getCode()));
            cond = cond.and(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.NODE_ENTER.getCode()))
    		.and(Tables.EH_FLOW_EVENT_LOGS.FLOW_USER_ID.eq(cmd.getUserId()))
    		.and(Tables.EH_FLOW_CASES.STEP_COUNT.eq(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT)); //step_cout must equal the same
    	}
    	else if (FlowCaseSearchType.DONE_LIST.equals(searchType)) {
            cond = cond.and(Tables.EH_FLOW_CASES.STATUS.in(FlowCaseStatus.INITIAL.getCode(), FlowCaseStatus.PROCESS.getCode(), FlowCaseStatus.FINISHED.getCode(), FlowCaseStatus.ABSORTED.getCode()));
    		cond = cond.and(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.BUTTON_FIRED.getCode()))
    		.and(Tables.EH_FLOW_EVENT_LOGS.FLOW_USER_ID.eq(cmd.getUserId()))
    		.and(FlowEventCustomField.BUTTON_FIRED_COUNT.getField().eq(0l));    		
    	}
    	else if(FlowCaseSearchType.SUPERVISOR.equals(searchType)) {
            cond = cond.and(Tables.EH_FLOW_CASES.STATUS.in(FlowCaseStatus.INITIAL.getCode(), FlowCaseStatus.PROCESS.getCode(), FlowCaseStatus.FINISHED.getCode(), FlowCaseStatus.ABSORTED.getCode()));
            cond = cond.and(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.FLOW_SUPERVISOR.getCode()))
    		.and(Tables.EH_FLOW_EVENT_LOGS.FLOW_USER_ID.eq(cmd.getUserId())); 
    	}
    	else {
    		return null;
    	}
    	
    	if(cmd.getModuleId() != null) {
    		cond = cond.and(Tables.EH_FLOW_CASES.MODULE_ID.eq(cmd.getModuleId()));
    	}
        if(cmd.getOrganizationId() != null) {
            cond = cond.and(Tables.EH_FLOW_CASES.ORGANIZATION_ID.eq(cmd.getOrganizationId()));
        }
    	if(cmd.getFlowCaseStatus() != null) {
    		cond = cond.and(Tables.EH_FLOW_CASES.STATUS.eq(cmd.getFlowCaseStatus()));
    	}
    	if(cmd.getOwnerId() != null) {
    		cond = cond.and(Tables.EH_FLOW_CASES.OWNER_ID.eq(cmd.getOwnerId()));
    	}
    	if(cmd.getOwnerType() != null) {
    		cond = cond.and(Tables.EH_FLOW_CASES.OWNER_TYPE.eq(cmd.getOwnerType()));
    	}
    	if(cmd.getKeyword() != null && !cmd.getKeyword().isEmpty()) {
    		cond = cond.and(
    				Tables.EH_FLOW_CASES.MODULE_NAME.like(cmd.getKeyword() + "%")
    				.or(Tables.EH_FLOW_CASES.APPLIER_NAME.like(cmd.getKeyword() + "%"))
    				.or(Tables.EH_FLOW_CASES.APPLIER_PHONE.like(cmd.getKeyword() + "%"))
    				);
    	}
    	
        if(locator.getAnchor() != null) {
    	    cond = cond.and(Tables.EH_FLOW_EVENT_LOGS.ID.lt(locator.getAnchor()));
        }
        cond = cond.and(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.ge(0L));
    	
    	final List<FlowCaseDetail> objs = new ArrayList<>();
		context.select().from(Tables.EH_FLOW_EVENT_LOGS).join(Tables.EH_FLOW_CASES)
		.on(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(Tables.EH_FLOW_CASES.ID))
		.join(Tables.EH_FLOWS)
    	.on(Tables.EH_FLOW_CASES.FLOW_MAIN_ID.eq(Tables.EH_FLOWS.FLOW_MAIN_ID)
                .and(Tables.EH_FLOW_CASES.FLOW_VERSION.eq(Tables.EH_FLOWS.FLOW_VERSION)))
    	.where(cond).orderBy(Tables.EH_FLOW_EVENT_LOGS.ID.desc()).limit(count).fetch().map((r)-> {
    		objs.add(convertRecordTODetail(r));
    		return null;
    	});
		
        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getEventLogId());
        } else {
            locator.setAnchor(null);
        }

		return objs;
    }
    
    @Override
    public FlowEventLog isProcessor(Long userId, FlowCase flowCase) {
    	ListingLocator locator = new ListingLocator();
    	List<FlowEventLog> objs = this.queryFlowEventLogs(locator, 100, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(flowCase.getId()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.NODE_ENTER.getCode()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_USER_ID.eq(userId));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.eq(flowCase.getStepCount()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_VERSION.eq(flowCase.getFlowVersion()));
				
				return query;
			}
    	});  
    	
    	if(objs != null && objs.size() > 0) {
    		return objs.get(0);
    	}
    	
    	return null;
    }
    
    @Override
    public FlowEventLog getValidEnterStep(Long userId, FlowCase flowCase) {
    	ListingLocator locator = new ListingLocator();
    	List<FlowEventLog> objs = this.queryFlowEventLogs(locator, 100, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(flowCase.getId()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.NODE_ENTER.getCode()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_USER_ID.eq(userId));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.eq(flowCase.getStepCount()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_VERSION.eq(flowCase.getFlowVersion()));
				
				return query;
			}
    	});  
    	
    	if(objs != null && objs.size() > 0) {
    		return objs.get(0);
    	}
    	
    	return null;
    }
    
    /**
     * 获取最后一个进入的节点日志
     */
    @Override
    public FlowEventLog getLastNodeEnterStep(FlowCase flowCase) {
    	ListingLocator locator = new ListingLocator();
    	List<FlowEventLog> objs = this.queryFlowEventLogs(locator, 100, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(flowCase.getId()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.NODE_ENTER.getCode()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.eq(flowCase.getStepCount()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_VERSION.eq(flowCase.getFlowVersion()));
				
				return query;
			}
    	});  
    	
    	if(objs != null && objs.size() > 0) {
    		return objs.get(objs.size()-1);
    	}
    	
    	return null;
    }
    
    private FlowCaseDetail convertRecordTODetail(Record r) {
    	FlowCaseDetail o = new FlowCaseDetail();
    	
    	o.setId(r.getValue(Tables.EH_FLOW_CASES.ID));
    	o.setNamespaceId(r.getValue(Tables.EH_FLOW_CASES.NAMESPACE_ID));
    	o.setOwnerId(r.getValue(Tables.EH_FLOW_CASES.OWNER_ID));
    	o.setOwnerType(r.getValue(Tables.EH_FLOW_CASES.OWNER_TYPE));
    	o.setModuleId(r.getValue(Tables.EH_FLOW_CASES.MODULE_ID));
    	o.setModuleType(r.getValue(Tables.EH_FLOW_CASES.MODULE_TYPE));
    	o.setModuleName(r.getValue(Tables.EH_FLOW_CASES.MODULE_NAME));
    	o.setApplierName(r.getValue(Tables.EH_FLOW_CASES.APPLIER_NAME));
    	o.setApplierPhone(r.getValue(Tables.EH_FLOW_CASES.APPLIER_PHONE));
    	o.setFlowMainId(r.getValue(Tables.EH_FLOW_CASES.FLOW_MAIN_ID));
    	o.setFlowVersion(r.getValue(Tables.EH_FLOW_CASES.FLOW_VERSION));
    	o.setApplyUserId(r.getValue(Tables.EH_FLOW_CASES.APPLY_USER_ID));
    	o.setProcessUserId(r.getValue(Tables.EH_FLOW_CASES.PROCESS_USER_ID));
    	o.setReferId(r.getValue(Tables.EH_FLOW_CASES.REFER_ID));
    	o.setReferType(r.getValue(Tables.EH_FLOW_CASES.REFER_TYPE));
    	o.setCurrentNodeId(r.getValue(Tables.EH_FLOW_CASES.CURRENT_NODE_ID));
    	o.setStatus(r.getValue(Tables.EH_FLOW_CASES.STATUS));
    	o.setRejectCount(r.getValue(Tables.EH_FLOW_CASES.REJECT_COUNT));
    	o.setRejectNodeId(r.getValue(Tables.EH_FLOW_CASES.REJECT_NODE_ID));
    	o.setStepCount(r.getValue(Tables.EH_FLOW_CASES.STEP_COUNT));
    	o.setLastStepTime(r.getValue(Tables.EH_FLOW_CASES.LAST_STEP_TIME));
    	o.setCreateTime(r.getValue(Tables.EH_FLOW_CASES.CREATE_TIME));
    	o.setCaseType(r.getValue(Tables.EH_FLOW_CASES.CASE_TYPE));
    	o.setContent(r.getValue(Tables.EH_FLOW_CASES.CONTENT));
    	o.setEvaluateScore(r.getValue(Tables.EH_FLOW_CASES.EVALUATE_SCORE));
    	o.setStringTag1(r.getValue(Tables.EH_FLOW_CASES.STRING_TAG1));
    	o.setStringTag2(r.getValue(Tables.EH_FLOW_CASES.STRING_TAG2));
    	o.setStringTag3(r.getValue(Tables.EH_FLOW_CASES.STRING_TAG3));
    	o.setStringTag4(r.getValue(Tables.EH_FLOW_CASES.STRING_TAG4));
    	o.setStringTag5(r.getValue(Tables.EH_FLOW_CASES.STRING_TAG5));
    	o.setIntegralTag1(r.getValue(Tables.EH_FLOW_CASES.INTEGRAL_TAG1));
    	o.setIntegralTag2(r.getValue(Tables.EH_FLOW_CASES.INTEGRAL_TAG2));
    	o.setIntegralTag3(r.getValue(Tables.EH_FLOW_CASES.INTEGRAL_TAG3));
    	o.setIntegralTag4(r.getValue(Tables.EH_FLOW_CASES.INTEGRAL_TAG4));
    	o.setIntegralTag5(r.getValue(Tables.EH_FLOW_CASES.INTEGRAL_TAG5));
    	
    	o.setEventLogId(r.getValue(Tables.EH_FLOW_EVENT_LOGS.ID));
    	o.setTitle(r.getValue(Tables.EH_FLOW_CASES.TITLE));
    	return o;
    }
    
    /**
     * 获取一个 FlowCase 的跳转日志列表
     */
    @Override
    public List<FlowEventLog> findStepEventLogs(Long caseId) {
    	ListingLocator locator = new ListingLocator();
    	return this.queryFlowEventLogs(locator, 100, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(caseId));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.STEP_TRACKER.getCode()));
				
				return query;
			}
    	});    	
    }
    
    @Override
    public List<FlowEventLog> findStepEventLogs(Long caseId, Long stepCount) {
    	ListingLocator locator = new ListingLocator();
    	return this.queryFlowEventLogs(locator, 100, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(caseId));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.STEP_TRACKER.getCode()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.eq(stepCount));
				
				return query;
			}
    	});    	
    }
    
    /**
     * 获取具体一个 FlowCase 是否经过某个节点
     */
    @Override
    public FlowEventLog getStepEvent(Long caseId, Long flowNodeId, Long stepCount) {
    	ListingLocator locator = new ListingLocator();
    	List<FlowEventLog> logs = this.queryFlowEventLogs(locator, 100, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(caseId));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.STEP_TRACKER.getCode()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_NODE_ID.eq(flowNodeId));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.eq(stepCount));
				return query;
			}
    	}); 
    	
    	if(logs != null && logs.size() > 0) {
    		return logs.get(0);
    	}
    	
    	return null;
    }
    
    /**
     *  节点的具体日志跟踪信息
     */
    @Override
    public List<FlowEventLog> findEventLogsByNodeId(Long nodeId, Long caseId, Long stepCount, FlowUserType flowUserType) {
    	ListingLocator locator = new ListingLocator();
    	return this.queryFlowEventLogs(locator, 100, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(caseId));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_NODE_ID.eq(nodeId));
				query.addConditions(
						Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.NODE_TRACKER.getCode())
						);
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.eq(stepCount));
				if(flowUserType == FlowUserType.APPLIER) {
					query.addConditions(FlowEventCustomField.TRACKER_APLIER.getField().eq(1l));
				} else if(flowUserType == FlowUserType.PROCESSOR) {
					query.addConditions(FlowEventCustomField.TRACKER_PROCESSOR.getField().eq(1l));
				}
				
				return query;
			}
    	});
    	
    }
    
    /**
     * 记录某个节点的记录
     */
    @Override
    public List<FlowEventLog> findFiredEventsByLog(FlowEventLog log) {
    	ListingLocator locator = new ListingLocator();
    	return this.queryFlowEventLogs(locator, 100, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(log.getFlowCaseId()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_MAIN_ID.eq(log.getFlowMainId()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_VERSION.eq(log.getFlowVersion()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.NAMESPACE_ID.eq(log.getNamespaceId()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_USER_ID.eq(log.getFlowUserId()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(log.getLogType()));
				
				if(log.getFlowNodeId() != null) {
					query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_NODE_ID.eq(log.getFlowNodeId()));
				}
				
				return query;
			}
    	});
    }
    
    @Override
    public void updateFlowEventLogs(List<FlowEventLog> objs) {
    	if(objs.size() > 0) {
    		  DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEventLogs.class));
	        EhFlowEventLogsDao dao = new EhFlowEventLogsDao(context.configuration());
	        dao.update(objs.toArray(new FlowEventLog[objs.size()]));	
    	}
       
    }
    
    
    @Override
    public List<FlowEventLog> findPrefixStepEventLogs(Long caseId, Long stepCount) {
    	ListingLocator locator = new ListingLocator();
    	return this.queryFlowEventLogs(locator, 100, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(caseId));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.STEP_TRACKER.getCode()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.le(stepCount-1));
				
				return query;
			}
    	});    	
    }
    
    /**
     * 某一步之前的所有实际处理人
     */
    @Override
    public List<FlowEventLog> findPrefixNodeEnterLogs(Long nodeId, Long caseId, Long stepCount) {
    	ListingLocator locator = new ListingLocator();
    	return this.queryFlowEventLogs(locator, 100, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_NODE_ID.eq(nodeId));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(caseId));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.NODE_ENTER.getCode()));
				//转交不算节点跳转 TODO <> 'node_enter'
//				query.addConditions(FlowEventCustomField.BUTTON_FIRED_STEP.getField().isNull()
//						.or(FlowEventCustomField.BUTTON_FIRED_STEP.getField().eq(FlowStepType.TRANSFER_STEP.getCode())));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.le(stepCount-1));
				
				return query;
			}
    	});    	
    }
    
    @Override
    public FlowEventLog findPefixFireLog(Long nodeId, Long fromNodeId, Long caseId, Long stepCount) {
    	ListingLocator locator = new ListingLocator();
    	List<FlowEventLog> logs = this.queryFlowEventLogs(locator, 100, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_NODE_ID.eq(nodeId));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(caseId));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.BUTTON_FIRED.getCode()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.eq(stepCount-1));
				
				return query;
			}
    	});
    	
    	if(logs != null && logs.size() > 0) {
    		return logs.get(0);
    	}
    	
    	return null;
    }
    
    /**
     * 当前节点的实际处理人的日志
     */
    @Override
    public List<FlowEventLog> findCurrentNodeEnterLogs(Long nodeId, Long caseId, Long stepCount) {
    	ListingLocator locator = new ListingLocator();
    	return this.queryFlowEventLogs(locator, 100, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_NODE_ID.eq(nodeId));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(caseId));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.NODE_ENTER.getCode()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.eq(stepCount));
				
				return query;
			}
    	});    	
    }

    /**
     * 查询flowCase的某个节点的最大stepCount
     */
    @Override
    public Long findMaxStepCountByNodeEnterLog(Long nodeId, Long caseId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select(DSL.max(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT))
                .from(Tables.EH_FLOW_EVENT_LOGS)
                .where(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(caseId))
                .and(Tables.EH_FLOW_EVENT_LOGS.FLOW_NODE_ID.eq(nodeId))
                .and(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.NODE_ENTER.getCode()))
                .fetchAnyInto(Long.class);
    }
}
