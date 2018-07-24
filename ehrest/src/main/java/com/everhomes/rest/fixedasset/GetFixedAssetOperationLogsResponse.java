package com.everhomes.rest.fixedasset;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>operationLogs: 编辑记录</li>
 * <li>nextPageAnchor: 下一页开始的锚点</li>
 * </ul>
 */
public class GetFixedAssetOperationLogsResponse {
    private List<FixedAssetOperationDTO> operationLogs;
    private Long nextPageAnchor;

    public List<FixedAssetOperationDTO> getOperationLogs() {
        return operationLogs;
    }

    public void setOperationLogs(List<FixedAssetOperationDTO> operationLogs) {
        this.operationLogs = operationLogs;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
