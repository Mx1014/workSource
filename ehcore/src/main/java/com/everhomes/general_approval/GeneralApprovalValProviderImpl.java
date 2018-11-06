package com.everhomes.general_approval;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhGeneralApprovalValsDao;
import com.everhomes.server.schema.tables.pojos.EhGeneralApprovalVals;
import com.everhomes.server.schema.tables.records.EhGeneralApprovalValsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class GeneralApprovalValProviderImpl implements GeneralApprovalValProvider {
    @Autowired
    private DbProvider dbProvider;

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
        obj.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        obj.setOperatorUid(UserContext.currentUserId());
        EhGeneralApprovalValsDao dao = new EhGeneralApprovalValsDao(context.configuration());
        dao.update(obj);
    }

    @Override
    public GeneralApprovalVal getSpecificApprovalValByFlowCaseId(Long flowCaseId, String fieldType) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhGeneralApprovalValsRecord> query = context.selectQuery(Tables.EH_GENERAL_APPROVAL_VALS);
        query.addConditions(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_CASE_ID.eq(flowCaseId));
        query.addConditions(Tables.EH_GENERAL_APPROVAL_VALS.FIELD_TYPE.eq(fieldType));
        return query.fetchAnyInto(GeneralApprovalVal.class);
    }

    @Override
    public List<GeneralApprovalVal> queryGeneralApprovalVals(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectQuery<EhGeneralApprovalValsRecord> query = context.selectQuery(Tables.EH_GENERAL_APPROVAL_VALS);
        if (queryBuilderCallback != null)
            queryBuilderCallback.buildCondition(locator, query);

        if (locator != null && locator.getAnchor() != null) {
            query.addConditions(Tables.EH_GENERAL_APPROVAL_VALS.ID.gt(locator.getAnchor()));
        }

        query.addLimit(count);
        List<GeneralApprovalVal> objs = query.fetch().map((r) -> ConvertHelper.convert(r, GeneralApprovalVal.class));

        if (objs.size() >= count) {
            locator.setAnchor(objs.get(objs.size() - 1).getId());
        } else {
            locator.setAnchor(null);
        }

        return objs;
    }

    private void prepareObj(GeneralApprovalVal obj) {
        Long l2 = DateHelper.currentGMTTime().getTime();
        obj.setCreateTime(new Timestamp(l2));
        obj.setCreatorUid(UserContext.currentUserId());
    }

    @Override
    public List<GeneralApprovalVal> queryGeneralApprovalValsByFlowCaseId(Long id) {
        return queryGeneralApprovalVals(new ListingLocator(),
                Integer.MAX_VALUE - 1, (locator, query) -> {
                    query.addConditions(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_CASE_ID.eq(id));
                    query.addConditions(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_NODE_ID.isNull().or(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_NODE_ID.eq(0L)));
                    return query;
                });
    }

    @Override
    public GeneralApprovalVal getGeneralApprovalByFlowCaseAndFeildType(Long id, String feildType){
        try {
            GeneralApprovalVal[] result = new GeneralApprovalVal[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralApprovalVals.class));

            result[0] = context.select().from(Tables.EH_GENERAL_APPROVAL_VALS)
                    .where(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_CASE_ID.eq(id))
                    .and(Tables.EH_GENERAL_APPROVAL_VALS.FIELD_TYPE.eq(feildType))
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
	public GeneralApprovalVal getGeneralApprovalByFlowCaseAndName(Long flowCaseId, String fieldName) {
		try {
	        GeneralApprovalVal[] result = new GeneralApprovalVal[1];
	        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhGeneralApprovalVals.class));

	        result[0] = context.select().from(Tables.EH_GENERAL_APPROVAL_VALS)
	            .where(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_CASE_ID.eq(flowCaseId))
                    .and(Tables.EH_GENERAL_APPROVAL_VALS.FIELD_NAME.eq(fieldName))
	            .fetchAny().map((r) -> ConvertHelper.convert(r, GeneralApprovalVal.class));

	        return result[0];
	        } catch (Exception ex) {
	            //fetchAny() maybe return null
	            return null;
	        }

	}

    @Override
    public GeneralApprovalVal getGeneralApprovalVal(Long flowCaseId, Long formOriginId, Long formVersion, String fieldName) {
        try {
            GeneralApprovalVal[] result = new GeneralApprovalVal[1];
            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

            result[0] = context.select().from(Tables.EH_GENERAL_APPROVAL_VALS)
                    .where(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_CASE_ID.eq(flowCaseId))
                    .and(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_NODE_ID.isNull().or(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_NODE_ID.eq(0L)))
                    .and(Tables.EH_GENERAL_APPROVAL_VALS.FORM_ORIGIN_ID.eq(formOriginId))
                    // .and(Tables.EH_GENERAL_APPROVAL_VALS.FORM_VERSION.eq(formVersion))
                    .and(Tables.EH_GENERAL_APPROVAL_VALS.FIELD_NAME.eq(fieldName))
                    .fetchAny().map((r) -> ConvertHelper.convert(r, GeneralApprovalVal.class));

            return result[0];
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<GeneralApprovalVal> getGeneralApprovalVal(Long formOriginId, Long formVersion, Long flowCaseId, Long flowNodeId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());

        SelectConditionStep<Record> query = context.select().from(Tables.EH_GENERAL_APPROVAL_VALS)
                .where(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_CASE_ID.eq(flowCaseId))
                .and(Tables.EH_GENERAL_APPROVAL_VALS.FORM_ORIGIN_ID.eq(formOriginId));
        if (formVersion != null) {
            query.and(Tables.EH_GENERAL_APPROVAL_VALS.FORM_VERSION.eq(formVersion));
        }
        if (flowNodeId == null) {
            query.and(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_NODE_ID.isNull().or(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_NODE_ID.eq(0L)));
        } else {
            query.and(Tables.EH_GENERAL_APPROVAL_VALS.FLOW_NODE_ID.eq(flowNodeId));
        }

        Result<Record> result = query.fetch();
        if (result == null || result.size() == 0) {
            return new ArrayList<>();
        }
        return result.map(record -> {
            return ConvertHelper.convert(record, GeneralApprovalVal.class);
        });
    }

    @Override
    public GeneralApprovalVal getGeneralApprovalById(Long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhGeneralApprovalValsDao dao = new EhGeneralApprovalValsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), GeneralApprovalVal.class);
    }
}
