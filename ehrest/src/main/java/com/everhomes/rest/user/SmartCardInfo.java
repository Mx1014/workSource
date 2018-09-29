package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>smartCardId: 每一个密钥都对应一个 ID，以表示密钥是否被更新过</li>
 * <li>smartCardKey: 一卡通进行 base64 之后的密码</li>
 * <li>smartCardhandlers: 所有此用户相关的二维码处理回调 {@link com.everhomes.rest.user.SmartCardHandler}</li>
 * <li>smartCardDescLink: 描述链接文档</li>
 * <li>baseItems: 基础拓展项，我的钱包，我的钥匙的信息</li>
 * </ul>
 * @author janson
 *
 */
public class SmartCardInfo {
    private Long smartCardId;
    private String smartCardKey;
    private String smartCardDescLink;
    
    @ItemType(SmartCardHandler.class)
    private List<SmartCardHandler> smartCardhandlers;
    
    @ItemType(SmartCardHandlerItem.class)
    private List<SmartCardHandlerItem> baseItems;
    
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
}
