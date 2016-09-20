package com.everhomes.rest.organization.pm;

/**
 * organization owner的string模板scope
 * Created by xq.tian on 2016/9/5.
 */
public interface OrganizationOwnerLocaleStringScope {

    /*----------  业主行为类型  --------*/

    String BEHAVIOR_SCOPE = "orgOwnerBehavior";


    /*----------  认证状态  --------*/

    String AUTH_TYPE_SCOPE = "orgOwnerAddressAuthType";


    /*----------  在户状态  --------*/

    String LIVING_STATUS_SCOPE = "orgOwnerAddressLivingStatus";

    /*----------  车辆停车类型  --------*/

    String PARKING_TYPE_SCOPE = "parkingType";

    /*----------  车辆用户的首要联系人标志  --------*/

    String PRIMARY_FLAG_SCOPE = "ownerCarPrimaryFlag";

}
