package com.everhomes.rest.contentserver;

import com.everhomes.util.StringHelper;

public class AudioConfig {
	private String format;

	private int bit;

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getBit() {
		return bit;
	}

	public void setBit(int bit) {
		this.bit = bit;
	}
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
