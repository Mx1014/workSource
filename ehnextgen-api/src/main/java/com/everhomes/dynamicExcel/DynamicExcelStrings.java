//@formatter:off
package com.everhomes.dynamicExcel;

/**
 * Created by Wentian on 2018/1/12.
 */
public interface DynamicExcelStrings {
    public static final String DYNAMIC_EXCEL_HANDLER = "DynamicExcelHandler";
    String CUSTOEMR = "customer";
    String INVITED_CUSTOMER = "investment_promotion";

    public static String baseIntro = "填写注意事项：（未按照如下要求填写，会导致数据不能正常导入）\n" +
            "1、请不要修改此表格的格式，包括插入删除行和列、合并拆分单元格等。需要填写的单元格有字段规则校验，请按照要求输入。\n" +
            "2、请在表格里面逐行录入数据，建议一次最多导入400条信息。\n" +
            "3、请不要随意复制单元格，这样会破坏字段规则校验。\n" +
            "4、红色字段为必填项,不填将导致导入失败。\n"  +
            "5、请注意: \n" +
             "1): 企业管理导入格式：企业管理员名称(手机号)，若一个企业有多个管理员用英文\",\"隔开 \n" +
             "2): 楼栋门牌用\"/\"斜杠连接楼栋门牌信息,若一个客户有多个楼栋门牌信息，用英文\",\"隔开 \n" +
             "3): 邮箱域名只填写@后面的部分（不含@符号） \n" +
             "4): 如果导入系统已存在的客户（客户名称相同认为是相同的客户），将按照导入信息更新系统中已存在的客户 \n" ;

    String baseIntroManager = "填写注意事项：（未按照如下要求填写，会导致数据不能正常导入）\n" +
            "1、请不要修改此表格的格式，包括插入删除行和列、合并拆分单元格等。需要填写的单元格有字段规则校验，请按照要求输入。\n" +
            "2、请在表格里面逐行录入数据，建议一次最多导入400条信息。\n" +
            "3、请不要随意复制单元格，这样会破坏字段规则校验。\n" +
            "4、红色字段为必填项,不填将导致导入失败。\n"  ;


    public static String enumNotice = "以下字段请按枚举值填写，否则导入将失败 \n";
}
