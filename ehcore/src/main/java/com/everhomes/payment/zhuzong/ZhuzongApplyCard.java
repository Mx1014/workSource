package com.everhomes.payment.zhuzong;

/**
 * ResultID 结果编号 0正常 1 失败 2 已绑定 3 已销户 4 不存在
 */
public class ZhuzongApplyCard {

    private String UserID;
    private String UserName;
    private String ResultID;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getResultID() {
        return ResultID;
    }

    public void setResultID(String resultID) {
        ResultID = resultID;
    }
}
