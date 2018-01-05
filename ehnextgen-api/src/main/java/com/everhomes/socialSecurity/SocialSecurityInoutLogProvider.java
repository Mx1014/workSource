package com.everhomes.socialSecurity;

import java.util.List;

public interface SocialSecurityInoutLogProvider {

    void createSocialSecurityInoutLog(SocialSecurityInoutLog inoutLog);

    List<SocialSecurityInoutLog> listSocialSecurityInoutLogs(Long organizationId, Long detailId);
}
