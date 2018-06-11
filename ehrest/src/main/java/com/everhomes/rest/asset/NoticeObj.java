//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2018/5/7.
 */

import java.io.Serializable;

/**
 *<ul>
 * <li>noticeObjType:催缴对象的类型</li>
 * <li>noticeObjId:催缴对象的id</li>
 * <li>noticeObjName:催缴对象的显示</li>
 *</ul>
 */
public class NoticeObj implements Serializable{
    private String noticeObjType;
    private Long noticeObjId;
    private String noticeObjName;

    public String getNoticeObjName() {
        return noticeObjName;
    }

    public void setNoticeObjName(String noticeObjName) {
        this.noticeObjName = noticeObjName;
    }

    public String getNoticeObjType() {
        return noticeObjType;
    }

    public void setNoticeObjType(String noticeObjType) {
        this.noticeObjType = noticeObjType;
    }

    public Long getNoticeObjId() {
        return noticeObjId;
    }

    public void setNoticeObjId(Long noticeObjId) {
        this.noticeObjId = noticeObjId;
    }
}
