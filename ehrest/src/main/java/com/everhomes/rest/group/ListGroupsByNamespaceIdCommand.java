package com.everhomes.rest.group;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>pageAnchor: pageAnchor</li>
 *     <li>pageSize: pageSize</li>
 *     <li>communityId: communityId</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>privateFlag: privateFlag</li>
 *     <li>keywords: keywords</li>
 *     <li>categoryId: categoryId</li>
 *     <li>clubType: clubType  NORMAL-0, Guild(行业协会)-1 参考{@link ClubType}</li>
 * </ul>
 */
public class ListGroupsByNamespaceIdCommand {
	private Long pageAnchor;

	private Integer pageSize;

	private Long communityId;

	@NotNull
	private Integer namespaceId;

	private Byte privateFlag;

	private String keywords;

	private Long categoryId;

	private Byte clubType;

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

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Byte getPrivateFlag() {
		return privateFlag;
	}

	public void setPrivateFlag(Byte privateFlag) {
		this.privateFlag = privateFlag;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Byte getClubType() {
		return clubType;
	}

	public void setClubType(Byte clubType) {
		this.clubType = clubType;
	}
}
