package com.everhomes.socialSecurity;

public interface SocialSecurityInoutTimeProvider {

    void createSocialSecurityInoutTime(SocialSecurityInoutTime inOutTime);

    SocialSecurityInoutTime getSocialSecurityInoutTimeById(Long id);

    void updateSocialSecurityInoutTime(SocialSecurityInoutTime inOutTime);

    SocialSecurityInoutTime getSocialSecurityInoutTimeByDetailId(Byte inOutType, Long detailId);
}
