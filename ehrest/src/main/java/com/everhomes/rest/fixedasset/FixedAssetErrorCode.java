package com.everhomes.rest.fixedasset;

public class FixedAssetErrorCode {

    public final static String SCOPE = "fixedAsset";
    public final static String FIXED_ASSET_OWNER_TYPE = "FIXED_ASSET_OWNER_TYPE";

    public final static int FIXED_ASSET_CATEGORY_NAME_DUPLICATE_ERROR = 10000;  // 分类名称已存在
    public final static int FIXED_ASSET_CATEGORY_HAVE_HAS_ITEMS_ERROR = 10001; // 存在资产属于此分类，请清空后再删除。
    public final static int FIXED_ASSET_ITEM_NUM_DUPLICATE_ERROR = 10002;  // 资产编号已经存在
    public final static int FIXED_ASSET_PARENT_CATEGORY_NOT_EXIST_ERROR = 10003; // 父分类不存在
    public final static int FIXED_ASSET_IMPORT_EMPTY = 10004; // 上传空文件
    public final static int FIXED_ASSET_CATEGORY_EMPTY_ERROR = 10005;  // 还没有创建分类

    public final static int FIXED_ASSET_SAVE_ERROR = 100000;    //  保存时出错
    public final static int FIXED_ASSET_ITEM_NUM_IS_EMPTY = 100001;    //  资产编号不能为空
    public final static int FIXED_ASSET_ITEM_NUM_EXCEEDS_LIMIT = 100002;    //  资产编号不能超过20个字
    public final static int FIXED_ASSET_CATEGORY_NO_EXIST = 100003;    //  资产分类不存在
    public final static int FIXED_ASSET_NAME_IS_EMPTY = 100004;    //  资产名称不能为空
    public final static int FIXED_ASSET_NAME_EXCEEDS_LIMIT = 100005;    //  资产名称不能超过20个字
    public final static int FIXED_ASSET_SPECIFICATION_EXCEEDS_LIMIT = 100006;    //  资产规格不能超过50个字
    public final static int FIXED_ASSET_PRICE_REQUIRE_DIGIT = 100007;    //  资产单价必须是数字
    public final static int FIXED_ASSET_PRICE_EXCEEDS_LIMIT = 100008;    //  资产单价超过最大范围
    public final static int FIXED_ASSET_BUYDATE_FORMAT = 100009;    //  资产购买时间格式错误
    public final static int FIXED_ASSET_VENDOR_EXCEEDS_LIMIT = 100010;    //  资产所属供应商不超过50字
    public final static int FIXED_ASSET_ADD_FROM_NO_EXIST = 100012;    //  资产来源不支持
    public final static int FIXED_ASSET_OTHER_EXCEEDS_LIMIT = 100013;    //  资产其它信息不能超过200个字
    public final static int FIXED_ASSET_STATUS_IS_EMPTY = 100014;    //  资产状态不能为空
    public final static int FIXED_ASSET_STATUS_NO_EXIST = 100015;    //  资产状态不支持
    public final static int FIXED_ASSET_OCCUPY_DEPARTMENT_NO_EXIST = 100016;    //  部门错误
    public final static int FIXED_ASSET_OCCUPY_USER_NO_EXIST = 100017;    //  使用人输入错误
    public final static int FIXED_ASSET_OCCUPYDATE_FORMAT = 100018;    //  资产领用时间格式错误
    public final static int FIXED_ASSET_LOCATION_EXCEEDS_LIMIT = 100019;    //  资产存放地点不能超过50个字
    public final static int FIXED_ASSET_REMARK_EXCEEDS_LIMIT = 100020;    //  资产备注不能超过200个字
    public final static int FIXED_ASSET_OCCUPYDATE_IS_EMPTY = 100021;    //  状态为使用中的领用时间必填
    public final static int FIXED_ASSET_DEPARTMENT_OR_USER_REQUIRED_ERROR = 100022;    //  状态为使用中的领用时间必填，使用部门、使用人至少填写一项
    public final static int FIXED_ASSET_CATEGORY_IS_NULL_ERROR = 100023;    //  资产分类为空
    public final static int FIXED_ASSET_ADD_FROM_NO_EMPTY = 100024;    //  资产来源不为空
    public final static int FIXED_ASSET_DEPARTMENT_OCCUPY_DATE_ERROR = 100025;    //  状态为使用中时，使用部门、领用时间必填
    public final static int FIXED_ASSET_DEPARTMENT_NAME_REQUIRE_ERROR = 100026;    //  使用人填写时，使用部门、领用时间必填
    public final static int FIXED_ASSET_DEPARTMENT_OCCUPY_DATE_BOTH_REQUIRE_ERROR = 100027;    //  使用部门、领用时间需要同时填写
    public final static int FIXED_ASSET_PARSE_OCCUPY_DATE_ERROR = 100028;    //  请输入正确的领用时间
    public final static int FIXED_ASSET_PARSE_BUY_DATE_ERROR = 100029;    //  请输入正确的购买时间

}
