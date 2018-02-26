-- 公告管理 add by zhiwei.zhang

-- 域空间volgo
-- 增加公告管理模块信息
INSERT INTO eh_service_modules(id,name,parent_id,path,type,level,status,create_time,creator_uid,operator_uid,action_type,multiple_flag,module_control_type,default_order)
VALUE(57000,'公告管理',50000,'/50000/57000',1,2,2,NOW(),0,0,70,0,'org_control',0);

-- 增加公告模块与域空间的关联信息
SET @id = (SELECT MAX(id) FROM eh_service_module_scopes);
INSERT INTO eh_service_module_scopes(id,namespace_id,module_id,module_name,owner_type,owner_id,apply_policy)
VALUE(@id+1,1,57000,'公告管理','EhNamespaces',1,2);

-- volgo 添加公告图标.
SET @item_id = (SELECT MAX(id) FROM eh_launch_pad_items);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`) VALUES (@item_id := @item_id + 1, '1', '0', '0', '0', '/home', 'Bizs', '公告', '公告', 'cs://1/image/aW1hZ2UvTVRvNE5XWmpNakV4TW1VNFlUbG1aR0ppWWpoaU16RmxNekUxWWpFMk1XRXlZUQ', '1', '1', '70', '{"title":"公告管理"}', '10', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', NULL, NULL, '0', NULL,NULL);


-- end by zhiwei.zhang