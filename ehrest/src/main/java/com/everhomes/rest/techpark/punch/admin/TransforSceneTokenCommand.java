package com.everhomes.rest.techpark.punch.admin;

public class TransforSceneTokenCommand {
	private Long userId;
	private String sceneToken;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}
}
