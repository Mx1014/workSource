CREATE TABLE `eh_service_module_app_mappings`(
  `id` BIGINT NOT NULL ,
  `app_origin_id_male` BIGINT COMMENT 'the origin id of app',
  `app_module_id_male` BIGINT COMMENT 'the module id of app',
  `app_origin_id_female` BIGINT COMMENT 'the origin id of app',
  `app_module_id_female` BIGINT COMMENT 'the module id of app',
  `create_time` DATETIME NOT NULL DEFAULT now(),
  `create_uid` BIGINT NOT NULL,
  `update_time` DATETIME NOT NULL DEFAULT now(),
  `update_uid` BIGINT NOT NULL,
  UNIQUE KEY `app_mapping` (`app_origin_id_male`, `app_origin_id_female`),
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'relation mappings among applications';