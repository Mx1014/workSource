package com.everhomes.rest.version;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class WithoutCurrentVersionRequestCommand {

	@NotNull
    private String realm;
	
	private String locale;
	
	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
