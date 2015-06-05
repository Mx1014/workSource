SET foreign_key_checks = 0;

use ehcore;

INSERT INTO `eh_configurations`(`name`, `value`, `description`) VALUES ('db.init.timestamp', UTC_TIMESTAMP(), 'Database seeding timestamp');
INSERT INTO `eh_configurations`(`name`, `value`, `description`) VALUES ('sms.handler.type','MW','sms handler');
INSERT INTO `eh_configurations`(`name`, `value`, `description`) VALUES ('mw.port','9003','sms handler');
INSERT INTO `eh_configurations`(`name`, `value`, `description`) VALUES ('mw.password','223651','mw password');
INSERT INTO `eh_configurations`(`name`, `value`, `description`) VALUES ('mw.user','J02300','mw user');
INSERT INTO `eh_configurations`(`name`, `value`, `description`) VALUES ('mw.host','61.145.229.29','mw host ,special line');
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
INSERT INTO `eh_users`(`id`, `account_name`, `nick_name`, `status`, `create_time`, `salt`, `password_hash`) VALUES (1, 'root', 'system user', 1, 
    NOW(), 'baf7c0473ec68eda2643882cecfb13fe', '8c7f2be062ee6c96affb6d78b6bc12bc7c60891cec30a6366278ebc958e39b5b');

#
# Reserve IDs
#
INSERT INTO `eh_sequences`(`domain`, `start_seq`) VALUES('EhUsers', 10000);
INSERT INTO `eh_sequences`(`domain`, `start_seq`) VALUES('EhForums', 10000);

SET foreign_key_checks = 1;
