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
    UPDATE_PUNCH_MONTH_REPORT("punch.month.report.update"),
    VACATION_BALANCE_UPDATE("vacation.balance.update"),
    REFRESH_PUNCH_RULE("techpark.punch.rule.refresh"),
    DOOR_ACCESS("aclink.door.access"),
    SCHEDULE_QUALITY_TASK("quality.task.schedule"),
    SCHEDULE_QUALITY_STAT("quality.task.stat"),
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
    INIT_APPROVAL_CATEGORY("init_approval_category"),
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
    PARKING_GENERATE_ORDER_NO("parking.generate.order.no"),

    PARKING_CLEARANCE_LOG("parking.clearance.log"),
    PARKING_CLEARANCE_LOG_STATISTICS("parking.clearance.log.statistics"),
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

    FILE_CONTENT_CHECK("file.content.check"),

    FLOW_CASE_UPDATE("flowCase.update"),
    FLOW_NODE_UPDATE("flowNode.update"),
    PUNCH_DAY_SCHEDULE("punch.day.schedule"),

    PRINT_ORDER_LOCK_FLAG("print.order.lock.flag"),
    
    OFFICE_CUBICLE_CITY_LOCK("office.cubicle.city.lock"),
	
    PRINT_UPDATE_ORDER_STATUS("print.update.order.status"),

    USER_APPEAL_LOG("user.appeal.log"),
	SALARY_GROUP_LOCK("salary.group"),
	SALARY_NEWMONTH_LOCK("salary.newmonth"),
    UNION_GROUP_LOCK("union.group"),
    UNION_GROUP_CLONE_LOCK("union_group_clone"),

    PAY_CREATE_PREORDER("pay.create.preorder"),

    BILL_STATUS_UPDATE("bill.status.update"),
    BILL_DUEDAYCOUNT_UPDATE("bill.duedaycount.update"),//欠费天数
    SYNC_ENTERPRISE_CUSTOMER("sync.enterprise.customer"),
    SYNC_THIRD_CONTRACT("sync.third.contract"),
    SYNC_THIRD_CUSTOMER("sync.third.customer"),

    TRACKING_PLAN_WARNING_SCHEDULE("tracking.plan.warning.schedule"),

    EVENT_STAT_SCHEDULE("event.stat.schedule"),
    ORGANIZATION_ORDER_LOCK("organization.order.lock"),

    FLOW_LANE("flow.lane"),
    FLOW("flow"),

    CLEANWRONGSTATUS_ORGANIZATIONMEMBERS("cleanwrongstatus.organizationmembers"),
    CREATE_ENERGY_TASK("energy.task.create"),
    SCHEDULE_ENERGY_TASK("energy.task.schedule"),

    ADD_ORGANIZATION_PERSONEL("add.organization.personel"),

    POINT_UPDATE_RULE_CATEGORY_SERVER_ID("point.update.ruleCategory.serverId"),
    POINT_UPDATE_POINT_SCORE("point.update.pointScore"),
    POINT_CATEGORY_SCHEDULE("point.category.schedule"),

    SYNC_CONTRACT("sync.contract"),
    FORUM_SETTING("forum.setting"),
    AUTH_RELATION("auth_relation"),

    SOCIAL_SECURITY_LIST_PAYMENTS("social_security_list_payments"),
    SOCIAL_SECURITY_INCRESE("social_security_increse"),
    SOCIAL_SECURITY_ADD("social_security_add"),
    FIXED_ASSET_CATEGORY_COPY_DEFAULT("fixed_asset_category_copy_default"),
    DEFAULT_REMIND_CATEGORY_ADD("default_remind_category_add"),
    REMIND_DEMO_ADD("remind_demo_add"),
    REMIND_CATEGORY_SORTING("remind_category_sorting"),
    REMIND_SORTING("remind_sorting"),
    REMIND_SCHEDULED("remind_scheduled"),
    MEETING_ROOM_DEFAULT_INIT("meeting_room_default_init"),
    MEETING_ROOM_TIME_LOCK("meeting_room_time_lock"),
    MEETING_ROOM_RESOURCE_RECOVERY("meeting_room_resource_recovery"),
    MEETING_REMIND("meeting_remind"),

    PORTAL_PUBLISH("portal.publish"),

    ARCHIVES_CONFIGURATION("archives_configuration"),
    ARCHIVES_NOTIFICATION("archives_notification"),
    VISITOR_SYS_GEN_IN_NO("visitor_sys_gen_in_no"),
    VISITOR_SYS_CONFIG("visitor_sys_config"),
    WORK_REPORT_RX_MSG("work_report_rx_msg"),
    WORK_REPORT_AU_BASIC_MSG("work_report_au_basic_msg"),
    WORK_REPORT_AU_MSG("work_report_au_msg"),

    VISITOR_SYS_LOCATION("visitor_sys_location"),
    ACTIVITY_SIGNUP_TIMEOUT("activity_signup_timeout"),

    PUNCH_DAY_LOG_INIT_OPERATION("punchDayLogInitializeByMonth"),
    PUNCH_DAY_LOG_INIT_SCHEDULE("punchDayLogInitializeSchedule"),
    EXCUTE_ADDRESS_ARRANGEMENT("excute_address_arrangement"),

    ANNOUNCEMENT_CREATE_NOTICE_USER("announcement_create_notice_user"),
	
	STATISTIC_BILL_BY_COMMUNITY("statistic_bill_by_community");//issue-38508 根据项目+月份统计缴费报表

    private String code;

    CoordinationLocks(String code) {
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
