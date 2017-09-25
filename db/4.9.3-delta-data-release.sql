-- by dengs, 20170925 服务联盟2.9
-- -------------------------------------------------------------------------------------------------------------------------------------------------
-- ------------- entry_id 写入 --------
DROP PROCEDURE if exists create_service_alliance_entry_id;
CREATE PROCEDURE `create_service_alliance_entry_id` ()
BEGIN
  DECLARE ns INTEGER;
  DECLARE aid LONG;
	DECLARE curns INTEGER DEFAULT -1;
	DECLARE nsorder INTEGER DEFAULT 1;
  DECLARE done INT DEFAULT FALSE;
  DECLARE cur CURSOR FOR select id,namespace_id from eh_service_alliance_categories WHERE parent_id = 0 ORDER BY namespace_id desc; 
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  OPEN cur;
  read_loop: LOOP
    FETCH cur INTO aid,ns;
    IF done THEN
      LEAVE read_loop;
    END IF;
		if curns<>ns THEN
				SET nsorder = 1;
				SET curns = ns;
		END IF;
		UPDATE eh_service_alliance_categories SET entry_id = nsorder WHERE id = aid;
		SET nsorder = nsorder + 1;
  END LOOP;
  CLOSE cur;
END;

CALL create_service_alliance_entry_id;
DROP PROCEDURE if exists create_service_alliance_entry_id;
-- ------------- entry_id 写入 end --------
-- ------------- 新建菜单、权限、等--------
delimiter $$;
drop procedure  if exists create_service_alliance_menu;
create procedure create_service_alliance_menu()
begin 
	declare anchor int;
	declare i int;
-- 	SET @iscreated = (SELECT ID from eh_web_menus WHERE id>=41700 AND id<=44600 LIMIT 1);
	SET @iscreated = null;
	if ISNULL(@iscreated) THEN
		set anchor=41700; -- 父菜单 41700 41800->44600 子菜单 4xx10->4xx60
		SET i = 1;
		while i <= 30 do
			set @menu_id = anchor;
			set @sub_menu_id1 = anchor+10;
			set @sub_menu_id2 = anchor+20;
			set @sub_menu_id3 = anchor+30;
			set @sub_menu_id4 = anchor+40;
			set @sub_menu_id5 = anchor+50;
			set @sub_menu_id6 = anchor+60;
			set @menu_name = CONCAT('服务联盟',i);
			set @entry_id = i;
			set @data_type = CONCAT('service_list/',@entry_id);-- 应用入口
			set @data_type1 = CONCAT('service_type_management/',@entry_id);-- 类型管理
			set @data_type2 = CONCAT('service_alliance/',@entry_id);-- 服务管理
			set @data_type3 = CONCAT('message_push_setting/',@entry_id);-- 消息推送
			-- set @data_type4 = CONCAT('',@entry_id); -- 表单管理
			set @data_type4 = CONCAT('react:/form-management/form-list/?moduleId=',@entry_id,'&moduleType=service_alliance'); -- 表单管理
			-- set @data_type5 = CONCAT('list/',@menu_id,'/EhOrganizations'); -- 审批列表 
			set @data_type5 = CONCAT('react:/approval-management/approval-list/',@entry_id,'/EhOrganizations?moduleType=service_alliance'); -- 审批列表 
			set @data_type6 = CONCAT('apply_record/',@entry_id); -- 申请记录
			set @module_id = 40500;
			set @sort = i * 10+300;
			-- 设置菜单
			INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`level`,`condition_type`,`category`) VALUES (@menu_id,      @menu_name, 40000, 	 null, @data_type,  1, 2, CONCAT('/40000/',@menu_id),                   'park', @sort+1, @module_id, 2, '', 'module');
			INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`level`,`condition_type`,`category`) VALUES (@sub_menu_id1, '类型管理', @menu_id, null, @data_type1, 0, 2, CONCAT('/40000/',@menu_id,'/',@sub_menu_id1), 'park', @sort+2, @module_id, 3, '', 'module');
			INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`level`,`condition_type`,`category`) VALUES (@sub_menu_id2, '服务管理', @menu_id, null, @data_type2, 0, 2, CONCAT('/40000/',@menu_id,'/',@sub_menu_id2), 'park', @sort+3, @module_id, 3, '', 'module');
			INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`level`,`condition_type`,`category`) VALUES (@sub_menu_id3, '消息推送', @menu_id, null, @data_type3, 0, 2, CONCAT('/40000/',@menu_id,'/',@sub_menu_id3), 'park', @sort+4, @module_id, 3, '', 'module');
			INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`level`,`condition_type`,`category`) VALUES (@sub_menu_id4, '表单管理', @menu_id, null, @data_type4, 0, 2, CONCAT('/40000/',@menu_id,'/',@sub_menu_id4), 'park', @sort+5, @module_id, 3, '', 'module');
			INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`level`,`condition_type`,`category`) VALUES (@sub_menu_id5, '审批列表', @menu_id, null, @data_type5, 0, 2, CONCAT('/40000/',@menu_id,'/',@sub_menu_id5), 'park', @sort+6, @module_id, 3, '', 'module');
			INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`level`,`condition_type`,`category`) VALUES (@sub_menu_id6, '申请记录', @menu_id, null, @data_type6, 0, 2, CONCAT('/40000/',@menu_id,'/',@sub_menu_id6), 'park', @sort+7, @module_id, 3, '', 'module');
			-- 设置菜单权限
			set @eh_acl_privileges_id = (select max(id) from eh_acl_privileges);
			INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)VALUES ((@eh_acl_privileges_id := @eh_acl_privileges_id+1), 0, @menu_name, CONCAT(@menu_name,' 全部权限'), @menu_id);
			-- 菜单对应的权限
			SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
			INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, @menu_id, @menu_name, 1, 1, CONCAT(@menu_name,' 全部权限'), 1);
			INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, @sub_menu_id1, @menu_name, 1, 1, CONCAT(@menu_name,' 全部权限'), 2);
			INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, @sub_menu_id2, @menu_name, 1, 1, CONCAT(@menu_name,' 全部权限'), 3);
			INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, @sub_menu_id3, @menu_name, 1, 1, CONCAT(@menu_name,' 全部权限'), 4);
			INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, @sub_menu_id4, @menu_name, 1, 1, CONCAT(@menu_name,' 全部权限'), 5);
			INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, @sub_menu_id5, @menu_name, 1, 1, CONCAT(@menu_name,' 全部权限'), 6);
			INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, @sub_menu_id6, @menu_name, 1, 1, CONCAT(@menu_name,' 全部权限'), 7);
		
			set i = i + 1;
			set anchor = anchor + 100;
		end while;
	ELSE
		SELECT CONCAT('exist id = ',ISNULL(@iscreated));
	END IF;
