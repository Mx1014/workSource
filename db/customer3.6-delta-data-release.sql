-- 通用脚本
-- 修改认知途径  by jiarui 20180622
UPDATE  eh_var_fields set display_name ='客户来源' WHERE  name = 'sourceItemId';

SET  @var_id = (SELECT  max(id) from eh_var_fields);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@var_id:=@var_id+1), 'enterprise_customer', 'talentSourceItemName', '信息来源', 'Long', '4', '/4/', '0', NULL, '2', '1', now(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

-- 通用脚本
-- 线索客户转意向时间
-- add by jiarui 20180709

SET @id = (SELECT max(id) from eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@id:=@id+1), 'customer.tracking', '60', 'zh_CN', '客户事件', '${operatorName}在${time}将线索客户${customerName}转成意向客户', '0');

