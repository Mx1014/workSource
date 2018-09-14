-- 新增公共门禁权限子模块
INSERT INTO `ehcore`.`eh_service_modules` (
	`id`,
	`name`,
	`parent_id`,
	`path`,
	`type`,
	`level`,
	`status`,
	`default_order`,
	`create_time`,
	`instance_config`,
	`action_type`,
	`update_time`,
	`operator_uid`,
	`creator_uid`,
	`description`,
	`multiple_flag`,
	`module_control_type`,
	`access_control_type`,
	`menu_auth_flag`,
	`category`
)
VALUES
	(
		41011,
		'门禁授权',
		'40000',
		'/200/10000/41010/41011',
		'1',
		'4',
		'2',
		0,
		NOW(),
		NULL,
		NULL,
		NOW(),
		'0',
		'0',
		'0',
		'0',
		'community_control',
		'2',
		'1',
		'subModule'
	);

INSERT INTO `ehcore`.`eh_service_modules` (
	`id`,
	`name`,
	`parent_id`,
	`path`,
	`type`,
	`level`,
	`status`,
	`default_order`,
	`create_time`,
	`instance_config`,
	`action_type`,
	`update_time`,
	`operator_uid`,
	`creator_uid`,
	`description`,
	`multiple_flag`,
	`module_control_type`,
	`access_control_type`,
	`menu_auth_flag`,
	`category`
)
VALUES
	(
		41012,
		'门禁日志',
		'40000',
		'/200/10000/41010/41012',
		'1',
		'4',
		'2',
		0,
		NOW(),
		NULL,
		NULL,
		NOW(),
		'0',
		'0',
		'0',
		'0',
		'community_control',
		'2',
		'1',
		'subModule'
	);

INSERT INTO `ehcore`.`eh_service_modules` (
	`id`,
	`name`,
	`parent_id`,
	`path`,
	`type`,
	`level`,
	`status`,
	`default_order`,
	`create_time`,
	`instance_config`,
	`action_type`,
	`update_time`,
	`operator_uid`,
	`creator_uid`,
	`description`,
	`multiple_flag`,
	`module_control_type`,
	`access_control_type`,
	`menu_auth_flag`,
	`category`
)
VALUES
	(
		41013,
		'数据统计',
		'40000',
		'/200/10000/41010/41013',
		'1',
		'4',
		'2',
		0,
		NOW(),
		NULL,
		NULL,
		NOW(),
		'0',
		'0',
		'0',
		'0',
		'community_control',
		'2',
		'1',
		'subModule'
	);

INSERT INTO `ehcore`.`eh_service_modules` (
	`id`,
	`name`,
	`parent_id`,
	`path`,
	`type`,
	`level`,
	`status`,
	`default_order`,
	`create_time`,
	`instance_config`,
	`action_type`,
	`update_time`,
	`operator_uid`,
	`creator_uid`,
	`description`,
	`multiple_flag`,
	`module_control_type`,
	`access_control_type`,
	`menu_auth_flag`,
	`category`
)
VALUES
	(
		41014,
		'移动端管理',
		'40000',
		'/200/10000/41010/41014',
		'1',
		'4',
		'2',
		0,
		NOW(),
		NULL,
		NULL,
		NOW(),
		'0',
		'0',
		'0',
		'0',
		'community_control',
		'2',
		'1',
		'subModule'
	);

-- 新增企业门禁权限子模块
INSERT INTO `ehcore`.`eh_service_modules` (
	`id`,
	`name`,
	`parent_id`,
	`path`,
	`type`,
	`level`,
	`status`,
	`default_order`,
	`create_time`,
	`instance_config`,
	`action_type`,
	`update_time`,
	`operator_uid`,
	`creator_uid`,
	`description`,
	`multiple_flag`,
	`module_control_type`,
	`access_control_type`,
	`menu_auth_flag`,
	`category`
)
VALUES
	(
		41021,
		'门禁授权',
		'310000',
		'/100/310000/41020/4102',
		'1',
		'4',
		'2',
		0,
		NOW(),
		NULL,
		NULL,
		NOW(),
		'0',
		'0',
		'0',
		'0',
		'community_control',
		'2',
		'1',
		'subModule'
	);

INSERT INTO `ehcore`.`eh_service_modules` (
	`id`,
	`name`,
	`parent_id`,
	`path`,
	`type`,
	`level`,
	`status`,
	`default_order`,
	`create_time`,
	`instance_config`,
	`action_type`,
	`update_time`,
	`operator_uid`,
	`creator_uid`,
	`description`,
	`multiple_flag`,
	`module_control_type`,
	`access_control_type`,
	`menu_auth_flag`,
	`category`
)
VALUES
	(
		41022,
		'门禁日志',
		'310000',
		'/100/310000/41020/41022',
		'1',
		'4',
		'2',
		0,
		NOW(),
		NULL,
		NULL,
		NOW(),
		'0',
		'0',
		'0',
		'0',
		'community_control',
		'2',
		'1',
		'subModule'
	);

