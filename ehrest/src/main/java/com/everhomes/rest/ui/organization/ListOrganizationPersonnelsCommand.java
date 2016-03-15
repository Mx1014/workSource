// @formatter:off
package com.everhomes.rest.ui.organization;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 *  <li>keywords : 关键词</li>
 * 	<li>pageAnchor : 页码</li>
 *	<li>pageSize : 页大小</li>
 * </ul>
 *
 */
public class ListOrganizationPersonnelsCommand {
	
	private String sceneToken;
	
	private String keywords;
	
	private Long pageAnchor; 
	
	private Integer pageSize;

	public String getSceneToken() {
		return sceneToken;
	}



	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}



	public Long getPageAnchor() {
		return pageAnchor;
	}



	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}



	public Integer getPageSize() {
		return pageSize;
	}



	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}



	public String getKeywords() {
		return keywords;
	}



	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
