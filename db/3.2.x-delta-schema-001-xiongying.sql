ALTER TABLE `eh_conf_account_categories` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_invoices` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_orders` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_order_account_map` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_order_account_map` ADD COLUMN `conf_account_namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_source_accounts` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';

ALTER TABLE `eh_conf_accounts` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_account_histories` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_conferences` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_reservations` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';


INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'videoConf', '10000', 'zh_CN', '账号不存在或无效');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'videoConf', '10001', 'zh_CN', '会议号无效');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'videoConf', '10002', 'zh_CN', '用户数为空');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'videoConf', '10003', 'zh_CN', '账号数为空');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'videoConf', '10004', 'zh_CN', '用户数大于账户数');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'videoConf', '10005', 'zh_CN', '账号在3个月内已分配过用户');

ALTER TABLE `eh_conf_conferences` ADD COLUMN `conference_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the conference id from 3rd conference provider';
ALTER TABLE `eh_conf_conferences` CHANGE `conf_id` `meeting_no` bigint(20) NOT NULL DEFAULT '0'COMMENT 'the meeting no from 3rd conference provider';
