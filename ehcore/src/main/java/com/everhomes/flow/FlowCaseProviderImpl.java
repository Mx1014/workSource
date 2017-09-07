package com.everhomes.flow;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.flow.FlowCaseSearchType;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.SearchFlowCaseCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFlowCasesDao;
import com.everhomes.server.schema.tables.pojos.EhFlowCases;
import com.everhomes.server.schema.tables.records.EhFlowCasesRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FlowCaseProviderImpl implements FlowCaseProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

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

    @Override
    public Long createFlowCaseHasId(FlowCase obj) {
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowCases.class));
        
        prepareObj(obj);
        EhFlowCasesDao dao = new EhFlowCasesDao(context.configuration());
        dao.insert(obj);
        return obj.getId();
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
    public List<FlowCaseDetail> findApplierFlowCases(ListingLocator locator, int count, SearchFlowCaseCommand cmd) {
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhFlowCases.class));
    	Condition cond = Tables.EH_FLOW_CASES.STATUS.ne(FlowCaseStatus.INVALID.getCode())
    			.and(Tables.EH_FLOW_CASES.NAMESPACE_ID.eq(cmd.getNamespaceId()));
    	
    	if(locator.getAnchor() == null) {
    		locator.setAnchor(cmd.getPageAnchor());
    	}
    	
    	FlowCaseSearchType searchType = FlowCaseSearchType.fromCode(cmd.getFlowCaseSearchType());
    	if(searchType.equals(FlowCaseSearchType.APPLIER)) {
    		cond = cond.and(Tables.EH_FLOW_CASES.APPLY_USER_ID.eq(cmd.getUserId()));
    		
    	    if(locator.getAnchor() != null) {
    	    	cond = cond.and(Tables.EH_FLOW_CASES.ID.lt(locator.getAnchor()));
            }
    	    
        	if(cmd.getModuleId() != null) {
        		cond = cond.and(Tables.EH_FLOW_CASES.MODULE_ID.eq(cmd.getModuleId()));
        	}
        	if(cmd.getOrganizationId() != null) {
        		cond = cond.and(Tables.EH_FLOW_CASES.ORGANIZATION_ID.eq(cmd.getOrganizationId()));
        	}
        	if(cmd.getOwnerId() != null) {
        		cond = cond.and(Tables.EH_FLOW_CASES.OWNER_ID.eq(cmd.getOwnerId()));
        	}
        	if(cmd.getOwnerType() != null) {
        		cond = cond.and(Tables.EH_FLOW_CASES.OWNER_TYPE.eq(cmd.getOwnerType()));
        	}
        	if(cmd.getFlowCaseStatus() != null) {
        		cond = cond.and(Tables.EH_FLOW_CASES.STATUS.eq(cmd.getFlowCaseStatus()));
        	}
        	if(cmd.getKeyword() != null && !cmd.getKeyword().isEmpty()) {
        		cond = cond.and(
        				Tables.EH_FLOW_CASES.MODULE_NAME.like(cmd.getKeyword() + "%")
        				.or(Tables.EH_FLOW_CASES.APPLIER_NAME.like(cmd.getKeyword() + "%"))
        				.or(Tables.EH_FLOW_CASES.APPLIER_PHONE.like(cmd.getKeyword() + "%"))
        				);
        	}
    		
    		List<EhFlowCasesRecord> records = context.select().from(Tables.EH_FLOW_CASES).leftOuterJoin(Tables.EH_FLOWS)
    		    	.on(Tables.EH_FLOW_CASES.FLOW_MAIN_ID.eq(Tables.EH_FLOWS.FLOW_MAIN_ID).and(Tables.EH_FLOW_CASES.FLOW_VERSION.eq(Tables.EH_FLOWS.FLOW_VERSION)))
    		    	.where(cond).orderBy(Tables.EH_FLOW_CASES.ID.desc())
    		    	.limit(count).fetch().map(new FlowCaseRecordMapper());
    		
    		List<FlowCaseDetail> objs = records.stream().map((r) -> {
    			return ConvertHelper.convert(r, FlowCaseDetail.class);
    		}).collect(Collectors.toList());
    		
            if(objs.size() >= count) {
                locator.setAnchor(objs.get(objs.size() - 1).getId());
            } else {
                locator.setAnchor(null);
            }
            
            return objs;
    		
    	} else {
    		return null;
    	}   	
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
    public List<FlowCaseDetail> findAdminFlowCases(ListingLocator locator, int count, SearchFlowCaseCommand cmd) {
    	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhFlowCases.class));
    	Condition cond = Tables.EH_FLOW_CASES.STATUS.ne(FlowCaseStatus.INVALID.getCode())
    			.and(Tables.EH_FLOW_CASES.NAMESPACE_ID.eq(cmd.getNamespaceId()));
    	
    	if(locator.getAnchor() == null) {
    		locator.setAnchor(cmd.getPageAnchor());
    	}
    	
    	FlowCaseSearchType searchType = FlowCaseSearchType.fromCode(cmd.getFlowCaseSearchType());
    	if(searchType.equals(FlowCaseSearchType.ADMIN)) { //enum equal
    		if(cmd.getUserId() != null) {
    			cond = cond.and(Tables.EH_FLOW_CASES.APPLY_USER_ID.eq(cmd.getUserId()));	
    		}
    		
    		if(cmd.getProjectId() != null) {
    			cond = cond.and(Tables.EH_FLOW_CASES.PROJECT_ID.eq(cmd.getProjectId()));
    		}
    		if(cmd.getProjectType() != null) {
    			cond = cond.and(Tables.EH_FLOW_CASES.PROJECT_TYPE.eq(cmd.getProjectType()));
    		}
    		if(cmd.getStartTime() != null && cmd.getEndTime() == null) {
    			cond = cond.and(Tables.EH_FLOW_CASES.CREATE_TIME.ge(new Timestamp(cmd.getStartTime())));
    		} else if(cmd.getEndTime() != null && cmd.getStartTime() == null) {
    			cond = cond.and(Tables.EH_FLOW_CASES.CREATE_TIME.le(new Timestamp(cmd.getEndTime())));
    		} else if(cmd.getEndTime() != null && cmd.getStartTime() != null) {
    			cond = cond.and(Tables.EH_FLOW_CASES.CREATE_TIME.between(new Timestamp(cmd.getStartTime()), new Timestamp(cmd.getEndTime())));
    		}
    		
    	    if(locator.getAnchor() != null) {
    	    	cond = cond.and(Tables.EH_FLOW_CASES.ID.lt(locator.getAnchor()));
            }
        	if(cmd.getModuleId() != null) {
        		cond = cond.and(Tables.EH_FLOW_CASES.MODULE_ID.eq(cmd.getModuleId()));
        	}
            if(cmd.getOrganizationId() != null) {
                cond = cond.and(Tables.EH_FLOW_CASES.ORGANIZATION_ID.eq(cmd.getOrganizationId()));
            }
        	if(cmd.getOwnerId() != null) {
        		cond = cond.and(Tables.EH_FLOW_CASES.OWNER_ID.eq(cmd.getOwnerId()));
        	}
        	if(cmd.getOwnerType() != null) {
        		cond = cond.and(Tables.EH_FLOW_CASES.OWNER_TYPE.eq(cmd.getOwnerType()));
        	}
        	if(cmd.getFlowCaseStatus() != null) {
        		cond = cond.and(Tables.EH_FLOW_CASES.STATUS.eq(cmd.getFlowCaseStatus()));
        	}
        	if(cmd.getKeyword() != null && !cmd.getKeyword().isEmpty()) {
        		cond = cond.and(
        				Tables.EH_FLOW_CASES.MODULE_NAME.like(cmd.getKeyword() + "%")
        				.or(Tables.EH_FLOW_CASES.APPLIER_NAME.like(cmd.getKeyword() + "%"))
        				.or(Tables.EH_FLOW_CASES.APPLIER_PHONE.like(cmd.getKeyword() + "%"))
        				);
        	}
    		
    		List<EhFlowCasesRecord> records = context.select().from(Tables.EH_FLOW_CASES).join(Tables.EH_FLOWS)
    		    	.on(Tables.EH_FLOW_CASES.FLOW_MAIN_ID.eq(Tables.EH_FLOWS.FLOW_MAIN_ID).and(Tables.EH_FLOW_CASES.FLOW_VERSION.eq(Tables.EH_FLOWS.FLOW_VERSION)))
    		    	.where(cond).orderBy(Tables.EH_FLOW_CASES.ID.desc()).limit(count)
    		    	.fetch().map(new FlowCaseRecordMapper());
    		
    		List<FlowCaseDetail> objs = records.stream().map((r) -> {
    			return ConvertHelper.convert(r, FlowCaseDetail.class);
    		}).collect(Collectors.toList());
    		
            if(objs.size() >= count) {
                locator.setAnchor(objs.get(objs.size() - 1).getId());
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
				return query;
			}
    		
    	});
    	
    	if(objs != null && objs.size() > 0) {
    		return objs.get(0);
    	}
    	
    	return null;
    }
}
