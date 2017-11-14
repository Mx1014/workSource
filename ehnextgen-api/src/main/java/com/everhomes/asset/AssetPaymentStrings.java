//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian on 2017/8/29.
 */
public interface AssetPaymentStrings {
    public static final Byte NATRUAL_MONTH = 1;

    public static final Byte CONTRACT_BEGIN_DATE_AS_FIXED_DAY_OF_MONTH = 2;
    public static final Byte BALANCE_ON_MONTH = 2;

    public static final String CONTRACT_SAVE = "CONTRACT_SAVE";
    public static final String CONTRACT_CANCEL = "CONTRACT_CONCEL";
    public static final String EH_USER = "eh_user";
    public static final String EH_ORGANIZATION = "eh_organization";


    public static final String DELETE_CHARGING_STANDARD_UNSAFE = "删除失败，该计费标准已经被账单组关联使用";
    public static final String DELETE_GROUP_RULE_UNSAFE = "删除失败，该账单组的计价条款已经关联合同，或者产生了账单";
    public static final String CREATE_CHARGING_ITEM_FAIL = "添加失败，一个收费项只能在一个账单组存在";
    public static final String MODIFY_GROUP_RULE_UNSAFE = "修改失败，该账单组的计价条款已经关联合同，或者产生了账单";
    public static final String DELTE_GROUP_UNSAFE = "删除失败，该账单组已经关联合同，或者产生了账单";

    public static final String CHARGING_ITEM_NAME_WATER = "水费";
    public static final String CHARGING_ITEM_NAME_ELECTRICITY = "电费";
    public static final String VARIABLE_YJ = "用量";



}
