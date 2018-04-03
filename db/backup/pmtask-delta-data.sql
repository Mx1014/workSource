
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ('340', 'pmtask', '10007', 'zh_CN', '该单已被其他人处理，请返回主界面刷新任务');

delete from eh_locale_templates where scope = 'pmtask.notification' and code = 7;

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('205', 'pmtask.notification', '7', 'zh_CN', '任务操作模版', '${creatorName} ${creatorPhone}已发起一个${categoryName}单，请尽快处理', '0');

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('350', 'sms.default.yzx', '15', 'zh_CN', '物业任务3-深业', '32949', '999992');

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`) 
VALUES ('20190', '统计', '20100', NULL, null, '0', '2', '/20000/20100/20190', 'park', '245');
INSERT INTO `eh_web_menus` VALUES ('20191', '服务统计', '20190', null, 'task_statistics', '0', '2', '/20000/20100/20190/20191', 'park', '180');
INSERT INTO `eh_web_menus` VALUES ('20192', '人员评分统计', '20190', null, 'staffScore_statistics', '0', '2', '/20000/20100/20190/20192', 'park', '181');


INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1020, '0', '人员评分统计', '人员评分统计', NULL);
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '1020', '1001', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '1020', '1002', '0', '1', UTC_TIMESTAMP());

-- INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10056, '0', '俱乐部管理', '俱乐部管理 全部权限', NULL);
-- INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10057, '0', '审核俱乐部', '审核俱乐部 全部权限', NULL);

INSERT INTO `eh_web_menu_privileges` VALUES ('300', '907', '20191', '服务统计', '1', '1', '服务统计 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('301', '907', '20191', '人员评分统计', '1', '1', '人员评分统计 全部权限', '710');
