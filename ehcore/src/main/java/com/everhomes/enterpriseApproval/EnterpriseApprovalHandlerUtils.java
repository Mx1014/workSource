package com.everhomes.enterpriseApproval;


import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.general_approval.GeneralApproval;

public class EnterpriseApprovalHandlerUtils {
    private EnterpriseApprovalHandlerUtils() {
    }

    public static EnterpriseApprovalHandler getEnterpriseApprovalHandler(GeneralApproval ga) {
        if (ga != null) {
            EnterpriseApprovalHandler handler = PlatformContext.getComponent(EnterpriseApprovalHandler.ENTERPRISE_APPROVAL_PREFIX
                    + ga.getApprovalAttribute());
            if (handler != null) {
                return handler;
            }
        }
        return PlatformContext.getComponent(EnterpriseApprovalDefaultHandler.ENTERPRISE_APPROVAL_DEFAULT_HANDLER_NAME);
    }
}
