// @formatter:off
package com.everhomes.rest.ui.forum;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * <li>contentCategory: 帖子类型</li>
 * <li>publishStatus: 帖子发布状态，{@link com.everhomes.rest.forum.TopicPublishStatus}</li>
 * <li>excludeCategories: 不查询的帖子类型 {@link com.everhomes.rest.category.CategoryConstants}</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListNoticeBySceneCommand {
	
    private String sceneToken;
    
    private Long contentCategory;
    
    private String publishStatus;
    
    @ItemType(Long.class)
    private List<Long> excludeCategories;
    
    private Long pageAnchor;
    
    private Integer pageSize;
    
    public ListNoticeBySceneCommand() {
    }

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }


    public Long getContentCategory() {
		return contentCategory;
	}

	public void setContentCategory(Long contentCategory) {
		this.contentCategory = contentCategory;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }
    
    

    public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}
	
	

	public List<Long> getExcludeCategories() {
		return excludeCategories;
	}

	public void setExcludeCategories(List<Long> excludeCategories) {
		this.excludeCategories = excludeCategories;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
