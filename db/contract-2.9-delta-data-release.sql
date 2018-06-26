-- 设置eh_locale_templates表的主键id
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_templates`),0);

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '1', 'zh_CN', '合同事件', '创建合同', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '2', 'zh_CN', '合同事件', '删除合同', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '3', 'zh_CN', '合同事件', '修改${display}:由${oldData}更改为${newData}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '4', 'zh_CN', '合同资产事件', '新增合同资产${apartmentName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '5', 'zh_CN', '合同资产事件', '删除合同资产${apartmentName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '6', 'zh_CN', '合同计价条款事件', '新增计价条款:${chargingItemName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '7', 'zh_CN', '合同计价条款事件', '删除计价条款:${chargingItemName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '8', 'zh_CN', '合同计价条款事件', '修改计价条款:${chargingItemName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '9', 'zh_CN', '合同附件事件', '新增附件:${attachmentName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '10', 'zh_CN', '合同附件事件', '删除附件:${attachmentName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '11', 'zh_CN', '调租计划事件', '新增调租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '12', 'zh_CN', '调租计划事件', '删除调租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '13', 'zh_CN', '调租计划事件', '修改调租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '14', 'zh_CN', '免租计划事件', '新增免租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '15', 'zh_CN', '免租计划事件', '删除免租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '16', 'zh_CN', '免租计划事件', '修改免租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '17', 'zh_CN', '合同续约事件', '产生续约合同，子合同名称为:${contractName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '18', 'zh_CN', '合同变更事件', '产生变更合同，子合同名称为:${contractName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '19', 'zh_CN', '资产更改事件', '修改关联资产，由${oldApartmnets}变为${newApartmnets}', '0');