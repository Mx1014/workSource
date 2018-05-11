-- issue-26498 停车缴费V6.5.2（深圳湾项目小猫接口更新） by huangmingbo 2018.5.10 
UPDATE `eh_configurations` SET `value`='http://119.23.144.8' WHERE  name='parking.xiaomao.url';
UPDATE `eh_configurations` SET `value`='0755000021433988491' WHERE  name='parking.xiaomao.parkId.10011';
UPDATE `eh_configurations` SET `value`='07550002501499136602' WHERE  name='parking.xiaomao.parkId.10012';

INSERT INTO `eh_configurations` (`name`, `value`) VALUES ('parking.xiaomao.accessKeyId.10011', 'rjcy0504');
INSERT INTO `eh_configurations` (`name`, `value`) VALUES ('parking.xiaomao.accessKeyId.10012', 'ctds0503');
INSERT INTO `eh_configurations` (`name`, `value`) VALUES ('parking.xiaomao.accessKeyValue.10011', '7c9f305115024764a3a74a44040f986b');
INSERT INTO `eh_configurations` (`name`, `value`) VALUES ('parking.xiaomao.accessKeyValue.10012', '1accf1d97436aadf2a31936b3c3e4184');
INSERT INTO `eh_configurations` (`name`, `value`, `description`) VALUES ('parking.xiaomao.free.time', '15', '临时停车默认免费时长');

delete from eh_configurations where name = 'parking.xiaomao.accessKeyId' ;
delete from eh_configurations where name = 'parking.xiaomao.accessKeyValue' ;
delete from eh_configurations where name = 'parking.xiaomao.types.10011' ;
delete from eh_configurations where name = 'parking.xiaomao.types.10012' ;

--  issue-26498 add by huangmingbo 
SET @ruanji_id = 10011;
SET @chuangtou_id = 10012;
SET @ruanji_community_id = 240111044331050370;
SET @chuangtou_community_id = 240111044331050371;
SET @szw_namespace_id = 999966;

-- issue-26498 add by huangmingbo 2018-05-09
SET @car_type_id = (SELECT IFNULL(MAX(id),0) FROM eh_parking_card_types);
INSERT INTO `eh_parking_card_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `card_type_id`, `card_type_name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES (@car_type_id:=@car_type_id+1, @szw_namespace_id, 'community', @chuangtou_community_id, @chuangtou_id, '02', 'VIP月卡', 2, 1, '2018-05-09 10:49:48', NULL, NULL);
INSERT INTO `eh_parking_card_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `card_type_id`, `card_type_name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES (@car_type_id:=@car_type_id+1, @szw_namespace_id, 'community', @ruanji_community_id, @ruanji_id, '11', 'VIP月卡', 2, 1, '2018-05-09 10:49:48', NULL, NULL);
INSERT INTO `eh_parking_card_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `card_type_id`, `card_type_name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES (@car_type_id:=@car_type_id+1, @szw_namespace_id, 'community', @ruanji_community_id, @ruanji_id, '5', '普通月卡', 2, 1, '2018-05-09 10:49:48', NULL, NULL);

-- issue-26498 add by huangmingbo 2018-05-09
UPDATE `eh_parking_lots` SET `config_json`='{"tempfeeFlag": 1, "rateFlag": 1, "lockCarFlag": 0, "searchCarFlag": 0, "currentInfoType": 2, "contact": "18665331243","identityCardFlag":0}' WHERE  `id`in (10011, 10012);
