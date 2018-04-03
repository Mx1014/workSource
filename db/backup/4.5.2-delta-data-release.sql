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
 
-- 打卡/审批状态的文字描述，add by wh, 20170411
SET @id =(SELECT MAX(id) FROM eh_locale_strings); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','0','zh_CN','正常');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','1','zh_CN','迟到');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','2','zh_CN','早退');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','3','zh_CN','缺勤');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','4','zh_CN','迟到且早退');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','5','zh_CN','事假');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','6','zh_CN','病假');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','7','zh_CN','调休');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','8','zh_CN','公出');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','9','zh_CN','加班');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','10','zh_CN','半天事假'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','11','zh_CN','半天病假'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','12','zh_CN','半天调休'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','13','zh_CN','半天公出'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','14','zh_CN','忘打卡'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','15','zh_CN','已离职'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','16','zh_CN','未入职'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.excel','schedule','zh_CN','排班表'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.excel','rule','zh_CN','排班');  
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.push','1','zh_CN','考勤提醒：还有5分钟就要上班了，别忘记打卡哦');   
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.default','timeRuleName','zh_CN','休息');  
 

-- 考勤排班的菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES('50634','查看排班表','50630',NULL,'punch--viewScheduling','0','2','/50000/50600/50630/50634','park',556,NULL);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 1000000, 2); 
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999992, 2);



SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10039, 50634, '考勤管理', 1, 1, '考勤管理 全部权限', 710);


-- merge from shenye201704 by xiongying 20170509
UPDATE eh_equipment_inspection_tasks t SET t.namespace_id = (SELECT o.namespace_id FROM eh_organizations o WHERE o.id = t.owner_id);
UPDATE eh_equipment_inspection_item_results r SET r.standard_id = (SELECT t.standard_id FROM eh_equipment_inspection_tasks t WHERE t.id = r.task_id);
UPDATE eh_equipment_inspection_item_results r SET r.equipment_id = (SELECT t.equipment_id FROM eh_equipment_inspection_tasks t WHERE t.id = r.task_id);
UPDATE eh_equipment_inspection_item_results r SET r.community_id = (SELECT t.target_id FROM eh_equipment_inspection_tasks t WHERE t.id = r.task_id);
UPDATE eh_equipment_inspection_item_results r SET r.inspection_category_id =(SELECT t.inspection_category_id FROM eh_equipment_inspection_tasks t WHERE t.id = r.task_id);
UPDATE eh_equipment_inspection_item_results r SET r.namespace_id = (SELECT t.namespace_id FROM eh_equipment_inspection_tasks t WHERE t.id = r.task_id);

UPDATE eh_equipment_inspection_task_logs r SET r.community_id = (SELECT t.target_id FROM eh_equipment_inspection_tasks t WHERE t.id = r.task_id);
UPDATE eh_equipment_inspection_task_logs r SET r.inspection_category_id = (SELECT t.inspection_category_id FROM eh_equipment_inspection_tasks t WHERE t.id = r.task_id);
UPDATE eh_equipment_inspection_task_logs r SET r.namespace_id = (SELECT t.namespace_id FROM eh_equipment_inspection_tasks t WHERE t.id = r.task_id);


UPDATE eh_web_menus SET data_type = 'react:/equipment-inspection/equipment-list' WHERE NAME LIKE '巡检对象';
UPDATE eh_web_menus SET data_type = '' WHERE id = 20850;

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`) 
VALUES (20851, '总览', 20850, NULL, 'react:/equipment-inspection/statistics-pandect', 0, 2, '/20000/20800/20850/20851', 'park', 310);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`) 
VALUES (20852, '查看所有任务', 20850, NULL, 'react:/equipment-inspection/statistics-task', 0, 2, '/20000/20800/20850/20852', 'park', 311);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) 
VALUES (20030, 0, '总览', '总览', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) 
VALUES (20031, 0, '巡检统计查看所有任务', '巡检统计查看所有任务', NULL);
SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 20030, 20851, '总览', 1, 1, '总览', 310); 
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 20031, 20852, '查看所有任务', 1, 1, '查看所有任务', 311); 

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20851, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20852, '', 'EhNamespaces', 999992, 2);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) 
VALUES ('20851', '总览', '20850', '/20000/20800/20850/20851', '0', '2', '2', '0', UTC_TIMESTAMP()); 
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) 
VALUES ('20852', '查看所有任务', '20850', '/20000/20800/20850/20852', '0', '2', '2', '0', UTC_TIMESTAMP()); 

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), '20851', '1', '20030', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), '20852', '1', '20031', NULL, '0', UTC_TIMESTAMP());

SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999992, 20851, '总览', 'EhNamespaces', 999992, NULL, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999992, 20852, '查看所有任务', 'EhNamespaces', 999992, NULL, 2);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 20030, 1005, 'EhAclRoles', 0, 1, NOW()); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 20030, 1001, 'EhAclRoles', 0, 1, NOW()); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 20031, 1005, 'EhAclRoles', 0, 1, NOW()); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 20031, 1001, 'EhAclRoles', 0, 1, NOW()); 

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), '10011', '20851', '设备巡检', '1', '1', '设备巡检 管理员权限', '710');
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), '10011', '20852', '设备巡检', '1', '1', '设备巡检 管理员权限', '710');


