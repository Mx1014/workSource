// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>actionType: (必填)事件 1,园区自助登记，2,园区到访确认，3,园区拒绝到访  4,企业自助登记，5,企业到访确认，6,企业拒绝到访 </li>
 * <li>time: (必填)事件发生时间</li>
 * <li>uid: (选填)操作人员id</li>
 * <li>uname: (必填)操作人员名称</li>
 * </ul>
 */
public class BaseVisitorActionDTO {
    private Byte actionType;
    private Timestamp time;
    private Long uid;
    private String uname;

    public BaseVisitorActionDTO(Byte actionType, Timestamp time, Long uid, String uname) {
        this.actionType = actionType;
        this.time = time;
        this.uid = uid;
        this.uname = uname;
    }

    public BaseVisitorActionDTO() {
    }

    public Byte getActionType() {
        return actionType;
    }

    public void setActionType(Byte actionType) {
        this.actionType = actionType;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
