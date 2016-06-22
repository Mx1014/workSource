INSERT INTO `eh_organizations` (`id`,`parent_id`,`organization_type`,`name`,`path`,`level`,`status`,`group_type`,`create_time`,`update_time`,`directly_enterprise_id`,`namespace_id`,`group_id`) 
select `id`,0,'ENTERPRISE',`name`,concat('/',`id`),1,if(`status` = 0,1,2),'ENTERPRISE',`create_time` ,`update_time`,0,`namespace_id`,`id` FROM `eh_groups` WHERE `discriminator` = 'enterprise';

set @organization_details_id = 10001;
INSERT INTO `eh_organization_details`(`id`,`organization_id`,`description`,`contact`,`address`,`create_time`,`display_name`,`member_count`,`checkin_date`,`avatar`,`post_uri`)
SELECT  (@organization_details_id := @organization_details_id + 1),`id`,`description`,`string_tag1`,`string_tag2`,`create_time`,IF(IFNULL(`display_name`,`name`) = '',name,IFNULL(`display_name`,`name`)),`member_count`,`string_tag3`,`avatar`,`string_tag5` FROM `eh_groups` WHERE `discriminator` = 'enterprise';

set @organization_member_id = 45;
SET foreign_key_checks = 0;
INSERT INTO `eh_organization_members` 
(`id`,`organization_id`,`contact_name`,`string_tag1`,`avatar`,`target_id`,`integral_tag1`,`status`,`integral_tag2`,`create_time`,`string_tag2`,`gender`,`integral_tag3`,`contact_token`,`target_type`,`contact_type`,`member_group`,`group_id`)
SELECT (@organization_member_id + c.`id`) id, c.`enterprise_id`,c.`name`,c.`nick_name`,c.`avatar`,c.`user_id`,c.`role`,c.`status`,c.`creator_uid`,c.`create_time`,c.`string_tag1`,IF(c.`string_tag2` = '��',1,IF(c.`string_tag2`='Ů',2,0)),c.`string_tag3`,e.`entry_value`,IF(c.`user_id` = 0,'UNTRACK', 'USER'),0,'manager',IFNULL((select @depatement_id_add + contact_group_id from `eh_enterprise_contact_group_members` where contact_id = c.id limit 1 ), 0) from `eh_enterprise_contacts` c left join `eh_enterprise_contact_entries` e on c.`id` = e.`contact_id` GROUP BY id; 


set @organization_addresses_id = 10001;
INSERT INTO `eh_organization_addresses`
(`id`,`organization_id`,`address_id`,`status`,`creator_uid`,`create_time`,`operator_uid`,`process_code`,`process_details`,`proof_resource_uri`,`approve_time`,`update_time`,`building_id`,`building_name`)
SELECT  @organization_addresses_id + `id`, `enterprise_id`,`address_id`,`status`,`creator_uid`,`create_time`,`operator_uid`,`process_code`,`process_details`,`proof_resource_uri`,`approve_time`,`update_time`,`building_id`,`building_name` FROM `eh_enterprise_addresses` ;

set @organization_community_requests_id = 10001;
INSERT INTO `eh_organization_community_requests`
(`id`,`community_id`,`member_type`,`member_id`,`member_status`,`creator_uid`,`create_time`,`operator_uid`,`process_code`,`process_details`,`proof_resource_uri`,`approve_time`,`requestor_comment`,`operation_type`,`inviter_uid`,`invite_time`,`update_time`)
SELECT @organization_community_requests_id + `id`,`community_id`,'organization',`member_id`,`member_status`,`creator_uid`,`create_time`,`operator_uid`,`process_code`,`process_details`,`proof_resource_uri`,`approve_time`,`requestor_comment`,2,`inviter_uid`,`invite_time`,`update_time`  FROM `eh_enterprise_community_map` limit 1;

#
#20160505
#
INSERT INTO `eh_acl_role_assignments` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`role_id`,`creator_uid`,`create_time`) values (6,'EhOrganizations',419,'EhUsers',100020,1005,1,now());


