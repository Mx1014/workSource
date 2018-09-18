package com.everhomes.rest.news.open;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 页码</li>
 * <li>keyword: 查询关键字。匹配标题和内容</li>
 * <li>newsTagIds: 文章标签 id (子标签)</li>
 * <li>status: 空—查询所有非 空 — 2- 发 布 状 态 1-草稿状态</li>
 * <li>ownerId: 所属项目 id </li>
 * <li>categoryId: 区分不同的快讯应用</li>
 * </ul>
 */
public class ListOpenNewsCommand {
	private Long pageAnchor;
	private Integer pageSize;
	private String keyword;
	private List<Long> newsTagIds;
	private Byte status;
	private Long ownerId;
	private Long categoryId;
	
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
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public List<Long> getNewsTagIds() {
		return newsTagIds;
	}
	public void setNewsTagIds(List<Long> newsTagIds) {
		this.newsTagIds = newsTagIds;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}
