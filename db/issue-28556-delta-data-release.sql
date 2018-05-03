-- issue-28556 add by huangmingbo 2018-05-03
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('parking', '10032', 'zh_CN', '未查询到月卡类型信息');

-- issue-28556 add by huangmingbo 2018-05-03
SET @car_type_id = (SELECT IFNULL(MAX(id),0) FROM eh_parking_card_types);
INSERT INTO `eh_parking_card_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `card_type_id`, `card_type_name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES (@car_type_id:=@car_type_id+1, 999983, 'community', 240111044332060208, 10031, '4', '普通客户', 2, 1, '2018-05-03 10:49:48', NULL, NULL);
INSERT INTO `eh_parking_card_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `card_type_id`, `card_type_name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES (@car_type_id:=@car_type_id+1, 999983, 'community', 240111044332060208, 10031, '10', '平安月卡', 2, 1, '2018-05-03 10:49:48', NULL, NULL);
