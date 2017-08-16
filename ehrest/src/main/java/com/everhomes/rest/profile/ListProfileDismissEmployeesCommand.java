package com.everhomes.rest.profile;

import com.everhomes.util.StringHelper;

import java.sql.Date;

/**
 * <ul>
 * <li>dismissTimeStart: 离职起始日期</li>
 * <li>dismissTimeEnd: 离职结束日期</li>
 * <li>checkInTimeStart: 入职起始日期</li>
 * <li>checkInTimeEnd: 入职结束日期</li>
 * <li>dismissType: 离职类型： 1-辞职 2-解雇 3-其他 参考{@link com.everhomes.rest.profile.DismissType}</li>
 * <li>dismissReason: 离职原因</li>
 * <li>contactName: 姓名</li>
 * </ul>
 */
public class ListProfileDismissEmployeesCommand {

    private Date dismissTimeStart;

    private Date dismissTimeEnd;

    private Date checkInTimeStart;

    private Date checkInTimeEnd;

    private Byte dismissType;

    private Byte dismissReason;

    private String contactName;

    public ListProfileDismissEmployeesCommand() {
    }

    public Date getDismissTimeStart() {
        return dismissTimeStart;
    }

    public void setDismissTimeStart(Date dismissTimeStart) {
        this.dismissTimeStart = dismissTimeStart;
    }

    public Date getDismissTimeEnd() {
        return dismissTimeEnd;
    }

    public void setDismissTimeEnd(Date dismissTimeEnd) {
        this.dismissTimeEnd = dismissTimeEnd;
    }

    public Date getCheckInTimeStart() {
        return checkInTimeStart;
    }

    public void setCheckInTimeStart(Date checkInTimeStart) {
        this.checkInTimeStart = checkInTimeStart;
    }

    public Date getCheckInTimeEnd() {
        return checkInTimeEnd;
    }

    public void setCheckInTimeEnd(Date checkInTimeEnd) {
        this.checkInTimeEnd = checkInTimeEnd;
    }

    public Byte getDismissType() {
        return dismissType;
    }

    public void setDismissType(Byte dismissType) {
        this.dismissType = dismissType;
    }

    public Byte getDismissReason() {
        return dismissReason;
    }

    public void setDismissReason(Byte dismissReason) {
        this.dismissReason = dismissReason;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
