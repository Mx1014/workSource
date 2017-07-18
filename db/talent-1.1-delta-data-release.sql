-- 企业人才申请的发消息，add by tt, 20170710
select max(id) into @id from `eh_locale_templates`;
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id+1, 'talent.notification', 1, 'zh_CN', '企业人才申请时发消息', '企业人才|收到一条企业人才的申请单', 0);


-- 以下菜单未经李磊同意不得在现网执行！！！！！！！！！！！
-- 先删除之前配的菜单，add by tt, 20170527
delete from `eh_web_menus` where id in (40730, 40731, 40732, 40733, 40734);
delete from `eh_acl_privileges` where name = '企业人才 管理员';
delete from `eh_web_menu_privileges` where menu_id in (40730, 40731, 40732, 40733, 40734);
delete from `eh_web_menu_scopes` where menu_id in (40730, 40731, 40732, 40733, 40734) and owner_id = 999990;

-- 菜单，add by tt, 20170527
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `category`) VALUES (40730, '企业人才', 40000, NULL, 'react:/enterprise-management/talent', 1, 2, '/40000/40730', 'park', 498, 40730,'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `category`) VALUES (40731, '人才管理', 40730, NULL, 'react:/enterprise-management/talent-list', 1, 2, '/40000/40730/40731', 'park', 499, 40730,'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `category`) VALUES (40732, '申请设置', 40730, NULL, 'react:/enterprise-management/apply-setting', 1, 2, '/40000/40730/40732', 'park', 500, 40730,'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `category`) VALUES (40733, '消息推送', 40730, NULL, 'react:/enterprise-management/message-push', 1, 2, '/40000/40730/40733', 'park', 501, 40730,'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `category`) VALUES (40734, '申请记录', 40730, NULL, 'react:/enterprise-management/apply-record', 1, 2, '/40000/40730/40734', 'park', 502, 40730,'module');

-- 权限，add by tt, 20170527
select max(id) into @pri_id from `eh_acl_privileges`;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@pri_id:=@pri_id+1, 0, '企业人才 管理员', '企业人才 业务模块权限', NULL);

-- 菜单关联权限，add by tt, 20170527
select max(id) into @id from `eh_web_menu_privileges`;
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@id:=@id+1, @pri_id, 40730, '企业人才', 1, 1, '企业人才 全部权限', 714);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@id:=@id+1, @pri_id, 40731, '企业人才', 1, 1, '企业人才 全部权限', 715);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@id:=@id+1, @pri_id, 40732, '企业人才', 1, 1, '企业人才 全部权限', 716);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@id:=@id+1, @pri_id, 40733, '企业人才', 1, 1, '企业人才 全部权限', 717);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@id:=@id+1, @pri_id, 40734, '企业人才', 1, 1, '企业人才 全部权限', 718);

-- 权限赋予超管，add by tt, 20170527
select max(id) into @id from `eh_acls`;
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `comment_tag1`, `comment_tag2`, `comment_tag3`, `comment_tag4`, `comment_tag5`) VALUES (@id+1, 'EhOrganizations', NULL, 1, @pri_id, 1001, 0, 1, '2017-06-05 09:41:07', 0, 'EhAclRoles', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- 显示菜单，add by tt, 20170527
select max(id) into @id from `eh_web_menu_scopes`;
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40730, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40731, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40732, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40733, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40734, '', 'EhNamespaces', 999990, 2);
