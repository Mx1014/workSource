SET FOREIGN_KEY_CHECKS=0;

-- DROP TABLE IF EXISTS `eh_organization_member_details`;
CREATE TABLE `eh_organization_member_details` (
  `id` BIGINT NOT NULL,
  `birthday` DATE,
  `marital_flag` TINYINT COMMENT '0: undisclosured, 1: married, 2: unmarried',
  `political_status` VARCHAR(128),
  `native_place` VARCHAR(128),
  `en_name` VARCHAR(128),
  `reg_residence` VARCHAR(128),
  `id_number` BIGINT,
  `email` VARCHAR(128),
  `wechat` VARCHAR(128),
  `qq` VARCHAR(128),
  `emergency_name` VARCHAR(128),
  `emergency_tel` VARCHAR(128),
  `address` VARCHAR(255),
  `employee_type` VARCHAR(64),
  `employee_status` VARCHAR(64) NOT NULL,
  `positive_time` DATETIME NOT NULL,
  `expiry_time` DATETIME,
  `salary_number` VARCHAR(128),
  `social_secu_number` VARCHAR(128),
  `provident_fund_number` VARCHAR(128),
  `profile_flag` TINYINT(4) COMMENT '0: not complete, 1: complete',
  `entry_time` DATETIME NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

