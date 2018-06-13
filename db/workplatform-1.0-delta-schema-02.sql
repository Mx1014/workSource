-- 1: 同步客户
-- /customer/syncEnterpriseCustomerIndex


-- Designer: wuhan
-- Description: ISSUE#25515: 薪酬V2.2（工资条发放管理；app支持工资条查看/确认）

CREATE TABLE `eh_salary_payslips` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INT ,
  `owner_type` VARCHAR(32)  COMMENT 'organization',
  `owner_id` BIGINT  COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT  COMMENT '属于哪一个总公司的',
  `salary_period` VARCHAR(12) COMMENT 'example:201705',
  `name` VARCHAR(1024) NOT NULL COMMENT '工资表名称',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `update_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY(`id`),
  KEY `i_eh_owner_period` (`owner_id`,`salary_period`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '工资条表';


CREATE TABLE `eh_salary_payslip_details` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `payslip_id` BIGINT NOT NULL COMMENT '父键;工资条id',
  `namespace_id` INT,
  `salary_period` VARCHAR(12) COMMENT 'example:201705',
  `owner_type` VARCHAR(32)  COMMENT 'organization',
  `owner_id` BIGINT  COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT  COMMENT '属于哪一个总公司的',
  `user_id` BIGINT,
  `user_detail_id` BIGINT,
  `name` VARCHAR(512) NOT NULL COMMENT '姓名',
  `user_contact` VARCHAR(20) NOT NULL COMMENT '手机号',
  `payslip_content` TEXT  COMMENT '导入的工资条数据(key-value对的json字符串)',
  `viewed_flag` TINYINT COMMENT '已查看0-否 1-是',
  `status` TINYINT COMMENT '状态0-已发送 1-已撤回  2-已确认',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `update_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY(`id`),
  KEY `i_eh_payslip_id` (`payslip_id`),
  KEY `i_eh_organization_user` (`user_id`,`organization_id`),
  KEY `i_eh_create_time`(`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '工资条详情表';
-- 薪酬2.2 end

-- app配置 1.2.1 start  add by yanjun 201805241019
ALTER TABLE `eh_portal_versions` ADD COLUMN `preview_count`  int(11) NULL DEFAULT 0 COMMENT '预览版本发布次数';

-- app配置 1.2.1 end  add by yanjun


-- 任务中心添加执行开始时间和上传开始时间  add by yanjun 201805241345
ALTER TABLE `eh_tasks` ADD COLUMN `execute_start_time`  datetime NULL;
ALTER TABLE `eh_tasks` ADD COLUMN `upload_file_start_time`  datetime NULL;
ALTER TABLE `eh_tasks` ADD COLUMN `upload_file_finish_time`  datetime NULL;



-- 资源预约 订单资源表增加字段
ALTER TABLE `eh_rentalv2_resource_orders` ADD COLUMN `resource_number`  VARCHAR(64) NULL;


-- ------------------------------
-- 工作流动态函数     add by xq.tian  2018/04/24
-- ------------------------------
DROP TABLE IF EXISTS `eh_flow_scripts`; -- 原来存在这张表，没有数据，删掉重新建
CREATE TABLE `eh_flow_scripts` (
	`id` BIGINT NOT NULL,
	`namespace_id` INTEGER NOT NULL DEFAULT '0',

	`module_type` VARCHAR(64) NOT NULL,
	`module_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the module id',

	`owner_type` VARCHAR(64),
	`owner_id` BIGINT NOT NULL DEFAULT 0,

	`script_category` VARCHAR(64) NOT NULL COMMENT 'system_script, user_script',
	`script_type` VARCHAR(64) NOT NULL COMMENT 'javascript, groovy, java and other',

	`script_main_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'ref eh_flow_scripts',
	`script_version` INTEGER NOT NULL DEFAULT '0' COMMENT 'script version',

	`name` VARCHAR(128) DEFAULT NULL COMMENT 'script name',
	`description` TEXT DEFAULT NULL COMMENT 'script description',
	`script` LONGTEXT DEFAULT NULL COMMENT 'script content',

	`status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: invalid, 1: valid',
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,

	`string_tag1` VARCHAR(128) DEFAULT NULL,
	`string_tag2` VARCHAR(128) DEFAULT NULL,
	`string_tag3` VARCHAR(128) DEFAULT NULL,
	`string_tag4` VARCHAR(128) DEFAULT NULL,
	`string_tag5` VARCHAR(128) DEFAULT NULL,
	`integral_tag1` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag2` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag3` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag4` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag5` BIGINT(20) NOT NULL DEFAULT '0',
	PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'flow scripts in dev mode';

-- ------------------------------
-- 工作流动态函数配置表     add by xq.tian  2018/04/24
-- ------------------------------
-- DROP TABLE IF EXISTS `eh_flow_script_configs`;
CREATE TABLE `eh_flow_script_configs` (
	`id` BIGINT NOT NULL,
	`namespace_id` INTEGER NOT NULL DEFAULT '0',

	`module_type` VARCHAR(64) NOT NULL,
	`module_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the module id',

	`flow_main_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the module id',
	`flow_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'flow version',

	`owner_type` VARCHAR(64),
	`owner_id` BIGINT NOT NULL DEFAULT 0,

	`script_type` VARCHAR(64) NOT NULL COMMENT 'javascript, groovy, java and other',

	`script_name` VARCHAR(128) NULL DEFAULT NULL COMMENT 'export script name, only for script type of java',
	`script_main_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'ref eh_flow_scripts',
	`script_version` INTEGER NOT NULL DEFAULT '0' COMMENT 'script version',

	`field_name` VARCHAR(1024) DEFAULT NULL COMMENT 'field name',
	`field_desc` TEXT DEFAULT NULL COMMENT 'field description',
	`field_value` VARCHAR(1024) DEFAULT NULL COMMENT 'field value',

	`status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: invalid, 1: valid',
	`create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,

	`string_tag1` VARCHAR(128) DEFAULT NULL,
	`string_tag2` VARCHAR(128) DEFAULT NULL,
	`string_tag3` VARCHAR(128) DEFAULT NULL,
	`string_tag4` VARCHAR(128) DEFAULT NULL,
	`string_tag5` VARCHAR(128) DEFAULT NULL,
	`integral_tag1` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag2` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag3` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag4` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag5` BIGINT(20) NOT NULL DEFAULT '0',
	PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'flow scripts config in dev mode';

ALTER TABLE eh_flow_evaluate_items ADD COLUMN flow_case_id BIGINT;

ALTER TABLE eh_flow_actions ADD COLUMN script_type VARCHAR(64);
ALTER TABLE eh_flow_actions ADD COLUMN script_id BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_flow_actions ADD COLUMN script_version INTEGER NOT NULL DEFAULT 0;

ALTER TABLE eh_flow_evaluate_items ADD COLUMN `string_tag6` VARCHAR(128) DEFAULT NULL;
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `string_tag7` VARCHAR(128) DEFAULT NULL;
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `string_tag8` VARCHAR(128) DEFAULT NULL;
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `string_tag9` VARCHAR(128) DEFAULT NULL;
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `string_tag10` VARCHAR(128) DEFAULT NULL;

ALTER TABLE eh_flow_evaluate_items ADD COLUMN `integral_tag6` BIGINT(20) NOT NULL DEFAULT '0';
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `integral_tag7` BIGINT(20) NOT NULL DEFAULT '0';
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `integral_tag8` BIGINT(20) NOT NULL DEFAULT '0';
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `integral_tag9` BIGINT(20) NOT NULL DEFAULT '0';
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `integral_tag10` BIGINT(20) NOT NULL DEFAULT '0';

ALTER TABLE eh_flow_cases ADD COLUMN path VARCHAR(1024) COMMENT 'flow case path';

ALTER TABLE eh_flow_actions CHANGE COLUMN script_id script_main_id BIGINT NOT NULL DEFAULT 0;


-- 政务服务 1.0
-- by shiheng.ma
-- 政策表
CREATE TABLE `eh_policies` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `category_id` bigint(20),
	`title` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '',
	`outline` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '',
	`content` text COMMENT 'content data',
	`priority` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the rank of policy',
	`creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
	`updater_uid` bigint(20) NOT NULL DEFAULT '0',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 政策类型中间表
CREATE TABLE `eh_policy_categories` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `policy_id` bigint(20) NOT NULL COMMENT 'id of the policy',
  `category_id` bigint(20) NOT NULL COMMENT 'category of policy',
	`active_flag` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 政策查询记录表
CREATE TABLE `eh_policy_records` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `category_id` bigint(20),
	`creator_id` bigint(20) NOT NULL COMMENT '',
	`creator_name` varchar(128) NOT NULL COMMENT '',
	`creator_phone` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '',
	`creator_org_id` bigint(20) NOT NULL COMMENT '',
	`creator_org_name` varchar(128) NOT NULL COMMENT '',
	`turnover` varchar(60) NOT NULL DEFAULT '' COMMENT '营业额',
	`tax` varchar(60) NOT NULL DEFAULT '' COMMENT '纳税总额',
	`qualification` varchar(60) NOT NULL DEFAULT '' COMMENT '单位资质',
	`financing` varchar(60) NOT NULL DEFAULT '' COMMENT 'A轮融资',
	`create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 政策代办配置表
CREATE TABLE `eh_policy_agent_rules` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'id',
	`namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type : community ; organization',
  `owner_id` bigint(20) DEFAULT NULL COMMENT 'community id or organization id',
  `agent_flag` TINYINT(4) DEFAULT NULL COMMENT '是否代办:0为不可代办，1为可代办',
  `agent_phone` varchar(64) DEFAULT NULL COMMENT '联系方式',
	`agent_info` text DEFAULT NULL COMMENT '代办介绍',
	`creator_id` bigint(20) NOT NULL COMMENT '创建人',
	`create_time` datetime DEFAULT NULL COMMENT '创建时间',
	`updater_uid` bigint(20) COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- 政务服务 1.0 end

-- 企业管理员列表 by jiarui
ALTER TABLE `eh_enterprise_customer_admins` ADD COLUMN `namespace_id`  int NOT NULL DEFAULT 0 AFTER `create_time`;



-- ------------------------------
-- 服务联盟V3.3（新增需求提单功能）
-- 产品功能 #26469 add by huangmingbo  2018/05/29
-- ------------------------------
-- DROP TABLE IF EXISTS `eh_flow_script_configs`;
CREATE TABLE `eh_service_alliance_providers` (
	`id` BIGINT(20) NOT NULL,
	`namespace_id` INT(11) NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '\'\'',
	`owner_id` BIGINT(20) NOT NULL DEFAULT '0',
	`app_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'module_app id, new type of alliance, represent one kine fo alliance',
	`type` BIGINT(20) NOT NULL COMMENT 'old type of Alliance，represent one kind of alliance',
	`name` VARCHAR(50) NOT NULL COMMENT 'provider name',
	`category_id` BIGINT(20) NOT NULL COMMENT '见 categories表',
	`mail` VARCHAR(50) NOT NULL COMMENT 'enterprise mail',
	`contact_number` VARCHAR(50) NOT NULL COMMENT 'mobile or contact phone',
	`contact_name` VARCHAR(50) NOT NULL COMMENT 'contact name',
	`total_score` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'total score',
	`score_times` INT(11) NOT NULL DEFAULT '0' COMMENT 'the num of times make the score',
	`score_flow_case_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'the final flow case id that make score',
	`status` TINYINT(4) NOT NULL DEFAULT '1' COMMENT '0-deleted 1-active',
	`create_time` DATETIME NOT NULL,
	`create_uid` BIGINT(20) NOT NULL COMMENT 'create user id',
	PRIMARY KEY (`id`)
)
COMMENT='服务商信息'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `eh_alliance_extra_events` (
	`id` BIGINT(20) NOT NULL,
	`flow_case_id` BIGINT(20) NOT NULL,
	`topic` VARCHAR(200) NOT NULL COMMENT 'topic of current event',
	`time` DATETIME NOT NULL COMMENT 'the time that event happen',
	`address` VARCHAR(200) NULL DEFAULT NULL,
	`provider_id` BIGINT(20) NULL DEFAULT NULL COMMENT 'id of alliance_providers',
	`provider_name` VARCHAR(50) NULL DEFAULT NULL COMMENT 'name of alliance_provider',
	`members` VARCHAR(500) NOT NULL COMMENT 'those who participate in',
	`content` MEDIUMTEXT NOT NULL COMMENT 'main body',
	`enable_read` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '0-hide for applier  1-show for applier',
	`enable_notify_by_email` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '0-not send email  1-send email to provider',
	`create_time` DATETIME NOT NULL,
	`create_uid` BIGINT(20) NOT NULL,
	PRIMARY KEY (`id`)
)
COMMENT='工作流中，新建事件表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `eh_alliance_extra_event_attachment` (
	`id` BIGINT(20) NOT NULL,
	`owner_id` BIGINT(20) NOT NULL COMMENT 'the id of eh_alliance_extra_events',
	`file_type` VARCHAR(32) NULL DEFAULT NULL COMMENT 'like image,jpg. in lower case',
	`file_uri` VARCHAR(1024) NOT NULL COMMENT 'like cs://1/...',
	`file_name` VARCHAR(200) NULL DEFAULT NULL,
	`file_size` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'file size (Byte)',
	`create_uid` BIGINT(20) NOT NULL,
	`create_time` DATETIME NOT NULL COMMENT 'create time',
	PRIMARY KEY (`id`)
)
COMMENT='用于服务联盟工作流中新建事件时保存附件使用'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


ALTER TABLE `eh_service_alliance_jump_module`
	ADD COLUMN `module_id` BIGINT NOT NULL DEFAULT '0' AFTER `module_name`;


ALTER TABLE `eh_service_alliance_jump_module`
	ADD COLUMN `instance_config` TEXT NULL DEFAULT NULL AFTER `module_url`;

-- 超级管理员 Added by janson
ALTER TABLE `eh_organizations` ADD COLUMN `admin_target_id`  bigint(20) NULL ;
-- 能耗抄表精度  by jiarui
ALTER TABLE `eh_energy_meter_reading_logs`
	MODIFY COLUMN `reading`  decimal(10,2) NULL DEFAULT NULL AFTER `meter_id`;
ALTER TABLE `eh_energy_meter_tasks`
	MODIFY COLUMN `last_task_reading`  decimal(10,2) NULL DEFAULT NULL AFTER `executive_expire_time`;
ALTER TABLE `eh_energy_meter_tasks`
	MODIFY COLUMN `reading`  decimal(10,2) NULL DEFAULT NULL AFTER `last_task_reading`;

-- added a new column for eh_payment_bill_groups by wentian
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `bills_day_type` TINYINT NOT NULL DEFAULT 4 COMMENT '1. 本周期前几日；2.本周期第几日；3.本周期结束日；4.下周期首月第几日';

-- ISSUE#26184 门禁人脸识别 by liuyilin 201180524
-- 内网服务器表创建
CREATE TABLE `eh_aclink_servers` (
    `id` BIGINT(20) NOT NULL,
    `name` VARCHAR(32) DEFAULT NULL,
	`namespace_id` INT(11) NOT NULL DEFAULT '0',
	`uuid` VARCHAR(6) NOT NULL COMMENT '配对码',
    `ip_address` VARCHAR(128) DEFAULT NULL COMMENT 'IP地址',
	`link_status` TINYINT NOT NULL DEFAULT '0' COMMENT '联网状态',
	`active_time` DATETIME DEFAULT NULL COMMENT '激活时间',
	`sync_time`  DATETIME DEFAULT NULL COMMENT '上次同步时间',
	`version` VARCHAR(8) DEFAULT NULL COMMENT '版本号',
	`owner_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '组织id',
	`owner_type` TINYINT NOT NULL DEFAULT '0' COMMENT '组织类型',
	`aes_server_key` VARCHAR(64) COMMENT 'AES公钥',
	`status` TINYINT NOT NULL DEFAULT '0' COMMENT '激活状态0未激活1已激活2已删除',
	`creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
	`create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
	`operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
	`operate_time` DATETIME NULL COMMENT '记录更新时间' ,
	PRIMARY KEY (`id`),
	UNIQUE `u_eh_aclink_servers_uuid`(`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '内网服务器表';

-- 内网ipad表创建
CREATE TABLE `eh_aclink_ipads` (
	`id` BIGINT(20) NOT NULL,
	`name` VARCHAR(32) DEFAULT NULL,
	`door_access_id` BIGINT(20) NOT NULL COMMENT '关联门禁id',
	`enter_status` TINYINT DEFAULT '0' COMMENT '进出标识 1进0出',
	`uuid` VARCHAR(6) NOT NULL COMMENT '配对码',
	`link_status` TINYINT NOT NULL DEFAULT '0' COMMENT '联网状态',
	`server_id` BIGINT(20) COMMENT '服务器id',
	`active_time` DATETIME DEFAULT NULL COMMENT '激活时间',
	`status` TINYINT NOT NULL DEFAULT '0' COMMENT '激活状态0未激活1已激活2已删除',
	`creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
	`create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
	`operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
	`operate_time` DATETIME NULL COMMENT '记录更新时间' ,
	PRIMARY KEY (`id`),
	UNIQUE `u_eh_aclink_ipads_uuid`(`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '内网ipad表';

-- 内网摄像头表创建
CREATE TABLE `eh_aclink_cameras` (
	`id` BIGINT(20) NOT NULL,
	`name` VARCHAR(32) DEFAULT NULL,
	`door_access_id` BIGINT NOT NULL COMMENT '关联门禁id',
	`enter_status` TINYINT DEFAULT '0' COMMENT '进出标识 1进0出',
	`link_status` TINYINT NOT NULL DEFAULT '0' COMMENT '联网状态',
	`ip_address` VARCHAR(128) DEFAULT NULL COMMENT 'IP地址',
	`server_id` BIGINT(20) COMMENT '服务器id',
	`status` TINYINT NOT NULL DEFAULT '1' COMMENT '状态1正常2已删除',
	`creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
	`create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
	`operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
	`operate_time` DATETIME NULL COMMENT '记录更新时间' ,
	`key_code` VARCHAR(128) NOT NULL COMMENT '摄像头密钥' ,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT = '内网摄像头表';

-- 门禁与服务器多对一关联
ALTER TABLE `eh_door_access`
ADD `local_server_id` BIGINT(20) COMMENT '服务器id';

-- 人脸识别照片表创建
CREATE TABLE `eh_face_recognition_photos` (
	`id` BIGINT(20) NOT NULL,
    `user_id` BIGINT(20) COMMENT '用户id(正式用户)',
	`auth_id` BIGINT(20) COMMENT '授权id(访客)',
	`user_type` TINYINT NOT NULL DEFAULT '0' COMMENT '照片关联的用户类型,0正式用户,1访客',
	`img_uri` VARCHAR(2048) COMMENT '照片uri',
	`img_url` VARCHAR(2048) NOT NULL COMMENT '照片url',
	`sync_time` DATETIME COMMENT '上次同步时间时间' ,
	`status` TINYINT NOT NULL DEFAULT '1' COMMENT '状态1正常2已删除',
	`creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
	`create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
	`operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
	`operate_time` DATETIME NULL COMMENT '记录更新时间' ,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT = '人脸识别照片表';

-- 门禁版本包url长度限制修改
ALTER TABLE eh_aclink_firmware MODIFY COLUMN `download_url` VARCHAR(1024);
ALTER TABLE eh_aclink_firmware MODIFY COLUMN `info_url` VARCHAR(1024);

-- issue28839允许修改门禁开门方式,增加字段表示是否支持二维码
ALTER TABLE `eh_door_access` ADD COLUMN `has_qr` TINYINT NOT NULL DEFAULT '1' COMMENT '门禁二维码能力0无1有';

-- End by: yilin Liu


-- 大沙河梯控
-- by shiheng.ma
-- 新增字段 第三方KeyU字段
ALTER TABLE `eh_door_auth`
ADD COLUMN `key_u` VARCHAR(16) NULL DEFAULT NULL COMMENT '第三方用户秘钥' AFTER `right_remote`;

-- 新增字段 授权楼层
ALTER TABLE `eh_door_access`
ADD COLUMN `floor_id` VARCHAR(2000) NULL DEFAULT NULL COMMENT '授权楼层' AFTER `groupId`;
-- 大沙河梯控 end

-- 唐岑
ALTER TABLE `eh_contract_param_group_map` ADD COLUMN `user_id` BIGINT DEFAULT 0 COMMENT '用户id';

-- 给eh_payment_bills表添加支付方式、留言、账单是否有缴费凭证的标志字段  by Steve Tang
ALTER TABLE `eh_payment_bills` ADD COLUMN `payment_type` int DEFAULT null COMMENT '账单的支付方式（0-线下缴费，1-微信支付，2-对公转账，8-支付宝支付）';

ALTER TABLE `eh_payment_bills` ADD COLUMN `certificate_note` varchar(255) DEFAULT NULL COMMENT '上传凭证图片时附加的留言';

ALTER TABLE `eh_payment_bills` ADD COLUMN `is_upload_certificate` tinyint(4) DEFAULT null COMMENT '该账单是否上传了缴费凭证（0:否，1：是）';

-- 创建缴费凭证表
DROP TABLE IF EXISTS `eh_payment_bill_certificate`;

CREATE TABLE `eh_payment_bill_certificate` (
  `id` bigint(20) NOT NULL,
  `bill_id` bigint(20) NOT NULL COMMENT '该凭证记录对应的账单id',
  `certificate_uri` varchar(255) DEFAULT NULL COMMENT '上传凭证图片的uri',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 访客管理1.0start
-- 访客管理预约/访客表 , add by dengs, 20180425
-- DROP TABLE IF EXISTS `eh_visitor_sys_visitors`;
CREATE TABLE `eh_visitor_sys_visitors` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父id,没有则为0，有则为父预约id，一般用于园区访客下访问公司预约',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `enterprise_id` BIGINT COMMENT '到访公司id',
  `enterprise_name` VARCHAR(256)  COMMENT '到访公司名称,园区则无',
-- 以下字段必须出现在展示和搜索中，所以必须提出来作为字段
  `visitor_name` VARCHAR(256) COMMENT '访客姓名',
  `visitor_phone` VARCHAR(32) COMMENT '访客电话',
  `visitor_type` TINYINT COMMENT '访客类型,0,临时访客；1,预约访客',
  `visitor_sign_uri` VARCHAR(1024) COMMENT '访客签名图片uri（ipad签名）',
  `visitor_sign_character` VARCHAR(1024) COMMENT '访客签名字符（客户端使用），也是自助签名姓名',
  `visitor_pic_uri` VARCHAR(1024) COMMENT '访客头像图片uri（ipad上传访客照片）',
  `door_guard_id` VARCHAR(1024) COMMENT '门禁二维码对应门禁id',
  `door_guard_qrcode` VARCHAR(1024) COMMENT '门禁二维码字符串',
  `door_guard_end_time` DATETIME COMMENT '门禁二维码失效时间',
  `inviter_id` BIGINT COMMENT '邀请者的用户id',
  `inviter_name` VARCHAR(256) COMMENT '邀请者的用户姓名',
  `planned_visit_time` DATETIME COMMENT '计划到访时间',
  `visit_time` DATETIME COMMENT '实际到访时间',
  `visit_status` TINYINT COMMENT '访客状态，0，已删除; 1，未到访;2，等待确认; 3，已到访; 4，已拒绝; ',
  `booking_status` TINYINT COMMENT '预约状态，0，已删除; 1，未到访;2，等待确认; 3，已到访;',
  `send_message_inviter_flag` TINYINT COMMENT '确认到访时是否发送消息给邀请人，0，不发送; 1，发送',
  `send_sms_Flag` TINYINT COMMENT '是否发送访客邀请函给邀请人，0，不发送; 1，发送',
  `office_location_id` BIGINT COMMENT '办公地点ID',
  `office_location_name` VARCHAR(512) COMMENT '办公地点名称',
  `visit_reason_id` BIGINT COMMENT '到访是由Id',
  `visit_reason` VARCHAR(512) COMMENT '到访是由',
  `follow_up_numbers` BIGINT COMMENT '随访人员数量',
  `invitation_no` VARCHAR(32) COMMENT '预约编号RG201804280001',
-- 以下字段属于表单自定义字段，不好控制，所以放在json中存储
  `invalid_time` VARCHAR(32) COMMENT '访邀有效时长',
  `plate_no` VARCHAR(32) COMMENT '车牌号码',
  `id_number` VARCHAR(64) COMMENT '证件号码',
  `visit_floor` VARCHAR(128) COMMENT '到访楼层',
  `visit_addresses` VARCHAR(1024) COMMENT '到访门牌',
  `form_json_value` TEXT COMMENT '表单提交的json',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '访客管理访客表 ';

-- 访客管理预约事件表 , add by dengs, 20180425
-- DROP TABLE IF EXISTS `eh_visitor_sys_actions`;
CREATE TABLE `eh_visitor_sys_actions` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `visitor_id` BIGINT NOT NULL COMMENT '访客表id',
	`action_type` TINYINT NOT NULL COMMENT '1,园区自助登记，2,园区到访确认，3,园区拒绝到访  4,企业自助登记，5,企业到访确认，6,企业拒绝到访',
	`creator_name` VARCHAR(256) COMMENT '事件操作者',
  `creator_uid` BIGINT COMMENT '创建者id，可以为空',
  `create_time` DATETIME COMMENT '事件发生时间',
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '访客管理预约编码表';

-- 访客管理预约编码表 , add by dengs, 20180425
-- DROP TABLE IF EXISTS `eh_visitor_sys_owner_code`;
CREATE TABLE `eh_visitor_sys_owner_code` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
	`random_code` VARCHAR(16) NOT NULL UNIQUE COMMENT '随机码',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '访客管理预约编码表';

-- 访客管理预约流水码表 , add by dengs, 20180425
-- DROP TABLE IF EXISTS `eh_visitor_sys_coding`;
CREATE TABLE `eh_visitor_sys_coding` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `day_mark` VARCHAR(16) COMMENT 'yyyymmdd 形式的字符串,日标识',
	`serial_code` INTEGER NOT NULL DEFAULT 0 COMMENT '流水码',
  `status` TINYINT  DEFAULT 2 COMMENT '0:未使用状态,2:使用状态',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '访客管理预约流水码表';

-- 访客管理设备表(ipad,printer) , add by dengs, 20180425
-- DROP TABLE IF EXISTS `eh_visitor_sys_devices`;
CREATE TABLE `eh_visitor_sys_devices` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `device_type` VARCHAR(256)  COMMENT '设备类型，ipad or printer',
  `device_type_name` VARCHAR(256)  COMMENT '设备类型名称，如ipad mini,printer,ipad pro',
  `device_id` VARCHAR(256)  COMMENT '设备id',
  `device_version` VARCHAR(128)  COMMENT '设备版本',
  `device_name` VARCHAR(128) COMMENT '设备名称',
  `app_version` VARCHAR(128) COMMENT 'app版本',
  `device_pic_uri` VARCHAR(1024) COMMENT '设备图片uri',
  `pairing_code` VARCHAR(32) COMMENT '配对成功时候使用的配对码',
  `app_key` VARCHAR(256) COMMENT '设备后台请求接口appkey',
  `secret_key` VARCHAR(256) COMMENT '设备后台请求接口secretkey',
  `status` TINYINT  DEFAULT 2 COMMENT '0:被删除状态,2:正常状态',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '访客管理设备表 ';

-- 访客管理办公地点表 , add by dengs, 20180425
-- DROP TABLE IF EXISTS `eh_visitor_sys_office_locations`;
CREATE TABLE `eh_visitor_sys_office_locations` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `office_location_name` VARCHAR(256)  COMMENT '办公地点名称',
  `addresses` VARCHAR(512) COMMENT '办公地点地址',
  `longitude` double DEFAULT NULL COMMENT '办公地点经度',
  `latitude` double DEFAULT NULL COMMENT '办公地点维度',
  `geohash` varchar(32) DEFAULT NULL COMMENT '办公地点经纬度hash值',
  `map_addresses` varchar(512) DEFAULT NULL COMMENT '办公地点地图选点地址',
  `status` TINYINT  DEFAULT 2 COMMENT '0:被删除状态,2:正常状态',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '访客管理办公地点表 ';

-- 访客管理配置表 , add by dengs, 20180425
-- DROP TABLE IF EXISTS `eh_visitor_sys_configurations`;
CREATE TABLE `eh_visitor_sys_configurations` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `config_version` BIGINT COMMENT '配置版本',
  `guide_info` varchar(2048) COMMENT '指引信息',
  `owner_token` VARCHAR(32) NOT NULL UNIQUE COMMENT '自助登记二维码ownerToken地址',
  `logo_uri` VARCHAR(1024) COMMENT '客户端封面图uri地址',
  `welcome_pic_uri` VARCHAR(1024) COMMENT '客户端欢迎页面uri地址',
  `ipad_theme_rgb` VARCHAR(32) COMMENT '客户端主题rgb',
  `secrecy_agreement` TEXT COMMENT '保密协议',
  `welcome_pages` TEXT COMMENT '欢迎页面',
  `welcome_show_type` VARCHAR(32) COMMENT '欢迎页面类型，image显示图片，text显示富文本',
  `config_json` TEXT   COMMENT '是否配置项的json配置,访客二维码，访客信息，交通指引，手机扫码自助登记，ipad启动欢迎页面，签署保密协议,允许拍照，允许跳过拍照，输入随访人数，是否启用门禁,门禁id',
  `config_form_json` TEXT   COMMENT '表单内容是否显示的配置项，有效期，车牌号码，证件号码，来访备注，到访楼层，到访门牌',
  `config_pass_card_json` TEXT   COMMENT '通行证配置，品牌形象，左侧图像，自定义字段，自定义字段，备注信息',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '访客管理配置表 ';

-- 访客管理黑名单表 , add by dengs, 20180425
-- DROP TABLE IF EXISTS `eh_visitor_sys_black_list`;
CREATE TABLE `eh_visitor_sys_black_list` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `visitor_name` VARCHAR(256) COMMENT '黑名单访客姓名',
  `visitor_phone` VARCHAR(32) COMMENT '黑名单访客电话',
  `reason` TEXT COMMENT '上黑名单原因',
  `status` TINYINT  DEFAULT 2 COMMENT '0:被删除状态,2:正常状态',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '访客管理黑名单表 ';

-- 访客管理到访是由表 , add by dengs, 20180425
-- DROP TABLE IF EXISTS `eh_visitor_sys_visit_reason`;
CREATE TABLE `eh_visitor_sys_visit_reason` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) COMMENT 'community or organization',
  `owner_id` BIGINT COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `visit_reason` VARCHAR(256) COMMENT '事由描述',
  `status` TINYINT DEFAULT 2 COMMENT '0:被删除状态,2:正常状态',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '访客管理到访是由表 ';

-- 访客管理1.0end

