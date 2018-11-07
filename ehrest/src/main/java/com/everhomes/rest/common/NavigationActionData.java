package com.everhomes.rest.common;

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
 * <li>entityTag: 帖子标签</li>
 * </ul>
 */
public class NavigationActionData implements Serializable{
    private static final long serialVersionUID = -8762650365959360709L;
    //"itemLocation":"/home/Pm","layoutName":"PmLayout","callPhones":"[15875300001,15875300002]","title":"xx"} 
    private String itemLocation;
    private String layoutName;
    @ItemType(String.class)
    private ArrayList<String> callPhones;
    private String title;
    private Long appId;
    private String entityTag;

    private Byte containerType;
    
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

    public ArrayList<String> getCallPhones() {
        return callPhones;
    }
    public void setCallPhones(ArrayList<String> callPhones) {
        this.callPhones = callPhones;
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

    public String getEntityTag() {
        return entityTag;
    }
    public void setEntityTag(String entityTag) {
        this.entityTag = entityTag;
    }

    public Byte getContainerType() {
        return containerType;
    }

    public void setContainerType(Byte containerType) {
        this.containerType = containerType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
