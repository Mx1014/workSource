-- AUTHOR: 梁燕龙
-- REMARK: 用户表增加企业ID
ALTER TABLE `eh_users` ADD COLUMN `company_id` BIGINT COMMENT '公司ID';
-- END

 
-- AUTHOR: 吴寒
-- REMARK: 公告1.8 修改表结构
ALTER TABLE `eh_enterprise_notices` ADD COLUMN `stick_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶，0-否，1-是';
ALTER TABLE `eh_enterprise_notices` ADD COLUMN `stick_time` DATETIME;
-- REMARK: 公告1.8 修改表结构
-- END

-- AUTHOR: 张智伟 20180822
-- REMARK: issue-36367 考勤规则新增打卡提醒设置
ALTER TABLE eh_punch_rules ADD COLUMN punch_remind_flag TINYINT NOT NULL DEFAULT 0 COMMENT '是否开启上下班打卡提醒：1 开启 0 关闭' AFTER china_holiday_flag;
ALTER TABLE eh_punch_rules ADD COLUMN remind_minutes_on_duty INT NOT NULL DEFAULT 0 COMMENT '上班提前分钟数打卡提醒' AFTER punch_remind_flag;

-- AUTHOR: 张智伟 20180822
-- REMARK: issue-36367 考勤规则新增打卡提醒设置,该表保存生成的提醒记录
CREATE TABLE `eh_punch_notifications` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间',
  `enterprise_id` BIGINT NOT NULL COMMENT '总公司id',
  `user_id` BIGINT NOT NULL COMMENT '被提醒人的uid',
  `detail_id` BIGINT NOT NULL COMMENT '被提醒人的detailId',
  `punch_rule_id` BIGINT NOT NULL COMMENT '所属考勤规则',
  `punch_type` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0- 上班打卡 ; 1- 下班打卡',
  `punch_interval_no` INT(11) DEFAULT '1' COMMENT '第几次排班的打卡',
  `punch_date` DATE NOT NULL COMMENT '打卡日期',
  `rule_time` DATETIME NOT NULL COMMENT '规则设置的该次打卡时间',
  `except_remind_time` DATETIME NOT NULL COMMENT '规则设置的打卡提醒时间',
  `act_remind_time` DATETIME NULL COMMENT '实际提醒时间',
  `invalid_reason` VARCHAR(512) COMMENT '提醒记录失效的原因',
  `invalid_flag` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0- 有效 ; 1- 无效',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `update_time` DATETIME NULL COMMENT '记录创建时间',
  PRIMARY KEY (`id`),
  KEY i_eh_enterprise_detail_id(`namespace_id`,`enterprise_id`,`detail_id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='打卡提醒队列，该数据只保留一天';

-- AUTHOR: 张智伟 20180822
-- REMARK: issue-36367 打卡记录报表排序
ALTER TABLE eh_punch_logs ADD COLUMN detail_id BIGINT COMMENT '员工 的detail Id' AFTER user_id;
ALTER TABLE eh_punch_log_files ADD COLUMN detail_id BIGINT COMMENT '员工 的detail Id' AFTER user_id;


-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.6（对接统一账单） 业务应用与缴费的关联关系表
-- REMARK: 1、contract_category_id字段改名为source_id
ALTER TABLE eh_asset_module_app_mappings CHANGE `contract_category_id` `source_id` BIGINT COMMENT '各个业务系统定义的唯一标识（id）';
-- REMARK: 2、增加相关字段
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `source_type` VARCHAR(1024) COMMENT '各个业务系统定义的唯一标识（类型）' after `source_id`;
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `config` VARCHAR(1024) COMMENT '各个业务系统自定义的JSON配置' after `source_type`;
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `owner_id` BIGINT COMMENT '园区ID' after `config`;
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `owner_type` VARCHAR(64) COMMENT '园区类型' after `owner_id`;
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `bill_group_id` BIGINT COMMENT '账单组ID';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `charging_item_id` BIGINT COMMENT '费项ID';
-- REMARK: 3、删除无效字段
ALTER TABLE `eh_asset_module_app_mappings` DROP COLUMN `energy_category_id`;
-- REMARK: 4、去掉原来的限制索引
ALTER TABLE eh_asset_module_app_mappings DROP INDEX u_asset_category_id;
ALTER TABLE eh_asset_module_app_mappings DROP INDEX u_contract_category_id;

-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.6（对接统一账单） 账单要增加来源字段
ALTER TABLE `eh_payment_bills` ADD COLUMN `source_type` VARCHAR(1024) COMMENT '各个业务系统定义的唯一标识（类型）';
ALTER TABLE `eh_payment_bills` ADD COLUMN `source_id` BIGINT COMMENT '各个业务系统定义的唯一标识（id）';
ALTER TABLE `eh_payment_bills` ADD COLUMN `source_name` VARCHAR(1024) COMMENT '账单来源（如：停车缴费，缴费的新增/导入等）';
ALTER TABLE `eh_payment_bills` ADD COLUMN `consume_user_id` BIGINT COMMENT '企业下面的某个人的ID';
-- REMARK: 物业缴费V6.6（对接统一账单） 账单费项要增加来源字段
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `source_type` VARCHAR(1024) COMMENT '各个业务系统定义的唯一标识（类型）';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `source_id` BIGINT COMMENT '各个业务系统定义的唯一标识（id）';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `source_name` VARCHAR(1024) COMMENT '账单来源（如：停车缴费，缴费的新增/导入等）';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `consume_user_id` BIGINT COMMENT '企业下面的某个人的ID';

