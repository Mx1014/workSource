//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2018/5/9.
 */

public class MsgTemplate {
    private Long msgTemplateId;
    private String msgTemplateStr;

    public MsgTemplate(Long msgTemplateId, String msgTemplateStr) {
        this.msgTemplateId = msgTemplateId;
        this.msgTemplateStr = msgTemplateStr;
    }

    public Long getMsgTemplateId() {
        return msgTemplateId;
    }

    public void setMsgTemplateId(Long msgTemplateId) {
        this.msgTemplateId = msgTemplateId;
    }

    public String getMsgTemplateStr() {
        return msgTemplateStr;
    }

    public void setMsgTemplateStr(String msgTemplateStr) {
        this.msgTemplateStr = msgTemplateStr;
    }
}
