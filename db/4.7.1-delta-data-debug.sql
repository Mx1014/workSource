-- 服务广场 -- 添加到左邻域 -- 在alpha，beta执行
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('beta.print.order.amount', 'true', '用于支付测试', '0', NULL);
SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` 
(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`,
 `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, 
`min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, 
`scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 0, 0, 0, 0, '/home', 'Default', '云打印', '云打印', 
'', 1, 1, 14, '{"url":"http://printtest.zuolin.com/cloud-print/build/index.html?hideNavigationBar=1#/home#sign_suffix"}', 3, 0,
 1, 1, 1, 0, NULL, NULL, NULL, 0,
 'default', 1, NULL, NULL, 0, NULL);
 
 INSERT INTO `eh_launch_pad_items` 
(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`,
 `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, 
`min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, 
`scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 0, 0, 0, 0, '/home', 'Default', '云打印', '云打印', 
'', 1, 1, 14, '{"url":"http://printtest.zuolin.com/cloud-print/build/index.html?hideNavigationBar=1#/home#sign_suffix"}', 3, 0,
 1, 1, 1, 0, NULL, NULL, NULL, 0,
 'park_tourist', 1, NULL, NULL, 0, NULL);
 
 INSERT INTO `eh_launch_pad_items` 
(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`,
 `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, 
`min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, 
`scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 0, 0, 0, 0, '/home', 'Default', '云打印', '云打印', 
'', 1, 1, 14, '{"url":"http://printtest.zuolin.com/cloud-print/build/index.html?hideNavigationBar=1#/home#sign_suffix"}', 3, 0,
 1, 1, 1, 0, NULL, NULL, NULL, 0,
 'pm_admin', 1, NULL, NULL, 0, NULL);
 -- 服务广场 -- 添加到左邻域 -- 在alpha，beta执行 -- end
 
-- by dengs,司印服务器ip地址,二维码时间，默认打印价格，生成打印二维码的url 20170615
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('print.siyin.server.url', 'http://siyin.zuolin.com:8119', '司印服务器ip地址', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('print.siyin.timeout', '10', '二维码的identifierToken在redis存在的时间', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('print.siyin.timeout.unit', 'MINUTES', '秒 SECONDS/分 MINUTES/小时 HOURS', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('print.default.price', '0.1', '打印默认价格', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('print.logon.scan.timout', '10000', '二维码是否被扫描检测的延迟时间,单位毫秒', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('print.siyin.pattern', '1', '1:司印方配置成不解锁打印机，直接打印的模式，2:司印方配置成发送文档到打印机，需要解锁再打印的模式。', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('print.siyin.job.count.timeout', '10', '用户正在打印任务数量放到redis中的，设置一个默认超时时间 10 单位分钟', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('print.inform.url', 'http://core.zuolin.com/evh/siyinprint/informPrint', '二维码url地址', '0', NULL);
-- by dengs,默认打印教程、默认复印/扫描教程 20170615
INSERT INTO `ehcore`.`eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', 'print_course_list', 'zh_CN', '在电脑上打开需要打印的文档，点击打印，\n 设置好打印参数，点击确定，电脑上出现二维码 | 打开APP，点击右上角扫一扫按钮，或者进入云打印界面，\n 点击右上角扫一扫按钮，扫描电脑出现的二维码 | APP上出现提示，询问是否立即打印，点击立即打印，\n 打印任务将发送给打印机，等待打印完成即可去打印机上\n 取回打印资料（为了确保打印的资料不被他人取走，您可以\n 走到打印机前再点击“立即打印”） | 打印完成后，APP端生成一个待付款的订单，您可以立即\n 支付，也可以等待下次打印时再进行支付（注意每次打印\n 必须保证上次的订单已付款，否则无法进行打印）\n ');
INSERT INTO `ehcore`.`eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', 'scan_copy_course_list', 'zh_CN', '准备好需要复印/扫描的资料，前往打印机处 |  打开APP，进入云打印界面，点击右上角扫一扫按钮，\n扫描打印机上的二维码，填写扫描件接受邮箱 |按照打印机上的指引进行操作，点击扫描/复印，进行\n扫描/复印参数设置，设置好参数进行对应的操作即可 |扫描/复印完成后，点击打印机屏幕右上角的登出按钮\n结束任务（或者直接离开，系统自动超时结束任务）|APP端生成待付款的订单，您可以立即支付，\n也可以等待下次扫描/复印时再进行支付\n（注意每次扫描/复印前必须保证上次的订单已付款，否则无法进行扫描/复印）');
INSERT INTO `ehcore`.`eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', 'print_subject', 'zh_CN', '打印订单');
INSERT INTO `ehcore`.`eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', 'print_surface', 'zh_CN', '面');

-- by dengs,添加打印机信息，目前只添加了公司的打印机 20170615
select IFNULL(max(id),-1) into @id from eh_siyin_print_printers;
INSERT INTO `eh_siyin_print_printers` (`id`, `namespace_id`, `owner_type`, `owner_id`, `reader_name`, `module_port`, `login_context`, `trademark`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, '0', NULL, NULL, 'TC101154727022', '8119', '/xeroxmfp/directLogin', '施乐', '2', NULL, NULL, NULL, NULL);

-- by dengs,云打印菜单配置 20170626
-- 添加菜单 -- 按照产品要求，添加菜单到左邻域

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
VALUES (41400, '云打印', '40000', NULL, NULL, '1', '2', '/40000/41400', 'park', '499', 41400);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
VALUES (41410, '打印记录', 41400, NULL, 'react:/cloud-print/record', '0', '2', '/40000/41400/41410', 'park', '500', NULL);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
VALUES (41420, '打印统计', 41400, NULL, 'react:/cloud-print/count', '0', '2', '/40000/41400/41420', 'park', '501', NULL);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
VALUES (41430, '打印价格', 41400, NULL, 'react:/cloud-print/setting', '0', '2', '/40000/41400/41430', 'park', '502', NULL);

-- 添加权限云打印
set @eh_acl_privileges_id = (select max(id) from eh_acl_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ((@eh_acl_privileges_id := @eh_acl_privileges_id+1), 0, '云打印', '云打印 全部权限', NULL);

-- 菜单对应的权限
SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, 41400, '云打印', 1, 1, '云打印  全部权限', 499);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, 41410, '打印记录', 1, 1, '打印记录  全部权限', 500);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, 41420, '打印统计', 1, 1, '打印统计  全部权限', 501);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, 41430, '打印价格', 1, 1, '打印价格  全部权限', 502);

-- 角色对应的菜单权限
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @eh_acl_privileges_id, 1001, 'EhAclRoles', 0, 1, NOW());

-- 模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES (41400, '云打印', 40000, '/40000/41400', 0, 2, 2, 0, UTC_TIMESTAMP());

-- 模块权限
SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), 41400, 1, @eh_acl_privileges_id, NULL, '0', UTC_TIMESTAMP());

-- 模块权限范围
SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 0, 41400, '云打印', NULL, NULL, NULL, 2);

-- by dengs 添加菜单 end

-- 菜单的范围暂时加到科技园做测试 by dengs,20170711
 SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 41400, '', 'EhNamespaces', 1000000, 2);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 41410, '', 'EhNamespaces', 1000000, 2);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 41420, '', 'EhNamespaces', 1000000, 2);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 41430, '', 'EhNamespaces', 1000000, 2);
 
 SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` 
