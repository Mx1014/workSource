package com.everhomes.sms;

import com.everhomes.util.StringHelper;

import java.util.Map;

/**
 * <ul>
 *     <li>uri: uri</li>
 *     <li>requestBody: requestBody</li>
 *     <li>requestContentType: requestContentType</li>
 *     <li>parameterMap: parameterMap</li>
 * </ul>
 */
public class SmsReportRequest {

    private String uri;
    private String requestBody;
    private String requestContentType;
    private Map<String, String[]> parameterMap;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    public String[] getParameter(String name) {
        if (parameterMap == null) {
            return null;
        }
        return parameterMap.get(name);
    }

    public void setParameterMap(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
