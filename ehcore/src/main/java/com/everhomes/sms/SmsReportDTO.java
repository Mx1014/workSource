package com.everhomes.sms;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>smsId: smsId</li>
 *     <li>mobile: mobile</li>
 *     <li>status: 报告状态</li>
 *     <li>responseBody: 给第三方响应的结果</li>
 *     <li>responseContentType: 响应体类型</li>
 * </ul>
 */
public class SmsReportDTO {

    private String smsId;
    private String mobile;
    private Byte status;
    private String responseBody;
    private String responseContentType;

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getResponseContentType() {
        return responseContentType;
    }

    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
