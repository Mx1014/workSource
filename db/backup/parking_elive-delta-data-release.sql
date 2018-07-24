INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`) VALUES ('10029', 'community', '240111044332060075', '住总停车场', 'ELIVE_JIESHUN', '', '2', '1', NOW(), '999955', null, '{"tempfeeFlag": 1, "rateFlag": 0, "lockCarFlag": 0, "searchCarFlag": 0, "currentInfoType": 2,"identityCardFlag":0}');

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.elivejieshun.loginurl', 'http://112.74.43.203/jsaims/login', '住总停车场logonurl', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.elivejieshun.funurl', 'http://112.74.43.203/jsaims/as', '住总停车场funurl', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.elivejieshun.cid', '8000004', '住总停车场cid', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.elivejieshun.usr', '8000004', '住总停车场usr', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.elivejieshun.psw', '8000004', '住总停车场psw', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.elivejieshun.v', '2', '住总停车场v', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.elivejieshun.signKey', '', '住总停车场signKey', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.elivejieshun.parkCode', '20171113', '住总停车场parkCode', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.elivejieshun.areaCode', '20171113', '住总停车场areaCode', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.elivejieshun.businesserCode', 'jsdstest', '住总停车场businesserCode', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.elivejieshun.personCode', 'SZ000014', '住总停车场personCode', 0, NULL);

