package com.everhomes.rest.xfyun;

import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>PM_TASK(1): 物业报修</li>
 * <li>HOTLINE(2): 服务热线</li>
 * <li>ENTERPRISE_VISITOR(3): 访客预约</li>
 * <li>PARKING(4): 停车缴费</li>
 * <li>MY_APPLY(10000): 我的工单</li>
 * <li>YOULINYUEXUAN(10001): 悦邻优选</li>
 * </ul>
 *
 */
public enum RouterTypeEnum {

	PM_TASK(1, ServiceModuleConstants.PM_TASK_MODULE, "物业报修"), 
	HOTLINE(2, ServiceModuleConstants.HOTLINE_MODULE, "服务热线"), 
	ENTERPRISE_VISITOR(3, ServiceModuleConstants.ENTERPRISE_VISITOR_MODULE, "访客预约"),
	PARKING(4,ServiceModuleConstants.PARKING_MODULE, "停车缴费"),
	
	//自定义路径
	MY_APPLY(10000, null, "我的工单"), 
	YOULINYUEXUAN(10001, null, "悦邻优选"), 
	NONE(-1,null,"");
	
	// 自定义路径最小的code
	public static final int MIN_SELF_ROUTER_CODE = 10000; 
	
	private Integer code;
	private Long moduleId;
	private String info; 

	private RouterTypeEnum(Integer code, Long moduleId, String info) {
		this.code = code;
		this.moduleId = moduleId;
		this.info = info;
	}

	public String getInfo() {
		return this.info;
	}
	
	public Long getModuleId() {
		return moduleId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Integer getCode() {
		return code;
	}
}
