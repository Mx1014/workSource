package com.everhomes.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NashornApiService {

    private static Map<String, FlowModuleNashornApiService> apiServices;

    public static FlowModuleNashornApiService getService(String serviceName) {
        return apiServices.get(serviceName);
    }

    @Autowired(required = false)
    public void setApiServices(List<FlowModuleNashornApiService> apiServices) {
        NashornApiService.apiServices = new HashMap<>();
        for (FlowModuleNashornApiService apiService : apiServices) {
            NashornApiService.apiServices.put(apiService.name(), apiService);
        }
    }
}
