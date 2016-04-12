

#
# Check whether the repeated data migration
#

select `name`,count(*),`namespace_id`,`id` FROM `eh_groups` WHERE `discriminator` = 'enterprise' group by name having count(*) > 1;

#
#
# 深圳市中融科互联网金融股份有限公司 统一联系人为 王涛
# 招商银行股份有限公司深圳南山支行 楼层为1楼 联系人不变
# 招商银行股份有限公司深圳南山分行 楼层为2楼 联系人不变
# 这三个公司的处理方式，其余若有重复，删除即可
#
# update eh_groups set name='招商银行股份有限公司深圳南山支行179921' where id=179921;
# update eh_groups set name='深圳市中融科互联网金融服务有限公司179913' where id=179913;
# update eh_groups set name='深圳市启银投资服务有限公司179057' where id=179057;
# update eh_groups set name='腾讯（深圳）有限公司179038' where id=179038;

##################################################################################
# 参数根据实际情况而定
#

set @organization_details_id = 10001;
set @organization_member_id = 2000000;
set @organization_addresses_id = 10001;
set @organization_attachments_id = 10001;
set @organization_community_requests_id = 10001;
set @depatement_id_add = 300000;

set @techpark_community_id = 240111044331048623;
set @xunmei_community_id = 240111044331049963;
set @organization_role_map_id = 10001;

#
# merge enterprise and organization data
#

INSERT INTO `eh_organizations` (`id`,`parent_id`,`organization_type`,`name`,`path`,`level`,`status`,`group_type`,`create_time`,`update_time`,`directly_enterprise_id`,`namespace_id`,`group_id`) 
select `id`,0,'ENTERPRISE',`name`,concat('/',`id`),1,if(`status` = 0,1,2),'ENTERPRISE',`create_time` ,`update_time`,0,`namespace_id`,`id` FROM `eh_groups` WHERE `discriminator` = 'enterprise';


INSERT INTO `eh_organization_details`(`id`,`organization_id`,`description`,`contact`,`address`,`create_time`,`display_name`,`member_count`,`checkin_date`,`avatar`,`post_uri`)
SELECT  (@organization_details_id := @organization_details_id + 1),`id`,`description`,`string_tag1`,`string_tag2`,`create_time`,IF(IFNULL(`display_name`,`name`) = '',name,IFNULL(`display_name`,`name`)),`member_count`,`string_tag3`,`avatar`,`string_tag5` FROM `eh_groups` WHERE `discriminator` = 'enterprise';


#
# update organization data
#

UPDATE `eh_organizations` SET path = CONCAT('/',id),`update_time`= now(), group_type='ENTERPRISE' WHERE path = CONCAT('/',name);

UPDATE `eh_organizations` SET path = CONCAT('/',parent_id,'/',id),directly_enterprise_id = parent_id,`update_time`= now(),group_type='DEPARTMENT' WHERE path = CONCAT('/',parent_id,'/',name);

UPDATE `eh_organizations` SET directly_enterprise_id = 1000001 WHERE path LIKE CONCAT('/',1000001,'/%');

UPDATE `eh_organizations` SET path = (SELECT b.path1 FROM (SELECT id,a.path,CONCAT('/',a.directly_enterprise_id,'/',(SELECT id FROM `eh_organizations` WHERE name = substring_index(path1,'/',1) AND parent_id = a.directly_enterprise_id limit 1),'/',a.id) path1 FROM (SELECT id,parent_id,directly_enterprise_id,path,substring_index(path, '/', -2) path1 FROM `eh_organizations` WHERE substring_index(path, '/', -1) IN (SELECT name FROM `eh_organizations`)) a
) b WHERE b.id= id limit 1),`update_time`= now(),group_type='DEPARTMENT' WHERE id IN (SELECT id FROM (SELECT id,parent_id,directly_enterprise_id,path,substring_index(path, '/', -2) path1 FROM `eh_organizations` WHERE substring_index(path, '/', -1) IN (SELECT name FROM `eh_organizations`)) a);

UPDATE `eh_organizations` SET group_type='ENTERPRISE' WHERE parent_id = 0;
UPDATE `eh_organizations` SET group_type='DEPARTMENT' WHERE group_type IS NULL;

UPDATE `eh_organizations` SET directly_enterprise_id = substring_index(substring_index(path,'/',2),'/',-1) WHERE group_type = 'DEPARTMENT';

#
# merge department and organization data
#
INSERT INTO `eh_organizations` (`id`,`parent_id`,`organization_type`,`name`,`path`,`level`,`status`,`group_type`,`create_time`,`directly_enterprise_id`) 
SELECT a.`id`,(SELECT @depatement_id_add + id FROM `eh_enterprise_contact_groups` WHERE name = a.parent_name AND enterprise_id = a.enterprise_id) parent_id,
'ENTERPRISE',a.`name`,concat(path,(SELECT @depatement_id_add + id FROM `eh_enterprise_contact_groups` WHERE name = a.parent_name AND enterprise_id = a.enterprise_id),'/',a.id),1,2,'DEPARTMENT',a.`create_time`,a.enterprise_id FROM 
(SELECT @depatement_id_add+`id` id,substring_index(string_tag1,'\\',1) parent_name,
`name`,concat('/',enterprise_id,'/') path,`create_time`,enterprise_id,string_tag1 FROM `eh_enterprise_contact_groups` 
WHERE id IN (SELECT id FROM `eh_enterprise_contact_groups` WHERE string_tag1 LIKE '%\\\\%')) a;

