-- 设置eh_locale_templates表的主键id
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_templates`),0);

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (`id`, 'contract.tracking', '1', 'zh_CN', '合同事件', '创建合同', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (`id`, 'contract.tracking', '2', 'zh_CN', '合同事件', '删除合同', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (`id`, 'contract.tracking', '3', 'zh_CN', '合同事件', '修改${display}:由${oldData}更改为${newData}', '0');
