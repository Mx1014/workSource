-- 快递初始化数据，add by tt, 20170504
INSERT INTO `eh_express_companies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `logo`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (1, 0, '', 0, 0, 'ems', 'cs://1/image/aW1hZ2UvTVRwak9XSTJOVFJqWXpjMVkyTmtNVGt4WW1NNU1qaGlNR0k1WlRNelpXRTJNdw', 2, 0, now(), now(), 0);
select max(id) into @id from `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'express', '10000', 'zh_CN', '订单状态错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'express', '10001', 'zh_CN', '您没有相关权限');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'express', '10002', 'zh_CN', '用户（%s）未注册，无法添加');

-- 添加菜单，add by tt, 20170504
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES (40700, '快递管理', 40000, NULL, NULL, 1, 2, '/40000/70100', 'park', 495, 40700);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES (40710, '快递员管理', 40700, NULL, 'react:/deliver-management/permission-setting/40700', 0, 2, '/40000/40700/40710', 'park', 496, 40700);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES (40720, '订单记录', 40700, NULL, 'react:/deliver-management/order-record/40700', 0, 2, '/40000/40700/40720', 'park', 497, 40700);

-- 添加权限，add by tt, 20170504
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30025, 0, '快递管理 管理员', '快递管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30026, 0, '快递管理 快递员', '快递管理 业务模块权限', NULL);

-- 关联菜单和权限，add by tt, 20170504
select max(id) into @id from `eh_web_menu_privileges`;
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@id:=@id+1, 30025, 40700, '快递管理', 1, 1, '快递管理 全部权限', 711);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@id:=@id+1, 30025, 40710, '快递管理', 1, 1, '快递管理 全部权限', 712);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@id:=@id+1, 30025, 40720, '快递管理', 1, 1, '快递管理 全部权限', 713);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@id:=@id+1, 30026, 40700, '快递管理', 1, 1, '快递管理 快递员权限', 711);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@id:=@id+1, 30026, 40720, '快递管理', 1, 1, '快递管理 快递员权限', 713);

-- 添加快递部门，add by tt, 20170504
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `department_type`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `show_flag`, `namespace_organization_token`, `namespace_organization_type`, `size`) VALUES (1007018, 1007144, 'PM', '快递部', 0, NULL, '/1007144/1007018', 2, 2, NULL, 'DEPARTMENT', '2016-10-31 15:02:13', '2016-10-31 15:02:13', 1007144, 999985, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL);

-- 关联权限，add by tt, 20170504
select max(id) into @id from `eh_acls`;
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `comment_tag1`, `comment_tag2`, `comment_tag3`, `comment_tag4`, `comment_tag5`) VALUES (@id:=@id+1, 'EhCommunities', 240111044331055035, 1, 30026, 1007018, 0, 1, now(), 999985, 'EhOrganizations', 'EhCommunities240111044331055035.M20000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `comment_tag1`, `comment_tag2`, `comment_tag3`, `comment_tag4`, `comment_tag5`) VALUES (@id:=@id+1, 'EhCommunities', 240111044331055036, 1, 30026, 1007018, 0, 1, now(), 999985, 'EhOrganizations', 'EhCommunities240111044331055036.M20000', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `comment_tag1`, `comment_tag2`, `comment_tag3`, `comment_tag4`, `comment_tag5`) VALUES (@id:=@id+1, 'EhOrganizations', NULL, 1, 30025, 1005, 0, 1, now(), 0, 'EhAclRoles', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `comment_tag1`, `comment_tag2`, `comment_tag3`, `comment_tag4`, `comment_tag5`) VALUES (@id:=@id+1, 'EhOrganizations', NULL, 1, 30025, 1001, 0, 1, now(), 0, 'EhAclRoles', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- 显示的菜单，add by tt, 20170504
select max(id) into @id from `eh_web_menu_scopes`;
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40700, '', 'EhNamespaces', 999985, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40710, '', 'EhNamespaces', 999985, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40720, '', 'EhNamespaces', 999985, 2);

