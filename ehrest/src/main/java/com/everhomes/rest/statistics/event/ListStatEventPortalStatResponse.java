package com.everhomes.rest.statistics.event;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>statList: statList {@link com.everhomes.rest.statistics.event.StatEventPortalStatDTO}</li>
 * </ul>
 */
public class ListStatEventPortalStatResponse {

    @ItemType(StatEventPortalStatDTO.class)
    private List<StatEventPortalStatDTO> statList;

    public List<StatEventPortalStatDTO> getStatList() {
        return statList;
    }

    public void setStatList(List<StatEventPortalStatDTO> statList) {
        this.statList = statList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
