-- 黑名单权限
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1001, '0', '禁止话题、投票帖子发言', '包括发帖、评论、回复评论', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1002, '0', '禁止活动帖子发言', '包括发帖、评论、回复评论', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1003, '0', '禁止公告帖子发言', '包括发帖、评论、回复评论', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1004, '0', '禁止园区快讯板块发言', '包括发帖、评论、回复评论', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1005, '0', '禁止新建俱乐部板块', '包括新建俱乐部', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1006, '0', '禁止物业服务板块发言', '包括【包括投诉建议、紧急求助、咨询求助、报修】发帖', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1007, '0', '禁止恶意留言', '包括在消息模块（包括群聊或私信）中发言', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1008, '0', '禁止意见反馈', '包括发帖、评论、回复评论', NULL);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'system', NULL, '1', '1001', '2000', 'EhAclRoles', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'system', NULL, '1', '1002', '2000', 'EhAclRoles', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'system', NULL, '1', '1003', '2000', 'EhAclRoles', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'system', NULL, '1', '1004', '2000', 'EhAclRoles', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'system', NULL, '1', '1005', '2000', 'EhAclRoles', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'system', NULL, '1', '1006', '2000', 'EhAclRoles', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'system', NULL, '1', '1007', '2000', 'EhAclRoles', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'system', NULL, '1', '1008', '2000', 'EhAclRoles', '0', '1', UTC_TIMESTAMP());
	
-- 错误提示语配置
UPDATE `eh_locale_strings` SET `text` = '您权限不足' WHERE `scope` = 'general' AND `code` = '505';
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'blacklist', '600000', 'zh_CN', '由于您已被禁言，不能正常使用该功能。');	
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'blacklist', '600020', 'zh_CN', '未查询到符合条件的对象，请查证。');	
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'blacklist', '600010', 'zh_CN', '黑名单已存在。');	

-- 黑名单权限 
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) values('30600','黑名单管理','30000',NULL,'react:/blacklist-management/black-list','0','2','/30000/30600','park','361','30600');
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10090, 0, '黑名单管理 管理员', '黑名单管理 管理员权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10090, 30600, '黑名单管理', 1, 1, '黑名单管理 管理员权限', 361);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 10090, 1001,'EhAclRoles', 0, 1, NOW());


SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 30600, '', 'EhNamespaces', 0, 2);

INSERT INTO `eh_locale_templates` (`scope`, `code`,`locale`,`description`, `text`, `namespace_id`) VALUES( 'blacklist.notification', '1', 'zh_CN', '通知用户已经被加入黑名单', '由于您的发言涉及部分违反相关版规行为，您已被禁言，将不能正常使用部分板块的发言功能。如有疑问，请联系左邻客服。', 0);	
INSERT INTO `eh_locale_templates` (`scope`, `code`,`locale`,`description`, `text`, `namespace_id`) VALUES( 'blacklist.notification', '2', 'zh_CN', '通知用户已经被解除黑名单', '您的禁言已被解除，可继续使用各大板块的发言功能。如有疑问，请联系左邻客服。', 0);	