-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.0 收费项配置可手动新增
ALTER TABLE `eh_payment_charging_items` ADD COLUMN `namespace_id` INTEGER COMMENT '增加域空间ID作为标识';
ALTER TABLE `eh_payment_charging_items` ADD COLUMN `owner_id` BIGINT COMMENT '增加园区ID作为标识';
ALTER TABLE `eh_payment_charging_items` ADD COLUMN `owner_type` VARCHAR(64)  COMMENT '增加园区ID作为标识';
ALTER TABLE `eh_payment_charging_items` ADD COLUMN `category_id` BIGINT COMMENT '多入口应用id';
-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
ALTER TABLE `eh_payment_bills` ADD COLUMN `can_delete` TINYINT DEFAULT 0 COMMENT '0：不可删除；1：可删除';
ALTER TABLE `eh_payment_bills` ADD COLUMN `can_modify` TINYINT DEFAULT 0 COMMENT '0：不可编辑；1：可编辑';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `can_delete` TINYINT DEFAULT 0 COMMENT '0：不可删除；1：可删除';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `can_modify` TINYINT DEFAULT 0 COMMENT '0：不可编辑；1：可编辑';
-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.0 账单、费项表增加是否删除状态字段
ALTER TABLE `eh_payment_bills` ADD COLUMN `delete_flag` TINYINT DEFAULT 1 COMMENT '删除状态：0：已删除；1：正常使用';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `delete_flag` TINYINT DEFAULT 1 COMMENT '删除状态：0：已删除；1：正常使用';

-- REMARK: 账单表增加第三方账单唯一标识字段
ALTER TABLE `eh_payment_bills` ADD COLUMN `third_bill_id` VARCHAR(1024) COMMENT '账单表增加第三方唯一标识字段';

-- AUTHOR: 黄明波
-- REMARK: #33683服务联盟样式列表添加排序 #37669修复
ALTER TABLE `eh_service_alliance_categories`	CHANGE COLUMN `default_order` `default_order` BIGINT NOT NULL DEFAULT '0' ;
ALTER TABLE `eh_service_alliances` CHANGE COLUMN `address` `address` VARCHAR(255) NULL DEFAULT NULL ;
-- END



-- AUTHOR: 吴寒
-- REMARK: issue-33887: 增加操作人姓名到目录/文件表
ALTER TABLE `eh_file_management_contents` ADD COLUMN `operator_name`  VARCHAR(256) ;
ALTER TABLE `eh_file_management_catalogs` ADD COLUMN `operator_name`  VARCHAR(256) ;
-- REMARK: issue-33887: 给文件表增加索引
ALTER TABLE `eh_file_management_contents` ADD INDEX  `i_eh_content_catalog_id` (`catalog_id`);
ALTER TABLE `eh_file_management_contents` ADD INDEX  `i_eh_content_parent_id` (`parent_id`);
-- REMARK: issue-33887
-- END



 
-- AUTHOR: 吴寒
-- REMARK: issue-33943 日程提醒1.2
ALTER TABLE eh_remind_settings ADD COLUMN app_version VARCHAR(32) DEFAULT '5.8.0' COMMENT '对应app版本(历史数据5.8.0),根据APP版本选择性展示';
ALTER TABLE eh_remind_settings ADD COLUMN before_time BIGINT COMMENT '提前多少时间(毫秒数)不超过1天的部分在这里减';
-- END issue-33943


 
-- AUTHOR: 吴寒
-- REMARK: 会议管理V1.2
ALTER TABLE `eh_meeting_reservations`  CHANGE `content` `content` TEXT COMMENT '会议详细内容';
ALTER TABLE `eh_meeting_reservations`  ADD COLUMN `attachment_flag` TINYINT DEFAULT 0 COMMENT '是否有附件 1-是 0-否';
ALTER TABLE `eh_meeting_records`  ADD COLUMN `attachment_flag` TINYINT DEFAULT 0 COMMENT '是否有附件 1-是 0-否';

-- 增加附件表 会议预定和会议纪要共用
CREATE TABLE `eh_meeting_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'owner type EhMeetingRecords/EhMeetingReservations',
  `owner_id` BIGINT NOT NULL COMMENT 'key of the owner',
  `content_name` VARCHAR(1024) COMMENT 'attachment object content name like: abc.jpg',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `content_size` INT(11)  COMMENT 'attachment object size',
  `content_icon_uri` VARCHAR(1024) COMMENT 'attachment object link of content icon',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- END 会议管理V1.2

-- AUTHOR: 荣楠
-- REMARK: issue-34029 工作汇报1.2
ALTER TABLE `eh_work_report_val_receiver_map` ADD COLUMN `organization_id` BIGINT DEFAULT 0 NOT NULL COMMENT 'the orgId for the user' AFTER `namespace_id`;
ALTER TABLE `eh_work_report_val_receiver_map` ADD INDEX `i_work_report_receiver_id` (`receiver_user_id`) ;

ALTER TABLE `eh_work_reports` ADD COLUMN `validity_setting` VARCHAR(512) COMMENT 'the expiry date of the work report' AFTER `form_version`;
ALTER TABLE `eh_work_reports` ADD COLUMN `receiver_msg_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of the receiver message settings' AFTER `validity_setting`;
ALTER TABLE `eh_work_reports` ADD COLUMN `receiver_msg_seeting` VARCHAR(512) COMMENT 'the time range of the receiver message' AFTER `receiver_msg_type`;
ALTER TABLE `eh_work_reports` ADD COLUMN `author_msg_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of the author message settings' AFTER `receiver_msg_seeting`;
ALTER TABLE `eh_work_reports` ADD COLUMN `author_msg_seeting` VARCHAR(512) COMMENT 'the time range of the author message' AFTER `author_msg_type`;

ALTER TABLE `eh_work_report_vals` ADD COLUMN `receiver_avatar` VARCHAR(1024) COMMENT 'the avatar of the fisrt receiver' AFTER `report_type`;
ALTER TABLE `eh_work_report_vals` ADD COLUMN `applier_avatar` VARCHAR(1024) COMMENT 'the avatar of the author' AFTER `receiver_avatar`;

ALTER TABLE `eh_work_report_vals` MODIFY COLUMN `report_time` DATE COMMENT 'the target time of the report';


CREATE TABLE `eh_work_report_val_receiver_msg` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `report_id` BIGINT NOT NULL COMMENT 'the id of the report',
  `report_val_id` BIGINT NOT NULL COMMENT 'id of the report val',
  `report_name` VARCHAR(128) NOT NULL,
  `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
  `report_time` DATE NOT NULL COMMENT 'the target time of the report',
  `reminder_time` DATETIME COMMENT 'the reminder time of the record',
  `receiver_user_id` BIGINT NOT NULL COMMENT 'the id of the receiver',
  `create_time` DATETIME COMMENT 'record create time',

  KEY `i_eh_work_report_val_receiver_msg_report_id`(`report_id`),
  KEY `i_eh_work_report_val_receiver_msg_report_val_id`(`report_val_id`),
  KEY `i_eh_work_report_val_receiver_msg_report_time`(`report_time`),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_work_report_scope_msg` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `report_id` BIGINT NOT NULL COMMENT 'the id of the report',
  `report_name` VARCHAR(128) NOT NULL,
  `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
  `report_time` DATE NOT NULL COMMENT 'the target time of the report',
  `reminder_time` DATETIME COMMENT 'the reminder time of the record',
  `end_time` DATETIME COMMENT 'the deadline of the report',
  `scope_ids` TEXT COMMENT 'the id list of the receiver',
  `create_time` DATETIME COMMENT 'record create time',

  KEY `i_eh_work_report_scope_msg_report_id`(`report_id`),
  KEY `i_eh_work_report_scope_msg_report_time`(`report_time`),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- END issue-34029

