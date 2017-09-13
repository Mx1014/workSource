-- 蒙版提示信息修改 by lei.lv
UPDATE `eh_namespace_masks` SET `id`='1', `namespace_id`='999971', `item_name`='人才公寓', `image_type`='3', `tips`='快速切换至人才公寓首页', `scene_type`='park_tourist' WHERE (`id`='1');
UPDATE `eh_namespace_masks` SET `id`='2', `namespace_id`='999971', `item_name`='园区服务', `image_type`='3', `tips`='快速切换至园区首页', `scene_type`='default' WHERE (`id`='2');


-- 把有管理员权限的用户，在eh_organization_members表里面标识成 manager add by sfyan 20170911
update `eh_organization_members` eom set `member_group` = '';
update `eh_organization_members` eom set `member_group` = ifnull((select 'manager' from eh_acls where `owner_type` = 'EhOrganizations' and `owner_id` = eom.organization_id and role_type = 'EhUsers' and role_id = eom.target_id and privilege_id in (10,15)  limit 1), '') where group_type = 'ENTERPRISE' and target_type = 'USER' and status = 3;
