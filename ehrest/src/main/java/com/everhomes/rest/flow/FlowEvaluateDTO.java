package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>flowCaseId: Case 的 ID</li>
 * <li>hasResults: 是否有结果{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * <li>results: 评分内容 {@link com.everhomes.rest.flow.FlowEvaluateResultDTO}</li>
 * </ul>
 * @author janson
 *
 */
public class FlowEvaluateDTO {

    private Integer namespaceId;
    private Long flowCaseId;
    private Byte hasResults;

    @ItemType(FlowEvaluateResultDTO.class)
    private List<FlowEvaluateResultDTO> results;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public List<FlowEvaluateResultDTO> getResults() {
        return results;
    }

    public void setResults(List<FlowEvaluateResultDTO> results) {
        this.results = results;
    }

    public Byte getHasResults() {
        return hasResults;
    }

    public void setHasResults(Byte hasResults) {
        this.hasResults = hasResults;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

