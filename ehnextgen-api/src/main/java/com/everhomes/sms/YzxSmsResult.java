// @formatter:off
package com.everhomes.sms;

import com.everhomes.util.StringHelper;

/**
 *  云之讯发送短信的响应结果
 * {
 *    "resp": {
 *        "respCode": "000000",
 *        "failure": 1,
 *        "templateSMS": {
 *            "createDate": 20140623185016,
 *            "smsId": "f96f79240e372587e9284cd580d8f953"
 *        }
 *     }
 * }
 */
public class YzxSmsResult {

    private Resp resp;

    public Resp getResp() {
        return resp;
    }

    public void setResp(Resp resp) {
        this.resp = resp;
    }

    public static class Resp {
        public String respCode;
        public Byte failure;
        public Sms templateSMS;

        @Override
        public String toString() {
            return StringHelper.toJsonString(this);
        }
    }

    public static class Sms {
        public Long createDate;
        public String smsId;

        @Override
        public String toString() {
            return StringHelper.toJsonString(this);
        }
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
