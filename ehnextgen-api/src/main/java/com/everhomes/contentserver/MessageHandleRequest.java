package com.everhomes.contentserver;


import com.everhomes.util.Name;
import com.everhomes.util.StringHelper;

@Name(value = "contentserver.request")
public class MessageHandleRequest extends ContentServerBase {
	private String format;

	private String ext;

	private transient String accessKey;

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
