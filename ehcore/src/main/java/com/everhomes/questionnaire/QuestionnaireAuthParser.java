//@formatter:off
package com.everhomes.questionnaire;

import com.everhomes.portal.PortalUrlParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class QuestionnaireAuthParser implements PortalUrlParser {
    @Override
    public Long getModuleId(Integer namespaceId, String actionData, Byte actionType, String itemLabel) {
        if(actionType == 13 || actionType == 14){
            Map<String,String> data= new Gson().fromJson(actionData,new TypeToken<HashMap<String,String>>(){}.getType());
            if(data!=null && data.size() > 0 && data.get("url")!=null){
                String wd = data.get("url");
                if(wd.contains("/questionnaire-survey/build/index.htm"))
                    return 41700L;
            }
        }
        return null;
    }
}
