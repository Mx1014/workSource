SET @id := (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'address', '20005', 'zh_CN', '门牌已关联合同，无法删除');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'building', '10003', 'zh_CN', '楼栋下有门牌已关联合同，无法删除');

SET @config_id = (SELECT MAX(id) from `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'ebei.url', 'http://183.62.222.87:5902/sf', '一碑url', '0', NULL);

SET @field_id = (SELECT MAX(id) from `eh_var_fields`);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) 
VALUES (@field_id:=@field_id+1, 'contract', 'denunciationTime', '退约日期', 'Long', '15', '/13/15', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) 
VALUES (@field_id:=@field_id+1, 'contract', 'denunciationUid', '退约经办人', 'Long', '15', '/13/15', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
