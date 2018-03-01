package com.everhomes.flow;

import com.everhomes.rest.flow.FlowEntityType;

import java.util.List;
import java.util.Map;

public interface FlowUserSelectionService {

    List<Long> resolveUserSelections(FlowCaseState ctx, Map<String, Long> processedEntities, FlowEntityType entityType, Long entityId, List<FlowUserSelection> selections, int loopCnt, int maxCount);

}
