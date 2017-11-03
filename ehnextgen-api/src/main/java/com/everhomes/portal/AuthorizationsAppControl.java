package com.everhomes.portal;

import com.everhomes.rest.module.ControlTarget;
import com.everhomes.util.StringHelper;

import java.util.List;

public class AuthorizationsAppControl {
    private List<ServiceModuleApp> serviceModuleApps;
    private List<ControlTarget> controlTargets;

    public List<ServiceModuleApp> getServiceModuleApps() {
        return serviceModuleApps;
    }

    public void setServiceModuleApps(List<ServiceModuleApp> serviceModuleApps) {
        this.serviceModuleApps = serviceModuleApps;
    }

    public List<ControlTarget> getControlTargets() {
        return controlTargets;
    }

    public void setControlTargets(List<ControlTarget> controlTargets) {
        this.controlTargets = controlTargets;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
