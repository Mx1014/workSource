--  客户事件假如动态表单
--  by jiarui 20180626
SET  @groupId = (SELECT  max(id) FROM  eh_var_field_groups);
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (( @groupId:= @groupId+1), 'enterprise_customer', '0', concat('/',@groupId), '客户事件', '', '0', NULL, '2', '1', NOW(), NULL, NULL);

SET  @fieldId = (SELECT  max(id) FROM  eh_var_fields);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@fieldId:=@fieldId+1), 'enterprise_customer', 'creatorName', '操作人', 'String', @groupId, concat('/',@groupId,'/'), '1', NULL, '2', '1', now(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@fieldId:=@fieldId+1), 'enterprise_customer', 'operateTime', '操作时间', 'String', @groupId, concat('/',@groupId,'/'), '1', NULL, '2', '1', now(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@fieldId:=@fieldId+1), 'enterprise_customer', 'sourceType', '事件来源', 'String', @groupId, concat('/',@groupId,'/'), '1', NULL, '2', '1', now(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@fieldId:=@fieldId+1), 'enterprise_customer', 'content', '描述', 'String', @groupId, concat('/',@groupId,'/'), '1', NULL, '2', '1', now(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


