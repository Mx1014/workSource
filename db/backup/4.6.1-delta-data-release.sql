-- 错误提示，add by tt, 20170527
SELECT MAX(id) INTO @id FROM `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'talent', '1', 'zh_CN', '本功能仅对企业管理员开放，如有需要请联系企业管理员');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'talent', '2', 'zh_CN', '分类名称不能重复');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'talent', '3', 'zh_CN', '导入失败，请检查数据是否按要求填写');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'parameters.error', '10002', 'zh_CN', '参数长度不足');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'parameters.error', '10003', 'zh_CN', '参数不能为空');

-- 默认头像，add by tt, 20170527
SELECT MAX(id) INTO @id FROM `eh_configurations`;
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@id:=@id+1, 'talent.male.uri', 'cs://1/image/aW1hZ2UvTVRwaE1tRmpOalEwWVRJME1UZ3pOalUwT0RKa09HRTNZV0ZtWkdabFpHTmtOdw', 'talen default male uri', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@id:=@id+1, 'talent.female.uri', 'cs://1/image/aW1hZ2UvTVRwa04yRTNaR1ZrWlRreU1XWmhabVV3WkdZd05qQTFZamxqTURSa1pHSmlNQQ', 'talen default female uri', 0, NULL);

-- 菜单，add by tt, 20170527
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES (40730, '企业人才', 40000, NULL, 'react:/enterprise-management/talent-list', 1, 2, '/40730/70100', 'park', 498, 40730);

-- 权限，add by tt, 20170527
SELECT MAX(id) INTO @pri_id FROM `eh_acl_privileges`;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@pri_id:=@pri_id+1, 0, '企业人才 管理员', '企业人才 业务模块权限', NULL);

-- 菜单关联权限，add by tt, 20170527
SELECT MAX(id) INTO @id FROM `eh_web_menu_privileges`;
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@id+1, @pri_id, 40730, '企业人才', 1, 1, '企业人才 全部权限', 714);

-- 权限赋予超管，add by tt, 20170527
SELECT MAX(id) INTO @id FROM `eh_acls`;
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `comment_tag1`, `comment_tag2`, `comment_tag3`, `comment_tag4`, `comment_tag5`) VALUES (@id+1, 'EhOrganizations', NULL, 1, @pri_id, 1001, 0, 1, '2017-06-05 09:41:07', 0, 'EhAclRoles', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- 显示菜单，add by tt, 20170527
SELECT MAX(id) INTO @id FROM `eh_web_menu_scopes`;
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id+1, 40730, '', 'EhNamespaces', 999990, 2);

-- 不同环境执行不同的语句！！！！
-- 服务市场配置，add by tt, 20170527(90)
-- select max(id) into @id from `eh_launch_pad_items`;
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 0, 0, '/home', 'Bizs', '企业人才', '企业人才', 'cs://1/image/aW1hZ2UvTVRvNE56STNNamxsWW1GalpUUTJOamhpTVRKbFpqTTBOVEJoTkRrelpUZzJaUQ', 1, 1, 14, '{"url":"http://10.1.10.90/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL, NULL, 0, NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 0, 0, '/home', 'Bizs', '企业人才', '企业人才', 'cs://1/image/aW1hZ2UvTVRvNE56STNNamxsWW1GalpUUTJOamhpTVRKbFpqTTBOVEJoTkRrelpUZzJaUQ', 1, 1, 14, '{"url":"http://10.1.10.90/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL, NULL, 0, NULL);

-- 服务市场配置，add by tt, 20170527(alpha)
-- select max(id) into @id from `eh_launch_pad_items`;
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 0, 0, '/home', 'Bizs', '企业人才', '企业人才', 'cs://1/image/aW1hZ2UvTVRvNE56STNNamxsWW1GalpUUTJOamhpTVRKbFpqTTBOVEJoTkRrelpUZzJaUQ', 1, 1, 14, '{"url":"http://alpha.lab.everhomes.com/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL, NULL, 0, NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 0, 0, '/home', 'Bizs', '企业人才', '企业人才', 'cs://1/image/aW1hZ2UvTVRvNE56STNNamxsWW1GalpUUTJOamhpTVRKbFpqTTBOVEJoTkRrelpUZzJaUQ', 1, 1, 14, '{"url":"http://alpha.lab.everhomes.com/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL, NULL, 0, NULL);

-- 服务市场配置，add by tt, 20170527(beta)
-- select max(id) into @id from `eh_launch_pad_items`;
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 0, 0, '/home', 'Bizs', '企业人才', '企业人才', 'cs://1/image/aW1hZ2UvTVRvNE56STNNamxsWW1GalpUUTJOamhpTVRKbFpqTTBOVEJoTkRrelpUZzJaUQ', 1, 1, 14, '{"url":"http://beta.zuolin.com/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL, NULL, 0, NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 0, 0, '/home', 'Bizs', '企业人才', '企业人才', 'cs://1/image/aW1hZ2UvTVRvNE56STNNamxsWW1GalpUUTJOamhpTVRKbFpqTTBOVEJoTkRrelpUZzJaUQ', 1, 1, 14, '{"url":"http://beta.zuolin.com/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL, NULL, 0, NULL);

-- 服务市场配置，add by tt, 20170527(core)
-- select max(id) into @id from `eh_launch_pad_items`;
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 0, 0, '/home', 'Bizs', '企业人才', '企业人才', 'cs://1/image/aW1hZ2UvTVRvNE56STNNamxsWW1GalpUUTJOamhpTVRKbFpqTTBOVEJoTkRrelpUZzJaUQ', 1, 1, 14, '{"url":"http://core.zuolin.com/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL, NULL, 0, NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 0, 0, '/home', 'Bizs', '企业人才', '企业人才', 'cs://1/image/aW1hZ2UvTVRvNE56STNNamxsWW1GalpUUTJOamhpTVRKbFpqTTBOVEJoTkRrelpUZzJaUQ', 1, 1, 14, '{"url":"http://core.zuolin.com/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL, NULL, 0, NULL);


-- 导出无数据提示，add by tt, 20170522
SELECT MAX(id) INTO @id FROM `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id+1, 'organization', '800000', 'zh_CN', '无数据！');

