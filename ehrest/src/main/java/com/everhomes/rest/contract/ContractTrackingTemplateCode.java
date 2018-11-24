package com.everhomes.rest.contract;

public interface ContractTrackingTemplateCode {
	String SCOPE = "contract.tracking";
	
	int CONTRACT_ADD = 1;        //合同新增
    int CONTRACT_DELETE = 2; 	//合同删除
    int CONTRACT_UPDATE = 3;    //合同更改
    int APARTMENT_ADD = 4;		//合同资产新增
    int APARTMENT_DELETE = 5;	//合同资产删除
    int CHARGING_ITEM_ADD = 6;	//合同计价条款新增
    int CHARGING_ITEM_DELETE = 7;	//合同计价条款删除
    int CHARGING_ITEM_UPDATE = 8;	//合同计价条款更改
    int ATTACHMENT_ADD = 9;     //合同附件新增
    int ATTACHMENT_DELETE = 10; //合同附件删除
    int ADJUST_ADD = 11;	//调租计划新增
    int ADJUST_DELETE = 12;	//调租计划删除
    int ADJUST_UPDATE = 13;	//调租计划修改
    int FREE_ADD = 14;	//免租计划新增
    int FREE_DELETE = 15;	//免租计划删除
    int FREE_UPDATE = 16;	//免租计划修改
    
    String MODULE_NAME = "contract";
    String GROUP_PATH = "/13";
    
    int CONTRACT_RENEW = 17;//合同续约
	int CONTRACT_CHANGE = 18;//合同变更
	int APARTMENT_UPDATE = 19;//资产更改事件 

	int CONTRACT_COPY = 20;        //合同复制
	int CONTRACT_INITIALIZE = 21;  //合同初始化
	int CONTRACT_EXEMPTION = 22;   //合同免批
}
