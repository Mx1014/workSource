package com.everhomes.rest.general_approval;

/**
 * 要使用这个附加字段对象的话，可以继承这个类，
 * 然后再写像 EnterpriseApprovalCustomField 那样的枚举，这样就不用记这些乱七八糟的名称了
 *
 * Created by ryan on 2017/10/24.
 */
public class GeneralApprovalAdditionalFieldDTO {

    private String stringTag1;
    private String stringTag2;
    private String stringTag3;
    private Long integralTag1;
    private Long integralTag2;
    private Long integralTag3;

    public String getStringTag1() {
        return stringTag1;
    }

    public void setStringTag1(String stringTag1) {
        this.stringTag1 = stringTag1;
    }

    public String getStringTag2() {
        return stringTag2;
    }

    public void setStringTag2(String stringTag2) {
        this.stringTag2 = stringTag2;
    }

    public String getStringTag3() {
        return stringTag3;
    }

    public void setStringTag3(String stringTag3) {
        this.stringTag3 = stringTag3;
    }

    public Long getIntegralTag1() {
        return integralTag1;
    }

    public void setIntegralTag1(Long integralTag1) {
        this.integralTag1 = integralTag1;
    }

    public Long getIntegralTag2() {
        return integralTag2;
    }

    public void setIntegralTag2(Long integralTag2) {
        this.integralTag2 = integralTag2;
    }

    public Long getIntegralTag3() {
        return integralTag3;
    }

    public void setIntegralTag3(Long integralTag3) {
        this.integralTag3 = integralTag3;
    }
}
