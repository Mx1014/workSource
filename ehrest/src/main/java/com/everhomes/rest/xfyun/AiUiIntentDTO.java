package com.everhomes.rest.xfyun;

import java.util.List;

import com.everhomes.util.StringHelper;

public class AiUiIntentDTO {

	private String category;
	private Integer rc;
	private String service;
	private String vendor;
	private String version;
	private List<AiUiIntentSemanticDTO> semantic;
	private Boolean shouldEndSession;
	private String sid;
	private String text;
	private String firstIntent; //semantic数组中首个intent标识
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getRc() {
		return rc;
	}
	public void setRc(Integer rc) {
		this.rc = rc;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Boolean getShouldEndSession() {
		return shouldEndSession;
	}
	public void setShouldEndSession(Boolean shouldEndSession) {
		this.shouldEndSession = shouldEndSession;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<AiUiIntentSemanticDTO> getSemantic() {
		return semantic;
	}
	public void setSemantic(List<AiUiIntentSemanticDTO> semantic) {
		this.semantic = semantic;
	}
	public String getFirstIntent() {
		return firstIntent;
	}
	public void setFirstIntent(String firstIntent) {
		this.firstIntent = firstIntent;
	}	
	
}


/*

{
    "intent":{
        "category":"ZUOLINHMB.alliance",
        "intentType":"custom",
        "rc":0,
        "semanticType":0,
        "service":"ZUOLINHMB.alliance",
        "uuid":"cida1157f3b@dx009a0f5da3c601000c",
        "vendor":"ZUOLINHMB",
        "version":"1.0",
        "semantic":[
            {
                "entrypoint":"ent",
                "hazard":false,
                "intent":"faq",
                "score":1,
                "slots":[

                ],
                "template":"服务联盟"
            }
        ],
        "state":null,
        "data":{
            "result":null
        },
        "answer":{
            "text":"请问是哪个域空间",
            "type":"T"
        },
        "shouldEndSession":false,
        "sid":"cida1157f3b@dx009a0f5da3c601000c",
        "text":"服务联盟"
    }
}



*/