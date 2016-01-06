ALTER TABLE `eh_conf_account_categories` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_invoices` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_orders` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_order_account_map` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_order_account_map` ADD COLUMN `conf_account_namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_source_accounts` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_warning_contacts` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';

ALTER TABLE `eh_conf_accounts` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_account_histories` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_conferences` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_conf_reservations` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
