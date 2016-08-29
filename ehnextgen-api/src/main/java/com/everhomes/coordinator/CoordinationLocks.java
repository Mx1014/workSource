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
    STAT_SETTLEMENT("stat.settlement"),
    UPDATE_ACTIVITY("activity.update");
    

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
