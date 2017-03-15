INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10138, '0', '物业报修 代发权限', '物业报修 代发权限', NULL);
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`, `role_type`)
	SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `id`, 1001, 0, 1, now(), 'EhAclRoles' FROM `eh_acl_privileges` WHERE id = 10138;
	
INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ('pmtask', '10012', 'zh_CN', '没有代发权限！');
INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ('pmtask', '10013', 'zh_CN', '查不到该用户信息！');

