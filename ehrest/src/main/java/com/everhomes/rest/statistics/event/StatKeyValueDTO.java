// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>key: key</li>
 *     <li>value: value</li>
 * </ul>
 */
public class StatKeyValueDTO {

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
