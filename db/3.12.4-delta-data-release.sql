-- 添加更新时间，add by tt, 20170111
update eh_users set update_time = now() where update_time is null;
update eh_organizations set update_time = now() where update_time is null;
update eh_organization_address_mappings set create_time = now(), update_time = now() where update_time is null;
update eh_rentalv2_orders set operate_time = now() where operate_time is null;
update eh_activities set update_time = now() where update_time is null;
update eh_office_cubicle_orders set create_time = now(), update_time = now() where update_time is null;

-- 金地同步数据代理的appKey，add by tt, 20170111
INSERT INTO `eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`, `update_uid`, `update_time`) VALUES (5001, 1, 'dca1f405-7675-4ac2-ab72-e16c918fd7c2', 'VgAddn6IGPK/gO44eUWutfjLuotMcyKz3ZpwAr2jAUSsJgIi50cvntxL4QOqgZYEXkcislDwrDmLzSeHuFWQgQ==', 'jin di idata proxy', 'jin di idata proxy', 1, '2016-11-09 11:49:16', NULL, NULL);

-- 金地同步数据限制域空间，add by tt, 20170111
select max(id) into @id from `eh_configurations`;
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@id+1, 'jindi.sync.namespace', '999989,999991', 'jindi sync data namespace', 0, NULL);

-- 活动管理权限管理模块 add sfyan 20170112
SET @service_module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@service_module_privilege_id := @service_module_privilege_id + 1), '10600', '0', '310', NULL, '0', UTC_TIMESTAMP());

-- 物业报修 add by sw 20170112
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) 
	VALUES ( 'pmtask', '10009', 'zh_CN', '用户已有该权限，不能重复添加！');
	
	