
-- 园区入驻2.4 by wuhan 2017-2-24
SET @id := (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES(( @id := @id +1),'expansion','3','zh_CN','园区入驻工作流详情内容','[{\"key\":\"发起人\",\"value\":\"${applyUserName}\",\"entityType\":\"list\"},{\"key\":\"联系电话\",\"value\":\"${contactPhone}\",\"entityType\":\"list\"},{\"key\":\"企业\",\"value\":\"${enterpriseName}\",\"entityType\":\"list\"},{\"key\":\"申请类型\",\"value\":\"${applyType}\",\"entityType\":\"list\"},{\"key\":\"申请来源\",\"value\":\"${sourceType}\",\"entityType\":\"list\"},{\"key\":\"备注\",\"value\":\"${description}\",\"entityType\":\"multi_line\"}]','0');

SET @id := (SELECT MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES(( @id := @id +1),'expansion.applyType','4','zh_CN','创客申请');

-- 更改威新LINK+的两条活动为官方活动，add by tt, 20170222
UPDATE eh_activities SET official_flag = 1, category_id = 1 WHERE id IN (1508,1509);
UPDATE eh_forum_posts SET official_flag = 1, activity_category_id = 1 WHERE id IN (188703, 188704);