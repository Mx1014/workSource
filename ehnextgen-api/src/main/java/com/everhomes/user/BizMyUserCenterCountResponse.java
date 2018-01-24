package com.everhomes.user;

import com.everhomes.util.StringHelper;

public class BizMyUserCenterCountResponse {

    private MyUserCenterCount response;

    public static class MyUserCenterCount {
        public long promotionCount;
        public long shoppingCardCount;
        public long orderCount;

        @Override
        public String toString() {
            return StringHelper.toJsonString(this);
        }
    }

    public MyUserCenterCount getResponse() {
        return response;
    }

    public void setResponse(MyUserCenterCount response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
