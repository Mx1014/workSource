-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR: 黄明波  20180703
-- REMARK: 服务联盟v3.4 issue-29989
-- REMARK: 更新服务的封面图，迁移服务广场的item
-- REMARK: /yellowPage/transferPosterUriToAttachment  参数:1802
-- REMARK: /yellowPage/transferLaunchPadItems 参数:1802
-- REMARK: 上线后清一下redis缓存。定时器有缓存问题。

-- AUTHOR: 邓爽
-- REMARK: 上线完成后请调用以下两个接口做停车缴费收款方数据迁移
-- REMARK: /parking/rechargeOrderMigration 迁移支付系统订单号到停车订单表

-- AUTHOR: 杨崇鑫  20180704
-- REMARK: 上线完成后请调用以下接口做物业缴费以前订单支付方式的数据迁移 by 杨崇鑫
-- REMARK: /asset/transferOrderPaymentType

-- AUTHOR: 梁燕龙 20180702
-- REMARK: 活动支付订单迁移，在执行迁移语句前，将eh_activity_roster，eh_payment_order_records这两张表进行全表备份


-- AUTHOR: 梁燕龙 20180702
-- REMARK: 活动收款方账号迁移，在执行eh_activity_biz_payee语句前，请与 陈毅峰 对照一下域空间是否有遗漏；

-- AUTHOR: 郑思挺
-- REMARK: 预约收款账户迁移，在执行eh_rentalv2_pay_accounts语句前，请与 陈毅峰 对照一下域空间是否有遗漏；

-- AUTHOR: 杨崇鑫  20180704
-- REMARK: 物业收款账户迁移，在执行eh_payment_bill_groups语句前，请与 陈毅峰 对照一下域空间是否有遗漏；

-- AUTHOR: 邓爽
-- REMARK: 预约收款账户迁移，在执行eh_parking_business_payee_accounts,eh_siyin_print_business_payee_accounts语句前，请与 陈毅峰 对照一下域空间是否有遗漏；

-- AUTHOR: 唐岑 2018年7月4日16:07:37
-- REMARK: 升级后需要执行db/search/contract.sh脚本，执行脚本后，需执行接口/contract/syncContracts，重新同步数据库中的合同数据到es中

-- AUTHOR: 唐岑 2018年7月4日22:38:16
-- REMARK: 升级后需要执行db/search/pmowner.sh脚本，更改pmowner的mapping结构；执行脚本后，需执行接口/pm/syncOwnerIndex，重新同步个人客户数据到es中

-- --------------------- SECTION END ---------------------------------------------------------






-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 郑思挺
-- REMARK: 数据迁移
SET @id = ifnull((SELECT MAX(id) FROM `eh_rentalv2_order_records`),10000);
INSERT INTO `eh_rentalv2_order_records` (`id`,`order_no`,`biz_order_num`,`pay_order_id`,`payment_order_type`,`status`,`create_time`,`update_time`)
    SELECT (@id := @id + 1), order_id,order_num,payment_order_id,payment_order_type,0,create_time,create_time  FROM eh_payment_order_records where order_type = 'rentalOrder';

update `eh_rentalv2_order_records` t1 right join `eh_rentalv2_orders` t2 on t1.`order_no` = t2.`order_no` set t1.order_id = t2.id,t1.amount = t2.pay_total_money,t1.status = IF(t2.status in (2,7,9,10,14,20),1,0) ;

-- AUTHOR: 郑思挺
-- REMARK: 资源类型表空值覆盖
update `eh_rentalv2_resource_types` set pay_mode = 0 where pay_mode is null;

	
-- AUTHOR: 杨崇鑫
-- REMARK: 支付回调
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.callback.url.asset', '/asset/payNotify', '物业缴费新支付回调接口', '0');

	
-- AUTHOR: liangqishi 20180703
-- REMARK: 重用原来的企业账户菜单，但web的data_type由enterprise-account修改为business-account 
UPDATE `eh_web_menus` SET `data_type`='business-account' WHERE `id`=52000000;  -- 企业账户(type: park)
UPDATE `eh_web_menus` SET `data_type`='business-account' WHERE `id`=16070000;  -- 企业账户模块(type: zuolin)


-- AUTHOR: 梁燕龙 20180702
-- REMARK: 活动模块支付回调
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.callback.url.activity', '/activity/payNotify', '活动报名新支付回调接口', '0');

	
-- AUTHOR: 梁燕龙 20180702
-- REMARK: 活动支付订单迁移
update eh_activity_roster r,eh_payment_order_records t set r.pay_order_id = t.payment_order_id where t.order_type = 'activitySignupOrder' and r.order_no = t.order_id ;
update eh_activity_roster r,eh_payment_order_records t set r.refund_pay_order_id = t.payment_order_id where t.order_type = 'activitySignupOrder' and r.refund_order_no = t.order_id ;


