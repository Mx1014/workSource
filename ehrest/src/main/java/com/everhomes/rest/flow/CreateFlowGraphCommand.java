// @formatter:off
package com.everhomes.rest.flow;

import java.util.List;

public class CreateFlowGraphCommand {

    private List<CreateFlowNodeCommand> nodes;
    private List<FlowGraphLinksDTO> links;
}
