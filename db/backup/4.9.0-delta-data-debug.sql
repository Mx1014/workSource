INSERT INTO `eh_payment_bill_groups`(`id`,`namespace_id`,`owner_id`,`owner_type`,`name`,`balance_date_type`,`bills_day`,`creator_uid`,`create_time`,`operator_uid`,`update_time`,`default_order`)
SELECT '1','999974','240111044332059749','community','租金', '2', '5', '0', '2017-02-01 11:48:03', NULL, NULL, '1';

INSERT INTO `eh_payment_bill_groups_rules` (`id`,`namespace_id`,`bill_group_id`,`charging_item_id`,`charging_standards_id`,`charging_item_name`,`variables_json_string`,`ownerType`,`ownerId`)
SELECT '1','999974','1','1','1','租金', '{\"ydj\":\"1000\",\"mj\":\"0\"}','community','240111044332059749';

INSERT INTO `eh_payment_charging_item_scopes` (`id`,`charging_item_id`,`namespace_id`,`owner_id`,`owner_type`) SELECT '1',
'1','999974','240111044332059749','community';

INSERT INTO `eh_payment_charging_items` (`id`,`name`,`creator_uid`,`create_time`,`operator_uid`,`update_time`,`default_order`) SELECT '1',
'租金', '0', '2017-02-01 11:48:03', null, null, '1';

INSERT INTO `eh_payment_charging_standards` (`id`,`name`,`charging_items_id`,`formula`,`formula_json`,`formula_type`,`billing_cycle`,`price_unit_type`,`creator_uid`,`create_time`,`operator_uid`,`update_time`)
SELECT '1','租金', '1', '月单价*面积', 'ydj*mj', '2', '1', '2', '0', '2017-02-01 11:48:03', null, null;

INSERT INTO `eh_payment_charging_standards_scopes` (`id`,`charging_standard_id`,`owner_type`,`owner_id`,`creator_uid`,`create_time`,`operator_uid`,`update_time`)
SELECT '1','1', 'community', '240111044332059749', '0', '2017-02-01 11:48:03', null, null;

INSERT INTO `eh_payment_variables` VALUES ('1','1','1','月单价', null, null, null, null, 'ydj');
INSERT INTO `eh_payment_variables` VALUES ('2','1', '1', '计费面积', null, null, null, null, 'mj');
