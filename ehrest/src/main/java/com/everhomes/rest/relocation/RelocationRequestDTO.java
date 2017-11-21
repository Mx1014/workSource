package com.everhomes.rest.relocation;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author sw on 2017/11/20.
 */
public class RelocationRequestDTO {

    private Long id;
    private Integer namespaceId;
    private String requestNo;
    private Long requestorEnterpriseId;
    private String requestorEntperiseName;
    private String requestorEntperiseAddress;
    private Long requestorUid;
    private String requestorName;
    private Timestamp relocationDate;
    private Byte status;
    private Long creatorUid;
    private Timestamp createTime;
    private Long flowCaseId;
    private Timestamp cancelTime;
    private Long cancelUid;
    @ItemType(RelocationRequestItemDTO.class)
    private List<RelocationRequestItemDTO> items;

    public List<RelocationRequestItemDTO> getItems() {
        return items;
    }

    public void setItems(List<RelocationRequestItemDTO> items) {
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public Long getRequestorEnterpriseId() {
        return requestorEnterpriseId;
    }

    public void setRequestorEnterpriseId(Long requestorEnterpriseId) {
        this.requestorEnterpriseId = requestorEnterpriseId;
    }

    public String getRequestorEntperiseName() {
        return requestorEntperiseName;
    }

    public void setRequestorEntperiseName(String requestorEntperiseName) {
        this.requestorEntperiseName = requestorEntperiseName;
    }

    public String getRequestorEntperiseAddress() {
        return requestorEntperiseAddress;
    }

    public void setRequestorEntperiseAddress(String requestorEntperiseAddress) {
        this.requestorEntperiseAddress = requestorEntperiseAddress;
    }

    public Long getRequestorUid() {
        return requestorUid;
    }

    public void setRequestorUid(Long requestorUid) {
        this.requestorUid = requestorUid;
    }

    public String getRequestorName() {
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public Timestamp getRelocationDate() {
        return relocationDate;
    }

    public void setRelocationDate(Timestamp relocationDate) {
        this.relocationDate = relocationDate;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public Timestamp getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Timestamp cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Long getCancelUid() {
        return cancelUid;
    }

    public void setCancelUid(Long cancelUid) {
        this.cancelUid = cancelUid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
