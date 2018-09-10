package com.everhomes.openapi.mybay;

/**
 * 深圳湾携程人事信息批量更新接口参数类
 * @author huanglm 20180619
 *
 */
public class AuthenticationInfo {
	/**
	 * 人事对接数据序号，用于与response返回结果中的Sequence编号匹配，
	 * 不传则response返回0.(建议用户输入不重复的值，方便返回出错时定位问题。)
	 */
	private Integer Sequence ;
	/**
	 * 人事更新实体 ,不能为空
	 */
	private AuthencationEntity Authentication ;
	
	public Integer getSequence() {
		return Sequence;
	}
	public void setSequence(Integer sequence) {
		Sequence = sequence;
	}
	public AuthencationEntity getAuthentication() {
		return Authentication;
	}
	public void setAuthentication(AuthencationEntity authentication) {
		Authentication = authentication;
	}

	
}
