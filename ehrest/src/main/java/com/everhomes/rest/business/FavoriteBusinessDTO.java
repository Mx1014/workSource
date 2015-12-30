package com.everhomes.rest.business;

/**
 * 
 *	<ul>
 *		<li>id : business's id</li>
 *		<li>favoriteFlag : favorite or not ,0-取消收藏,1-收藏,详情{@link com.everhomes.rest.business.FavoriteFlagType}</li>
 *	</ul>
 *
 */
public class FavoriteBusinessDTO {
	
	private Long id;
	private Byte favoriteFlag;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Byte getFavoriteFlag() {
		return favoriteFlag;
	}
	public void setFavoriteFlag(Byte favoriteFlag) {
		this.favoriteFlag = favoriteFlag;
	}
	

}