-- AUTHOR: 郑思挺
-- REMARK: 回调接口
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.callback.url.rental', '/rental/payNotify', '资源预订新支付回调接口', '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('refund.v2.callback.url.rental', '/rental/refundNotify', '资源预订新退款回调接口', '0');

	
-- AUTHOR: 邓爽
-- REMARK: 32033	左邻	任务	停车支持发票系统接口 (未处理)
-- REMARK: 设置发票系统的appkey
set @id= IFNULL((select max(id) from eh_apps),0);
INSERT INTO `eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`, `update_uid`, `update_time`) 
	VALUES (@id:=@id+1, '1', '95908e84-fe3c-4bb3-a2a4-db6a078abfe3', 'fz8QGHnJ0796c1LIyyMQI2z1rAVY0DRcynEh23CdpPatapDmHkv0sqGWDBVLWHLBVmOu3StHw4JrD4TB8iX1EQ==', '发票系统', NULL, '1', NOW(), NULL, NULL);

 
-- AUTHOR: 杨崇鑫
-- REMARK: 解决导入的时候费项名称多了*的bug by cx.yang
update eh_payment_bill_items set charging_item_name=REPLACE(charging_item_name,"*","");
update eh_payment_bill_items set charging_item_name=REPLACE(charging_item_name,"(元)","");


