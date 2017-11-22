package com.everhomes.rest.point;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>modules: modules {@link com.everhomes.rest.point.PointModuleDTO}</li>
 * </ul>
 */
public class ListPointModulesResponse {

    @ItemType(PointModuleDTO.class)
    private List<PointModuleDTO> modules;
}
