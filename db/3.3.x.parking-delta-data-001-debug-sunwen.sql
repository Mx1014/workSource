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

update eh_parking_lots set vendor_name = 'BOSIGAO';