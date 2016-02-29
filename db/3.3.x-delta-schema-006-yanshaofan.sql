
#
# data adjustment script
#

UPDATE `eh_groups` SET name = CONCAT(name,'(1)') WHERE id = 178346;
UPDATE `eh_organizations` SET name = CONCAT(name,'(1)') WHERE id = 178024;

INSERT INTO `eh_organizations` (`id`,`parent_id`,`organization_type`,`name`,`path`,`level`,`status`,`group_type`,`create_time`,`update_time`,`directly_enterprise_id`,`namespace_id`,`group_id`) 
SELECT `id`,0,'ENTERPRISE',`name`,concat('/',`id`),1,if(`status` = 0,1,2),'ENTERPRISE',`create_time` ,`update_time`,0,`namespace_id`,`id` FROM `eh_groups` WHERE 
 id IN (SELECT `member_id` FROM `eh_organization_community_requests` WHERE `member_id` NOT IN(SELECT id FROM `eh_organizations` ) GROUP BY member_id);

DELETE FROM `eh_organization_community_requests` WHERE `member_id` IN (178766,178776);

INSERT INTO `eh_organization_details`(`id`,`organization_id`)values(1000001,1000001);