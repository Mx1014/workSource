// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId: (必填)域空间id</li>
 * <li>ownerType: (必填)归属的类型，{@link com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
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
 * <li>visitStatus: (必填)访客状态列表，{@link VisitorsysStatus}</li>
 * <li>bookingStatus: (必填)预约状态列表，{@link VisitorsysStatus}</li>
 * <li>visitorType: (必填)访客类型，{@link com.everhomes.rest.visitorsys.VisitorsysVisitorType}</li>
 * <li>enterpriseId: (选填)公司id，园区访客必填</li>
 * <li>enterpriseName: (选填)公司名称，园区访客必填</li>
 * <li>officeLocationId: (选填)办公地点id,公司访客必填</li>
 * <li>officeLocationName: (选填)办公地点,公司访客必填</li>
 * <li>communityFormValues: (选填) 园区表单值 {@link com.everhomes.rest.general_approval.PostApprovalFormItem}</li>
 * <li>enterpriseFormValues: (选填) 公司表单值 {@link com.everhomes.rest.general_approval.PostApprovalFormItem}</li>
 * <li>visitorPicUri: (选填)访客头像url</li>
 * <li>sendMessageInviterFlag: (选填)是否发送消息给邀请者，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 * <li>sendSmsFlag: (选填)是否发送邀请函短信，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 * <li>visitorSignUri: (选填)签名图片或者pdf的地址</li>
 * <li>visitorSignCharacter: (选填)访客签名字符串</li>
 * </ul>
 */
public class CreateOrUpdateVisitorCommand extends BaseVisitorDTO{
//    private Timestamp invalidTime;
//    private String plateNo;
//    private String idNumber;
//    private String visitFloor;
//    private String visitAddresses;
// * <li>invalidTime: (根据配置选填/必填)有效期</li>
// * <li>plateNo: (根据配置选填/必填)车牌号</li>
// * <li>idNumber: (根据配置选填/必填)证件号码</li>
// * <li>visitFloor: (根据配置选填/必填)到访楼层</li>
// * <li>visitAddresses: (根据配置选填/必填)到访门牌</li>
    @ItemType(PostApprovalFormItem.class)
    private List<PostApprovalFormItem> communityFormValues;
    @ItemType(PostApprovalFormItem.class)
    private List<PostApprovalFormItem> enterpriseFormValues;

    private String visitorPicUri;
    private Byte sendMessageInviterFlag;
    private Byte sendSmsFlag;

//    private String formJsonValue;
    private String visitorSignUri;
    private String visitorSignCharacter;

    public List<PostApprovalFormItem> getCommunityFormValues() {
        return communityFormValues;
    }

    public void setCommunityFormValues(List<PostApprovalFormItem> communityFormValues) {
        this.communityFormValues = communityFormValues;
    }

    public List<PostApprovalFormItem> getEnterpriseFormValues() {
        return enterpriseFormValues;
    }

    public void setEnterpriseFormValues(List<PostApprovalFormItem> enterpriseFormValues) {
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
