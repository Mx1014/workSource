package com.everhomes.rest.news;

public enum NewsContentType {
	RICH_TEXT("rich text");
	private String code;
	private NewsContentType(String code) {
		this.code = code;
	}
	public String getCode(){
		return this.code;
	}
	public NewsContentType fromCode(String code){
		if (this.code == null) {
			return null;
		}
		
		for (NewsContentType type : NewsContentType.values()) {
			if (code.equalsIgnoreCase(type.code)) {
				return type;
			}
		}
		
		return null;
	}
}
