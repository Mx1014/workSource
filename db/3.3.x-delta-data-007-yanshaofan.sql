
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 11, 'zh_CN','接受任务时回复的帖子消息', '该任务已由{targetUName}（{targetUToken}）确认，将会很快联系您。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 12, 'zh_CN', '新发布一条任务短信消息','您有一个新的任务，请尽快处理。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 13, 'zh_CN', '处理任务时发送的短信消息','{operatorUName}已分配给你一个物业任务，请尽快联系业主（{createUName}）处理。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 14, 'zh_CN', '处理任务时回复的帖子消息','{operatorUName}（{operatorUToken}）已将该任务指派给{targetUName}处理，请等待。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 15, 'zh_CN', '任务被拒绝收到的短信消息','该任务已被{targetUName}（{targetUToken}）拒绝，请重新分配');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 16, 'zh_CN', '任务已完成后的短信消息','您的服务已完成，滑动去查看详情');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 17, 'zh_CN', '任务已完成后回复的帖子消息','该服务已由{operatorUName}完成，稍后我们会将进行回访');


INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`) values (171,0,'all.task.posts.list',null,'MANAGER_TASK');
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`) values (172,0,'guarantee.task.posts.list',null,'MANAGER_TASK');
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`) values (173,0,'seekHelp.task.posts.list',null,'MANAGER_TASK');



INSERT INTO `eh_acls` (`id`,`owner_type`,`owner_id`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) values (313,'EhOrganizations',null,1,171,1001,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`owner_id`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) values (314,'EhOrganizations',null,1,172,1001,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`owner_id`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) values (315,'EhOrganizations',null,1,173,1001,0,1,now());

INSERT INTO `eh_acl_role_assignments` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`role_id`,`creator_uid`,`create_time`) values (20000,'system',null,'EhUsers',202608,1002,1,now());
INSERT INTO `eh_acl_role_assignments` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`role_id`,`creator_uid`,`create_time`) values (20001,'system',null,'EhUsers',196607,1003,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`owner_id`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) values (316,'EhOrganizations',null,1,172,1002,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`owner_id`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) values (317,'EhOrganizations',null,1,173,1003,0,1,now());


