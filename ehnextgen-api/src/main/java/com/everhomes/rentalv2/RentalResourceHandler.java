package com.everhomes.rentalv2;

import com.everhomes.rest.rentalv2.ListRentalBillsCommand;
import com.everhomes.rest.rentalv2.admin.AddDefaultRuleAdminCommand;
import com.everhomes.rest.rentalv2.admin.QueryDefaultRuleAdminCommand;
import com.everhomes.rest.rentalv2.admin.SearchRentalOrdersCommand;

import javax.servlet.http.HttpServletResponse;

/**
 * @author sw on 2017/12/25.
 */
public interface RentalResourceHandler {

    String RENTAL_RESOURCE_HANDLER_PREFIX = "RentalResourceHandler-";
    String DEFAULT = "default";
    RentalResource getRentalResourceById(Long id);

    void updateRentalResource(String resourceJson);

    void buildDefaultRule(AddDefaultRuleAdminCommand addCmd);

    void setRuleOwnerTypeByResource(QueryDefaultRuleAdminCommand queryRuleCmd, RentalResource resource);

    default void exportRentalBills(ListRentalBillsCommand cmd, HttpServletResponse response){}

    default void exportRentalBills(SearchRentalOrdersCommand cmd, HttpServletResponse response){}
}
