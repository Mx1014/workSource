// @formatter:off
package com.everhomes.rest.zhenzhihui;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>name: 姓名</li>
 *     <li>identifyType: 证件类型,请参考{@link com.everhomes.rest.zhenzhihui.ZhenZhiHuiCertificateType}</li>
 *     <li>identifyToken: 证件号码</li>
 *     <li>email: 邮件</li>
 * </ul>
 */
public class CreateZhenZhiHuiUserInfoCommand {
    private String      name;
    private Integer     identifyType;
    private String     identifyToken;
    private String     email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIdentifyType() {
        return identifyType;
    }


    public void setIdentifyType(Integer identifyType) {
        this.identifyType = identifyType;
    }


    public String getIdentifyToken() {
        return identifyToken;
    }


    public void setIdentifyToken(String identifyToken) {
        this.identifyToken = identifyToken;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
