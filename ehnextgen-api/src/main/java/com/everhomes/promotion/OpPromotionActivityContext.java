package com.everhomes.promotion;

import com.everhomes.user.User;
import com.everhomes.util.StringHelper;

public class OpPromotionActivityContext implements OpPromotionContext {
    OpPromotionActivity promotion;
    User user;
    
    Boolean needUpdate;

    public OpPromotionActivityContext(OpPromotionActivity op) {
        this.promotion = op;
        this.needUpdate = false;
    }
    
    public OpPromotionActivity getPromotion() {
        return promotion;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(Boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
