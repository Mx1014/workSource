package com.everhomes.rest.category;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 	<li>id : category的Id</li>
 * 	<li>logoUri : logoUri</li>
 * </ul>
 *
 */
public class UpdateCategoryLogoUriCommand {
	@NotNull
	private Long id;
	private String logoUri;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLogoUri() {
		return logoUri;
	}
	public void setLogoUri(String logoUri) {
		this.logoUri = logoUri;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
