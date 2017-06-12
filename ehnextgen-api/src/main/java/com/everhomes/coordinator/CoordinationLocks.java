// @formatter:off
package com.everhomes.coordinator;

public enum CoordinationLocks {
    CREATE_FAMILY("family.create"),
    LEAVE_FAMILY("family.leave"),
    CREATE_ADDRESS("address.create"),
    CREATE_RESOURCE("resource.create"),
    SETUP_ACCOUNT_NAME("account.name.setup"),
    UPDATE_POST("post.update"),
    UPDATE_GROUP("group.update"),
    UPDATE_PM_ORDER("pm.order.update"),
    CREATE_O_BILL_ACCOUNT("org.bill.account.create"),
    CREATE_F_BILL_ACCOUNT("family.bill.account.create"),
    CREATE_RENTAL_BILL("techpark.rental.bill.create"),
    CREATE_PUNCH_LOG("techpark.punch.log.create"),
    DOOR_ACCESS("aclink.door.access"),
    SCHEDULE_QUALITY_TASK("quality.task.schedule"),
    CREATE_QUALITY_TASK("quality.task.create"),
    PAYMENT_CARD("payment.card"),
	UPDATE_NEWS("news.update"),
	SCHEDULE_EQUIPMENT_TASK("equipment.task.schedule"),
    CREATE_EQUIPMENT_TASK("equipment.task.create"),
	WANKE_LOGIN("wanke.login"),
    
    UPDATE_ACTIVITY("activity.update"),
    UPDATE_ACTIVITY_ROSTER("activity.update.roster"),
    CREATE_NEW_ORG("org.create"),
	UPDATE_APPROVAL_CATEGORY("update_approval_category"),
	UPDATE_APPROVAL_RULE("update_approval_rule"),
	UPDATE_APPROVAL_FLOW("update_approval_flow"),
	UPDATE_APPROVAL_REQUEST("update_approval_request"),

	UPDATE_APPROVAL_TARGET_RULE("update_approval_target_rule"),
    STAT_SETTLEMENT("stat.settlement"),
    STAT_TERMINAL("stat.terminal"),

    UPDATE_ORGANIZATION_OWNER("organization.owner.update"),
    UPDATE_ORGANIZATION_OWNER_CAR("organization.owner.car.update"),
    
    WARNING_ACTIVITY_SCHEDULE("warning.activity.schedule"),
    CONTRACT_SCHEDULE("contract.schedule"),

    PMTASK_STATISTICS("pmtask.statistics"),
    PMTASK_TARGET_STATISTICS("pmtask.target.statistics"),
    
    PARKING_STATISTICS("parking.statistics"),
    PARKING_UPDATE_ORDER_STATUS("parking.update.order.status"),

    PARKING_CLEARANCE_LOG("parking.clearance.log"),
    PARKING_CLEARANCE_OPERATOR("parking.clearance.operator"),

    ENERGY_DAY_STAT_SCHEDULE("energy.day.stat.schedule"),
    ENERGY_MONTH_STAT_SCHEDULE("energy.month.stat.schedule"),
    ENERGY_METER("energyMeter"),
    ENERGY_METER_CATEGORY("energyMeter.category"),
    ENERGY_METER_FORMULA("energyMeter.formula"),

    UPDATE_ASSET_BILL_TEMPLATE("assetBillTemplate.update"),

    UPDATE_QUESTIONNAIRE("update.questionnaire"),
	UPDATE_QUESTIONNAIRE_OPTION("update.questionnaire.option"),

    WARNING_EQUIPMENT_TASK("warning.equipment.task"),
    WARNING_QUALITY_TASK("warning.quality.task"),

    OS_OBJECT("os.object"),
    ENERGY_METER_PRICE_CONFIG("energyMeter.price.config"),
 
    UPDATE_EXPRESS_ORDER("update.express.order"),

    USER_NOTIFICATION_SETTING("user.notification.setting"),
    UPDATE_WAREHOUSE("warehouse.update"),
    UPDATE_WAREHOUSE_CATEGORY("warehouse.category.update"),
    UPDATE_WAREHOUSE_MATERIAL("warehouse.material.update"),

    FLOW_CASE_UPDATE("flowCase.update"),
    ;

    private String code;
    private CoordinationLocks(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static CoordinationLocks fromCode(String code) {
        for(CoordinationLocks coordinationLock:CoordinationLocks.values()){
            if(coordinationLock.code.equalsIgnoreCase(code)){
                return coordinationLock;
            }
        }
        return null;
    }
}
