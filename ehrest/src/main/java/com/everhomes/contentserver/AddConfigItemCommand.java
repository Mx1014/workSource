package com.everhomes.contentserver;

import javax.validation.constraints.NotNull;

public class AddConfigItemCommand {
    @NotNull
    private String configName;

    @NotNull
    private String configType;

    // include imageconfig, audioconfig,videoconfig
    private Object configProps;

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

    public Object getConfigProps() {
        return configProps;
    }

    public void setConfigProps(Object configProps) {
        this.configProps = configProps;
    }

}
