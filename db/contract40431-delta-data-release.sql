
SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'contract', '10015', 'zh_CN', '该房源不是待租状态');

-- 合同复制权限
SET @id = (SELECT MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 21210, 0, 21224, '复制', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (21224, 0, '合同管理 合同复制', '合同管理 合同复制权限', NULL);

INSERT INTO `ehcore`.`eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES (21224, '21200', '21224', '复制');


-- 合同记录日志
SET @id = (SELECT MAX(id) from eh_locale_templates);
INSERT INTO eh_locale_templates (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '20', 'zh_CN', '合同复制事件', '合同复制，该合同复制于${contractName}', '0');
INSERT INTO eh_locale_templates (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '21', 'zh_CN', '合同初始化事件', '该合同进行了初始化', '0');
INSERT INTO eh_locale_templates (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '22', 'zh_CN', '合同免批事件', '该合同已免批', '0');
