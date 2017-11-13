package com.everhomes.rest.equipment;


/**
 * <ul>
 *  <li>id: 巡检计划id</li>
 *  <li>ownerId: 计划所属的主体id</li>
 *  <li>ownerType: 计划所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 * </ul>
 */
public class DeleteEquipmentPlanCommand {
    private  Long  id;

    private  Long  ownerId;
}
