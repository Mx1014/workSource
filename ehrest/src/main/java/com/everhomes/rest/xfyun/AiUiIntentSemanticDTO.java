package com.everhomes.rest.xfyun;

import com.everhomes.util.StringHelper;

public class AiUiIntentSemanticDTO {
	
	private String intent;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}
	
}


/*
 * 
 *         "semantic":[
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
 * 
 * 
 * */
