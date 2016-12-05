-- ALL 全部菜单的范围-----start 不要执行
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10100', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10200', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10400', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10600', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10700', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10750', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10751', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10752', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10800', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10850', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10851', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10852', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10900', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '11000', '', 'EhNamespaces', '999987', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20100', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20110', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20120', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20130', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20140', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20150', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20155', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20160', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20170', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20180', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20190', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20191', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20192', '', 'EhNamespaces', '999987', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20400', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20410', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20420', '', 'EhNamespaces', '999987', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20600', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20610', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20620', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20630', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20640', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20650', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20660', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20661', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20662', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20670', '', 'EhNamespaces', '999987', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20800', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20810', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20811', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20812', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20820', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20821', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20822', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20830', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20831', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20840', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20841', '', 'EhNamespaces', '999987', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30500', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '31000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '32000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '33000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '34000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '35000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '37000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '38000', '', 'EhNamespaces', '999987', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40100', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40110', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40120', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40200', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40210', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40220', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40300', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40400', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40410', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40420', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40430', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40440', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40500', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40510', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40520', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40530', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40540', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40600', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40700', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40750', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40800', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40810', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40830', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40840', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40900', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41010', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41020', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41030', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41040', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41050', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41060', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41100', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41200', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41210', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41220', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41230', '', 'EhNamespaces', '999987', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50100', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50110', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50200', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50210', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50220', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50300', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50400', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50500', '', 'EhNamespaces', '999987', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50600', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50630', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50631', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50632', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50633', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50640', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50650', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50651', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50652', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50653', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50660', '', 'EhNamespaces', '999987', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50700', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50710', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50720', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50730', '', 'EhNamespaces', '999987', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50800', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50810', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50820', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50830', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50840', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50850', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50860', '', 'EhNamespaces', '999987', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60100', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60200', '', 'EhNamespaces', '999987', '2');
--  全部菜单的范围-----end 不要执行

	
-- 科技园
delete from eh_web_menu_scopes;
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (1, '10000', '', 'EhNamespaces', '1000000', '2');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10100', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10400', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10600', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '11000', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20000', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20100', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20140', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20150', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20155', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20160', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20170', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20180', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20190', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20191', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20192', '', 'EhNamespaces', '1000000', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30000', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '31000', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '32000', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '33000', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '34000', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40000', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40100', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40110', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40120', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40300', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40400', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40410', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40420', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40430', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40440', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40500', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40510', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40520', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40530', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40540', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40600', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40700', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40750', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40800', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40810', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40830', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40840', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50000', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50100', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50110', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50200', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50210', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50220', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50300', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50400', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50500', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50600', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50630', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50631', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50632', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50633', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50640', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50650', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50651', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50652', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50653', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50660', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50700', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50710', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50720', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50730', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60000', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60100', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60200', '', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10750', '俱乐部', 'EhNamespaces', '1000000', '1');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10751', '俱乐部管理', 'EhNamespaces', '1000000', '1');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10752', '审核俱乐部', 'EhNamespaces', '1000000', '1');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '32500', '合同管理', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '12200', '短信推送', 'EhNamespaces', '1000000', '2');

-- 迅美
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10000', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10100', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10200', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10400', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10600', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '11000', '', 'EhNamespaces', '999999', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20000', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20100', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20140', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20150', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20155', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20160', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20170', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20180', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20190', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20191', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20192', '', 'EhNamespaces', '999999', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30000', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '31000', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '32000', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '33000', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '34000', '', 'EhNamespaces', '999999', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40000', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40200', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40210', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40220', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40300', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40400', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40410', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40420', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40430', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40440', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40500', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40510', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40520', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40530', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40540', '', 'EhNamespaces', '999999', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50000', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50100', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50110', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50200', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50210', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50220', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50300', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50400', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50500', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50700', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50710', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50720', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50730', '', 'EhNamespaces', '999999', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60000', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60100', '', 'EhNamespaces', '999999', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60200', '', 'EhNamespaces', '999999', '2');

-- 海岸
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10000', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10100', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10400', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10600', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '11000', '', 'EhNamespaces', '999993', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20000', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20100', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20140', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20150', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20155', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20160', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20170', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20180', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20190', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20191', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20192', '', 'EhNamespaces', '999993', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20400', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20410', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20420', '', 'EhNamespaces', '999993', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30000', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30500', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '31000', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '32000', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '33000', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '34000', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '35000', '', 'EhNamespaces', '999993', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40000', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40100', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40110', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40120', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40300', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40400', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40410', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40420', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40430', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40440', '', 'EhNamespaces', '999993', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50000', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50100', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50110', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50200', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50210', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50220', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50300', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50400', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50500', '', 'EhNamespaces', '999993', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50700', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50710', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50720', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50730', '', 'EhNamespaces', '999993', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60000', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60100', '', 'EhNamespaces', '999993', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60200', '', 'EhNamespaces', '999993', '2');

