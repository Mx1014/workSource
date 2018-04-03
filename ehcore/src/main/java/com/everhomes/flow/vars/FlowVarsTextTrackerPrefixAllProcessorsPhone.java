package com.everhomes.flow.vars;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowVariableTextResolver;
import com.everhomes.rest.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 节点跟踪的 上一步处理人手机号
 * @author janson
 *
 */
@Component("flow-variable-text-tracker-prefix-processors-phone")
public class FlowVarsTextTrackerPrefixAllProcessorsPhone extends FlowVarsUserRemindPrefixProcessors implements FlowVariableTextResolver {

    @Autowired
    FlowService flowService;

    @Autowired
    FlowEventLogProvider flowEventLogProvider;

    @Override
    public String variableTextRender(FlowCaseState ctx, String variable) {
        List<Long> userIds = variableUserResolve(ctx, null, null, null, null, 10);
        String text = "";
        for (int i = 0; i < userIds.size() && i < 3; i++) {
            UserInfo ui = flowService.getUserInfoInContext(ctx, userIds.get(i));
            if (ui != null && ui.getPhones() != null && ui.getPhones().size() > 0) {
                text += ui.getPhones().get(0) + ", ";
            }
        }
        if (text.length() > 2) {
            text = text.substring(0, text.length() - 2);
        }
        return text;
    }
}
