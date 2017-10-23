-- 园区入驻 add by sw 20171023
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ('1', '1000000', NULL, NULL, 'renewFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ('3', '1000000', NULL, NULL, 'rentAmountFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ('4', '1000000', NULL, NULL, 'rentAmountUnit', 'MONTH_UNIT', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ('5', '1000000', NULL, NULL, 'issuingLeaseFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ('6', '1000000', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ('apply.entry.lease.project.detail.url', '/park-entry/dist/index.html?hideNavigationBar=1#/project_intro/%s', NULL, '0', NULL);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
	VALUES ('40103', '项目介绍', '40100', NULL, 'react:/project-intro/project-list', '0', '2', '/40000/40100/40103', 'park', '411', '40100', '3', NULL, 'module');
update eh_web_menus set name = '楼栋介绍' where id = 40105;
set @id = (select MAX(id) FROM eh_web_menu_scopes);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	select (@id := @id + 1), '40103', '', 'EhNamespaces', owner_id, '2' from eh_web_menu_scopes where menu_id = 40100;
