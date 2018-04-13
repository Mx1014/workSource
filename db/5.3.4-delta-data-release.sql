-- ���������˵�����ʾ���� by xiongying20180413
INSERT INTO `eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES ('95', '20400', '0', '���������˵�');

SET @exclude_id = (SELECT MAX(id) FROM `eh_service_module_exclude_functions`);
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES ((@exclude_id := @exclude_id + 1), '999971', NULL, '20400', '95');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES ((@exclude_id := @exclude_id + 1), '999983', NULL, '20400', '95');


-- 物业缴费对接 by 王者行 2018/4/13
SET @asset_vendor_id = IFNULL((SELECT MAX(id) FROM `eh_asset_vendor`),1);
INSERT INTO `eh_asset_vendor` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `status`, `namespace_id`)
select @asset_vendor_id := @asset_vendor_id + 1, 'community', id, name, 'SZW', '2', '999966' from eh_communities where namespace_id = 999966;
-- 物业缴费对接：我方提供发送消息通知接口给金蝶EAS by 王者行 2018/4/13
SET @eh_apps_id = IFNULL((SELECT MAX(id) FROM `eh_apps`), 0);
INSERT INTO `eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`, `update_uid`, `update_time`)
VALUES ((@eh_apps_id := @eh_apps_id + 1), 1, '03c9d78c-5369-4269-8a46-2058d1c54696', 'S/IxJYKRq1y2Sk6Mh0jE3NVfNm7T91CL+V3cR8f9QC+p04XaJqZ5gsjtuWxTKe0B8/soqELcJfyul1FoES/P6w==', 'Kingdee', NULL, 1, now(), NULL, NULL);

