package com.everhomes.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.rest.flow.FlowEntityType;

/**
 * <ul>
 * <li>当前节点的所有处理人员</li>
 * </ul>
 * @author janson
 *
 */
@Component(FlowVariableUserResolver.CURR_NODE_PROCESSORS)
public class FlowVariableCurrentNodeProcessorsResolver implements FlowVariableUserResolver {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowVariableCurrentNodeProcessorResolver.class);

	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx, Map<String, Long> processedEntities,
			FlowEntityType fromEntity, Long entityId,
			FlowUserSelection userSelection, int loopCnt) {
		//可能来之同步请求或者异步请求
		return new ArrayList<Long>();
	}
}
