// @formatter:off

package com.everhomes.rest.news;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数 
 * <li>newsToken: 新闻标识</li>
 * <li>likeFlag: 现在的点赞状态，0未点赞，1不喜欢，2已点赞，参考{@link com.everhomes.rest.user.UserLikeType}</li>
 * </ul>
 */
public class SetNewsLikeFlagForWebCommand {
 
	@NotNull
	private String newsToken;
	@NotNull
	private Byte likeFlag;
 

	public String getNewsToken() {
		return newsToken;
	}

	public void setNewsToken(String newsToken) {
		this.newsToken = newsToken;
	}

	public Byte getLikeFlag() {
		return likeFlag;
	}

	public void setLikeFlag(Byte likeFlag) {
		this.likeFlag = likeFlag;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}