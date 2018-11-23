-- AUTHOR: 杨崇鑫 20181114
-- REMARK: 物业缴费V7.5（中天-资管与财务EAS系统对接） ： 不支持同一笔账单即在左邻支付一半，又在EAS支付一半，不允许两边分别支付
ALTER TABLE `eh_payment_bills` ADD COLUMN `third_paid` TINYINT COMMENT '不支持同一笔账单即在左邻支付一半，又在EAS支付一半，不允许两边分别支付，0：没有任何支付，1：已在EAS支付';

-- AUTHOR: 黄鹏宇 2018-11-19
-- REMARK: 客户表单自定义
CREATE TABLE `eh_var_field_scope_filters` (
  `id` bigint NOT NULL,
  `namespace_id` int,
  `community_id` bigint COMMENT '被筛选的表单所在的园区id',
  `module_name` varchar(32) COMMENT '被筛选的表单的moduleName',
  `group_path` varchar(32) COMMENT '被筛选的表单的group',
  `field_id` bigint COMMENT '被筛选的表单id',
  `user_id` bigint COMMENT '被筛选的表单所属的用户id',
  `create_time` datetime,
  `create_uid` bigint,
  `status` tinyint,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '筛选显示的表单';

-- AUTHOR: 张智伟 20181010
-- REMARK: 企业支付授权1.0   -  企业支付授权管理员安全认证设置表
CREATE TABLE eh_service_module_securities (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL COMMENT '所属域空间',
    `module_id` BIGINT NOT NULL COMMENT '模块id，安全设置的模块',
    `owner_id` BIGINT COMMENT 'ownerType对应的对象ID',
    `owner_type` VARCHAR(48) COMMENT 'EhOrganizations或其它',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `salt` VARCHAR(64),
    `password_hash` VARCHAR(128),
    `last_login_time` DATETIME COMMENT '最近一次密码验证的时间',
    `create_time` DATETIME NOT NULL COMMENT '初次设置密码的时间',
    `update_time` DATETIME COMMENT '最近一次修改密码的时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `u_eh_organization_user_id` (`namespace_id` , `module_id`,`owner_id`,`owner_type` , `user_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT '模块安全认证设置表';

-- AUTHOR: 张智伟 20181010
-- REMARK: 企业支付授权1.0  -- 企业支付应用场景限制额度设置表
CREATE TABLE eh_enterprise_payment_auth_scene_limits (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL COMMENT '所属域空间',
    `organization_id` BIGINT NOT NULL COMMENT '所属企业ID',
    `payment_scene_app_id` BIGINT NOT NULL COMMENT '支付应用场景ID',
    `limit_amount` DECIMAL(20 , 2 ) COMMENT '每月额度，null值表示不限额',
    `historical_total_pay_amount` DECIMAL(20 , 2 ) NOT NULL DEFAULT 0 COMMENT '历史累计支付金额，放在这个表避免每次查询的计算损耗',
    `historical_pay_count` INTEGER NOT NULL DEFAULT 0 COMMENT '历史累计支付笔数，放在这个表避免每次查询的计算损耗',
	  `is_delete` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除状态,0:有效，1:删除',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operator_uid` BIGINT COMMENT '记录更新人userId',
    `operate_time` DATETIME COMMENT '记录更新时间',
    PRIMARY KEY (`id`),
	  KEY `i_eh_organization_payment_scene` (`namespace_id` , `organization_id` , `payment_scene_app_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT '企业支付应用场景限制额度设置表';

-- AUTHOR: 张智伟 20181010
-- REMARK: 企业支付授权1.0 -- 企业支付应用场景各个月份支付汇总表
CREATE TABLE eh_enterprise_payment_auth_scene_pay_histories (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL COMMENT '所属域空间',
    `organization_id` BIGINT NOT NULL COMMENT '所属企业ID',
    `payment_scene_app_id` BIGINT NOT NULL COMMENT '支付应用场景ID',
    `pay_month` CHAR(6) NOT NULL COMMENT '历史月份，格式:yyyyMM',
    `used_amount` DECIMAL(20 , 2 ) NOT NULL DEFAULT 0 COMMENT '当月使用额度',
    `pay_count` INTEGER NOT NULL DEFAULT 0 COMMENT '当月累计支付笔数',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operator_uid` BIGINT COMMENT '记录更新人userId',
    `operate_time` DATETIME COMMENT '记录更新时间',
    PRIMARY KEY (`id`),
	  KEY `i_eh_organization_payment_scene_month` (`namespace_id` , `organization_id` , `payment_scene_app_id`,`pay_month` DESC)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT '企业支付应用场景各个月份支付汇总表';

-- AUTHOR: 张智伟 20181010
-- REMARK: 企业支付授权1.0-- 企业支付员工限制额度设置表
CREATE TABLE eh_enterprise_payment_auth_employee_limits (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL COMMENT '所属域空间',
    `organization_id` BIGINT NOT NULL COMMENT '所属企业ID',
	  `user_id` BIGINT NULL COMMENT '被授权用户ID',
    `detail_id` BIGINT NOT NULL COMMENT '被授权用户detailId',
    `limit_amount` DECIMAL(20 , 2 ) COMMENT '每月额度，null值表示不限额',
    `historical_total_pay_amount` DECIMAL(20 , 2 ) NOT NULL DEFAULT 0 COMMENT '历史累计支付金额',
    `historical_pay_count` INTEGER NOT NULL DEFAULT 0 COMMENT '历史累计支付笔数',
    `payment_scene_list` VARCHAR(512) COMMENT '支付场景id列表，逗号隔开，主要用于列表展示，避免连表查询',
    `is_delete` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除状态,0:有效，1:删除',
    `delete_reason` VARCHAR(36) COMMENT '删除备注',
    `wait_auto_delete_month` CHAR(6) COMMENT '员工离职后下个月自动删除该配置',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operator_uid` BIGINT COMMENT '记录更新人userId',
    `operate_time` DATETIME COMMENT '记录更新时间',
    PRIMARY KEY (`id`),
	 KEY `i_eh_organization_detail_id` (`namespace_id` , `organization_id`,`detail_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT '企业支付员工限制额度设置表';

-- AUTHOR: 张智伟 20181010
-- REMARK: 企业支付授权1.0-- 企业支付员工全场景每月支付汇总表
CREATE TABLE eh_enterprise_payment_auth_employee_pay_histories (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL COMMENT '所属域空间',
    `organization_id` BIGINT NOT NULL COMMENT '所属企业ID',
	  `user_id` BIGINT NULL COMMENT '被授权用户ID',
    `detail_id` BIGINT NOT NULL COMMENT '被授权用户detailId',
    `pay_month` CHAR(6) NOT NULL COMMENT '历史月份，格式:yyyyMM',
    `used_amount` DECIMAL(20 , 2 ) NOT NULL DEFAULT 0 COMMENT '当月使用额度',
    `pay_count` INTEGER NOT NULL DEFAULT 0 COMMENT '当月累计支付笔数',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operator_uid` BIGINT COMMENT '记录更新人userId',
    `operate_time` DATETIME COMMENT '记录更新时间',
    PRIMARY KEY (`id`),
	 KEY `i_eh_organization_detail_month` (`namespace_id` , `organization_id`,`detail_id`,`pay_month` DESC)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT '企业支付员工全场景每月支付汇总表';

-- AUTHOR: 张智伟 20181010
-- REMARK: 企业支付授权1.0-- 企业支付员工基于支付场景限制额度设置表
CREATE TABLE eh_enterprise_payment_auth_employee_limit_details (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL COMMENT '所属域空间',
    `organization_id` BIGINT NOT NULL COMMENT '所属企业ID',
	  `user_id` BIGINT NULL COMMENT '被授权用户ID',
    `detail_id` BIGINT NOT NULL COMMENT '被授权用户detailId',
	  `payment_scene_app_id` BIGINT NOT NULL COMMENT '支付应用场景ID',
    `limit_amount` DECIMAL(20 , 2 ) COMMENT '每月额度，null值表示不限额',
    `historical_total_pay_amount` DECIMAL(20 , 2 ) NOT NULL DEFAULT 0 COMMENT '历史累计支付金额',
    `historical_pay_count` INTEGER NOT NULL DEFAULT 0 COMMENT '历史累计支付笔数',
    `is_delete` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除状态,0:有效，1:删除',
    `delete_reason` VARCHAR(36) COMMENT '删除备注',
    `wait_auto_delete_month` CHAR(6) COMMENT '员工离职后下个月自动删除该配置',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operator_uid` BIGINT COMMENT '记录更新人userId',
    `operate_time` DATETIME COMMENT '记录更新时间',
    PRIMARY KEY (`id`),
	KEY `i_eh_organization_detail_payment_scene` (`namespace_id` , `organization_id` , `detail_id`,`payment_scene_app_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT '企业支付员工基于支付场景限制额度设置表';

-- AUTHOR: 张智伟 20181010
-- REMARK: 企业支付授权1.0-- 企业支付员工基于支付场景每月支付汇总表
CREATE TABLE eh_enterprise_payment_auth_employee_pay_scene_histories (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL COMMENT '所属域空间',
    `organization_id` BIGINT NOT NULL COMMENT '所属企业ID',
	  `user_id` BIGINT NULL COMMENT '被授权用户ID',
    `detail_id` BIGINT NOT NULL COMMENT '被授权用户detailId',
	  `payment_scene_app_id` BIGINT NOT NULL COMMENT '支付应用场景ID',
	  `pay_month` CHAR(6) NOT NULL COMMENT '历史月份，格式:yyyyMM',
    `used_amount` DECIMAL(20 , 2 ) NOT NULL DEFAULT 0 COMMENT '已使用额度',
    `pay_count` INTEGER NOT NULL DEFAULT 0 COMMENT '当月累计支付笔数',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operator_uid` BIGINT COMMENT '记录更新人userId',
    `operate_time` DATETIME COMMENT '记录更新时间',
    PRIMARY KEY (`id`),
	KEY `i_eh_organization_detail_payment_scene_month` (`namespace_id` , `organization_id` , `detail_id`,`payment_scene_app_id`,`pay_month`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT '企业支付员工基于支付场景每月支付汇总表';

-- AUTHOR: 张智伟 20181010
-- REMARK: 企业支付授权1.0-- 企业支付授权管理员授权操作日志
CREATE TABLE eh_enterprise_payment_auth_operate_logs (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL COMMENT '所属域空间',
    `organization_id` BIGINT NOT NULL COMMENT '所属企业ID',
	  `log` TEXT COMMENT '授权日志',
    `operator_uid` BIGINT COMMENT '操作人userId',
    `operate_time` DATETIME COMMENT '操作时间',
    PRIMARY KEY (`id`),
	KEY `i_eh_namespace_organization_id` (`namespace_id` , `organization_id`,`id` DESC )
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT '企业支付授权管理员授权操作日志';

-- AUTHOR: 张智伟 20181010
-- REMARK: 企业支付授权1.0-- 企业支付员工授权额度变更记录表
CREATE TABLE eh_enterprise_payment_auth_employee_limit_change_logs (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL COMMENT '所属域空间',
    `organization_id` BIGINT NOT NULL COMMENT '所属企业ID',
	  `user_id` BIGINT NULL COMMENT '被授权用户ID',
    `detail_id` BIGINT NOT NULL COMMENT '被授权用户detailId',
	  `operate_source_id` BIGINT NOT NULL COMMENT '操作资源ID:支付应用场景Id,或者0表示每月额度',
    `operate_source_type` VARCHAR(32) COMMENT '操作资源类型，PAYMENT_SCENE:支付场景，LIMIT_AMOUNT:每月额度',
    `operate_source_name` VARCHAR(128) COMMENT '操作资源的名称:如支付场景 云打印',
    `operate_type` TINYINT NOT NULL DEFAULT 0 COMMENT '操作类型：-1：删除，0：变更额度，1：添加',
    `changed_amount` DECIMAL(20,2) COMMENT '变更额度',
    `current_amount` DECIMAL(20,2) COMMENT '变更后的额度',
    `operate_no` BIGINT NOT NULL COMMENT '一组操作的标识',
    `operator_uid` BIGINT COMMENT '操作人userId',
    `operate_time` DATETIME COMMENT '操作时间',
    PRIMARY KEY (`id`),
	KEY `i_eh_organization_detail_id` (`namespace_id` , `organization_id`,`detail_id`,`operate_no` DESC )
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT '企业支付员工授权额度变更记录表';

-- AUTHOR: 张智伟 20181010
-- REMARK: 企业支付授权1.0-- 企业支付员工支付记录表
CREATE TABLE eh_enterprise_payment_auth_pay_logs (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL COMMENT '所属域空间',
    `organization_id` BIGINT NOT NULL COMMENT '所属企业ID',
    `user_id` BIGINT NOT NULL COMMENT '被授权用户ID',
    `detail_id` BIGINT NOT NULL COMMENT '被授权用户detailId',
    `payment_scene_app_id` BIGINT NOT NULL COMMENT '支付应用场景ID',
    `merchant_order_id` BIGINT NOT NULL COMMENT '统一订单系统订单号',
    `biz_order_num` VARCHAR(256) COMMENT '订单编号',
    `pay_ammount` DECIMAL(20 , 2 ) COMMENT '支付金额',
    `pay_time` DATETIME COMMENT '支付时间',
    `payment_type` TINYINT COMMENT '支付类型：企业账单、企业钱包、其它企业支付类型',
    `is_refund` TINYINT NOT NULL DEFAULT 0 COMMENT '是否退款记录：0:否 1:是',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    PRIMARY KEY (`id`),
    KEY `i_eh_organization_detail` (`namespace_id` , `organization_id`,`detail_id`,`pay_time` DESC )
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT '企业支付员工支付记录表';

-- AUTHOR: 张智伟 20181010
-- REMARK: 企业支付授权1.0-- 企业支付授权-支付余额冻结表
CREATE TABLE eh_enterprise_payment_auth_frozen_requests (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL COMMENT '所属域空间',
    `organization_id` BIGINT NOT NULL COMMENT '所属企业ID',
    `user_id` BIGINT NOT NULL COMMENT '被授权用户ID',
    `detail_id` BIGINT NOT NULL COMMENT '被授权用户detailId',
    `payment_scene_app_id` BIGINT NOT NULL COMMENT '支付应用场景ID',
    `merchant_order_id` BIGINT NOT NULL COMMENT '统一订单系统订单号',
    `frozen_ammount` DECIMAL(20 , 2 ) COMMENT '待支付金额（冻结额度）',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1:冻结中，2:冻结确认，3:取消冻结',
    `remark` VARCHAR(128) COMMENT '备注信息',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operate_time` DATETIME COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `i_eh_order_id` (`merchant_order_id` DESC ),
    KEY `i_eh_create_time`(`create_time` DESC )
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT '企业支付授权-支付余额冻结表';

-- AUTHOR: 张智伟 20181010
-- REMARK: 企业支付授权1.0
delimiter //
-- 当eh_organization_member_details的employee_status被变更为离职状态3时执行该触发器
CREATE TRIGGER `employee_dismiss_trigger_for_auto_remove_payment_limit` AFTER UPDATE ON `eh_organization_member_details` FOR EACH ROW
  BEGIN
	DECLARE nextMonthDate DATETIME;
	DECLARE month VARCHAR(6);
	SET nextMonthDate = (SELECT date_add(NOW(), interval 1 MONTH));
	SET month=(SELECT CONCAT(YEAR(nextMonthDate),MONTH(nextMonthDate)));

	IF NEW.employee_status != OLD.employee_status AND NEW.employee_status = 3 THEN
		UPDATE eh_enterprise_payment_auth_employee_limits SET wait_auto_delete_month=month WHERE namespace_id=OLD.namespace_id AND organization_id=OLD.organization_id AND detail_id=OLD.id AND is_delete=0;
		UPDATE eh_enterprise_payment_auth_employee_limit_details SET wait_auto_delete_month=month WHERE namespace_id=OLD.namespace_id AND organization_id=OLD.organization_id AND detail_id=OLD.id AND is_delete=0;
	END IF;

  END;
//

delimiter ;

-- AUTHOR: 梁燕龙 20181123
-- REMARK: 圳智慧对接所需表
CREATE TABLE `eh_zhenzhihui_user_info` (
  `id` BIGINT NOT NULL,
  `name` VARCHAR (62) NOT NULL DEFAULT '' COMMENT '姓名',
  `identify_token` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '证件号码',
  `identify_type` INTEGER NOT NULL DEFAULT 10 COMMENT '证件类型',
  `user_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_users',
  `email` VARCHAR(64) NOT NULL DEFAULT '""' COMMENT '邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='圳智慧所需用户信息。';

CREATE TABLE `eh_zhenzhihui_enterprise_info` (
  `id` BIGINT NOT NULL,
  `enterprise_name` VARCHAR(64) DEFAULT '' COMMENT '单位名称',
  `enterprise_token` VARCHAR(64) DEFAULT '' COMMENT '单位证件号码',
  `enterprise_type`  INTEGER NOT NULL DEFAULT 10 COMMENT '单位证件类型',
  `corporation_name` VARCHAR(32) DEFAULT '' COMMENT '法人名称',
  `identify_token` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '法人证件号码',
  `identify_type` INTEGER NOT NULL DEFAULT 10 COMMENT '法人证件类型',
  `user_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_users',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='圳智慧所需企业信息。';

-- AUTHOR: djm
-- REMARK: 合同初始化，审批，复制进度记录表
CREATE TABLE `eh_contract_task_operate_logs` (
	`id` BIGINT (20) NOT NULL,
	`namespace_id` INT (11) DEFAULT NULL,
	`owner_id` BIGINT (20) DEFAULT NULL COMMENT '园区id',
	`owner_type` VARCHAR (64) NOT NULL,
	`org_id` BIGINT (20) DEFAULT NULL,
	`name` VARCHAR (64),
	`process` INTEGER COMMENT 'rate of progress',
	`operate_type` TINYINT (4) DEFAULT NULL COMMENT '1 合同初始化, ２　合同免批,3　合同复制',
	`processed_number` INT (11) DEFAULT NULL COMMENT '已处理记录数',
	`total_number` INT (11) DEFAULT NULL COMMENT '总记录数',
	`status` TINYINT (4) DEFAULT NULL COMMENT '0:无效状态，2：激活状态',
	`start_time` DATETIME DEFAULT NULL,
	`finish_time` DATETIME DEFAULT NULL,
	`execute_time` BIGINT DEFAULT NULL,
	`params` TEXT,
	`error_description` VARCHAR(255),
	`category_id` BIGINT COMMENT 'asset category id',
	`creator_uid` BIGINT (20) DEFAULT NULL,
	`create_time` DATETIME DEFAULT NULL,
	`operator_uid` BIGINT (20) DEFAULT NULL,
	`operator_time` DATETIME DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '合同初始化,审批,复制进度记录表';


-- AUTHOR: 杨崇鑫
-- REMARK: 缺陷 #42416 【中天】更新自然季，合同刷新账单报错。
ALTER TABLE `eh_contracts` MODIFY COLUMN `rent` decimal(20, 2) NULL DEFAULT NULL COMMENT '租金' AFTER `rent_size`;

-- AUTHOR: 李清岩
-- REMARK: 门禁v3.0.2 issue-34771
-- REMARK: 增加门禁组门禁关系表
CREATE TABLE `eh_aclink_group_doors` (
	`id` BIGINT  NOT NULL,
	`namespace_id` INT  DEFAULT NULL COMMENT '域空间id',
	`owner_id` BIGINT  NOT NULL DEFAULT '0' COMMENT '门禁组所属机构id',
	`owner_type` TINYINT DEFAULT NULL COMMENT '门禁组所属机构类型 0园区 1公司',
	`group_id` BIGINT DEFAULT NULL COMMENT '门禁组id',
	`door_id` BIGINT DEFAULT NULL COMMENT '门禁组所属门禁id',
	`status` TINYINT NOT NULL DEFAULT '1' COMMENT '状态 0删除 1有效',
	`creator_uid` BIGINT  DEFAULT NULL COMMENT '创建者id',
	`create_time` datetime DEFAULT NULL COMMENT '创建时间',
	`operator_uid` BIGINT  DEFAULT NULL COMMENT '修改者id',
	`operator_time` datetime DEFAULT NULL COMMENT '修改时间',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '门禁组门禁关系表';

-- AUTHOR: 李清岩
-- REMARK: 门禁v3.0.2 issue-34771
-- REMARK: 门禁iPad自定义logo
ALTER TABLE `eh_aclink_ipads` ADD COLUMN `logo_uri` VARCHAR(2048) DEFAULT NULL COMMENT 'logo uri' ;
ALTER TABLE `eh_aclink_ipads` ADD COLUMN `logo_url` VARCHAR(2048) DEFAULT NULL COMMENT 'logo url' ;

-- AUTHOR: 李清岩
-- REMARK: 门禁v3.0.2 issue-34771
-- REMARK: 调整eh_door_access列名
ALTER TABLE `eh_door_access` CHANGE adress_detail address_detail VARCHAR(64);