-- AUTHOR: tangcen 20180703
-- REMARK: 合同日志的模板
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_templates`),0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '1', 'zh_CN', '合同事件', '创建合同', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '2', 'zh_CN', '合同事件', '删除合同', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '3', 'zh_CN', '合同事件', '修改${display}:由${oldData}更改为${newData}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '4', 'zh_CN', '合同资产事件', '新增合同资产:${apartmentName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '5', 'zh_CN', '合同资产事件', '删除合同资产:${apartmentName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '6', 'zh_CN', '合同计价条款事件', '新增计价条款:${chargingItemName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '7', 'zh_CN', '合同计价条款事件', '删除计价条款:${chargingItemName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '8', 'zh_CN', '合同计价条款事件', '修改计价条款:${chargingItemName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '9', 'zh_CN', '合同附件事件', '新增附件:${attachmentName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '10', 'zh_CN', '合同附件事件', '删除附件:${attachmentName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '11', 'zh_CN', '调租计划事件', '新增调租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '12', 'zh_CN', '调租计划事件', '删除调租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '13', 'zh_CN', '调租计划事件', '修改调租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '14', 'zh_CN', '免租计划事件', '新增免租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '15', 'zh_CN', '免租计划事件', '删除免租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '16', 'zh_CN', '免租计划事件', '修改免租计划:${chargingChangeName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '17', 'zh_CN', '合同续约事件', '产生续约合同，子合同名称为:${contractName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '18', 'zh_CN', '合同变更事件', '产生变更合同，子合同名称为:${contractName}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '19', 'zh_CN', '资产更改事件', '修改关联资产，由${oldApartmnets}变为${newApartmnets}', '0');


-- AUTHOR: 丁建民  20180703
-- REMARK: #30490 资产管理V2.7（产品功能）
SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'community', '10017', 'zh_CN', '省份不存在');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'community', '10018', 'zh_CN', '城市不存在');
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'community', '10019', 'zh_CN', '区县不存在');


-- AUTHOR: 严军  2018年7月3日16:01:33
-- REMARK: 服务广场V2.8  #26705  域空间配置V1.6 #18061
-- REMARK: 0-all, 1-logon, 2-auth
-- REMARK: 一些模块设置为“所有用户”或者“仅认证用户”
UPDATE `eh_service_modules` SET access_control_type = 1;
-- REMARK: 全部
UPDATE `eh_service_modules` SET access_control_type = 0 where id in(10800, 10800001, 40500, 92100, 10750, 10760, 10100, 10600);
-- REMARK: 认证
UPDATE `eh_service_modules` SET access_control_type = 2 where id in(50600, 50100, 13000, 41020, 41010, 41000, 20400);
UPDATE `eh_service_module_apps` SET access_control_type = 1;
-- REMARK: 全部
UPDATE `eh_service_module_apps` SET access_control_type = 0 where module_id in(10800, 10800001, 40500, 92100, 10750, 10760, 10100, 10600);
-- REMARK: 认证
UPDATE `eh_service_module_apps` SET access_control_type = 2 where module_id in(50600, 50100, 13000, 41020, 41010, 41000, 20400);
UPDATE  eh_launch_pad_items  SET access_control_type = 0;


-- AUTHOR: 黄明波  20180703
-- REMARK: 服务联盟v3.4 issue-29989
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11100', 'zh_CN', '需要更新的筛选为空');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11101', 'zh_CN', '筛选的类型不合法');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10010', 'zh_CN', '未选择项目或项目不合法');

update eh_locale_templates set text = '<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><html><head><style>img{height: 200px;width: 200px;}</style><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>${title}</title></head><body><p>预订人：${creatorName}</p><p>手机号：${creatorMobile}</p><p>公司名称：${creatorOrganization}</p><p>服务名称：${serviceOrgName}</p>${note}</body></html>' where scope = 'serviceAlliance.request.notification' and code = 4;
update eh_locale_strings set text = 'USER_NAME,USER_PHONE,USER_COMPANY' where scope = 'serviceAlliance.request.notification' and code = '10006';

update eh_service_modules set action_type = 14 where id = 40500;
update eh_service_module_apps set action_type = 14 where module_id = 40500;

update eh_flow_predefined_params set namespace_id = 0 where module_id = 40500 and entity_type = 'flow_button';

-- REMARK: 执行完数据迁移后，需要修改下类型。
update eh_service_alliance_attachments set attachment_type = 2 where creator_uid = 2 and attachment_type = 0;





-- AUTHOR: wentian integrated by jiarui  20180703
-- REMARK: 动态表单
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (37, 'enterprise_customer', '0', '/37', '物业报修', '', '0', NULL, '2', '1', NOW(), NULL, NULL);

SET @field_id = (SELECT MAX(id) FROM `eh_var_fields`);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
	VALUES (@field_id := @field_id + 1, 'enterprise_customer', 'taskCategoryName', '服务类型', 'String', '37', '/37/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
	VALUES (@field_id := @field_id + 1, 'enterprise_customer', 'pmTaskSource', '来源', 'Long', '37', '/37/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
	VALUES (@field_id := @field_id + 1, 'enterprise_customer', 'buildingName', '服务区域', 'String', '37', '/37/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
	VALUES (@field_id := @field_id + 1, 'enterprise_customer', 'requestorName', '联系人', 'String', '37', '/37/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
	VALUES (@field_id := @field_id + 1, 'enterprise_customer', 'requestorPhone', '联系电话', 'String', '37', '/37/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
	VALUES (@field_id := @field_id + 1, 'enterprise_customer', 'createTime', '发起时间', 'datetime', '37', '/37/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetime\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
	VALUES (@field_id := @field_id + 1, 'enterprise_customer', 'status', '状态', 'datetime', '37', '/37/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES
	(@field_id := @field_id + 1, 'enterprise_customer', 'content', '服务内容', 'datetime', '37', '/37/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');



-- REMARK: app模板
-- AUTHOR: wentian   integrated by jiarui  20180703
SET @id = ifnull((SELECT max(`id`) from `eh_locale_templates`),0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (@id:=@id+1, 'customer.notification', 1, 'zh_CN','msg_to_new_tracking', '管理员已将${customerName} 交由您来跟进，请尽快跟进', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (@id:=@id+1, 'customer.notification', 2, 'zh_CN','msg_to_old_tracking', '管理员已将${customerName}交由${currentTrackingName} 跟进，请悉知', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (@id:=@id+1, 'customer.notification', 3, 'zh_CN','msg_to_admin_no_candidate', '${originalTrackingName}已放弃跟进${customerName}，请尽快处理', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (@id:=@id+1, 'customer.notification', 4, 'zh_CN','msg_to_tracking_on_customer_delete', '${customerName}已被删除，请悉知', 0);


-- AUTHOR: 黄良铭  2018年7月3日
-- REMARK: issue-30602  消息推送V2.2 iOS推送流程升级，少证书版本
SET @a_id = (SELECT MAX(id) FROM eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
VALUES (@a_id:= @a_id +1, 'apple.pusher.flag', '0','苹果推送方式开关，值为1时为基于http2的新方式推送，其他值（一般选0）或空为旧方式推送',0,NULL);

SET @c_id = (SELECT IFNULL(MAX(id),1) FROM eh_developer_account_info);
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.ios','4H44DAN2YD','Q77V8W78L2','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgexxG8QrtsI2+Xuwf75baYuZBQYBDzSBfEPijhC2GOmagCgYIKoZIzj0DAQehRANCAAT4qnM82JvBxnJ/R3ESPFgylVgU4st8QlQdf1QEYQtU3lVNStrg8W6DcLvYyL/7I/Tc0HFXm+8YQijz7ayWDOp7','2018-06-12 16:11:41','');

-- AUTHOR: 梁燕龙 20180704
-- REMARK: 活动模块分享链接修改
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('post.link.share.url', '/mobile/static/share_post/share_post.html#', 'the relative path for sharing topic link', '0');
UPDATE `eh_configurations` SET value = '/forum/build/index.html#/detail' WHERE NAME = 'post.share.url';
-- --------------------- SECTION END ---------------------------------------------------------






-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本

-- AUTHOR: 杨崇鑫
-- REMARK: 新支付的配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.appKey', '69ee0cb3-5afb-4d83-ae12-ef493de48de2', '新支付appKey', '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.secretKey', 'T6PEjA9GBAVMBmlBYDs9RkoQMurrH5XQjFoP1v+oGomKeIdsqVhwpTVv8AHPLWo/I09IudgxR4/zjvM9YYwxzg==', '新支付secretKey', '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.payHomeUrl', 'https://payv2.zuolin.com/pay', '新支付payHomeUrl', '0');

-- AUTHOR: 杨崇鑫
-- REMARK: 支付回调
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.callback.url.pmsy', '/pmsy/payNotify', '物业缴费新支付回调接口', '999993');

-- AUTHOR: 杨崇鑫
-- REMARK: 由于海岸馨服务是定制的，web没有账单组管理，所以需要初始化收款方账户配置
SET @id = ifnull((SELECT MAX(id) FROM `eh_payment_bill_groups`),0);
INSERT INTO `eh_payment_bill_groups` (`id`, `namespace_id`, `owner_id`, `owner_type`, `name`, `balance_date_type`, `bills_day`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`, `due_day`, `due_day_type`, `brother_group_id`, `bills_day_type`, `biz_payee_type`, `biz_payee_id`, `category_id`) 
VALUES (@id := @id + 1, '999993', '999993', 'community', '物业缴费', '2', '5', '0', UTC_TIMESTAMP(), NULL, NULL, '1', NULL, NULL, NULL, '4',
	'EhOrganizations', '4443', '1028');