end $$;

CALL create_service_alliance_menu;
drop procedure  if exists create_service_alliance_menu;

-- ------------- 新建菜单、权限、等 END--------
-- ------------- 新建菜单Scope --------
DROP PROCEDURE if exists create_service_alliance_menu_scope;
CREATE PROCEDURE `create_service_alliance_menu_scope` ()
BEGIN
  DECLARE aid LONG;
	DECLARE ans INTEGER;
	DECLARE aeid INTEGER;
	DECLARE aname VARCHAR(200);
  DECLARE done INT DEFAULT FALSE;
  DECLARE cur CURSOR FOR select id,`name`,namespace_id,entry_id from eh_service_alliance_categories WHERE parent_id = 0 ORDER BY namespace_id desc; 
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  OPEN cur;
  read_loop: LOOP
    FETCH cur INTO aid,aname,ans,aeid;
    IF done THEN
      LEAVE read_loop;
    END IF;
		set @menu_id = (select id FROM eh_web_menus WHERE `name` = CONCAT('服务联盟', aeid) AND parent_id = 40000);
		set @eh_acl_privileges_id = (SELECT id from eh_acl_privileges WHERE `name` = CONCAT('服务联盟', aeid) AND tag = @menu_id);
		SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
		INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)VALUES ((@acl_id := @acl_id + 1), ans, 'EhOrganizations', NULL, 1, @eh_acl_privileges_id, 1001, 'EhAclRoles', 0, 1, NOW());
		-- 菜单范围添加
		SET @scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
		INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@scope_id:=@scope_id+1), @menu_id,aname, 'EhNamespaces', ans , 1);
		INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@scope_id:=@scope_id+1), @menu_id+10,'', 'EhNamespaces', ans , 2);
		INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@scope_id:=@scope_id+1), @menu_id+20,'', 'EhNamespaces', ans , 2);
		INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@scope_id:=@scope_id+1), @menu_id+30,'', 'EhNamespaces', ans , 2);
		INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@scope_id:=@scope_id+1), @menu_id+40,'', 'EhNamespaces', ans , 2);
		INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@scope_id:=@scope_id+1), @menu_id+50,'', 'EhNamespaces', ans , 2);
		INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@scope_id:=@scope_id+1), @menu_id+60,'', 'EhNamespaces', ans , 2);
		-- end 设置菜单权限

  END LOOP;
  CLOSE cur;
END
CALL create_service_alliance_menu_scope;
DROP PROCEDURE if exists create_service_alliance_menu_scope;
-- ------------- 新建菜单Scope END--------
-- 删除服务联盟老菜单
DELETE from eh_web_menus WHERE id = 40500;
-- end by dengs, 20170925 服务联盟2.9