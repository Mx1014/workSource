-- merge from mergeAsset by xiongying20170417
CREATE TABLE `eh_asset_vendor` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the vendor, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'used to display',
  `vendor_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'reference to name of eh_parking_vendors',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_asset_vendor_owner_id` (`owner_type`,`owner_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;