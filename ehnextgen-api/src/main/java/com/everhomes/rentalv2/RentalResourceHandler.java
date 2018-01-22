package com.everhomes.rentalv2;

import com.everhomes.rest.rentalv2.admin.QueryDefaultRuleAdminCommand;

/**
 * @author sw on 2017/12/25.
 */
public interface RentalResourceHandler {

    String RENTAL_RESOURCE_HANDLER_PREFIX = "RentalResourceHandler-";
    String DEFAULT = "default";
    RentalResource getRentalResourceById(Long id);

    void updateRentalResource(String resourceJson);

    void setRuleOwnerTypeByResource(QueryDefaultRuleAdminCommand queryRuleCmd, RentalResource resource);
}
