
-- AUTHOR: 梁燕龙
-- REMARK: 微信分享配置中增加主题色字段
ALTER TABLE `eh_app_urls` ADD COLUMN `theme_color` VARCHAR(64) COMMENT '主题色';
ALTER TABLE `eh_app_urls` ADD COLUMN `package_name` VARCHAR(64) COMMENT '包名';

ALTER TABLE `eh_rentalv2_site_resources`
MODIFY COLUMN `name`  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `type`;

-- AUTHOR: 吴寒
-- REMARK: 打卡考勤V8.2 - 支持人脸识别关联考勤；支持自动打卡
ALTER TABLE `eh_punch_logs` ADD COLUMN `create_type` TINYINT NOT NULL DEFAULT 0 COMMENT '创建类型 : 0-正常打卡创建 1-自动打卡创建 2-人脸识别打卡创建 4-其他第三方接口创建(通过第三方接口打卡没有带创建类型)' ;

-- 外出打卡表
CREATE TABLE `eh_punch_go_out_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `user_id` BIGINT DEFAULT NULL COMMENT 'user''s id',
  `detail_id` BIGINT DEFAULT NULL COMMENT 'eh_organization_member_details id',
  `organization_id` BIGINT DEFAULT NULL COMMENT 'organization id',
  `namespace_id` INT DEFAULT NULL COMMENT 'NAMESPACE id',
  `longitude` DOUBLE DEFAULT NULL,
  `latitude` DOUBLE DEFAULT NULL,
  `location_info` VARCHAR(1024) DEFAULT NULL COMMENT '打卡用到的地址定位',
  `wifi_info` VARCHAR(1024) DEFAULT NULL COMMENT '打卡用到的WiFi信息(暂时无用)',
  `img_uri` VARCHAR(2048) DEFAULT NULL COMMENT '打卡上传图片uri地址',
  `description` VARCHAR(1024) DEFAULT NULL COMMENT '备注',
  `punch_date` DATE DEFAULT NULL COMMENT 'user punch date',
  `punch_time` DATETIME DEFAULT NULL COMMENT '打卡时间',
  `identification` VARCHAR(255) DEFAULT NULL COMMENT 'unique identification for a phone',
  `status` TINYINT(4) DEFAULT NULL COMMENT '打卡状态 0-正常 1-迟到 2-早退 3-缺勤 14-缺卡',
  `update_date` DATE DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_user_id` (`user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_punch_day_logs` ADD COLUMN `go_out_punch_flag` TINYINT DEFAULT 0 COMMENT '是否外出打卡，1：是 0：否' AFTER `normal_flag`;
ALTER TABLE `eh_punch_statistics` ADD COLUMN `go_out_punch_day_count` INT DEFAULT 0 COMMENT '当月外出打卡天数' AFTER `rest_day_count`;

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
`owner_type` tinyint(4) NOT NULL COMMENT '记录所属对象类型 0园区 1公司',
`creator_uid` bigint(20) NOT NULL COMMENT '记录创建人userId',
`create_time` datetime NOT NULL COMMENT '记录创建时间',
`operator_uid` bigint(20) DEFAULT NULL COMMENT '记录更新人userId',
`operate_time` datetime DEFAULT NULL COMMENT '记录更新时间',
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '门禁组';

ALTER TABLE `eh_door_auth` ADD COLUMN `licensee_type` TINYINT NULL DEFAULT 0 COMMENT '被授权对象的类型 0用户 1组织架构节点 2项目(公司) 3楼栋(公司) 4楼层(公司) 5项目(家庭) 6楼栋(家庭) 7楼层(家庭)' AFTER `user_id`;
ALTER TABLE `eh_door_auth` ADD COLUMN `group_type` TINYINT NULL DEFAULT 0 COMMENT '门禁集合的类型 0 单个门禁 1 新门禁组(门禁3.0) ' AFTER `user_id`;

ALTER TABLE `eh_door_access` ADD COLUMN `adress_detail` varchar(64) NULL COMMENT '办公地点/楼栋_楼层' AFTER `address`;

-- AUTHOR: 张智伟 20180914
-- REMARK: ISSUE-37602 表单关联工作流节点
ALTER TABLE eh_general_approval_vals ADD COLUMN flow_node_id BIGINT COMMENT '表单绑定的工作流节点ID' AFTER flow_case_id;
ALTER TABLE eh_general_approval_vals ADD COLUMN creator_uid BIGINT COMMENT '创建人uid' AFTER create_time;
ALTER TABLE eh_general_approval_vals ADD COLUMN operator_uid BIGINT COMMENT '操作人Uid' AFTER creator_uid;
ALTER TABLE eh_general_approval_vals ADD COLUMN operate_time DATETIME COMMENT '编辑时间' AFTER operator_uid;

ALTER TABLE eh_general_approval_vals ADD INDEX i_eh_flow_case_id(`flow_case_id`);

-- AUTHOR: 杨崇鑫   20181017
-- REMARK: 缴费管理V7.0（新增缴费相关统计报表） 
-- REMARK: 增加项目-时间段（月份）统计结果集表
CREATE TABLE `eh_payment_bill_statistic_community` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),  
  `date_str` VARCHAR(10),  
  `amount_receivable` DECIMAL(10,2) COMMENT '应收（含税)',
  `amount_receivable_without_tax` DECIMAL(10,2) COMMENT '应收（不含税）',
  `tax_amount` DECIMAL(10,2) COMMENT '税额',
  `amount_received` DECIMAL(10,2) COMMENT '已收（含税）',
  `amount_received_without_tax` DECIMAL(10,2) COMMENT '已收（不含税）',
  `amount_owed` DECIMAL(10,2) COMMENT '待收（含税）',
  `amount_owed_without_tax` DECIMAL(10,2)  COMMENT '待收（不含税）',
  `amount_exemption` DECIMAL(10,2) COMMENT 'amount reduced',
  `amount_supplement` DECIMAL(10,2) COMMENT 'amount increased',  
  `due_day_count` DECIMAL(10,2) COMMENT '总欠费天数', 
  `notice_times` DECIMAL(10,2) COMMENT '总催缴次数',
  `collection_rate` DECIMAL(10,2) COMMENT '收缴率=已收金额/应收含税金额*100%',
  `create_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP, 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='项目-时间段（月份）统计结果集表';

-- AUTHOR: 杨崇鑫   20181022
-- REMARK: 缴费管理V7.0（新增缴费相关统计报表） 
-- REMARK: 增加楼宇-时间段（月份）统计结果集表
CREATE TABLE `eh_payment_bill_statistic_building` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64), 
  `building_id` BIGINT(20),
  `building_name` VARCHAR(256),
  `date_str` VARCHAR(10), 
  `amount_receivable` DECIMAL(10,2) COMMENT '应收（含税)',
  `amount_receivable_without_tax` DECIMAL(10,2) COMMENT '应收（不含税）',
  `tax_amount` DECIMAL(10,2) COMMENT '税额',
  `amount_received` DECIMAL(10,2) COMMENT '已收（含税）',
  `amount_received_without_tax` DECIMAL(10,2) COMMENT '已收（不含税）',
  `amount_owed` DECIMAL(10,2) COMMENT '待收（含税）',
  `amount_owed_without_tax` DECIMAL(10,2)  COMMENT '待收（不含税）',
  `due_day_count` DECIMAL(10,2) COMMENT '总欠费天数', 
  `notice_times` DECIMAL(10,2) COMMENT '总催缴次数',
  `collection_rate` DECIMAL(10,2) COMMENT '收缴率=已收金额/应收含税金额*100%',
  `create_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP, 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='楼宇-时间段（月份）统计结果集表';

