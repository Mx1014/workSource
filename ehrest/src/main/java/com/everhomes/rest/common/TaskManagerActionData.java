package com.everhomes.rest.common;

import java.io.Serializable;



import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为MANAGER_TASK 获取权限跳转入口
 * <li>module: 功能模块</li>
 * </ul>
 */
public class TaskManagerActionData implements Serializable{
    private static final long serialVersionUID = 3538472620854484985L;
    
    private String module;
    
    

    public String getModule() {
		return module;
	}



	public void setModule(String module) {
		this.module = module;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
