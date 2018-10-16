
-- AUTH 黄鹏宇 2018年9月11日
-- REMARK 根据用户等级分类是招商客户还是租客
UPDATE eh_enterprise_customers SET customer_source = 1 where level_item_id = 6;
UPDATE eh_enterprise_customers SET customer_source = 0 where level_item_id <> 6 || level_item_id is null;




-- AUTHOR  jiarui  20180831
-- REMARK:迁移联系人数据
DROP PROCEDURE if exists transfer_contact;
delimiter //
CREATE PROCEDURE `transfer_contact` ()
BEGIN
  DECLARE ns INTEGER;
  DECLARE customerSource TINYINT;
  DECLARE communityId LONG;
  DECLARE customerId LONG;
  DECLARE contactName VARCHAR(1024);
  DECLARE contactPhone VARCHAR(1024);
  DECLARE done INT DEFAULT FALSE;
  DECLARE k_id LONG DEFAULT (IFNULL((SELECT MAX(id) from eh_customer_contacts),0));
  DECLARE cur CURSOR FOR select id, namespace_id,community_id,customer_source, contact_name,contact_phone from eh_enterprise_customers where status = 2;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  OPEN cur;
  read_loop: LOOP
                FETCH cur INTO customerId,ns,communityId,customerSource,contactName,contactPhone;
                IF done THEN
                    LEAVE read_loop;
                END IF;
				set k_id = k_id +1;

                INSERT INTO eh_customer_contacts VALUES(k_id,ns,communityId,customerId,contactName,contactPhone,null,null,null,0,customerSource,2,now(),1,null,null);
  END LOOP;
  CLOSE cur;
END
//
delimiter ;
CALL transfer_contact;



-- AUTHOR  jiarui  20180910
-- REMARK:迁移跟进人数据
DROP PROCEDURE if exists migrate_tracker;
delimiter //
CREATE PROCEDURE `migrate_tracker` ()
BEGIN
  DECLARE ns INTEGER;
  DECLARE customerSource TINYINT;
  DECLARE communityId LONG;
  DECLARE customerId LONG;
  DECLARE trackerId LONG;
  DECLARE done INT DEFAULT FALSE;
  DECLARE k_id LONG DEFAULT (IFNULL((SELECT MAX(id) from eh_customer_trackers ),0));
  DECLARE cur CURSOR FOR select id, namespace_id,community_id,customer_source, tracking_uid from eh_enterprise_customers where status = 2;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  OPEN cur;
  read_loop: LOOP
                FETCH cur INTO customerId,ns,communityId,customerSource,trackerId;
                IF done THEN
                    LEAVE read_loop;
                END IF;
				        set k_id = k_id +1;
                INSERT INTO eh_customer_trackers VALUES(k_id,ns,communityId,customerId,trackerId,customerSource,customerSource,2,NOW(),1,null,null);
  END LOOP;
  CLOSE cur;
END
//
delimiter ;
CALL migrate_tracker;
-- END


-- AUTHOR:黄鹏宇 2018-9-12
-- REMARK:迁移客户表单field进入range表中

DROP PROCEDURE if exists transfer_field;
delimiter //
CREATE DEFINER=`root`@`%` PROCEDURE `transfer_field`()
BEGIN
  DECLARE ns INTEGER;
  DECLARE field_id BIGINT;
	DECLARE group_path_1 VARCHAR(128);
  DECLARE k_id LONG DEFAULT (IFNULL((SELECT MAX(id) from eh_var_field_ranges),0));
	DECLARE done INT DEFAULT FALSE;
  DECLARE cur CURSOR FOR select id,group_path from eh_var_fields where module_name = 'enterprise_customer' and status = 2;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  OPEN cur;
  read_loop: LOOP
                FETCH cur INTO field_id,group_path_1;
                IF done THEN
                    LEAVE read_loop;
                END IF;
				set k_id = k_id +1;

                INSERT INTO eh_var_field_ranges VALUES(k_id,group_path_1,field_id,'enterprise_customer','enterprise_customer');
  END LOOP;
  CLOSE cur;
END
//
delimiter ;
CALL transfer_field;
-- END


