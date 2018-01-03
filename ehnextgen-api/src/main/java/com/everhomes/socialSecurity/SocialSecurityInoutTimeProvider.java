package com.everhomes.socialSecurity;

import java.util.List;

public interface SocialSecurityInoutTimeProvider {

    void createSocialSecurityInoutTime(SocialSecurityInoutTime inOutTime);

    SocialSecurityInoutTime getSocialSecurityInoutTimeById(Long id);

    void updateSocialSecurityInoutTime(SocialSecurityInoutTime inOutTime);

    SocialSecurityInoutTime getSocialSecurityInoutTimeByDetailId(Byte inOutType, Long detailId);

    List<Long> listSocialSecurityEmployeeDetailIdsByPayMonth(Long ownerId, String payMonth, Byte inOutTime);
}
