package com.everhomes.organization;

import com.everhomes.forum.Post;
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;
import com.everhomes.util.StringHelper;

public class OrganizationPostVo {
	private Post post;
	private OrganizationTask task;
	private User user;
	private UserIdentifier userIden;
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public OrganizationTask getTask() {
		return task;
	}
	public void setTask(OrganizationTask task) {
		this.task = task;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public UserIdentifier getUserIden() {
		return userIden;
	}
	public void setUserIden(UserIdentifier userIden) {
		this.userIden = userIden;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
