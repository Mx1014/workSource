// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>stat: 结果{@link com.everhomes.rest.statistics.event.StatKeyValueListDTO}</li>
 * </ul>
 */
public class StatEventStatDTO {

    @ItemType(StatKeyValueListDTO.class)
    private List<StatKeyValueListDTO> stat;

    public List<StatKeyValueListDTO> getStat() {
        return stat;
    }

    public void setStat(List<StatKeyValueListDTO> stat) {
        this.stat = stat;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
