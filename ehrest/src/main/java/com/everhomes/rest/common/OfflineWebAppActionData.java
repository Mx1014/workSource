package com.everhomes.rest.common;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

public class OfflineWebAppActionData implements Serializable {

	private static final long serialVersionUID = -7724235725672221440L;

	// {"realm":"quality","entryUrl":"http://zuolin.com/nar/quality/index.html"}
    private String realm;
    private String entryUrl;
    

    public String getRealm() {
		return realm;
	}


	public void setRealm(String realm) {
		this.realm = realm;
	}


	public String getEntryUrl() {
		return entryUrl;
	}


	public void setEntryUrl(String entryUrl) {
		this.entryUrl = entryUrl;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
