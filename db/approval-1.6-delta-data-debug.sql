-- added by R for approval-1.6 start.
SET @scope_id = (SELECT MAX(id) from `eh_web_menu_scopes`)
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id +1, 52000, '', 'EhNamespaces', 999975, 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id +1, 52010, '审批记录', 'EhNamespaces', 999975, 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id +1, 52020, '规则设置', 'EhNamespaces', 999975, 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id +1, 52030, '表单管理', 'EhNamespaces', 999975, 2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id +1, 52000, '', 'EhNamespaces', 1, 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id +1, 52010, '审批记录', 'EhNamespaces', 1, 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id +1, 52020, '规则设置', 'EhNamespaces', 1, 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id +1, 52030, '表单管理', 'EhNamespaces', 1, 2);
-- added by R end.

SET @category_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_approval_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `approval_type`, `category_name`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@category_id := @category_id + 1, '0', NULL, '0', '1', '调休', '2', '212500', '2017-11-03 09:00:05', '2017-11-03 09:00:05', '212500');
INSERT INTO `eh_approval_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `approval_type`, `category_name`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@category_id := @category_id + 1, '0', NULL, '0', '1', '婚假', '2', '212500', '2017-11-03 09:00:06', '2017-11-03 09:00:06', '212500');
INSERT INTO `eh_approval_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `approval_type`, `category_name`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@category_id := @category_id + 1, '0', NULL, '0', '1', '产假', '2', '212500', '2017-11-03 09:00:07', '2017-11-03 09:00:07', '212500');
INSERT INTO `eh_approval_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `approval_type`, `category_name`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@category_id := @category_id + 1, '0', NULL, '0', '1', '陪产假', '2', '212500', '2017-11-03 09:00:08', '2017-11-03 09:00:08', '212500');
INSERT INTO `eh_approval_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `approval_type`, `category_name`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@category_id := @category_id + 1, '0', NULL, '0', '1', '丧假', '2', '212500', '2017-11-03 09:00:09', '2017-11-03 09:00:09', '212500');
INSERT INTO `eh_approval_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `approval_type`, `category_name`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@category_id := @category_id + 1, '0', NULL, '0', '1', '工伤假', '2', '212500', '2017-11-03 09:00:10', '2017-11-03 09:00:10', '212500');
INSERT INTO `eh_approval_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `approval_type`, `category_name`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (@category_id := @category_id + 1, '0', NULL, '0', '1', '路途假', '2', '212500', '2017-11-03 09:00:11', '2017-11-03 09:00:11', '212500');
