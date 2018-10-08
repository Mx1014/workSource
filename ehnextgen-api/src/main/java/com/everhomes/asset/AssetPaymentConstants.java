//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian on 2017/8/29.
 */
public interface AssetPaymentConstants {
    //物业缴费模块的变量配置
    public static final Byte NATRUAL_MONTH = 1;

    public static final Byte CONTRACT_BEGIN_DATE_AS_FIXED_DAY_OF_MONTH = 2;
    public static final Byte BALANCE_ON_MONTH = 2;
    //常量
    public static final String CONTRACT_SAVE = "CONTRACT_SAVE";
    public static final String CONTRACT_CANCEL = "CONTRACT_CONCEL";
    public static final String EH_USER = "eh_user";
    public static final String EH_ORGANIZATION = "eh_organization";

    //错误信息
    public static final String DELETE_CHARGING_STANDARD_UNSAFE = "删除失败，该计费标准已经被账单组关联使用";
    public static final String DELETE_GROUP_RULE_UNSAFE = "删除失败，该账单组的计价条款已经关联合同，或者产生了账单，或者已被其他应用所关联";
    public static final String CREATE_CHARGING_ITEM_FAIL = "添加失败，一个收费项只能在一个账单组存在";
    public static final String MODIFY_GROUP_RULE_UNSAFE = "修改失败，该账单组的计价条款已经关联合同，或者产生了账单";
    public static final String DELTE_GROUP_UNSAFE = "删除失败，该账单组已经关联合同，或者产生了账单，或者已被其他应用所关联";

    public static final String CHARGING_ITEM_NAME_WATER = "水费";
    public static final String CHARGING_ITEM_NAME_ELECTRICITY = "电费";
    public static final String VARIABLE_YJ = "用量";
    public static final String VARIABLE_BLXS = "比例系数";
    public static final String LATE_FINE_NAME = "滞纳金";
    //收费想固定id
    public static final long LATE_FINE_ID = 6;


}
