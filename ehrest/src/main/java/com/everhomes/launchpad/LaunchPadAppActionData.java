package com.everhomes.launchpad;

import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为app点击item需要的参数
 * <li>itemLocation: 获取下一级的item需要的路径</li>
 * <li>layoutName: 下一级使用的layout</li>
 * </ul>
 */
public class LaunchPadAppActionData {
    //"itemLocation":"/home/Pm","layoutName":"PmLayout"} 
    private String itemLocation;
    private String layoutName;
    
    public String getItemLocation() {
        return itemLocation;
    }
    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }
    public String getLayoutName() {
        return layoutName;
    }
    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
