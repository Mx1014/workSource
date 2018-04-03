// @formatter:off
package com.everhomes.talent;

public interface TalentServiceErrorCode {
	String SCOPE = "talent";
	int NOT_ADMIN = 1;
	int DUPLICATED_NAME = 2;  //分类名称不能重复
	int ERROR_EXCEL = 3;  //导入失败，请检查数据是否按要求填写
	
}
