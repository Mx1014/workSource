

#
# data adjustment script
#

UPDATE `eh_groups` SET name = CONCAT(name,'(1)') WHERE id = 178346;
UPDATE `eh_organizations` SET name = CONCAT(name,'(1)') WHERE id = 178024;


INSERT INTO `eh_organization_details`(`id`,`organization_id`,`description`,`contact`,`address`,`create_time`,`display_name`,`member_count`,`checkin_date`,`avatar`,`post_uri`)
SELECT  (@organization_details_id := @organization_details_id + 1),`id`,`description`,`string_tag1`,`string_tag2`,`create_time`,IF(IFNULL(`display_name`,`name`) = '',name,IFNULL(`display_name`,`name`)),`member_count`,`string_tag3`,`avatar`,`string_tag5` FROM `eh_groups` WHERE
id IN (SELECT `member_id` FROM `eh_organization_community_requests` WHERE `member_id` NOT IN(SELECT id FROM `eh_organizations` ) GROUP BY member_id);


INSERT INTO `eh_organizations` (`id`,`parent_id`,`organization_type`,`name`,`path`,`level`,`status`,`group_type`,`create_time`,`update_time`,`directly_enterprise_id`,`namespace_id`,`group_id`) 
SELECT `id`,0,'ENTERPRISE',`name`,concat('/',`id`),1,if(`status` = 0,1,2),'ENTERPRISE',`create_time` ,`update_time`,0,`namespace_id`,`id` FROM `eh_groups` WHERE 
 id IN (SELECT `member_id` FROM `eh_organization_community_requests` WHERE `member_id` NOT IN(SELECT id FROM `eh_organizations` ) GROUP BY member_id);

 
 
DELETE FROM `eh_organization_community_requests` WHERE `member_id` IN (178766,178776);

INSERT INTO `eh_organization_details`(`id`,`organization_id`)values(1000001,1000001);


#
# 科技园
#
UPDATE `eh_organizations` SET name = CONCAT(name,'(1)') WHERE id = 178395;
UPDATE `eh_organizations` SET name = '深圳科技工业园（集团）有限公司' WHERE id = 1000001;

DELETE FROM `eh_organization_members` WHERE `organization_id` = 1000001;
DELETE FROM `eh_organizations` WHERE path LIKE '/1000001/%';
UPDATE `eh_organization_members` SET `organization_id` = 1000001 WHERE `organization_id` = 178395;
UPDATE `eh_organizations` SET `parent_id` = replace(parent_id,178395,1000001), path = replace(path,178395,1000001) WHERE path LIKE '/178395/%';


#
# 讯美
#
UPDATE `eh_organizations` SET name = CONCAT(name,'(1)') WHERE id = 178689;
UPDATE `eh_organizations` SET name = '讯美科技' WHERE id = 1000100;

DELETE FROM `eh_organization_members` WHERE `organization_id` = 1000100;
DELETE FROM `eh_organizations` WHERE path LIKE '/1000100/%';
UPDATE `eh_organization_members` SET `organization_id` = 1000100 WHERE `organization_id` = 178689;
UPDATE `eh_organizations` SET `parent_id` = replace(parent_id,178689,1000100), path = replace(path,178689,1000100) WHERE path LIKE '/178689/%';


#
# 海岸城
#
UPDATE `eh_organizations` SET name = CONCAT(name,'(1)') WHERE id = 180000;
UPDATE `eh_organizations` SET name = '深圳市海岸物业管理有限公司' WHERE id = 1000631;

DELETE FROM `eh_organization_members` WHERE `organization_id` = 1000631;
DELETE FROM `eh_organizations` WHERE path LIKE '/1000631/%';
UPDATE `eh_organization_members` SET `organization_id` = 1000631 WHERE `organization_id` = 180000;
UPDATE `eh_organizations` SET `parent_id` = replace(parent_id,180000,1000631), path = replace(path,180000,1000631) WHERE path LIKE '/180000/%';

#
# 补充楼栋名称
#
update `eh_organization_addresses` eoa set `building_name` = (select `building_name` from `eh_addresses` where `id` = eoa.`address_id` limit 1);






UPDATE `eh_organizations` SET namespace_id = 1000000 WHERE id = 1000001;
UPDATE `eh_organizations` SET namespace_id = 999999 WHERE id = 1000100;
UPDATE `eh_organizations` SET namespace_id = 999994 WHERE id = 1000531;
UPDATE `eh_organizations` SET namespace_id = 999993 WHERE id = 1000631;



#
#add enterprise admin privilege
#
set @assignment_id = 10000;
INSERT INTO `eh_acl_role_assignments`(`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`role_id`,`creator_uid`,`create_time`)
select (@assignment_id := @assignment_id + 1),'system',null,'EhUsers',user_id,1005,1025,now() from `eh_enterprise_contacts` where `role` = 5 and `status` = 3;




update `eh_buildings` set namespace_id = 1000000 where id in (176121,176123,176124);

update `eh_yellow_pages` set id = 10000000 where name = '深圳仲裁委员会科技园工作站';
update `eh_yellow_pages` set id = 10005 where id = 5;
update `eh_yellow_pages` set id = 5 where id = 10000000;

