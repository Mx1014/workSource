ALTER TABLE `eh_conf_account_categories` MODIFY COLUMN `conf_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: 25方仅视频, 1: 25方支持电话, 2: 100方仅视频, 3: 100方支持电话, 4: 6方仅视频, 5: 50方仅视频, 6: 50方支持电话';

INSERT INTO `eh_conf_account_categories` VALUES ('9', '0', '4', '3', '110.00', '0');
INSERT INTO `eh_conf_account_categories` VALUES ('10', '0', '5', '3', '150.00', '0');
INSERT INTO `eh_conf_account_categories` VALUES ('11', '0', '6', '3', '130.00', '0');
INSERT INTO `eh_conf_account_categories` VALUES ('12', '1', '4', '3', '180.00', '0');
INSERT INTO `eh_conf_account_categories` VALUES ('13', '1', '5', '3', '100.00', '0');
INSERT INTO `eh_conf_account_categories` VALUES ('14', '1', '6', '3', '130.00', '0');