-- AUTHOR: 杨崇鑫
-- REMARK: 新支付数据迁移
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='2327' where namespace_id=1; -- Volgo
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4592' where namespace_id=999944; -- 天企汇
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4333' where namespace_id=999946; -- 聚变
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4241' where namespace_id=999947; -- 智慧空港
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4334' where namespace_id=999948; -- 国贸服务
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4303' where namespace_id=999950; -- 智汇银星
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4208' where namespace_id=999951; -- 鼎峰汇
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4335' where namespace_id=999952; -- 创集合
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4242' where namespace_id=999955; -- ELive
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4197' where namespace_id=999957; -- 杭州越空间
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4249' where namespace_id=999958; -- 创意园
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='3799' where namespace_id=999959; -- 启迪香山
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='3777' where namespace_id=999961; -- 智富汇
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4590' where namespace_id=999963; -- 路福联合广场
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4526' where namespace_id=999967; -- 大沙河建投
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1000' where namespace_id=999971; -- 张江高科
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1004' where namespace_id=999973; -- E-BOILL
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='3800' where namespace_id=999981; -- 上海万科星商汇
-- update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1006' where namespace_id=999983; 
-- update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1001' where namespace_id=999985;
-- update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1003' where namespace_id=999990;
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4443' where namespace_id=999993; -- 海岸馨服务
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4691' where namespace_id=999954; -- 新微创源

-- AUTHOR: 梁燕龙 20180702
-- REMARK: 活动收款方账号迁移,在执行语句前，请与 陈毅峰 对照一下域空间是否有遗漏；
SET @id = ifnull((SELECT MAX(id) FROM `eh_activity_biz_payee`),10000);
set @namespace_id = 1;
set @account_id = 2327;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999944;
set @account_id = 4592;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999946;
set @account_id = 4333;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999947;
set @account_id = 4241;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999948;
set @account_id = 4334;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999950;
set @account_id = 4303;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999951;
set @account_id = 4208;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999952;
set @account_id = 4335;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999955;
set @account_id = 4242;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999957;
set @account_id = 4197;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999958;
set @account_id = 4249;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999959;
set @account_id = 3799;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999961;
set @account_id = 3777;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999963;
set @account_id = 4590;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999967;
set @account_id = 4526;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;


set @namespace_id = 999973;
set @account_id = 1004;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999981;
set @account_id = 3800;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999993;
set @account_id = 4443;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

set @namespace_id = 999954;
set @account_id = 4691;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;



