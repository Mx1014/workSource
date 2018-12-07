// @formatter:off
package com.everhomes.rest.zhenzhihui;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id:id</li>
 * <li>mingcheng: 用户名</li>
 * <li>shouji: 手机号码</li>
 * <li>haoma: 身份证号码</li>
 * <li>youxiang: 邮箱</li>
 * <li>companyname: 公司名称</li>
 * <li>orgno: 企业证件号码</li>
 * <li>orgbossname: 法人名称</li>
 * <li>orgbosscareno: 法人证件号码</li>
 * <li>usetype: 用户类型：1(个人用户)2（即是个人用户又是企业用户）</li>
 * <li>code: 功能对应code</li>
 * </ul>
 */
public class ZhenZhiHuiDTO {

    private String id;

    private String mingcheng;

    private String shouji;

    private String haoma;

    private String youxiang;

    private String companyname;

    private String orgno;

    private String orgbossname;

    private String orgbosscareno;

    private Integer usetype;

    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMingcheng() {
        return mingcheng;
    }

    public void setMingcheng(String mingcheng) {
        this.mingcheng = mingcheng;
    }

    public String getShouji() {
        return shouji;
    }

    public void setShouji(String shouji) {
        this.shouji = shouji;
    }

    public String getHaoma() {
        return haoma;
    }

    public void setHaoma(String haoma) {
        this.haoma = haoma;
    }

    public String getYouxiang() {
        return youxiang;
    }

    public void setYouxiang(String youxiang) {
        this.youxiang = youxiang;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getOrgno() {
        return orgno;
    }

    public void setOrgno(String orgno) {
        this.orgno = orgno;
    }

    public String getOrgbossname() {
        return orgbossname;
    }

    public void setOrgbossname(String orgbossname) {
        this.orgbossname = orgbossname;
    }

    public String getOrgbosscareno() {
        return orgbosscareno;
    }

    public void setOrgbosscareno(String orgbosscareno) {
        this.orgbosscareno = orgbosscareno;
    }

    public Integer getUsetype() {
        return usetype;
    }

    public void setUsetype(Integer usetype) {
        this.usetype = usetype;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
