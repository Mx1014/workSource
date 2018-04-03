package com.everhomes.rest.flow;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>flowNodeName: flowNodeName</li>
 * </ul>
 */
public class FlowCaseGoToProcessDTO {

    private Long id;
    private String flowNodeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlowNodeName() {
        return flowNodeName;
    }

    public void setFlowNodeName(String flowNodeName) {
        this.flowNodeName = flowNodeName;
    }

}
