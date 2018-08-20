package com.everhomes.enterpriseApproval;

import com.everhomes.general_approval.GeneralApprovalFormDataVerifyHandler;
import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(EnterpriseApprovalFormDataVerifyDefaultHandler.ENTERPRISE_APPROVAL_FORM_DATA_VERIFY)
public class EnterpriseApprovalFormDataVerifyDefaultHandler implements GeneralApprovalFormDataVerifyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseApprovalFormDataVerifyDefaultHandler.class);

    static final String ENTERPRISE_APPROVAL_FORM_DATA_VERIFY = GeneralApprovalFormDataVerifyHandler.GENERAL_APPROVAL_FORM_DATA_VERIFY_PREFIX + "any-module";

    @Override
    public void verify(PostApprovalFormCommand cmd) {

    }
}
