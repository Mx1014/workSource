-- AUTHOR: 梁燕龙
-- REMARK: issue-36940 用户认证，邮箱认证提示文案
SET @max_id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_strings`),1);
INSERT INTO eh_locale_strings (id, scope, code, locale, text)
VALUES (@max_id:=@max_id+1,'organization', 900039, 'zh_CN', '该邮箱已被认证');
