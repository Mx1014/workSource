-- oauth2client 1.0   add by xq.tian 2017/03/09
--
-- oauth2 client AccessToken
--
-- DROP TABLE IF EXISTS `eh_oauth2_client_tokens`;
CREATE TABLE `eh_oauth2_client_tokens` (
  `id` BIGINT NOT NULL,
  `token_string` VARCHAR(128) NOT NULL COMMENT 'token string issued to requestor',
  `vendor` VARCHAR(32) NOT NULL COMMENT 'OAuth2 server name',
  `grantor_uid` BIGINT NOT NULL COMMENT 'eh_users id',
  `expiration_time` DATETIME NOT NULL COMMENT 'a successful acquire of access token by the code should immediately expires it',
  `scope` VARCHAR(256) NULL DEFAULT NULL COMMENT 'space-delimited scope tokens per RFC 6749',
  `type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: access token, 1: refresh token',
  `create_time` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHAR SET ='utf8mb4';

--
-- oauth2 servers
--
-- DROP TABLE IF EXISTS `eh_oauth2_servers`;
CREATE TABLE `eh_oauth2_servers` (
  `id` BIGINT NOT NULL,
  `vendor` VARCHAR(32) NOT NULL COMMENT 'OAuth2 server name',
  `client_id` VARCHAR(128) NOT NULL COMMENT 'third part provided',
  `client_secret` VARCHAR(128) NOT NULL COMMENT 'third part provided',
  `redirect_uri` VARCHAR(1024) NOT NULL COMMENT 'authorize success redirect to this url',
  `response_type` VARCHAR(128) NOT NULL COMMENT 'e.g: code',
  `grant_type` VARCHAR(128) NOT NULL COMMENT 'e.g: authorization_code',
  `state` VARCHAR(128) NOT NULL COMMENT 'e.g: OAuth server will response this filed original',
  `scope` VARCHAR(256) NULL DEFAULT NULL COMMENT 'space-delimited scope tokens per RFC 6749',
  `authorize_url` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'OAuth server provided authorize url',
  `token_url` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'OAuth server provided get token url',
  `create_time` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHAR SET ='utf8mb4';

-- 增加全部页面item排序
ALTER TABLE eh_launch_pad_items ADD COLUMN  `more_order` INT NOT NULL DEFAULT 0;
-- 按场景分类
ALTER TABLE eh_item_service_categries ADD COLUMN `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default';

-- 帖子表添加父评论id, add by tt, 20170314
ALTER TABLE `eh_forum_posts` ADD COLUMN `parent_comment_id` BIGINT(20) COMMENT 'parent comment id';

-- 短信日志 add by sw 20170327
CREATE TABLE `eh_sms_logs`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `scope` VARCHAR(64),
  `code` INTEGER,
  `locale` VARCHAR(16),
  `mobile` VARCHAR(128),
  `text` text,
  `variables` VARCHAR(512),
  `result` text,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 增加域空间 add by xiongying20170328
ALTER TABLE eh_equipment_inspection_accessories ADD COLUMN  `namespace_id` INT NOT NULL DEFAULT 0;
ALTER TABLE eh_equipment_inspection_items ADD COLUMN  `namespace_id` INT NOT NULL DEFAULT 0;
ALTER TABLE eh_equipment_inspection_templates ADD COLUMN  `namespace_id` INT NOT NULL DEFAULT 0;

-- 品质核查增加域空间 add by xiongying20170329
ALTER TABLE eh_quality_inspection_categories ADD COLUMN  `namespace_id` INT NOT NULL DEFAULT 0;
ALTER TABLE eh_quality_inspection_evaluation_factors ADD COLUMN  `namespace_id` INT NOT NULL DEFAULT 0;
ALTER TABLE eh_quality_inspection_tasks ADD COLUMN  `namespace_id` INT NOT NULL DEFAULT 0;
ALTER TABLE eh_quality_inspection_logs ADD COLUMN  `namespace_id` INT NOT NULL DEFAULT 0;