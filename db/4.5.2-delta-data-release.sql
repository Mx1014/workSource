-- 审批模板 add by xq.tian  2017/04/17
UPDATE `eh_locale_templates` SET `text` = '${userName}申请加入“${groupName}” （理由：${reason}），是否同意？'
WHERE `scope` = 'group.notification' AND `code` = 44 AND `namespace_id` = 0;
UPDATE `eh_locale_templates` SET `text` = '${userName}${description}申请加入公司“${enterpriseName}”，是否同意？'
WHERE `scope` = 'enterprise.notification' AND `code` = 7 AND `namespace_id` = 0;
UPDATE `eh_locale_templates` SET `text` = '您的家人${userName}申请加入${address}，是否同意？'
WHERE `scope` = 'family.notification' AND `code` = 2 AND `namespace_id` = 0;

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


-- 配置能量加油站, add by tt, 20170509（已执行）
select max(id) into @id from `eh_launch_pad_items`;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 1000000, 0, 4, 178170, '/home', 'Bizs', '能量加油站', '能量加油站', 'cs://1/image/aW1hZ2UvTVRwbU4yRXhNek0xTldVek1EaGhPV1ZrT0RVM1pEUmtPR1kzWXpKbU5qUXlOZw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14937905385175861793%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 1000000, 0, 4, 178170, '/home', 'Bizs', '能量加油站', '能量加油站', 'cs://1/image/aW1hZ2UvTVRwbU4yRXhNek0xTldVek1EaGhPV1ZrT0RVM1pEUmtPR1kzWXpKbU5qUXlOZw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14937905385175861793%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 1000000, 0, 4, 178307, '/home', 'Bizs', '能量加油站', '能量加油站', 'cs://1/image/aW1hZ2UvTVRwbU4yRXhNek0xTldVek1EaGhPV1ZrT0RVM1pEUmtPR1kzWXpKbU5qUXlOZw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14938834358936096746%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 1000000, 0, 4, 178307, '/home', 'Bizs', '能量加油站', '能量加油站', 'cs://1/image/aW1hZ2UvTVRwbU4yRXhNek0xTldVek1EaGhPV1ZrT0RVM1pEUmtPR1kzWXpKbU5qUXlOZw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14938834358936096746%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 4, 1006090, '/home', 'Bizs', '能量加油站', '能量加油站', 'cs://1/image/aW1hZ2UvTVRwbU4yRXhNek0xTldVek1EaGhPV1ZrT0RVM1pEUmtPR1kzWXpKbU5qUXlOZw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14937781453413755100%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 4, 1006090, '/home', 'Bizs', '能量加油站', '能量加油站', 'cs://1/image/aW1hZ2UvTVRwbU4yRXhNek0xTldVek1EaGhPV1ZrT0RVM1pEUmtPR1kzWXpKbU5qUXlOZw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14937781453413755100%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 4, 1001115, '/home', 'Bizs', '能量加油站', '能量加油站', 'cs://1/image/aW1hZ2UvTVRwbU4yRXhNek0xTldVek1EaGhPV1ZrT0RVM1pEUmtPR1kzWXpKbU5qUXlOZw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14937902738181683764%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 4, 1001115, '/home', 'Bizs', '能量加油站', '能量加油站', 'cs://1/image/aW1hZ2UvTVRwbU4yRXhNek0xTldVek1EaGhPV1ZrT0RVM1pEUmtPR1kzWXpKbU5qUXlOZw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14937902738181683764%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 1000000, 0, 4, 1011046, '/home', 'Bizs', '能量加油站', '能量加油站', 'cs://1/image/aW1hZ2UvTVRwbU4yRXhNek0xTldVek1EaGhPV1ZrT0RVM1pEUmtPR1kzWXpKbU5qUXlOZw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14937905385175861793%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 1000000, 0, 4, 1011046, '/home', 'Bizs', '能量加油站', '能量加油站', 'cs://1/image/aW1hZ2UvTVRwbU4yRXhNek0xTldVek1EaGhPV1ZrT0RVM1pEUmtPR1kzWXpKbU5qUXlOZw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14937905385175861793%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 1000000, 0, 4, 1011073, '/home', 'Bizs', '能量加油站', '能量加油站', 'cs://1/image/aW1hZ2UvTVRwbU4yRXhNek0xTldVek1EaGhPV1ZrT0RVM1pEUmtPR1kzWXpKbU5qUXlOZw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14938834358936096746%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 1000000, 0, 4, 1011073, '/home', 'Bizs', '能量加油站', '能量加油站', 'cs://1/image/aW1hZ2UvTVRwbU4yRXhNek0xTldVek1EaGhPV1ZrT0RVM1pEUmtPR1kzWXpKbU5qUXlOZw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14938834358936096746%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, NULL);


-- merge from shenye201704 by xiongying 20170509
update eh_equipment_inspection_tasks t set t.namespace_id = (select o.namespace_id from eh_organizations o where o.id = t.owner_id);
update eh_equipment_inspection_item_results r set r.standard_id = (select t.standard_id from eh_equipment_inspection_tasks t where t.id = r.task_id);
update eh_equipment_inspection_item_results r set r.equipment_id = (select t.equipment_id from eh_equipment_inspection_tasks t where t.id = r.task_id);
update eh_equipment_inspection_item_results r set r.community_id = (select t.target_id from eh_equipment_inspection_tasks t where t.id = r.task_id);
update eh_equipment_inspection_item_results r set r.inspection_category_id =(select t.inspection_category_id from eh_equipment_inspection_tasks t where t.id = r.task_id);
update eh_equipment_inspection_item_results r set r.namespace_id = (select t.namespace_id from eh_equipment_inspection_tasks t where t.id = r.task_id);

update eh_equipment_inspection_task_logs r set r.community_id = (select t.target_id from eh_equipment_inspection_tasks t where t.id = r.task_id);
update eh_equipment_inspection_task_logs r set r.inspection_category_id = (select t.inspection_category_id from eh_equipment_inspection_tasks t where t.id = r.task_id);
update eh_equipment_inspection_task_logs r set r.namespace_id = (select t.namespace_id from eh_equipment_inspection_tasks t where t.id = r.task_id);


update eh_web_menus set data_type = 'react:/equipment-inspection/equipment-list' where name like '巡检对象';
update eh_web_menus set data_type = '' where id = 20850;

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
