package com.everhomes.sms;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>reports: reports</li>
 *     <li>responseBody: responseBody</li>
 *     <li>responseContentType: responseContentType</li>
 * </ul>
 */
public class SmsReportResponse {

    private List<SmsReportDTO> reports;

    private String responseBody;
    private String responseContentType;

    public SmsReportResponse(List<SmsReportDTO> reports) {
        this.reports = reports;
    }

    public List<SmsReportDTO> getReports() {
        return reports;
    }

    public void setReports(List<SmsReportDTO> reports) {
        this.reports = reports;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseContentType() {
        return responseContentType;
    }

    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
