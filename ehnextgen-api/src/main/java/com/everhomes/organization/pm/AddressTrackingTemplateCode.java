package com.everhomes.organization.pm;

public interface AddressTrackingTemplateCode {
	
	String SCOPE = "address.tracking";
	
	int ADDRESS_ADD = 1;           					//创建房源
	int ADDRESS_DELETE = 2;        					//删除房源
	int ADDRESS_UPDATE = 3;        					//更新房源
	
	int ADDRESS_MERGE_ARRANGEMENT_ADD = 4;          //创建房源合并计划
	int ADDRESS_SPLIT_ARRANGEMENT_ADD = 5;			//创建房源拆分计划
	int ADDRESS_MERGE_ARRANGEMENT_DELETE = 6;		//删除房源合并计划
	int ADDRESS_SPLIT_ARRANGEMENT_DELETE = 7;		//删除房源拆分计划
	int ADDRESS_MERGE_ARRANGEMENT_UPDATE = 8;		//更新房源合并计划
	int ADDRESS_SPLIT_ARRANGEMENT_UPDATE = 9;		//更新房源拆分计划

	int ADDRESS_AUTHORIZE_PRICE_ADD = 10;			//创建房源授权价
	int ADDRESS_AUTHORIZE_PRICE_DELETE = 11;		//删除房源授权价
	int ADDRESS_AUTHORIZE_PRICE_UPDATE = 12;		//更新房源授权价

	int ADDRESS_RESERVATION_ADD = 13;				//创建房源预定计划
	int ADDRESS_RESERVATION_DELETE = 14;			//删除房源预定计划
	int ADDRESS_RESERVATION_CANCEL = 15;			//取消房源预定计划
	int ADDRESS_RESERVATION_UPDATE = 16;			//更新房源预定计划

}
