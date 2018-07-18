package com.everhomes.scriptengine.nashorn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NashornApiService {

    private static Map<String, NashornModuleApiService> apiServices;

    public static NashornModuleApiService getService(String serviceName) {
        return apiServices.get(serviceName);
    }

    @Autowired(required = false)
    public void setApiServices(List<NashornModuleApiService> apiServices) {
        NashornApiService.apiServices = new HashMap<>();
        for (NashornModuleApiService apiService : apiServices) {
            NashornApiService.apiServices.put(apiService.name(), apiService);
        }
    }
}
