
-- 电商运营数据api   add by xq.tian 2017/03/01
SET @conf_id := (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES(@conf_id := @conf_id+1, 'biz.business.promotion.api', '/Zl-MallMgt/shopCommo/admin/queryRecommendList.ihtml', 'biz promotion data api', '0', '电商首页运营数据api');

UPDATE `eh_launch_pad_items` SET `action_data`='{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%23%2fmicroshop%2fhome%3fisfromindex%3d0%26_k%3dzlbiz%23sign_suffix"}'
WHERE `namespace_id`=999985 AND `item_group`='OPPushBiz' AND `item_label`='OE优选';

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10138, '0', '��ҵ���� ����Ȩ��', '��ҵ���� ����Ȩ��', NULL);
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`, `role_type`)
	SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `id`, 1001, 0, 1, now(), 'EhAclRoles' FROM `eh_acl_privileges` WHERE id = 10138;
	
INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ('pmtask', '10012', 'zh_CN', 'û�д���Ȩ�ޣ�');
INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ('pmtask', '10013', 'zh_CN', '�鲻�����û���Ϣ��');
