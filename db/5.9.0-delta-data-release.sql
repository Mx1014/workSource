-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR: liangqishi  20180702
-- REMARK: 某某模块涉及到数据迁移，升级后需要调用/xxxx/xxxx接口更新ES
-- REMARK: content图片程序升级，从本版中的content二进制更新到正式环境中

-- AUTHOR: 杨崇鑫 2018年9月5日
-- REMARK: 1、备份表eh_asset_module_app_mappings
--         2、调用接口/asset/tranferAssetMappings


-- --------------------- SECTION END ---------------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫 2018年9月5日
-- REMARK: 物业缴费V6.6（对接统一账单） ：业务应用与缴费的关联关系表历史数据迁移
update eh_asset_module_app_mappings set config=concat('{"contractOriginId":', contract_originId , ',' , '"contractChangeFlag":' , '"', contract_changeFlag , '"' , '}') 
	where contract_originId is not null;
update eh_asset_module_app_mappings set source_type='contract' where source_type is null;
	
-- AUTHOR: 张智伟 20180822
-- REMARK: issue-36367 考勤规则新增打卡提醒设置初始化，默认开启
UPDATE eh_punch_rules SET punch_remind_flag=1,remind_minutes_on_duty=10 WHERE rule_type=1;

-- AUTHOR: 张智伟 20180822
-- REMARK: issue-36367 假期余额变更消息提醒文案配置
SET @max_template_id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_templates`),1);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10006, 'zh_CN', '消息标题','假期余额变动', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10007, 'zh_CN', '假期余额变动提醒：管理员调整假期余额','管理员调整了你的假期余额：<#if annualLeaveAdd != 0><#if annualLeaveAdd gt 0>发放<#else>扣除</#if>年假${annualLeaveAddShow}</#if><#if annualLeaveAdd != 0 && overtimeAdd != 0>，</#if><#if overtimeAdd != 0><#if overtimeAdd gt 0>发放<#else>扣除</#if>调休${overtimeAddShow}</#if>。当前余额为：年假${annualLeaveBalance} ，调休${overtimeCompensationBalance} 。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10008, 'zh_CN', '假期余额变动提醒：年假/调休的审批被撤销/终止/删除','${categoryName}申请（${requestTime}） 已失效，假期已返还。当前余额为：年假 ${annualLeaveBalance} ，调休${overtimeCompensationBalance} 。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10009, 'zh_CN', '假期余额变动提醒：请假审批提交','${categoryName}申请（${requestTime}） 已提交，假期已扣除。当前余额为：年假 ${annualLeaveBalance} ，调休${overtimeCompensationBalance} 。', 0);

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUE (@max_template_id:=@max_template_id+1, 'punch.remind', 1, 'zh_CN', '消息标题','<#if punchType eq 0>上班打卡<#else>下班打卡</#if>', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUE (@max_template_id:=@max_template_id+1, 'punch.remind', 2, 'zh_CN', '上班打卡提醒','最晚${onDutyTime}上班打卡，立即打卡', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUE (@max_template_id:=@max_template_id+1, 'punch.remind', 3, 'zh_CN', '下班打卡提醒','上班辛苦了，记得打下班卡', 0);

-- AUTHOR: 张智伟 20180822
-- REMARK: issue-36367 打卡记录报表排序
UPDATE eh_punch_logs l INNER JOIN eh_organization_member_details d ON d.organization_id=l.enterprise_id AND d.target_id=l.user_id
SET l.detail_id=d.id
WHERE d.target_id>0 AND l.detail_id IS NULL;

UPDATE eh_punch_log_files l INNER JOIN eh_organization_member_details d ON d.organization_id=l.enterprise_id AND d.target_id=l.user_id
SET l.detail_id=d.id
WHERE d.target_id>0 AND l.detail_id IS NULL;

-- --------------------- SECTION END ---------------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR: liangqishi  20180702
-- REMARK: （此行放说明，可以多行，多行时每行前面都有REMARK）某模块增加初始化数据。。。。。。。
-- REMARK:。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。
INSERT INTO xxxx() VALUES();
-- --------------------- SECTION END ---------------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION只在开发库中执行的脚本
-- AUTHOR: liangqishi  20180702
-- REMARK: （此行放说明，可以多行，多行时每行前面都有REMARK）某模块增加初始化数据。。。。。。。
-- REMARK:。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。
INSERT INTO xxxx() VALUES();
-- --------------------- SECTION END ---------------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zijing
-- DESCRIPTION: 此SECTION只在清华信息港(紫荆)-999984执行的脚本
-- AUTHOR: liangqishi  20180702
-- REMARK: 某模块增加初始化数据
INSERT INTO xxxx() VALUES();
-- --------------------- SECTION END ---------------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本
-- AUTHOR: liangqishi  20180702
-- REMARK: 某模块增加初始化数据
INSERT INTO xxxx() VALUES();
-- --------------------- SECTION END ---------------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本
-- AUTHOR: liangqishi  20180702
-- REMARK: 某模块增加初始化数据
INSERT INTO xxxx() VALUES();
-- --------------------- SECTION END ---------------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本
-- AUTHOR: liangqishi  20180702
-- REMARK: 某模块增加初始化数据
INSERT INTO xxxx() VALUES();
-- --------------------- SECTION END ---------------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- -- ENV: anbang
-- -- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- -- AUTHOR: liangqishi  20180702
-- -- REMARK: 某模块增加初始化数据
-- INSERT INTO xxxx() VALUES();
-- -- --------------------- SECTION END ---------------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- -- ENV: guangzhouyuekongjian
-- -- DESCRIPTION: 此SECTION只在广州越空间-999930执行的脚本
-- -- AUTHOR: 缪洲  20180912
-- -- REMARK: 插入广州越空间url,app-key,停车场数据,与支付账号
INSERT INTO `ehcore`.`eh_configurations`( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ( 'parking.yuekongjian.url', 'http://220.160.111.118:8099', '广州越空间Url', 0, NULL, NULL);
INSERT INTO `ehcore`.`eh_configurations`(`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ( 'parking.yuekongjian.key', 'b20887292a374637b4a9d6e9f940b1e6', NULL, 0, NULL, NULL);

INSERT INTO `ehcore`.`eh_parking_lots`(`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`, `order_tag`, `order_code`, `id_hash`, `func_list`) VALUES (10040,'community', 240111044332063564, '财富广场地下停车', 'YUE_KONG_JIAN', '', 2, 1, CURRENT_TIMESTAMP, 999930, NULL, '{\"tempfeeFlag\":1,\"rateFlag\":1,\"lockCarFlag\":0,\"searchCarFlag\":0,\"currentInfoType\":2,\"invoiceFlag\":0,\"businessLicenseFlag\":0,\"vipParkingFlag\":0,\"monthRechargeFlag\":1,\"identityCardFlag\":0,\"monthCardFlag\":0}', '931', 1, NULL, '[\"vipParking\",\"tempfee\",\"monthRecharge\",\"freePlace\"]');

INSERT INTO `ehcore`.`eh_parking_business_payee_accounts`(`namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `parking_lot_name`, `business_type`, `payee_id`, `payee_user_type`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES (999930, 'community', 240111044332063564, 10040, '财富广场地下停车场', 'tempfee', 4303, 'EhOrganizations', 2, 486680, CURRENT_TIMESTAMP, 486680, CURRENT_TIMESTAMP);
-- -- --------------------- SECTION END ---------------------------------------------------------