package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>meterId: 水电表id</li>
 *     <li>namespaceId: 表所属的域空间id</li>
 * </ul>
 * Created by ying.xiong on 2017/6/26.
 */
public class GetEnergyMeterQRCodeCommand {

    private Long meterId;

    private Integer namespaceId;

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
