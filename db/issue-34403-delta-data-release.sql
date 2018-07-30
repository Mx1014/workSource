set @id = (select max(id) from eh_general_forms);
INSERT INTO `eh_general_forms`(`id`,`namespace_id`,`organization_id`,`owner_id`,`owner_type`,`module_id`,`module_type`,`form_name`,`form_origin_id`,`form_version`,`template_type`,
                               `template_text`,`status`,`create_time`,`form_attribute`,`modify_flag`,`delete_flag`)
    VALUES (@id+1,0,0,0,'ActivitySignup',10600,'any-module','活动报名默认表单',0,0,'DEFAULT_JSON','[{"dynamicFlag":0,"fieldDesc":"请输入数字","fieldDisplayName":"手机","fieldExtra":"{\\"defaultValue\\":\\"\\"}","fieldName":"手机","fieldType":"NUMBER_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"NUM_LIMIT","visibleType":"READONLY"},{"dynamicFlag":0,"fieldDesc":"请输入姓名","fieldDisplayName":"姓名","fieldExtra":"{\\"limitWord\\":8}","fieldName":"姓名","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dynamicFlag":0,"fieldDesc":"请输入公司","fieldDisplayName":"公司","fieldExtra":"{\\"limitWord\\":36}","fieldName":"公司","fieldType":"MULTI_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dynamicFlag":0,"fieldDesc":"请输入职位","fieldDisplayName":"职位","fieldExtra":"{\\"limitWord\\":32}","fieldName":"职位","fieldType":"MULTI_LINE_TEXT","renderType":"DEFAULT","requiredFlag":0,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dynamicFlag":0,"fieldDesc":"请输入邮箱","fieldDisplayName":"邮箱","fieldExtra":"{\\"limitWord\\":64}","fieldName":"邮箱","fieldType":"MULTI_LINE_TEXT","renderType":"DEFAULT","requiredFlag":0,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"}]',
    2,now(),'DEFAULT',1,1);

-- 导入模板备注文字
INSERT into `eh_locale_strings`(`scope`,`code`,`locale`,`text`) VALUES ('activity','27','zh_CN','填写注意事项：（未按照如下要求填写，会导致数据不能正常导入）
1、请不要修改此表格的格式，包括插入删除行和列、合并拆分单元格等。需要填写的单元格有字段规则校验，请按照提示输入。
2、请在表格里面逐行录入数据，建议一次导入不超过500条信息。
3、请不要随意复制单元格，这样会破坏字段规则校验。
4、日期输入格式：yyyy-MM-dd（2017-01-01）或 yyyy/MM/dd（2017/01/01）
5、红色字段为必填项
6、请注意：手机号码是唯一的，若重复添加，则自动更新覆盖（仅更新不为空值的字段信息）');