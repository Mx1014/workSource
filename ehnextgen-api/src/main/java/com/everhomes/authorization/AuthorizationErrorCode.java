// @formatter:off
package com.everhomes.authorization;


public class AuthorizationErrorCode {
	public static final String SCOPE = "third.party.authorization";
	
	public static final String WORK_FLOW_TITLE = "work_flow_title";//姓名 : |证件号码 : |个人认证申请|组织机构代码 : |企业联系人 : |企业认证申请
	public static final String PERSONAL_BACK_CODE_DETAIL = "personal_back_code_detail";//很遗憾，您的认证未成功：|1、请检测信息填写是否正确，如填写有误，请重新提交|2、如填写无误，请到携带相关证件管理处核对信息|您已退租，如有疑问请联系客服|恭喜您，成为我们的一员，您承租的地址信息如下：|未定义的返回码|园区[|]不存在|园区[|]名称存在多个
	public static final String ORGANIZATION_BACK_CODE_DETAIL = "organization_back_code_detail";//提交信息匹配不成功：|1、请检测信息填写是否正确，如填写有误，请重新提交|2、如填写无误，请到携带相关证件管理处核对信息|您已退租，如有疑问请联系客服|认证成功！您承租地址信息如下：|未定义的返回码|园区[|]不存在|园区[|]名称存在多个
	public static final String WORK_FLOW_CONTENT = "work_flow_content";//组织机构代码|企业联系人|企业联系电话|认证反馈结果|住址|手机号|姓名|证件类型|证件号码|认证反馈结果|住址|
	public static final String FAMILY_DETAIL = "family_detail";//家庭信息|您还未加入任何家庭，快去加入吧!|添加住址
	
	public static final String WORK_FLOW_TITLE_S = "姓名 : |证件号码 : |个人认证申请|组织机构代码 : |企业联系人 : |企业认证申请";
	public static final String PERSONAL_BACK_CODE_DETAIL_S = "很遗憾，您的认证未成功|1、请检测信息填写是否正确，如填写有误，请重新提交|2、如填写无误，请到携带相关证件管理处核对信息|您已退租，如有疑问请联系客服|恭喜您，成为我们的一员，您承租的地址信息如下|未定义的返回码|园区[|]不存在|园区[|]名称存在多个|地址|请求认证失败！|系统认证异常，已退出家庭";
	public static final String ORGANIZATION_BACK_CODE_DETAIL_S = "提交信息匹配不成功|1、请检测信息填写是否正确，如填写有误，请重新提交|2、如填写无误，请到携带相关证件管理处核对信息|您已退租，如有疑问请联系客服|认证成功！您承租地址信息如下|未定义的返回码|园区[|]不存在|园区[|]名称存在多个|地址|请求认证失败！|系统认证异常，已退出公司";
	public static final String WORK_FLOW_CONTENT_S = "组织机构代码|企业联系人|企业联系电话|认证反馈结果|手机号|姓名|证件类型|证件号码|认证反馈结果|身份证|未知证件类型";
	public static final String FAMILY_DETAIL_S = "家庭信息|您还未加入任何家庭，快去加入吧!|添加住址";
	
}