-- AUTHOR: 郑思挺
-- REMARK: 收款账户迁移 eh_rentalv2_pay_accounts
SET @id = ifnull((SELECT MAX(id) FROM `eh_rentalv2_pay_accounts`),10000);
set @namespace_id = 999973;
set @account_id = 1004;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 1;
set @account_id = 2327;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999961;
set @account_id = 3777;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999959;
set @account_id = 3799;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999981;
set @account_id = 3800;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999957;
set @account_id = 4197;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999951;
set @account_id = 4208;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999947;
set @account_id = 4241;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999955;
set @account_id = 4242;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999958;
set @account_id = 4249;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999950;
set @account_id = 4303;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999946;
set @account_id = 4333;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999948;
set @account_id = 4334;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999952;
set @account_id = 4335;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999967;
set @account_id = 4526;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999963;
set @account_id = 4590;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999944;
set @account_id = 4592;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999993;
set @account_id = 4443;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

set @namespace_id = 999954;
set @account_id = 4691;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;

-- AUTHOR: 邓爽  20180703
-- REMARK: 基线停车支付收款方迁移
-- REMARK: Volgo
SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 1;
set @communityid = '240111044331058733';
set @parkinglotId = 10030;
SET @parkingLotName = '左邻停车场';
set @payeeId = 2327;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

-- REMARK: 天企汇
SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999944;
set @communityid = '240111044332061061';
set @parkinglotId = 10034;
SET @parkingLotName = '联科停车场';
set @payeeId = 4592;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999944;
set @communityid = '240111044332061063';
set @parkinglotId = 10035;
SET @parkingLotName = '创投大厦停车场';
set @payeeId = 4592;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999944;
set @communityid = '240111044332061063';
set @parkinglotId = 10036;
SET @parkingLotName = '孵化中心停车场';
set @payeeId = 4592;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999944;
set @communityid = '240111044332061063';
set @parkinglotId = 10037;
SET @parkingLotName = 'IT研发中心停车场';
set @payeeId = 4592;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

-- REMARK: 智汇银星
SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999950;
set @communityid = '240111044332060200';
set @parkinglotId = 10028;
SET @parkingLotName = '银星工业区停车场';
set @payeeId = 4303;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());


-- REMARK: 创意园
SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999958;
set @communityid = '240111044332060016';
set @parkinglotId = 10024;
SET @parkingLotName = '中百畅停车场';
set @payeeId = 4249;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

-- REMARK: 路福联合广场
SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999963;
set @communityid = '240111044332060139';
set @parkinglotId = 10032;
SET @parkingLotName = '路福联合大厦停车场';
set @payeeId = 4590;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

-- REMARK: 上海万科星商汇
SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999981;
set @communityid = '240111044331056041';
set @parkinglotId = 10021;
SET @parkingLotName = '御河企业公馆停车场';
set @payeeId = 3800;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());


