package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 所属类型</li>
 * <li>ownerId: 所属项目id</li>
 * <li>ownerName: 所属项目名称</li>
 * <li>type: 统计项目</li>
 * <li>total: 统计数量</li>
 * <li>processingCount: 处理中数量</li>
 * <li>closeCount: 已取消数量</li>
 * <li>initCount: 用户发起数量</li>
 * <li>agentCount: 员工代发数量</li>
 * </ul>
 */
public class PmTaskStatSubDTO {

    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String ownerName;
    private String type;
    private Integer total;

    public PmTaskStatSubDTO() {
        super();
        this.total = 0;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
