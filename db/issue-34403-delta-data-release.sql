-- 导入模板备注文字
INSERT into `eh_locale_strings`(`scope`,`code`,`locale`,`text`) VALUES ('activity','27','zh_CN','填写注意事项：（未按照如下要求填写，会导致数据不能正常导入）
1、请不要修改此表格的格式，包括插入删除行和列、合并拆分单元格等。需要填写的单元格有字段规则校验，请按照提示输入。
2、请在表格里面逐行录入数据，建议一次导入不超过500条信息。
3、请不要随意复制单元格，这样会破坏字段规则校验。
4、日期输入格式：yyyy-MM-dd（2017-01-01）或 yyyy/MM/dd（2017/01/01）
5、红色字段为必填项
6、请注意：手机号码是唯一的，若重复添加，则自动更新覆盖（仅更新不为空值的字段信息）');

SET @id = (SELECT max(id) from eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@id:=@id+1), 'activity.notification', '21', 'zh_CN', '活动报名成功', '您在「${postName}」的报名已提交。', '0');