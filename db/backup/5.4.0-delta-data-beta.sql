-- 上线后请调用接口，/officecubicle/dataMigration 工位预定数据迁移


-- 深圳湾mybay物业缴费对接 by 杨崇鑫
SET @asset_vendor_id = IFNULL((SELECT MAX(id) FROM `eh_asset_vendor`),1);
INSERT INTO `eh_asset_vendor` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `status`, `namespace_id`)
select @asset_vendor_id := @asset_vendor_id + 1, 'community', id, name, 'SZW', '2', '999966' from eh_communities where namespace_id = 999966;
-- 深圳湾mybay物业缴费对接：我方提供发送消息通知接口给金蝶EAS by 杨崇鑫
SET @eh_apps_id = IFNULL((SELECT MAX(id) FROM `eh_apps`), 0);
INSERT INTO `eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`, `update_uid`, `update_time`)
VALUES ((@eh_apps_id := @eh_apps_id + 1), 1, '03c9d78c-5369-4269-8a46-2058d1c54696', 'S/IxJYKRq1y2Sk6Mh0jE3NVfNm7T91CL+V3cR8f9QC+p04XaJqZ5gsjtuWxTKe0B8/soqELcJfyul1FoES/P6w==', 'Kingdee', NULL, 1, now(), NULL, NULL);
-- 为深圳湾增加物业查费入口（物业缴费模块还没有对接严军的应用发布） by 杨崇鑫
set @id = (select MAX(`id`) from `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`, `preview_portal_version_id`) VALUES (@id:=@id+1, '999966', '0', '1', '0', '/home', 'Bizs', '费用查询', '物业查费', null, '1', '1', '13', '{\"url\":\"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\"}', '1', '0', '1', '1', NULL, '0', NULL, NULL, NULL, '1', 'park_tourist', '1', NULL, NULL, '0', NULL, NULL, NULL);
-- 深圳湾webservice对接访问地址配置 by 杨崇鑫
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('asset.shenzhenwan.EASLogin_address', 'http://192.168.1.200:6888/ormrpc/services/EASLogin', '深圳湾webservice对接', 999966, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('asset.shenzhenwan.WSWSSyncMyBayFacade_address', 'http://192.168.1.200:6888/ormrpc/services/WSWSSyncMyBayFacade', '深圳湾webservice对接', 999966, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('asset.shenzhenwan.username', 'mybay', '深圳湾webservice对接用户名', 999966, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('asset.shenzhenwan.password', 'mybay', '深圳湾webservice对接密码', 999966, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('asset.shenzhenwan.slnName', 'eas', '深圳湾webservice对接slnName', 999966, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('asset.shenzhenwan.dcName', 'cs200', '深圳湾webservice对接dcName', 999966, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('asset.shenzhenwan.language', 'l2', '深圳湾webservice对接language', 999966, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('asset.shenzhenwan.dbType', '2', '深圳湾webservice对接dbType', 999966, NULL);
-- 增加深圳湾合同详情模块接口调用配置 by 杨崇鑫
SET @config_id = IFNULL((SELECT MAX(id) FROM `eh_configurations`),1);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@config_id :=@config_id+1), 'contractService', '999966', '增加深圳湾合同详情模块接口', '999966', NULL);
 


