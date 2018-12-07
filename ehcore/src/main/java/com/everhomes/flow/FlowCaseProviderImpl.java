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
import com.everhomes.server.schema.tables.daos.EhFlowCasesDao;
import com.everhomes.server.schema.tables.pojos.EhFlowCases;
import com.everhomes.server.schema.tables.records.EhFlowCasesRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RecordHelper;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class FlowCaseProviderImpl implements FlowCaseProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Caching(evict = {
        @CacheEvict(value = "ApplierServiceTypes", key="{#obj.namespaceId, #obj.applyUserId}"),
        @CacheEvict(value = "ApplierApps", key="{#obj.namespaceId, #obj.applyUserId}")
    })
    @Override
    public Long createFlowCase(FlowCase obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowCases.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowCases.class));
        obj.setId(id);
        prepareObj(obj);
        EhFlowCasesDao dao = new EhFlowCasesDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Caching(evict = {
        @CacheEvict(value = "ApplierServiceTypes", key="{#obj.namespaceId, #obj.applyUserId}"),
        @CacheEvict(value = "ApplierApps", key="{#obj.namespaceId, #obj.applyUserId}")
    })
    @Override
    public Long createFlowCaseHasId(FlowCase obj) {
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowCases.class));
        
        prepareObj(obj);
        EhFlowCasesDao dao = new EhFlowCasesDao(context.configuration());
        dao.insert(obj);
        return obj.getId();
    }

    @Override
    public List<FlowCase> findFlowCaseByNode(Long originalNodeId, Long convergenceNodeId) {
        return this.queryFlowCases(new ListingLocator(), 100, (locator, query) -> {
            com.everhomes.server.schema.tables.EhFlowCases t = Tables.EH_FLOW_CASES;
            query.addConditions(t.START_NODE_ID.eq(originalNodeId));
            query.addConditions(t.END_NODE_ID.eq(convergenceNodeId));
            return query;
        });
    }

    @Override
    public List<FlowCase> listFlowCaseByParentId(Long parentId) {
        return this.queryFlowCases(new ListingLocator(), 100, (locator, query) -> {
            com.everhomes.server.schema.tables.EhFlowCases t = Tables.EH_FLOW_CASES;
            query.addConditions(t.PARENT_ID.eq(parentId));
            return query;
        });
    }

    @Override
    public FlowCase findFlowCaseByStartLinkId(Long parentId, Long startNodeId, Long endNodeId, Long startLinkId) {
        List<FlowCase> flowCases = this.queryFlowCases(new ListingLocator(), 1, (locator, query) -> {
            com.everhomes.server.schema.tables.EhFlowCases t = Tables.EH_FLOW_CASES;
            query.addConditions(t.PARENT_ID.eq(parentId));
            query.addConditions(t.START_NODE_ID.eq(startNodeId));
            query.addConditions(t.END_NODE_ID.eq(endNodeId));
            query.addConditions(t.START_LINK_ID.eq(startLinkId));
            return query;
        });
        if (flowCases.size() > 0) {
            return flowCases.get(0);
        }
        return null;
    }

    @Override
    public Long getNextId() {
        return this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowCases.class));
    }

    @Override
    public Integer countAdminFlowCases(SearchFlowCaseCommand cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowCases.class));
        FlowCaseSearchType searchType = FlowCaseSearchType.fromCode(cmd.getFlowCaseSearchType());
        if(searchType.equals(FlowCaseSearchType.ADMIN)) {
            Condition condition = buildSearchFlowCaseCmdCondition(new ListingLocator(), cmd);
            return context.select()
                    .from(Tables.EH_FLOW_CASES)
                    .where(condition)
                    .fetchCount();
        }
        return null;
    }

    @Override
    public List<FlowCase> listFlowCaseGroupByServiceTypes(Integer namespaceId, SearchFlowCaseCommand cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhFlowCases t = Tables.EH_FLOW_CASES;

        Condition condition = buildSearchFlowCaseCmdCondition(new ListingLocator(), cmd);
        return context.selectFrom(t)
                .where(t.NAMESPACE_ID.eq(namespaceId))
                .and(condition)
                .groupBy(t.MODULE_ID, t.SERVICE_TYPE)
                .fetchInto(FlowCase.class);
    }

    @Override
    public FlowCase getFlowCaseBySubFlowParentId(Long parentId) {
        List<FlowCase> list = this.queryFlowCases(new ListingLocator(), 1, (locator, query) -> {
            com.everhomes.server.schema.tables.EhFlowCases t = Tables.EH_FLOW_CASES;
            query.addConditions(t.SUB_FLOW_PARENT_ID.eq(parentId));
            return query;
        });
        if (list.size() > 0) {
            return list.iterator().next();
        }
        return null;
    }

    @Cacheable(value = "ApplierServiceTypes", key="{#namespaceId, #userId}", unless="#result == null")
    @Override
    public List<FlowServiceTypeDTO> listApplierServiceTypes(Integer namespaceId, Long userId, SearchFlowCaseCommand cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhFlowCases t = Tables.EH_FLOW_CASES;

        Condition condition = buildSearchFlowCaseCmdCondition(new ListingLocator(), cmd);

        return context.selectFrom(t)
                .where(condition)
                .groupBy(t.MODULE_ID, t.SERVICE_TYPE)
                .fetch(record -> {
                    FlowServiceTypeDTO dto = RecordHelper.convert(record, FlowServiceTypeDTO.class);
                    dto.setServiceName(record.getServiceType());
                    return dto;
                });
    }

    @Override
    public List<FlowModuleAppDTO> listAdminApps(Integer namespaceId, SearchFlowCaseCommand cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhFlowCases t = Tables.EH_FLOW_CASES;

        cmd.setUserId(null);
        Condition condition = buildSearchFlowCaseCmdCondition(new ListingLocator(), cmd);

        return context.selectFrom(t)
                .where(condition)
                .groupBy(t.ORIGIN_APP_ID)
                .fetch(record -> {
                    FlowModuleAppDTO dto = RecordHelper.convert(record, FlowModuleAppDTO.class);
                    dto.setOriginId(record.getOriginAppId());
                    return dto;
                });
    }

    @Cacheable(value = "ApplierApps", key="{#namespaceId, #userId}", unless="#result == null")
    @Override
    public List<FlowModuleAppDTO> listApplierApps(Integer namespaceId, Long userId, SearchFlowCaseCommand cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        com.everhomes.server.schema.tables.EhFlowCases t = Tables.EH_FLOW_CASES;

        Condition condition = buildSearchFlowCaseCmdCondition(new ListingLocator(), cmd);

        return context.selectFrom(t)
                .where(condition)
                .groupBy(t.ORIGIN_APP_ID)
                .fetch(record -> {
                    FlowModuleAppDTO dto = RecordHelper.convert(record, FlowModuleAppDTO.class);
                    dto.setOriginId(record.getOriginAppId());
                    return dto;
                });
    }

    @Override
    public Integer countApplierFlowCases(SearchFlowCaseCommand cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowCases.class));
        FlowCaseSearchType searchType = FlowCaseSearchType.fromCode(cmd.getFlowCaseSearchType());
        if(searchType.equals(FlowCaseSearchType.APPLIER)) {
            Condition condition = buildSearchFlowCaseCmdCondition(new ListingLocator(), cmd);
            return context.select()
                    .from(Tables.EH_FLOW_CASES)
                    .where(condition)
                    .fetchCount();
        }
        return null;
    }

    @Override
    public void updateFlowCase(FlowCase obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowCases.class));
        EhFlowCasesDao dao = new EhFlowCasesDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteFlowCase(FlowCase obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowCases.class));
        EhFlowCasesDao dao = new EhFlowCasesDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public FlowCase getFlowCaseById(Long id) {
        try {
        FlowCase[] result = new FlowCase[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowCases.class));

        result[0] = context.select().from(Tables.EH_FLOW_CASES)
            .where(Tables.EH_FLOW_CASES.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, FlowCase.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<FlowCase> queryFlowCases(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowCases.class));

        SelectQuery<EhFlowCasesRecord> query = context.selectQuery(Tables.EH_FLOW_CASES);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator.getAnchor() != null) {
            query.addConditions(Tables.EH_FLOW_CASES.ID.lt(locator.getAnchor()));
            }

        query.addLimit(count);
        query.addOrderBy(Tables.EH_FLOW_CASES.ID.desc());
        List<FlowCase> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, FlowCase.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(FlowCase obj) {
		Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
		obj.setCreateTime(now);
		obj.setLastStepTime(now);
    }
    
    @Override
    public List<FlowCaseDetail> findApplierFlowCases(ListingLocator locator, int count, SearchFlowCaseCommand cmd, ListingQueryBuilderCallback callback) {
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhFlowCases.class));

    	if(locator.getAnchor() == null) {
    		locator.setAnchor(cmd.getPageAnchor());
        }

        FlowCaseSearchType searchType = FlowCaseSearchType.fromCode(cmd.getFlowCaseSearchType());
        if(searchType.equals(FlowCaseSearchType.APPLIER)) {
            com.everhomes.server.schema.tables.EhFlowCases t = Tables.EH_FLOW_CASES;

            Condition cond = buildSearchFlowCaseCmdCondition(locator, cmd);
            cond = cond.and(t.SUB_FLOW_PARENT_ID.eq(0L));

            SelectQuery<Record> query = context
                    .select(t.fields())
                    .from(t)
                    .where(cond)
                    .getQuery();

            if (callback != null) {
                callback.buildCondition(locator, query);
            }
            query.addOrderBy(t.ID.desc());
            query.addLimit(count + 1);

            List<FlowCaseDetail> objs = query.fetchInto(FlowCaseDetail.class);

            // List<FlowCaseDetail> objs = records.stream().map((r) -> {
    		// 	return ConvertHelper.convert(r, FlowCaseDetail.class);
    		// }).collect(Collectors.toList());

            if(objs.size() > count) {
                locator.setAnchor(objs.get(objs.size() - 1).getId());
                objs.remove(objs.size() - 1);
            } else {
                locator.setAnchor(null);
            }
            return objs;
    	} else {
    		return null;
    	}
    }

    private Condition buildSearchFlowCaseCmdCondition(ListingLocator locator, SearchFlowCaseCommand cmd) {
        Condition cond = Tables.EH_FLOW_CASES.STATUS.ne(FlowCaseStatus.INVALID.getCode())
                .and(Tables.EH_FLOW_CASES.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                .and(Tables.EH_FLOW_CASES.DELETE_FLAG.eq(TrueOrFalseFlag.FALSE.getCode()));

        if(cmd.getUserId() != null)
            cond = cond.and(Tables.EH_FLOW_CASES.APPLY_USER_ID.eq(cmd.getUserId()));
        cond = cond.and(Tables.EH_FLOW_CASES.PARENT_ID.eq(0L));
        cond = cond.and(Tables.EH_FLOW_CASES.DELETE_FLAG.eq(TrueOrFalseFlag.FALSE.getCode()));

        if(locator.getAnchor() != null) {
            cond = cond.and(Tables.EH_FLOW_CASES.ID.le(locator.getAnchor()));
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
        if(cmd.getOwnerId() != null) {
            cond = cond.and(Tables.EH_FLOW_CASES.OWNER_ID.eq(cmd.getOwnerId()));
        }
        if(cmd.getOwnerType() != null) {
            cond = cond.and(Tables.EH_FLOW_CASES.OWNER_TYPE.eq(cmd.getOwnerType()));
        }
        if(cmd.getFlowCaseStatus() != null) {
            cond = cond.and(Tables.EH_FLOW_CASES.STATUS.eq(cmd.getFlowCaseStatus()));
        } else {
           cond = cond.and(Tables.EH_FLOW_CASES.STATUS.notIn(
                   FlowCaseStatus.INITIAL.getCode(), FlowCaseStatus.INVALID.getCode())
           );
        }
        if(cmd.getServiceType() != null) {
            cond = cond.and(
                    Tables.EH_FLOW_CASES.TITLE.eq(cmd.getServiceType())
                            .or(Tables.EH_FLOW_CASES.MODULE_NAME.eq(cmd.getServiceType())
                                    .or(Tables.EH_FLOW_CASES.SERVICE_TYPE.eq(cmd.getServiceType())))
            );
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
            String keyword = "%" + cmd.getKeyword().trim() + "%";
            cond = cond.and(
                    Tables.EH_FLOW_CASES.TITLE.like(keyword)
                    .or(Tables.EH_FLOW_CASES.SERVICE_TYPE.like(keyword))
                    .or(Tables.EH_FLOW_CASES.CONTENT.like(keyword))
                    .or(Tables.EH_FLOW_CASES.APPLIER_NAME.like(keyword))
                    .or(Tables.EH_FLOW_CASES.APPLIER_PHONE.like(keyword))
            );
        }
        return cond;
    }

    public List<FlowCaseDetail> listFlowCasesByModuleId(ListingLocator locator,
												 int count, SearchFlowCaseCommand cmd){
			DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhFlowCases.class));
			Condition cond = Tables.EH_FLOW_CASES.STATUS.ne(FlowCaseStatus.INVALID.getCode());
			if(locator.getAnchor() == null) {
				locator.setAnchor(cmd.getPageAnchor());
			}
			if(locator.getAnchor() != null) {
				cond = cond.and(Tables.EH_FLOW_CASES.ID.lt(locator.getAnchor()));
			}
			FlowCaseSearchType searchType = FlowCaseSearchType.fromCode(cmd.getFlowCaseSearchType());
			if(searchType.equals(FlowCaseSearchType.APPLIER)) {
				cond = cond.and(Tables.EH_FLOW_CASES.MODULE_ID.eq(cmd.getModuleId()));

				List<FlowCaseDetail> records = context.select().from(Tables.EH_FLOW_CASES)
						.where(cond).orderBy(Tables.EH_FLOW_CASES.ID.desc())
						.limit(count).fetch().map(r->ConvertHelper.convert(r, FlowCaseDetail.class));

				if(records.size() >= count) {
					locator.setAnchor(records.get(records.size() - 1).getId());
				} else {
					locator.setAnchor(null);
				}

				return records;

			} else {
				return null;
			}
	}

    @Override
    public List<FlowCaseDetail> findAdminFlowCases(ListingLocator locator, int count, SearchFlowCaseCommand cmd, ListingQueryBuilderCallback callback) {
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhFlowCases.class));


    	if(locator.getAnchor() == null) {
    		locator.setAnchor(cmd.getPageAnchor());
    	}

    	FlowCaseSearchType searchType = FlowCaseSearchType.fromCode(cmd.getFlowCaseSearchType());
    	if(searchType.equals(FlowCaseSearchType.ADMIN)) { //enum equal

           Condition cond = buildSearchFlowCaseCmdCondition(locator,cmd);

            SelectQuery<Record> query = context.select(Tables.EH_FLOW_CASES.fields())
                    .from(Tables.EH_FLOW_CASES).join(Tables.EH_FLOWS)
                    .on(Tables.EH_FLOW_CASES.FLOW_MAIN_ID.eq(Tables.EH_FLOWS.FLOW_MAIN_ID)
                            .and(Tables.EH_FLOW_CASES.FLOW_VERSION.eq(Tables.EH_FLOWS.FLOW_VERSION)))
                    .where(cond).orderBy(Tables.EH_FLOW_CASES.ID.desc()).limit(count + 1).getQuery();

            if (callback != null) {
                callback.buildCondition(locator, query);
            }

            List<FlowCaseDetail> objs = query.fetchInto(FlowCaseDetail.class);

            if(objs.size() > count) {
                locator.setAnchor(objs.get(objs.size() - 1).getId());
                objs.remove(objs.size() - 1);
            } else {
                locator.setAnchor(null);
            }
            return objs;
    	} else {
    		return null;
    	}
    }
    
    @Override
    public boolean updateIfValid(Long flowCaseId, Timestamp last, Timestamp now) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowCases.class));
        
        int effect = context.update(Tables.EH_FLOW_CASES)
        .set(Tables.EH_FLOW_CASES.LAST_STEP_TIME, now)
        .where(Tables.EH_FLOW_CASES.LAST_STEP_TIME.eq(last).and(Tables.EH_FLOW_CASES.ID.eq(flowCaseId)))
        .execute();
        
        if(effect > 0) {
        	return true;
        }
        
        return false;
    }
    
    @Override
    public FlowCase findFlowCaseByReferId(Long referId, String referType, Long moduleId) {
    	ListingLocator locator = new ListingLocator();
    	
    	List<FlowCase> objs = this.queryFlowCases(locator, 1, new ListingQueryBuilderCallback() {

			@Override
			public SelectQuery<? extends Record> buildCondition(
					ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_FLOW_CASES.REFER_ID.eq(referId));
				query.addConditions(Tables.EH_FLOW_CASES.REFER_TYPE.eq(referType));
				query.addConditions(Tables.EH_FLOW_CASES.MODULE_ID.eq(moduleId));
				query.addConditions(Tables.EH_FLOW_CASES.PARENT_ID.eq(0L));
				return query;
			}
    		
    	});
    	
    	if(objs != null && objs.size() > 0) {
    		return objs.get(0);
    	}
    	
    	return null;
    }

    @Override
    public List<FlowServiceTypeDTO> listAdminServiceTypes(Integer namespaceId, SearchFlowCaseCommand cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhFlowCases.class));
        com.everhomes.server.schema.tables.EhFlowCases t = Tables.EH_FLOW_CASES;

        cmd.setUserId(null);
        Condition condition = buildSearchFlowCaseCmdCondition(new ListingLocator(), cmd);
        return context.select(t.fields())
                .from(t)
                .join(Tables.EH_FLOWS)
                .on(t.FLOW_MAIN_ID.eq(Tables.EH_FLOWS.FLOW_MAIN_ID)
                        .and(t.FLOW_VERSION.eq(Tables.EH_FLOWS.FLOW_VERSION)))
                .where(condition)
                .groupBy(t.MODULE_ID, t.SERVICE_TYPE)
                .fetch(record -> {
                    FlowServiceTypeDTO dto = new FlowServiceTypeDTO();
                    dto.setModuleId(record.getValue(t.MODULE_ID));
                    dto.setServiceName(record.getValue(t.SERVICE_TYPE));
                    dto.setNamespaceId(record.getValue(t.NAMESPACE_ID));
                    return dto;
                });
    }
}
