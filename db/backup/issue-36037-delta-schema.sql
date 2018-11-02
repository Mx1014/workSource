-- AUTHOR: 梁燕龙 20181026
-- REMARK: 广告管理单应用修改为多应用
CREATE TABLE eh_banner_categories
(
  id            BIGINT                 NOT NULL
    PRIMARY KEY,
  owner_type    VARCHAR(32) DEFAULT '' NOT NULL
  COMMENT 'the type of who own the category, community, etc',
  owner_id      BIGINT DEFAULT '0'     NOT NULL,
  parent_id     BIGINT DEFAULT '0'     NOT NULL,
  name          VARCHAR(64)            NOT NULL,
  path          VARCHAR(128)           NULL,
  default_order INT                    NULL,
  status        TINYINT DEFAULT '0'    NOT NULL
  COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  creator_uid   BIGINT DEFAULT '0'     NOT NULL
  COMMENT 'record creator user id',
  create_time   DATETIME               NULL,
  delete_uid    BIGINT DEFAULT '0'     NOT NULL
  COMMENT 'record deleter user id',
  delete_time   DATETIME               NULL,
  namespace_id  INT DEFAULT '0'        NOT NULL,
  logo_uri      VARCHAR(1024)          NULL
  COMMENT 'default cover uri',
  entry_id      INT                    NULL
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '广告管理应用入口表';

ALTER TABLE eh_banners ADD COLUMN `category_id` BIGINT COMMENT '应用入口ID';