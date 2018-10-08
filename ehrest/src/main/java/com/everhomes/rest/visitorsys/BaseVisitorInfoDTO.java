// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
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
 * <li>visitorPicUrl: (选填)访客头像url</li>
 * <li>sendMessageInviterFlag: (选填)是否发送消息给邀请者，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 * <li>sendSmsFlag: (选填)是否发送邀请函短信，{@link com.everhomes.rest.visitorsys.VisitorsysFlagType}</li>
 * <li>visitorSignUri: (选填)签名图片或者pdf的uri</li>
 * <li>visitorSignUrl: (选填)签名图片或者pdf的url</li>
 * <li>visitorSignCharacter: (选填)访客签名字符串</li>
 * <li>createTime: (必填)预约创建时间</li>
 * <li>invitationNo: (必填)邀请码</li>
 * <li>visitorActionList: (选填)事件列表, {@link com.everhomes.rest.visitorsys.BaseVisitorActionDTO}</li>
 * <li>authRuleType: 授权规则种类，0 时长，1 次数</li>
 * <li>doorAccessInvalidTimes: 门禁使用次数</li>
 * <li>doorAccessInvalidDuration: 门禁有效期（天）</li>
 * </ul>
 */
public class BaseVisitorInfoDTO extends BaseVisitorDTO{
    @ItemType(VisitorsysApprovalFormItem.class)
    private List<VisitorsysApprovalFormItem> communityFormValues;
    @ItemType(VisitorsysApprovalFormItem.class)
    private List<VisitorsysApprovalFormItem> enterpriseFormValues;

    private String visitorPicUrl;
    private String visitorPicUri;

    private Byte sendMessageInviterFlag;
    private Byte sendSmsFlag;

    private String visitorSignUri;
    private String visitorSignUrl;
    private String visitorSignCharacter;
    private Timestamp createTime;
    private String invitationNo;

    @ItemType(BaseVisitorActionDTO.class)
    private List<BaseVisitorActionDTO> visitorActionList;

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

    public String getVisitorPicUrl() {
        return visitorPicUrl;
    }

    public void setVisitorPicUrl(String visitorPicUrl) {
        this.visitorPicUrl = visitorPicUrl;
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

    public String getVisitorSignUrl() {
        return visitorSignUrl;
    }

    public void setVisitorSignUrl(String visitorSignUrl) {
        this.visitorSignUrl = visitorSignUrl;
    }

    public String getVisitorSignCharacter() {
        return visitorSignCharacter;
    }

    public void setVisitorSignCharacter(String visitorSignCharacter) {
        this.visitorSignCharacter = visitorSignCharacter;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getInvitationNo() {
        return invitationNo;
    }

    public void setInvitationNo(String invitationNo) {
        this.invitationNo = invitationNo;
    }

    public List<BaseVisitorActionDTO> getVisitorActionList() {
        return visitorActionList;
    }

    public void setVisitorActionList(List<BaseVisitorActionDTO> visitorActionList) {
        this.visitorActionList = visitorActionList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
