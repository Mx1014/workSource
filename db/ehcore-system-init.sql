SET foreign_key_checks = 0;

use ehcore;

INSERT INTO `eh_configurations`(`name`, `value`, `description`) VALUES ('db.init.timestamp', UTC_TIMESTAMP(), 'Database seeding timestamp');

ALTER TABLE `eh_acl_privileges` AUTO_INCREMENT = 4096;
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(1, 0, 'All');
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(2, 0, 'Visible');
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(3, 0, 'Read');
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(4, 0, 'Create');
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(5, 0, 'Write');
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(6, 0, 'Delete');

INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(100, 2, 'forum.topic.new');
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(101, 2, 'forum.topic.delete');
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(102, 2, 'forum.reply.new');
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(103, 2, 'forum.reply.delete');

ALTER TABLE `eh_acl_roles` AUTO_INCREMENT = 4096;
INSERT INTO `eh_acl_roles`(`id`, `app_id`, `name`) VALUES(1, 0, 'Anonymous');
INSERT INTO `eh_acl_roles`(`id`, `app_id`, `name`) VALUES(2, 0, 'SystemAdmin');
INSERT INTO `eh_acl_roles`(`id`, `app_id`, `name`) VALUES(3, 0, 'AuthenticatedUser');
INSERT INTO `eh_acl_roles`(`id`, `app_id`, `name`) VALUES(4, 0, 'ResourceCreator');
INSERT INTO `eh_acl_roles`(`id`, `app_id`, `name`) VALUES(5, 0, 'ResourceAdmin');
INSERT INTO `eh_acl_roles`(`id`, `app_id`, `name`) VALUES(6, 0, 'ResourceOperator');
INSERT INTO `eh_acl_roles`(`id`, `app_id`, `name`) VALUES(7, 0, 'ResourceUser');
INSERT INTO `eh_acl_roles`(`id`, `app_id`, `name`) VALUES(8, 0, 'SystemExtension');

ALTER TABLE `eh_apps` AUTO_INCREMENT = 4096;

ALTER TABLE `eh_namespaces` AUTO_INCREMENT = 4096;

#
# populate default system user root/password
#
INSERT INTO `eh_users`(`id`, `account_name`, `nick_name`, `status`, `create_time`, `password_hash`) VALUES (1, 'root', 'system user', 1, 
    NOW(), '10:8e70e9c1ebf861202a28ed0020c4db0f4d9a3a3d29fb1c4d:40d84ad3b14b8da5575274136678ca1ab07d114e1d04ef70');

#
# Reserve IDs
#
INSERT INTO `eh_sequences`(`domain`, `start_seq`) VALUES('EhUsers', 10000);
INSERT INTO `eh_sequences`(`domain`, `start_seq`) VALUES('EhForums', 10000);

SET foreign_key_checks = 1;
