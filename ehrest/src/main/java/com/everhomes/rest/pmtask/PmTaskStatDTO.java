package com.everhomes.rest.pmtask;

import java.util.Objects;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 所属类型</li>
 * <li>ownerId: 所属项目id</li>
 * <li>ownerName: 所属项目名称</li>
 * <li>total: 合计</li>
 * <li>complete: 处理完成数量</li>
 * <li>processing: 处理中数量</li>
 * <li>close: 已取消数量</li>
 * <li>init: 用户发起数量</li>
 * <li>agent: 员工代发数量</li>
 * </ul>
 */
public class PmTaskStatDTO {

    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String ownerName;
    private Integer total;
    private Integer complete;
    private Integer processing;
    private Integer close;
    private Integer init;
    private Integer agent;

    public PmTaskStatDTO() {
        super();
        this.total = 0;
        this.complete = 0;
        this.processing = 0;
        this.close = 0;
        this.init = 0;
        this.agent = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PmTaskStatDTO that = (PmTaskStatDTO) o;
        return Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ownerId);
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getComplete() {
        return complete;
    }

    public void setComplete(Integer complete) {
        this.complete = complete;
    }

    public Integer getProcessing() {
        return processing;
    }

    public void setProcessing(Integer processing) {
        this.processing = processing;
    }

    public Integer getClose() {
        return close;
    }

    public void setClose(Integer close) {
        this.close = close;
    }

    public Integer getInit() {
        return init;
    }

    public void setInit(Integer init) {
        this.init = init;
    }

    public Integer getAgent() {
        return agent;
    }

    public void setAgent(Integer agent) {
        this.agent = agent;
    }
}
