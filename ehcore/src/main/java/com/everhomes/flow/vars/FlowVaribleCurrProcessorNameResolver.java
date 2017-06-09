package com.everhomes.flow.vars;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowVariableTextResolver;
import com.everhomes.rest.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <ul>
 * <li>当前节点执行人</li>
 * </ul>
 * @author janson
 *
 */
@Component(FlowVariableTextResolver.CURR_PROCESSOR_NAME)
public class FlowVaribleCurrProcessorNameResolver implements FlowVariableTextResolver {
    @Autowired
    FlowService flowService;

    @Override
    public String variableTextRender(FlowCaseState ctx, String variable) {
        UserInfo userInfo = flowService.getCurrProcessor(ctx, variable);
        if (userInfo == null || userInfo.getNickName() == null) {
            return "error";//TODO use error ?
        }
        return userInfo.getNickName();
    }

}
