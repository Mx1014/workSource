-- by dengs, 20170925 服务联盟2.9
-- -------------------------------------------------------------------------------------------------------------------------------------------------
-- ------------- entry_id 写入 --------
DROP PROCEDURE if exists create_service_alliance_entry_id;
delimiter $$;
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
END $$;

delimiter ;
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
delimiter ;
CALL create_service_alliance_menu;
drop procedure  if exists create_service_alliance_menu;

-- ------------- 新建菜单、权限、等 END--------
-- ------------- 新建菜单Scope --------
DROP PROCEDURE if exists create_service_alliance_menu_scope;
delimiter //
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
//
delimiter ;
CALL create_service_alliance_menu_scope;
DROP PROCEDURE if exists create_service_alliance_menu_scope;
-- ------------- 新建菜单Scope END--------
-- ------------- 删除服务广场没有使用的应用入口的菜单Scope --------
DROP PROCEDURE if exists delete_service_alliance_menu_scope;
delimiter //
CREATE PROCEDURE `delete_service_alliance_menu_scope` ()
BEGIN
  DECLARE aid LONG;
	DECLARE ans INTEGER;

  DECLARE done INT DEFAULT FALSE;
  DECLARE cur CURSOR FOR select namespace_id,entry_id*100+41600 from eh_service_alliance_categories WHERE  parent_id =0 AND id NOT IN (select a.categry_id from (SELECT DISTINCT CAST(SUBSTRING(action_data,LOCATE('type":',action_data)+6,LOCATE(',',action_data)-6-LOCATE('type":',action_data)) AS UNSIGNED) AS categry_id from eh_launch_pad_items WHERE action_type = 33) as a WHERE a.categry_id IS NOT NULL);
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  OPEN cur;
  read_loop: LOOP
    FETCH cur INTO ans,aid;
    IF done THEN
      LEAVE read_loop;
    END IF;
		DELETE FROM eh_web_menu_scopes WHERE owner_id = ans AND menu_id = aid;
	  DELETE FROM eh_web_menu_scopes WHERE owner_id = ans AND menu_id = aid+10;
	  DELETE FROM eh_web_menu_scopes WHERE owner_id = ans AND menu_id = aid+20;
	  DELETE FROM eh_web_menu_scopes WHERE owner_id = ans AND menu_id = aid+30;
	  DELETE FROM eh_web_menu_scopes WHERE owner_id = ans AND menu_id = aid+40;
	  DELETE FROM eh_web_menu_scopes WHERE owner_id = ans AND menu_id = aid+50;
		DELETE FROM eh_web_menu_scopes WHERE owner_id = ans AND menu_id = aid+60;
  END LOOP;
  CLOSE cur;
END
//
delimiter ;
CALL delete_service_alliance_menu_scope;
DROP PROCEDURE if exists delete_service_alliance_menu_scope;
-- ------------- 删除服务广场没有使用的应用入口的菜单Scope end--------
-- 删除服务联盟老菜单
DELETE from eh_web_menus WHERE id = 40500;
-- 菜单位置调整
DELETE FROM eh_web_menus WHERE id in (40510,40520,40530,40541,40542);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('40500', '服务联盟', '40000', NULL, NULL, '1', '2', '/40000/40500', 'park', '450', '40500', '2', NULL, 'module');
UPDATE eh_web_menus SET path = CONCAT('/40000/40500/',parent_id,'/',id),`level`=4 WHERE id >=41700 AND id <=44660 AND `level` =3;
UPDATE eh_web_menus SET parent_id = 40500,path = CONCAT('/40000/40500/',id),`level`=3 WHERE id >=41700 AND id <=44660 AND `level` =2;
-- end by dengs, 20170925 服务联盟2.9


--
-- 联信通  短信配置 add by xq.tian  2017/08/30
--
SET @configurations_id = IFNULL((SELECT MAX(id) FROM `eh_configurations`), 1);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@configurations_id := @configurations_id + 1), 'sms.LianXinTong.server', '', '联信通server', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@configurations_id := @configurations_id + 1), 'sms.LianXinTong.spId', '', '联信通账户', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@configurations_id := @configurations_id + 1), 'sms.LianXinTong.authCode', '', '联信通密码', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@configurations_id := @configurations_id + 1), 'sms.LianXinTong.srcId', '', '联信通srcId', 0, NULL);

