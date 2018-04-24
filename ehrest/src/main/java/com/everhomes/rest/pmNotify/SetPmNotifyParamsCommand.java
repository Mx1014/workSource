package com.everhomes.rest.pmNotify;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>params: 提醒参数 参考{@link com.everhomes.rest.pmNotify.PmNotifyParams}</li>
 * </ul>
 * Created by ying.xiong on 2017/9/11.
 */
public class SetPmNotifyParamsCommand {

    @ItemType(PmNotifyParams.class)
    private List<PmNotifyParams> params;

    private String ownerType;

    public List<PmNotifyParams> getParams() {
        return params;
    }

    public void setParams(List<PmNotifyParams> params) {
        this.params = params;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
