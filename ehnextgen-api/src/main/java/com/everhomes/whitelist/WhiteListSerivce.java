// @formatter:off
package com.everhomes.whitelist;

import com.everhomes.rest.whitelist.*;

public interface WhiteListSerivce {

    void createWhiteList(CreateWhiteListCommand cmd);

    void batchCreateWhiteList(BatchCreateWhiteListCommand cmd);

    void deleteWhiteList(DeleteWhiteListCommand cmd);

    void batchDeleteWhiteList(BatchDeleteWhiteListCommand cmd);

    void updateWhiteList(UpdateWhiteListCommand cmd);

    WhiteListDTO getWhiteList(GetWhiteListCommand cmd);

    ListWhiteListResponse listWhiteList(ListWhiteListCommand cmd);
}
