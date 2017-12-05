CREATE TABLE `eh_equipment_module_community_map` (
  `id` bigint(20) NOT NULL,
  `target_type` varchar(255) DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL COMMENT 'community id ',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_equipment_inspection_standards`
ADD COLUMN `refer_id`  bigint(20) NULL AFTER `repeat_type`;