-- AUTHOR: 邓爽  20180703
-- REMARK: 基线执行云打印收款方
-- REMARK: volgo
SET @id = IFNULL((SELECT MAX(id) from eh_siyin_print_business_payee_accounts),9999);
INSERT INTO `eh_siyin_print_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, '1', 'community', '240111044331058733', '2327', 'EhOrganizations', '2', '1', now(), '1', now());


-- AUTHOR: 黄良铭  2018年7月3日
-- REMARK: issue-30602  消息推送V2.2 iOS推送流程升级，少证书版本
SET @c_id = (SELECT IFNULL(MAX(id),1) FROM eh_developer_account_info);

INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.techpark.ios.zuolin','4H44DAN2YD','Q77V8W78L2','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgexxG8QrtsI2+Xuwf75baYuZBQYBDzSBfEPijhC2GOmagCgYIKoZIzj0DAQehRANCAAT4qnM82JvBxnJ/R3ESPFgylVgU4st8QlQdf1QEYQtU3lVNStrg8W6DcLvYyL/7I/Tc0HFXm+8YQijz7ayWDOp7','2018-06-12 16:08:58','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.dashahejiantou.ios.custom','AHS6J3P93Q','VSF7L946QT','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgxndN+H066MaLTMcVfReEH3h+zRJvzRGa1htf+ZS+GcCgCgYIKoZIzj0DAQehRANCAATMWieJA9Zg4MM/SIgQMqFavpdkl5TPHNQ3eE7Ac7gLj6qkvBY6PKUAZrRDFOWLd1Q5GlEMb++1eZMHbPpq6Tao','2018-06-13 16:56:30','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.cshidai.ios.custom','7U3CS4KE7L','K95KQU5W33','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgZFBo9Zs1r0vCW1/GlXIWVInRD9YAV/3vOZPpgHRP4RCgCgYIKoZIzj0DAQehRANCAASHx9QZLr+ui9S3kHJn4mlJ+3Xs13lAE4pLYYa8ab0xYcO92iYcvII21wHVErUdljxmTrJmyLbRqEPxMVxsyEwM','2018-06-13 16:58:07','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.qidixiangshan.ios','CB8GBE78S6','579796H6SZ','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgZkPKJv9d0476NRyK+/Lilnh9zlQIoQhTZpw6ib1i/rWgCgYIKoZIzj0DAQehRANCAATe4FEwlSalfQUXkuyGvewPaJexURCt3NvqILtR3yHpahPmcf3zNleyqxZtrx3p/oZslSGKG1TcAaT65RIIdHlC','2018-06-13 16:59:00','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.changzhihuiV1.ios','W2VC9SAD48','4YMXXWUW56','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgmjr+rWQF+GAZu7yeT7hd61h2xO2wbKWLa1hiLlOjF6CgCgYIKoZIzj0DAQehRANCAASHpSxhCQRuYv64cIY9qK7rMOJj6A69tzqWOlluzzTbYTgPHg3MnEwVEZ8JTY0zDKPh9tV0RM4IcALyt6izCuqP','2018-06-13 17:00:28','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.xinchengzhihui.ios','EWGTG9634M','89R6W792SN','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQge3a6g0w1CyQbN550h3/U6eIOIizxm7I1CGcGY5Du/xqgCgYIKoZIzj0DAQehRANCAAQKGbiI5VFcgkMEzlzRvpH35XSjqH3dQfRRA0+d+XpO+qImBohENCFuAEPJ377OT6+riKJXIZOCjKsCAL0Ux5b0','2018-06-13 17:01:42','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.xingshanghui.ios','Z94T2ZAC3Y','BM353BPZ8D','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgjzhpaksTeIFPcT9LKrMPB6KlprXqddL/TxnTwBh2YrSgCgYIKoZIzj0DAQehRANCAART9X8VuS9GVcdLFV4cIjHYHrqb1c/JvlK3D/1Po1XiWCCm6svVACIWC3EB6r3o1MTu8tNMmgCXLOKLN0flD04h','2018-06-13 17:02:32','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.baojiezhigu.ios.release','9DDTF2D6F9','V9KULR6LUW','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgBNdZytMskEl/El3NRTdJ0UWb0kbt09LuZ3rL7KLfWxOgCgYIKoZIzj0DAQehRANCAATkS3McOx8J+goHQ0hihPcskWgYi9mWSyDQ51AhYT3k+Oplc5LxMcRo+dilYLKzM6zCbMEfQE4aaMa6/FWjTU7Z','2018-06-13 17:03:46','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.xinweilai.ios','RZW43KR9Q4','PU68B9P9QS','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQg7BgXf3zlgxFj6hmPVB6wLrAe5glhNKBDTcdRzywJaySgCgYIKoZIzj0DAQehRANCAAQQzPNYG9+mAhxA7XAgyhkuK87Xp1bhconXboQ2KLG4Wo/oqNj1wstgLhRdDLmfiArWeZn2hBeb0XaqIqyxJO0i','2018-06-13 17:08:01','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.kexing.ios','424FEFTT88','Y3X3PV3ZN9','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgZw/YZBFOt4/ngxEYTqUWDb5m4+NywDbpVoCOqJJ4+4CgCgYIKoZIzj0DAQehRANCAAQSUXcU1Hi+sHbgyOh3G4MGM2yftY77sNYe0n0GoM3gVs9b8StMnpsSWxQNDfsrw8B5EvvDcDHleY1fBzfeAhIq','2018-06-13 17:08:46','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.coastalpark.ios','GQ85Y2XDQ3','BDDR55F8B5','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQg6N1jQ2A1u2d9Jyn0TDpDdIf47604bt2z03koge9fXCSgCgYIKoZIzj0DAQehRANCAAS6A6ACWWvTnt0dj38UaPq8MiNfL98kxX2vC4TOkYvPhnwNLtG6pR5yFV3NqDZnsZi8DnsPQ4Oasps33S+o3I5D','2018-06-13 17:10:50','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.oa.ios','T2777ZE59G','D8X4WHNV9A','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQg8fthR/B8nnCWVRCwoNa2rHp1U4sM1QqGMQssVAbWjpugCgYIKoZIzj0DAQehRANCAARvoymYJ2M5uC3B5xWt82hA/NoOUAjJZeophmpt+5A3aAjb5O5vM806xceKxNMwo7K4Mkk1mkS+rfXvkDhhL/b1','2018-06-13 17:12:40','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.upark.ios.custom','T2777ZE59G','D8X4WHNV9A','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQg8fthR/B8nnCWVRCwoNa2rHp1U4sM1QqGMQssVAbWjpugCgYIKoZIzj0DAQehRANCAARvoymYJ2M5uC3B5xWt82hA/NoOUAjJZeophmpt+5A3aAjb5O5vM806xceKxNMwo7K4Mkk1mkS+rfXvkDhhL/b1','2018-06-13 17:12:40','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.junminronghe.ios.custom','7R8J548669','N4Q33TX6FT','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgftOeRKfafY1+SW5GYmjZBYb1OYoqD42P20fss0+3Pd6gCgYIKoZIzj0DAQehRANCAATqbAYxO4w/03QzBnovtCOmiraoaRkbSdirJE/f61oybR7dIM4ZNgGutDrxseVrdO2FI1i27in8IVGcUN/DgG0S','2018-06-13 17:13:19','');
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
VALUES(@c_id:= @c_id +1,'com.Dms365.GMQ','6TW7VRG635','5TUW2BDUUK','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgFtIkB8orcVPNvr5CR0g5GgsX+ubdoYU45DNzz6hoLPKgCgYIKoZIzj0DAQehRANCAASxSX6jYTPpbosIF/j35t26irE2LcDrha72WaaECdVVyCvi06bfhaV40l+vDtvcHg+e9B2IepAXLBpofIju/Y+2','2018-06-13 17:13:58','');

SET @b_id = (SELECT IFNULL(MAX(id),1) FROM eh_bundleid_mapper);
INSERT INTO `eh_bundleid_mapper` (`id`, `namespace_id`, `identify`, `bundle_id`) VALUES(@b_id:= @b_id +1,'1000000','develop ','com.techpark.ios.zuolin');
INSERT INTO `eh_bundleid_mapper` (`id`, `namespace_id`, `identify`, `bundle_id`) VALUES(@b_id:= @b_id +1,'1000000','appbeta','com.techpark.ios.zuolin');
INSERT INTO `eh_bundleid_mapper` (`id`, `namespace_id`, `identify`, `bundle_id`) VALUES(@b_id:= @b_id +1,'1000000','appstore','com.techpark.ios.zuolin');



-- --------------------- SECTION END ---------------------------------------------------------





-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION只在开发库中执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------




-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zijing
-- DESCRIPTION: 此SECTION只在清华信息港(紫荆)-999984执行的脚本
-- AUTHOR: 黄良铭  2018年7月3日
-- REMARK: issue-30602  消息推送V2.2 iOS推送流程升级，少证书版本
SET @c_id = (SELECT IFNULL(MAX(id),1) FROM eh_developer_account_info);
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
	VALUES(@c_id:= @c_id +1,'com.qinghua.ios','RZW43KR9Q4','PU68B9P9QS','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQg7BgXf3zlgxFj6hmPVB6wLrAe5glhNKBDTcdRzywJaySgCgYIKoZIzj0DAQehRANCAAQQzPNYG9+mAhxA7XAgyhkuK87Xp1bhconXboQ2KLG4Wo/oqNj1wstgLhRdDLmfiArWeZn2hBeb0XaqIqyxJO0i','2018-06-13 17:08:01','');

-- --------------------- SECTION END ---------------------------------------------------------




-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本
-- AUTHOR: 杨崇鑫 
-- REMARK: 新支付数据迁移
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4422' where namespace_id=999979; -- 光大we谷

-- AUTHOR: 梁燕龙 20180702
-- REMARK: 活动收款方账号迁移；
set @namespace_id = 999979;
set @account_id = 4422;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;


-- AUTHOR: 邓爽  20180703
-- REMARK: 基线停车支付收款方迁移
SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999979;
set @communityid = '240111044331056800';
set @parkinglotId = 10026;
SET @parkingLotName = '光大we谷停车场';
set @payeeId = 4422;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

-- AUTHOR: 郑思挺
-- REMARK: 收款账户迁移 eh_rentalv2_pay_accounts
SET @id = ifnull((SELECT MAX(id) FROM `eh_rentalv2_pay_accounts`),10000);
set @namespace_id = 999979;
set @account_id = 4422;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;
-- --------------------- SECTION END ---------------------------------------------------------





-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本

-- AUTHOR: 杨崇鑫
-- REMARK: 新支付的配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.appKey', '2ecedecc-3592-4edd-bfff-205b71fde95c', '新支付appKey', '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.secretKey', 'T1nIY3vNMK83dkJNcD/DIk9xx2rZup9KDjGwITiUnVFMVGVVwnmk5XHBHSe7BOm5Ex4TKflryC5IEURNQWHDIg==', '新支付secretKey', '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`)
	VALUES ('pay.v2.payHomeUrl', 'https://payv2.zuolin.com/pay', '新支付payHomeUrl', '0');

	
