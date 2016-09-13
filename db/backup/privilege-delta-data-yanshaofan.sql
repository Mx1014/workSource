-- 
-- 检查目前最大的id，然后设置@acl_id
-- SELECT MAX(id) FROM `eh_acls`;
-- 

set @acl_id = (SELECT MAX(id) FROM `eh_acls`);

DELETE FROM `eh_acls` WHERE `role_id` IN (1001,1002,1005,1006,1010);

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, id, 1001,0,1,now() FROM `eh_acl_privileges`;

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, id, 1002,0,1,now() FROM `eh_acl_privileges`;
DELETE FROM `eh_acls` WHERE `privilege_id` = 605 AND `role_id` = 1002;

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%51000/%');
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%52000/%');
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%56000/%');
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%59000/%');



INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1006,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%51000/%');
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1006,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%52000/%');
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1006,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%56000/%');
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1006,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%59000/%');


DELETE FROM `eh_acls` WHERE `privilege_id` = 605 AND `role_id` = 1006;

-- 增加固定角色
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1010,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58210;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1010,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58220;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1010,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58230;
