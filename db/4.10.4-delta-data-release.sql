-- version 1 的手动复制出来
SET @conf_id := (SELECT MAX(id) FROM eh_uniongroup_configures);
SET @member_id := (SELECT MAX(id) FROM eh_uniongroup_configures);
;
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
FROM `eh_uniongroup_configures`
 ;
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