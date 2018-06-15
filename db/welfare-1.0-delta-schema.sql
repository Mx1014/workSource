-- 企业福利1.0 建表sql
CREATE TABLE `eh_welfares` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(128) DEFAULT NULL COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT DEFAULT NULL COMMENT 'owner resource(user/organization) id',
  `subject` VARCHAR(128) COMMENT '主题名称',
  `content` TEXT COMMENT '祝福语',
  `sender_name` VARCHAR(128) COMMENT '发放人姓名',
  `sender_uid` BIGINT COMMENT '发放人userId',
  `sender_detail_id` BIGINT COMMENT '发放人detailId',
  `img_uri` VARCHAR(1024) COMMENT '附图uri',
  `status` TINYINT COMMENT '0-草稿 1-发送',
  `send_time` DATETIME DEFAULT NULL, 
  `creator_name` VARCHAR(128)  DEFAULT NULL, 
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL, 
  `operator_name` VARCHAR(128)  DEFAULT NULL,  
  `operator_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '企业福利表';

CREATE TABLE `eh_welfare_receivers` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `welfare_id` BIGINT NOT NULL COMMENT '福利id',
  `owner_type` VARCHAR(128) DEFAULT NULL COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT DEFAULT NULL COMMENT 'owner resource(user/organization) id', 
  `receiver_uid` BIGINT COMMENT '接收者userId',
  `receiver_name` VARCHAR(128) COMMENT '接收者姓名',
  `receiver_detail_id` BIGINT COMMENT '接收者detailId', 
  `status` TINYINT COMMENT '0-未接收 1-已接受',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '企业福利接收人表';

CREATE TABLE `eh_welfare_items` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `welfare_id` BIGINT NOT NULL COMMENT '福利id',
  `owner_type` VARCHAR(128) DEFAULT NULL COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT DEFAULT NULL COMMENT 'owner resource(user/organization) id', 
  `item_type` TINYINT COMMENT '企业福利类型(现在只有红包)',
  `item_single_quantity` VARCHAR(128) COMMENT '单个数量(用字符串表示)',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '企业福利项表';