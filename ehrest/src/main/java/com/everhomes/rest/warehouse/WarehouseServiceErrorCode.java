package com.everhomes.rest.warehouse;

/**
 * Created by ying.xiong on 2017/5/12.
 */
public interface WarehouseServiceErrorCode {
    static final String SCOPE = "warehouse";
    static final int ERROR_WAREHOUSE_NOT_EXIST = 10001;//仓库不存在
    static final int ERROR_WAREHOUSE_MATERIAL_CATEGORY_NOT_EXIST = 10002;//物品分类不存在
    static final int ERROR_WAREHOUSE_MATERIAL_NOT_EXIST = 10003;//物品不存在
    static final int ERROR_WAREHOUSE_NUMBER_ALREADY_EXIST = 10004;//仓库编码已存在
    static final int ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER_ALREADY_EXIST = 10005;//物品分类编码已存在
    static final int ERROR_WAREHOUSE_MATERIAL_NUMBER_ALREADY_EXIST = 10006;//物品编码已存在
    static final int ERROR_WAREHOUSE_STOCK_NOT_NULL = 10007;//仓库库存不为0
    static final int ERROR_WAREHOUSE_MATERIAL_CATEGORY_IN_USE = 10008;//物品分类有关联物品
    static final int ERROR_WAREHOUSE_MATERIAL_RELATED_TO_WAREHOUSE = 10009;//物品有库存引用
    static final int ERROR_WAREHOUSE_STOCK_SHORTAGE = 10010;//库存不足
    static final int ERROR_ENABLE_FLOW = 10011;//请启用工作流
    static final int ERROR_CREATE_EXCEL = 10012;//创建excel失败
    static final int ERROR_DOWNLOAD_EXCEL = 10013;//下载excel失败

    static final int ERROR_WAREHOUSE_MATERIAL_NAME_IS_NULL = 10014;
    static final int ERROR_WAREHOUSE_MATERIAL_NUMBER_IS_NULL = 10015;
    static final int ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER_IS_NULL = 10016;
    static final int ERROR_WAREHOUSE_MATERIAL_CATEGORY_NUMBER = 10017;
    static final int ERROR_WAREHOUSE_MATERIAL_PRICE = 10018;
    static final int ERROR_WAREHOUSE_MATERIAL_UNIT_IS_NULL = 10019;
    static final int ERROR_WAREHOUSE_MATERIAL_CATEGORY_NAME_IS_NULL = 10020;
    static final int ERROR_WAREHOUSE_REQUEST_MATERIAL_IS_NOT_EXIST = 10021;//申请物品不存在
    static final int ERROR_WAREHOUSE_REQUEST_MATERIAL_IS_NOT_QUALIFIED = 10022;//申请物品未通过审核
    static final int ERROR_WAREHOUSE_REQUEST_MATERIAL_IS_ALREADY_DELIVERY = 10023;//申请物品已交付
    static final int ERROR_WAREHOUSE_REQUEST_MATERIAL_SHOULD_LARGER_THAN_ZERO = 10024;//warehouse stock change amount
    static final int ERROR_WAREHOUSE_IS_NOT_ACTIVE = 10025;//warehouse is not active
}

