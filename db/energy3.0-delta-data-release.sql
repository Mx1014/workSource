-- 能耗权限细化
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(49100,'能耗管理',49100,'/20000/49100','1','2','2','0',NOW()); 
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(49110,'表计管理',49100,'/20000/49100/49110','1','3','2','0',NOW()); 
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(49120,'抄表记录',49100,'/20000/49100/49120','1','3','2','0',NOW()); 
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(49130,'统计信息',49100,'/20000/49100/49130','1','3','2','0',NOW()); 
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(49140,'参数设置',49100,'/20000/49100/49140','1','3','2','0',NOW()); 

SET @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
SET @privilege_id = (SELECT MAX(id) FROM `eh_acl_privileges`);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (20999, '0', '能耗管理 管理员', '能耗管理 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'49100','1','20999','能耗管理管理权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'49100','2','20999','能耗管理全部权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21000, '0', '能耗管理 新增表计权限', '能耗管理 新增表计权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'49110','0',21000,'能耗管理 新增表计权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21001, '0', '能耗管理 批量修改权限', '能耗管理 批量修改权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'49110','0',21001,'能耗管理 批量修改权限','0',NOW());
    
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21002, '0', '能耗管理 导入表计权限', '能耗管理 导入表计权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'49110','0',21002,'能耗管理 导入表计权限','0',NOW());    
    
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21003, '0', '能耗管理 抄表权限', '能耗管理 抄表权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'49110','0',21003,'能耗管理 抄表权限','0',NOW());
    
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21004, '0', '能耗管理 查看表计权限', '能耗管理 查看表计权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'49110','0',21004,'能耗管理 查看表计权限','0',NOW());
    
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21005, '0', '能耗管理 换表权限', '能耗管理 换表权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'49110','0',21005,'能耗管理 换表权限','0',NOW());
 
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21006, '0', '能耗管理 删除/报废表计权限', '能耗管理 删除/报废表计权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'49110','0',21006,'能耗管理 删除/报废表计权限','0',NOW());
    
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21007, '0', '能耗管理 查看抄表记录权限', '能耗管理 查看抄表记录权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'49120','0',21007,'能耗管理 查看抄表记录权限','0',NOW());
    
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21008, '0', '能耗管理 删除抄表记录权限', '能耗管理 删除抄表记录权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'49120','0',21000,'能耗管理 删除抄表记录权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21009, '0', '能耗管理 查看每日水电总表权限', '能耗管理 查看每日水电总表权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'49130','0',21000,'能耗管理 查看每日水电总表权限','0',NOW());
    
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21010, '0', '能耗管理 查看月度水电分析表权限', '能耗管理 查看月度水电分析表权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'49130','0',21000,'能耗管理 查看月度水电分析表权限','0',NOW());
    
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21011, '0', '能耗管理 查看年度水电用量收支对比表', '能耗管理 查看年度水电用量收支对比表', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'49130','0',21000,'能耗管理 查看年度水电用量收支对比表','0',NOW());    

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21012, '0', '能耗管理 查看各项目月水电能耗情况权限', '能耗管理 查看各项目月水电能耗情况权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'49130','0',21000,'能耗管理 查看各项目月水电能耗情况权限','0',NOW());  

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21013, '0', '能耗管理 参数设置权限', '能耗管理 参数设置权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'49140','0',21000,'能耗管理 参数设置权限','0',NOW());  
    
-- 提醒
SET @max_template_id = IFNULL((SELECT MAX(id) FROM `eh_locale_templates`), 1);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'energy.notification', 1, 'zh_CN', '任务过期前提醒', '你的抄表任务${taskName}将在${time}结束，请尽快处理。', 0);
