-- added by wh 2016-10-11 设置活跃用户数的区间
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'active.count','6-10','active count ');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (999987, 'active.count','6-10','active count ');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (1000000, 'active.count','6-10','active count ');

-- added by sfyan 20161011
set @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) VALUES((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 315, 1001,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) VALUES((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 315, 1002,0,1,now());

-- added by sfyan 20161011
UPDATE `eh_web_menu_scopes` SET `menu_name` = '创业活动', `apply_policy` = 1 WHERE `owner_type` = 'EhNamespaces' AND `owner_id` = 999987 AND `menu_id` = 11100;
UPDATE `eh_web_menu_scopes` SET `menu_name` = '路演直播', `apply_policy` = 1 WHERE `owner_type` = 'EhNamespaces' AND `owner_id` = 999987 AND `menu_id` = 11400;