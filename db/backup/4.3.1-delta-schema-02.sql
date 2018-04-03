
-- 对接映射表 add by sw 20170302
CREATE TABLE `eh_docking_mappings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',  
  `scope` VARCHAR(64) NOT NULL,
  `name` VARCHAR(256),
  `mapping_value` VARCHAR(256),
  `mapping_json` VARCHAR(1024),
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;