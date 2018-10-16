// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId: (必填)域空间id</li>
 * <li>ownerType: (必填)归属的类型，{@link com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
 * <li>appId: (必填)应用Id</li>
 * <li>ownerToken: (必填)公司/园区访客注册地址标识</li>
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
 * <li>visitorPicUri: (选填)访客头像url</li>
 * <li>sendMessageInviterFlag: (选填)是否发送消息给邀请者，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 * <li>sendSmsFlag: (选填)是否发送邀请函短信，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 * <li>visitorSignUri: (选填)签名图片或者pdf的地址</li>
 * <li>visitorSignCharacter: (选填)访客签名字符串</li>
 * <li>doorAccessAuthDurationType: 访客授权有效期种类,0 天数，1 小时数</li>
 * <li>doorAccessAuthDuration: 访客授权有效期</li>
 * <li>doorAccessEnableAuthCount: 访客授权次数开关 0 关 1 开</li>
 * <li>doorAccessAuthCount: 访客授权次数</li>
 * <li>doorAccessEndTime: 门禁授权结束时间有效期</li>
 * <li>doorGuardId: 授权的门禁Id</li>
 *
 * </ul>
 */
public class CreateOrUpdateVisitorCommand extends BaseVisitorDTO{
    @ItemType(VisitorsysApprovalFormItem.class)
    private List<VisitorsysApprovalFormItem> communityFormValues;
    @ItemType(VisitorsysApprovalFormItem.class)
    private List<VisitorsysApprovalFormItem> enterpriseFormValues;

    private String visitorPicUri;
    private Byte sendMessageInviterFlag;
    private Byte sendSmsFlag;

    private String visitorSignUri;
    private String visitorSignCharacter;
    private Byte source = VisitorsysSourceType.INTERNAL.getCode();
    private Byte notifyThirdSuccessFlag = VisitorsysNotifyThirdType.NOT_YET.getCode();

//  扫码登记或ipad自助登记标志
    private Byte fromDevice;

    private String doorGuardId;

    public Byte getSource() {
        return source;
    }

    public void setSource(Byte source) {
        this.source = source;
    }

    public Byte getNotifyThirdSuccessFlag() {
        return notifyThirdSuccessFlag;
    }

    public void setNotifyThirdSuccessFlag(Byte notifyThirdSuccessFlag) {
        this.notifyThirdSuccessFlag = notifyThirdSuccessFlag;
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

    public Byte getSendMessageInviterFlag() {
        return sendMessageInviterFlag;
    }

    public void setSendMessageInviterFlag(Byte sendMessageInviterFlag) {
        this.sendMessageInviterFlag = sendMessageInviterFlag;
    }

    public Byte getSendSmsFlag() {
        return sendSmsFlag;
    }

    public void setSendSmsFlag(Byte sendSmsFlag) {
        this.sendSmsFlag = sendSmsFlag;
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

    public Byte getFromDevice() {
        return fromDevice;
    }

    public void setFromDevice(Byte fromDevice) {
        this.fromDevice = fromDevice;
    }

    public String getDoorGuardId() {
        return doorGuardId;
    }

    public void setDoorGuardId(String doorGuardId) {
        this.doorGuardId = doorGuardId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
