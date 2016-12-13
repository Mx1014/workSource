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
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'blacklist', '600000', 'zh_CN', '您已被管理员禁止了此操作。');	
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'blacklist', '600020', 'zh_CN', '未查询到符合条件的对象，请查证。');	
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'blacklist', '600010', 'zh_CN', '黑名单已存在。');	