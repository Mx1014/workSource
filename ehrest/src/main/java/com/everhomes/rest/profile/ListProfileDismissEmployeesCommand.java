package com.everhomes.rest.profile;

import com.everhomes.util.StringHelper;

import java.sql.Date;

/**
 * <ul>
 * <li>organizationId: 公司 id</li>
 * <li>dismissTimeStart: 离职起始日期</li>
 * <li>dismissTimeEnd: 离职结束日期</li>
 * <li>checkInTimeStart: 入职起始日期</li>
 * <li>checkInTimeEnd: 入职结束日期</li>
 * <li>dismissType: 离职类型： 1-辞职 2-解雇 3-其他 参考{@link com.everhomes.rest.profile.DismissType}</li>
 * <li>dismissReason: 离职原因</li>
 * <li>contactName: 姓名</li>
 * <li>pageAnchor: 锚点值</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListProfileDismissEmployeesCommand {

    private Long organizationId;

    private Date dismissTimeStart;

    private Date dismissTimeEnd;

    private Date checkInTimeStart;

    private Date checkInTimeEnd;

    private Byte dismissType;

    private String dismissReason;

    private String contactName;

    private Long pageAnchor;

    private Integer pageSize;

    public ListProfileDismissEmployeesCommand() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Date getDismissTimeStart() {
        return dismissTimeStart;
    }

    public void setDismissTimeStart(String dismissTimeStart) {
        this.dismissTimeStart = ProfileDateUtil.parseDate(dismissTimeStart);
    }

    public Date getDismissTimeEnd() {
        return dismissTimeEnd;
    }

    public void setDismissTimeEnd(String dismissTimeEnd) {
        this.dismissTimeEnd = ProfileDateUtil.parseDate(dismissTimeEnd);
    }

    public Date getCheckInTimeStart() {
        return checkInTimeStart;
    }

    public void setCheckInTimeStart(String checkInTimeStart) {
        this.checkInTimeStart = ProfileDateUtil.parseDate(checkInTimeStart);
    }

    public Date getCheckInTimeEnd() {
        return checkInTimeEnd;
    }

    public void setCheckInTimeEnd(String checkInTimeEnd) {
        this.checkInTimeEnd = ProfileDateUtil.parseDate(checkInTimeEnd);
    }

    public Byte getDismissType() {
        return dismissType;
    }

    public void setDismissType(Byte dismissType) {
        this.dismissType = dismissType;
    }

    public String getDismissReason() {
        return dismissReason;
    }

    public void setDismissReason(String dismissReason) {
        this.dismissReason = dismissReason;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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
