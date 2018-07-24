SET @locale_id = (SELECT MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '1001', 'zh_CN', '通讯录删除');
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '1002', 'zh_CN', '通讯录成员列表');
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '1003', 'zh_CN', '人员档案导入模板');
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '1004', 'zh_CN', '人员档案列表');
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '1005', 'zh_CN', '填写须知：\r\n    1、请不要对员工信息类别进行增加、删除或修改，以免无法识别员工信息；\r\n    2、Excel中红色字段为必填字段,黑色字段为选填字段\r\n    3、请不要包含公式，以免错误识别员工信息；\r\n    4、多次导入时，若系统中已存在相同手机号码的员工，将以导入的信息为准；\r\n    5、部门：上下级部门间用‘/’隔开，且从最上级部门开始，例如\\\"左邻/深圳研发中心/研发部\\\"；部门若为空，则自动将成员添加到选择的目录下；\r\n    6、手机：支持国内、国际手机号（国内手机号直接输入手机号即可；国际手机号必须包含加号以及国家地区码，格式示例：“+85259****24”）；\r\n    7、合同公司：合同公司若为空，将默认使用公司全称\r\n    8、若要删除某行信息，请右键行号，选择删除\r\n    9、注意日期格式为 xxxx-xx-xx');

SET @template_id = (SELECT MAX(id) from eh_locale_templates);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', 5, 'zh_CN', '人事提醒开头', '你好，${contactName}\r\n\r\n\r\n${companyName}近一周需要注意的人事日程如下：\r\n\r\n\r\n', 0);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', 6, 'zh_CN', '人事转正提醒', '${contactNames} 转正\r\n', 0);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', 7, 'zh_CN', '人事合同到期提醒', '${contactNames} 合同到期\r\n', 0);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', 8, 'zh_CN', '人事身份证到期提醒', '${contactNames} 身份证到期\r\n', 0);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', 9, 'zh_CN', '人事周年提醒', '${contactName} 在${companyName}工作满 ${year} 年\r\n', 0);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', 10, 'zh_CN', '人事生日提醒', '${contactName} ${year} 岁生日\r\n', 0);
