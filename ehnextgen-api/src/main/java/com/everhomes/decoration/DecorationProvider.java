package com.everhomes.decoration;

import com.everhomes.server.schema.tables.pojos.EhDecorationSetting;

public interface DecorationProvider {


    Long createDecorationSetting(EhDecorationSetting setting);

    void updateDecorationSetting(EhDecorationSetting setting);

    DecorationSetting getDecorationSetting(Integer namespaceId,Long communityId,String ownerType,Long ownerId);
}
