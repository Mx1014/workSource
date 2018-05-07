//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2018/5/7.
 */
/**
 *<ul>
 * <li>noticeObjType:催缴对象的类型</li>
 * <li>noticeObjId:催缴对象的id</li>
 * <li>noticeObjName:催缴对象的显示</li>
 *</ul>
 */
public class NoticeObj {
    private Byte noticeObjType;
    private Long noticeObjId;
    private String noticeObjName;

    public String getNoticeObjName() {
        return noticeObjName;
    }

    public void setNoticeObjName(String noticeObjName) {
        this.noticeObjName = noticeObjName;
    }

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
}
