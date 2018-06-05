package com.everhomes.rest.contract;

public interface ContractTrackingTemplateCode {
	String SCOPE = "contract.tracking";
	
	int ADD = 1;        //新增
    int DELETE = 2; 	//删除
    int UPDATE = 3;    //更改
    
    String MODULE_NAME = "contract";
    String GROUP_PATH = "/13";

}