INSERT INTO `eh_organizations` (`id`,`parent_id`,`organization_type`,`name`,`path`,`level`,`status`,`group_type`,`create_time`,`directly_enterprise_id`) 
SELECT @depatement_id_add + `id`,enterprise_id,'ENTERPRISE',`name`,concat('/',enterprise_id,'/',@depatement_id_add + `id`),1,2,'DEPARTMENT',`create_time`,enterprise_id FROM `eh_enterprise_contact_groups` WHERE id NOT IN (SELECT id FROM `eh_enterprise_contact_groups` WHERE string_tag1 LIKE '%\\\\%');


#
# merge enterprise member and organization member data
#

UPDATE `eh_organization_members` SET  `status` = 3 WHERE `status` = 2;

SET foreign_key_checks = 0;
INSERT INTO `eh_organization_members` 
(`id`,`organization_id`,`contact_name`,`string_tag1`,`avatar`,`target_id`,`integral_tag1`,`status`,`integral_tag2`,`create_time`,`string_tag2`,`gender`,`integral_tag3`,`contact_token`,`target_type`,`contact_type`,`member_group`,`group_id`)
SELECT (@organization_member_id + c.`id`) id, c.`enterprise_id`,c.`name`,c.`nick_name`,c.`avatar`,c.`user_id`,c.`role`,c.`status`,c.`creator_uid`,c.`create_time`,c.`string_tag1`,IF(c.`string_tag2` = '男',1,IF(c.`string_tag2`='女',2,0)),c.`string_tag3`,e.`entry_value`,IF(c.`user_id` = 0,'UNTRACK', 'USER'),0,'manager',IFNULL((select @depatement_id_add + contact_group_id from `eh_enterprise_contact_group_members` where contact_id = c.id limit 1 ), 0) from `eh_enterprise_contacts` c left join `eh_enterprise_contact_entries` e on c.`id` = e.`contact_id` GROUP BY id; 

#
# move enterprise address information
#
INSERT INTO `eh_organization_addresses`
(`id`,`organization_id`,`address_id`,`status`,`creator_uid`,`create_time`,`operator_uid`,`process_code`,`process_details`,`proof_resource_uri`,`approve_time`,`update_time`,`building_id`,`building_name`)
SELECT  @organization_addresses_id + `id`, `enterprise_id`,`address_id`,`status`,`creator_uid`,`create_time`,`operator_uid`,`process_code`,`process_details`,`proof_resource_uri`,`approve_time`,`update_time`,`building_id`,`building_name` FROM `eh_enterprise_addresses` ;

#
# move enterprise attachment information
#
INSERT INTO `eh_organization_attachments`
(`id`,`organization_id`,`content_type`,`content_uri`,`creator_uid`,`create_time`)
SELECT @organization_attachments_id + `id`,`enterprise_id`,`content_type`,`content_uri`,`creator_uid`,`create_time`  FROM `eh_enterprise_attachments`;

#
# move enterprise community requests information
#
INSERT INTO `eh_organization_community_requests`
(`id`,`community_id`,`member_type`,`member_id`,`member_status`,`creator_uid`,`create_time`,`operator_uid`,`process_code`,`process_details`,`proof_resource_uri`,`approve_time`,`requestor_comment`,`operation_type`,`inviter_uid`,`invite_time`,`update_time`)
SELECT @organization_community_requests_id + `id`,`community_id`,'organization',`member_id`,`member_status`,`creator_uid`,`create_time`,`operator_uid`,`process_code`,`process_details`,`proof_resource_uri`,`approve_time`,`requestor_comment`,`operation_type`,`inviter_uid`,`invite_time`,`update_time`  FROM `eh_enterprise_community_map`;


update `eh_yellow_pages` set `owner_type` = 'community', `owner_id`= @techpark_community_id where `owner_id` = 1000000;
update `eh_yellow_pages` set `owner_type` = 'community', `owner_id`= @xunmei_community_id where `owner_id` = 999999;

INSERT INTO `eh_preferential_rules` (`id`,`owner_type`,`owner_id`,`start_time`,`end_time`,`type`,`before_nember`) VALUES (1,'EhCommunities',@techpark_community_id,NOW(),NOW(),'parking',0);
INSERT INTO `eh_preferential_rules` (`id`,`owner_type`,`owner_id`,`start_time`,`end_time`,`type`,`before_nember`) VALUES (2,'EhCommunities',@xunmei_community_id,NOW(),NOW(),'parking',0);

INSERT INTO `eh_organization_role_map` (`id`,`owner_type`,`owner_id`,`role_id`,`private_flag`,`status`,`create_time`)
SELECT  (@organization_role_map_id := @organization_role_map_id + 1),'EhOrganizations',0,`id`,0,2,now() FROM `eh_acl_roles` WHERE `app_id` = 32;


