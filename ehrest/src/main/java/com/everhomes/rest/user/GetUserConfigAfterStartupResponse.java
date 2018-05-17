package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>smartCardId: 每一个密钥都对应一个 ID，以表示密钥是否被更新过</li>
 * <li>smartCardKey: 一卡通进行 base64 之后的密码</li>
 * <li>smartCardhandlers: 所有此用户相关的二维码处理回调</li>
 * <li>smartCardDescLink: 描述链接文档</li>
 * </ul>
 * @author janson
 *
 */
public class GetUserConfigAfterStartupResponse {
    private Long smartCardId;
    private String smartCardKey;
    private String smartCardDescLink;
    
    @ItemType(SmartCardHandler.class)
    private List<SmartCardHandler> smartCardhandlers;
    
    public Long getSmartCardId() {
        return smartCardId;
    }

    public void setSmartCardId(Long smartCardId) {
        this.smartCardId = smartCardId;
    }

    public String getSmartCardKey() {
        return smartCardKey;
    }

    public void setSmartCardKey(String smartCardKey) {
        this.smartCardKey = smartCardKey;
    }

    public List<SmartCardHandler> getSmartCardhandlers() {
        return smartCardhandlers;
    }

    public void setSmartCardhandlers(List<SmartCardHandler> smartCardhandlers) {
        this.smartCardhandlers = smartCardhandlers;
    }

    public String getSmartCardDescLink() {
        return smartCardDescLink;
    }

    public void setSmartCardDescLink(String smartCardDescLink) {
        this.smartCardDescLink = smartCardDescLink;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
