//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2018/5/7.
 */
/**
 *<ul>
 * <li>dayRespectToDueDay: 距离欠费日期的天数</li>
 * <li>dayType: dayRespectToDueDay的type， 1：前；2：后</li>
 * <li>appNoticeTemplateId: app催缴模板的id</li>
 * <li>msgNoticeTemplateId: 短信催缴模板的id</li>
 * <li>noticeObjType: 通知对象的type</li>
 * <li>msgNoticeTemplateId: 通知对象的id</li>
 *</ul>
 */
public class NoticeConfig {
    private Byte dayType;
    private String dayRespectToDueDay;
    private Long appNoticeTemplateId;
    private Long msgNoticeTemplateId;
    private Byte noticeObjType;
    private Long noticeObjId;

    public Byte getNoticeObjType() {
        return noticeObjType;
    }

    public void setNoticeObjType(Byte noticeObjType) {
        this.noticeObjType = noticeObjType;
    }

    public Long getNoticeObjId() {
        return noticeObjId;
    }

    public void setNoticeObjId(Long noticeObjId) {
        this.noticeObjId = noticeObjId;
    }

    public Byte getDayType() {
        return dayType;
    }

    public void setDayType(Byte dayType) {
        this.dayType = dayType;
    }

    public String getDayRespectToDueDay() {
        return dayRespectToDueDay;
    }

    public void setDayRespectToDueDay(String dayRespectToDueDay) {
        this.dayRespectToDueDay = dayRespectToDueDay;
    }

    public Long getAppNoticeTemplateId() {
        return appNoticeTemplateId;
    }

    public void setAppNoticeTemplateId(Long appNoticeTemplateId) {
        this.appNoticeTemplateId = appNoticeTemplateId;
    }

    public Long getMsgNoticeTemplateId() {
        return msgNoticeTemplateId;
    }

    public void setMsgNoticeTemplateId(Long msgNoticeTemplateId) {
        this.msgNoticeTemplateId = msgNoticeTemplateId;
    }
}
