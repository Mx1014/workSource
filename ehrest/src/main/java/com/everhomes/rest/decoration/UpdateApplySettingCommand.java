package com.everhomes.rest.decoration;

import java.util.List;

public class UpdateApplySettingCommand {

    private List<UpdateIllustrationCommand> settings;

    public List<UpdateIllustrationCommand> getSettings() {
        return settings;
    }

    public void setSettings(List<UpdateIllustrationCommand> settings) {
        this.settings = settings;
    }
}
