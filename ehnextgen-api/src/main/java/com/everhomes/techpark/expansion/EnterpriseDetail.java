package com.everhomes.techpark.expansion;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseDetails;

public class EnterpriseDetail extends EhEnterpriseDetails {
	
	private String buildingName;
	
	private String enterpriseName;
	
	private String avatar;
	

	public String getBuildingName() {
		return buildingName;
	}




	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}





	public String getEnterpriseName() {
		return enterpriseName;
	}




	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}




	public String getAvatar() {
		return avatar;
	}




	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}




	public static long getSerialversionuid() {
		return serialVersionUID;
	}








	/**
	 * 
	 */
	private static final long serialVersionUID = 8187469107917304407L;

	
	
}
