package com.everhomes.rest.developer_account_info;

import java.util.List;

/**
 * 开发者账号信息创建返回的DTO
 * @author huanglm 20180611
 */
public class CreateDeveloperAccountDTO {
	
	/**
	 * 成功创建的bundleId信息
	 */
	private List<String> success ;
	
	/**
	 * 创建失败的bundleId信息
	 */	
	private List<String> failure ;
	
	public CreateDeveloperAccountDTO(){
		
	}
	
	public List<String> getSuccess() {
		return success;
	}

	public void setSuccess(List<String> success) {
		this.success = success;
	}

	public List<String> getFailure() {
		return failure;
	}

	public void setFailure(List<String> failure) {
		this.failure = failure;
	}
	

}