-- 初始化organization表中是否设置了企业管理员标记，add by tt, 20170522
UPDATE eh_organizations t1
SET t1.set_admin_flag = 1
WHERE t1.`status` = 2
AND t1.parent_id = 0
AND t1.group_type = 'ENTERPRISE'
AND EXISTS (
	SELECT 1
	FROM eh_acl_role_assignments t2
	WHERE t2.owner_type = 'EhOrganizations'
	AND t2.owner_id = t1.id
	AND t2.target_type = 'EhUsers'
	AND t2.role_id = 1005
);


-- 服务联盟初始化排序的序号为id  by dengs, 20170523
UPDATE `eh_service_alliances` SET `default_order` = `id`;

-- 将服务联盟下的菜单机构管理改为服务管理 by dengs,20170524
UPDATE `eh_web_menus` SET `name`='服务管理' WHERE `id` = 40520;

-- added by wh  增加审批管理菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
VALUES('52000','审批管理','50000',NULL,'react:/approval-management/approval-list/51000','0','2','/50000/51000','park','591','52000');
-- 给科兴加
SET @id = (SELECT MAX(id) FROM eh_web_menu_scopes);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'52000','','EhNamespaces','999983','2');

-- 加module
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES('52000','审批管理','50000','/50000/51000','0','2','2','0','2017-05-19 11:50:20');

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10137, 0, '审批管理', '审批管理 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10137, 52000, '审批管理', 1, 1, '审批管理  全部权限', 202);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 10137, 1005, 'EhAclRoles', 0, 1, NOW());
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 10137, 1001, 'EhAclRoles', 0, 1, NOW());

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), '52000', '1', '10137', NULL, '0', UTC_TIMESTAMP());
SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999983, 52000, '审批管理', 'EhNamespaces', 999983, NULL, 2);

SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
 VALUES((@eh_launch_pad_items_id:=@eh_launch_pad_items_id+1),'999983','0','0','0','/home','Bizs','审批','审批','cs://1/image/aW1hZ2UvTVRwak5qVTVZV1EyWkdVM1l6TTBaR1k1Tm1RMk56STVaVGt3TldGbU1HVmlOQQ','1','1','65','','4','0','1','1','','0',NULL,NULL,NULL,'0','pm_admin','0',NULL,NULL,'0',NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES((@eh_launch_pad_items_id:=@eh_launch_pad_items_id+1),'999983','0','0','0','/home','Bizs','审批','审批','cs://1/image/aW1hZ2UvTVRwak5qVTVZV1EyWkdVM1l6TTBaR1k1Tm1RMk56STVaVGt3TldGbU1HVmlOQQ','1','1','65','','4','0','1','1','','0',NULL,NULL,NULL,'0','park_tourist','0',NULL,NULL,'0',NULL);




-- 打卡审批工作流的title，add by wh, 20170502
SET @id =(SELECT MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow.title','1','zh_CN','请假申请');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow.title','2','zh_CN','异常申请');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow.title','3','zh_CN','加班申请');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','creator','zh_CN','发起人');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','contact','zh_CN','联系电话');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','requestDate','zh_CN','申请日期');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','overtimeLength','zh_CN','加班时长');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','punchDetail','zh_CN','打卡详情');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','requestReson','zh_CN','申请理由');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','category','zh_CN','请假类型');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','absentTime','zh_CN','请假时间');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','absentLength','zh_CN','请假时长');


-- 打卡审批工作流的内容，add by wh, 20170502
SET @id =(SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'approval.flow.context','1','zh_CN','请假申请的内容','请假类型：${absentCategory}\n请假时间${beginTime}至${endTime}',0);
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'approval.flow.context','2','zh_CN','异常申请的内容','异常日期：${exceptionDate}\n打卡详情：${punchDetail}',0);
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'approval.flow.context','3','zh_CN','加班申请的内容','加班日期：${overTimeDate}\n加班时长：${timeLength}',0);
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'approval.flow','1','zh_CN','请假申请的时间','${beginTime}至${endTime}',0);

-- 工作流新增两个变量 add by xq.tian  2017/06/09
SET @flow_var_max_id = (SELECT MAX(id) FROM `eh_flow_variables`);
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ((@flow_var_max_id := @flow_var_max_id + 1), 0, 0, '', 0, '', 'user_applier_organization_manager', '发起人的企业管理员', 'node_user_processor', 'bean_id', 'flow-variable-applier-organization-manager', 1);
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ((@flow_var_max_id := @flow_var_max_id + 1), 0, 0, '', 0, '', 'user_applier_department_manager', '发起人的部门经理', 'node_user_processor', 'bean_id', 'flow-variable-applier-department-manager', 1);

-- 新的二维码支持与门禁权限 added by janson
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (1000000, 'aclink.qr_driver_zuolin_inner', 'zuolin_v2', 'use version2 of zuolin driver');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (1000000, 'aclink.join_company_auto_auth', 'building,176121,DE:2E:71:67:3A:2F;company,1000001,DE:2E:71:67:3A:2F', '为生产力大楼自动授权');

-- remove qr driver config by janson
delete from `eh_configurations` where `name` = 'aclink.qr_driver_zuolin_inner' and `namespace_id` = 1000000 limit 1;

-- update menu add by xq.tian 2017/06/16
UPDATE eh_web_menus SET data_type='react:/approval-management/approval-list/40500/community' WHERE id =40541;

UPDATE eh_web_menus
SET data_type = 'react:/approval-management/approval-list/52000/EhOrganizations'
WHERE id = 52000;

-- added by janson, fix techpark qr sms 20170619
UPDATE `eh_locale_templates` SET `description`='${username}已授权给你${doorname}门禁二维码，请点击以下链接使用：https://core.zuolin.com/evh/aclink/v?id=${id}（24小时有效）' where `code`= 8 and `namespace_id` = 1000000;

-- added by janson, add zuolin_v2
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (1000000, 'aclink.qr_driver_zuolin_inner', 'zuolin_v2', 'use version2 of zuolin driver');

-- added by janson, update action_data
UPDATE `eh_launch_pad_items` SET `action_data`='{\"isSupportQR\":1,\"isSupportSmart\":0, \"isSupportKeyShowing\":1}' where namespace_id = 1000000 and action_type = 40 and scope_code =0;

