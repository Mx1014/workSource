//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/11/10.
 */
/**
 *<ul>
 * <li>noticeDays:数字列表，催缴的设置</li>
 *</ul>
 */
public class ListAutoNoticeConfigResponse {
    @ItemType(Integer.class)
    private List<Integer> noticeDays;

    public List<Integer> getNoticeDays() {
        return noticeDays;
    }

    public void setNoticeDays(List<Integer> noticeDays) {
        this.noticeDays = noticeDays;
    }
}