-- 深业
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10000', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10100', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10400', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10600', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '11000', '', 'EhNamespaces', '999992', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20000', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20100', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20140', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20150', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20155', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20160', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20170', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20180', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20190', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20191', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20192', '', 'EhNamespaces', '999992', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20400', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20410', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20420', '', 'EhNamespaces', '999992', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20600', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20610', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20620', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20630', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20640', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20650', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20660', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20661', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20662', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20670', '', 'EhNamespaces', '999992', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20800', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20810', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20811', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20812', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20820', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20821', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20822', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20830', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20831', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20840', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20841', '', 'EhNamespaces', '999992', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30000', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30500', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '31000', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '32000', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '34000', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '35000', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '37000', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '38000', '', 'EhNamespaces', '999992', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40000', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40300', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40400', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40410', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40420', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40430', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40440', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40800', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40810', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40830', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40840', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40900', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41000', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41010', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41020', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41030', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41040', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41050', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41060', '', 'EhNamespaces', '999992', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 49100, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 49110, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 49120, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 49130, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 49140, '', 'EhNamespaces', 999992, 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50000', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50100', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50110', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50200', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50210', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50220', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50300', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50400', '', 'EhNamespaces', '999992', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50600', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50630', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50631', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50632', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50633', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50640', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50650', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50651', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50652', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50653', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50660', '', 'EhNamespaces', '999992', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50700', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50710', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50720', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50730', '', 'EhNamespaces', '999992', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50800', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50810', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50820', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50830', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50840', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50850', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50860', '', 'EhNamespaces', '999992', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60000', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60100', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60200', '', 'EhNamespaces', '999992', '2');

-- 储能
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10000', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10100', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10400', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10600', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '11000', '', 'EhNamespaces', '999990', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20000', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20100', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20110', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20120', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20130', '', 'EhNamespaces', '999990', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30000', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30500', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '31000', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '32000', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '33000', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '34000', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '35000', '', 'EhNamespaces', '999990', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40000', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40500', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40510', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40520', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40530', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40540', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40800', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40810', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40830', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40840', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41000', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41010', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41020', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41030', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41040', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41050', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41060', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41100', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41200', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41210', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41220', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41230', '', 'EhNamespaces', '999990', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50000', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50100', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50110', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50200', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50210', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50220', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50300', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50400', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50500', '', 'EhNamespaces', '999990', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50700', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50710', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50720', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50730', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60000', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60100', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60200', '', 'EhNamespaces', '999990', '2');

-- ibase
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10000', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10100', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10400', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10600', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '11000', '', 'EhNamespaces', '999989', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20000', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20100', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20110', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20120', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20130', '', 'EhNamespaces', '999989', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30000', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30500', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '31000', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '32000', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '33000', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '34000', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '35000', '', 'EhNamespaces', '999989', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40000', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40200', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40210', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40220', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40300', '', 'EhNamespaces', '999989', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40400', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40410', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40420', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40430', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40440', '', 'EhNamespaces', '999989', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41000', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41010', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41020', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41030', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41040', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41050', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41060', '', 'EhNamespaces', '999989', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50000', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50100', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50110', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50200', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50210', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50220', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50300', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50400', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50500', '', 'EhNamespaces', '999989', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50600', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50630', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50631', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50632', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50633', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50640', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50650', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50651', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50652', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50653', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50660', '', 'EhNamespaces', '999989', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50700', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50710', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50720', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50730', '', 'EhNamespaces', '999989', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60000', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60100', '', 'EhNamespaces', '999989', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60200', '', 'EhNamespaces', '999989', '2');

-- 威新LINK+
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10000', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10100', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10400', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10600', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10850', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10851', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10852', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '11000', '', 'EhNamespaces', '999991', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20000', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20100', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20140', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20150', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20155', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20160', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20170', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20180', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20190', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20191', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20192', '', 'EhNamespaces', '999991', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30000', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '31000', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '32000', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '33000', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '34000', '', 'EhNamespaces', '999991', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40000', '', 'EhNamespaces', '999991', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40100', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40110', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40120', '', 'EhNamespaces', '999991', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40400', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40410', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40420', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40430', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40440', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40600', '', 'EhNamespaces', '999991', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50000', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50100', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50110', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50200', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50210', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50220', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50300', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50400', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50500', '', 'EhNamespaces', '999991', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60000', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60100', '', 'EhNamespaces', '999991', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60200', '', 'EhNamespaces', '999991', '2');

