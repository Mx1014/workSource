-- 导出无数据提示，add by tt, 20170522
select max(id) into @id from `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id+1, 'organization', '800000', 'zh_CN', '无数据！');

-- 初始化organization表中是否设置了企业管理员标记，add by tt, 20170522
update eh_organizations t1
set t1.set_admin_flag = 1
where t1.`status` = 2
and t1.parent_id = 0
and t1.group_type = 'ENTERPRISE'
and exists (
	select 1 
	from eh_acl_role_assignments t2
	where t2.owner_type = 'EhOrganizations'
	and t2.owner_id = t1.id
	and t2.target_type = 'EhUsers'
	and t2.role_id = 1005
);