(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`,
 `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, 
`min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, 
`scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 1000000, 0, 0, 0, '/home', 'Bizs', '云打印', '云打印', 
'', 1, 1, 14, '{"url":"http://printtest.zuolin.com/cloud-print/build/index.html?hideNavigationBar=1#/home#sign_suffix"}', 3, 0,
 1, 1, 1, 0, NULL, NULL, NULL, 0,
 'default', 1, NULL, NULL, 0, NULL);
 
 INSERT INTO `eh_launch_pad_items` 
(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`,
 `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, 
`min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, 
`scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 1000000, 0, 0, 0, '/home', 'Bizs', '云打印', '云打印', 
'', 1, 1, 14, '{"url":"http://printtest.zuolin.com/cloud-print/build/index.html?hideNavigationBar=1#/home#sign_suffix"}', 3, 0,
 1, 1, 1, 0, NULL, NULL, NULL, 0,
 'park_tourist', 1, NULL, NULL, 0, NULL);
 
 INSERT INTO `eh_launch_pad_items` 
(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`,
 `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, 
`min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, 
`scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 1000000, 0, 0, 0, '/home', 'Bizs', '云打印', '云打印', 
'', 1, 1, 14, '{"url":"http://printtest.zuolin.com/cloud-print/build/index.html?hideNavigationBar=1#/home#sign_suffix"}', 3, 0,
 1, 1, 1, 0, NULL, NULL, NULL, 0,
 'pm_admin', 1, NULL, NULL, 0, NULL);
 
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 1000000, 'EhOrganizations', NULL, 1, 30079, 1001, 'EhAclRoles', 0, 1, NOW());

 -- 服务广场 -- 添加到科技园域 -- 在alpha，beta执行 -- end