-- 服务广场配置，add by tt, 20170523(beta)
select max(id) into @id from `eh_launch_pad_items`;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999985, 0, 0, 0, '/home', 'Bizs', '快递', '快递', 'cs://1/image/aW1hZ2UvTVRveU1URXhNR1F5WkdVMFlUZzFNREUyTTJRNU1qVmhNekEzTWpjek5ETTJOQQ', 1, 1, 14, '{"url":"http://beta.zuolin.com/deliver/dist/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999985, 0, 0, 0, '/home', 'Bizs', '快递', '快递', 'cs://1/image/aW1hZ2UvTVRveU1URXhNR1F5WkdVMFlUZzFNREUyTTJRNU1qVmhNekEzTWpjek5ETTJOQQ', 1, 1, 14, '{"url":"http://beta.zuolin.com/deliver/dist/index.html?hideNavigationBar=1#/home_page#sign_suffixl"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0, NULL);

-- 服务广场配置，add by tt, 20170523(core)
-- select max(id) into @id from `eh_launch_pad_items`;
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999985, 0, 0, 0, '/home', 'Bizs', '快递', '快递', 'cs://1/image/aW1hZ2UvTVRveU1URXhNR1F5WkdVMFlUZzFNREUyTTJRNU1qVmhNekEzTWpjek5ETTJOQQ', 1, 1, 14, '{"url":"http://core.zuolin.com/deliver/dist/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0, NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999985, 0, 0, 0, '/home', 'Bizs', '快递', '快递', 'cs://1/image/aW1hZ2UvTVRveU1URXhNR1F5WkdVMFlUZzFNREUyTTJRNU1qVmhNekEzTWpjek5ETTJOQQ', 1, 1, 14, '{"url":"http://core.zuolin.com/deliver/dist/index.html?hideNavigationBar=1#/home_page#sign_suffixl"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0, NULL);


-- merge from activity-3.0.0 20170522  start

-- 添加中文字段 add by yanjun 20170502
SET @eh_locale_strings_id = (SELECT MAX(id) FROM `eh_locale_strings`);
insert into `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) values(@eh_locale_strings_id := @eh_locale_strings_id + 1,'activity','10020','zh_CN','活动报名缴费');

insert into `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) values(@eh_locale_strings_id := @eh_locale_strings_id + 1,'activity','10021','zh_CN','活动待确认');

insert into `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) values(@eh_locale_strings_id := @eh_locale_strings_id + 1,'activity','10022','zh_CN','活动已确认');

insert into `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) values(@eh_locale_strings_id := @eh_locale_strings_id + 1,'activity','10023','zh_CN','活动待支付');

-- 增加通知支付的消息模板 add by yanjun 20170513
-- 增加通知支付的消息模板 add by yanjun 20170513
SET @eh_locale_templates_id = (SELECT MAX(id) FROM eh_locale_templates); 
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES(@eh_locale_templates_id := @eh_locale_templates_id + 1, 'activity.notification','8','zh_CN','活动待确认，通知活动创建者进行确认','“${userName}”报名了活动「${postName}」，请尽快确认。','0');

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES(@eh_locale_templates_id := @eh_locale_templates_id + 1, 'activity.notification','9','zh_CN','活动被管理员同意，并且不需要支付，通知活动报名者','你报名的活动「${postName}」已被确认。','0');

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES(@eh_locale_templates_id := @eh_locale_templates_id + 1, 'activity.notification','10','zh_CN','活动被管理员同意，通知活动报名者进行支付','你报名的活动「${postName}」已被确认，请在${payTimeDays}天${payTimeHours}小时之内尽快完成支付。','0');


-- 活动报名表新增organization_id后，刷新数据  add by yanjun 20170517
UPDATE eh_activity_roster r
SET r.organization_id = (SELECT
                           a.id
                         FROM eh_organizations a,
                           eh_organization_members b,
                           eh_activities c
                         WHERE a.id = b.organization_id
                             AND a.namespace_id = c.namespace_id
                             AND c.id = r.activity_id
                             AND r.uid = b.target_id
                             AND b.target_type = 'USER'
                         LIMIT 1 );
                         
-- 刷新活动tag，将null和''的刷成'其他'，方便统计  add by yanjun 20170518
UPDATE eh_activities ac SET ac.tag = '其他' WHERE ac.tag IS NULL OR ac.tag = '';

-- 添加中文字段，当取消报名时用于提示超过截止日期  add by yanjun 20170519
SET @eh_locale_strings_id = (SELECT MAX(id) FROM `eh_locale_strings`);
insert into `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) values(@eh_locale_strings_id := @eh_locale_strings_id + 1,'activity','10025','zh_CN','报名已截止，不可取消报名');

-- merge from activity-3.0.0 20170522  end