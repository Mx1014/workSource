-- 电商运营数据api   add by xq.tian 2017/02/23
SET @conf_id := (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES(@conf_id := @conf_id+1, 'biz.business.promotion.api', '/Zl-MallMgt/shopCommo/admin/queryRecommendList.ihtml', 'biz promotion data api', '0', '电商首页运营数据api');

UPDATE `eh_launch_pad_items` SET `action_data`='{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdefault%3Fpos%3D1%26_k%3Dzlbiz#sign_suffix"}'
WHERE `namespace_id`=999985 AND `item_group`='OPPushBiz' AND `item_label`='OE优选';