-- merge form feedback-1.0
-- 举报管理菜单 仅适用于管理公司企业后台。功能放置于内部管理分类下。不分项目   add by yanjun  20170428
--
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`) 
VALUES (51000, '举报管理', 50000, NULL, 'react:/feedback-management/feedback-list', 0, 2, '/50000/51000', 'park', 591);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) 
VALUES (10160, 0, '举报管理', '举报管理 管理员', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10160, 51000, '举报管理', 1, 1, '举报管理  管理员权限', 710); 

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 10160, 1001, 'EhAclRoles', 0, 1, NOW()); 

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 1000000, 2);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) 
VALUES ('51000', '举报管理', '50000', '/50000/51000', '0', '2', '2', '0', UTC_TIMESTAMP()); 

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), '51000', '1', 10160, NULL, '0', UTC_TIMESTAMP());

SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 1000000, 51000, '举报管理', NULL, 2);

-- merge form feedback-1.0
-- 上面是1000000，以下为其他空间  add by yanjun  20170428

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 1, 2);

SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 1, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999975, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999975, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999976, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999976, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999979, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999979, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999980, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999980, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999981, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999981, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999982, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999982, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999983, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999984, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999985, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999985, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999986, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999986, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999987, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999987, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999988, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999988, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999989, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999989, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999990, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999991, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999991, 51000, '举报管理', NULL, 2);


INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999992, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999993, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999993, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999994, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999994, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999995, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999995, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999996, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999996, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999997, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999997, 51000, '举报管理', NULL, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999998, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999998, 51000, '举报管理', NULL, 2);


INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999999, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999999, 51000, '举报管理', NULL, 2);


INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 1000001, 2);


INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 1000001, 51000, '举报管理', NULL, 2);


-- 车辆放行导出excel无数据提示，add by tt, 20170510（清华）
SELECT MAX(id) INTO @id FROM `eh_locale_strings`; 
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id+1, 'parking.clearance', '10011', 'zh_CN', '没有数据');


-- 修改帖子评论发消息内容模板，add by tt, 20170511
UPDATE eh_locale_templates SET TEXT = '新的评论\t${userName}评论了你的帖子「${postName}」。' WHERE  scope = 'forum.notification' AND CODE  =  2;
UPDATE eh_locale_templates SET TEXT = '新的回复\t${userName}回复了你在帖子「${postName}」 的评论。' WHERE  scope = 'forum.notification' AND CODE  =  3;

-- by dengs 20170512 国际化 邮件发送
SELECT MAX(id) INTO @id FROM eh_locale_strings;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'serviceAlliance.request.notification', '10001', 'zh_CN', '的申请单');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'serviceAlliance.request.notification', '10002', 'zh_CN', '见邮件附件');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'serviceAlliance.request.notification', '10003', 'zh_CN', '审批');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'serviceAlliance.request.notification', '10004', 'zh_CN', '序号,用户姓名,手机号码,企业,服务机构,提交时间');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'serviceAlliance.request.notification', '10005', 'zh_CN', '申请类型,申请来源,服务机构');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'serviceAlliance.request.notification', '10006', 'zh_CN', '姓名,联系电话,企业');

SELECT MAX(id) INTO @id FROM eh_locale_templates;
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`,`locale`, `description`, `text`)
    VALUES(@id:=@id+1, 'serviceAlliance.request.notification', 4, 'zh_CN', '提交申请通知给机构和管理员', '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>${title}</title></head><body><p>预订人：${creatorName}</p><p>手机号：${creatorMobile}</p><p>公司名称：${creatorOrganization}</p><p>服务名称：${serviceOrgName}</p>${note}</body></html>');
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`,`locale`, `description`, `text`)
    VALUES(@id:=@id+1, 'serviceAlliance.request.notification', 5, 'zh_CN', '邮件内容生成PDF文件', '预订人：${creatorName}\n手机号：${creatorMobile}\n公司名称：${creatorOrganization}\n服务名称：${serviceOrgName}\n');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1,'serviceAlliance.request.notification', 6, 'zh_CN', '邮件主题', '${serviceOrgName}的申请单', 0);

-- by dengs 20170512 服务联盟后台菜单（审批列表、申请记录）移动到上一层
UPDATE `eh_web_menus` SET  `parent_id` = 40500,`path` = '/40000/40500/40541' WHERE ID = 40541;
UPDATE `eh_web_menus` SET  `parent_id` = 40500,`path` = '/40000/40500/40542' WHERE ID = 40542;

DELETE FROM `eh_web_menu_scopes` WHERE `menu_id`=40540;
DELETE FROM `eh_web_menu_privileges` WHERE `menu_id`=40540;
DELETE FROM `eh_web_menus` WHERE `id`=40540;

-- added by wh 加考勤排班各域空间的菜单
 
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 1, 2); 
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999993, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999994, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999991, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999989, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999988, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999987, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999986, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999985, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999982, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999981, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999980, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999979, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999978, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999977, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999976, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999975, 2); 

-- 康利添加举报管理菜单 add by yanjun 201705152031
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhNamespaces', 999978, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999978, 51000, '举报管理', NULL, 2);


INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES('user.agent.blacklist','Apache-HttpClient','UserAgent黑名单，含该关键字的UserAgent会被禁止访问，多个关键字使用逗号分隔','0',NULL);