--
-- 优讯通
--
SET @configurations_id = IFNULL((SELECT MAX(id) FROM `eh_configurations`), 1);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@configurations_id := @configurations_id + 1), 'sms.YouXunTong.server', 'http://new.yxuntong.com/emmpdata/sms/Submit', '优讯通server', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@configurations_id := @configurations_id + 1), 'sms.YouXunTong.accountName', '2002467', '优讯通账户', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@configurations_id := @configurations_id + 1), 'sms.YouXunTong.password', '(Un"|71#', '优讯通密码', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@configurations_id := @configurations_id + 1), 'sms.YouXunTong.token', '', '优讯通token', 0, NULL);

--
-- 短信签名
--
SET @max_template_id = IFNULL((SELECT MAX(id) FROM `eh_locale_templates`), 1);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '﻿左邻短信签名', '【﻿左邻】', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '科技园短信签名', '【科技园】', 1000000);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '讯美短信签名', '【讯美】', 999999);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '金隅嘉业短信签名', '【金隅嘉业】', 999995);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '海岸物业短信签名', '【海岸物业】', 999993);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '深业物业短信签名', '【深业物业】', 999992);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '威新LINK短信签名', '【威新LINK】', 999991);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '储能短信签名', '【储能】', 999990);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '金地Ibase短信签名', '【金地Ibase】', 999989);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '爱特家短信签名', '【爱特家】', 999988);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '深圳湾短信签名', '【深圳湾】', 999987);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '创源短信签名', '【创源】', 999986);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '华润置地OE短信签名', '【华润置地OE】', 999985);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '清华信息港短信签名', '【清华信息港】', 999984);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', 'T空间短信签名', '【T空间】', 999982);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '正中会短信签名', '【正中会】', 999983);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '星商汇园区短信签名', '【星商汇园区】', 999981);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '全至100短信签名', '【全至100】', 999980);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '光大we谷短信签名', '【光大we谷】', 999979);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '康利K生活短信签名', '【康利K生活】', 999978);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', 'Volgo短信签名', '【Volgo】', 1);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '创梦云短信签名', '【创梦云】', 999977);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '名网邦短信签名', '【名网邦】', 999976);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '荣超股份短信签名', '【荣超股份】', 999975);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '嘉定新城智慧管家短信签名', '【嘉定新城智慧管家】', 999974);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '保集e智谷短信签名', '【保集e智谷】', 999973);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '军民融合APP短信签名', '【军民融合APP】', 999972);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '张江高科短信签名', '【张江高科】', 999971);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '互联网产业园短信签名', '【互联网产业园】', 999970);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '昌发展短信签名', '【昌发展】', 999969);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '国贸圈短信签名', '【国贸圈】', 999968);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.sign', 0, 'zh_CN', '大沙河建投短信签名', '【大沙河建投】', 999967);

