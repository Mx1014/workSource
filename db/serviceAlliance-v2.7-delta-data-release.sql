-- 初始化range字段 by st.zheng
update `eh_service_alliances` set `range`=owner_id where owner_type='community';

update `eh_service_alliances` set `range`='all' where owner_type='orgnization';

-- 将原来的审批置为删除 by.st.zheng
update `eh_service_alliance_jump_module` set `signal`=0 where moudle_name = '审批';

set @eh_service_alliance_jump_id = (select max(id) from eh_service_alliance_jump_module);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `module_name`,`signal`, `module_url`, `parent_id`)
VALUES (@eh_service_alliance_jump_id + 1, '审批', 2,'zl://approval/create?approvalId={}&sourceId={}', '0');
