package com.everhomes.payment.zhuzong;

/**
 * UserID : 账号
 * ResultID : 结果编号 1 失败 2 没有信息 3 已解绑 4 已销户
 * CardID : 卡号
 * UserName : 姓名
 * DeptName : 部门
 * Balance : 本金金额
 * StateID : 状态 0正常 1挂失
 */
public class ZhuzongUserCardInfo {

    private String UserID;
    private String ResultID;
    private String CardID;
    private String UserName;
    private String DeptName;
    private String Balance;
    private String StateID;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getResultID() {
        return ResultID;
    }

    public void setResultID(String resultID) {
        ResultID = resultID;
    }

    public String getCardID() {
        return CardID;
    }

    public void setCardID(String cardID) {
        CardID = cardID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getStateID() {
        return StateID;
    }

    public void setStateID(String stateID) {
        StateID = stateID;
    }
}
