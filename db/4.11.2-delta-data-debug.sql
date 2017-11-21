-- added by R for approval-1.6 start.
SET @scope_id = (SELECT MAX(id) from `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id +1, 52000, '', 'EhNamespaces', 999975, 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id +1, 52010, '审批记录', 'EhNamespaces', 999975, 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id +1, 52020, '规则设置', 'EhNamespaces', 999975, 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id +1, 52030, '表单管理', 'EhNamespaces', 999975, 2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id +1, 52000, '', 'EhNamespaces', 1, 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id +1, 52010, '审批记录', 'EhNamespaces', 1, 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id +1, 52020, '规则设置', 'EhNamespaces', 1, 2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id +1, 52030, '表单管理', 'EhNamespaces', 1, 2);
-- added by R end.