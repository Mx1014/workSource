// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuthPayLogs;
import com.everhomes.util.StringHelper;

public class EnterprisePaymentAuthPayLog extends EhEnterprisePaymentAuthPayLogs {

    private static final long serialVersionUID = 7210965277046754483L;

    public EnterprisePaymentAuthPayLog() {

    }

    public EnterprisePaymentAuthPayLog(Integer namespaceId, Long organizationId, Long userId, Long detailId, Long appId) {
        setNamespaceId(namespaceId);
        setOrganizationId(organizationId);
        setUserId(userId);
        setDetailId(detailId);
        setPaymentSceneAppId(appId);
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}