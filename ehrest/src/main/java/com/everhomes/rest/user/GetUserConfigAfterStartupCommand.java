package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul> 此接口，在 APP 初始化完成之后，再请求用户的一些配置信息
 * <li>smartCardSupportVer：客户端当前支持的一卡通生成版本。未来一卡通会有多个版本，旧的 APP 通过这个字段来确认一卡通的生成方式。生成一卡通  Code 过程，使用 GMT 时间。</li>
 * </ul>
 * @author janson
 *
 */
public class GetUserConfigAfterStartupCommand {
    String smartCardSupportVer;

    public String getSmartCardSupportVer() {
        return smartCardSupportVer;
    }

    public void setSmartCardSupportVer(String smartCardSupportVer) {
        this.smartCardSupportVer = smartCardSupportVer;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