update eh_organizations set organization_type = 'PM' where id = 419;

	
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`) 
	VALUES (49, 999997, '0', '0', '0', '/home', 'Bizs', '门禁', '门禁', 'cs://1/image/aW1hZ2UvTVRwalptSm1PRFE1T1dObVpqTXdZelZqWVRNME56azBNelk1TkdNNU1UUm1Zdw', '1', '1', '40', '', '1', '0', '1', '1', '', '0', NULL, NULL, NULL);


	
INSERT INTO `eh_acl_roles` (`id`, `app_id`, `name`, `description`, `tag`, `namespace_id`, `owner_type`, `owner_id`) VALUES ('1001', '32', '超级管理员', '所有权限（All rights）', NULL, '0', 'EhOrganizations', NULL);
INSERT INTO `eh_acl_roles` (`id`, `app_id`, `name`, `description`, `tag`, `namespace_id`, `owner_type`, `owner_id`) VALUES ('1002', '32', '普通管理员', '不能添加修改删除管理员，其他权限都有', NULL, '0', 'EhOrganizations', NULL);
INSERT INTO `eh_acl_roles` (`id`, `app_id`, `name`, `description`, `tag`, `namespace_id`, `owner_type`, `owner_id`) VALUES ('1003', '32', '财务管理', '财务管理（Accounting）', NULL, '0', NULL, NULL);
INSERT INTO `eh_acl_roles` (`id`, `app_id`, `name`, `description`, `tag`, `namespace_id`, `owner_type`, `owner_id`) VALUES ('1004', '32', '招商管理', '招商管理（Property_mgt）', NULL, '0', NULL, NULL);
INSERT INTO `eh_acl_roles` (`id`, `app_id`, `name`, `description`, `tag`, `namespace_id`, `owner_type`, `owner_id`) VALUES ('1005', '32', '超级管理员', '内部管理的所有权限', NULL, '0', 'EhOrganizations', NULL);
INSERT INTO `eh_acl_roles` (`id`, `app_id`, `name`, `description`, `tag`, `namespace_id`, `owner_type`, `owner_id`) VALUES ('1006', '32', '普通管理员', '不能添加修改删除管理员，其他内部管理的所有权限都有', NULL, '0', 'EhOrganizations', NULL);
INSERT INTO `eh_acl_roles` (`id`, `app_id`, `name`, `description`, `tag`, `namespace_id`, `owner_type`, `owner_id`) VALUES ('1007', '32', '客服管理', '客服管理（ke fu）', NULL, '0', NULL, NULL);
INSERT INTO `eh_acl_roles` (`id`, `app_id`, `name`, `description`, `tag`, `namespace_id`, `owner_type`, `owner_id`) VALUES ('1010', '32', '客服管理员', NULL, NULL, '1000000', 'EhOrganizations', '1000001');
	
	
	
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`) 
	VALUES ('34', '999997', 'ServiceMarketLayout', '{\"versionCode\":\"2016031201\",\"versionName\":\"3.3.0\",\"layoutName\":\"ServiceMarketLayout\",\"displayName\":\"服务市场\",\"groups\":[{\"groupName\":\"\",\"widget\":\"Banners\",\"instanceConfig\":{\"itemGroup\":\"Default\"},\"style\":\"Default\",\"defaultOrder\":1,\"separatorFlag\":0,\"separatorHeight\":0},{\"groupName\":\"\",\"widget\":\"Navigator\",\"instanceConfig\":{\"itemGroup\":\"GovAgencies\"},\"style\":\"Default\",\"defaultOrder\":2,\"separatorFlag\":1,\"separatorHeight\":21,\"columnCount\":4},{\"groupName\":\"\",\"widget\":\"Coupons\",\"instanceConfig\":{\"itemGroup\":\"Coupons\"},\"style\":\"Default\",\"defaultOrder\":3,\"separatorFlag\":1,\"separatorHeight\":21},{\"groupName\":\"商家服务\",\"widget\":\"Navigator\",\"instanceConfig\":{\"itemGroup\":\"Bizs\"},\"style\":\"Default\",\"defaultOrder\":5,\"separatorFlag\":0,\"separatorHeight\":0}]}', '2016031201', '0', '2', '2016-03-12 19:16:25', 'pm_admin');
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`) 
	VALUES ('35', '999997', 'SceneNoticeLayout', '{\"versionCode\":\"2015072815\",\"versionName\":\"3.0.0\",\"displayName\":\"公告管理\",\"layoutName\":\"SceneNoticeLayout\",\"groups\":[{\"groupName\":\"\",\"widget\":\"Navigator\",\"instanceConfig\":{\"itemGroup\":\"GaActions\"},\"style\":\"Default\",\"defaultOrder\":2,\"separatorFlag\":1,\"separatorHeight\":21,\"columnCount\":4},{\"groupName\":\"\",\"widget\":\"ScenePosts\",\"instanceConfig\":{\"filterType\":\"ga_notice\"},\"style\":\"Default\",\"defaultOrder\":3,\"separatorFlag\":0,\"separatorHeight\":0}]}', '2015082914', '2015061701', '2', '2015-06-27 14:04:57', 'pm_admin');


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
	VALUES ('10055', '999997', '0', '0', '0', '/home', 'Bizs', 'PUNCH', '打卡', 'cs://1/image/aW1hZ2UvTVRveE0ySmpaVFZpTWpoalptRTNZMll3TWpreVpHWTNabU5pWW1FM1lqZGhOQQ', '1', '1', '23', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('10058', '999997', '0', '0', '0', '/home', 'Bizs', 'DoorManagement', '门禁', 'cs://1/image/aW1hZ2UvTVRwalptSm1PRFE1T1dObVpqTXdZelZqWVRNME56azBNelk1TkdNNU1UUm1Zdw', '1', '1', '40', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
	