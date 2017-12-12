// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;
import java.util.Map;

/**
 * <ul>
 *     <li>items: 结果对象</li>
 * </ul>
 */
public class StatEventStatDTO {

    @ItemType(String.class)
    private List<Map<String, Object>> items;

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
