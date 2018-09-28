// @formatter:off
package com.everhomes.rest.personal_center;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>versionList: 版本列表</li>
 * </ul>
 */
public class ListVersionListResponse {

    @ItemType(Integer.class)
    private List<Integer> versionList;

    public List<Integer> getVersionList() {
        return versionList;
    }

    public void setVersionList(List<Integer> versionList) {
        this.versionList = versionList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
