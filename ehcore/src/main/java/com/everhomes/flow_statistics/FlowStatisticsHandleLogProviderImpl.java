package com.everhomes.flow_statistics;

import com.everhomes.buttscript.ButtScriptPublishInfo;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.flow_statistics.FlowVersionCycleDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhFlowStatisticsHandleLogDao;
import com.everhomes.server.schema.tables.pojos.EhFlowEventLogs;
import com.everhomes.server.schema.tables.pojos.EhFlowStatisticsHandleLog;
import com.everhomes.server.schema.tables.records.EhFlowStatisticsHandleLogRecord;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Component
public class FlowStatisticsHandleLogProviderImpl implements  FlowStatisticsHandleLogProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowStatisticsHandleLogProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public List<FlowStatisticsHandleLog> findFlowStatisticsHandleLogByParams(FlowStatisticsHandleLog params ,List<Field> orders) {
        com.everhomes.server.schema.tables.EhFlowStatisticsHandleLog t = Tables.EH_FLOW_STATISTICS_HANDLE_LOG;
        List<FlowStatisticsHandleLog> list =   this.query(new ListingLocator(), 0, (locator1, query) -> {

            if(params == null){
                return query;
            }
            if(params.getFlowMainId() !=null){
                Long flowMainId = params.getFlowMainId() ;
                query.addConditions(t.FLOW_MAIN_ID.eq(flowMainId));
            }
            if(params.getFlowVersion()!=null){
                Integer flowVersion = params.getFlowVersion();
                query.addConditions(t.FLOW_VERSION.eq(flowVersion));
            }
            if(params.getFlowNodeId()!=null){
                Long flowNodeId = params.getFlowNodeId();
                query.addConditions(t.FLOW_NODE_ID.eq(flowNodeId));
            }
            if(params.getFlowLanesId()!=null){
                Long flowLanesId = params.getFlowLanesId();
                query.addConditions(t.FLOW_LANES_ID.eq(flowLanesId));
            }
            if(params.getStartTime()!=null){
                query.addConditions(t.START_TIME.ge(params.getStartTime()));//大于等于
            }
            if(params.getEndTime()!=null){
                query.addConditions(t.END_TIME.le(params.getEndTime()));//小于等于
            }
            return query;

        },(locator2, query)->{ //排序
            if(orders != null && orders.size()>0){
                orders.stream().forEach(r->{
                    query.addOrderBy(r);
                });
            }
            return query;
        });

