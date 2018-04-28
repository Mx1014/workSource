// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

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
 * <li>visitStatus: (必填)到访状态列表，{@link com.everhomes.rest.visitorsys.VisitorsysVisitStatus}</li>
 * <li>visitorType: (必填)访客类型，{@link com.everhomes.rest.visitorsys.VisitorsysVisitorType}</li>
 * <li>enterpriseId: (选填)公司id，园区访客必填</li>
 * <li>enterpriseName: (选填)公司名称，园区访客必填</li>
 * <li>officeLocationId: (选填)办公地点id,公司访客必填</li>
 * <li>officeLocationName: (选填)办公地点,公司访客必填</li>
 * <li>invalidTime: (根据配置选填/必填)有效期</li>
 * <li>plateNo: (根据配置选填/必填)车牌号</li>
 * <li>idNumber: (根据配置选填/必填)证件号码</li>
 * <li>visitFloor: (根据配置选填/必填)到访楼层</li>
 * <li>visitAddresses: (根据配置选填/必填)到访门牌</li>
 * <li>visitorPicUri: (选填)访客头像url</li>
 * </ul>
 */
public class CreateOrUpdateVisitorCommand extends BaseVisitorDTO{
    private Timestamp invalidTime;
    private String plateNo;
    private String idNumber;
    private String visitFloor;
    private String visitAddresses;

    private String visitorPicUri;

    public Timestamp getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Timestamp invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getVisitFloor() {
        return visitFloor;
    }

    public void setVisitFloor(String visitFloor) {
        this.visitFloor = visitFloor;
    }

    public String getVisitAddresses() {
        return visitAddresses;
    }

    public void setVisitAddresses(String visitAddresses) {
        this.visitAddresses = visitAddresses;
    }

    public String getVisitorPicUri() {
        return visitorPicUri;
    }

    public void setVisitorPicUri(String visitorPicUri) {
        this.visitorPicUri = visitorPicUri;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
