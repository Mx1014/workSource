
-- 通用脚本
-- ADD BY 黄良铭  2018年7月3日
-- issue-30602  iOS推送流程升级 #30602

-- 创建 开发者账号信息表
CREATE TABLE `eh_developer_account_info` (
  `id` INT(11)  NOT NULL COMMENT '主键',
  `bundle_ids` varchar(200)  COMMENT '关联应用',
  `team_id` VARCHAR(100)  COMMENT '关联开发者帐号',
  `authkey_id` VARCHAR(100)  COMMENT 'authkey_id',
  `authkey`  BLOB COMMENT  'authkey',
  `create_time` DATETIME    COMMENT '创建时间',
  `create_name` VARCHAR(50)   COMMENT '创建者',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '开发者账号信息表';


-- 设备注册信息表新增3列（字段 ）
ALTER  TABLE eh_devices  ADD  pusher_service_type  VARCHAR(40)   COMMENT '推送服务类型：develop或productiom';
ALTER  TABLE eh_devices  ADD  bundle_id  VARCHAR(100)    COMMENT '关联应用';

-- 创建 bundleId 映射信息表
CREATE TABLE `eh_bundleid_mapper` (
  `id` INT(11)  NOT NULL COMMENT '主键',
  `namespace_id` INT(11)  COMMENT '域空间ID',
  `identify` VARCHAR(40)  COMMENT 'pusherIdentify中截取的标志字符串',
  `bundle_id` VARCHAR(100)  COMMENT '关联应用',
  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'bundleId 映射信息表';
-- END