package com.everhomes.buttscript.engine;

import com.everhomes.bus.LocalEvent;
import com.everhomes.rest.user.UserDTO;
import com.everhomes.util.StringHelper;

/**
 * 入参类,用一个对象来封闭所有脚本方法所需的入参;
 * 此法是为了方便扩展,不必为以后增加入参个数后要修改以前的script脚本
 */
public class ButtScriptParameter {
    /**
     * 事件的触发者
     */
    private UserDTO operator;
    /**
     * 事件对象本身
     */
    private LocalEvent event ;

    public UserDTO getOperator() {
        return operator;
    }

    public void setOperator(UserDTO operator) {
        this.operator = operator;
    }

    public LocalEvent getEvent() {
        return event;
    }

    public void setEvent(LocalEvent event) {
        this.event = event;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
