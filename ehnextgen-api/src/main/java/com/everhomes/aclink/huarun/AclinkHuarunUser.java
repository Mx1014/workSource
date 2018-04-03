package com.everhomes.aclink.huarun;

import com.everhomes.util.StringHelper;

public class AclinkHuarunUser {
	private String name;
	private String phone;
	private String sex;
	private String officetel;
	private Long certtype;
	private String certno;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getOfficetel() {
		return officetel;
	}
	public void setOfficetel(String officetel) {
		this.officetel = officetel;
	}
	public Long getCerttype() {
		return certtype;
	}
	public void setCerttype(Long certtype) {
		this.certtype = certtype;
	}
	public String getCertno() {
		return certno;
	}
	public void setCertno(String certno) {
		this.certno = certno;
	}
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
