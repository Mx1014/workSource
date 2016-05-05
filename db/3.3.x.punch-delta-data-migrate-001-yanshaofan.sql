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
