-- 给一碑增加app_key by st.zheng
SET @id = (SELECT MAX(id) FROM eh_apps);
INSERT INTO `eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`) VALUES (@id:=@id+1, '1', 'c9620212-8877-11e7-b08e-0050569605f3', 'OmnSTXMJPqvCxW8n5AmkT1xSGnJ2sWZSyWcDUi32HAD7htoLLxuzGaZUPgRN9bew6mOBW55WliSbcXRV3laC3g==', 'yibei sign', 'yibei.app', '1', now());

-- 添加服务录入菜单 by st.zheng
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `category`) VALUES (20225, '服务录入', '20200', 'task_management_service_entry', '0', '2', '/20000/20200/20225', 'park', '420', '20100', '3', 'module');

set @privilege_id = (select privilege_id from eh_web_menu_privileges where menu_id=20220 );
set @eh_web_menu_privilege_id = (select max(id) from eh_web_menu_privileges);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@eh_web_menu_privilege_id+1, @privilege_id, '20225', '物业报修', '1', '1', '物业报修 管理员权限', '720');

set @menu_scope_id = (select max(id) from eh_web_menu_scopes);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@menu_scope_id  + 1, '20225', '', 'EhNamespaces', '999983', '2');

update eh_launch_pad_items set action_data='{"url":"zl://propertyrepair/create?type=user&taskCategoryId=1&displayName=报修"}',action_type=60 where namespace_id=999983 and item_label = '报修';

-- 修改owner_type by st.zheng
update eh_service_alliances set owner_id=(select organization_id from eh_organization_communities where eh_service_alliances.owner_id = community_id limit 0,1),owner_type = 'organaization' where owner_type = 'community';
