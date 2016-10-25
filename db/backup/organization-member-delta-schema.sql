-- 通讯录增加 隐藏 显示字段 by sfyan 20161010
ALTER TABLE `eh_organization_members` ADD COLUMN `visible_flag` TINYINT(4) DEFAULT 0 COMMENT '0 show 1 hide';

-- DROP TABLE IF EXISTS `eh_region_codes`;
CREATE TABLE `eh_region_codes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(64) NOT NULL COMMENT 'region name',
  `code` INTEGER NOT NULL COMMENT 'region code',
  `pinyin` VARCHAR(256) NOT NULL COMMENT 'region name pinyin',
  `first_letter` CHAR(2) NOT NULL COMMENT 'region name pinyin first letter',
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active', 
  `hot_flag` TINYINT NOT NULL DEFAULT '0' COMMENT '0: no, 1: yes', 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 增加区号 by sfyan 20161012
ALTER TABLE `eh_user_identifiers` ADD COLUMN `region_code` INTEGER DEFAULT 86 COMMENT 'region code 86 852';