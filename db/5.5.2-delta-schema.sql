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

-- janson 升级链接长度
ALTER TABLE `ehcore`.`eh_version_urls` CHANGE COLUMN `download_url` `download_url` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'example configuration: http://serviceurl/download/client-packages/${locale}/andriod-${major}-${minor}-${revision}.apk';
ALTER TABLE `ehcore`.`eh_version_urls` CHANGE COLUMN `info_url` `info_url` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'example configuration: http://serviceurl/download/client-package-info/${locale}/andriod-${major}-${minor}-${revision}.html';
--end

