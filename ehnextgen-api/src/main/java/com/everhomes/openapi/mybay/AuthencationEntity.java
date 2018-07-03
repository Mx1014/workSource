package com.everhomes.openapi.mybay;


/**
 * 深圳湾携程人事信息批量更新接口参数类
 * @author huanglm 20180619
 *
 */
public class AuthencationEntity {
	/**
	 * 员工编号
	 */
	private String EmployeeID ;
	/**
	 * 中国大陆籍用户姓名  例：张三 (外籍和港澳台无需传递)
	 */
	private String Name ;
	/**
	 * 手机号码。仅支持11位中国境内手机号码。
	 * 注：手机号码异常，仅手机号该字段不落地保存，其余字段如正常则该条人事数据正常落地
	 */
	private String MobilePhone ;
	/**
	 * 联系邮箱
	 */
	private String Email ;
	/**
	 * 在职情况 (A-在职,I-离职)
	 */
	private String Valid ;
	/**
	 * 部门1  （可在月结账单及结算数据中体现）
	 */
	private String Dept1 ;
	/**
	 * 部门2  （可在月结账单及结算数据中体现）
	 */
	private String Dept2 ;
	/**
	 * 部门3  （可在月结账单及结算数据中体现）
	 */
	private String Dept3 ;
	private String Dept4 ;
	private String Dept5 ;
	private String Dept6 ;
	private String Dept7 ;
	private String Dept8 ;
	private String Dept9 ;
	private String Dept10 ;
	/**
	 * 子账户的名称，由携程提供的对应的账户信息。新用户必须提供
	 */
	private String SubAccountName ;
	/**
	 * 是否发送开通邮件，True=是，False=否；空，默认为False。
	 * 如有开通自动开卡功能的客户，这个字段传"True"，否则将无法收到开卡成功邮件，导致用户无法得知卡号和密码信息。
	 */
	private boolean IsSendEMail ;


	public String getEmployeeID() {
		return EmployeeID;
	}
	public void setEmployeeID(String employeeID) {
		EmployeeID = employeeID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	public String getDept1() {
		return Dept1;
	}
	public void setDept1(String dept1) {
		Dept1 = dept1;
	}
	public String getDept2() {
		return Dept2;
	}
	public void setDept2(String dept2) {
		Dept2 = dept2;
	}
	public String getDept3() {
		return Dept3;
	}
	public void setDept3(String dept3) {
		Dept3 = dept3;
	}
	public String getDept4() {
		return Dept4;
	}
	public void setDept4(String dept4) {
		Dept4 = dept4;
	}
	public String getDept5() {
		return Dept5;
	}
	public void setDept5(String dept5) {
		Dept5 = dept5;
	}
	public String getDept6() {
		return Dept6;
	}
	public void setDept6(String dept6) {
		Dept6 = dept6;
	}
	public String getDept7() {
		return Dept7;
	}
	public void setDept7(String dept7) {
		Dept7 = dept7;
	}
	public String getDept8() {
		return Dept8;
	}
	public void setDept8(String dept8) {
		Dept8 = dept8;
	}
	public String getDept9() {
		return Dept9;
	}
	public void setDept9(String dept9) {
		Dept9 = dept9;
	}
	public String getDept10() {
		return Dept10;
	}
	public void setDept10(String dept10) {
		Dept10 = dept10;
	}
	
	public String getMobilePhone() {
		return MobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		MobilePhone = mobilePhone;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getValid() {
		return Valid;
	}
	public void setValid(String valid) {
		Valid = valid;
	}
	public String getSubAccountName() {
		return SubAccountName;
	}
	public void setSubAccountName(String subAccountName) {
		SubAccountName = subAccountName;
	}	
	
	public boolean isIsSendEMail() {
		return IsSendEMail;
	}
	public void setIsSendEMail(boolean isSendEMail) {
		IsSendEMail = isSendEMail;
	}
		

}
