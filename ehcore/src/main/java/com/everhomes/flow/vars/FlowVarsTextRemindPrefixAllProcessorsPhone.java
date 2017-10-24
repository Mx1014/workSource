package com.everhomes.flow.vars;

import org.springframework.stereotype.Component;

/**
 * 节点提醒的 上个节点处理人手机号
 * @author janson
 *
 */
@Component("flow-variable-text-remind-prefix-processors-phone")
public class FlowVarsTextRemindPrefixAllProcessorsPhone extends FlowVarsTextTrackerPrefixAllProcessorsPhone {

	/*@Autowired
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
    }*/
}
