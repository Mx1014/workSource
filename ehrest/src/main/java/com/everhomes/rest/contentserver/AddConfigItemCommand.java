package com.everhomes.rest.contentserver;

import java.util.Map;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class AddConfigItemCommand {
    @NotNull
    private String configName;

    @NotNull
    private String configType;

    // include imageconfig, audioconfig,videoconfig
    @ItemType(String.class)
    private Map<String, String> configProps;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public Map<String, String> getConfigProps() {
        return configProps;
    }

    public void setConfigProps(Map<String, String> configProps) {
        this.configProps = configProps;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    

}
