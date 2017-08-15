-- 初始化range字段 by st.zheng
update `eh_service_alliances` set `range`=owner_id where owner_type='community';

update `eh_service_alliances` set `range`='all' where owner_type='orgnization';

-- 将原来的审批置为删除 by.st.zheng
update `eh_service_alliance_jump_module` set `signal`=0 where module_name = '审批';

set @eh_service_alliance_jump_id = (select max(id) from eh_service_alliance_jump_module);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `module_name`,`signal`, `module_url`, `parent_id`)
VALUES (@eh_service_alliance_jump_id + 1, '审批', 2,'zl://approval/create?approvalId={}&sourceId={}', '0');

-- 更改菜单 by.st.zheng
UPDATE `eh_web_menus` SET `data_type`='react:/approval-management/approval-list/40500/EhOrganizations' WHERE `id`='40541' and name='审批列表';

--
-- 用户输入内容变量 add by xq.tian  2017/08/05
--
SELECT MAX(id) FROM `eh_flow_variables` INTO @flow_variables_id;
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ((@flow_variables_id := @flow_variables_id + 1), 0, 0, '', 0, '', 'text_button_msg_input_content', '文本备注内容', 'text_button_msg', 'bean_id', 'flow-variable-text-button-msg-user-input-content', 1);