        return list ;
    }

    /**
     * 通过原（处理前的）记录ＩＤ查询
     * @param eventLogId
     * @return
     */
    @Override
    public List<FlowStatisticsHandleLog> getStatisticsHandleLogByEventLogId(Long eventLogId){

        com.everhomes.server.schema.tables.EhFlowStatisticsHandleLog t = Tables.EH_FLOW_STATISTICS_HANDLE_LOG;
        List<FlowStatisticsHandleLog> list =   this.query(new ListingLocator(), 0, (locator1, query) -> {

            if(eventLogId!=null){
                query.addConditions(t.LOG_ID.eq(eventLogId));
            }
            return query;
        },(locator2, query)->{ //排序
            return query;
        });

        return list ;
    }

    /**
     * 节点处理次数
     * @param flowMainId
     * @param version
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Integer countNodesTimes(Long flowMainId , Integer version ,Timestamp startTime , Timestamp endTime , Long nodeId) {
        Integer intCount = 0 ;
        try {

            SelectConditionStep step = context().selectCount()
                    .from(Tables.EH_FLOW_STATISTICS_HANDLE_LOG)
                    .where(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.FLOW_MAIN_ID.eq(flowMainId))
                    .and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.FLOW_VERSION.eq(version));

            if(nodeId != null){
                step.and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.FLOW_NODE_ID.eq(nodeId));
            }
            if(startTime != null){
                step.and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.START_TIME.ge(startTime));
            }
            if(endTime != null){
                step.and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.END_TIME.le(endTime));
            }
            Object count = step.fetchOneInto(Integer.class);
            intCount = count==null?0:(Integer)count;

            return intCount;
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return intCount;
        }
    }

    /**
     * 节点处理时长
     * @param flowMainId
     * @param version
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Long countNodesCycle(Long flowMainId , Integer version ,Timestamp startTime , Timestamp endTime, Long nodeId){
        Long longCount = 0L ;
        try {
            SelectConditionStep step = context().select(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.CYCLE.sum())
                    .from(Tables.EH_FLOW_STATISTICS_HANDLE_LOG)
                    .where(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.FLOW_MAIN_ID.eq(flowMainId))
                    .and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.FLOW_VERSION.eq(version));

            if(nodeId != null){
                step.and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.FLOW_NODE_ID.eq(nodeId));
            }
            if(startTime != null){
                step.and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.START_TIME.ge(startTime));
            }
            if(endTime != null){
                step.and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.END_TIME.le(endTime));
            }
            Object count = step.fetchOneInto(Long.class);
            longCount = count==null?0:(Long)count;

            return longCount;
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return longCount;
        }
    }

    /**
     * 泳道处理次数
     * @param flowMainId
     * @param version
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Integer countLanesTimes(Long flowMainId , Integer version ,Timestamp startTime , Timestamp endTime , Long laneId , Long nodeId) {
        Integer intCount = 0 ;
        try {

            SelectConditionStep step = context().selectCount()
                    .from(Tables.EH_FLOW_STATISTICS_HANDLE_LOG)
                    .where(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.FLOW_MAIN_ID.eq(flowMainId))
                    .and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.FLOW_VERSION.eq(version));

            if(laneId != null){
                step.and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.FLOW_LANES_ID.eq(laneId));
            }
            if(startTime != null){
                step.and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.START_TIME.ge(startTime));
            }
            if(endTime != null){
                step.and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.END_TIME.le(endTime));
            }
            Object count = step.fetchOneInto(Integer.class);
            intCount = count==null?0:(Integer)count;

            return intCount;
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return intCount;
        }
    }

    /**
     * 泳道处理时长
     * @param flowMainId
     * @param version
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Long countLanesCycle(Long flowMainId , Integer version ,Timestamp startTime , Timestamp endTime, Long laneId){
        Long longCount = 0L ;
        try {
            SelectConditionStep step = context().select(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.CYCLE.sum())
                    .from(Tables.EH_FLOW_STATISTICS_HANDLE_LOG)
                    .where(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.FLOW_MAIN_ID.eq(flowMainId))
                    .and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.FLOW_VERSION.eq(version));

            if(laneId != null){
                step.and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.FLOW_LANES_ID.eq(laneId));
            }
            if(startTime != null){
                step.and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.START_TIME.ge(startTime));
            }
            if(endTime != null){
                step.and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.END_TIME.le(endTime));
            }
            Object count = step.fetchOneInto(Long.class);
            longCount = count==null?0:(Long)count;

            return longCount;
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return longCount;
        }
    }

    /**
     * 统计泳道个数
     * @param flowMainId
     * @param version
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Integer countLanes(Long flowMainId, Integer version, Timestamp startTime, Timestamp endTime) {
        Integer intCount = 0 ;
        try {
            SelectConditionStep step = context().select(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.FLOW_LANES_ID)
                    .from(Tables.EH_FLOW_STATISTICS_HANDLE_LOG)
                    .where(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.FLOW_MAIN_ID.eq(flowMainId))
                    .and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.FLOW_VERSION.eq(version));

            if(startTime != null){
                step.and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.START_TIME.ge(startTime));
            }
            if(endTime != null){
                step.and(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.END_TIME.le(endTime));
            }

            step.groupBy(Tables.EH_FLOW_STATISTICS_HANDLE_LOG.FLOW_LANES_ID);
            List<Long> count = step.fetchInto(Long.class);
            if(CollectionUtils.isNotEmpty(count)){
                intCount =  count.size();
            }
            return intCount;
        } catch (Exception ex) {
            //fetchAny() maybe return null
            return intCount;
        }
    }

    @Override
    public Long create(FlowStatisticsHandleLog bo) {
        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhFlowStatisticsHandleLog.class));
        bo.setId(id);
        rwDao().insert(bo);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhFlowStatisticsHandleLog.class, id);
        return id;
    }

    @Override
    public void update(FlowStatisticsHandleLog bo) {

        rwDao().update(bo);
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhFlowStatisticsHandleLog.class, bo.getId());
    }

    @Override
    public void delete(FlowStatisticsHandleLog bo) {

        com.everhomes.server.schema.tables.EhFlowStatisticsHandleLog t = Tables.EH_FLOW_STATISTICS_HANDLE_LOG;

        rwContext().delete(t)
                .where(t.ID.eq(bo.getId()))
                .execute();
    }
    @Override
    public void deleteAll(Integer namespaceId){
        com.everhomes.server.schema.tables.EhFlowStatisticsHandleLog t = Tables.EH_FLOW_STATISTICS_HANDLE_LOG;
        if(namespaceId != null){
            rwContext()
                    .delete(t)
                    .where(t.NAMESPACE_ID.eq(namespaceId))
                    .execute();
        }else{
            rwContext()
                    .delete(t)
                    .execute();
        }

    }

    /**
     * 查询底层方法
     * @param locator
     * @param count
     * @param callback 条件
     * @param orderCallback　排序
     * @return
     */
    @Override
    public List<FlowStatisticsHandleLog> query(ListingLocator locator, int count,
                                               ListingQueryBuilderCallback callback ,
                                               ListingQueryBuilderCallback orderCallback ) {
        com.everhomes.server.schema.tables.EhFlowStatisticsHandleLog t = Tables.EH_FLOW_STATISTICS_HANDLE_LOG;

        SelectQuery<EhFlowStatisticsHandleLogRecord> query = context().selectQuery(t);
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

        List<FlowStatisticsHandleLog> list = query.fetchInto(FlowStatisticsHandleLog.class);
        if (list.size() > count && count > 0) {
            locator.setAnchor(list.get(list.size() - 1).getId());
            list.remove(list.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        return list;
    }

    private EhFlowStatisticsHandleLogDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhFlowStatisticsHandleLogDao(context.configuration());
    }

    private EhFlowStatisticsHandleLogDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhFlowStatisticsHandleLogDao(context.configuration());
    }

    private DSLContext rwContext() {
        return dbProvider.getDslContext(AccessSpec.readWrite());
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }
}
