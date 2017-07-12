package com.everhomes.rest.configuration;

public class ServerInfoDTO {
	private String oAuthServer;
	private String payServer;
	
	public String getoAuthServer() {
		return oAuthServer;
	}
	public void setoAuthServer(String oAuthServer) {
		this.oAuthServer = oAuthServer;
	}
	public String getPayServer() {
		return payServer;
	}
	public void setPayServer(String payServer) {
		this.payServer = payServer;
	}

}