-- 爱特家迷你居
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10000', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10100', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10400', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10600', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '11000', '', 'EhNamespaces', '999988', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20000', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20100', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20140', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20150', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20155', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20160', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20170', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20180', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20190', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20191', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20192', '', 'EhNamespaces', '999988', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30000', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30500', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '31000', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '32000', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '33000', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '34000', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '35000', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '37000', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '38000', '', 'EhNamespaces', '999988', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40000', '', 'EhNamespaces', '999988', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40300', '', 'EhNamespaces', '999988', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40400', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40410', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40420', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40430', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40440', '', 'EhNamespaces', '999988', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40800', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40810', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40830', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40840', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41000', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41010', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41020', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41030', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41040', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41050', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41060', '', 'EhNamespaces', '999988', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50000', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50100', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50110', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50200', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50210', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50220', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50300', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50400', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50500', '', 'EhNamespaces', '999988', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50600', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50630', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50631', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50632', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50633', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50640', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50650', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50651', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50652', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50653', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50660', '', 'EhNamespaces', '999988', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50800', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50810', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50820', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50830', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50840', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50850', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50860', '', 'EhNamespaces', '999988', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60000', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60100', '', 'EhNamespaces', '999988', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60200', '', 'EhNamespaces', '999988', '2');

-- 深圳湾
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10100', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10400', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10600', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10700', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10800', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10900', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '11000', '', 'EhNamespaces', '999987', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20100', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20140', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20150', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20155', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20160', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20170', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20180', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20190', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20191', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20192', '', 'EhNamespaces', '999987', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30500', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '31000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '32000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '33000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '34000', '', 'EhNamespaces', '999987', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40300', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40500', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40510', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40520', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40530', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40540', '', 'EhNamespaces', '999987', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50100', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50110', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50200', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50210', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50220', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50300', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50400', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50500', '', 'EhNamespaces', '999987', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60000', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60100', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60200', '', 'EhNamespaces', '999987', '2');

-- 创源
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10000', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10100', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10400', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10600', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10800', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '11000', '', 'EhNamespaces', '999986', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20000', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20100', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20140', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20150', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20155', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20160', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20170', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20180', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20190', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20191', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20192', '', 'EhNamespaces', '999986', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30000', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30500', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '31000', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '32000', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '33000', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '34000', '', 'EhNamespaces', '999986', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40000', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40200', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40210', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40220', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40300', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40400', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40410', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40420', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40430', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40440', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40500', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40510', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40520', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40530', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40540', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40600', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40800', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40810', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40830', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40840', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41000', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41010', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41020', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41030', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41040', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41050', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41060', '', 'EhNamespaces', '999986', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50000', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50100', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50110', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50200', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50210', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50220', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50300', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50400', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50500', '', 'EhNamespaces', '999986', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50600', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50630', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50631', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50632', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50633', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50640', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50650', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50651', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50652', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50653', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50660', '', 'EhNamespaces', '999986', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50700', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50710', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50720', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50730', '', 'EhNamespaces', '999986', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50800', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50810', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50820', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50830', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50840', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50850', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50860', '', 'EhNamespaces', '999986', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60000', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60100', '', 'EhNamespaces', '999986', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60200', '', 'EhNamespaces', '999986', '2');

-- 华润置地OE
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10000', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10100', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10400', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10600', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES ((@menu_scope_id := @menu_scope_id + 1), '10610', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES ((@menu_scope_id := @menu_scope_id + 1), '10620', '', 'EhNamespaces', '999985', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '11000', '', 'EhNamespaces', '999985', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30000', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30500', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '31000', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '32000', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '33000', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '34000', '', 'EhNamespaces', '999985', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40000', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40300', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40400', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40410', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40420', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40430', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40440', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40500', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40510', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40520', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40530', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40540', '', 'EhNamespaces', '999985', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50000', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50100', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50110', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50200', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50210', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50220', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50300', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50400', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50500', '', 'EhNamespaces', '999985', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50600', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50630', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50631', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50632', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50633', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50640', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50650', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50651', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50652', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50653', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50660', '', 'EhNamespaces', '999985', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50700', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50710', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50720', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50730', '', 'EhNamespaces', '999985', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60000', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60100', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60200', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10750', '社团', 'EhNamespaces', '999985', '1');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10751', '社团管理', 'EhNamespaces', '999985', '1');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10752', '审核社团', 'EhNamespaces', '999985', '1');

