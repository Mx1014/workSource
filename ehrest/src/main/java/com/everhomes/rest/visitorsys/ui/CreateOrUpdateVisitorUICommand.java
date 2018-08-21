package com.everhomes.rest.visitorsys.ui;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.visitorsys.BaseVisitorDTO;
import com.everhomes.rest.visitorsys.VisitorsysApprovalFormItem;
import com.everhomes.rest.visitorsys.VisitorsysStatus;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>appkey: (必填)appkey</li>
 * <li>signature: (必填)签名</li>
 * <li>nonce: (必填)3位随机数</li>
 * <li>timestamp: (必填)当前时间戳</li>
 * <li>deviceType: (必填)设备类型，{@link com.everhomes.rest.visitorsys.VisitorsysDeviceType}</li>
 * <li>deviceId: (必填)设备唯一标识</li>
 * <li>id: (选填)访客/预约ID,更新必填</li>
 * <li>visitorName: (必填)访客姓名</li>
 * <li>followUpNumbers: (选填)随访人数</li>
 * <li>visitorPhone: (必填)访客电话</li>
 * <li>visitReasonId: (必填)随访事由id</li>
 * <li>visitReason: (必填)随访事由</li>
 * <li>inviterId: (选填)邀请者id,预约访客必填</li>
 * <li>inviterName: (选填)邀请者姓名,预约访客必填</li>
 * <li>plannedVisitTime: (必填)计划到访时间</li>
 * <li>visitTime: (选填)到访时间/登记时间</li>
 * <li>visitStatus: (必填)访客状态列表，{@link com.everhomes.rest.visitorsys.VisitorsysStatus}</li>
 * <li>bookingStatus: (必填)预约状态列表，{@link com.everhomes.rest.visitorsys.VisitorsysStatus}</li>
 * <li>visitorType: (必填)访客类型，{@link com.everhomes.rest.visitorsys.VisitorsysVisitorType}</li>
 * <li>enterpriseId: (选填)公司id，园区访客必填</li>
 * <li>enterpriseName: (选填)公司名称，园区访客必填</li>
 * <li>officeLocationId: (选填)办公地点id,公司访客必填</li>
 * <li>officeLocationName: (选填)办公地点,公司访客必填</li>
 * <li>communityFormValues: (选填) 园区表单值 {@link com.everhomes.rest.visitorsys.VisitorsysApprovalFormItem}</li>
 * <li>enterpriseFormValues: (选填) 公司表单值 {@link com.everhomes.rest.visitorsys.VisitorsysApprovalFormItem}</li>
 * <li>visitorPicUri: (选填)访客头像uri</li>
 * <li>formJsonValue: (选填)表单提交json值</li>
 * <li>visitorSignUri: (选填)签名图片或者pdf的地址</li>
 * <li>visitorSignCharacter: (选填)访客签名字符串</li>
 * </ul>
 */
public class CreateOrUpdateVisitorUICommand extends BaseVisitorDTO {
    private String appkey;
    private String signature;
    private Integer nonce;
    private Long timestamp;
    private String deviceType;
    private String deviceId;
    @ItemType(VisitorsysApprovalFormItem.class)
    private List<VisitorsysApprovalFormItem> communityFormValues;
    @ItemType(VisitorsysApprovalFormItem.class)
    private List<VisitorsysApprovalFormItem> enterpriseFormValues;

    private String visitorPicUri;
    private String visitorSignUri;
    private String visitorSignCharacter;

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getNonce() {
        return nonce;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<VisitorsysApprovalFormItem> getCommunityFormValues() {
        return communityFormValues;
    }

    public void setCommunityFormValues(List<VisitorsysApprovalFormItem> communityFormValues) {
        this.communityFormValues = communityFormValues;
    }

    public List<VisitorsysApprovalFormItem> getEnterpriseFormValues() {
        return enterpriseFormValues;
    }

    public void setEnterpriseFormValues(List<VisitorsysApprovalFormItem> enterpriseFormValues) {
        this.enterpriseFormValues = enterpriseFormValues;
    }

    public String getVisitorPicUri() {
        return visitorPicUri;
    }

    public void setVisitorPicUri(String visitorPicUri) {
        this.visitorPicUri = visitorPicUri;
    }

    public String getVisitorSignUri() {
        return visitorSignUri;
    }

    public void setVisitorSignUri(String visitorSignUri) {
        this.visitorSignUri = visitorSignUri;
    }

    public String getVisitorSignCharacter() {
        return visitorSignCharacter;
    }

    public void setVisitorSignCharacter(String visitorSignCharacter) {
        this.visitorSignCharacter = visitorSignCharacter;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
