package com.everhomes.flow_statistics;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import org.jooq.Field;
import org.jooq.TableField;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface FlowStatisticsHandleLogProvider {

    List<FlowStatisticsHandleLog> findFlowStatisticsHandleLogByParams(FlowStatisticsHandleLog params , List<Field> orders);

    Long create(FlowStatisticsHandleLog bo);

    void update(FlowStatisticsHandleLog bo);

    void delete(FlowStatisticsHandleLog bo);

    void deleteAll(Integer namespaceId);

    List<FlowStatisticsHandleLog> query(ListingLocator locator, int count,
                                        ListingQueryBuilderCallback callback ,
                                        ListingQueryBuilderCallback orderCallback );

    Integer countNodesTimes(Long flowMainId , Integer version , Timestamp startTime , Timestamp endTime , Long nodeId);

    Long countNodesCycle(Long flowMainId , Integer version ,Timestamp startTime , Timestamp endTime , Long nodeId) ;

    Long countLanesCycle(Long flowMainId , Integer version ,Timestamp startTime , Timestamp endTime, Long laneId) ;

    Integer countLanesTimes(Long flowMainId , Integer version ,Timestamp startTime , Timestamp endTime , Long laneId, Long nodeId) ;

    List<FlowStatisticsHandleLog> getStatisticsHandleLogByEventLogId(Long eventLogId) ;

    Integer countLanes(Long flowMainId , Integer version ,Timestamp startTime , Timestamp endTime);
}
