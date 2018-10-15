package com.everhomes.flow.vars;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowVariableTextResolver;
import com.everhomes.rest.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(FlowVariableTextResolver.APPLIER_PHONE)
public class FlowVaribleApplierPhoneResolver implements FlowVariableTextResolver {
    @Autowired
    FlowService flowService;

    @Override
    public String variableTextRender(FlowCaseState ctx, String variable) {
        UserInfo userInfo = flowService.getUserInfoInContext(ctx, ctx.getFlowCase().getApplyUserId());
        if (userInfo == null || userInfo.getPhones() == null || userInfo.getPhones().size() == 0) {
            return "error";//TODO use error ?
        }
        return userInfo.getPhones().get(0);
    }

}