-- AUTHOR: 唐岑   20181021
-- REMARK: 资产管理V3.4（资产统计报表） 
-- REMARK: 项目信息报表结果集（项目-月份） 
CREATE TABLE `eh_property_statistic_community` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11),
  `community_id` bigint(20),
  `community_name` varchar(64),
  `date_str` varchar(10) COMMENT '统计月份（格式为xxxx-xx）',
  `building_count` int(11) DEFAULT '0' COMMENT '园区下的楼宇总数',
  `total_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的房源总数',
  `free_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的待租房源数',
  `rent_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的已出租房源数',
  `occupied_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的已占用房源数',
  `living_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的自用房源数',
  `saled_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的已售房源数',
  `area_size` decimal(10,2) DEFAULT '0.00' COMMENT '园区的建筑面积',
  `rent_area` decimal(10,2) DEFAULT '0.00' COMMENT '园区的在租面积',
  `free_area` decimal(10,2) DEFAULT '0.00' COMMENT '园区的可招租面积',
  `rent_rate` decimal(10,2) COMMENT '出租率=在租面积/总的建筑面积*100%',
  `free_rate` decimal(10,2) COMMENT '空置率=可招租面积/总的建筑面积*100% ',
  `status` tinyint(4) DEFAULT '2' COMMENT '该条的记录状态：0-inactive, 1-confirming, 2-active',
  `create_time` datetime ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目信息报表结果集（项目-月份）';

-- AUTHOR: 唐岑   20181021
-- REMARK: 资产管理V3.4（资产统计报表） 
-- REMARK: 楼宇信息报表结果集（楼宇-月份） 
CREATE TABLE `eh_property_statistic_building` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11),
  `community_id` bigint(20),
  `building_id` bigint(20),
  `building_name` varchar(64),
  `date_str` varchar(10) COMMENT '统计月份（格式为xxxx-xx）',
  `total_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的房源总数',
  `free_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的待租房源数',
  `rent_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的已出租房源数',
  `occupied_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的已占用房源数',
  `living_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的自用房源数',
  `saled_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的已售房源数',
  `area_size` decimal(10,2) DEFAULT '0.00' COMMENT '楼宇的建筑面积',
  `rent_area` decimal(10,2) DEFAULT '0.00' COMMENT '楼宇的在租面积',
  `free_area` decimal(10,2) DEFAULT '0.00' COMMENT '楼宇的可招租面积',
  `rent_rate` decimal(10,2) COMMENT '出租率=在租面积/总的建筑面积*100%',
  `free_rate` decimal(10,2) COMMENT '空置率=可招租面积/总的建筑面积*100% ',
  `status` tinyint(4) DEFAULT '2' COMMENT '该条的记录状态：0-inactive, 1-confirming, 2-active',
  `create_time` datetime ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='楼宇信息报表结果集（楼宇-月份）'; 


-- AUTHOR: 李清岩 20181029
-- REMARK:issue-38336 门禁3.0.2 门禁管理授权
CREATE TABLE `eh_aclink_management` (
`id` bigint NOT NULL ,
`namespace_id` int(11) NOT NULL ,
`door_id` bigint NOT NULL COMMENT '门禁Id',
`owner_id` bigint NOT NULL COMMENT '门禁归属对象Id',
`owner_type` tinyint NOT NULL COMMENT '门禁归属对象类型 0园区 1公司',
`manager_id` bigint NOT NULL COMMENT '授权对象Id',
`manager_type` tinyint NOT NULL COMMENT '授权对象类型 0园区 1公司',
`creator_uid` bigint NOT NULL COMMENT '记录创建人userId',
`create_time` datetime NOT NULL COMMENT '记录创建时间',
`status` tinyint NOT NULL DEFAULT '1' COMMENT '0已删除1有效',
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '门禁管理授权';
