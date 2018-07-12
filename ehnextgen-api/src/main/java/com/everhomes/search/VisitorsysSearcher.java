// @formatter:off
package com.everhomes.search;

import com.everhomes.rest.visitorsys.*;
import com.everhomes.visitorsys.ListBookedVisitorParams;
import com.everhomes.visitorsys.VisitorSysVisitor;

import java.util.List;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/4 17:12
 * 访客预约/管理搜索引擎
 */
public interface VisitorsysSearcher {
    /**
     * 删除es父预约id和id对应的子预约
     * @param visitorId
     */
    void deleteById(Long visitorId);

    /**
     *  搜索预约
     * @param params
     * @return
     */
    ListBookedVisitorsResponse searchVisitors(ListBookedVisitorParams params);

    /**
     * 重新同步es预约
     */
    void syncVisitorsFromDb(Integer namespaceId);

    /**
     * 同步到es
     * @param visitorId
     */
    void syncVisitor(Integer namespaceId,Long visitorId);

    /**
     * 同步到es
     * @param visitor
     */
    void syncVisitor(VisitorSysVisitor visitor);

    /**
     * 统计域空间下项目的预约和访客数量
     * @param cmd
     * @return
     */

    GetStatisticsResponse getStatistics(GetStatisticsCommand cmd);

}