-- AUTHOR:黄鹏宇 2018-9-12
-- REMARK:迁移客户表单field_group进入range表中
DROP PROCEDURE if exists transfer_field_group;
delimiter //
CREATE DEFINER=`root`@`%` PROCEDURE `transfer_field_group`()
BEGIN
  DECLARE ns INTEGER;
  DECLARE group_id BIGINT;
  DECLARE k_id LONG DEFAULT (IFNULL((SELECT MAX(id) from eh_var_field_group_ranges),0));
	DECLARE done INT DEFAULT FALSE;
  DECLARE cur CURSOR FOR select id from eh_var_field_groups where module_name = 'enterprise_customer' and status = 2;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  OPEN cur;
  read_loop: LOOP
                FETCH cur INTO group_id;
                IF done THEN
                    LEAVE read_loop;
                END IF;
				set k_id = k_id +1;

                INSERT INTO eh_var_field_group_ranges VALUES(k_id,group_id,'enterprise_customer','enterprise_customer');
  END LOOP;
  CLOSE cur;
END
//
delimiter ;
CALL transfer_field_group;


set @eecid=(select max(id)+1 from `eh_var_fields`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'entryStatusItemId', '入驻状态', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"select\", \"length\": 32}');
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (@eeciId, 'enterprise_customer', @eecid, '未入驻', 1, 2, 1, sysdate(), NULL, NULL, NULL);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (@eeciId, 'enterprise_customer', @eecid, '已入驻', 2, 2, 1, sysdate(), NULL, NULL, NULL);


set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (@eeciId, 'enterprise_customer', 5, '流失客户', 2, 2, 1, sysdate(), NULL, NULL, NULL);



INSERT INTO `eh_var_field_groups`(`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (20001, 'investment_enterprise', 0, '/20001' , '招商客户信息', 'com.everhomes.customer.EnterpriseCustomer', 0, NULL, 2, 1, SYSDATE(), NULL, NULL);

INSERT INTO `eh_var_field_groups`(`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (20002, 'investment_enterprise', 20001, '/20001/20002' , '基本信息', 'com.everhomes.customer.EnterpriseCustomer', 0, NULL, 2, 1, SYSDATE(), NULL, NULL);


set @eecid=(select max(id) from `eh_var_fields`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid:=@eecid+1, 'enterprise_customer', 'customerContact', '客户联系人', 'String', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid:=@eecid+1, 'enterprise_customer', 'channelContact', '渠道联系人', 'String', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid:=@eecid+1, 'enterprise_customer', 'trackerUid', '招商跟进人', 'String', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid:=@eecid+1, 'enterprise_customer', 'transactionRatio', '成交几率', 'String', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid:=@eecid+1, 'enterprise_customer', 'expectedSignDate', '预计签约时间', 'Date', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"datetime\", \"length\": 32}');


SET @id = IFNULL((select max(id) from eh_var_field_ranges), 1);
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',2,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',5,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',6,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',24,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',12111,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',12112,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',12113,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',12114,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',12115,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',12073,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',48,'investment_promotion','enterprise_customer');


UPDATE eh_var_fields SET field_param = '{\"fieldParamType\": \"unRenameSelect\", \"length\": 32}' WHERE id = 5;
UPDATE eh_var_fields SET field_param = '{\"fieldParamType\": \"text\", \"length\": 32}' WHERE id = 12037;
UPDATE eh_var_fields SET field_param = '{\"fieldParamType\": \"text\", \"length\": 32}' WHERE id = 12041;

UPDATE eh_var_fields SET mandatory_flag = 1 WHERE id =12111;
UPDATE eh_var_fields SET group_id = 10,group_path = '/1/10' WHERE id IN (SELECT field_id FROM eh_var_field_ranges WHERE module_name = 'investment_promotion');

SET @id = (select max(id) from eh_var_field_group_ranges);
INSERT INTO `eh_var_field_group_ranges`(`id`, `group_id`, `module_name`, `module_type`) VALUES (@id:=@id+1, 1, 'investment_promotion', 'enterprise_customer');
INSERT INTO `eh_var_field_group_ranges`(`id`, `group_id`, `module_name`, `module_type`) VALUES (@id:=@id+1, 10, 'investment_promotion', 'enterprise_customer');

DELETE FROM eh_var_field_ranges WHERE field_id = 12109 AND module_name='enterprise_customer' AND module_type='enterprise_customer';
DELETE FROM eh_var_field_ranges WHERE field_id = 5 AND module_name='enterprise_customer' AND module_type='enterprise_customer';


-- AUTHOR 黄鹏宇 2018年9月11日
-- REMARK 增加招商客户权限细化
SET @id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150001, '查看客户权限', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150002, '创建客户权限', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150003, '编辑客户权限', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150004, '删除客户权限', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150005, '一键转为租客权限', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150006, '签约权限', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150007, '续约权限', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150008, '导入权限', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150009, '导出权限', 0, SYSDATE());


INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150001, 0, '招商管理 查看客户权限', '招商管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150002, 0, '招商管理 创建客户权限', '招商管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150003, 0, '招商管理 编辑客户权限', '招商管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150004, 0, '招商管理 删除客户权限', '招商管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150005, 0, '招商管理 一键转为租客权限', '招商管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150006, 0, '招商管理 签约权限', '招商管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150007, 0, '招商管理 续约权限', '招商管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150008, 0, '招商管理 导入权限', '招商管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150009, 0, '招商管理 导出权限', '招商管理 业务模块权限', NULL);

update eh_var_fields set display_name = '拜访人' where id =211;
update eh_var_fields set display_name = '拜访时间' where id =11002;
update eh_var_fields set display_name = '拜访内容' where id =11003;
update eh_var_fields set display_name = '拜访方式' where id =11004;
update eh_var_fields set display_name = '拜访时间' where id =11006;
update eh_var_fields set display_name = '拜访方式' where id =11010;
update eh_var_fields set display_name = '拜访人' where id = 11011;
update eh_var_fields set display_name = '拜访时长(小时)' where id = 12057;
update eh_var_field_groups set title = '拜访信息' where id =24;


-- END


-- END

-- AUTHOR: 杨崇鑫
-- REMARK: 房源招商增加“客户账单”动态表单
INSERT INTO `eh_var_field_groups`(`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) 
	VALUES (38, 'enterprise_customer', 0, '/38', '客户账单', '', 0, NULL, 2, 1, SYSDATE(), NULL, NULL);
set @id = IFNULL((SELECT max(id) from eh_var_field_group_ranges),1);
INSERT INTO `eh_var_field_group_ranges`(`id`, `group_id`, `module_name`, `module_type`) VALUES (@id:=@id+1, 38, 'enterprise_customer', 'enterprise_customer');

-- AUTHOR: 黄鹏宇
-- REMARK: 房源招商增加“活动记录”动态表单
INSERT INTO `eh_var_field_groups`(`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`)
	VALUES (39, 'enterprise_customer', 0, '/39', '活动记录', '', 0, NULL, 2, 1, SYSDATE(), NULL, NULL);
set @id = IFNULL((SELECT max(id) from eh_var_field_group_ranges),1);
INSERT INTO `eh_var_field_group_ranges`(`id`, `group_id`, `module_name`, `module_type`) VALUES (@id:=@id+1, 39, 'enterprise_customer', 'enterprise_customer');


-- AUTHOR: 黄鹏宇
-- REMARK: 更改客户状态为不可更改
UPDATE eh_var_fields set

-- AUTHOR: 黄鹏宇
-- REMARK: 更改企业客户管理菜单为租客管理
update eh_web_menus set name = '租客管理' where name = '企业客户管理';
-- END

-- AUTHOR: 黄鹏宇
-- REMARK: 导出招商本地化
set @id = (select max(id) from `eh_locale_strings`);
INSERT INTO `ehcore`.`eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id := @id +1, 'enterpriseCustomer.export', '2', 'zh_CN', '招商客户数据导出');


UPDATE eh_var_fields SET display_name = '行业领域' WHERE id = 24;

-- AUTHOR 黄鹏宇
-- REMARK 同步名称
UPDATE eh_var_field_item_scopes a inner join eh_var_field_items b on a.item_id = b.id SET a.item_display_name = b.display_name;
UPDATE eh_var_field_scopes a inner join eh_var_fields b on a.field_id = b.id SET a.field_display_name = b.display_name, a.field_param = b.field_param;


-- AUTHOR 黄鹏宇
-- REMARK 增加默认字段

set @item_id = (select max(id) from `eh_var_field_group_scopes`);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'investment_promotion', 1, '客户信息', 1, 2, 1, '2018-09-20 19:11:22', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'investment_promotion', 10, '基本信息', 2, 2, 1, '2018-09-20 19:11:22', NULL, NULL, NULL, NULL, NULL);


