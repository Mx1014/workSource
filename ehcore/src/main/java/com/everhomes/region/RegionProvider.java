// @formatter:off
package com.everhomes.region;

import java.util.List;

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
    
    List<Region> listRegions(RegionScope scope, RegionAdminStatus status, Tuple<String, SortOrder>... orderBy);
    List<Region> listChildRegions(Long parentRegionId, RegionScope scope, RegionAdminStatus status, Tuple<String, SortOrder>... orderBy);
    List<Region> listDescendantRegions(Long parentRegionId, RegionScope scope, RegionAdminStatus status, Tuple<String, SortOrder>... orderBy);
}
