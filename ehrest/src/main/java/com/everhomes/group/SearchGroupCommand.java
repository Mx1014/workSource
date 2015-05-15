// @formatter:off
package com.everhomes.group;

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
    private Long familyId;
    private Double longitude;
    private Double latitude;
    
    private String queryString;
    
    @ItemType(String.class)
    private Map<String, String> includeTerms;
    
    @ItemType(String.class)
    private Map<String, String> excludeTerms;
    
    private Long pageOffset;
    private int pageSize;
    
    public SearchGroupCommand() {
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Long getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Long pageOffset) {
        this.pageOffset = pageOffset;
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

    public Map<String, String> getIncludeTerms() {
        return includeTerms;
    }

    public void setIncludeTerms(Map<String, String> includeTerms) {
        this.includeTerms = includeTerms;
    }

    public Map<String, String> getExcludeTerms() {
        return excludeTerms;
    }

    public void setExcludeTerms(Map<String, String> excludeTerms) {
        this.excludeTerms = excludeTerms;
    }
    
}
