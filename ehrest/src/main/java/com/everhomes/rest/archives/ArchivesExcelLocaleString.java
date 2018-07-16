package com.everhomes.rest.archives;

public class ArchivesExcelLocaleString {

    public static String T_FILENAME= "人事档案导入模板";
    public static String T_HEADER = "\"1、此表用于批量导入员工到人事档案，请不要修改或删除表头内容；\n" +
            "2、Excel中红色字段为必填字段,黑色字段为选填字段\n" +
            "3、请不要包含公式，以免错误识别员工信息；\n" +
            "4、多次导入时，若系统中已存在相同手机号码的员工，将以新导入的信息为准；\n" +
            "5、部门：上下级部门间用‘/'隔开，且从最上级部门开始，例如\"\"左邻/深圳研发中心/研发部\"\"；部门若为空，则自动将成员添加到选择的目录下；\n" +
            "6、手机：支持国内、国际手机号（国内手机号直接输入手机号即可；国际手机号必须包含加号以及国家地区码，格式示例：“+85259****24”）；\n" +
            "7、其他字段规则详见「字段规则说明」，由于员工档案字段较多，感谢您的耐心使用。\n";

    public static String T_NAME = "姓名\n";
    public static String T_NAME_VALUE = "文本；20字以内\n";

}
