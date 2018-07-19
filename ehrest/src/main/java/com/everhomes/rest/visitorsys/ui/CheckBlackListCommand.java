// @formatter:off
package com.everhomes.rest.visitorsys.ui;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>appkey: (必填)appkey</li>
 * <li>signature: (必填)签名</li>
 * <li>nonce: (必填)3位随机数</li>
 * <li>timestamp: (必填)当前时间戳</li>
 * <li>deviceType: (必填)设备类型，{@link com.everhomes.rest.visitorsys.VisitorsysDeviceType}</li>
 * <li>deviceId: (必填)设备唯一标识</li>
 * <li>visitorPhone: (必填)用户电话</li>
 * <li>enterpriseId: (必填)选择的到访公司id</li>
 * </ul>
 */
public class CheckBlackListCommand extends BaseVisitorsysUICommand{
    private String appkey;
    private String signature;
    private Integer nonce;
    private Long timestamp;
    private String deviceType;
    private String deviceId;
    private String visitorPhone;

    private Long enterpriseId;

    @Override
    public String getAppkey() {
        return appkey;
    }

    @Override
    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    @Override
    public String getSignature() {
        return signature;
    }

    @Override
    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public Integer getNonce() {
        return nonce;
    }

    @Override
    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    @Override
    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getDeviceType() {
        return deviceType;
    }

    @Override
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