INSERT INTO `ehcore`.`eh_service_modules` (
	`id`,
	`name`,
	`parent_id`,
	`path`,
	`type`,
	`level`,
	`status`,
	`default_order`,
	`create_time`,
	`instance_config`,
	`action_type`,
	`update_time`,
	`operator_uid`,
	`creator_uid`,
	`description`,
	`multiple_flag`,
	`module_control_type`,
	`access_control_type`,
	`menu_auth_flag`,
	`category`
)
VALUES
	(
		41023,
		'数据统计',
		'310000',
		'/100/310000/41020/41023',
		'1',
		'4',
		'2',
		0,
		NOW(),
		NULL,
		NULL,
		NOW(),
		'0',
		'0',
		'0',
		'0',
		'community_control',
		'2',
		'1',
		'subModule'
	);

INSERT INTO `ehcore`.`eh_service_modules` (
	`id`,
	`name`,
	`parent_id`,
	`path`,
	`type`,
	`level`,
	`status`,
	`default_order`,
	`create_time`,
	`instance_config`,
	`action_type`,
	`update_time`,
	`operator_uid`,
	`creator_uid`,
	`description`,
	`multiple_flag`,
	`module_control_type`,
	`access_control_type`,
	`menu_auth_flag`,
	`category`
)
VALUES
	(
		41024,
		'移动端管理',
		'310000',
		'/100/310000/41020/41024',
		'1',
		'4',
		'2',
		0,
		NOW(),
		NULL,
		NULL,
		NOW(),
		'0',
		'0',
		'0',
		'0',
		'community_control',
		'2',
		'1',
		'subModule'
	);

-- 新增门禁权限项
INSERT INTO `ehcore`.`eh_acl_privileges` (
	`id`,
	`app_id`,
	`name`,
	`description`,
	`tag`
)
VALUES
	(
		4101041010,
		0,
		'公共门禁 全部权限',
		'公共门禁 全部权限',
		NULL
	);
INSERT INTO `ehcore`.`eh_acl_privileges` (
	`id`,
	`app_id`,
	`name`,
	`description`,
	`tag`
)
VALUES
	(
		4101041011,
		0,
		'公共门禁 门禁授权',
		'公共门禁 门禁授权权限',
		NULL
	);
INSERT INTO `ehcore`.`eh_acl_privileges` (
	`id`,
	`app_id`,
	`name`,
	`description`,
	`tag`
)
VALUES
	(
		4101041012,
		0,
		'公共门禁 门禁日志',
		'公共门禁 门禁日志权限',
		NULL
	);
INSERT INTO `ehcore`.`eh_acl_privileges` (
	`id`,
	`app_id`,
	`name`,
	`description`,
	`tag`
)
VALUES
	(
		4101041013,
		0,
		'公共门禁 数据统计',
		'公共门禁 数据统计权限',
		NULL
	);
INSERT INTO `ehcore`.`eh_acl_privileges` (
	`id`,
	`app_id`,
	`name`,
	`description`,
	`tag`
)
VALUES
	(
		4101041014,
		0,
		'公共门禁 移动端管理',
		'公共门禁 移动端管理权限',
		NULL
	);

INSERT INTO `ehcore`.`eh_acl_privileges` (
	`id`,
	`app_id`,
	`name`,
	`description`,
	`tag`
)
VALUES
	(
		4102041020,
		0,
		'企业门禁 全部权限',
		'企业门禁 全部权限',
		NULL
	);
INSERT INTO `ehcore`.`eh_acl_privileges` (
	`id`,
	`app_id`,
	`name`,
	`description`,
	`tag`
)
VALUES
	(
		4102041021,
		0,
		'企业门禁 门禁授权',
		'企业门禁 门禁授权权限',
		NULL
	);
INSERT INTO `ehcore`.`eh_acl_privileges` (
	`id`,
	`app_id`,
	`name`,
	`description`,
	`tag`
)
VALUES
	(
		4102041022,
		0,
		'企业门禁 门禁日志',
		'企业门禁 门禁日志权限',
		NULL
	);
INSERT INTO `ehcore`.`eh_acl_privileges` (
	`id`,
	`app_id`,
	`name`,
	`description`,
	`tag`
)
VALUES
	(
		4102041023,
		0,
		'企业门禁 数据统计',
		'企业门禁 数据统计权限',
		NULL
	);
INSERT INTO `ehcore`.`eh_acl_privileges` (
	`id`,
	`app_id`,
	`name`,
	`description`,
	`tag`
)
VALUES
	(
		4102041024,
		0,
		'企业门禁 移动端管理',
		'企业门禁 移动端管理权限',
		NULL
	);

