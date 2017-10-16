set @menu_id = 46000;
set @sub_menu_id1 = 46001;
set @sub_menu_id2 = 46002;
set @sub_menu_id3 = 46003;
set @sub_menu_id4 = 46004;
set @sub_menu_id5 = 46005;
set @sub_menu_id6 = 46006;
set @menu_name = '园区办事';
set @namespace_id = 999983;
set @data_type = '';
set @data_type1 = 'service_type_management';
set @data_type2 = 'service_alliance';
set @data_type3 = 'message_push_setting';
set @data_type4 = '';
set @data_type5 = CONCAT('list/',@menu_id,'/EhOrganizations');
set @data_type6 = 'apply_record';

-- 设置菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`level`,`condition_type`,`category`) VALUES (@menu_id,      @menu_name, 40000, 	 null, @data_type,  1, 2, CONCAT('/40000/',@menu_id),                   'park', 600, @menu_id, 2, '', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`level`,`condition_type`,`category`) VALUES (@sub_menu_id1, '类型管理', @menu_id, null, @data_type1, 0, 2, CONCAT('/40000/',@menu_id,'/',@sub_menu_id1), 'park', 601, @menu_id, 3, '', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`level`,`condition_type`,`category`) VALUES (@sub_menu_id2, '服务管理', @menu_id, null, @data_type2, 0, 2, CONCAT('/40000/',@menu_id,'/',@sub_menu_id2), 'park', 602, @menu_id, 3, '', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`level`,`condition_type`,`category`) VALUES (@sub_menu_id3, '消息推送', @menu_id, null, @data_type3, 0, 2, CONCAT('/40000/',@menu_id,'/',@sub_menu_id3), 'park', 603, @menu_id, 3, '', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`level`,`condition_type`,`category`) VALUES (@sub_menu_id4, '表单管理', @menu_id, null, @data_type4, 0, 2, CONCAT('/40000/',@menu_id,'/',@sub_menu_id4), 'park', 604, @menu_id, 3, '', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`level`,`condition_type`,`category`) VALUES (@sub_menu_id5, '审批列表', @menu_id, null, @data_type5, 0, 2, CONCAT('/40000/',@menu_id,'/',@sub_menu_id5), 'park', 605, @menu_id, 3, '', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`level`,`condition_type`,`category`) VALUES (@sub_menu_id6, '申请记录', @menu_id, null, @data_type6, 0, 2, CONCAT('/40000/',@menu_id,'/',@sub_menu_id6), 'park', 606, @menu_id, 3, '', 'module');

-- 设置菜单权限
set @eh_acl_privileges_id = (select max(id) from eh_acl_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)VALUES ((@eh_acl_privileges_id := @eh_acl_privileges_id+1), 0, @menu_name, CONCAT(@menu_name,' 全部权限'), NULL);

-- 角色对应的菜单权限
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)VALUES ((@acl_id := @acl_id + 1), @namespace_id, 'EhOrganizations', NULL, 1, @eh_acl_privileges_id, 1001, 'EhAclRoles', 0, 1, NOW());

-- 菜单对应的权限
SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, @menu_id, @menu_name, 1, 1, CONCAT(@menu_name,' 全部权限'), 1);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, @sub_menu_id1, @menu_name, 1, 1, CONCAT(@menu_name,' 全部权限'), 2);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, @sub_menu_id2, @menu_name, 1, 1, CONCAT(@menu_name,' 全部权限'), 3);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, @sub_menu_id3, @menu_name, 1, 1, CONCAT(@menu_name,'全部权限'), 4);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, @sub_menu_id4, @menu_name, 1, 1, CONCAT(@menu_name,' 全部权限'), 5);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, @sub_menu_id5, @menu_name, 1, 1, CONCAT(@menu_name,' 全部权限'), 6);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, @sub_menu_id6, @menu_name, 1, 1, CONCAT(@menu_name,' 全部权限'), 7);

-- 菜单范围添加
SET @scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@scope_id:=@scope_id+1), @menu_id,'', 'EhNamespaces', @namespace_id , 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@scope_id:=@scope_id+1), @sub_menu_id1,'', 'EhNamespaces', @namespace_id , 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@scope_id:=@scope_id+1), @sub_menu_id2,'', 'EhNamespaces', @namespace_id , 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@scope_id:=@scope_id+1), @sub_menu_id3,'', 'EhNamespaces', @namespace_id , 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@scope_id:=@scope_id+1), @sub_menu_id4,'', 'EhNamespaces', @namespace_id , 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@scope_id:=@scope_id+1), @sub_menu_id5,'', 'EhNamespaces', @namespace_id , 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@scope_id:=@scope_id+1), @sub_menu_id6,'', 'EhNamespaces', @namespace_id , 2);
-- end 设置菜单权限