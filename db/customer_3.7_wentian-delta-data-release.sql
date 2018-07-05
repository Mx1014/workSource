INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (37, 'enterprise_customer', '0', '/37', '物业报修', '', '0', NULL, '2', '1', NOW(), NULL, NULL);

SET @field_id = (SELECT MAX(id) FROM `eh_var_fields`);

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES
  (@field_id := @field_id + 1, 'enterprise_customer', 'taskCategoryName', '服务类型', 'String', '37', '/37/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES
  (@field_id := @field_id + 1, 'enterprise_customer', 'organizationUid', '来源', 'Long', '37', '/37/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES
  (@field_id := @field_id + 1, 'enterprise_customer', 'buildingName', '服务区域', 'String', '37', '/37/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES
  (@field_id := @field_id + 1, 'enterprise_customer', 'requestorName', '联系人', 'String', '37', '/37/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES
  (@field_id := @field_id + 1, 'enterprise_customer', 'requestorPhone', '联系电话', 'String', '37', '/37/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES
  (@field_id := @field_id + 1, 'enterprise_customer', 'createTime', '发起时间', 'datetime', '37', '/37/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetime\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES
  (@field_id := @field_id + 1, 'enterprise_customer', 'status', '状态', 'datetime', '37', '/37/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES
  (@field_id := @field_id + 1, 'enterprise_customer', 'content', '服务内容', 'datetime', '37', '/37/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');



-- app模板
SET @id = ifnull((SELECT max(`id`) from `eh_locale_templates`),0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'customer.notification', 1, 'zh_CN','msg_to_new_tracking', '管理员已将${customerName} 交由您来跟进，请尽快跟进', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'customer.notification', 2, 'zh_CN','msg_to_old_tracking', '管理员已将${customerName}交由${currentTrackingName} 跟进，请悉知', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'customer.notification', 3, 'zh_CN','msg_to_admin_no_candidate', '${originalTrackingName}已放弃跟进${customerName}，请尽快处理', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'customer.notification', 4, 'zh_CN','msg_to_tracking_on_customer_delete', '${customerName}已被删除，请悉知', 0);