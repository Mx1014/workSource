package com.everhomes.promotion;

import com.everhomes.user.User;

public interface OpPromotionUserCallback {
    void userFound(User u, OpPromotionUserVisitor visitor);
}
