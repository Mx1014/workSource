-- 审批模板 add by xq.tian  2017/04/17
UPDATE `eh_locale_templates` SET `text` = '${userName}申请加入“${groupName}” （理由：${reason}），是否同意？'
  WHERE `scope` = 'group.notification' AND `code` = 44 AND `namespace_id` = 0;
UPDATE `eh_locale_templates` SET `text` = '${userName}${description}申请加入公司“${enterpriseName}”，是否同意？'
  WHERE `scope` = 'enterprise.notification' AND `code` = 7 AND `namespace_id` = 0;
UPDATE `eh_locale_templates` SET `text` = '您的家人${userName}申请加入${address}，是否同意？'
  WHERE `scope` = 'family.notification' AND `code` = 2 AND `namespace_id` = 0;
