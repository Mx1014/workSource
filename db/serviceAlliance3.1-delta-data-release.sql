-- 删除审批这种服务联盟跳转模块 by dengs,2018/1/12
select * from eh_service_alliance_jump_module WHERE module_url like '%zl://approval/create%' and module_name='审批';
DELETE FROM eh_service_alliance_jump_module WHERE module_url like '%zl://approval/create%' and module_name='审批';
