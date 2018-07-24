package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>colours: 颜色列表</li>
 * </ul>
 */
public class GetRemindCategoryColorsResponse {
    private List<String> colours;

    public List<String> getColours() {
        return colours;
    }

    public void setColours(List<String> colours) {
        this.colours = colours;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
