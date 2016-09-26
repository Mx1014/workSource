package com.everhomes.wx;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <p>从微信获取到的用户信息格式</p>
 * <ul>结果正确时，微信回来的报文格式：{@link https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140842&token=&lang=zh_CN} 第四步：拉取用户信息(需scope为 snsapi_userinfo)
 * <li>openid: 用户的唯一标识</li>
 * <li>nickname: 用户昵称</li>
 * <li>sex: 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知</li>
 * <li>province: 用户个人资料填写的省份</li>
 * <li>city: 普通用户个人资料填写的城市</li>
 * <li>country: 国家，如中国为CN</li>
 * <li>headimgurl: 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。</li>
 * <li>privilege: 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）</li>
 * <li>unionid 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。</li>
 * </ul>
 * <p />
 * <ul>结果错误时，微信回来的报文格式：{@link https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140842&token=&lang=zh_CN} 第四步：拉取用户信息(需scope为 snsapi_userinfo)
 * <li>errcode: 错误码</li>
 * <li>errmsg: 错误描述</li>
 * </ul>
 */
public class WxUserInfo {
    private Integer errcode;
    private String errmsg;
	private String openid;
	private String nickname;
	private Byte sex;
	private String country;
	private String province;
	private String city;
	private String language;
	private String headimgurl;
	private List<String> privilege;
	private String unionid;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public List<String> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<String> privilege) {
        this.privilege = privilege;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
