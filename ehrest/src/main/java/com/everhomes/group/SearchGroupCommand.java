// @formatter:off
package com.everhomes.group;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>familyId: 请求人所在的家庭ID</li>
 * <li>longitude: 请求人所有位置的经度</li> 
 * <li>latitude: 请求人所有位置的纬度</li> 
 * <li>searchCondition: 搜索条件</li> 
 * <li>pageAnchor: 分页的锚点，本次开始取的位置</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class SearchGroupCommand {
    private String queryString;
    
    private Long pageAnchor;
    
    private int pageSize;
    
    public SearchGroupCommand() {
    }
    
    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
    
}
