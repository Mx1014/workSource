-- rental3.5 by st.zheng
update eh_locale_strings set text = '用户姓名' where scope = 'rental.flow' and `code` = 'user';
update eh_locale_strings set text = '联系方式' where scope = 'rental.flow' and `code` = 'contact';
update eh_locale_strings set text = '公司名称' where scope = 'rental.flow' and `code` = 'organization';
INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ( 'rental.flow', 'spec', 'zh_CN', '资源规格');
INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ( 'rental.flow', 'address', 'zh_CN', '资源地址');
update eh_locale_strings set text = '免费物资' where scope = 'rental.flow' and `code` = 'goodItem';
update eh_locale_strings set text = '付费商品' where scope = 'rental.flow' and `code` = 'item';
update eh_locale_strings set text = '预订数量' where scope = 'rental.flow' and `code` = 'count';
INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ( 'rental.notification', '13', 'zh_CN', '亲爱的用户，为保障资源使用效益，现在取消订单，系统将不予退款，恳请您谅解。');

SET @ns_id = 0;
SET @module_id = 40400;
SET @entity_type = 'flow_button'; -- 节点参数为：flow_node， 按钮参数为：flow_button
SET @flow_predefined_params_id = IFNULL((SELECT MAX(id) FROM `eh_flow_predefined_params`), 1);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', @module_id, 'any-module', @entity_type, '线上支付', 'onlinePay', '{"nodeType":"ONLINE_PAY"}', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', @module_id, 'any-module', @entity_type, '线下支付', 'offlinePay', '{"nodeType":"OFFLINE_PAY"}', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', @module_id, 'any-module', @entity_type, '调整费用', 'changeAmount', '{"nodeType":"CHANGE_AMOUNT"}', 2, NULL, NULL, NULL, NULL);

delete from eh_flow_predefined_params where module_id = 40400 and name = '已完成';
INSERT INTO `eh_rentalv2_holiday` (`id`, `holiday_type`, `close_date`) VALUES ('1', '1', '1515168000000,1515254400000,1515772800000,1515859200000,1516377600000,1516464000000,1516982400000,1517068800000,1517587200000,1517673600000,1518192000000,1518278400000,1518796800000,1518883200000,1519401600000,1519488000000,1520006400000,1520092800000,1520611200000,1520697600000,1521216000000,1521302400000,1521820800000,1521907200000,1522425600000,1522512000000,1523030400000,1523116800000,1523635200000,1523721600000,1524240000000,1524326400000,1524844800000,1524931200000,1525449600000,1525536000000,1526054400000,1526140800000,1526659200000,1526745600000,1527264000000,1527350400000,1527868800000,1527955200000,1528473600000,1528560000000,1529078400000,1529164800000,1529683200000,1529769600000,1530288000000,1530374400000,1530892800000,1530979200000,1531497600000,1531584000000,1532102400000,1532188800000,1532707200000,1532793600000,1533312000000,1533398400000,1533916800000,1534003200000,1534521600000,1534608000000,1535126400000,1535212800000,1535731200000,1535817600000,1536336000000,1536422400000,1536940800000,1537027200000,1537545600000,1537632000000,1538150400000,1538236800000,1538755200000,1538841600000,1539360000000,1539446400000,1539964800000,1540051200000,1540569600000,1540656000000,1541174400000,1541260800000,1541779200000,1541865600000,1542384000000,1542470400000,1542988800000,1543075200000,1543593600000,1543680000000,1544198400000,1544284800000,1544803200000,1544889600000,1545408000000,1545494400000,1546012800000,1546099200000');
INSERT INTO `eh_rentalv2_holiday` (`id`, `holiday_type`, `close_date`) VALUES ('2', '2', '1546099200000, 1546012800000, 1545494400000, 1545408000000, 1544889600000, 1544803200000, 1544284800000, 1544198400000, 1543680000000, 1543593600000, 1543075200000, 1542988800000, 1542470400000, 1542384000000, 1541865600000, 1541779200000, 1541260800000, 1541174400000, 1540656000000, 1540569600000, 1540051200000, 1539964800000, 1539446400000, 1539360000000, 1538841600000, 1538755200000, 1538668800000, 1538582400000, 1538496000000, 1538409600000, 1538323200000, 1537718400000, 1537632000000, 1537545600000, 1537027200000, 1536940800000, 1536422400000, 1536336000000, 1535817600000, 1535731200000, 1535212800000, 1535126400000, 1534608000000, 1534521600000, 1534003200000, 1533916800000, 1533398400000, 1533312000000, 1532793600000, 1532707200000, 1532188800000, 1532102400000, 1531584000000, 1531497600000, 1530979200000, 1530892800000, 1530374400000, 1530288000000, 1529769600000, 1529683200000, 1529251200000, 1529164800000, 1529078400000, 1528560000000, 1528473600000, 1527955200000, 1527868800000, 1527350400000, 1527264000000, 1526745600000, 1526659200000, 1526140800000, 1526054400000, 1525536000000, 1525449600000, 1525104000000, 1525017600000, 1524931200000, 1524326400000, 1524240000000, 1523721600000, 1523635200000, 1523030400000, 1522944000000, 1522857600000, 1522512000000, 1522425600000, 1521907200000, 1521820800000, 1521302400000, 1521216000000, 1520697600000, 1520611200000, 1520092800000, 1520006400000, 1519488000000, 1519401600000, 1518883200000, 1518796800000, 1518278400000, 1518192000000, 1517673600000, 1517587200000, 1517068800000, 1516982400000, 1516464000000, 1516377600000, 1515859200000, 1515772800000, 1515254400000, 1515168000000,1514736000000');

UPDATE eh_rentalv2_default_rules
RIGHT JOIN eh_organization_communities ON eh_rentalv2_default_rules.owner_id = eh_organization_communities.organization_id
SET eh_rentalv2_default_rules.owner_type = 'community',
 eh_rentalv2_default_rules.owner_id = eh_organization_communities.community_id
WHERE
	eh_rentalv2_default_rules.source_type = 'default_rule'
AND eh_rentalv2_default_rules.owner_type = 'organization';

update eh_configurations set VALUE = '/resource-rental/build/index.html#/resource-detail?namespaceId=%s&rentalSiteId=%s&canRental=1&isApp=1'
where name = 'rental.resource.detail.url';