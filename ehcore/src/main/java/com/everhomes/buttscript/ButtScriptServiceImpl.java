package com.everhomes.buttscript;

import com.everhomes.configurations.ConfigurationsAdminProviderImpl;
import com.everhomes.rest.buttscript.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ButtScriptServiceImpl implements ButtScriptService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ButtScriptServiceImpl.class);

    @Override
    public ScriptDTO getScriptByNamespace(GetScriptCommand cmd) {
        return null;
    }

    @Override
    public ScriptVersionInfoDTO saveScript(SaveScriptCommand cmd) {
        return null;
    }

    @Override
    public ScriptVersionInfoResponse findScriptVersionInfoByNamespaceId(FindScriptVersionInfoCommand cmd) {
        return null;
    }

    @Override
    public void publishScriptVersion(PublishScriptVersionCommand cmd) {

    }

    @Override
    public ScriptnInfoTypeResponse findScriptInfoType(FindScriptInfoTypeCommand cmd) {
        return null;
    }

    @Override
    public void publishScriptVersionCancel(PublishScriptVersionCancelCommand cmd) {

    }
}
