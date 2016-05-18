INSERT INTO `eh_parking_lots` VALUES (10001, 'community', 240111044331048623, '科技工业园大厦停车场', 'BOSIGAO', NULL, 3, 2, 1025, '2016-3-31 17:07:20');
INSERT INTO `eh_parking_lots` VALUES (10002, 'community', 240111044331048623, '生产力大楼停车场', 'BOSIGAO', NULL, 4, 2, 1025, '2016-3-31 17:09:53');
INSERT INTO `eh_parking_lots` VALUES (10003, 'community', 240111044331048623, '综合楼停车场', 'BOSIGAO', NULL, 3, 2, 1025, '2016-3-30 17:11:02');

INSERT INTO `eh_parking_recharge_rates` VALUES (10001, 'community', 240111044331048623, 10001, '3个月', 3.00, 1200.00, 2, 1025, '2016-3-31 17:14:36');
INSERT INTO `eh_parking_recharge_rates` VALUES (10002, 'community', 240111044331048623, 10001, '6个月', 6.00, 2400.00, 2, 1025, '2016-3-31 17:15:41');
INSERT INTO `eh_parking_recharge_rates` VALUES (10003, 'community', 240111044331048623, 10002, '3个月', 3.00, 1200.00, 2, 1025, '2016-3-31 17:16:17');
INSERT INTO `eh_parking_recharge_rates` VALUES (10004, 'community', 240111044331048623, 10003, '3个月', 3.00, 1200.00, 2, 1025, '2016-3-31 17:16:17');
INSERT INTO `eh_parking_recharge_rates` VALUES (10005, 'community', 240111044331048623, 10002, '6个月', 6.00, 2400.00, 2, 1025, '2016-3-31 17:16:17');
INSERT INTO `eh_parking_recharge_rates` VALUES (10006, 'community', 240111044331048623, 10003, '6个月', 6.00, 2400.00, 2, 1025, '2016-3-31 17:16:17');

INSERT INTO `eh_parking_recharge_rates` VALUES (10007, 'community', 240111044331048623, 10003, '1个月', 1.00, 400.00, 2, 1025, '2016-3-31 17:16:17');


INSERT INTO `eh_parking_recharge_orders` VALUES (10001, 'community', 240111044331048623, 10001, '粤B88888', '测试', '13212341234', 1233, 1025, '13212341234', '2016-4-1 17:56:31', 'BOSIGAO', '13485', '10001', '3个月', 3.00, 1200.00, 2, 1, '2016-4-1 17:57:51', 1025, '2016-4-1 17:58:01');
INSERT INTO `eh_parking_recharge_orders` VALUES (10002, 'community', 240111044331048623, 10001, '粤B88888', '测试', '13212341234', 1233, 1025, '13212341234', '2016-4-1 17:56:31', 'BOSIGAO', '13485', '10001', '3个月', 3.00, 1200.00, 2, 1, '2016-4-1 17:57:51', 1025, '2016-4-1 17:58:01');
INSERT INTO `eh_parking_recharge_orders` VALUES (10003, 'community', 240111044331048623, 10001, '粤B88888', '测试', '13212341234', 1233, 1025, '13212341234', '2016-4-1 17:56:31', 'BOSIGAO', '13485', '10001', '3个月', 3.00, 1200.00, 2, 1, '2016-4-1 17:57:51', 1025, '2016-4-1 17:58:01');

INSERT INTO `eh_configurations`(`name`, `value`, `description`, `namespace_id`) VALUES ('pay.appKey','7bbb5727-9d37-443a-a080-55bbf37dc8e1','pay.appKey',0);
INSERT INTO `eh_apps` (`creator_uid`,`app_key`,`secret_key`,`name`,`description`,`status`,`create_time`,`update_uid`,`update_time`) VALUES ('1', '7bbb5727-9d37-443a-a080-55bbf37dc8e1', '1k0ty3aZPC8bjMm8V9+pFmsU5B7cImfQXB4GUm4ACSFPP1IhZI5basNbUBXe7p6gJ7OC8J03DW1U8fvvtpim6Q==', 'pay.app', 'pay.app', '1', '2016-01-12 14:52:06', null, null);


