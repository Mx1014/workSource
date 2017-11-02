-- version 1 的手动复制出来
SET @conf_id := (SELECT MAX(id) FROM eh_uniongroup_configures);
SET @member_id := (SELECT MAX(id) FROM eh_uniongroup_configures);

INSERT INTO eh_uniongroup_configures 
SELECT
  @conf_id + id id,
  `namespace_id`,
  `enterprise_id`,
  `group_type`,
  `group_id`,
  `current_id`,
  `current_type`,
  `current_name`,
  `operator_uid`,
  `update_time`,
  1 version_code
FROM `eh_uniongroup_configures`;
INSERT INTO eh_uniongroup_member_details 
SELECT
  @member_id  + `id` id,
  `namespace_id`,
  `group_type`,
  `group_id`,
  `detail_id`,
  `target_type`,
  `target_id`,
  `enterprise_id`,
  `contact_name`,
  `contact_token`,
  `update_time`,
  `operator_uid`,
  1 version_code
FROM `eh_uniongroup_member_details`;

-- by dengs,菜单名称修改， 20171027
UPDATE eh_web_menus SET `name` = '问卷调查' WHERE id = 40150 AND `name` = '企业问卷调查';
INSERT INTO `eh_configurations` (`name`, `value`, `description`) VALUES ('questionnaire.detail.url', '/questionnaire-survey/build/index.html#/question/%s/0', '问卷地址URL');
INSERT INTO `eh_configurations` (`name`, `value`, `description`) VALUES ('questionnaire.send.message.express', '0 0 1 * * ?', '定时任务表达式');
INSERT INTO `eh_configurations` (`name`, `value`, `description`) VALUES ('questionnaire.remind.time.interval', '24', '通知value小时内，没有回答问卷的用户');




-- merge from forum-2.4 add by yanjun 201710311836
-- 论坛入口表默认入口
set @m_id = (select ifnull(max(id), 0) from eh_forum_categories);
INSERT INTO `eh_forum_categories` (id, `uuid`, `namespace_id`, `forum_id`, `entry_id`, `name`, `activity_entry_id`, `create_time`, `update_time`)
SELECT (@m_id:=@m_id+1),UUID(), namespace_id, default_forum_id, 0, '发现', '0', NOW(), NOW() from eh_communities where `status` = 2 GROUP BY namespace_id, default_forum_id ORDER BY namespace_id ;

set @id := (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'forum', '10020', 'zh_CN', '此版本目前不支持评论功能。由此带来的不便请谅解。');

-- merge from forum-2.4 add by yanjun 201710311836

-- 所有配置的收费项目第一版只能计费周期为月 -- by wentian
update `eh_payment_charging_standards` set billing_cycle = 2;
-- 物业缴费错误码 -- by wentian
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'assetv2', '10001', 'zh_CN', '正在生成中');

-- 园区入驻 add by sw 20171102
UPDATE eh_configurations set `value` = '/park-entry/dist/index.html?hideNavigationBar=1#/project_intro/%s/%s' where `name` = 'apply.entry.lease.project.detail.url';
UPDATE eh_configurations set `value` = '/park-entry/dist/index.html?hideNavigationBar=1#/building_detail/%s/%s' where `name` = 'apply.entry.lease.building.detail.url';
UPDATE eh_configurations set `value` = '/park-entry/dist/index.html?hideNavigationBar=1#/rent_detail/%s/%s' where `name` = 'apply.entry.detail.url';

-- 删除分类  add by yanjun 20171102 已在beta、alpha执行
DELETE from eh_categories WHERE name in ('二手和租售', '免费物品', '失物招领', '紧急通知');