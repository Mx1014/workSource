// @formatter:off
package com.everhomes.launchpad;

import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.launchpadbase.BulletinsCard;

import java.util.List;

public interface BulletinsHandler {
    String BULLETINS_HANDLER_TYPE = "BulletinsHandlerType-";

    List<BulletinsCard> listBulletinsCards(Long appId, AppContext context, Integer rowCount);

}
