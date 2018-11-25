// @formatter:off
package com.everhomes.rest.zhenzhihui;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>name: 姓名</li>
 *     <li>identifyType: 证件类型,请参考{@link com.everhomes.rest.zhenzhihui.ZhenZhiHuiCertificateType}</li>
 *     <li>identifyToken: 证件号码</li>
 *     <li>email: 邮件</li>
 *     <li>corporationToken: 法人证件号码</li>
 *     <li>corporationType: 法人证件类型,请参考{@link com.everhomes.rest.zhenzhihui.ZhenZhiHuiCertificateType}</li>
 *     <li>corporationName: 法人名称</li>
 *     <li>enterpriseToken: 单位证件号码</li>
 *     <li>enterpriseName: 单位名称</li>
 * </ul>
 */
public class CreateZhenZhiHuiUserAndEnterpriseInfoCommand {
    private String      name;
    private Integer     identifyType;
    private String     identifyToken;
    private String     email;
    private String     corporationToken;
    private Integer     corporationType;
    private String     corporationName;
    private String     enterpriseToken;
    private String     enterpriseName;
    private Integer    enterpriseType;

    public Integer getEnterpriseType() {
        return enterpriseType;
    }

    public void setEnterpriseType(Integer enterpriseType) {
        this.enterpriseType = enterpriseType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnterpriseToken() {
        return enterpriseToken;
    }

    public void setEnterpriseToken(String enterpriseToken) {
        this.enterpriseToken = enterpriseToken;
    }

    public Integer getCorporationType() {
        return corporationType;
    }

    public void setCorporationType(Integer corporationType) {
        this.corporationType = corporationType;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getCorporationName() {
        return corporationName;
    }

    public void setCorporationName(String corporationName) {
        this.corporationName = corporationName;
    }

    public String getCorporationToken() {

        return corporationToken;
    }

    public void setCorporationToken(String corporationToken) {
        this.corporationToken = corporationToken;
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
