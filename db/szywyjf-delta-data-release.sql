--【深圳湾mybay】物业缴费对接
SET @asset_vendor_id = IFNULL((SELECT MAX(id) FROM `eh_asset_vendor`),1);
INSERT INTO `eh_asset_vendor` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `status`, `namespace_id`)
select @asset_vendor_id := @asset_vendor_id + 1, 'community', id, name, 'SZW', '2', '999966' from eh_communities where namespace_id = 999966;
--【深圳湾mybay】物业缴费对接：我方提供发送消息通知接口给金蝶EAS
SET @eh_apps_id = IFNULL((SELECT MAX(id) FROM `eh_apps`), 0);
INSERT INTO `eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`, `update_uid`, `update_time`)
VALUES ((@eh_apps_id := @eh_apps_id + 1), 1, '03c9d78c-5369-4269-8a46-2058d1c54696', 'S/IxJYKRq1y2Sk6Mh0jE3NVfNm7T91CL+V3cR8f9QC+p04XaJqZ5gsjtuWxTKe0B8/soqELcJfyul1FoES/P6w==', 'Kingdee', NULL, 1, now(), NULL, NULL);

-- 深圳湾webservice对接访问地址配置 by 杨崇鑫
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('asset.shenzhenwan.EASLogin_address', 'http://192.168.1.200:6888/ormrpc/services/EASLogin', '深圳湾webservice对接', 999966, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('asset.shenzhenwan.WSWSSyncMyBayFacade_address', 'http://192.168.1.200:6888/ormrpc/services/WSWSSyncMyBayFacade', '深圳湾webservice对接', 999966, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('asset.shenzhenwan.username', 'mybay', '深圳湾webservice对接用户名', 999966, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('asset.shenzhenwan.password', 'mybay', '深圳湾webservice对接密码', 999966, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('asset.shenzhenwan.slnName', 'eas', '深圳湾webservice对接slnName', 999966, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('asset.shenzhenwan.dcName', 'cs200', '深圳湾webservice对接dcName', 999966, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('asset.shenzhenwan.language', 'l2', '深圳湾webservice对接language', 999966, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('asset.shenzhenwan.dbType', '2', '深圳湾webservice对接dbType', 999966, NULL);