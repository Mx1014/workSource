//@formatter:off
package com.everhomes.asset;


import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wentian Wang on 2018/5/9.
 */

public class AssetAppNoticePak {
    private Long uid;
    private Integer templateId;
    private Map<String, String> varibles;
    private Boolean useTemplate;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Boolean getUseTemplate() {
        return useTemplate;
    }

    public void setUseTemplate(Boolean useTemplate) {
        this.useTemplate = useTemplate;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public void setVaribles(Map<String, String> varibles) {
        this.varibles = varibles;
    }

    public Map<String, String> getVaribles() {
        return varibles;
    }

    public void addToVariables(String key, String val) {
        if(this.varibles == null){
            this.varibles = new HashMap<>();
        }
        this.varibles.put(key, val);
    }
}
