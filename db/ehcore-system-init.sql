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

INSERT INTO `eh_sequences`(`domain`, `start_seq`) VALUES('EhForums', 4096);

SET foreign_key_checks = 1;
