package com.everhomes.rest.common;

import java.io.Serializable;



import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为MORE BUTTON时,用户收藏完，返回首页需要的参数
 * <li>itemLocation: 路径/li>
 * <li>itemGroup: item组名/li>
 * </ul>
 */
public class MoreActionData implements Serializable{

    private static final long serialVersionUID = 9188324335803429261L;
    //{"itemLocation":"/home","itemGroup":"Bizs"}
    private String itemLocation;
    private String itemGroup;

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
