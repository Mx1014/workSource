// @formatter:off
package com.everhomes.settings;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.app.AppConstants;

public class PaginationConfigHelper {
    public static int getPageSize(ConfigurationProvider configProvider, Integer requestedPageSize) {
        if(requestedPageSize == null) {
            return configProvider.getIntValue("pagination.default.size", AppConstants.PAGINATION_DEFAULT_SIZE);
        }
        
        int maxSize = configProvider.getIntValue("pagination.max.size", AppConstants.PAGINATION_MAX_SIZE);
        if(requestedPageSize.intValue() > maxSize)
            return maxSize;
        
        return requestedPageSize.intValue();
    }
}
