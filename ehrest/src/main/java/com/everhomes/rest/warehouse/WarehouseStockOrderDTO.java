//@formatter:off
package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * Created by Wentian Wang on 2018/1/11.
 */
/**
 *<ul>
 * <li>identity:出入库单号</li>
 * <li>executor:执行人</li>
 * <li>serviceType:出入库类型： 1：普通入库;2.领用出库;3.采购入库;4.批量 </li>
 * <li>executionTime:执行时间</li>
 * <li>id:单的id</li>
 *</ul>
 */
public class WarehouseStockOrderDTO {
    private String identity;
    private String executor;
    private Byte serviceType;
    private String executionTime;
    private Long id;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public Byte getServiceType() {
        return serviceType;
    }

    public void setServiceType(Byte serviceType) {
        this.serviceType = serviceType;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
