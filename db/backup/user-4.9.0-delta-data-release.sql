-- 把有管理员权限的用户，在eh_organization_members表里面标识成 manager add by sfyan 20170911
update `eh_organization_members` eom set `member_group` = '';
update `eh_organization_members` eom set `member_group` = ifnull((select 'manager' from eh_acls where `owner_type` = 'EhOrganizations' and `owner_id` = eom.organization_id and role_type = 'EhUsers' and role_id = eom.target_id and privilege_id in (10,15)  limit 1), '') where group_type = 'ENTERPRISE' and target_type = 'USER' and status = 3;