-- AUTHOR: 杨崇鑫 
-- REMARK: 新支付数据迁移
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='1005' where namespace_id=999966; -- 深圳湾


-- AUTHOR: 梁燕龙 20180702
-- REMARK: 活动收款方账号迁移；
set @namespace_id = 999966;
set @account_id = 1005;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;


-- AUTHOR: dengs  20180703
-- REMARK: 基线停车支付收款方迁移
SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999966;
set @communityid = '240111044331050370';
set @parkinglotId = 10011;
SET @parkingLotName = '软件产业基地停车场';
set @payeeId = 1129;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999966;
set @communityid = '240111044331050371';
set @parkinglotId = 10012;
SET @parkingLotName = '创投大厦停车场';
set @payeeId = 1129;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

SET @id = IFNULL((SELECT MAX(id) from eh_parking_business_payee_accounts),9999);
set @ns = 999966;
set @communityid = '240111044331050369';
set @parkinglotId = 10013;
SET @parkingLotName = '生态园停车场';
set @payeeId = 1129;
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'tempfee', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'vipParking', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());
INSERT INTO `eh_parking_business_payee_accounts` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) 
	VALUES (@id:=@id+1, @ns, 'community', @communityid, @parkinglotId, @parkingLotName, 'monthRecharge', @payeeId, 'EhOrganizations', '2', '1', now(), '1', now());

