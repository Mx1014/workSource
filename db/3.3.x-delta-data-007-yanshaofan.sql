
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 10, 'zh_CN','接受任务时回复的帖子消息', '该任务已由${targetUName}（${targetUToken}）确认，将会很快联系您。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 11, 'zh_CN', '新发布一条任务短信消息','您有一个新的任务，请尽快处理。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 12, 'zh_CN', '处理任务时发送的短信消息','${operatorUName}给你分配了一个任务，请直接联系用户${targetUName}（电话${targetUToken}），帮他处理该问题。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 13, 'zh_CN', '处理任务时回复的帖子消息','${operatorUName}（${operatorUToken}）已将该任务指派给${targetUName}处理，请等待。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 14, 'zh_CN', '任务被拒绝收到的短信消息','该任务已被${targetUName}（${targetUToken}）拒绝，请重新分配');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 15, 'zh_CN', '任务已完成后的短信消息','您的服务已完成');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 16, 'zh_CN', '任务已完成后回复的帖子消息','该服务已由${operatorUName}完成，稍后我们会将进行回访');


INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1012, 1, 0, '紧急求助', '帖子/紧急求助', 0, 2, UTC_TIMESTAMP());


delete from eh_launch_pad_items where id = 10006; 
INSERT INTO `eh_launch_pad_items`(`id`, `scene_type`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`) 
	VALUES (10006, 'pm_admin', 0, 0, 0, 0, '/home','GovAgencies','NoticeManagerment','公告管理','cs://1/image/aW1hZ2UvTVRveU5tSXhOMlV6TmpSak5qQmpOMlk1T1RVMFpqSTNPR0U1TXpNMll6QmlNdw',1,1,43, '{}', 0, 0, 1, 1, '', '0', NULL, NULL, NULL);

	
UPDATE `eh_acl_roles` SET `namespace_id` = 0, `owner_type` = 'EhOrganizations', `name` = '物业超级管理员',description='所有权限（All rights）'  where `id` = 1001;
UPDATE `eh_acl_roles` SET `namespace_id` = 0, `owner_type` = 'EhOrganizations', `name` = '物业普通管理员',description='不能添加修改删除管理员，其他权限都有' where `id` = 1002;
UPDATE `eh_acl_roles` SET `namespace_id` = 0, `owner_type` = 'EhOrganizations', `name` = '企业超级管理员',description='内部管理的所有权限' where `id` = 1005;
UPDATE `eh_acl_roles` SET `namespace_id` = 0, `owner_type` = 'EhOrganizations', `name` = '企业普通管理员',description='不能添加修改删除管理员，其他内部管理的所有权限都有' where `id` = 1006;

#
#区分小区任务
#
UPDATE `eh_organization_tasks` eot SET `visible_region_type` = (SELECT `visible_region_type` FROM `eh_forum_posts` WHERE `embedded_id` = eot.id and embedded_app_id =27 limit 0,1), `visible_region_id` = (SELECT `visible_region_id` FROM `eh_forum_posts` WHERE `embedded_id` = eot.id and embedded_app_id =27 limit 0,1);


#
#directly under the company 修改
#
UPDATE `eh_organizations` SET directly_enterprise_id = 1000001 WHERE directly_enterprise_id = 178395;
UPDATE `eh_organizations` SET directly_enterprise_id = 1000100 WHERE directly_enterprise_id = 178689;
UPDATE `eh_organizations` SET directly_enterprise_id = 1000631 WHERE directly_enterprise_id = 180000; 