--
-- 短信模板
--
SET @max_template_id = IFNULL((SELECT MAX(id) FROM `eh_locale_templates`), 1);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 1, 'zh_CN', '验证码', '您的验证码为${vcode}，10分钟内有效，感谢您的使用。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 4, 'zh_CN', '派单', '业主${phone}发布了新的${topicType}帖，您已被分配处理该业主的需求，请尽快联系该业主。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 6, 'zh_CN', '任务2', '${operatorUName}给你分配了一个任务，请直接联系用户${createUName}（电话${createUToken}），帮他处理该问题。', 0);
--
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 3, 'zh_CN', '物业缴费通知', '您${year}年${month}月物业账单为，本月金额:${dueAmount},往期欠款:${oweAmount}，本月实付金额:${payAmount}，应付金额:${balance}， 请尽快使用左邻缴纳物业费。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 7, 'zh_CN', '新报修', '${createUName}(手机号：${createUToken})已发布了新的报修任务，主题为：${subject}，请立即处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 8, 'zh_CN', '门禁', '${username}已授权给你${doorname}门禁二维码，请点击以下链接使用：${link}/aclink/v?id=${id} （24小时有效）。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 8, 'zh_CN', '门禁', '${username}已授权给你${doorname}门禁二维码，请点击以下链接使用：http://core.zuolin.com/aclink/v?id=${id} （24小时有效）。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 9, 'zh_CN', '看楼申请', '用户${userName}（手机号：${userPhone}）于${applyTime}提交了预约${applyType}申请：参观位置：${location}\n面积需求：${area}\n公司名称：${enterpriseName}\n备注：${description}', 1000000);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 10, 'zh_CN', '物业2', '${operatorName} ${operatorPhone}已将一个${categoryName}单派发给你，请尽快处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 11, 'zh_CN', '物业', '${operatorName} ${operatorPhone}已发起一个${categoryName}单，请尽快处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 12, 'zh_CN', '预定1', '您已成功预约了${resourceName}，使用时间：${useDetail}。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 13, 'zh_CN', '预定2', '您已成功预约了${resourceName}，使用时间：${useDetail}，预约数量：${count}。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 14, 'zh_CN', '预定3', '您已成功预约了${resourceName}，使用详情：${useDetail}。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 15, 'zh_CN', '物业任务3', '您于${day}日${hour}时发起的服务已派发至处理人 ${operatorName}（电话： ${operatorPhone}），请耐心等待。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 17, 'zh_CN', '合两有', '尊敬的客户您好，我是您的专属客服经理${serviceUserName}，电话${serviceUserPhone}，您的租期将在${contractEndDate}到期，请到“${appName1}app”办理续签等相关手续。 如未安装“${appName2}app”，请到应用市场下载安装。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 18, 'zh_CN', '合两无', '尊敬的客户您好，您的租期将在${contractEndDate}到期，请到“${appName1}app”办理续签等相关手续。 如未安装“${appName2}app”，请到应用市场下载安装。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 19, 'zh_CN', '合一有', '尊敬的客户您好，我是您的专属客服经理${serviceUserName}，电话${serviceUserPhone}，您的租期将在${contractEndDate}到期，请到“${appName1}app”办理续签等相关手续。 如未安装“${appName2}app”，请到应用市场下载安装，如果您已经与科技园联系过，请忽略该短信。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 20, 'zh_CN', '合一无', '尊敬的客户您好，您的租期将在${contractEndDate}到期，请到“${appName1}app”办理续签等相关手续。 如未安装“${appName2}app”，请到应用市场下载安装，如果您已经与科技园联系过，请忽略该短信。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 21, 'zh_CN', '发新有', '尊敬的客户您好，我是您的专属客服经理${serviceUserName}，电话${serviceUserPhone}，欢迎入住${communityName}，有任何问题请随时与我联系。为更好地为您做好服务，请下载安装“${appName}app”，体会指尖上的园区给您带来的便利和高效，请到应用市场下载安装。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 22, 'zh_CN', '发新无', '尊敬的客户您好，欢迎入住${communityName}。为更好地为您做好服务，请下载安装“${appName}app”，体会指尖上的园区给您带来的便利和高效，请到应用市场下载安装。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 23, 'zh_CN', '排队', '您有一个关于${parkingLotName}停车场的月卡申请任务，请尽快处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 24, 'zh_CN', '排队驳回', '您的月卡申请（停车场：${parkingLotName}，车牌：${plateNumber}）审核未通过，请前往查看详情。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 25, 'zh_CN', '	待办理', '您的月卡申请（停车场：${parkingLotName}，车牌：${plateNumber}）已审核通过，请前往查看详情（逾期未办理将取消资格）。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 26, 'zh_CN', '待办办理', '恭喜，您的停车月卡（停车场：${parkingLotName}，车牌：${plateNumber}）已成功办理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 27, 'zh_CN', '待办取消', '您的月卡申请（停车场：${parkingLotName}，车牌：${plateNumber}）审核未通过，请前往查看详情。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 28, 'zh_CN', '资申成', '您申请预约的${useTime}的${resourceName}已通过审批，为确保您成功预约，请尽快完成支付，支付方式支持：1. 请联系${offlinePayeeName}（${offlinePayeeContact}）上门收费，2. 到${offlineCashierAddress}付款；感谢您的使用。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 29, 'zh_CN', '资申败', '您申请预约的${useTime}的${resourceName}没有通过审批，您可以申请预约其他空闲资源，由此给您造成的不便，敬请谅解，感谢您的使用。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 30, 'zh_CN', '资付成', '您已完成支付，成功预约${useTime}的${resourceName}，请按照预约的时段使用资源，感谢您的使用。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 31, 'zh_CN', '资申成', '您申请预约的${useTime}的${resourceName}已通过审批，为确保您成功预约，请尽快到APP完成在线支付，感谢您的使用。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 32, 'zh_CN', '资超时', '您申请预约的${useTime}的${resourceName}由于超时未支付或被其他客户抢先预约，已自动取消，由此给您造成的不便，敬请谅解，感谢您的使用。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 33, 'zh_CN', '资催办', '客户（${userName}${userPhone}）提交资源预约的线下支付申请，预约${resourceName}，使用时间：${useTime}，订单金额${price}，请尽快联系客户完成支付。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 34, 'zh_CN', '物业任务4', '（${creatorName}，电话：${creatorPhone}）已发起物业报修，由（处理人：${operatorName}、电话：${operatorPhone}）负责处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 35, 'zh_CN', '入驻待处', '您有客户（姓名：${applyUserName}，电话：${applyContact}）园区入驻流程待处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 36, 'zh_CN', '驻待处受', '您的园区入驻需求已受理，我们将安排客户经理（${operatorName}） 为您服务，您也可以与他联系，电话（${operatorContact}）。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 37, 'zh_CN', '驻处理驳', '您的园区入驻任务未通过，具体原因见APP中“我的申请”。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 38, 'zh_CN', '待办办理', '（姓名：${applyUserName}、电话${applyContact}）督办您处理他的园区入驻申请 。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 39, 'zh_CN', '驻成功', '您的园区入驻需求已办理完毕，请给我们的服务做个评价。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 40, 'zh_CN', '资待处理', '（姓名：${userName}、电话${userPhone}）督办您处理他的园区入驻申请 。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 41, 'zh_CN', '资待督办', '请督办（处理人:${operatorName}，处理人电话:${operatorContact}）处理（姓名：${userName}，电话：${userPhone}）的${resourceName}预约申请任务。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 42, 'zh_CN', '资待受理', '您的${resourceName}预约需求已受理，我们将安排客户经理（${operatorName}） 为您服务，您也可以与他联系，电话（${operatorContact}）。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 43, 'zh_CN', '资待驳回', '您的${resourceName}预约任务未通过，具体原因见APP内“我的申请”。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 44, 'zh_CN', '资待催办', '（姓名：${userName}，电话：${userPhone}）催办您处理他的${resourceName}预约申请。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 45, 'zh_CN', '资办成功', '您的${resourceName}预约需求已办理完毕，请给我们的服务做个评价。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 46, 'zh_CN', '报修受督', '请督办（处理人:${operatorName}，处理人电话:${operatorContact}）处理（姓名：${userName}，电话：${userPhone}）的${resourceName}预约申请任务。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 47, 'zh_CN', '报修待分', '您的${resourceName}预约需求已受理，我们将安排客户经理（${operatorName}） 为您服务，您也可以与他联系，电话（${operatorContact}）。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 48, 'zh_CN', '报待分督', '客户（${operatorName}，电话：${operatorPhone}）的一个新的${categoryName}任务超时未分配人员，请督办尽快受理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 49, 'zh_CN', '报修完成', '您的${categoryName}任务已处理完毕，可在APP内“我-我的申请”中查看完成情况说明，请给我们的服务做个评价。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 50, 'zh_CN', '正中会', '客户${userName}（联系方式：${userPhone}）完成支付，成功预约${useTime}的${resourceName}，请提前做好相关准备工作。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 51, 'zh_CN', '视频会', '视频会议账号${accountName}将在一周后（${date}）到期，请及时处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 52, 'zh_CN', '视测会', '视频会议试用账号${accountName}将在3天后（${date}）到期,请及时处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 53, 'zh_CN', '申诉', '您的申诉已通过，账号手机已更新为${newIdentifier}，若非本人操作请联系客服，感谢您的使用。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default', 54, 'zh_CN', '物业催缴', '${targetName}先生/女士，您好，您的物业账单已出，账期${dateStr}，使用"${appName} APP"可及时查看账单并支持在线付款。', 0);