-- 	清华信息港
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10000', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10100', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10400', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10600', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10800', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '11000', '', 'EhNamespaces', '999984', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20000', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20100', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20140', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20150', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20155', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20160', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20170', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20180', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20190', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20191', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20192', '', 'EhNamespaces', '999984', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30000', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '31000', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '32000', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '33000', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '34000', '', 'EhNamespaces', '999984', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40000', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40100', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40110', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40120', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40400', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40410', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40420', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40430', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40440', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40500', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40510', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40520', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40530', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40540', '', 'EhNamespaces', '999984', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41000', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41010', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41020', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41030', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41040', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41050', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '41060', '', 'EhNamespaces', '999984', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50000', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50100', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50110', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50200', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50210', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50220', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50300', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50400', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50500', '', 'EhNamespaces', '999984', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50600', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50630', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50631', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50632', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50633', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50640', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50650', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50651', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50652', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50653', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50660', '', 'EhNamespaces', '999984', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60000', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60100', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60200', '', 'EhNamespaces', '999984', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10750', '俱乐部', 'EhNamespaces', '999984', '1');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10751', '俱乐部管理', 'EhNamespaces', '999984', '1');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10752', '审核俱乐部', 'EhNamespaces', '999984', '1');

-- 	科兴科学园
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10000', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10100', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10400', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '10600', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '11000', '', 'EhNamespaces', '999983', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20000', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20100', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20140', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20150', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20155', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20160', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20170', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20180', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20190', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20191', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20192', '', 'EhNamespaces', '999983', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20400', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20410', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '20420', '', 'EhNamespaces', '999983', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30000', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '30500', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '31000', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '32000', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '33000', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '34000', '', 'EhNamespaces', '999983', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40000', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40100', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40110', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40120', '', 'EhNamespaces', '999983', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40200', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40210', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40220', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40300', '', 'EhNamespaces', '999983', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40400', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40410', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40420', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40430', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40440', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40500', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40510', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40520', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40530', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40540', '', 'EhNamespaces', '999983', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40800', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40810', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40830', '', 'EhNamespaces', '999987', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '40840', '', 'EhNamespaces', '999987', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50000', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50100', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50110', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50200', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50210', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50220', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50300', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50400', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '50500', '', 'EhNamespaces', '999983', '2');

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60000', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60100', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '60200', '', 'EhNamespaces', '999983', '2');

-- 东方建富
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 10000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 10100, '', 'EhOrganizations', 1004005 , 2);
	INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 10400, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 10600, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 11000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20110, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20120, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20130, '', 'EhOrganizations', 1004005 , 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 30000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 30500, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 31000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 32000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 33000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 34000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 35000, '', 'EhOrganizations', 1004005 , 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40400, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40410, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40420, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40430, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40440, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40300, '', 'EhOrganizations', 1004005 , 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50100, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50110, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50200, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50210, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50220, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50300, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50400, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50500, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50600, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50630, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50631, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50632, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50633, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50640, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50650, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50651, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50652, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50653, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50660, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50700, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50710, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50720, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50730, '', 'EhOrganizations', 1004005 , 2);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 10000, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 10100, '', 'EhOrganizations', 1002756 , 2);
	INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 10400, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 10600, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 11000, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20000, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20110, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20120, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20130, '', 'EhOrganizations', 1002756 , 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 30000, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 30500, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 31000, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 32000, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 33000, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 34000, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 35000, '', 'EhOrganizations', 1002756 , 2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40000, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40400, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40410, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40420, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40430, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40440, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40300, '', 'EhOrganizations', 1002756 , 2);
	
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50000, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50100, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50110, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50200, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50210, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50220, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50300, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50400, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50500, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50600, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50630, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50631, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50632, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50633, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50640, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50650, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50651, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50652, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50653, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50660, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50700, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50710, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50720, '', 'EhOrganizations', 1002756 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50730, '', 'EhOrganizations', 1002756 , 2);

-------------------------------------------- 业务模块范围
delete from eh_service_module_scopes;
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) 
	VALUES ('1', '999984', '10000', '', 'EhNamespaces', '999984', NULL, '2');
SET @eh_service_module_scopes = (SELECT MAX(id) FROM `eh_service_module_scopes`);

INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) 
select (@eh_service_module_scopes := @eh_service_module_scopes + 1), owner_id, menu_id, '', owner_type, owner_id, NULL, '2' from eh_web_menu_scopes where 
menu_id in (select id from eh_service_modules);