-- AUTHOR: 丁建民 20180920
-- REMARK: issue-37007 资产设置一房一价，租赁价格大于设定的价格，则合同不需要审批
alter table eh_contracts add sponsor_uid BIGINT COMMENT '发起人id';
alter table eh_contracts add sponsor_time DATETIME COMMENT '发起时间';

CREATE TABLE `eh_address_properties` (
	`id` BIGINT (20) NOT NULL COMMENT 'id of the record',
	`namespace_id` INT NOT NULL DEFAULT '0' COMMENT 'namespaceId',
	`community_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'communityId',
	`building_id` BIGINT (20) DEFAULT NULL COMMENT '楼栋id',
	`address_id` BIGINT (20) DEFAULT NULL COMMENT '房源id',
	`charging_items_id` BIGINT (20) COMMENT '费项id',
	`authorize_price` DECIMAL (10, 2) COMMENT '授权价',
	`apartment_authorize_type` TINYINT (4) DEFAULT NULL COMMENT '房源授权价类型（1:每天; 2:每月; 3:每个季度; 4:每年;)',
	`status` TINYINT (4) COMMENT '0-无效状态 ,2-有效状态',
	`create_time` DATETIME COMMENT '创建日期',
	`creator_uid` BIGINT COMMENT '创建人',
	`operator_time` DATETIME COMMENT '最近修改时间',
	`operator_uid` BIGINT COMMENT '最近修改人',
	PRIMARY KEY (id)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '楼宇属性信息表';

alter table eh_payment_bills modify column noticeTel varchar(255) COMMENT '催缴手机号码';
-- END issue-37007



-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 招商客户联系人表

CREATE TABLE `eh_customer_contacts`
(
	`id`                   BIGINT NOT NULL,
	`namespace_id` 				INT NOT NULL DEFAULT '0' COMMENT 'namespaceId',
	`community_id`         BIGINT NOT NULL DEFAULT '0' COMMENT 'communityId',
	`customer_id` 					BIGINT NOT NULL DEFAULT '0' COMMENT '关联的客户ID',
	`name`             		VARCHAR(64)  COMMENT '联系人名称',
	`phone_number`         BIGINT  COMMENT '联系人电话',
	`email`           			VARCHAR(128)  COMMENT '联系人邮箱',
	`position`         		VARCHAR(128)  COMMENT '联系人职务',
	`address`							VARCHAR(256)  COMMENT '联系人通讯地址',
	`contact_type`				   TINYINT  COMMENT '联系人类型，0-客户联系人、1-渠道联系人',
	`customer_source`					TINYINT  COMMENT '联系人来源，0-客户管理，1-租客管理',
	`status`								TINYINT  COMMENT '联系人状态，0-invalid ,2-valid',
	`create_time`          DATETIME  COMMENT '创建日期',
	`creator_uid` 						BIGINT  COMMENT '创建人',
	`operator_time` 				DATETIME  COMMENT '最近修改时间',
	`operator_uid`					BIGINT  COMMENT '最近修改人',
	primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '招商客户联系人表';
ALTER TABLE `eh_customer_contacts` ADD INDEX idx_namespace_id(namespace_id);

-- end


-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 招商客户跟进人表

CREATE TABLE `eh_customer_trackers`
(
	`id`                   BIGINT NOT NULL,
	`namespace_id` 				INT NOT NULL DEFAULT '0' COMMENT 'namespaceId',
	`community_id`         BIGINT NOT NULL DEFAULT '0' COMMENT 'communityId',
	`customer_id` 					BIGINT NOT NULL DEFAULT '0' COMMENT '关联的客户ID',
	`tracker_uid`           BIGINT COMMENT '跟进人id',
	`tracker_type`				   TINYINT COMMENT '跟进人类型，0-招商跟进人、1-租户拜访人',
	`customer_source`					TINYINT  COMMENT '联系人来源，0-客户管理，1-租客管理',
	`status`								TINYINT  COMMENT '状态，0-invalid ,2-valid',
	`create_time`          DATETIME  COMMENT '创建日期',
	`creator_uid` 						BIGINT  COMMENT '创建人',
	`operator_time` 				DATETIME  COMMENT '最近修改时间',
	`operator_uid`					BIGINT  COMMENT '最近修改人',
	primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '招商客户跟进人表';
ALTER TABLE `eh_customer_trackers` ADD INDEX idx_namespace_id(namespace_id);

-- end

-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 招商客户需求信息表


CREATE TABLE `eh_customer_requirements`
(
   `id`                   BIGINT NOT NULL,
	 `namespace_id` 				INT NOT NULL DEFAULT '0' COMMENT 'namespaceId',
	 `community_id`         BIGINT NOT NULL DEFAULT '0' COMMENT 'communityId',
	 `customer_id` 					BIGINT NOT NULL DEFAULT '0' COMMENT '关联的客户ID',
   `intention_location`    VARCHAR(256) COMMENT '期望地段',
	 `min_area`			DECIMAL(10,2) COMMENT '期望最小面积',
	 `max_area`			DECIMAL(10,2) COMMENT '期望最大面积',
	 `min_rent_price`			DECIMAL(10,2) COMMENT '期望最小租金-单价',
	 `max_rent_price`			DECIMAL(10,2) COMMENT '期望最大租金-单价',
	 `rent_price_unit`		TINYINT COMMENT '期望租金单位，0-元/㎡，1-元/㎡/月,2-元/天，3-元/月，4-元',
	 `rent_type`					TINYINT COMMENT '租赁/购买：0-租赁，1-购买',
	 `version`				      BIGINT COMMENT '记录版本',
	 `status`								TINYINT  COMMENT '状态，0-invalid ,2-valid',
	`create_time`          DATETIME  COMMENT '创建日期',
	`creator_uid` 						BIGINT  COMMENT '创建人',
	`operator_time` 				DATETIME  COMMENT '最近修改时间',
	`operator_uid`					BIGINT  COMMENT '最近修改人',
   primary key (id)

) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'eh_enterprise_investment_demand in dev mode';

ALTER TABLE `eh_customer_requirements` ADD INDEX idx_namespace_id(namespace_id);


-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 招商客户需求房源关系表

CREATE TABLE `eh_customer_requirement_addresses`
(
   `id`                   BIGINT NOT NULL,
	 `namespace_id` 				INT NOT NULL DEFAULT '0' COMMENT 'namespaceId',
	 `community_id`         BIGINT NOT NULL DEFAULT '0' COMMENT 'communityId',
	 `requirement_id` 			BIGINT NOT NULL DEFAULT '0' COMMENT '关联的需求ID',
	 `customer_id` 					BIGINT NOT NULL DEFAULT '0' COMMENT '关联的客户ID',
	 `address_id`			      BIGINT COMMENT '意向房源',
	 `status`								TINYINT  COMMENT '状态，0-invalid ,2-valid',
	`create_time`          DATETIME  COMMENT '创建日期',
	`creator_uid` 						BIGINT  COMMENT '创建人',
	`operator_time` 				DATETIME  COMMENT '最近修改时间',
	`operator_uid`					BIGINT  COMMENT '最近修改人',
   primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '招商客户需求房源关系表';
ALTER TABLE `eh_customer_requirement_addresses` ADD INDEX idx_namespace_id(namespace_id);

-- END


-- AUTHOR 黄鹏宇 2018-9-6
-- REMARK 动态表单公用组件表

CREATE TABLE `eh_var_field_ranges`
(
   `id`                   BIGINT NOT NULL,
	 `group_path` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'refer to eh_var_fields',
    `field_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refer to eh_var_fields',
    `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
    `module_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '一组公用表单的类型',
    primary key (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '动态表单公用组件表';
ALTER TABLE `eh_var_field_ranges` ADD INDEX idx_module_name(module_name);
ALTER TABLE `eh_var_field_ranges` ADD INDEX idx_module_type(module_type);

-- AUTHOR 黄鹏宇 2018-9-6
-- REMARK 动态表单公用组件表

CREATE TABLE `eh_var_field_group_ranges`
(
   `id`                   BIGINT NOT NULL,
	 `group_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refer to eh_var_field_groups',
    `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
    `module_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '一组公用表单的类型',
    primary key (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '动态表单公用分组表';
ALTER TABLE `eh_var_field_group_ranges` ADD INDEX idx_module_name(module_name);
ALTER TABLE `eh_var_field_group_ranges` ADD INDEX idx_module_type(module_type);


-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 招商客户当前信息表

CREATE TABLE `eh_customer_current_rents`
(
   `id`                   BIGINT NOT NULL,
	 `namespace_id` 				INT NOT NULL DEFAULT '0' COMMENT 'namespaceId',
	 `community_id`         BIGINT NOT NULL DEFAULT '0' COMMENT 'communityId',
	 `customer_id` 					BIGINT NOT NULL DEFAULT '0' COMMENT '关联的客户ID',
   `address`    			    VARCHAR(256) COMMENT '当前地址',
	 `rent_price`						DECIMAL(10,2) COMMENT '当前租金',
	 `rent_price_unit`		  TINYINT COMMENT '租金单位，0-元/㎡，1-元/㎡/月,2-元/天，3-元/月，4-元',
	 `rent_area`							DECIMAL(10,2) COMMENT '当前租赁面积',
	 `contract_intention_date` DATETIME COMMENT '当前合同到期日',
	 `version`				      BIGINT COMMENT '记录版本',
	 `status`								TINYINT  COMMENT '状态，0-invalid ,2-valid',
	`create_time`          DATETIME  COMMENT '创建日期',
	`creator_uid` 						BIGINT  COMMENT '创建人',
	`operator_time` 				DATETIME  COMMENT '最近修改时间',
	`operator_uid`					BIGINT  COMMENT '最近修改人',
   primary key (id)

) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '招商客户当前信息表';
ALTER TABLE `eh_customer_current_rents` ADD INDEX idx_namespace_id(namespace_id);

-- end


-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 对客户表增加招商客户和成交租客的表示加以区分

ALTER TABLE `eh_enterprise_customers` ADD `customer_source` TINYINT COMMENT '跟进信息类型，0-招商客户，1-成交租客';

-- end


-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 对当前跟进表加上类型标识

ALTER TABLE `eh_customer_trackings` ADD `customer_source` TINYINT COMMENT '跟进信息类型，0-客户跟进信息，1-租客跟进信息';

-- end


-- AUTHOR: jiarui 20180831
-- REMARK: 客户表增加相关字段
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `transaction_ratio` VARCHAR(64);

ALTER TABLE `eh_enterprise_customers` ADD COLUMN `expected_sign_date` DATETIME;
-- end

-- AUTHOR: jiarui  20180831
-- REMARK: 园区入驻字段及数据迁移
-- ALTER TABLE eh_lease_promotions MODIFY rent_amount VARCHAR(1024) ;
-- end

-- 黄鹏宇 2018-9-4
-- 日志表增加操作对象类型
ALTER TABLE `eh_customer_events` ADD COLUMN investment_type TINYINT COMMENT '操作客户类型，0-客户管理，1-租客管理';

-- REMARK: 客户表增加是否入驻状态
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `entry_status_item_id` BIGINT COMMENT '该用户是否入驻，1-未入驻，2-入驻';

-- end

-- AUTHOR: 杨崇鑫  20180920
-- REMARK: 账单表增加一个企业客户ID的字段
ALTER TABLE `eh_payment_bills` ADD COLUMN `customer_id` BIGINT COMMENT '企业客户ID';

-- END

-- AUTHOR:梁燕龙
-- REMARK: 通用表单打印表 issue-35063
CREATE TABLE `eh_general_form_print_templates`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
  `name` VARCHAR(64) NOT NULL COMMENT '表单打印模板名称',
  `last_commit` VARCHAR(40) COMMENT '最近一次提交ID',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT '表单打印模板所属ID',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '表单打印所属类型',
  `status` TINYINT DEFAULT 2 COMMENT '打印模板状态,0为失效，2为生效',
  `creator_uid` BIGINT COMMENT 'record creator user id',
  `create_time` DATETIME COMMENT '创建时间',
  `delete_uid` BIGINT COMMENT 'record deleter user id',
  `delete_time` DATETIME COMMENT '删除时间',
  `update_uid` BIGINT COMMENT 'record update user id',
  `update_time` DATETIME COMMENT '更新时间',

  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '通用表单打印模板表';
-- END ISSUE-35063

-- AUTHOR: 郑思挺  20180920
-- REMARK: 资源预约3.7
ALTER TABLE `eh_rentalv2_order_records`
ADD COLUMN `account_name`  varchar(255) NULL AFTER `account_id`;
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `account_name`  varchar(255) NULL AFTER `old_custom_object`;
-- END

-- AUTHOR: 缪洲
-- REMARK: 停车缴费V6.7，增加用户须知

ALTER TABLE `eh_parking_lots` ADD COLUMN `notice_contact` varchar(20) COMMENT '用户须知联系电话';
ALTER TABLE `eh_parking_lots` ADD COLUMN `summary` text COMMENT '用户须知';

-- AUTHOR: tangcen 2018年9月20日21:38:22
-- REMARK: 园区入驻，一键转为意向客户功能
ALTER TABLE `eh_enterprise_op_requests` ADD COLUMN `transform_flag`  tinyint NULL DEFAULT 0 COMMENT '是否转化为意向客户：0-否，1-是';
ALTER TABLE `eh_enterprise_op_requests` ADD COLUMN `customer_name`  varchar(255) NULL COMMENT '承租方';
-- END
-- AUTHOR:黄良铭
-- REMARK:  20180903-huangliangming-用户对接脚本方案-#36568
CREATE TABLE `eh_butt_script_config` (
  `id`  bigint(20)  NOT NULL COMMENT '主键',
  `info_type` varchar(64)  COMMENT '分类',
  `info_describe` varchar(128) COMMENT '描述',
  `namespace_id` int(11)  COMMENT '域空间ID',
  `module_id`  bigint(20) COMMENT  '这个没啥意思,自己定义,因为建库入参需要,应该是作区分用',
  `module_type` varchar(64)    COMMENT '这个没啥意思,自己定义,因为建库入参需要,应该是作区分用 ',
  `owner_id`  bigint(20)   ,
  `owner_type`  varchar(64)   ,
  `remark`  varchar(240)   COMMENT '备注',
  `status`  tinyint(4)    COMMENT '状态;0失效,1生效',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '存储创建GOGS仓库时所需的指定相关表';

CREATE TABLE `eh_butt_script_publish_info` (
  `id`  bigint(20)  NOT NULL COMMENT '主键',
  `info_type` varchar(64)  COMMENT '分类 ,对应 eh_butt_script_config 表',
  `namespace_id` int(11)  COMMENT '域空间ID',
  `commit_version`  varchar(64)  COMMENT  '版本号',
  `publish_time` datetime     COMMENT '版本发布 时间',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '版本信息及发布信息表';

CREATE TABLE `eh_butt_info_type_event_mapping` (
  `id`  bigint(20)  NOT NULL COMMENT '主键',
  `info_type` varchar(64)  COMMENT '分类 ,对应 eh_butt_script_config 表',
  `namespace_id` int(11)  COMMENT '域空间ID',
  `event_name`  varchar(128)  COMMENT  '触发该脚本的事件',
  `sync_flag`  tinyint(4)  COMMENT  '0 同步;1异步  同步执行还是异执行',
  `describe`  varchar(256)  COMMENT  '描述',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '脚本与事件映射表';

CREATE TABLE `eh_butt_script_last_commit` (
  `id`  BIGINT(20)  NOT NULL COMMENT '主键',
  `info_type` VARCHAR(64)  COMMENT '分类 ,对应 eh_butt_script_config 表',
  `namespace_id` INT(11)  COMMENT '域空间ID',
  `last_commit`  VARCHAR(128)  COMMENT  '最后一次提交版本号',
  `commit_msg`  VARCHAR(256)  COMMENT  '提交相关信息',
  `commit_time`  DATETIME    COMMENT  '提交时间',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'last_commit 存储表';
-- END

-- AUTHOR: 梁燕龙
-- REMARK: 用户增加会员等级
ALTER TABLE `eh_users` ADD COLUMN `vip_level` INTEGER COMMENT '会员等级';

-- AUTHOR: 马世亨
-- REMARK: visitorsys1.2 访客门禁授权默认方案表
CREATE TABLE `eh_visitor_sys_door_access` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0' COMMENT 'namespace id',
  `owner_type` varchar(64) NOT NULL COMMENT 'community or organization',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `door_access_id` bigint(20) NOT NULL COMMENT '门禁组Id',
  `door_access_name` varchar(256) DEFAULT NULL COMMENT '门禁组名称',
	`default_auth_duration_type` TINYINT(4) DEFAULT 0 COMMENT '默认访客授权有效期种类,0 天数，1 小时数',
  `default_auth_duration` int DEFAULT 0 COMMENT '默认访客授权有效期',
	`default_enable_auth_count` TINYINT(4) DEFAULT 0 COMMENT '默认访客授权次数开关 0 关 1 开',
  `default_auth_count` int DEFAULT 0 COMMENT '默认访客授权次数',
  `default_door_access_flag` tinyint(4) DEFAULT 0 COMMENT '默认门禁组 0 非默认 1 默认',
  `status` tinyint(4) DEFAULT '2' COMMENT '0:被删除状态,2:正常状态',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='访客门禁授权默认方案表';

-- AUTHOR: 马世亨
-- REMARK: visitorsys1.2 访客表修改
ALTER TABLE `eh_visitor_sys_visitors` ADD COLUMN `door_access_auth_duration_type` tinyint(4) NULL COMMENT '访客授权有效期种类,0 天数，1 小时数';
ALTER TABLE `eh_visitor_sys_visitors` ADD COLUMN `door_access_auth_duration` int NULL COMMENT '访客授权有效期';
ALTER TABLE `eh_visitor_sys_visitors` ADD COLUMN `door_access_enable_auth_count` TINYINT(4) DEFAULT 0 COMMENT '访客授权次数开关 0 关 1 开';
ALTER TABLE `eh_visitor_sys_visitors` ADD COLUMN `door_access_auth_count` int NULL COMMENT '访客授权次数';

-- AUTHOR:tangcen
-- REMARK:招商广告表
CREATE TABLE `eh_investment_advertisements` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT NULL,
  `community_id` bigint(20) DEFAULT NULL,
  `owner_type` varchar(64) DEFAULT NULL COMMENT '默认为EhOrganizations',
  `owner_id` bigint(20) DEFAULT NULL COMMENT '默认为organizationId',
  `title` varchar(255) DEFAULT NULL COMMENT '广告主题',
  `investment_type` tinyint(4) DEFAULT NULL COMMENT '广告类型 : 1-招租广告，2-招商广告',
  `investment_status` tinyint(4) DEFAULT NULL COMMENT '招商状态 : 1-招商中，2-已下线，3-已出租',
  `available_area_min` decimal(10,2) DEFAULT NULL COMMENT '招商面积起点',
  `available_area_max` decimal(10,2) DEFAULT NULL COMMENT '招商面积终点',
  `asset_price_min` decimal(10,2) DEFAULT NULL COMMENT '招商价格起点',
  `asset_price_max` decimal(10,2) DEFAULT NULL COMMENT '招商价格终点',
  `price_unit` tinyint(4) DEFAULT NULL COMMENT '价格单位：1-元/平*月',
  `apartment_floor_min` int(11) DEFAULT NULL COMMENT '招商楼层起点',
  `apartment_floor_max` int(11) DEFAULT NULL COMMENT '招商楼层终点',
  `orientation` varchar(64) DEFAULT NULL COMMENT '朝向',
  `address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `longitude` double DEFAULT NULL COMMENT '经度',
  `latitude` double DEFAULT NULL COMMENT '纬度',
  `geohash` varchar(32) DEFAULT NULL COMMENT 'geohash值，用于GPS定位',
  `contact_name` varchar(128) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(128) DEFAULT NULL COMMENT '联系电话',
  `description` text COMMENT '广告内容描述',
  `poster_uri` varchar(256) DEFAULT NULL COMMENT '封面图uri',
  `asset_dispaly_flag` tinyint(4) DEFAULT NULL COMMENT '是否显示楼宇房源：0-否，1-是',
  `custom_form_flag` tinyint(4) DEFAULT NULL COMMENT '是否添加自定义表单：0-否，1-是',
  `general_form_id` bigint(20) DEFAULT NULL COMMENT '关联的自定义表单id',
  `default_order` bigint(20) DEFAULT NULL COMMENT '排序字段（初始值等于主键id）',
  `status` tinyint(4) DEFAULT '2' COMMENT '该条的记录状态：0: inactive, 1: confirming, 2: active',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `operator_uid` bigint(20) DEFAULT NULL COMMENT '更新人',
  `operate_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='招商广告表';

-- AUTHOR:tangcen
-- REMARK:招商广告轮播图表
CREATE TABLE `eh_investment_advertisement_banners` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT NULL,
  `advertisement_id` bigint(20) DEFAULT NULL COMMENT '关联的广告id',
  `content_uri` varchar(256) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '2' COMMENT '该条的记录状态：0: inactive, 1: confirming, 2: active',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='招商广告轮播图表';

-- AUTHOR:tangcen
-- REMARK:招商广告关联资产表
CREATE TABLE `eh_investment_advertisement_assets` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT NULL,
  `advertisement_id` bigint(20) DEFAULT NULL COMMENT '关联的广告id',
  `asset_type` tinyint(4) DEFAULT NULL COMMENT '关联的资产类型 : 1-community,2-building,3-apartment',
  `asset_id` bigint(20) DEFAULT NULL COMMENT '关联的资产id',
  `status` tinyint(4) DEFAULT '2' COMMENT '该条的记录状态：0- inactive, 1- confirming, 2- active',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='招商广告关联资产表';

-- AUTHOR:tangcen
-- REMARK:自定义表单添加字段
ALTER TABLE `eh_general_form_val_requests` ADD COLUMN `integral_tag1` bigint(20) NULL DEFAULT 0 COMMENT '业务字段（用于表示招商租赁的预约申请记录状态）';
ALTER TABLE `eh_general_form_val_requests` ADD COLUMN `integral_tag2` bigint(20) NULL DEFAULT NULL COMMENT '业务字段（用于表示招商租赁的预约记录的来源广告的id）';

-- END


-- AUTHOR: 黄鹏宇
-- REMARK: 增加表单的创建时间
ALTER TABLE `eh_general_form_val_requests` ADD COLUMN `created_time` DATE NULL DEFAULT 0 COMMENT '创建时间';
ALTER TABLE `eh_general_form_val_requests` ADD COLUMN `creator_uid` BIGINT NULL DEFAULT 0 COMMENT '创建人ID';
ALTER TABLE `eh_general_form_val_requests` ADD COLUMN `operator_time` DATE NULL DEFAULT 0 COMMENT '操作时间';
ALTER TABLE `eh_general_form_val_requests` ADD COLUMN `operator_uid` BIGINT NULL DEFAULT 0 COMMENT '操作人ID';

-- AUTHOR: 李清岩
-- REMARK: 20180930 issue-38336
ALTER TABLE `eh_door_access` ADD COLUMN `firmware_id` bigint(20) DEFAULT NULL COMMENT '门禁设备固件版本id';
ALTER TABLE `eh_door_access` ADD COLUMN `firmware_name` VARCHAR (128) DEFAULT NULL COMMENT '门禁设备固件名';
ALTER TABLE `eh_door_access` ADD COLUMN `device_id` bigint(20) DEFAULT NULL COMMENT '门禁设备类型id';
ALTER TABLE `eh_door_access` ADD COLUMN `device_name` VARCHAR(128) DEFAULT NULL COMMENT '门禁设备固件名';
ALTER TABLE `eh_door_access` ADD COLUMN `city_id` bigint(20) DEFAULT NULL COMMENT '城市id';
ALTER TABLE `eh_door_access` ADD COLUMN `province` VARCHAR(64) DEFAULT NULL COMMENT '省份名';

CREATE TABLE `eh_aclink_device` (
	`id` bigint(20),
	`name` VARCHAR(128) COMMENT '设备类型名称',
	`type` TINYINT(4) DEFAULT NULL COMMENT '设备类型 0：自有设备 1：第三方设备',
	`description` VARCHAR(1024) DEFAULT NULL COMMENT '设备特性',
	`support_bt` TINYINT(4) DEFAULT NULL COMMENT '蓝牙开门 0：不支持 1：支持',
	`support_qr` TINYINT(4) DEFAULT NULL COMMENT '二维码开门 0：不支持 1：支持',
	`support_face` TINYINT(4) DEFAULT NULL COMMENT '人脸识别开门 0：不支持 1：支持',
	`support_tempauth` TINYINT(4) DEFAULT NULL COMMENT '临时授权 0：不支持 1：支持',
	`firmware` VARCHAR(128) DEFAULT NULL COMMENT '固件名称',
	`firmware_id` bigint(20),
	`update` TINYINT(4) DEFAULT NULL COMMENT '默认升级 0：不支持 1：支持',
	`create_time` datetime DEFAULT NULL COMMENT '创建时间',
	`status` tinyint(4) DEFAULT 1 COMMENT '状态 0：失效 1：有效',
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门禁设备类型';

CREATE TABLE `eh_aclink_firmware_new` (
  `id` bigint(20),
	`name` VARCHAR(128) COMMENT '固件名称',
	`version` VARCHAR(128) COMMENT '版本号，例如1.0.0',
	`number` int(11) DEFAULT NULL COMMENT '固件编号',
	`description` VARCHAR(1024) DEFAULT NULL,
  `bluetooth_name` VARCHAR(128) DEFAULT NULL COMMENT '蓝牙名称' ,
	`bluetooth_id` bigint(20),
  `wifi_name` VARCHAR(128) DEFAULT NULL COMMENT 'wifi名称' ,
	`wifi_id` bigint(20),
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
	`status` tinyint(4) DEFAULT NULL COMMENT '状态 0：失效 1：有效',
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门禁固件新表';

CREATE TABLE `eh_aclink_firmware_package` (
  `id` bigint(20),
	`name` VARCHAR(128) COMMENT '程序名称',
  `type` TINYINT(4) DEFAULT NULL COMMENT '程序类型 0：蓝牙 1：wifi',
	`size` int(11),
  `download_url` varchar(1024) DEFAULT NULL COMMENT '存储地址' ,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
	`status` tinyint(4) DEFAULT NULL COMMENT '状态 0：失效 1：有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门禁固件程序表';



=======
-- AUTHOR: 刘一麟 2018年8月23日
-- REMARK:issue-36233 门禁3.0.2

ALTER TABLE `eh_aclink_cameras` MODIFY COLUMN `door_access_id` BIGINT NULL DEFAULT NULL;
ALTER TABLE `eh_aclink_cameras` ADD COLUMN `owner_id` BIGINT NOT NULL DEFAULT '0' COMMENT '所属组织id' AFTER `server_id`;
ALTER TABLE `eh_aclink_cameras` ADD COLUMN `owner_type` TINYINT NOT NULL DEFAULT '0' COMMENT '所属组织类型' AFTER `server_id`;
ALTER TABLE `eh_aclink_cameras` ADD COLUMN `namespace_id` BIGINT NOT NULL DEFAULT '0' COMMENT '域空间id' AFTER `id`;
ALTER TABLE `eh_aclink_ipads` MODIFY COLUMN `door_access_id` BIGINT NULL DEFAULT NULL;
ALTER TABLE `eh_aclink_ipads` ADD COLUMN `owner_id` BIGINT NOT NULL DEFAULT '0' COMMENT '所属组织id' AFTER `server_id`;
ALTER TABLE `eh_aclink_ipads` ADD COLUMN `owner_type` TINYINT NOT NULL DEFAULT '0' COMMENT '所属组织类型' AFTER `server_id`;
ALTER TABLE `eh_aclink_ipads` ADD COLUMN `namespace_id` BIGINT NOT NULL DEFAULT '0' COMMENT '域空间id' AFTER `id`;
ALTER TABLE `eh_door_access` ADD COLUMN `firmware_version` VARCHAR(64) NULL DEFAULT NULL COMMENT '门禁固件版本' AFTER `id`;

CREATE TABLE `eh_aclink_form_titles` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`owner_id`  bigint(20) NULL COMMENT '所属对象id',
`owner_type`  tinyint(4) NULL COMMENT '所属对象类型 0园区 1公司 2家庭 3门禁',
`path` varchar(1024) DEFAULT NULL COMMENT '记录更新人userId',
`name` varchar(64) NULL COMMENT '表单项名称',
`item_type` tinyint(4) NULL COMMENT '表单项类型, 0 表单中间结点 1 文本 2 单选 3 多选',
`status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0已删除1有效',
`creator_uid` bigint(20) NOT NULL COMMENT '记录创建人userId',
`create_time` datetime NOT NULL COMMENT '记录创建时间',
`operator_uid` bigint(20) DEFAULT NULL COMMENT '记录更新人userId',
`operate_time` datetime DEFAULT NULL COMMENT '记录更新时间',
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '门禁表单 标题';

CREATE TABLE `eh_aclink_form_values` (
`id` bigint(20) NOT NULL ,
`namespace_id` int(11) NOT NULL ,
`title_id` bigint(20) NOT NULL COMMENT '对应表单标题的id',
`value` varchar(1024) NULL COMMENT '表单项的值',
`type` tinyint(4) NULL COMMENT '值类型, 0 初始值(select,checkbox等) 1 默认值 2 输入值',
`status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0已删除1有效',
`owner_id` bigint(20) NOT NULL COMMENT '记录所属对象Id',
`owner_type` tinyint(4) NOT NULL COMMENT '记录所属对象类型 0园区 1公司 2家庭 3门禁 4用户 5授权记录',
`creator_uid` bigint(20) NOT NULL COMMENT '记录创建人userId',
`create_time` datetime NOT NULL COMMENT '记录创建时间',
`operator_uid` bigint(20) DEFAULT NULL COMMENT '记录更新人userId',
`operate_time` datetime DEFAULT NULL COMMENT '记录更新时间',
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '门禁表单 输入值';

CREATE TABLE `eh_aclink_group` (
`id` bigint(20) NOT NULL ,
`namespace_id` int(11) NOT NULL ,
`name` varchar(1024) NULL COMMENT '门禁组名称',
`status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0已删除1有效',
`owner_id` bigint(20) NOT NULL COMMENT '记录所属对象Id',
`owner_type` tinyint(4) NOT NULL COMMENT '记录所属对象类型 0园区 1公司 2家庭 3门禁 4用户 5授权记录',
`creator_uid` bigint(20) NOT NULL COMMENT '记录创建人userId',
`create_time` datetime NOT NULL COMMENT '记录创建时间',
`operator_uid` bigint(20) DEFAULT NULL COMMENT '记录更新人userId',
`operate_time` datetime DEFAULT NULL COMMENT '记录更新时间',
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '门禁组';

ALTER TABLE `eh_door_auth` ADD COLUMN `licensee_type` TINYINT NULL DEFAULT 0 COMMENT '被授权对象的类型 0用户 1组织架构节点 2项目(公司) 3楼栋(公司) 4楼层(公司) 5项目(家庭) 6楼栋(家庭) 7楼层(家庭)' AFTER `user_id`;
ALTER TABLE `eh_door_auth` ADD COLUMN `group_type` TINYINT NULL DEFAULT 0 COMMENT '门禁集合的类型 0 单个门禁 1 新门禁组(门禁3.0) ' AFTER `user_id`;

ALTER TABLE `eh_door_access` ADD COLUMN `adress_detail` varchar(64) NULL COMMENT '办公地点/楼栋_楼层' AFTER `address`;
-- END issue-36233
)

