// @formatter:off
package com.everhomes.search;

import com.everhomes.rest.visitorsys.BaseVisitorDTO;
import com.everhomes.rest.visitorsys.ListBookedVisitorsCommand;
import com.everhomes.rest.visitorsys.ListBookedVisitorsResponse;
import com.everhomes.visitorsys.ListBookedVisitorParams;

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
     * 同步预约/访客至es
     * @param baseVisitorDTO
     */
    void feedDoc(BaseVisitorDTO baseVisitorDTO);

    /**
     *  搜索预约
     * @param cmd
     * @return
     */
    ListBookedVisitorsResponse searchVisitors(ListBookedVisitorParams params);

    /**
     * 重新同步es预约
     */
    void syncVisitorsFromDb(Integer namespaceId);
}
