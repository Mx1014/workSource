package com.everhomes.promotion;

import com.everhomes.organization.OrganizationMember;

public interface PromotionUserService {

    void listAllUser(OpPromotionUserVisitor visitor, OpPromotionUserCallback callback);

    void listUserByCity(OpPromotionUserVisitor visitor, OpPromotionUserCallback callback);

    void listUserByCommunity(OpPromotionUserVisitor visitor, OpPromotionUserCallback callback);

    void listUserByCompany(OpPromotionUserVisitor visitor, OpPromotionUserCallback callback);

    Boolean checkOrganizationMember(OpPromotionActivity promotion, OrganizationMember member);

    void listUserByUserId(OpPromotionUserVisitor visitor, OpPromotionUserCallback callback);
}
