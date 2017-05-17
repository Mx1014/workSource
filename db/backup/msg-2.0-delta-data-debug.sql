-- 修改审批字符串模板 add by xq.tian  2017/04/17
UPDATE `eh_locale_templates` SET `text` = '${userName}申请加入“${groupName}”（理由：${reason}），是否同意？'
  WHERE `scope` = 'group.notification' AND `code` = 44 AND `namespace_id` = 0;
UPDATE `eh_locale_templates` SET `text` = '${userName}${description}申请加入公司“${enterpriseName}”，是否同意？'
  WHERE `scope` = 'enterprise.notification' AND `code` = 7 AND `namespace_id` = 0;
UPDATE `eh_locale_templates` SET `text` = '您的家人${userName}申请加入${address}，是否同意？'
  WHERE `scope` = 'family.notification' AND `code` = 2 AND `namespace_id` = 0;

-- 修改原来的“系统小助手” 名称为“系统消息”，并且修改成新的头像
UPDATE `eh_users` SET `nick_name` = '系统消息', `avatar` = 'cs://1/image/aW1hZ2UvTVRvM09HRXhaakF5TWpFek5UZGlOV00zWVRObE9Ua3lORGRrT0dJek1ETTVaZw' WHERE `id` = 2;
UPDATE `eh_users` SET `nick_name` = '电商小助手', `avatar` = 'cs://1/image/aW1hZ2UvTVRwbVptTXhZVFJrTVROaU16SmtZMkk1WXpBeE9XTXhZVEl5WmpSa1l6YzNaZw' WHERE `id` = 3;