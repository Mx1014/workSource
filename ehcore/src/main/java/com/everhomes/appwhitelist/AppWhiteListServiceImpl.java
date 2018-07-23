// @formatter:off
package com.everhomes.appwhitelist;

import com.everhomes.rest.appwhitelist.AppWhiteListDTO;
import com.everhomes.rest.appwhitelist.ListAppWhiteListResponse;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class AppWhiteListServiceImpl implements AppWhiteListService{

    @Autowired
    private AppWhiteListProvider appWhiteListProvider;

    @Override
    public ListAppWhiteListResponse listAppWhiteList() {
        ListAppWhiteListResponse response = new ListAppWhiteListResponse();
        List<AppWhiteList> apps = this.appWhiteListProvider.listAppWhiteList();
        List<AppWhiteListDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(apps)) {
            apps.stream().forEach(r -> {
                AppWhiteListDTO appWhiteListDTO = ConvertHelper.convert(r,AppWhiteListDTO.class);
                dtos.add(appWhiteListDTO);
            });
        }
        response.setDtos(dtos);
        return response;
    }
}
