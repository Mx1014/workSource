package com.everhomes.videoconf;

import com.everhomes.server.schema.tables.pojos.EhConfSourceAccounts;
import com.everhomes.util.StringHelper;

public class ConfSourceAccounts extends EhConfSourceAccounts {

	private static final long serialVersionUID = -2819639405827609004L;
	
	private Long occupyAccountId;
	
	private Byte occupyFlag;
	
	private Long confId;
	
	public Long getOccupyAccountId() {
		return occupyAccountId;
	}

	public void setOccupyAccountId(Long occupyAccountId) {
		this.occupyAccountId = occupyAccountId;
	}

	public Byte getOccupyFlag() {
		return occupyFlag;
	}

	public void setOccupyFlag(Byte occupyFlag) {
		this.occupyFlag = occupyFlag;
	}

	public Long getConfId() {
		return confId;
	}

	public void setConfId(Long confId) {
		this.confId = confId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