set @item_id = (select max(id) from `eh_var_field_scopes`);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 2, '{\"fieldParamType\": \"text\", \"length\": 32}', '客户名称', 1, 1, 2, 1, '2018-09-20 19:13:15', NULL, NULL, NULL, '/1/10', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 5, '{\"fieldParamType\": \"unRenameSelect\", \"length\": 32}', '客户状态', 1, 2, 2, 1, '2018-09-20 19:13:15', NULL, NULL, NULL, '/1/10', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 12111, '{\"fieldParamType\": \"text\", \"length\": 32}', '客户联系人', 1, 3, 2, 1, '2018-09-20 19:13:15', NULL, NULL, NULL, '/1/10', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 24, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}', '行业领域', 0, 4, 2, 1, '2018-09-20 19:13:15', NULL, NULL, NULL, '/1/10', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 12114, '{\"fieldParamType\": \"text\", \"length\": 32}', '成交几率', 0, 5, 2, 1, '2018-09-20 19:13:15', NULL, NULL, NULL, '/1/10', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 12115, '{\"fieldParamType\": \"datetime\", \"length\": 32}', '预计签约时间', 0, 6, 2, 1, '2018-09-20 19:13:15', NULL, NULL, NULL, '/1/10', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 12112, '{\"fieldParamType\": \"text\", \"length\": 32}', '渠道联系人', 0, 7, 2, 1, '2018-09-20 19:13:15', NULL, NULL, NULL, '/1/10', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 48, '{\"fieldParamType\": \"multiText\", \"length\": 2048}', '备注', 0, 8, 2, 1, '2018-09-20 19:13:15', NULL, NULL, NULL, '/1/10', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 12073, '{\"fieldParamType\": \"file\", \"length\": 9}', '附件', 0, 9, 2, 1, '2018-09-20 19:13:15', NULL, NULL, NULL, '/1/10', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 6, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}', '客户来源', 0, 10, 2, 1, '2018-09-20 19:13:15', NULL, NULL, NULL, '/1/10', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 12113, '{\"fieldParamType\": \"text\", \"length\": 32}', '招商跟进人', 0, 11, 2, 1, '2018-09-20 19:13:15', NULL, NULL, NULL, '/1/10', NULL);

set @item_id = (select max(id) from `eh_var_field_item_scopes`);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 5, 3, '初次接触', 1, 2, 1, '2018-09-20 19:14:42', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 5, 4, '潜在客户', 2, 2, 1, '2018-09-20 19:14:42', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 5, 5, '意向客户', 3, 2, 1, '2018-09-20 19:14:42', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 5, 6, '已成交客户', 4, 2, 1, '2018-09-20 19:14:42', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 5, 7, '历史客户', 5, 2, 1, '2018-09-20 19:14:42', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 5, 12169, '流失客户', 6, 2, 1, '2018-09-20 19:14:42', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 202, '集成电路', 1, 2, 1, '2018-09-20 19:14:42', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 203, '软件', 2, 2, 1, '2018-09-20 19:14:42', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 204, '通信技术', 3, 2, 1, '2018-09-20 19:14:42', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 205, '生物医药', 4, 2, 1, '2018-09-20 19:14:42', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 206, '医疗器械', 5, 2, 1, '2018-09-20 19:14:42', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 207, '光机电', 6, 2, 1, '2018-09-20 19:14:43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 208, '金融服务', 7, 2, 1, '2018-09-20 19:14:43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 209, '新能源与环保', 8, 2, 1, '2018-09-20 19:14:43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 210, '文化创意', 9, 2, 1, '2018-09-20 19:14:43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 211, '商业-餐饮', 10, 2, 1, '2018-09-20 19:14:43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 212, '商业-超市', 11, 2, 1, '2018-09-20 19:14:43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 213, '商业-食堂', 12, 2, 1, '2018-09-20 19:14:43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 214, '商业-其他', 13, 2, 1, '2018-09-20 19:14:43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 215, '其他', 14, 2, 1, '2018-09-20 19:14:43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 6, 8, '其他', 1, 2, 1, '2018-09-20 19:14:43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 6, 192, '广告', 2, 2, 1, '2018-09-20 19:14:43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 6, 193, '报纸', 3, 2, 1, '2018-09-20 19:14:43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 6, 194, '中介', 4, 2, 1, '2018-09-20 19:14:43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 6, 195, '网站', 5, 2, 1, '2018-09-20 19:14:43', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 6, 196, '活动', 6, 2, 1, '2018-09-20 19:14:43', NULL, NULL, NULL, NULL, NULL);
