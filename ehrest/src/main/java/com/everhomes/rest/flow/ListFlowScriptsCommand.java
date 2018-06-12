// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>flowId: flowId</li>
 *     <li>scriptType: 脚本类型 {@link com.everhomes.rest.flow.FlowScriptType}</li>
 *     <li>keyword: 关键字</li>
 *     <li>pageSize: pageSize</li>
 *     <li>pageAnchor: pageAnchor</li>
 * </ul>
 */
public class ListFlowScriptsCommand {

    @NotNull
    private Long flowId;
    private String scriptType;
    private String keyword;
    private Integer pageSize;
    private Long pageAnchor;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
