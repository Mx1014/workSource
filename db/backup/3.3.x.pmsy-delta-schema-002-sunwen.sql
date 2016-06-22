# add siyuan order column project_id and customer_id
ALTER TABLE `eh_pmsy_orders` ADD COLUMN `project_id` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the id of siyuan project';
ALTER TABLE `eh_pmsy_orders` ADD COLUMN `customer_id` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the id of customer according the third system, siyuan';


# add 服务热线
DELETE FROM `eh_launch_pad_items` WHERE id=1764;
DELETE FROM `eh_launch_pad_items` WHERE id=1767;
UPDATE `eh_launch_pad_items` SET item_name = '家政服务',item_label = '家政服务' WHERE id = 1765;
UPDATE `eh_launch_pad_items` SET item_name = '家政服务',item_label = '家政服务' WHERE id = 1768;

INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) 
	VALUES (1764, 999993, 0, '0', 0, '/home/Pm', 'GaActions', '便民服务', '便民服务', 'cs://1/image/aW1hZ2UvTVRwaFptSXdNMlJtWWpSak9XVm1ZekV4TXpReE5tRXdPRFJrTlRZNVpHUTJOdw', 1, 1, 45, '', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) 
	VALUES (1767, 999993, 0, '0', 0, '/home/Pm', 'GaPosts', '便民服务', '便民服务', NULL, 1, 1, 45, '', 0, 0, 1, 1, '', 0,NULL);
	
	
	INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES 
('94', 'haian.siyuan', 'http://sysuser.kmdns.net:9090/NetApp/SYS86Service.asmx/GetSYS86Service', 'the url of siyuan wuyw', '0', NULL);



INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) VALUES 
('1', '0', '24210090697427178', '00100020090900000003', '联系电话', '提示信息');
