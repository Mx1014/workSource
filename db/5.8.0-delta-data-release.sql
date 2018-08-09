-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR: 杨崇鑫  20180720
-- REMARK: content图片程序升级，① 从本版中的content二进制更新到正式环境中  ② 把allowOriginToGet = * 加到 config.ini 配置文件中的 system 区域下

-- AUTHOR: ryan  20180807
-- REMARK: 执行 /archives/cleanRedundantArchivesDetails 接口(可能速度有点慢，但可重复执行)

-- AUTHOR: jiarui  20180807
-- REMARK: 执行search 下脚本 enter_meter.sh
-- 执行 /energy/syncEnergyMeterIndex 接口(可能速度有点慢，但可重复执行)

-- --------------------- SECTION END ---------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫
-- REMARK: 根据域空间判断是否展示能耗数据，测试环境初始化鼎峰汇支持展示能耗数据
INSERT INTO `eh_service_module_functions`(`id`, `module_id`, `privilege_id`, `explain`) VALUES (101, 20400, 0, '账单显示用量');

-- AUTHOR: 梁燕龙
-- REMARK: 活动报名人数不足最低限制人数自动取消活动消息推送
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
VALUES( 'activity.notification', 18, 'zh_CN', '活动报名人数不足最低限制人数，活动取消', '您报名的活动「${subject}」由于未达到最低人数，已被取消。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
VALUES( 'activity.notification', 19, 'zh_CN', '活动报名人数不足最低限制人数，活动取消', '您报名的活动「${subject}」由于未达到最低人数，已被取消，报名费用将在三个工作日内退回您的账户上。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
VALUES( 'activity.notification', 20, 'zh_CN', '活动报名人数不足最低限制人数，活动取消', '您发起的活动「${subject}」由于未达到最低人数，已被自动取消。');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`) VALUES ('activity',28,'zh_CN','活动取消通知');
UPDATE eh_locale_strings SET text = '来晚啦，活动已删除' WHERE scope = 'forum' and code = 10006;
-- AUTHOR: 梁燕龙
-- REMARK: 活动消息推送文案修改。
UPDATE eh_locale_templates SET text = '${userName}报名参加了您发起的活动「${postName}」' WHERE scope = 'activity.notification' and code = 1;
UPDATE eh_locale_templates SET text = '${userName}取消了您发起的活动「${postName}」报名' WHERE scope = 'activity.notification' and code = 2;
UPDATE eh_locale_templates SET text = '您报名参加的活动「${postName}」已被管理员通过' WHERE scope = 'activity.notification' and code = 3;
UPDATE eh_locale_templates SET text = '很抱歉通知您：您报名的活动「${tag} 丨 ${title}」因故取消。
更多活动敬请继续关注。' WHERE scope = 'activity.notification' and code = 5;
UPDATE eh_locale_templates SET text = '您报名的活动 「${tag} 丨 ${title}」 还有 ${time}就要开始了。' WHERE scope = 'activity.notification' and code = 6;
UPDATE eh_locale_templates SET text = '「${userName}」报名了活动「${postName}」，请尽快确认。' WHERE scope = 'activity.notification' and code = 8;
UPDATE eh_locale_templates SET text = '您参加的活动「${postName}」的主题已被发起方改成「${newPostName}」。' WHERE scope = 'activity.notification' and code = 11;
UPDATE eh_locale_templates SET text = '您参加的活动「${postName}」的时间已被发起方改成「${startTime}~${endTime}」。' WHERE scope = 'activity.notification' and code = 12;
UPDATE eh_locale_templates SET text = '您参加的活动「${postName}」的地点已被发起方改成「${address}」。' WHERE scope = 'activity.notification' and code = 13;
UPDATE eh_locale_templates SET text = '您参加的活动「${postName}」被发起方修改，详情如下：主题被改成「${newPostName}」、时间被改成「${startTime}~${endTime}」。' WHERE scope = 'activity.notification' and code = 14;
UPDATE eh_locale_templates SET text = '您参加的活动「${postName}」被发起方修改，详情如下：主题被改成「${newPostName}」、地点被改成「${address}」。' WHERE scope = 'activity.notification' and code = 15;
UPDATE eh_locale_templates SET text = '您参加的活动「${postName}」被发起方修改，详情如下：时间被改成「${startTime}~${endTime}」、地点被改成「${address}」。' WHERE scope = 'activity.notification' and code = 16;
UPDATE eh_locale_templates SET text = '您参加的活动「${postName}」被发起方修改，详情如下：主题被改成「${newPostName}」、时间被改成「${startTime}~${endTime}」、地点被改成「${address}」。' WHERE scope = 'activity.notification' and code = 17;

-- AUTHOR: jiarui
-- REMARK: 物业巡检离线包版本更新
UPDATE eh_version_urls SET download_url = replace(download_url,'1-0-1','1-0-2') WHERE app_name = '物业巡检';
UPDATE eh_version_urls SET info_url = replace(info_url,'1-0-1','1-0-2') where app_name = '物业巡检';
UPDATE eh_version_urls SET target_version = '1.0.2' WHERE app_name = '物业巡检';

-- AUTHOR: dengs
-- REMARK: 访客管理1.1 移动端管理权限
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41850', '移动端管理', '41800', '/200/20000/41800/41850', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
update eh_service_modules SET  path='/200/20000/41800/41810' WHERE id = 41810;
update eh_service_modules SET  path='/200/20000/41800/41840' WHERE id = 41840;

set @privilege_id = (select max(id) from eh_service_module_privileges);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4180041850, '0', '园区访客 移动端管理权限', '园区访客 移动端管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41850', '0', 4180041850, '移动端管理权限', '0', now());

SET @homeurl = (select `value` from eh_configurations WHERE `name`='home.url' AND namespace_id = 0 limit 1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('52200', '企业访客管理', '50000', '/100/50000/52200', '1', '2', '2', '220', now(), CONCAT('{"url":"',@homeurl,'/visitor-management/build/index.html?ns=%s&appId=%s&ownerType=enterprise#/home#sign_suffix"}'), '13', now(), '0', '0', '0', '0', 'community_control');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('42100', '园区访客管理', '40000', '/200/20000/42100', '1', '2', '2', '210', now(), CONCAT('{"url":"',@homeurl,'/visitor-management/build/index.html?ns=%s&appId=%s&ownerType=community#/home#sign_suffix"}'), '13', now(), '0', '0', '0', '0', 'org_control');

-- 更新访客的instance_config
update eh_service_modules SET instance_config = REPLACE(instance_config,' ','') WHERE id = 52100;
update eh_service_module_apps SET instance_config = REPLACE(instance_config,' ','') WHERE module_id = 52100;

-- AUTHOR: xq.tian  20180725
-- REMARK: 工作流 2.7
SET @eh_locale_templates_id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
  VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20008, 'zh_CN', '子业务流程进行中', '${serviceName} 进行中', 0);
INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
  VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20009, 'zh_CN', '子流程创建成功，点击此处查看父流程详情。', '子流程创建成功，点击此处查看父流程详情', 0);
INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
  VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20010, 'zh_CN', '${serviceName} 已完成', '${serviceName} 已完成', 0);
INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
  VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20011, 'zh_CN', '${serviceName} 已终止', '${serviceName} 已终止', 0);
INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
  VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20012, 'zh_CN', '子流程循环层级过多，流程已终止，详情请联系管理员', '子流程循环层级过多，流程已终止，详情请联系管理员', 0);

SET @eh_locale_strings_id = (SELECT MAX(id) FROM eh_locale_strings);
INSERT INTO eh_locale_strings (id, scope, code, locale, text)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'flow', '100025', 'zh_CN', '子流程异常，请检查设置');
INSERT INTO eh_locale_strings (id, scope, code, locale, text)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'flow', '100026', 'zh_CN', '请先发布新版本后再启用');
INSERT INTO eh_locale_strings (id, scope, code, locale, text)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'flow', '100027', 'zh_CN', '当前工作流未被修改，请修改后发布新版本');

-- --------------------- SECTION END ---------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 1000000 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999999 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999996 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999994 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999993 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999992 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999990 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999991 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999989 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999988 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999986 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999985 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999983 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999982 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999981 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999980 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999978 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999977 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 1 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999976 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999975 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999974 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999973 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999972 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999971 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999970 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999969 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999968 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999967 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999965 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999963 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999962 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999961 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999960 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999959 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999958 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999957 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999956 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999955 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999954 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999953 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999952 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999951 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999950 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999948 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999947 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999946 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999945 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999944 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999943 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999942 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999941 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999940 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999939 , NULL, 20400, 101);
-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION只在开发库中执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zijing
-- DESCRIPTION: 此SECTION只在清华信息港(紫荆)-999984执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999984 , NULL, 20400, 101);

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999979 , NULL, 20400, 101);

-- AUTHOR: Ryan
-- REMARK: 由纲哥需求在所有域空间增加 “公文收发” 审批模板，此sql需在5.6.0以上版本的代码执行
INSERT INTO `eh_enterprise_approval_groups` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `status`, `group_attribute`, `approval_icon`, `operator_uid`, `operator_time`) VALUES ('10', '0', NULL, '0', '公文收发', '1', 'CUSTOMIZE', 'cs://1/image/aW1hZ2UvTVRwak1UazVOekZpT0dZNU1UVTRZMlpoTjJKak56WXpOMlUyWVRobFpqa3hZZw', '1', '2018-07-31 09:00:00');
UPDATE eh_enterprise_approval_groups SET id = 20 WHERE id = 1;
UPDATE eh_enterprise_approval_groups SET id = 30 WHERE id = 2;
UPDATE eh_enterprise_approval_groups SET id = 40 WHERE id = 3;
UPDATE eh_enterprise_approval_groups SET id = 50 WHERE id = 4;
UPDATE eh_enterprise_approval_groups SET id = 60 WHERE id = 5;

UPDATE eh_enterprise_approval_templates SET group_id = 20 WHERE group_id = 1;
UPDATE eh_enterprise_approval_templates SET group_id = 30 WHERE group_id = 2;
UPDATE eh_enterprise_approval_templates SET group_id = 40 WHERE group_id = 3;
UPDATE eh_enterprise_approval_templates SET group_id = 50 WHERE group_id = 4;
UPDATE eh_enterprise_approval_templates SET group_id = 60 WHERE group_id = 5;

UPDATE eh_general_approvals SET integral_tag1 = 20 WHERE integral_tag1 = 1 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 30 WHERE integral_tag1 = 2 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 40 WHERE integral_tag1 = 3 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 50 WHERE integral_tag1 = 4 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 60 WHERE integral_tag1 = 5 AND module_id = 52000;

INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('15', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '15', '0', '发文（红头文件）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRwallUUTBNR05rTkdZMFptVXpaVEJoT1RZNU9EZ3dOMkpsTkRjNU9XTTROZw', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('16', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '16', '0', '发文（非红头文件）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRwallUUTBNR05rTkdZMFptVXpaVEJoT1RZNU9EZ3dOMkpsTkRjNU9XTTROZw', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('17', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '17', '0', '行政收文（紧急）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM00yTXlOek0yTnpnNU0yVXhOR1JtWW1Gak9Ea3dZemxpWkdFMU9EVXpaQQ', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('18', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '18', '0', '行政收文', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM00yTXlOek0yTnpnNU0yVXhOR1JtWW1Gak9Ea3dZemxpWkdFMU9EVXpaQQ', '2018-07-31 14:49:45', '2018-07-31 14:49:45');

INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('15', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '红头文件发文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"文件标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文号\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"主送单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"主送单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"抄送单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"抄送单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件份数\",\"fieldExtra\":\"{\\\"defaultValue\\\":\\\"\\\"}\",\"fieldName\":\"文件份数\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"NUM_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('16', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '非红头文件发文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"文件标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"是否盖章\",\"fieldExtra\":\"{\\\"selectValue\\\":[\\\"是\\\",\\\"否\\\"]}\",\"fieldName\":\"是否盖章\",\"fieldType\":\"DROP_BOX\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('17', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '行政收文（紧急）申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"收文日期\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"收文日期\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文编号\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文编号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('18', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '行政收文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"收文日期\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"收文日期\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文编号\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文编号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999966 , NULL, 20400, 101);
-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999964 , NULL, 20400, 101);

-- AUTHOR: Ryan
-- REMARK: 由纲哥需求在所有域空间增加 “公文收发” 审批模板，此sql需在5.6.0以上版本的代码执行
INSERT INTO `eh_enterprise_approval_groups` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `status`, `group_attribute`, `approval_icon`, `operator_uid`, `operator_time`) VALUES ('10', '0', NULL, '0', '公文收发', '1', 'CUSTOMIZE', 'cs://1/image/aW1hZ2UvTVRwak1UazVOekZpT0dZNU1UVTRZMlpoTjJKak56WXpOMlUyWVRobFpqa3hZZw', '1', '2018-07-31 09:00:00');
UPDATE eh_enterprise_approval_groups SET id = 20 WHERE id = 1;
UPDATE eh_enterprise_approval_groups SET id = 30 WHERE id = 2;
UPDATE eh_enterprise_approval_groups SET id = 40 WHERE id = 3;
UPDATE eh_enterprise_approval_groups SET id = 50 WHERE id = 4;
UPDATE eh_enterprise_approval_groups SET id = 60 WHERE id = 5;

UPDATE eh_enterprise_approval_templates SET group_id = 20 WHERE group_id = 1;
UPDATE eh_enterprise_approval_templates SET group_id = 30 WHERE group_id = 2;
UPDATE eh_enterprise_approval_templates SET group_id = 40 WHERE group_id = 3;
UPDATE eh_enterprise_approval_templates SET group_id = 50 WHERE group_id = 4;
UPDATE eh_enterprise_approval_templates SET group_id = 60 WHERE group_id = 5;

UPDATE eh_general_approvals SET integral_tag1 = 20 WHERE integral_tag1 = 1 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 30 WHERE integral_tag1 = 2 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 40 WHERE integral_tag1 = 3 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 50 WHERE integral_tag1 = 4 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 60 WHERE integral_tag1 = 5 AND module_id = 52000;

INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('15', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '15', '0', '发文（红头文件）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRwallUUTBNR05rTkdZMFptVXpaVEJoT1RZNU9EZ3dOMkpsTkRjNU9XTTROZw', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('16', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '16', '0', '发文（非红头文件）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRwallUUTBNR05rTkdZMFptVXpaVEJoT1RZNU9EZ3dOMkpsTkRjNU9XTTROZw', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('17', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '17', '0', '行政收文（紧急）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM00yTXlOek0yTnpnNU0yVXhOR1JtWW1Gak9Ea3dZemxpWkdFMU9EVXpaQQ', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('18', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '18', '0', '行政收文', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM00yTXlOek0yTnpnNU0yVXhOR1JtWW1Gak9Ea3dZemxpWkdFMU9EVXpaQQ', '2018-07-31 14:49:45', '2018-07-31 14:49:45');

INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('15', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '红头文件发文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"文件标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文号\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"主送单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"主送单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"抄送单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"抄送单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件份数\",\"fieldExtra\":\"{\\\"defaultValue\\\":\\\"\\\"}\",\"fieldName\":\"文件份数\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"NUM_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('16', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '非红头文件发文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"文件标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"是否盖章\",\"fieldExtra\":\"{\\\"selectValue\\\":[\\\"是\\\",\\\"否\\\"]}\",\"fieldName\":\"是否盖章\",\"fieldType\":\"DROP_BOX\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('17', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '行政收文（紧急）申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"收文日期\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"收文日期\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文编号\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文编号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('18', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '行政收文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"收文日期\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"收文日期\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文编号\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文编号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999949 , NULL, 20400, 101);

-- AUTHOR: Ryan
-- REMARK: 由纲哥需求在所有域空间增加 “公文收发” 审批模板，此sql需在5.6.0以上版本的代码执行
INSERT INTO `eh_enterprise_approval_groups` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `status`, `group_attribute`, `approval_icon`, `operator_uid`, `operator_time`) VALUES ('10', '0', NULL, '0', '公文收发', '1', 'CUSTOMIZE', 'cs://1/image/aW1hZ2UvTVRwak1UazVOekZpT0dZNU1UVTRZMlpoTjJKak56WXpOMlUyWVRobFpqa3hZZw', '1', '2018-07-31 09:00:00');
UPDATE eh_enterprise_approval_groups SET id = 20 WHERE id = 1;
UPDATE eh_enterprise_approval_groups SET id = 30 WHERE id = 2;
UPDATE eh_enterprise_approval_groups SET id = 40 WHERE id = 3;
UPDATE eh_enterprise_approval_groups SET id = 50 WHERE id = 4;
UPDATE eh_enterprise_approval_groups SET id = 60 WHERE id = 5;

UPDATE eh_enterprise_approval_templates SET group_id = 20 WHERE group_id = 1;
UPDATE eh_enterprise_approval_templates SET group_id = 30 WHERE group_id = 2;
UPDATE eh_enterprise_approval_templates SET group_id = 40 WHERE group_id = 3;
UPDATE eh_enterprise_approval_templates SET group_id = 50 WHERE group_id = 4;
UPDATE eh_enterprise_approval_templates SET group_id = 60 WHERE group_id = 5;

UPDATE eh_general_approvals SET integral_tag1 = 20 WHERE integral_tag1 = 1 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 30 WHERE integral_tag1 = 2 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 40 WHERE integral_tag1 = 3 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 50 WHERE integral_tag1 = 4 AND module_id = 52000;
UPDATE eh_general_approvals SET integral_tag1 = 60 WHERE integral_tag1 = 5 AND module_id = 52000;

INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('15', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '15', '0', '发文（红头文件）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRwallUUTBNR05rTkdZMFptVXpaVEJoT1RZNU9EZ3dOMkpsTkRjNU9XTTROZw', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('16', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '16', '0', '发文（非红头文件）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRwallUUTBNR05rTkdZMFptVXpaVEJoT1RZNU9EZ3dOMkpsTkRjNU9XTTROZw', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('17', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '17', '0', '行政收文（紧急）', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM00yTXlOek0yTnpnNU0yVXhOR1JtWW1Gak9Ea3dZemxpWkdFMU9EVXpaQQ', '2018-07-31 14:49:45', '2018-07-31 14:49:45');
INSERT INTO `eh_enterprise_approval_templates` (`id`, `namespace_id`, `owner_id`, `owner_type`, `organization_id`, `module_id`, `module_type`, `project_id`, `project_type`, `form_template_id`, `support_type`, `approval_name`, `approval_remark`, `group_id`, `approval_attribute`, `modify_flag`, `delete_flag`, `icon_uri`, `update_time`, `create_time`) VALUES ('18', '0', '0', 'EhOrganizaions', '0', '52000', 'any-module', '0', NULL, '18', '0', '行政收文', NULL, '10', 'CUSTOMIZE', '1', '1', 'cs://1/image/aW1hZ2UvTVRvM00yTXlOek0yTnpnNU0yVXhOR1JtWW1Gak9Ea3dZemxpWkdFMU9EVXpaQQ', '2018-07-31 14:49:45', '2018-07-31 14:49:45');

INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('15', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '红头文件发文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"文件标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文号\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"主送单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"主送单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"抄送单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"抄送单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件份数\",\"fieldExtra\":\"{\\\"defaultValue\\\":\\\"\\\"}\",\"fieldName\":\"文件份数\",\"fieldType\":\"NUMBER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"NUM_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('16', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '非红头文件发文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"文件标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"文件标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"是否盖章\",\"fieldExtra\":\"{\\\"selectValue\\\":[\\\"是\\\",\\\"否\\\"]}\",\"fieldName\":\"是否盖章\",\"fieldType\":\"DROP_BOX\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('17', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '行政收文（紧急）申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"收文日期\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"收文日期\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文编号\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文编号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES ('18', '0', '0', '0', 'EhOrganizations', '52000', 'any-module', '行政收文申请表', '0', 'DEFAULT_JSON', '[{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"发文单位\",\"fieldExtra\":\"{}\",\"fieldName\":\"发文单位\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请选择\",\"fieldDisplayName\":\"收文日期\",\"fieldExtra\":\"{\\\"type\\\":\\\"DATE\\\"}\",\"fieldName\":\"收文日期\",\"fieldType\":\"DATE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文编号\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文编号\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"收文标题\",\"fieldExtra\":\"{}\",\"fieldName\":\"收文标题\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDisplayName\":\"文件上传\",\"fieldExtra\":\"{\\\"limitCount\\\":3,\\\"limitPerSize\\\":10485760}\",\"fieldName\":\"文件上传\",\"fieldType\":\"FILE\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"FILE_COUNT_SIZE_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dynamicFlag\":0,\"fieldDesc\":\"请输入\",\"fieldDisplayName\":\"备注信息\",\"fieldExtra\":\"{}\",\"fieldName\":\"备注信息\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"}]', '1', '1', '2018-07-31 14:57:08', '2018-07-31 14:57:08');


-- --------------------- SECTION END ---------------------------------------------------------


