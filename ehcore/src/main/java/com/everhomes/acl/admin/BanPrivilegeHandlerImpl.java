package com.everhomes.acl.admin;

import com.everhomes.acl.BanPrivilegeHandler;
import com.everhomes.module.ServiceModuleExcludeFunction;
import com.everhomes.module.ServiceModuleFunction;
import com.everhomes.module.ServiceModuleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2018/1/2.
 */
@Component(BanPrivilegeHandler.BAN_PRIVILEGE_OBJECT_PREFIX)
public class BanPrivilegeHandlerImpl implements BanPrivilegeHandler {

    @Autowired
    private ServiceModuleProvider serviceModuleProvider;

    @Override
    public List<Long> listBanPrivilegesByModuleIdAndAppId(Integer namespaceId, Long module, Long appId) {
        List<Long> functionIds = new ArrayList<>();
        List<ServiceModuleExcludeFunction> excludeFunctions = serviceModuleProvider.listExcludeFunctions(namespaceId, null, module);
        if (excludeFunctions != null && excludeFunctions.size() > 0) {
            excludeFunctions.forEach(excludeFunction -> {
                functionIds.add(excludeFunction.getFunctionId());
            });
        }
        List<ServiceModuleFunction> functions = serviceModuleProvider.listFunctionsByIds(functionIds);
        if(functions != null && functions.size() > 0) {
            return functions.stream().map(function -> {
                return function.getPrivilegeId();
            }).collect(Collectors.toList());
        }

        return null;
    }
}
