package com.everhomes.socialSecurity;

import com.everhomes.rest.socialSecurity.InOutLogType;

import java.util.List;

public interface SocialSecurityInoutLogProvider {

    void createSocialSecurityInoutLog(SocialSecurityInoutLog inoutLog);

    List<SocialSecurityInoutLog> listSocialSecurityInoutLogs(Long organizationId, Long detailId, String month);

    List<Long> listSocialSecurityInoutLogDetailIds(Long ownerId, String month, InOutLogType accumulationFundIn);

    List<Long> listSocialSecurityInoutLogDetailIds(Long ownerId, String month, List<Byte> accumulationFundIns);
}
