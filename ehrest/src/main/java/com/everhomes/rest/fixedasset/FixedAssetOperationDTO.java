package com.everhomes.rest.fixedasset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.Date;
import java.util.List;

/**
 * <ul>
 * <li>operationType: 操作类型，新建或编辑</li>
 * <li>createUid: 操作uid</li>
 * <li>createTime: 操作时间</li>
 * <li>changeItems: 变更字段值列表，参考{@link com.everhomes.rest.fixedasset.FixedAssetOperationItemDTO}</li>
 * </ul>
 */
public class FixedAssetOperationDTO {
    private String operationType;
    private Long createUid;
    private Date createTime;
    private String operatorName;
    @ItemType(FixedAssetOperationItemDTO.class)
    private List<FixedAssetOperationItemDTO> changeItems;

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Long getCreateUid() {
        return createUid;
    }

    public void setCreateUid(Long createUid) {
        this.createUid = createUid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public List<FixedAssetOperationItemDTO> getChangeItems() {
        return changeItems;
    }

    public void setChangeItems(List<FixedAssetOperationItemDTO> changeItems) {
        this.changeItems = changeItems;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
