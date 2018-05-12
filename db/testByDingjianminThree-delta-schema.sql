ALTER TABLE `eh_test_infos`
ADD COLUMN `address`  varchar(255) NULL AFTER `age`,
ADD COLUMN `sex`  bit NULL AFTER `address`;