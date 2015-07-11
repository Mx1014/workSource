package com.everhomes.launchpad;

import java.io.Serializable;
import java.util.ArrayList;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为app点击item需要的参数
 * <li>itemLocation: 获取下一级的item需要的路径</li>
 * <li>layoutName: 下一级使用的layout</li>
 * <li>cellPhones: 下一级使用的电话列表</li>
 * <li>title: 下一级电话列表前面的显示标题</li>
 * </ul>
 */
public class LaunchPadAppActionData implements Serializable{
    private static final long serialVersionUID = -8762650365959360709L;
    //"itemLocation":"/home/Pm","layoutName":"PmLayout","cellPhones":"[15875300001,15875300002]","title":"xx"} 
    private String itemLocation;
    private String layoutName;
    @ItemType(String.class)
    private ArrayList<String> cellPhones;
    private String title;
    private Long appId;
    private String postEntityTag;
    
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

    public ArrayList<String> getCellPhones() {
        return cellPhones;
    }
    public void setCellPhones(ArrayList<String> cellPhones) {
        this.cellPhones = cellPhones;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Long getAppId() {
        return appId;
    }
    public void setAppId(Long appId) {
        this.appId = appId;
    }
    public String getPostEntityTag() {
        return postEntityTag;
    }
    public void setPostEntityTag(String postEntityTag) {
        this.postEntityTag = postEntityTag;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
