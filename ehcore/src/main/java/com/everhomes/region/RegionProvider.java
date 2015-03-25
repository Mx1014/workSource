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
    
    List<Region> listRegion(RegionScope scope, RegionAdminStatus status, Tuple<String, SortOrder>... orderBy);
    List<Region> listChildRegion(Long parentRegionId, RegionScope scope, RegionAdminStatus status, Tuple<String, SortOrder>... orderBy);
    List<Region> listDescendantRegion(Long parentRegionId, RegionScope scope, RegionAdminStatus status, Tuple<String, SortOrder>... orderBy);
}
