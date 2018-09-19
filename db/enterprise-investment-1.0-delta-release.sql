
set @eecid=(select max(id)+1 from `eh_var_fields`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'entryStatusItemId', '入驻状态', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"select\", \"length\": 32}');
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (@eeciId, 'enterprise_customer', @eecid, '未入驻', 1, 2, 1, sysdate(), NULL, NULL, NULL);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (@eeciId, 'enterprise_customer', @eecid, '已入驻', 2, 2, 1, sysdate(), NULL, NULL, NULL);



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


update eh_var_fields set mandatory_flag = 1 where id =12111;
update eh_var_fields set group_id = 10,group_path = '/1/10' where id in (select field_id from eh_var_field_ranges where module_name = 'investment_promotion');

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
	