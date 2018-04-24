--【深圳湾mybay】物业缴费对接
SET @asset_vendor_id = IFNULL((SELECT MAX(id) FROM `eh_asset_vendor`),1);
INSERT INTO `eh_asset_vendor` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `status`, `namespace_id`)
select @asset_vendor_id := @asset_vendor_id + 1, 'community', id, name, 'SZW', '2', '999966' from eh_communities where namespace_id = 999966;
--【深圳湾mybay】物业缴费对接：我方提供发送消息通知接口给金蝶EAS
SET @eh_apps_id = IFNULL((SELECT MAX(id) FROM `eh_apps`), 0);
INSERT INTO `eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`, `update_uid`, `update_time`)
VALUES ((@eh_apps_id := @eh_apps_id + 1), 1, '03c9d78c-5369-4269-8a46-2058d1c54696', 'S/IxJYKRq1y2Sk6Mh0jE3NVfNm7T91CL+V3cR8f9QC+p04XaJqZ5gsjtuWxTKe0B8/soqELcJfyul1FoES/P6w==', 'Kingdee', NULL, 1, now(), NULL, NULL);