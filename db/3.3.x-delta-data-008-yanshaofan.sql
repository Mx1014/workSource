


#
# 查询用户拥有2个同样的角色数据
#
select `target_id`,`role_id`, count(*) from `eh_acl_role_assignments` group by `target_id`, `role_id` having count(*) >1;

#
#删除用户拥有2个同样角色数据的其中一条，最好是id小的
#
#select * from `eh_acl_role_assignments` where `target_id` = 195870 and `role_id` = 1005;
#select * from `eh_acl_role_assignments` where `target_id` = 196846 and `role_id` = 1005;
#select * from `eh_acl_role_assignments` where `target_id` = 198773 and `role_id` = 1005;
#select * from `eh_acl_role_assignments` where `target_id` = 198943 and `role_id` = 1005;
#select * from `eh_acl_role_assignments` where `target_id` = 199030 and `role_id` = 1005;
#delete from `eh_acl_role_assignments`  where id = 10030;




#
# 查询用户在多个机构的数据
#
select `target_id`, count(*) from `eh_organization_members` where `status`= 3 and `target_id` != 0 group by `target_id` having count(*) >1;

#
#处理对象是用户的权限数据
#
UPDATE `eh_acl_role_assignments` eara SET `owner_type` = 'EhOrganizations', `owner_id` = (select organization_id from `eh_organization_members` where `target_id`=eara.target_id and `status`= 3) 
WHERE `target_type` = 'EhUsers' AND `role_id` > 1000 AND `role_id` < 2000 
#
#过滤掉用户在多个机构的数据，不做处理
#AND  `target_id` NOT IN (10713,196846,198773,198943,199030);
#
#对多个用户在多个机构的数据批量再做处理
#
#先把当前数据处理成用户第一个机构的角色
#
UPDATE `eh_acl_role_assignments` eara SET `owner_type` = 'EhOrganizations', `owner_id` = (select organization_id from `eh_organization_members` where `target_id`=eara.target_id and `status`= 3 limit 0,1) 
WHERE `target_type` = 'EhUsers' AND `role_id` > 1000 AND `role_id` < 2000 AND  `target_id` IN (10713,196846,198773,198943,199030);

#
#复制添加成用户第二个机构的角色
#
#查询最大id
select max(id) from  `eh_organization_members` ;
#根据最大id设置值
set @assi_id = 2101139;
INSERT INTO `eh_acl_role_assignments`(`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`role_id`,`creator_uid`,`create_time`) 
SELECT (@assi_id := @assi_id + 1),`owner_type`,(select organization_id from `eh_organization_members` where `target_id`=eara.target_id and `status`= 3 limit 1,1),`target_type`,`target_id`,`role_id`,`creator_uid`,`create_time` FROM `eh_acl_role_assignments` eara  WHERE `target_type` = 'EhUsers' AND `role_id` > 1000 AND `role_id` < 2000 AND  `target_id` IN (10713,196846,198773,198943,199030);

#
#如果用户有第三个机构 类似第二种方式复制添加，limit 2,1 就可以了
#




#
#处理对象是机构的权限数据
#
UPDATE `eh_acl_role_assignments` eara SET `owner_type` = 'EhOrganizations', `owner_id` = (select directly_enterprise_id from `eh_organizations` where `id`=eara.target_id and `status`= 2)
WHERE `target_type` = 'EhOrganizations' AND `role_id` > 1000 AND `role_id` < 2000; 

