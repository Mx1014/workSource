package com.everhomes.general_approval;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.server.schema.Tables;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhGeneralApprovalValsDao;
import com.everhomes.server.schema.tables.pojos.EhGeneralApprovalVals;
import com.everhomes.server.schema.tables.records.EhGeneralApprovalValsRecord;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class GeneralApprovalValProviderImpl implements GeneralApprovalValProvider {
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ShardingProvider shardingProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public Long createGeneralApprovalVal(GeneralApprovalVal obj) {
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhGeneralApprovalVals.class));
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralApprovalVals.class));
        obj.setId(id);
        prepareObj(obj);
        EhGeneralApprovalValsDao dao = new EhGeneralApprovalValsDao(context.configuration());
        dao.insert(obj);
        return id;
    }

    @Override
    public void updateGeneralApprovalVal(GeneralApprovalVal obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralApprovalVals.class));
        EhGeneralApprovalValsDao dao = new EhGeneralApprovalValsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public void deleteGeneralApprovalVal(GeneralApprovalVal obj) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralApprovalVals.class));
        EhGeneralApprovalValsDao dao = new EhGeneralApprovalValsDao(context.configuration());
        dao.deleteById(obj.getId());
    }

    @Override
    public GeneralApprovalVal getGeneralApprovalValById(Long id) {
        try {
        GeneralApprovalVal[] result = new GeneralApprovalVal[1];
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralApprovalVals.class));

        result[0] = context.select().from(Tables.EH_GENERAL_APPROVAL_VALS)
            .where(Tables.EH_GENERAL_APPROVAL_VALS.ID.eq(id))
            .fetchAny().map((r) -> {
                return ConvertHelper.convert(r, GeneralApprovalVal.class);
            });

        return result[0];
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return null;
        }
    }

    @Override
    public List<GeneralApprovalVal> queryGeneralApprovalVals(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralApprovalVals.class));

        SelectQuery<EhGeneralApprovalValsRecord> query = context.selectQuery(Tables.EH_GENERAL_APPROVAL_VALS);
        if(queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if(locator!=null && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_GENERAL_APPROVAL_VALS.ID.gt(locator.getAnchor()));
            }

        query.addLimit(count);
        List<GeneralApprovalVal> objs = query.fetch().map((r) -> {
            return ConvertHelper.convert(r, GeneralApprovalVal.class);
        });

        if(objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(GeneralApprovalVal obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
    }

	@Override
	public List<GeneralApprovalVal> queryGeneralApprovalValsByFlowCaseId(Long id) {
		return queryGeneralApprovalVals(new ListingLocator(),
				Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {
					@Override
					public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
							SelectQuery<? extends Record> query) {
						query.addConditions(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_CASE_ID.eq(id));  
						return query;
					}
				});
	}
	
	@Override
	public GeneralApprovalVal getGeneralApprovalByFlowCaseAndName(Long id, String fieldName){
		try {
	        GeneralApprovalVal[] result = new GeneralApprovalVal[1];
	        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralApprovalVals.class));

	        result[0] = context.select().from(Tables.EH_GENERAL_APPROVAL_VALS)
	            .where(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_CASE_ID.eq(id)).and(Tables.EH_GENERAL_APPROVAL_VALS.FIELD_NAME.eq(fieldName))
	            .fetchAny().map((r) -> {
	                return ConvertHelper.convert(r, GeneralApprovalVal.class);
	            });

	        return result[0];
	        } catch (Exception ex) {
	            //fetchAny() maybe return null
	            return null;
	        }
		
	}
}
