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

    private Long targetId;

    private String targetType;

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

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