--模块权限关联
SET @mp_id = (
	SELECT
		MAX(id)
	FROM
		eh_service_module_privileges
);

INSERT INTO `eh_service_module_privileges` (
	`id`,
	`module_id`,
	`privilege_type`,
	`privilege_id`,
	`remark`,
	`default_order`,
	`create_time`
)
VALUES
	(
		@mp_id :=@mp_id + 1,
		'41010',
		'0',
		4101041010,
		'全部权限',
		'0',
		NOW()
	);
	SET @mp_id = (
	SELECT
		MAX(id)
	FROM
		eh_service_module_privileges
);

INSERT INTO `eh_service_module_privileges` (
	`id`,
	`module_id`,
	`privilege_type`,
	`privilege_id`,
	`remark`,
	`default_order`,
	`create_time`
)
VALUES
	(
		@mp_id :=@mp_id + 1,
		'41011',
		'0',
		4101041011,
		'门禁授权权限',
		'0',
		NOW()
	);
	SET @mp_id = (
	SELECT
		MAX(id)
	FROM
		eh_service_module_privileges
);

INSERT INTO `eh_service_module_privileges` (
	`id`,
	`module_id`,
	`privilege_type`,
	`privilege_id`,
	`remark`,
	`default_order`,
	`create_time`
)
VALUES
	(
		@mp_id :=@mp_id + 1,
		'41012',
		'0',
		4101041012,
		'门禁日志权限',
		'0',
		NOW()
	);
	SET @mp_id = (
	SELECT
		MAX(id)
	FROM
		eh_service_module_privileges
);

INSERT INTO `eh_service_module_privileges` (
	`id`,
	`module_id`,
	`privilege_type`,
	`privilege_id`,
	`remark`,
	`default_order`,
	`create_time`
)
VALUES
	(
		@mp_id :=@mp_id + 1,
		'41013',
		'0',
		4101041013,
		'数据统计权限',
		'0',
		NOW()
	);
	SET @mp_id = (
	SELECT
		MAX(id)
	FROM
		eh_service_module_privileges
);

INSERT INTO `eh_service_module_privileges` (
	`id`,
	`module_id`,
	`privilege_type`,
	`privilege_id`,
	`remark`,
	`default_order`,
	`create_time`
)
VALUES
	(
		@mp_id :=@mp_id + 1,
		'41014',
		'0',
		4101041014,
		'移动端管理权限',
		'0',
		NOW()
	);
	SET @mp_id = (
	SELECT
		MAX(id)
	FROM
		eh_service_module_privileges
);

INSERT INTO `eh_service_module_privileges` (
	`id`,
	`module_id`,
	`privilege_type`,
	`privilege_id`,
	`remark`,
	`default_order`,
	`create_time`
)
VALUES
	(
		@mp_id :=@mp_id + 1,
		'41020',
		'0',
		4102041020,
		'全部权限',
		'0',
		NOW()
	);
	SET @mp_id = (
	SELECT
		MAX(id)
	FROM
		eh_service_module_privileges
);

INSERT INTO `eh_service_module_privileges` (
	`id`,
	`module_id`,
	`privilege_type`,
	`privilege_id`,
	`remark`,
	`default_order`,
	`create_time`
)
VALUES
	(
		@mp_id :=@mp_id + 1,
		'41021',
		'0',
		4102041021,
		'门禁授权权限',
		'0',
		NOW()
	);
	SET @mp_id = (
	SELECT
		MAX(id)
	FROM
		eh_service_module_privileges
);

INSERT INTO `eh_service_module_privileges` (
	`id`,
	`module_id`,
	`privilege_type`,
	`privilege_id`,
	`remark`,
	`default_order`,
	`create_time`
)
VALUES
	(
		@mp_id :=@mp_id + 1,
		'41022',
		'0',
		4102041022,
		'门禁日志权限',
		'0',
		NOW()
	);
	SET @mp_id = (
	SELECT
		MAX(id)
	FROM
		eh_service_module_privileges
);

INSERT INTO `eh_service_module_privileges` (
	`id`,
	`module_id`,
	`privilege_type`,
	`privilege_id`,
	`remark`,
	`default_order`,
	`create_time`
)
VALUES
	(
		@mp_id :=@mp_id + 1,
		'41023',
		'0',
		4102041023,
		'数据统计权限',
		'0',
		NOW()
	);
  SET @mp_id = (
	SELECT
		MAX(id)
	FROM
		eh_service_module_privileges
);

INSERT INTO `eh_service_module_privileges` (
	`id`,
	`module_id`,
	`privilege_type`,
	`privilege_id`,
	`remark`,
	`default_order`,
	`create_time`
)
VALUES
	(
		@mp_id :=@mp_id + 1,
		'41024',
		'0',
		4102041024,
		'移动端管理权限',
		'0',
		NOW()
	);



