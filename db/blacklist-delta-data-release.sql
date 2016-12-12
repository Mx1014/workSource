-- 黑名单权限
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1001, '0', '禁止发现板块发言', '包括发帖、评论、回复评论', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1002, '0', '禁止活动板块发言', '包括发帖、评论、回复评论', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1003, '0', '禁止公告板块发言', '包括发帖、评论、回复评论', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1004, '0', '禁止园区快讯板块发言', '包括发帖、评论、回复评论', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1005, '0', '禁止俱乐部板块发言', '包括新建俱乐部、发帖、评论、回复评论', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1006, '0', '禁止物业服务板块发言', '包括【包括投诉建议、紧急求助、咨询求助、报修】发帖', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1007, '0', '禁止恶意举报', '包括活动、发现、公告、园区快讯、俱乐部帖子', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1008, '0', '禁止恶意留言', '包括在消息模块（包括群聊或私信）中发言', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1009, '0', '禁止意见反馈', '包括发帖、评论、回复评论', NULL);

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
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'system', NULL, '1', '1009', '2000', 'EhAclRoles', '0', '1', UTC_TIMESTAMP());