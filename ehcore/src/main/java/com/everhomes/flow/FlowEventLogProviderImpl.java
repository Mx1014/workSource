package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.TrueOrFalseFlag;
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
import com.everhomes.util.RecordHelper;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public class FlowEventLogProviderImpl implements FlowEventLogProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Autowired
    private CacheManager cacheManager;

    @Caching(evict = {
        @CacheEvict(value = "ProcessorServiceTypes", key = "{#obj.flowUserId}"),
        @CacheEvict(value = "ProcessorApps", key = "{#obj.flowUserId}"),
    })
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

    @Caching(evict = {
        @CacheEvict(value = "ProcessorServiceTypes", key = "{#obj.flowUserId}"),
        @CacheEvict(value = "ProcessorApps", key = "{#obj.flowUserId}"),
    })
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
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.ID.ge(locator.getAnchor()));
        }
        query.addConditions(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.ge(0L));

        query.addOrderBy(Tables.EH_FLOW_EVENT_LOGS.ID);
        query.addLimit(count + 1);
        List<FlowEventLog> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FlowEventLog.class);
        });

        if(objs.size() > count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
            objs.remove(objs.size() - 1);
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(FlowEventLog obj) {
    	Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
    	obj.setCreateTime(now);

    	// 清缓存
        Cache cache = cacheManager.getCache("ProcessorServiceTypes");
        if (cache != null) {
            cache.evict(obj.getFlowUserId());
        }
        cache = cacheManager.getCache("ProcessorApps");
        if (cache != null) {
            cache.evict(obj.getFlowUserId());
        }
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
        dao.insert(objs.toArray(new FlowEventLog[0]));
    }
    
    /**
     * 获取 待处理/处理中/督办 的 FlowCase
     */
    @Override
    public List<FlowCaseDetail> findProcessorFlowCases(ListingLocator locator, int count, SearchFlowCaseCommand cmd, ListingQueryBuilderCallback callback) {
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhFlowCases.class));

        if(locator.getAnchor() == null) {
            locator.setAnchor(cmd.getPageAnchor());
        }

        Condition cond = buildSearchFlowCaseCondition(locator, cmd);

        List<Field> fields = new ArrayList<>(Arrays.asList(Tables.EH_FLOW_CASES.fields()));
        fields.add(Tables.EH_FLOW_EVENT_LOGS.ID);

        SelectQuery<Record> query = context.select(fields.toArray(new Field[fields.size()]))
                .from(Tables.EH_FLOW_EVENT_LOGS).join(Tables.EH_FLOW_CASES)
                .on(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(Tables.EH_FLOW_CASES.ID))
                .join(Tables.EH_FLOWS)
                .on(Tables.EH_FLOW_CASES.FLOW_MAIN_ID.eq(Tables.EH_FLOWS.FLOW_MAIN_ID)
                        .and(Tables.EH_FLOW_CASES.FLOW_VERSION.eq(Tables.EH_FLOWS.FLOW_VERSION)))
                .where(cond).orderBy(Tables.EH_FLOW_EVENT_LOGS.ID.desc()).limit(count + 1).getQuery();

        if (callback != null) {
            callback.buildCondition(locator, query);
        }

        List<FlowCaseDetail> objs = query.fetch().map((r) -> {
            FlowCaseDetail detail = r.into(FlowCaseDetail.class);
            detail.setId(r.getValue(Tables.EH_FLOW_CASES.ID));
            detail.setEventLogId(r.getValue(Tables.EH_FLOW_EVENT_LOGS.ID));
            // objs.add(convertRecordToDetail(r));
            return detail;
        });

        if(objs.size() > count) {
            locator.setAnchor(objs.get(objs.size() - 1).getEventLogId());
            objs.remove(objs.size() - 1);
        } else {
            locator.setAnchor(null);
        }
		return objs;
    }

    private Condition buildSearchFlowCaseCondition(ListingLocator locator, SearchFlowCaseCommand cmd) {
        Condition cond = Tables.EH_FLOW_CASES.STATUS.ne(FlowCaseStatus.INVALID.getCode())
                .and(Tables.EH_FLOW_CASES.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                .and(Tables.EH_FLOW_CASES.DELETE_FLAG.eq(TrueOrFalseFlag.FALSE.getCode()));

        FlowCaseSearchType searchType = FlowCaseSearchType.fromCode(cmd.getFlowCaseSearchType());
        if(FlowCaseSearchType.TODO_LIST.equals(searchType)) {
            cond = cond.and(Tables.EH_FLOW_CASES.STATUS.in(
                    FlowCaseStatus.PROCESS.getCode(), FlowCaseStatus.SUSPEND.getCode()));
            cond = cond.and(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.NODE_ENTER.getCode()))
    		.and(Tables.EH_FLOW_EVENT_LOGS.FLOW_USER_ID.eq(cmd.getUserId()))
    		.and(Tables.EH_FLOW_CASES.STEP_COUNT.eq(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT)) //step_cout must equal the same
            .and(Tables.EH_FLOW_EVENT_LOGS.ENTER_LOG_COMPLETE_FLAG.eq(TrueOrFalseFlag.FALSE.getCode()));
        } else if (FlowCaseSearchType.DONE_LIST.equals(searchType)) {
            // cond = cond.and(Tables.EH_FLOW_CASES.PARENT_ID.eq(0L));
            cond = cond.and(Tables.EH_FLOW_CASES.STATUS.in(
                    FlowCaseStatus.PROCESS.getCode(),
                    FlowCaseStatus.FINISHED.getCode(),
                    FlowCaseStatus.SUSPEND.getCode(),
                    FlowCaseStatus.ABSORTED.getCode())
            );
    		cond = cond.and(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.BUTTON_FIRED.getCode()))
    		.and(Tables.EH_FLOW_EVENT_LOGS.FLOW_USER_ID.eq(cmd.getUserId()))
    		.and(FlowEventCustomField.BUTTON_FIRED_COUNT.getField().eq(0L));
    	} else if(FlowCaseSearchType.SUPERVISOR.equals(searchType)) {
            cond = cond.and(Tables.EH_FLOW_CASES.PARENT_ID.eq(0L));
            cond = cond.and(Tables.EH_FLOW_CASES.STATUS.in(
                    FlowCaseStatus.SUSPEND.getCode(),
                    FlowCaseStatus.PROCESS.getCode(),
                    FlowCaseStatus.FINISHED.getCode(),
                    FlowCaseStatus.ABSORTED.getCode())
            );
            cond = cond.and(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.FLOW_SUPERVISOR.getCode()))
    		.and(Tables.EH_FLOW_EVENT_LOGS.FLOW_USER_ID.eq(cmd.getUserId()));
    	}

        if(cmd.getModuleId() != null) {
            cond = cond.and(Tables.EH_FLOW_CASES.MODULE_ID.eq(cmd.getModuleId()));
        }
        if (cmd.getOriginAppId() != null && cmd.getOriginAppId() != 0) {
            cond = cond.and(Tables.EH_FLOW_CASES.ORIGIN_APP_ID.eq(cmd.getOriginAppId()));
        }
        if(cmd.getOrganizationId() != null) {
            cond = cond.and(Tables.EH_FLOW_CASES.ORGANIZATION_ID.eq(cmd.getOrganizationId()));
        }
        if(cmd.getProjectId() != null) {
            cond = cond.and(Tables.EH_FLOW_CASES.PROJECT_ID.eq(cmd.getProjectId()));
        }
        if(cmd.getProjectType() != null) {
            cond = cond.and(Tables.EH_FLOW_CASES.PROJECT_TYPE.eq(cmd.getProjectType()));
        }
        if(cmd.getFlowCaseStatus() != null) {
            cond = cond.and(Tables.EH_FLOW_CASES.STATUS.eq(cmd.getFlowCaseStatus()));
        }
        if(cmd.getServiceType() != null) {
            cond = cond.and(
                Tables.EH_FLOW_CASES.TITLE.eq(cmd.getServiceType())
                    .or(Tables.EH_FLOW_CASES.MODULE_NAME.eq(cmd.getServiceType())
                        .or(Tables.EH_FLOW_CASES.SERVICE_TYPE.eq(cmd.getServiceType())))
            );
        }
        if(cmd.getOwnerId() != null) {
            cond = cond.and(Tables.EH_FLOW_CASES.OWNER_ID.eq(cmd.getOwnerId()));
        }
        if(cmd.getOwnerType() != null) {
            cond = cond.and(Tables.EH_FLOW_CASES.OWNER_TYPE.eq(cmd.getOwnerType()));
        }
        if(cmd.getStartTime() != null && cmd.getEndTime() == null) {
            cond = cond.and(Tables.EH_FLOW_CASES.CREATE_TIME.ge(new Timestamp(cmd.getStartTime())));
        } else if(cmd.getEndTime() != null && cmd.getStartTime() == null) {
            cond = cond.and(Tables.EH_FLOW_CASES.CREATE_TIME.le(new Timestamp(cmd.getEndTime())));
        } else if(cmd.getEndTime() != null && cmd.getStartTime() != null) {
            cond = cond.and(Tables.EH_FLOW_CASES.CREATE_TIME.between(
                    new Timestamp(cmd.getStartTime()), new Timestamp(cmd.getEndTime())));
        }
        if(cmd.getKeyword() != null && !cmd.getKeyword().isEmpty()) {
            cond = cond.and(
                    Tables.EH_FLOW_CASES.TITLE.like("%" + cmd.getKeyword() + "%")
                        .or(Tables.EH_FLOW_CASES.SERVICE_TYPE.like("%" + cmd.getKeyword() + "%"))
                        .or(Tables.EH_FLOW_CASES.CONTENT.like("%" + cmd.getKeyword() + "%"))
                            .or(Tables.EH_FLOW_CASES.APPLIER_NAME.like(cmd.getKeyword() + "%"))
                                .or(Tables.EH_FLOW_CASES.APPLIER_PHONE.like(cmd.getKeyword() + "%"))
            );

        }

        if(locator.getAnchor() != null) {
    	    cond = cond.and(Tables.EH_FLOW_EVENT_LOGS.ID.le(locator.getAnchor()));

        }
        cond = cond.and(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.ge(0L));
        return cond;
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
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.ENTER_LOG_COMPLETE_FLAG.eq(TrueOrFalseFlag.FALSE.getCode()));

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
    	List<FlowEventLog> objs = this.queryFlowEventLogs(locator, 1000, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(flowCase.getId()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.NODE_ENTER.getCode()));
                if (userId != null) {
                    query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_USER_ID.eq(userId));
                }
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.eq(flowCase.getStepCount()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_VERSION.eq(flowCase.getFlowVersion()));
                query.addConditions(Tables.EH_FLOW_EVENT_LOGS.ENTER_LOG_COMPLETE_FLAG.eq(TrueOrFalseFlag.FALSE.getCode()));
				
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
    
    /**
     * 获取一个 FlowCase 的跳转日志列表
     */
    @Override
    public List<FlowEventLog> findStepEventLogs(Long caseId) {
    	ListingLocator locator = new ListingLocator();
    	return this.queryFlowEventLogs(locator, 200, new ListingQueryBuilderCallback() {
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
    public List<FlowEventLog> findEventLogsByNodeId(Long nodeId, Long caseId, Long stepCount, Set<FlowUserType> flowUserTypes) {
    	return this.queryFlowEventLogs(new ListingLocator(), 100, (locator, query) -> {
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(caseId));
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_NODE_ID.eq(nodeId));
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.NODE_TRACKER.getCode()));
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.eq(stepCount));

            Condition userTypeCondition = null;
            for (FlowUserType flowUserType : flowUserTypes) {
                if(flowUserType == FlowUserType.APPLIER) {
                    if (userTypeCondition == null) {
                        userTypeCondition = FlowEventCustomField.TRACKER_APPLIER.getField().eq(1L);
                    } else {
                        userTypeCondition = userTypeCondition.or(FlowEventCustomField.TRACKER_APPLIER.getField().eq(1L));
                    }
                } else if(flowUserType == FlowUserType.PROCESSOR) {
                    if (userTypeCondition == null) {
                        userTypeCondition = FlowEventCustomField.TRACKER_PROCESSOR.getField().eq(1L);
                    } else {
                        userTypeCondition = userTypeCondition.or(FlowEventCustomField.TRACKER_PROCESSOR.getField().eq(1L));
                    }
                }
            }
            if (userTypeCondition != null) {
                query.addConditions(userTypeCondition);
            }
            return query;
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
     * 当前节点的还没有处理完成的处理人
     */
    @Override
    public List<FlowEventLog> findCurrentNodeNotCompleteEnterLogs(Long nodeId, Long caseId, Long stepCount) {
    	ListingLocator locator = new ListingLocator();
    	return this.queryFlowEventLogs(locator, 100, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_NODE_ID.eq(nodeId));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(caseId));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.NODE_ENTER.getCode()));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.eq(stepCount));
				query.addConditions(Tables.EH_FLOW_EVENT_LOGS.ENTER_LOG_COMPLETE_FLAG.eq(TrueOrFalseFlag.FALSE.getCode()));
				return query;
			}
    	});
    }

    /**
     * 节点的处理人列表，不管是否已经处理，转交后以转交后的为准
     */
    @Override
    public List<FlowEventLog> findNodeEnterLogs(Long nodeId, Long caseId, Long stepCount) {
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

    @Override
    public FlowEventLog findNodeLastStepTrackerLog(Long nodeId, Long caseId) {
        List<FlowEventLog> flowEventLogs = this.queryFlowEventLogs(new ListingLocator(), 1, (locator, query) -> {
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_NODE_ID.eq(nodeId));
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(caseId));
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.STEP_TRACKER.getCode()));
            query.addOrderBy(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.desc());
            return query;
        });
        if (flowEventLogs.size() > 0) {
            return flowEventLogs.get(0);
        }
        return null;
    }

    @Override
    public List<FlowEventLog> findStepEventLogs(List<Long> flowCaseIdList) {
        return this.queryFlowEventLogs(new ListingLocator(), 200, (locator, query) -> {
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.in(flowCaseIdList));
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.STEP_TRACKER.getCode()));
            return query;
        });
    }

    @Override
    public List<FlowOperateLogDTO> searchOperateLogs(SearchFlowOperateLogsCommand cmd, Integer pageSize, ListingLocator locator) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        com.everhomes.server.schema.tables.EhFlowEventLogs log = Tables.EH_FLOW_EVENT_LOGS;
        com.everhomes.server.schema.tables.EhFlowCases flowCase = Tables.EH_FLOW_CASES;

        SelectQuery<Record10<Long, Long, String, String, String, Long, String, String, String, Timestamp>> query = context
                .select(log.ID, flowCase.ID, flowCase.TITLE, flowCase.ROUTE_URI, flowCase.MODULE_NAME, flowCase.MODULE_ID,
                        flowCase.CONTENT, log.LOG_CONTENT, log.FLOW_USER_NAME, log.CREATE_TIME)
                .from(log)
                .join(flowCase).on(log.FLOW_CASE_ID.eq(flowCase.ID))
                .getQuery();

        query.addConditions(flowCase.STATUS.ne(FlowCaseStatus.INVALID.getCode()));// 无效状态的flowCase不要显示
        query.addConditions(flowCase.DELETE_FLAG.ne(TrueOrFalseFlag.TRUE.getCode()));// 无效状态的flowCase不要显示

        if (cmd.getModuleId() != null && cmd.getModuleId() != 0) {
            query.addConditions(flowCase.MODULE_ID.eq(cmd.getModuleId()));
        }
        if (cmd.getOrganizationId() != null && cmd.getOrganizationId() != 0) {
            query.addConditions(flowCase.ORGANIZATION_ID.eq(cmd.getOrganizationId()));
        }
        if (cmd.getFlowCaseId() != null && cmd.getFlowCaseId() != 0) {
            query.addConditions(flowCase.ID.eq(cmd.getFlowCaseId()));
        }
        if (cmd.getUserId() != null && cmd.getUserId() != 0) {
            query.addConditions(log.FLOW_USER_ID.eq(cmd.getUserId()));
        }

        query.addConditions(log.LOG_TYPE.eq(FlowLogType.BUTTON_FIRED.getCode()));

        if (cmd.getServiceType() != null) {
            query.addConditions(flowCase.SERVICE_TYPE.eq(cmd.getServiceType()));
        }
        if (cmd.getKeyword() != null && cmd.getKeyword().length() > 0) {
            String kw = "%" + cmd.getKeyword() + "%";
            query.addConditions(flowCase.CONTENT.like(kw).or(flowCase.TITLE.like(kw)).or(flowCase.MODULE_NAME.like(kw)));
        }
        query.addOrderBy(log.ID.desc());

        if (locator.getAnchor() != null) {
            query.addConditions(log.ID.le(locator.getAnchor()));
        }
        query.addLimit(pageSize + 1);

        List<FlowOperateLogDTO> dtoList = query.fetch().map(r -> {
            FlowOperateLogDTO dto = new FlowOperateLogDTO();
            dto.setId(r.getValue(log.ID));
            dto.setFlowCaseId(r.getValue(flowCase.ID));
            dto.setModuleId(r.getValue(flowCase.MODULE_ID));
            dto.setRouteUri(r.getValue(flowCase.ROUTE_URI));
            String title = r.getValue(flowCase.TITLE);
            if (title == null || title.isEmpty()) {
                title = r.getValue(flowCase.MODULE_NAME);
            }
            dto.setFlowCaseTitle(title);
            dto.setFlowCaseContent(r.getValue(flowCase.CONTENT));
            dto.setLogContent(r.getValue(log.LOG_CONTENT));
            dto.setCreateTime(r.getValue(log.CREATE_TIME));
            dto.setFlowUserName(r.getValue(log.FLOW_USER_NAME));
            return dto;
        });
        if (dtoList.size() > pageSize) {
            FlowOperateLogDTO dto = dtoList.remove(dtoList.size() - 1);
            locator.setAnchor(dto.getId());
        } else {
            locator.setAnchor(null);
        }
        return dtoList;
    }

    @Override
    public FlowEventLog isSupervisor(Long userId, FlowCase flowCase) {
        List<FlowEventLog> objs = this.queryFlowEventLogs(new ListingLocator(), 1, (locator, query) -> {
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(flowCase.getId()));
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.FLOW_SUPERVISOR.getCode()));
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_USER_ID.eq(userId));
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_VERSION.eq(flowCase.getFlowVersion()));
            return query;
        });
        if(objs != null && objs.size() > 0) {
            return objs.get(0);
        }
        return null;
    }

    @Override
    public List<FlowEventLog> findFlowCaseSupervisors(FlowCase flowCase) {
        return this.queryFlowEventLogs(new ListingLocator(), 200, (locator, query) -> {
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(flowCase.getId()));
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_VERSION.eq(flowCase.getFlowVersion()));
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.FLOW_SUPERVISOR.getCode()));
            return query;
        });
    }

    @Override
    public List<FlowEventLog> findRejectEventLogsByNodeId(Long nodeId, Long flowCaseId, Long stepCount) {
        return this.queryFlowEventLogs(new ListingLocator(), 1, (locator, query) -> {
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_NODE_ID.eq(nodeId));
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(flowCaseId));
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.REJECT_TRACKER.getCode()));
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.STEP_COUNT.eq(stepCount));
            return query;
        });
    }

    @Override
    public FlowEventLog isHistoryProcessors(Long userId, FlowCase flowCase) {
        List<FlowEventLog> flowEventLogs = this.queryFlowEventLogs(new ListingLocator(), 1, (locator, query) -> {
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.in(flowCase.getId()));
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.FLOW_USER_ID.eq(userId));
            query.addConditions(Tables.EH_FLOW_EVENT_LOGS.LOG_TYPE.eq(FlowLogType.BUTTON_FIRED.getCode()));
            return query;
        });
        if (flowEventLogs != null && flowEventLogs.size() > 0) {
            return flowEventLogs.get(0);
        }
        return null;
    }

    @Override
    public Integer countProcessorFlowCases(SearchFlowCaseCommand cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhFlowCases.class));

        Condition condition = buildSearchFlowCaseCondition(new ListingLocator(), cmd);

        return context.select(DSL.count())
                .from(Tables.EH_FLOW_EVENT_LOGS)
                .join(Tables.EH_FLOW_CASES)
                .on(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(Tables.EH_FLOW_CASES.ID))
                .join(Tables.EH_FLOWS)
                .on(Tables.EH_FLOW_CASES.FLOW_MAIN_ID.eq(Tables.EH_FLOWS.FLOW_MAIN_ID)
                        .and(Tables.EH_FLOW_CASES.FLOW_VERSION.eq(Tables.EH_FLOWS.FLOW_VERSION)))
                .where(condition)
                .fetchAnyInto(Integer.class);
    }

    // @Cacheable(value = "ProcessorServiceTypes", key = "{#userId}", unless = "#result == null")
    @Override
    public List<FlowServiceTypeDTO> listProcessorServiceTypes(Integer namespaceId, Long userId, SearchFlowCaseCommand cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhFlowCases.class));
        com.everhomes.server.schema.tables.EhFlowCases t = Tables.EH_FLOW_CASES;

        Condition condition = buildSearchFlowCaseCondition(new ListingLocator(), cmd);
        return context.select(t.fields())
                .from(Tables.EH_FLOW_EVENT_LOGS)
                .join(t)
                .on(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(t.ID))
                .join(Tables.EH_FLOWS)
                .on(t.FLOW_MAIN_ID.eq(Tables.EH_FLOWS.FLOW_MAIN_ID)
                        .and(t.FLOW_VERSION.eq(Tables.EH_FLOWS.FLOW_VERSION)))
                .where(condition)
                .groupBy(t.MODULE_ID, t.SERVICE_TYPE)
                .fetch(record -> {
                    FlowServiceTypeDTO dto = RecordHelper.convert(record, FlowServiceTypeDTO.class);
                    dto.setServiceName(record.getValue(t.SERVICE_TYPE));
                    return dto;
                });
    }

    @Cacheable(value = "ProcessorApps", key = "{#userId}", unless = "#result == null")
    @Override
    public List<FlowModuleAppDTO> listProcessorApps(Integer namespaceId, Long userId, SearchFlowCaseCommand cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhFlowCases.class));
        com.everhomes.server.schema.tables.EhFlowCases t = Tables.EH_FLOW_CASES;

        Condition condition = buildSearchFlowCaseCondition(new ListingLocator(), cmd);
        return context.select(t.fields())
                .from(Tables.EH_FLOW_EVENT_LOGS)
                .join(t)
                .on(Tables.EH_FLOW_EVENT_LOGS.FLOW_CASE_ID.eq(t.ID))
                .join(Tables.EH_FLOWS)
                .on(t.FLOW_MAIN_ID.eq(Tables.EH_FLOWS.FLOW_MAIN_ID)
                        .and(t.FLOW_VERSION.eq(Tables.EH_FLOWS.FLOW_VERSION)))
                .where(condition)
                .groupBy(t.ORIGIN_APP_ID)
                .fetch(record -> {
                    FlowModuleAppDTO dto = RecordHelper.convert(record, FlowModuleAppDTO.class);
                    dto.setOriginId(record.getValue(t.ORIGIN_APP_ID));
                    return dto;
                });
    }
}
