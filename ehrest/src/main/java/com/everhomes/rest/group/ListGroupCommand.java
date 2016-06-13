package com.everhomes.rest.group;


/**
 *<ul>
 *<li>pageAnchor:锚点</li>
 *<li>pageSize:每页的个数</li>
 *<li>categoryId:group的类型id</li>
 *<li>keyWord:关键字</li>
 *</ul>
 */
public class ListGroupCommand {

	private Long pageAnchor;
    
    private Integer pageSize;
    
    private Long categoryId;
    
    private String keyWord;


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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
    
    
}
