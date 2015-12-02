# belong eh_users partition

DROP TABLE IF EXISTS `eh_user_group_histories`;
CREATE TABLE `eh_user_group_histories` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
    `group_discriminator` VARCHAR(32) COMMENT 'redendant info for quickly distinguishing associated group', 
    `group_id` BIGINT,
    `community_id` BIGINT,
    `address_id` BIGINT,
    `create_time` DATETIME NOT NULL COMMENT 'remove-deletion policy, user directly managed data',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



##alter tables 

ALTER TABLE  `eh_rental_rules`  ADD COLUMN  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization';
ALTER TABLE  `eh_rental_rules`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';
ALTER TABLE  `eh_rental_rules`   ADD COLUMN `rental_type` TINYINT(4) DEFAULT NULL COMMENT '0: as hour:min  1-as half day 2-as day';
ALTER TABLE  `eh_rental_rules`   ADD COLUMN `cancel_time` BIGINT(20) DEFAULT NULL;
ALTER TABLE  `eh_rental_rules`   ADD COLUMN `overtime_time` BIGINT(20) DEFAULT NULL;


ALTER TABLE `eh_rental_sites` ADD COLUMN  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization';
ALTER TABLE `eh_rental_sites`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';
ALTER TABLE `eh_rental_sites` ADD COLUMN `introduction` TEXT;
ALTER TABLE `eh_rental_sites` ADD COLUMN `notice` TEXT;

ALTER TABLE `eh_rental_sites_bills` ADD COLUMN  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization';
ALTER TABLE `eh_rental_sites_bills`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';

ALTER TABLE `eh_rental_site_rules` ADD COLUMN  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization';
ALTER TABLE `eh_rental_si`ehcore`te_rules`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';

ALTER TABLE `eh_rental_bills` ADD COLUMN  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization';
ALTER TABLE `eh_rental_bills`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';

ALTER TABLE `eh_rental_bill_paybill_map` ADD COLUMN  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization';
ALTER TABLE `eh_rental_bill_paybill_map`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';

ALTER TABLE `eh_rental_bill_attachments` ADD COLUMN  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization';
ALTER TABLE `eh_rental_bill_attachments`  CHANGE `community_id` `owner_id` BIGINT(20) NOT NULL COMMENT '    community id or organization id ';
