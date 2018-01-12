-- 文档管理 added by nan.rong 01/12/2018

-- DROP TABLE eh_file_management_catalogs;
CREATE TABLE `eh_file_management_catalogs` (
  `id` BIGINT,
  `namespace_id` INT NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) ,
  `name` VARCHAR(64) COMMENT 'the name of the catalog',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-invalid 1-valid',
  `creator_uid` BIGINT  DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- DROP TABLE eh_file_management_catalog_scopes;
CREATE TABLE `eh_file_management_catalog_scopes` (
  `id` BIGINT,
  `namespace_id` INT NOT NULL DEFAULT 0,
  `catalog_id` BIGINT NOT NULL COMMENT 'the id of the file catalog',
  `source_id` BIGINT NOT NULL COMMENT 'the id of the source',
  `source_description` VARCHAR(128) COMMENT 'the description of the scope class',
  `download_permission` TINYINT NOT NULL DEFAULT 0 COMMENT '0-refuse, 1-allow',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- DROP TABLE eh_file_management_contents;
CREATE TABLE `eh_file_management_contents` (
  `id` BIGINT,
  `namespace_id` INT NOT NULL DEFAULT 0,
  `owner_id` BIGINT,
  `owner_type` VARCHAR (64),
  `catalog_id` BIGINT COMMENT 'the id of the catalog',
  `name` VARCHAR(256) NOT NULL COMMENT 'the name of the content',
  `size` INT NOT NULL COMMENT 'the size of the content',
  `parent_id` BIGINT COMMENT 'the parent id of the folder',
  `content_type` VARCHAR(32) COMMENT 'file, folder',
  `content_uri` VARCHAR(2048) COMMENT 'the uri of the content',
  `status` TINYINT NOT NULL DEFAULT 1 '0-invalid, 1-valid',
  `creator_uid` BIGINT  DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- end by nan.rong



