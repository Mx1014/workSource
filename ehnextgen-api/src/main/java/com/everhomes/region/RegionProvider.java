// @formatter:off
package com.everhomes.region;

import java.util.List;

import com.everhomes.rest.region.RegionAdminStatus;
import com.everhomes.rest.region.RegionScope;
import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

/**
 * Region management
 * 
 * @author Kelven Yang
 *
 */
@SuppressWarnings("unchecked")
public interface RegionProvider {
    void createRegion(Region region);
    void updateRegion(Region region);
    void deleteRegion(Region region);
    void deleteRegionById(long regionId);
    Region findRegionById(long regionId);
    
    List<Region> listRegions(Integer namespaceId, RegionScope scope, RegionAdminStatus status, Tuple<String, SortOrder>... orderBy);
    List<Region> listChildRegions(Integer namespaceId, Long parentRegionId, RegionScope scope, RegionAdminStatus status, Tuple<String, SortOrder>... orderBy);
    List<Region> listDescendantRegions(Integer namespaceId, Long parentRegionId, RegionScope scope, RegionAdminStatus status, Tuple<String, SortOrder>... orderBy);
    List<Region> listRegionByKeyword(Long parentRegionId, RegionScope scope, RegionAdminStatus status, 
            Tuple<String, SortOrder> orderBy, String keyword, int namespaceId);
    List<Region> listActiveRegion(RegionScope scope);
	List<Region> listRegionByName(Long parentRegionId, RegionScope scope,RegionAdminStatus status, Tuple<String, SortOrder> orderBy,String keyword);
	Region findRegionByPath(String path);
	Region findRegionByPath(Integer namespaceId, String path);
	List<Region> listRegionByParentId(Integer namespaceId, Long parentId, RegionScope scope, RegionAdminStatus status);
    void createRegionCode(RegionCodes regionCode);
    void updateRegionCode(RegionCodes regionCode);
    List<RegionCodes> listRegionCodes(String name, Integer code);
    RegionCodes findRegionCodeById(Long id);
}
