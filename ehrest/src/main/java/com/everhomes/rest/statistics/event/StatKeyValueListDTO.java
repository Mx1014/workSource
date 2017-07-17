// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>list: list {@link com.everhomes.rest.statistics.event.StatKeyValueDTO}</li>
 * </ul>
 */
public class StatKeyValueListDTO {

    @ItemType(StatKeyValueDTO.class)
    private List<StatKeyValueDTO> list;

    public List<StatKeyValueDTO> getList() {
        return list;
    }

    public void setList(List<StatKeyValueDTO> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
