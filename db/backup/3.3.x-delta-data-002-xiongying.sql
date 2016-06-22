
INSERT INTO `eh_conf_account_categories` VALUES ('1', '5', '0', '12', '358.00', '0', '298.00', '1');
INSERT INTO `eh_conf_account_categories` VALUES ('2', '5', '2', '12', '358.00', '0', '298.00', '0');
INSERT INTO `eh_conf_account_categories` VALUES ('3', '5', '1', '12', '358.00', '0', '298.00', '1');
INSERT INTO `eh_conf_account_categories` VALUES ('4', '5', '3', '12', '358.00', '0', '298.00', '0');
INSERT INTO `eh_conf_account_categories` VALUES ('9', '5', '4', '12', '358.00', '0', '298.00', '0');
INSERT INTO `eh_conf_account_categories` VALUES ('10', '5', '5', '12', '358.00', '0', '298.00', '0');
INSERT INTO `eh_conf_account_categories` VALUES ('11', '5', '6', '12', '358.00', '0', '298.00', '0');

 
 UPDATE `eh_conf_accounts` SET `account_category_id` = 1 WHERE `account_category_id` = 5;
 UPDATE `eh_conf_accounts` SET `account_category_id` = 3 WHERE `account_category_id` = 7;
 UPDATE `eh_conf_accounts` SET `account_category_id` = 2 WHERE `account_category_id` = 6;
 UPDATE `eh_conf_accounts` SET `account_category_id` = 4 WHERE `account_category_id` = 8;
 UPDATE `eh_conf_accounts` SET `account_category_id` = 9 WHERE `account_category_id` = 12;
 UPDATE `eh_conf_accounts` SET `account_category_id` = 10 WHERE `account_category_id` = 13;
 UPDATE `eh_conf_accounts` SET `account_category_id` = 11 WHERE `account_category_id` = 14; 
 
 UPDATE `eh_conf_account_histories` SET `account_category_id` = 1 WHERE `account_category_id` = 5;
 UPDATE `eh_conf_account_histories` SET `account_category_id` = 3 WHERE `account_category_id` = 7;
 UPDATE `eh_conf_account_histories` SET `account_category_id` = 2 WHERE `account_category_id` = 6;
 UPDATE `eh_conf_account_histories` SET `account_category_id` = 4 WHERE `account_category_id` = 8;
 UPDATE `eh_conf_account_histories` SET `account_category_id` = 9 WHERE `account_category_id` = 12;
 UPDATE `eh_conf_account_histories` SET `account_category_id` = 10 WHERE `account_category_id` = 13;
 UPDATE `eh_conf_account_histories` SET `account_category_id` = 11 WHERE `account_category_id` = 14;
 
 UPDATE `eh_conf_orders` SET `account_category_id` = 1 WHERE `account_category_id` = 5;
 UPDATE `eh_conf_orders` SET `account_category_id` = 3 WHERE `account_category_id` = 7;
 UPDATE `eh_conf_orders` SET `account_category_id` = 2 WHERE `account_category_id` = 6;
 UPDATE `eh_conf_orders` SET `account_category_id` = 4 WHERE `account_category_id` = 8;
 UPDATE `eh_conf_orders` SET `account_category_id` = 9 WHERE `account_category_id` = 12;
 UPDATE `eh_conf_orders` SET `account_category_id` = 10 WHERE `account_category_id` = 13;
 UPDATE `eh_conf_orders` SET `account_category_id` = 11 WHERE `account_category_id` = 14;
 
 UPDATE `eh_conf_source_accounts` SET `account_category_id` = 1 WHERE `account_category_id` = 5;
 UPDATE `eh_conf_source_accounts` SET `account_category_id` = 3 WHERE `account_category_id` = 7;
 UPDATE `eh_conf_source_accounts` SET `account_category_id` = 2 WHERE `account_category_id` = 6;
 UPDATE `eh_conf_source_accounts` SET `account_category_id` = 4 WHERE `account_category_id` = 8;
 UPDATE `eh_conf_source_accounts` SET `account_category_id` = 9 WHERE `account_category_id` = 12;
 UPDATE `eh_conf_source_accounts` SET `account_category_id` = 10 WHERE `account_category_id` = 13;
 UPDATE `eh_conf_source_accounts` SET `account_category_id` = 11 WHERE `account_category_id` = 14;

