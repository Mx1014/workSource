package com.everhomes.rest.common;

import java.io.Serializable;
import java.util.ArrayList;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为PHONE_CALL时拨电话
 * <li>callPhones: 电话号码列表/li>
 * </ul>
 */
public class PhoneCallActionData implements Serializable{

    private static final long serialVersionUID = -5364366676212368720L;
    //{"callPhones": "[15875301110]"} 
    
    @ItemType(String.class)
    private ArrayList<String> callPhones;

    public ArrayList<String> getCallPhones() {
        return callPhones;
    }

    public void setCallPhones(ArrayList<String> callPhones) {
        this.callPhones = callPhones;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
