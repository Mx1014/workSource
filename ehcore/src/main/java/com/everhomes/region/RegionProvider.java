// @formatter:off
package com.everhomes.region;

import java.util.List;

public interface RegionProvider {
    List<Region> listRegionByScope(RegionScope scope);
    List<Region> listChildRegionOfScope(long parentRegionId, RegionScope scope);
    List<Region> listDescendantRegionOfScope(long parentRegionId, RegionScope scope);
}