-- AUTHOR: 黄良铭  2018年7月3日
-- REMARK: issue-30602  消息推送V2.2 iOS推送流程升级，少证书版本
SET @c_id = (SELECT IFNULL(MAX(id),1) FROM eh_developer_account_info);
INSERT INTO `eh_developer_account_info` (`id`, `bundle_ids`, `team_id`, `authkey_id`, `authkey`, `create_time`, `create_name`) 
	VALUES(@c_id:= @c_id +1,'com.mybay.ios','2PRZ3336PJ','UT7B95V928','MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgOj5WmRF9x4eO3CASZUYBKv56BTkf1ZyhJJPhGcSJxC2gCgYIKoZIzj0DAQehRANCAATX2Qw+DzB3IocGXaVDfxX17WJ8D9PT8jaj7rRwKeHDXS1IXDidOVxAnhxedwNcP9UKqLu0zpqUIGvCvyC83hU0','2018-06-13 17:11:34','');

-- AUTHOR: 郑思挺
-- REMARK: 收款账户迁移 eh_rentalv2_pay_accounts
SET @id = ifnull((SELECT MAX(id) FROM `eh_rentalv2_pay_accounts`),10000);
set @namespace_id = 999966;
set @account_id = 1129;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;
-- --------------------- SECTION END ---------------------------------------------------------




-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------






-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费新支付数据迁移
update eh_payment_bill_groups set biz_payee_type="EhOrganizations",biz_payee_id='4692' where namespace_id=999949; -- 安邦物业

-- AUTHOR: 梁燕龙 20180702
-- REMARK: 活动收款方账号迁移；
set @namespace_id = 999949;
set @account_id = 4692;
INSERT INTO `eh_activity_biz_payee` (`id`,`namespace_id`,`owner_id`,`biz_payee_id`,`biz_payee_type`)
SELECT (@id := @id + 1), @namespace_id,t.id,@account_id,'EhOrganizations' FROM eh_activity_categories t where t.namespace_id = @namespace_id;

-- AUTHOR: 郑思挺
-- REMARK: 收款账户迁移 eh_rentalv2_pay_accounts
SET @id = ifnull((SELECT MAX(id) FROM `eh_rentalv2_pay_accounts`),10000);
set @namespace_id = 999949;
set @account_id = 4692;
INSERT INTO `eh_rentalv2_pay_accounts` (`id`,`namespace_id`,`community_id`,`resource_type`,`source_type`,`source_id`,`account_id`,`create_time`)
SELECT (@id := @id + 1), @namespace_id,c.id,'default','default_rule',b.id,@account_id,now() FROM eh_rentalv2_resource_types b LEFT JOIN eh_communities c on b.namespace_id = c.namespace_id where b.namespace_id = @namespace_id;
-- --------------------- SECTION END ---------------------------------------------------------


