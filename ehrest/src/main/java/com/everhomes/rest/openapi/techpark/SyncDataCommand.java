package com.everhomes.rest.openapi.techpark;

import com.everhomes.util.StringHelper;

public class SyncDataCommand {
    private String appKey;
    
    private Long timestamp;
    
    private Integer nonce;
    
    private String syncState;
    
    private Byte dataType;
    
    private String varDataList;
    
    private String delDataList;
    
    private Byte allFlag;
    
    private Integer nextPage;

    public Byte getAllFlag() {
		return allFlag;
	}

	public void setAllFlag(Byte allFlag) {
		this.allFlag = allFlag;
	}

	public Integer getNextPage() {
		return nextPage;
	}

	public void setNextPage(Integer nextPage) {
		this.nextPage = nextPage;
	}

	public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getNonce() {
        return nonce;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    public String getSyncState() {
        return syncState;
    }

    public void setSyncState(String syncState) {
        this.syncState = syncState;
    }

    public Byte getDataType() {
        return dataType;
    }

    public void setDataType(Byte dataType) {
        this.dataType = dataType;
    }

    public String getVarDataList() {
        return varDataList;
    }

    public void setVarDataList(String varDataList) {
        this.varDataList = varDataList;
    }

    public String getDelDataList() {
        return delDataList;
    }

    public void setDelDataList(String delDataList) {
        this.delDataList = delDataList;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
