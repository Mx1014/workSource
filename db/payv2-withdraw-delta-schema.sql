ALTER TABLE `eh_payment_users` ADD COLUMN `bank_name` VARCHAR(512) COMMENT 'the name of bank where enterprise has a account';
ALTER TABLE `eh_payment_users` ADD COLUMN `bank_number` VARCHAR(128) COMMENT 'the number of bank where enterprise has a account';
ALTER TABLE `eh_payment_users` ADD COLUMN `bank_card_number` VARCHAR(128) COMMENT 'the card number of enterprise bank account';

ALTER TABLE `eh_payment_users` ADD COLUMN `enterprise_name` VARCHAR(512) COMMENT 'the name of enterprise';
ALTER TABLE `eh_payment_users` ADD COLUMN `enterprise_business_licence` VARCHAR(128) COMMENT 'the business licence number of enterprise';
ALTER TABLE `eh_payment_users` ADD COLUMN `enterprise_business_licence_uri` VARCHAR(128) COMMENT 'the image of business licence of enterprise';
ALTER TABLE `eh_payment_users` ADD COLUMN `enterprise_account_licence_uri` VARCHAR(128) COMMENT 'the image of account licence of enterprise';
 
ALTER TABLE `eh_payment_users` ADD COLUMN `legal_person_name` VARCHAR(512) COMMENT 'the real name of legal person in an enterprise';
ALTER TABLE `eh_payment_users` ADD COLUMN `legal_person_phone` VARCHAR(512) COMMENT 'the phone number of legal person in an enterprise';
ALTER TABLE `eh_payment_users` ADD COLUMN `legal_person_identity_type` VARCHAR(512) COMMENT 'the identity type of legal person in an enterprise';
ALTER TABLE `eh_payment_users` ADD COLUMN `legal_person_identity_number` VARCHAR(512) COMMENT 'the identity number of legal person in an enterprise';
ALTER TABLE `eh_payment_users` ADD COLUMN `legal_person_identity_front_uri` VARCHAR(1024) COMMENT 'the front side of identity image of legal person in an enterprise';
ALTER TABLE `eh_payment_users` ADD COLUMN `legal_person_identity_back_uri` VARCHAR(1024) COMMENT 'the back side identity image of legal person in an enterprise';


ALTER TABLE `eh_payment_users` ADD COLUMN `status` TINYINT NOT NULL COMMENT '0-inactive, 1-waiting for approval, 2-active';
ALTER TABLE `eh_payment_users` ADD COLUMN `creator_uid` BIGINT;
ALTER TABLE `eh_payment_users` ADD COLUMN `operator_uid` BIGINT;
ALTER TABLE `eh_payment_users` ADD COLUMN `update_time` DATETIME;

-- record the withdraw orders
DROP TABLE IF EXISTS `eh_payment_withdraw_orders`;
CREATE TABLE `eh_payment_withdraw_orders` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `payment_user_type` INTEGER NOT NULL COMMENT 'the account type to withdraw the monty: 1-普通会员,2-企业会员',
  `payment_user_id` BIGINT NOT NULL COMMENT 'the account in pay-system to withdraw the monty',
  `amount` DECIMAL(10,2) COMMENT 'the amount to withdraw',
  `status` TINYINT NOT NULL COMMENT '0-inactive, 1-waiting for confirm, 2-success, 3-failed',
  `callback_time` DATETIME,
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user who withdraw the money',
  `operate_time` DATETIME COMMENT 'the time to withdraw the money',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



