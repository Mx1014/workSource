package com.everhomes.flow_statistics;

import com.everhomes.buttscript.ButtScriptConfig;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.FlowNode;
import com.everhomes.rest.flow_statistics.FlowVersionCycleDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhFlowEventLogs;
import com.everhomes.server.schema.tables.pojos.EhFlowNodes;
import com.everhomes.server.schema.tables.records.EhFlowNodesRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class FlowStatisticsProviderImpl implements FlowStatisticsProvider {

    @Autowired
    private DbProvider dbProvider;

    @Override
    public FlowVersionCycleDTO getFlowVersionCycle(Long flowMainId ,Integer version) {
        FlowVersionCycleDTO result = new FlowVersionCycleDTO();
        try {

            DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowEventLogs.class));

            Record record = context.select(DSL.max(Tables.EH_FLOW_EVENT_LOGS.CREATE_TIME),
                    DSL.min(Tables.EH_FLOW_EVENT_LOGS.CREATE_TIME))
                    .from(Tables.EH_FLOW_EVENT_LOGS)
                    .where(Tables.EH_FLOW_EVENT_LOGS.FLOW_MAIN_ID.eq(flowMainId))
                    .and(Tables.EH_FLOW_EVENT_LOGS.FLOW_VERSION.eq(version))
                    .fetchOne();

            if (record != null) {
                Timestamp maxTime = record.getValue(DSL.max(Tables.EH_FLOW_EVENT_LOGS.CREATE_TIME));
                Date maxDate = new Date(maxTime.getTime());
                Timestamp minTime = record.getValue(DSL.min(Tables.EH_FLOW_EVENT_LOGS.CREATE_TIME));
                Date minDate = new Date(minTime.getTime());
                result.setStartDate(minDate);
                result.setEndDate(maxDate);
                return result ;
            }

            return result;
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return result;
        }
    }

    /**
     * 通过flowMainId　，version　查询所有节点
     * @param flowMainId
     * @param version
     * @return
     */
    public List<FlowNode> getFlowNodes(Long flowMainId , Integer version){

        List<FlowNode> list = new ArrayList<FlowNode>();

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowNodes.class));
        com.everhomes.server.schema.tables.EhFlowNodes t = Tables.EH_FLOW_NODES;
        SelectQuery<EhFlowNodesRecord> query = context.selectQuery(t);
        query.addConditions(t.FLOW_MAIN_ID.eq(flowMainId));
        query.addConditions(t.FLOW_VERSION.eq(version));
        query.addOrderBy(t.NODE_LEVEL);
        list = query.fetchInto(FlowNode.class);

        return list ;
    }
}
