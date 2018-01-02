-- 工作汇报1.0 add by nan.rong
SET @scope_id = (SELECT MAX(id) FROM eh_web_menu_scopes);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id + 1, '54000', '', 'EhNamespaces', '1', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id + 1, '54010', '', 'EhNamespaces', '1', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@scope_id := @scope_id + 1, '54020', '', 'EhNamespaces', '1', '2');

-- end by nan.rong