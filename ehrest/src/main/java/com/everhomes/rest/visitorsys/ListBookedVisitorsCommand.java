// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId: (必填)域空间id</li>
 * <li>ownerType: (必填)归属的类型，{@link com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
 * <li>appId: (必填)应用Id</li>
 * <li>keyWords: (选填)访客信息,访客姓名手机号码模糊搜索</li>
 * <li>visitorType: (选填)访客类型，{@link com.everhomes.rest.visitorsys.VisitorsysVisitorType}</li>
 * <li>visitStatusList: (选填)到访状态列表，{@link com.everhomes.rest.visitorsys.VisitorsysVisitStatus}</li>
 * <li>startPlannedVisitTime: (选填)计划到访时间开始，预约管理用</li>
 * <li>endPlannedVisitTime: (选填)计划到访时间结束，预约管理用</li>
 * <li>officeLocationId: (选填)办公地点id，企业访客模块用</li>
 * <li>enterpriseId: (选填)公司id，园区访客模块用</li>
 * <li>visitReasonId: (选填)事由id</li>
 * <li>startVisitTime: (选填)实际到访企业/公司时间开始，访客管理用</li>
 * <li>endVisitTime: (选填)实际到访企业/公司时间结束，访客管理用</li>
 * <li>pageAnchor: (选填)锚点</li>
 * <li>pageSize: (选填)每页的数量</li>
 * </ul>
 */
public class ListBookedVisitorsCommand extends BaseVisitorsysCommand{
    private String keyWords;
    private Byte visitorType;
    private List<Byte> visitStatusList;
    private Long startPlannedVisitTime;
    private Long endPlannedVisitTime;
    private Long officeLocationId;
    private Long enterpriseId;
    private Long visitReasonId;
    private Long startVisitTime;
    private Long endVisitTime;
    private Long pageAnchor;
    private Integer pageSize;

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public Byte getVisitorType() {
        return visitorType;
    }

    public void setVisitorType(Byte visitorType) {
        this.visitorType = visitorType;
    }

    public List<Byte> getVisitStatusList() {
        return visitStatusList;
    }

    public void setVisitStatusList(List<Byte> visitStatusList) {
        this.visitStatusList = visitStatusList;
    }

    public Long getStartPlannedVisitTime() {
        return startPlannedVisitTime;
    }

    public void setStartPlannedVisitTime(Long startPlannedVisitTime) {
        this.startPlannedVisitTime = startPlannedVisitTime;
    }

    public Long getEndPlannedVisitTime() {
        return endPlannedVisitTime;
    }

    public void setEndPlannedVisitTime(Long endPlannedVisitTime) {
        this.endPlannedVisitTime = endPlannedVisitTime;
    }

    public Long getOfficeLocationId() {
        return officeLocationId;
    }

    public void setOfficeLocationId(Long officeLocationId) {
        this.officeLocationId = officeLocationId;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getVisitReasonId() {
        return visitReasonId;
    }

    public void setVisitReasonId(Long visitReasonId) {
        this.visitReasonId = visitReasonId;
    }

    public Long getStartVisitTime() {
        return startVisitTime;
    }

    public void setStartVisitTime(Long startVisitTime) {
        this.startVisitTime = startVisitTime;
    }

    public Long getEndVisitTime() {
        return endVisitTime;
    }

    public void setEndVisitTime(Long endVisitTime) {
        this.endVisitTime = endVisitTime;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