-- 停车 add by sw 20170925
INSERT INTO `eh_parking_invoice_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`, `invoice_token`)
  VALUES ('1', '999983', 'community', '240111044331055940', '10006', '公司', '2', '1', '2017-09-19 10:46:18', NULL, NULL, '0');
INSERT INTO `eh_parking_invoice_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`, `invoice_token`)
  VALUES ('2', '999983', 'community', '240111044331055940', '10006', '个人', '2', '1', '2017-09-19 10:46:18', NULL, NULL, '1');
INSERT INTO `eh_parking_invoice_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`, `invoice_token`)
  VALUES ('3', '999983', 'community', '240111044331055940', '10006', '车牌号码', '2', '1', '2017-09-19 10:46:18', NULL, NULL, '2');
update eh_configurations set `value` = 'http://220.160.111.114:9090' where `name` = 'parking.kexing.url';
update eh_configurations set `value` = 'F7A0B971B199FD2A1017CEC5' where `name` = 'parking.kexing.key';
update eh_configurations set `value` = 'ktapi' where `name` = 'parking.kexing.user';
update eh_configurations set `value` = '0306A9' where `name` = 'parking.kexing.pwd';
INSERT INTO `eh_parking_card_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `card_type_id`, `card_type_name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
  VALUES ('1', '999983', 'community', '240111044331055940', '10006', '2', '月租车', '2', '1', '2017-09-19 10:49:48', NULL, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('parking.default.card.type', '[{\"typeId\":\"普通月卡\", \"typeName\":\"普通月卡\"}]', NULL, '0', NULL);

delete from eh_parking_lots where id = 10006;
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`)
	VALUES ('10006', 'community', '240111044331055940', '科兴科学园停车场', 'KETUO2', NULL, '2', '1025', '2016-12-16 17:07:20', '0', '{\"expiredRechargeFlag\":1,\"expiredRechargeMonthCount\":2,\"expiredRechargeType\":2,\"maxExpiredDay\":365,\"monthlyDiscountFlag\":0,\"tempFeeDiscountFlag\":0}', '{\"tempfeeFlag\": 0, \"rateFlag\": 0, \"lockCarFlag\": 0, \"searchCarFlag\": 1, \"currentInfoType\": 2, \"contact\": \"18718523489\",\"invoiceFlag\":1}');


-- 短信供应商配置  add by xq.tian  2017/08/30
UPDATE `eh_configurations` SET `value` = 'YZX,YouXunTong' WHERE `name` = 'sms.handler.type' AND `namespace_id` = 0;

-- 企业统一信用代码校验 add by xiongying 20170926
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('organization', '900026', 'zh_CN', '统一社会信用代码已存在');



-- merge from incubator-1.0 成都孵化器 start  by yanjun

-- 微信绑定手机页面url   add by yanjun 20170901
SET @eh_configurations_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES((@eh_configurations_id := @eh_configurations_id + 1),'wx.bind.phone.url','/service-hub/build/#/register','微信用户绑定手机页面_默认','0',NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES((@eh_configurations_id := @eh_configurations_id + 1),'wx.bind.phone.url','/service-hub/build/#/register','微信用户绑定手机页面_成都孵化器','999964',NULL);

-- merge from incubator-1.0 成都孵化器 end by yanjun

-- 科兴的论坛 2.1 add by xq.tian 2017/09/26
SET @max_id = (SELECT max(id) FROM `eh_launch_pad_layouts`);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`)
VALUES ((@max_id := @max_id + 1), 999983, 'ForumLayout', '{"versionCode":"2017071201","versionName":"4.7.3","layoutName":"ForumLayout","displayName":"论坛","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":1,"separatorFlag":0,"separatorHeight":0}]}', 2017071201, 2017071201, 2, NOW(), 'park_tourist', 0, 0, 0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`)
VALUES ((@max_id := @max_id + 1), 999983, 'ForumLayout', '{"versionCode":"2017071201","versionName":"4.7.3","layoutName":"ForumLayout","displayName":"论坛","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":1,"separatorFlag":0,"separatorHeight":0}]}', 2017071201, 2017071201, 2, NOW(), 'pm_admin', 0, 0, 0);

SET @item_id = (SELECT max(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@item_id := @item_id + 1), 999983, 0, 0, 0, '/forum', 'TabGroup', 'DISCOVER', '图文', 'cs://1/image/aW1hZ2UvTVRveU1ESTRabVJrTVRRek56ZGhOV1kyT0RCaU1tRTJZekEwTVdNMU9URmxPQQ', 1, 1, 62, '', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@item_id := @item_id + 1), 999983, 0, 0, 0, '/forum', 'TabGroup', 'DISCOVER', '图文', 'cs://1/image/aW1hZ2UvTVRveU1ESTRabVJrTVRRek56ZGhOV1kyT0RCaU1tRTJZekEwTVdNMU9URmxPQQ', 1, 1, 62, '', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0, NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@item_id := @item_id + 1), 999983, 0, 0, 0, '/forum', 'TabGroup', 'TRD_VIDEO', '视频', 'cs://1/image/aW1hZ2UvTVRveU1ESTRabVJrTVRRek56ZGhOV1kyT0RCaU1tRTJZekEwTVdNMU9URmxPQQ', 1, 1, 14, '{"url":"https://weishang.movmovie.com/video/zz-zone.html","declareFlag": "1"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@item_id := @item_id + 1), 999983, 0, 0, 0, '/forum', 'TabGroup', 'TRD_VIDEO', '视频', 'cs://1/image/aW1hZ2UvTVRveU1ESTRabVJrTVRRek56ZGhOV1kyT0RCaU1tRTJZekEwTVdNMU9URmxPQQ', 1, 1, 14, '{"url":"https://weishang.movmovie.com/video/zz-zone.html","declareFlag": "1"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0, NULL);

SET @apps_id = IFNULL((SELECT MAX(id) FROM `eh_apps`), 1);
INSERT INTO `eh_apps` (`creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`, `update_uid`, `update_time`)
VALUES ((@apps_id := @apps_id + 1), '466dd353-bdd8-4764-9315-56e6e7151f06', 'KemccuJycjKbnYnBUIzQLU0LNB5S8aDpeMNIT0mxKVFGtz8pKj83spLELs1y49mAY89hzGqVdODsgiyTaKveig==', 'kexing.qipai', 'kexing qipai app', 1, NOW(), NULL, NULL);


-- 重新刷新organization的setAdminFlag字段的值 add by sfyan 20170926
update `eh_organizations` eo set `set_admin_flag` = (select if(count(*) > 0, 1, 0) from `eh_organization_members` where organization_id = eo.id and `member_group` = 'manager' and status = 3) where `group_type` = 'ENTERPRISE' and status = 2;


SET @id =(SELECT MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'user','400001','zh_CN','手机号码错误');


-- 检验用户的临时token。报错信息-无效的用户token    add by yanjun 20170927
SET @id =(SELECT MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'user','400002','zh_CN','无效的userToken');


-- 修改园区快讯模块默认参数 add by sfyan 20170927
update `eh_service_modules` set instance_config = '{"timeWidgetStyle":"date","entityCount":0,"subjectHeight":0,"descriptionHeight":0}' where id = 10800;


