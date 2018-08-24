package com.everhomes.rest.zhenzhihui;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class ZhenZhiHuiEnterpriseInfoDTO {
    private String     identifyToken;
    private String     enterpriseToken;
    private Long     userId;
    private Integer     identifyType;
    private String     enterpriseName;
    private String     corporationName;
    private Long     id;


    public String getIdentifyToken() {
		return identifyToken;
	}


	public void setIdentifyToken(String identifyToken) {
		this.identifyToken = identifyToken;
	}


	public String getEnterpriseToken() {
		return enterpriseToken;
	}


	public void setEnterpriseToken(String enterpriseToken) {
		this.enterpriseToken = enterpriseToken;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public Integer getIdentifyType() {
		return identifyType;
	}


	public void setIdentifyType(Integer identifyType) {
		this.identifyType = identifyType;
	}


	public String getEnterpriseName() {
		return enterpriseName;
	}


	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}


	public String getCorporationName() {
		return corporationName;
	}


	public void setCorporationName(String corporationName) {
		this.corporationName = corporationName;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

