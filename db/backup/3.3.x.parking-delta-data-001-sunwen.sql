INSERT INTO `eh_parking_lots` VALUES (10001, 'community', 240111044331048623, '科技工业园大厦停车场', 'BOSIGAO', NULL, 3, 2, 1025, '2016-3-31 17:07:20');


INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
VALUES ('62', 'parking.communityId', '240111044331048623', 'parking.communityId', '1000000', '停车场ID(兼容停车充值1.0版本)');

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
VALUES ('62', 'parking.parkingLotId', '10001', 'parking.parkingLotId', '1000000', '园区ID(兼容停车充值1.0版本)');
