// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *     <li>listCardJson: listCardJson</li>
 * </ul>
 */
public class OPPushCards {

    private List<String> listCardJson;

    public List<String> getListCardJson() {
        return listCardJson;
    }

    public void setListCardJson(List<String> listCardJson) {
        this.listCardJson = listCardJson;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
