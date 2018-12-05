package com.everhomes.rest.xfyun;

import com.everhomes.util.StringHelper;

public class AfterDealRespMsg {
	
	private String Type;
	private String ContentType;
	private String Content;
	

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getContentType() {
		return ContentType;
	}
	public void setContentType(String contentType) {
		ContentType = contentType;
	}
	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

}
/*
 * 
 * 
 *     
    {
    
    "Type":"text",
    
    "ContentType":"Json",
    
    "Content":"eyJzbiI6MiwibHMiOnRydWUsImJnIjowLCJlZCI6MCwid3MiOlt7ImJnIjowLCJjdyI6W3sic2MiOjAsInciOiLvvJ8ifV19XX0="
    
    }
    
 * 
 * */
