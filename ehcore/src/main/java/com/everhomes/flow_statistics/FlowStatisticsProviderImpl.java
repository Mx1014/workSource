package com.everhomes.flow_statistics;

import com.everhomes.buttscript.ButtScriptConfig;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowNode;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.flow_statistics.FlowVersionCycleDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFlowEventLogsDao;
import com.everhomes.server.schema.tables.daos.EhFlowStatisticsHandleLogDao;
import com.everhomes.server.schema.tables.pojos.EhFlowEventLogs;
import com.everhomes.server.schema.tables.pojos.EhFlowNodes;
import com.everhomes.server.schema.tables.records.EhFlowEventLogsRecord;
import com.everhomes.server.schema.tables.records.EhFlowNodesRecord;
import com.everhomes.server.schema.tables.records.EhFlowStatisticsHandleLogRecord;
import org.apache.commons.collections.CollectionUtils;
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

    /**
     * 该版本的时间跨度为该版本的创建时间到下一版本的创建时间.(该方法目前废弃)
     * @param flowMainId
     * @param version
     * @return
     */
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
    @Override
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

    /**
     * 通过节点ｌｅｖｅｌ获取节点信息
     * @param flowMainId
     * @param version
     * @param flowNodeLevel
     * @return
     */
    @Override
    public FlowNode getFlowNodeByFlowLevel(Long flowMainId, Integer version, Integer flowNodeLevel) {

        List<FlowNode> list = new ArrayList<FlowNode>();

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowNodes.class));
        com.everhomes.server.schema.tables.EhFlowNodes t = Tables.EH_FLOW_NODES;
        SelectQuery<EhFlowNodesRecord> query = context.selectQuery(t);
        query.addConditions(t.FLOW_MAIN_ID.eq(flowMainId));
        query.addConditions(t.FLOW_VERSION.eq(version));
        query.addConditions(t.NODE_LEVEL.eq(flowNodeLevel));
        list = query.fetchInto(FlowNode.class);

        if(CollectionUtils.isNotEmpty(list)){
            return list.get(0);
        }
        return null ;
    }

    /**
     * 获取所有指定类型为step_tracker的记录
     * @return
     */
    @Override
    public List<FlowEventLog> getAllFlowEventLogs(Integer namespaceId){

        com.everhomes.server.schema.tables.EhFlowEventLogs t = Tables.EH_FLOW_EVENT_LOGS;
        List<FlowEventLog> list =   this.queryFlowEventLog(new ListingLocator(), 0, (locator1, query) -> {

            query.addConditions(t.LOG_TYPE.eq(FlowLogType.STEP_TRACKER.getCode()));
            if(namespaceId != null){
                query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
            }
            return query;

        },(locator2, query)->{ //排序
            query.addOrderBy(t.NAMESPACE_ID);
            query.addOrderBy(t.FLOW_MAIN_ID);
            query.addOrderBy(t.FLOW_VERSION);
            query.addOrderBy(t.ID);
            return query;
        });

        return list ;

    }

    /**
     * 查询记录信息
     * @param flowMainId
     * @param version
     * @param flowCases
     * @param startDate
     * @param flowNodeIds
     * @return
     */
    @Override
    public List<FlowEventLog> getFlowEventLogs(Long flowMainId ,Integer version ,List<Long>flowCases , Timestamp startDate ,List<Long> flowNodeIds){
        com.everhomes.server.schema.tables.EhFlowEventLogs t = Tables.EH_FLOW_EVENT_LOGS;
        List<FlowEventLog> list =   this.queryFlowEventLog(new ListingLocator(), 0, (locator1, query) -> {

            query.addConditions(t.LOG_TYPE.eq(FlowLogType.STEP_TRACKER.getCode()));
            if(flowMainId != null){
                query.addConditions(t.FLOW_MAIN_ID.eq(flowMainId));
            }
            if(version != null){
                query.addConditions(t.FLOW_VERSION.eq(version));
            }
            if(CollectionUtils.isNotEmpty(flowCases)){
                query.addConditions(t.FLOW_CASE_ID.in(flowCases));
            }
            if(startDate != null){
                query.addConditions(t.CREATE_TIME.ge(startDate));//大于等于
            }
            if(CollectionUtils.isNotEmpty(flowNodeIds)){
                query.addConditions(t.FLOW_NODE_ID.in(flowNodeIds));//等于
            }
            return query;

        },(locator2, query)->{ //排序
            query.addOrderBy(t.CREATE_TIME);
            return query;
        });

        return list ;
    }

    /**
     *
     * @param locator
     * @param count
     * @param callback 条件
     * @param orderCallback 排序
     * @return
     */
    @Override
    public List<FlowEventLog> queryFlowEventLog(ListingLocator locator, int count,
                                                ListingQueryBuilderCallback callback ,
                                                ListingQueryBuilderCallback orderCallback ) {
        com.everhomes.server.schema.tables.EhFlowEventLogs t = Tables.EH_FLOW_EVENT_LOGS;

        SelectQuery<EhFlowEventLogsRecord> query = context().selectQuery(t);
        if (callback != null) {
            callback.buildCondition(locator, query);
        }
        if (locator != null && locator.getAnchor() != null) {
            query.addConditions(t.ID.ge(locator.getAnchor()));
        }

        if (count > 0) {
            query.addLimit(count + 1);
        }
        if(orderCallback != null){
            orderCallback.buildCondition(null, query);
        }

        List<FlowEventLog> list = query.fetchInto(FlowEventLog.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

    @Override
    public List<FlowNode> getFlowNodeByLaneId(Long flowMainId, Integer version, Long laneId) {

        List<FlowNode> list = new ArrayList<FlowNode>();

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhFlowNodes.class));
        com.everhomes.server.schema.tables.EhFlowNodes t = Tables.EH_FLOW_NODES;
        SelectQuery<EhFlowNodesRecord> query = context.selectQuery(t);
        query.addConditions(t.FLOW_MAIN_ID.eq(flowMainId));
        query.addConditions(t.FLOW_VERSION.eq(version));
        query.addConditions(t.FLOW_LANE_ID.eq(laneId));
        list = query.fetchInto(FlowNode.class);

        return list ;
    }

    private EhFlowEventLogsDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhFlowEventLogsDao(context.configuration());
    }

    private EhFlowEventLogsDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhFlowEventLogsDao(context.configuration());
    }

    private DSLContext rwContext() {
        return dbProvider.getDslContext(AccessSpec.readWrite());
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
