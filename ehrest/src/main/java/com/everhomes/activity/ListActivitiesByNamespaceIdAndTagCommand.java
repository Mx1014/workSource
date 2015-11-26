package com.everhomes.activity;

import com.everhomes.util.StringHelper;
/**
 * 
 *<ul>
 *<li>tag:活动标签</li>
 *<li>namespaceId: 定制版本ID</li>
 *</ul>
 */
public class ListActivitiesByNamespaceIdAndTagCommand {
	
    private String tag;
    
    private Integer namespaceId;
    
    private Long anchor;
    
   
    public String getTag() {
		return tag;
	}


	public void setTag(String tag) {
		this.tag = tag;
	}




	public Integer getNamespaceId() {
		return namespaceId;
	}


	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}


	public Long getAnchor() {
		return anchor;
	}


	public void setAnchor(Long anchor) {
		this.anchor = anchor;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
