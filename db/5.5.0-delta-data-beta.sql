-- issue-26471 服务热线V1.5（客服聊天记录保存和导出) by huangmingbo 2018.5.8  start
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('hotline', '10001', 'zh_CN', '相同号码已存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('hotline', '10002', 'zh_CN', '查询记录时未指定客服或热线');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('hotline', '10003', 'zh_CN', '查询记录时未指定用户');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('hotline', '10004', 'zh_CN', '需要更新/删除的记录不存在');

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40310, '公共热线', 40300, '/40000/40300/40310', 1, 3, 2, 0, '2018-04-02 17:18:58', NULL, NULL, '2018-05-02 17:18:58', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40320, '专属客服', 40300, '/40000/40300/40320', 1, 3, 2, 0, '2018-04-02 17:18:58', NULL, NULL, '2018-05-02 17:18:58', 0, 1, '1', NULL, '');

SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@mp_id:=@mp_id+1, 40310, 0, 4030040310, '公共热线', 0, '2018-04-02 17:18:58');
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@mp_id:=@mp_id+1, 40320, 0, 4030040320, '专属客服', 0, '2018-04-02 17:18:58');
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@mp_id:=@mp_id+1, 40320, 0, 4030040321, '历史会话', 0, '2018-04-02 17:18:58');

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4030040310, 0, '公共热线 全部权限', '公共热线 全部权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4030040320, 0, '专属客服 客服管理', '专属客服 客服管理', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4030040321, 0, '专属客服 历史会话', '专属客服 历史会话', NULL);

-- by st.zheng 路福联合广场 停车场配置
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.fujica.parkingCode', '17000104590501', 'parkingCode', '0', NULL);
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.fujica.appid', 'fujica_dacfe36d3f15ae51', 'appid', '0', NULL);
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.fujica.appSecret', '69416bc8b9e44d56b7672d18b1106668', 'appSecret', '0', NULL);
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.fujica.privateKey', 'MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALATu5WNNx+XcrASNJieGRsY7VryQjcs+pABXGh4BOCqAW3FgAAc21aIG0uxvYHdQUw1I7J80RiQJ506aIktGTmZKJTjez7JNF7GO4ieGmLMI3ds8iXyJrfUMp/1U3Cq6vja8vuRIKYVQCxZe8un98Nr03F7oMlKpBydJQKVdlAHAgMBAAECgYBPcqfqhAyCWbCrF5vZ3URQwL+gkL0l7kqknaiXjsgMo0j/weTOqDaj5cgDMJDkvvPOsg+IYt9qKOlm/Urb0piVb9v9iAnp6gIppcyHrPUesgYRSbqR/HhdGtnzn/4f45YpkXA6URMfc04xBrIUCw5DmnqonJZJkjd6jjXiL65rYQJBAN8eg99yFH45D2cdlPBTN0LVWXgqy3N22byYh0j6kgo25WOPMcuIZPM/XdSoTfhYeHixvEwrmCAkCmSkf524prECQQDKBnzZRj3eCij1z2SZJCRZhRQUhFmK0AYYTXw9R0DfEaZNrEAe8PEypS94lYROvNV3TnSnK2FHKeH8YhHXEoA3AkBRHzMrRrsUuYJUJ3lDd74b2p5RBp46OPgpjfuCGTiH5jW44RNlwQ2TM3LWIutWZDRJDbY8q40AApqUxQpxOfXBAkA8bB5RGZoNW7qOcj3jM5UPlSbBUCg7xSXdhOdAqJv1W6ECoB75YhSxkggVp5pPtlid+0AWc3n/v74QLwCo86aXAkEAlxY3hoxQjJiCjD2PbL0GLkMKNBqJgLcDWqeoHcal+iY2Fpr3U/iCJZNoBuovC2Qvc5J1y61PojrvFNyFMEkWTQ==', 'appid', '0', NULL);
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.fujica.url', 'http://mops-test.fujica.com.cn:8021/Api', 'url', '0', NULL);


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


-- 路福联合广场对接的秘钥 by 杨崇鑫
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) 
VALUES ('openapi.lufu.key', '70f2ea6d54fb44d5a18ac11f66d25154', '路福联合广场对接的秘钥', 999963, NULL);





-- 先ES所在serevr执行下 curl -XDELETE localhost:9200/everhomesv3/enterpriseCustomer
-- 1: 迁移organization to customer
-- /customer/syncOrganizationToCustomer


-- 2:同步下客户管理ES
-- /customer/syncEnterpriseCustomerIndex


--  3:同步下企业管理ES
--  /org/syncIndex




