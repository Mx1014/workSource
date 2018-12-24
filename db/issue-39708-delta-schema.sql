-- AUTHOR :黄鹏宇 2018年12月20日
-- REMARK :添加物业配置表
CREATE TABLE `eh_property_configurations` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT NULL,
  `community_id` bigint(20) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `module_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '物业通用配置';

-- AUTHOR: 黄鹏宇 20181217
-- REMARK: 物业缴费V7.4(瑞安项目-资产管理对接CM系统) -41302：增加第三方客户分类
ALTER TABLE eh_enterprise_customers ADD COLUMN namespace_customer_group VARCHAR(128) COMMENT '第三方客户分类' AFTER namespace_customer_type;

-- AUTHOR: 杨崇鑫 2018-12-17
-- REMARK: 物业缴费V7.4(瑞安项目-资产管理对接CM系统) ： 一个特殊error描述给左邻系统
ALTER TABLE `eh_payment_bills` ADD COLUMN `third_error_description` VARCHAR(256) COMMENT '一个特殊error描述给左邻系统';

-- AUTHOR: 刘一麟 2018-12-19
-- REMARK: 人脸识别v1.8 照片注册结果通知
CREATE TABLE `eh_aclink_photo_sync_result` (
    `id` BIGINT NOT NULL COMMENT '主键',
    `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
    `server_id` BIGINT NOT NULL COMMENT '内网服务器id',
    `photo_id` BIGINT NOT NULL COMMENT '照片id',
    `res_code` TINYINT NOT NULL COMMENT '同步结果',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operate_time` DATETIME COMMENT '记录更新时间',
    `operator_uid` BIGINT COMMENT '记录更新人userId',
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='照片同步结果表';

ALTER TABLE eh_door_auth_level ADD COLUMN `name` varchar(128) COMMENT '自动授权对象名称' AFTER `level_type`;

