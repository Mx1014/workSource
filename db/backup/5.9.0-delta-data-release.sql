-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR: liangqishi  20180702
-- REMARK: 某某模块涉及到数据迁移，升级后需要调用/xxxx/xxxx接口更新ES
-- REMARK: content图片程序升级，从本版中的content二进制更新到正式环境中

-- AUTHOR: 杨崇鑫 2018年9月5日
-- REMARK: 1、备份表eh_asset_module_app_mappings
--         2、调用接口/asset/tranferAssetMappings

-- AUTHOR: 黄鹏宇 2018年9月5日
-- REMARK: 1、调用接口/customer/syncEnterpriseCustomerIndex





-- ---------------------------------------------------    标准版在5.8.4.21080925已经执行过，这里不再执行  start ----------------



-- AUTHOR: ryan  20180827
-- REMARK: 执行 /workReport/syncWorkReportReceiver 接口, 用以同步工作汇报接收人公司信息(以下接口需等待上一接口执行完毕,基线大约需要10分钟)

-- AUTHOR: ryan  20180827
-- REMARK: 执行 /workReport/updateWorkReportReceiverAvatar 接口, 用以更新工作汇报接收人头像(需等待上一接口执行完毕,基线大约需要10分钟)

-- AUTHOR: ryan  20180926
-- REMARK: 执行 /workReport/updateWorkReportValAvatar 接口, 用以更新历史工作汇报值的头像(需等待上一接口执行完毕,基线大约需要10分钟)



-- ---------------------------------------------------    标准版在5.8.4.21080925已经执行过，这里不再执行  end ----------------





-- AUTHOR: 黄良铭 20180927
-- REMARK: core server 和 point server  的kafka配置 加上:client-id: ehcore   (client-id的名字建议根据各服务名来起比如积分的可叫point)# 如果是自己的环境不需要 Kafka, 则把这个值配置为 disable, 示例： client-id: disable(5.9.0 之后的代码将认为没有配置即不启用kafka)
--                可参照:http://serverdoc.lab.everhomes.com/docs/faq/baseline-21535076011                   

-- --------------------- SECTION END ---------------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫 2018年9月5日
-- REMARK: 物业缴费V6.6（对接统一账单） ：业务应用与缴费的关联关系表历史数据迁移
update eh_asset_module_app_mappings set config=concat('{"contractOriginId":', contract_originId , ',' , '"contractChangeFlag":' , '"', contract_changeFlag , '"' , '}') 
	where contract_originId is not null;
update eh_asset_module_app_mappings set source_type='contract' where source_type is null;

-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.0 账单区分数据来源
-- REMARK: 如果合同ID或合同编号为空，那么账单来源于缴费新增（默认是缴费新增，因为没有办法区分是新增还是导入）
update eh_payment_bills set source_type='asset' where contract_id is null or contract_num is null;
update eh_payment_bills set source_id='1' where contract_id is null or contract_num is null;
update eh_payment_bills set source_name='手动新增' where contract_id is null or contract_num is null;

update eh_payment_bill_items set source_type='asset' where contract_id is null or contract_num is null;
update eh_payment_bill_items set source_id='1' where contract_id is null or contract_num is null;
update eh_payment_bill_items set source_name='手动新增' where contract_id is null or contract_num is null;
-- REMARK: 如果合同ID和合同编号都不为空，并且如果根据合同ID能够在eh_energy_meter_tasks表找到数据，说明是来源于能耗
update eh_payment_bills set source_type='energy' where contract_id in (select id from eh_energy_meter_tasks) and (contract_id is not null and contract_num is not null);
update eh_payment_bills set source_name='能耗产生' where contract_id in (select id from eh_energy_meter_tasks) and (contract_id is not null and contract_num is not null);

update eh_payment_bill_items set source_type='energy' where contract_id in (select id from eh_energy_meter_tasks) and (contract_id is not null and contract_num is not null);
update eh_payment_bill_items set source_name='能耗产生' where contract_id in (select id from eh_energy_meter_tasks) and (contract_id is not null and contract_num is not null);
-- REMARK: 如果合同ID和合同编号都不为空，并且如果根据合同ID能够在eh_contracts表找到数据，说明是来源于合同
update eh_payment_bills set source_type='contract' where contract_id in (select id from eh_contracts) and (contract_id is not null and contract_num is not null);
update eh_payment_bills set source_name='合同产生' where contract_id in (select id from eh_contracts) and (contract_id is not null and contract_num is not null);

update eh_payment_bill_items set source_type='contract' where contract_id in (select id from eh_contracts) and (contract_id is not null and contract_num is not null);
update eh_payment_bill_items set source_name='合同产生' where contract_id in (select id from eh_contracts) and (contract_id is not null and contract_num is not null);

-- REMARK:物业缴费V6.0 账单区分数据来源
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'asset.source', '10001', 'zh_CN', '手动新增');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'asset.source', '10002', 'zh_CN', '批量导入');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'asset.source', '10003', 'zh_CN', '合同产生');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'asset.source', '10004', 'zh_CN', '能耗产生');
	
-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段，需做历史数据初始化
-- REMARK: 如果来源于缴费新增/导入，支持删除、支持修改
update eh_payment_bills set can_delete = 1 where source_type='asset';
update eh_payment_bills set can_modify = 1 where source_type='asset';
update eh_payment_bill_items set can_delete = 1 where source_type='asset';
update eh_payment_bill_items set can_modify = 1 where source_type='asset';
-- REMARK: 如果来源于能耗，不支持删除、不支持修改
update eh_payment_bills set can_delete = 0 where source_type='energy';
update eh_payment_bills set can_modify = 0 where source_type='energy';
update eh_payment_bill_items set can_delete = 0 where source_type='energy';
update eh_payment_bill_items set can_modify = 0 where source_type='energy';
-- REMARK: 如果来源于合同，支持删除账单/修改账单的减免增收等配置，不支持删除/修改费项
update eh_payment_bills set can_delete = 1 where source_type='contract';
update eh_payment_bills set can_modify = 1 where source_type='contract';
update eh_payment_bill_items set can_delete = 0 where source_type='contract';
update eh_payment_bill_items set can_modify = 0 where source_type='contract';
-- REMARK: 如果是已出已缴账单，则不允许删除/修改
update eh_payment_bills set can_delete = 0 where switch=1 and status=1;
update eh_payment_bills set can_modify = 0 where switch=1 and status=1;
update eh_payment_bill_items set can_delete = 0 where bill_id in (select id from eh_payment_bills where switch=1 and status=1 );
update eh_payment_bill_items set can_modify = 0 where bill_id in (select id from eh_payment_bills where switch=1 and status=1 );
	
-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.0 权限控制
-- REMARK: 物业缴费V6.0 将“ 账单查看、筛选”的权限去掉，因为有此模块权限的用户默认就会有查看和筛选的权限；
delete from eh_acl_privileges where id=204001001;
delete from eh_service_module_privileges where privilege_id=204001001;
-- REMARK: 物业缴费V6.0 将“新增账单”改为“新增账单、批量导入”权限；
update eh_acl_privileges set name='新增账单、批量导入',description='账单管理 新增账单、批量导入' where id=204001002;
update eh_service_module_privileges set remark='新增账单、批量导入' where privilege_id=204001002;
-- REMARK: 物业缴费V6.0 新增以下权限：“账单修改、删除”
SET @p_id = 204001007;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '账单修改、删除', '账单管理 账单修改、删除', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '204011', '0', @p_id, '账单修改、删除', '0', NOW());
-- REMARK: 物业缴费V6.0 新增以下权限：“未出批量转已出”
SET @p_id = 204001008;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '未出批量转已出', '账单管理 未出批量转已出', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '204011', '0', @p_id, '未出批量转已出', '0', NOW());
-- REMARK: 物业缴费V6.0 新增以下权限：“未缴批量转已缴”
SET @p_id = 204001009;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '未缴批量转已缴', '账单管理 未缴批量转已缴', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '204011', '0', @p_id, '未缴批量转已缴', '0', NOW());
-- REMARK: 物业缴费V6.0 新增以下权限：“批量减免”权限；
SET @p_id = 204001010;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '批量减免', '账单管理 批量减免', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '204011', '0', @p_id, '批量减免', '0', NOW());

-- AUTHOR: 杨崇鑫 2018年9月13日
-- REMARK: 物业缴费V6.8（海岸馨服务项目对接） ：release：正式环境，beta：测试环境，以此来判断是否需要为海岸造测试数据
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES ('pay.v2.asset.haian_environment', 'release', 'release：正式环境，beta：测试环境，以此来判断是否需要为海岸造测试数据', 999993, NULL, 0);


--  ----------------------------------------- 如下脚本为OA脚本，已经提前在5.8.4.21080925上线了，这里不再执行      start   ------------------------------------------------

-- -- AUTHOR: 张智伟 20180822
-- -- REMARK: issue-36367 考勤规则新增打卡提醒设置初始化，默认开启
-- UPDATE eh_punch_rules SET punch_remind_flag=1,remind_minutes_on_duty=10 WHERE rule_type=1;
--
-- -- AUTHOR: 张智伟 20180822
-- -- REMARK: issue-36367 假期余额变更消息提醒文案配置
-- SET @max_template_id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_templates`),1);
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
-- VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10006, 'zh_CN', '消息标题','假期余额变动', 0);
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
-- VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10007, 'zh_CN', '假期余额变动提醒：管理员调整假期余额','管理员调整了你的假期余额：<#if annualLeaveAdd != 0><#if annualLeaveAdd gt 0>发放<#else>扣除</#if>年假${annualLeaveAddShow}</#if><#if annualLeaveAdd != 0 && overtimeAdd != 0>，</#if><#if overtimeAdd != 0><#if overtimeAdd gt 0>发放<#else>扣除</#if>调休${overtimeAddShow}</#if>。当前余额为：年假${annualLeaveBalance} ，调休${overtimeCompensationBalance} 。', 0);
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
-- VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10008, 'zh_CN', '假期余额变动提醒：年假/调休的审批被撤销/终止/删除','${categoryName}申请（${requestTime}） 已失效，假期已返还。当前余额为：年假 ${annualLeaveBalance} ，调休${overtimeCompensationBalance} 。', 0);
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
-- VALUE (@max_template_id:=@max_template_id+1, 'approval.tip.info', 10009, 'zh_CN', '假期余额变动提醒：请假审批提交','${categoryName}申请（${requestTime}） 已提交，假期已扣除。当前余额为：年假 ${annualLeaveBalance} ，调休${overtimeCompensationBalance} 。', 0);
--
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
-- VALUE (@max_template_id:=@max_template_id+1, 'punch.remind', 1, 'zh_CN', '消息标题','<#if punchType eq 0>上班打卡<#else>下班打卡</#if>', 0);
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
-- VALUE (@max_template_id:=@max_template_id+1, 'punch.remind', 2, 'zh_CN', '上班打卡提醒','最晚${onDutyTime}上班打卡，立即打卡', 0);
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
-- VALUE (@max_template_id:=@max_template_id+1, 'punch.remind', 3, 'zh_CN', '下班打卡提醒','上班辛苦了，记得打下班卡', 0);
--
-- -- AUTHOR: 张智伟 20180822
-- -- REMARK: issue-36367 打卡记录报表排序
-- UPDATE eh_punch_logs l INNER JOIN eh_organization_member_details d ON d.organization_id=l.enterprise_id AND d.target_id=l.user_id
-- SET l.detail_id=d.id
-- WHERE d.target_id>0 AND l.detail_id IS NULL;
--
-- UPDATE eh_punch_log_files l INNER JOIN eh_organization_member_details d ON d.organization_id=l.enterprise_id AND d.target_id=l.user_id
-- SET l.detail_id=d.id
-- WHERE d.target_id>0 AND l.detail_id IS NULL;
--
-- -- AUTHOR: 吴寒
-- -- REMARK: issue-33943 日程提醒1.2 增加5.9.0之后使用的提醒设置
-- SET @id = (SELECT MAX(id) FROM eh_remind_settings);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'准时','0',NULL,'1','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前5分钟','0',NULL,'2','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',300000);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前15分钟','0',NULL,'3','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',900000);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前30分钟','0',NULL,'4','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',1800000);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前1小时','0',NULL,'5','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',3600000);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前2小时','0',NULL,'6','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',7200000);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前1天','1',NULL,'7','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前2天','2',NULL,'8','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前3天','3',NULL,'9','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前5天','5',NULL,'10','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);
-- INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前1周','7',NULL,'11','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);
--
-- INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES('remind.version.segmen','5.8.4','日程提醒的版本分隔','0',NULL,NULL);
--
-- SET @tem_id = (SELECT MAX(id) FROM eh_locale_templates);
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','1','zh_CN','完成日程发消息','${trackContractName}的日程“${planDescription}” 已完成','0');
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','2','zh_CN','重置未完成日程发消息','${trackContractName}的日程“${planDescription}” 重置为未完成','0');
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','3','zh_CN','修改日程发消息','${trackContractName}修改了日程“${planDescription}”','0');
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','4','zh_CN','删除日程发消息','${trackContractName}删除了日程“${planDescription}”','0');
-- INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','5','zh_CN','取消共享日程发消息','${trackContractName}的日程“${planDescription}” 取消共享了','0');
--
-- -- AUTHOR: 吴寒
-- -- REMARK: 会议管理V1.2
-- UPDATE eh_configurations SET VALUE = 5000 WHERE NAME ='meeting.record.word.limit';
--
-- -- AUTHOR: 荣楠
-- -- REMARK: 工作汇报V1.2
-- UPDATE `eh_locale_templates` SET `text`='${applierName}给你提交了${reportName}（${reportTime}）', `code` = 101 WHERE `scope`='work.report.notification' AND `code`='1';
-- UPDATE `eh_locale_templates` SET `text`='${applierName}更新了Ta的${reportName}（${reportTime}）', `code` = 102 WHERE `scope`='work.report.notification' AND `code`='2';
-- UPDATE `eh_locale_templates` SET `text`='${commentatorName}在Ta的${reportName}（${reportTime}）中 回复了你', `code` = 1, `description` = '评论消息类型1' WHERE `scope`='work.report.notification' AND `code`='3';
-- UPDATE `eh_locale_templates` SET `text`='${commentatorName}在Ta的${reportName}（${reportTime}）中 发表了评论', `code` = 2, `description` = '评论消息类型2' WHERE `scope`='work.report.notification' AND `code`='4';
-- UPDATE `eh_locale_templates` SET `text`='${commentatorName}在你的${reportName}（${reportTime}）中 回复了你', `code` = 3, `description` = '评论消息类型3' WHERE `scope`='work.report.notification' AND `code`='5';
-- UPDATE `eh_locale_templates` SET `text`='${commentatorName}在${applierName}的${reportName}（${reportTime}）中 回复了你', `code` = 4, `description` = '评论消息类型4' WHERE `scope`='work.report.notification' AND `code`='6';
-- UPDATE `eh_locale_templates` SET `text`='${commentatorName}在你的${reportName}（${reportTime}）中 发表了评论', `code` = 5, `description` = '评论消息类型5' WHERE `scope`='work.report.notification' AND `code`='7';
--
--
-- UPDATE `eh_work_reports` SET `validity_setting` = '{"endTime":"10:00","endType":1,"startTime":"15:00","startType":0}' WHERE `report_type` = 0;
-- UPDATE `eh_work_reports` SET `validity_setting` = '{"endMark":"1","endTime":"10:00","endType":1,"startMark":"5","startTime":"15:00","startType":0}' WHERE `report_type` = 1;
-- UPDATE `eh_work_reports` SET `validity_setting` = '{"endMark":"1","endTime":"10:00","endType":1,"startMark":"31","startTime":"15:00","startType":0}' WHERE `report_type` = 2;
--
-- UPDATE `eh_work_report_templates` SET `icon_uri`='cs://1/image/aW1hZ2UvTVRwaFlqazJNVFk1WkdabFpEbGhNamc0T1RjNVltWmlOakl3TmpobE1qUXpOUQ' WHERE (`id`='1') LIMIT 1;
-- UPDATE `eh_work_report_templates` SET `icon_uri`='cs://1/image/aW1hZ2UvTVRvMU1tRTVPV0kwWlRjeU16WmlOR05rTnpWbE9HUTFZV1ExTlRZMFpHUm1Odw' WHERE (`id`='2') LIMIT 1;
-- UPDATE `eh_work_report_templates` SET `icon_uri`='cs://1/image/aW1hZ2UvTVRvd1lXRmtObUkwWkRaaFlXRXdPRFV5TmpjMU5qa3hNelk1TVRRNE5XUTRNdw' WHERE (`id`='3') LIMIT 1;
--
-- SET @config_id = (SELECT MAX(id) FROM eh_configurations);
-- INSERT INTO `ehcore`.`eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES (@config_id := @config_id + 1, 'work.report.icon', 'cs://1/image/aW1hZ2UvTVRveE9UVm1aRGhsTTJRek9HRmpaRFV3WXpKaU5ESTNNV1ppT0RneVpHRTFaQQ', 'the icon of the customize workReport ', '0', NULL, NULL);
--
-- UPDATE eh_work_reports SET `icon_uri`='cs://1/image/aW1hZ2UvTVRveE9UVm1aRGhsTTJRek9HRmpaRFV3WXpKaU5ESTNNV1ppT0RneVpHRTFaQQ' ;
-- UPDATE eh_work_reports SET `icon_uri`='cs://1/image/aW1hZ2UvTVRwaFlqazJNVFk1WkdabFpEbGhNamc0T1RjNVltWmlOakl3TmpobE1qUXpOUQ' WHERE report_template_id = 1;
-- UPDATE eh_work_reports SET `icon_uri`='cs://1/image/aW1hZ2UvTVRvMU1tRTVPV0kwWlRjeU16WmlOR05rTnpWbE9HUTFZV1ExTlRZMFpHUm1Odw' WHERE report_template_id = 2;
-- UPDATE eh_work_reports SET `icon_uri`='cs://1/image/aW1hZ2UvTVRvd1lXRmtObUkwWkRaaFlXRXdPRFV5TmpjMU5qa3hNelk1TVRRNE5XUTRNdw' WHERE report_template_id = 3;



--  ----------------------------------------- 如上脚本为OA脚本，已经提前在5.8.4.21080925上线了，这里不再执行      end   ------------------------------------------------

-- -- AUTHOR: 张智伟 20180822
-- -- REMARK: issue-36367 假期余额变更消息提醒文案配置
SET @max_template_id = IFNULL((SELECT MAX(`id`) FROM `eh_locale_templates`),1);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUE (@max_template_id:=@max_template_id+1, 'punch.remind', 4, 'zh_CN', '打卡首页','打卡', 0);



--  ----------------------------------------- “ 活动报名人”脚本，已经提前在5.8.4.21080925上线了，这里不再执行      start   ------------------------------------------------

-- -- AUTHOR: 梁燕龙
-- -- REMARK: 活动报名人数不足最低限制人数自动取消活动消息推送
-- INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
-- VALUES( 'announcement.notification', 1, 'zh_CN', '公告消息', '${subject}');
-- INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`) VALUES ('announcement',1,'zh_CN','公告消息');
-- INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`) VALUES ('forum',10007,'zh_CN','来晚啦，公告已不存在');
--

--  ----------------------------------------- “ 活动报名人”脚本，已经提前在5.8.4.21080925上线了，这里不再执行      end   ------------------------------------------------


-- AUTHOR: 严军
-- REMARK: 删除待办事项菜单
UPDATE eh_web_menus SET `status` = 0 WHERE id = 48130000;
-- AUTHOR:huangmingbo 20180919
-- REMARK: issue-36688/issue-33683/ 停车缴费V6.6.3 服务联盟V3.6
-- 37669修复
update eh_service_alliance_categories set default_order = id;

-- 33683 停车缴费V6.6.3
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('parking.jieshun', '10001', 'zh_CN', 'token获取失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('parking.jieshun', '10002', 'zh_CN', '生成请求摘要失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('parking.jieshun', '10003', 'zh_CN', '不支持当前充值类型');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('parking.jieshun', '10004', 'zh_CN', '不支持按天计算逾期金额');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('parking.jieshun', '10005', 'zh_CN', '未找到月卡信息');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('parking.jieshun', '10006', 'zh_CN', '未获取到费率信息');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('parking.jieshun', '10007', 'zh_CN', '未获取到月卡对应的费率信息');

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.loginUrl', 'http://www.jslife.com.cn/jsaims/login', '捷顺登录地址', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.funcUrl', 'http://www.jslife.com.cn/jsaims/as', '捷顺接口请求地址', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.version', '2', '捷顺接口版本', 0, NULL, 1);

-- -------------------------------------------------- 这一段已经有了。 这里注释掉  start---------------------------

-- -- AUTHOR: 严军
-- -- REMARK: 增加“企业钱包”模块，更新“会议室管理”菜单path
-- -- issue null
-- UPDATE eh_web_menus SET path = '/23000000/23010000/16041900' WHERE id = 16041900;
-- INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('59200', '企业钱包', '50000', '/100/50000/59200', '1', '3', '2', '240', '2018-09-19 14:42:53', NULL, NULL, '2018-09-19 14:43:07', '0', '0', '0', '0', 'org_control', '1', '1', 'module');
-- INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79100000', '企业钱包', '23010000', NULL, 'enterprise-wallet', '1', '2', '/23000000/23010000/79100000', 'zuolin', '240', '59200', '3', 'system', 'module', NULL);
-- INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79200000', '企业钱包', '77000000', NULL, 'enterprise-wallet', '1', '2', '/70000010/77000000/79200000', 'organization', '240', '59200', '3', 'system', 'module', NULL);
-- UPDATE eh_web_menus SET parent_id = 53000000, path = '/40000010/53000000/79200000', type = 'park', sort_num = 80 WHERE id =  79200000;
--
-- -------------------------------------------------- 这一段已经有了。 这里注释掉  end---------------------------



-- AUTHOR: 马世亨  20180919
-- REMARK: 停车缴费V6.6.2 基金小镇项目对接“车安”停车厂商
SET @configid = (select max(id) from eh_configurations);
INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name, is_readonly)
VALUES (@configid := @configid + 1 , 'parking.chean.url', 'http://113.98.59.44:9022', '车安基金小镇停车场url', '0', NULL, NULL);

INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name, is_readonly)
VALUES (@configid := @configid + 1 , 'parking.chean.privatekey', '71cfa1c59773ddfa289994e6d505bba3', '车安基金小镇停车场私钥', '0', NULL, NULL);

INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name, is_readonly)
VALUES (@configid := @configid + 1 , 'parking.chean.accessKeyId', 'UT', '车安基金小镇停车场接入方标识', '0', NULL, NULL);

INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name, is_readonly)
VALUES (@configid := @configid + 1 , 'parking.chean.branchno', '0', '车安基金小镇停车分点编号', '0', NULL, NULL);


-- -------------------------------------  标准版执行冲突了，并且标准版没有999938这些数据 这里注释了   start   ------------------------------------
-- SET @parkinglotid = (select max(id) from eh_parking_lots);
-- INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`, `order_tag`, `order_code`, `id_hash`, `func_list`)
-- VALUES (@parkinglotid := @parkinglotid + 1, 'community', '240111044332063798', '基金小镇停车场', 'CHEAN', '', '2', '1', '2018-07-27 17:48:07', '999938', NULL, '{\"tempfeeFlag\": 1, \"rateFlag\": 0, \"lockCarFlag\": 0, \"searchCarFlag\": 1, \"currentInfoType\": 0,\"identityCardFlag\":1}', SUBSTRING(@id, -3), '0', NULL,  '[\"tempfee\",\"monthRecharge\",\"searchCar\",\"carNum\",\"monthCardApply\"]');
--
-- SET @parkingcardid = (select max(id) from eh_parking_lots);
-- INSERT INTO `eh_parking_card_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `card_type_id`, `card_type_name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
-- VALUES (@parkingcardid := @parkingcardid + 1, '999938', 'community', '240111044332063798', @parkinglotid, '1', '月卡', '2', '1', '2018-05-03 10:49:48', NULL, NULL);

-- -------------------------------------  标准版执行冲突了，并且标准版没有999938这些数据 这里注释了   end   ------------------------------------

SET @locId = (select max(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@locId := @locId + 1, 'parking', '10033', 'zh_CN', '未查询到停车记录');


-- -------------------------------------  标准版执行冲突了，并且标准版没有“大沙河建投”这些数据 这里注释了   start   ------------------------------------
--
-- -- AUTHOR: 马世亨  20180919
-- -- REMARK: 停车缴费V6.6.4 【大沙河建投】二期智园项目，对接车安系统
-- SET @namespaceid = '999967';
-- SET @communityid = '240111044332064133';
-- SET @configid = (select max(id) from eh_configurations);
-- INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name, is_readonly)
-- VALUES (@configid := @configid + 1 , 'parking.cheanzhiyuan.url', 'http://113.98.59.44:9022', '车安大沙河智园停车场url', '0', NULL, NULL);
--
-- INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name, is_readonly)
-- VALUES (@configid := @configid + 1 , 'parking.cheanzhiyuan.privatekey', '71cfa1c59773ddfa289994e6d505bba3', '车安大沙河智园停车场私钥', '0', NULL, NULL);
--
-- INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name, is_readonly)
-- VALUES (@configid := @configid + 1 , 'parking.cheanzhiyuan.accessKeyId', 'UT', '车安大沙河智园停车场接入方标识', '0', NULL, NULL);
--
-- INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name, is_readonly)
-- VALUES (@configid := @configid + 1 , 'parking.cheanzhiyuan.branchno', '0', '车安大沙河智园停车分点编号', '0', NULL, NULL);
--
-- SET @parkinglotid = (select max(id) from eh_parking_lots);
-- INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`, `order_tag`, `order_code`, `id_hash`, `func_list`)
-- VALUES (@parkinglotid := @parkinglotid + 1, 'community', @communityid, '大沙河智园停车场', 'CHEANZHIYUAN', @namespaceid, '2', '1', '2018-05-21 14:36:29', '0', '{\"expiredRechargeFlag\": 0,\"monthlyDiscountFlag\":0}', '{\"tempfeeFlag\": 1, \"rateFlag\": 1, \"lockCarFlag\": 0, \"searchCarFlag\": 1, \"currentInfoType\": 0,\"identityCardFlag\":1,\"monthCardFlag\":1,\"monthRechargeFlag\":1}', SUBSTRING(@parkinglotid, -3), '8', NULL, '[\"tempfee\",\"monthRecharge\",\"searchCar\"]');
--
-- SET @parkingcardid = (select max(id) from eh_parking_card_types);
-- INSERT INTO `eh_parking_card_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `card_type_id`, `card_type_name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
-- VALUES (@parkingcardid := @parkingcardid + 1, @namespaceid, 'community', @communityid, @parkinglotid, '1', '月卡', '2', '1', '2018-05-03 10:49:48', NULL, NULL);


-- -------------------------------------  标准版执行冲突了，并且标准版没有“大沙河建投”这些数据 这里注释了   end   ------------------------------------


-- AUTHOR:丁建民 20180920
-- REMARK: issue-37007 资产设置一房一价，租赁价格大于设定的价格，则合同不需要审批
SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '20017', 'zh_CN', '周期输入格式不正确');
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '20018', 'zh_CN', '授权价格式不正确');

INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'contract', '10013', 'zh_CN', '合同计价条款计费周期不存在');
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'contract', '10014', 'zh_CN', '该房源信息已经存在');

-- AUTHOR: tangcen 2018年9月20日20:48:46
-- REMARK: 楼宇资产管理模块添加自定义tab卡
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (1001, 'asset_management', '0', '/1001', '租客管理', '', '0', NULL, '2', '1', NOW(), NULL, NULL);
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (1002, 'asset_management', '0', '/1002', '住户管理', '', '0', NULL, '2', '1', NOW(), NULL, NULL);
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (1003, 'asset_management', '0', '/1003', '合同管理', '', '0', NULL, '2', '1', NOW(), NULL, NULL);
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (1004, 'asset_management', '0', '/1004', '账单管理', '', '0', NULL, '2', '1', NOW(), NULL, NULL);
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (1005, 'asset_management', '0', '/1005', '活动记录', '', '0', NULL, '2', '1', NOW(), NULL, NULL);
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (1006, 'asset_management', '0', '/1006', '车辆管理', '', '0', NULL, '2', '1', NOW(), NULL, NULL);
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (1007, 'asset_management', '0', '/1007', '服务信息', '', '0', NULL, '2', '1', NOW(), NULL, NULL);
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (1008, 'asset_management', '0', '/1008', '历史房源', '', '0', NULL, '2', '1', NOW(), NULL, NULL);
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (1009, 'asset_management', '0', '/1009', '附件', '', '0', NULL, '2', '1', NOW(), NULL, NULL);




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
CREATE PROCEDURE `transfer_field`()
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
CREATE PROCEDURE `transfer_field_group`()
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


INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12114, 'enterprise_customer', 'entryStatusItemId', '入驻状态', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (5000, 'enterprise_customer', 12114, '未入驻', 1, 2, 1, sysdate(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (5001, 'enterprise_customer', 12114, '已入驻', 2, 2, 1, sysdate(), NULL, NULL, NULL);

INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (5003, 'enterprise_customer', 5, '流失客户', 2, 2, 1, sysdate(), NULL, NULL, NULL);



INSERT INTO `eh_var_field_groups`(`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (20001, 'investment_enterprise', 0, '/20001' , '招商客户信息', 'com.everhomes.customer.EnterpriseCustomer', 0, NULL, 2, 1, SYSDATE(), NULL, NULL);

INSERT INTO `eh_var_field_groups`(`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (20002, 'investment_enterprise', 20001, '/20001/20002' , '基本信息', 'com.everhomes.customer.EnterpriseCustomer', 0, NULL, 2, 1, SYSDATE(), NULL, NULL);


INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12115, 'enterprise_customer', 'customerContact', '客户联系人', 'String', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12116, 'enterprise_customer', 'channelContact', '渠道联系人', 'String', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12117, 'enterprise_customer', 'trackerUid', '招商跟进人', 'String', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12118, 'enterprise_customer', 'transactionRatio', '成交几率', 'String', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12119, 'enterprise_customer', 'expectedSignDate', '预计签约时间', 'Date', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"datetime\", \"length\": 32}');


SET @id = IFNULL((select max(id) from eh_var_field_ranges), 1);
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',2,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',5,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',6,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',24,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',12113,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',12115,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',12116,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',12117,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',12118,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',12119,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',12077,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/10/',48,'investment_promotion','enterprise_customer');


UPDATE eh_var_fields SET field_param = '{\"fieldParamType\": \"unRenameSelect\", \"length\": 32}' WHERE id = 5;
UPDATE eh_var_fields SET field_param = '{\"fieldParamType\": \"text\", \"length\": 32}' WHERE id = 12037;
UPDATE eh_var_fields SET field_param = '{\"fieldParamType\": \"text\", \"length\": 32}' WHERE id = 12041;

UPDATE eh_var_fields SET mandatory_flag = 1 WHERE id = 12115;
UPDATE eh_var_fields SET group_id = 10,group_path = '/1/10/' WHERE id IN (SELECT field_id FROM eh_var_field_ranges WHERE module_name = 'investment_promotion');

SET @id = (select max(id) from eh_var_field_group_ranges);
INSERT INTO `eh_var_field_group_ranges`(`id`, `group_id`, `module_name`, `module_type`) VALUES (@id:=@id+1, 1, 'investment_promotion', 'enterprise_customer');
INSERT INTO `eh_var_field_group_ranges`(`id`, `group_id`, `module_name`, `module_type`) VALUES (@id:=@id+1, 10, 'investment_promotion', 'enterprise_customer');

DELETE FROM eh_var_field_ranges WHERE field_id = 12113 AND module_name='enterprise_customer' AND module_type='enterprise_customer';
DELETE FROM eh_var_field_ranges WHERE field_id = 12115 AND module_name='enterprise_customer' AND module_type='enterprise_customer';
DELETE FROM eh_var_field_ranges WHERE field_id = 12116 AND module_name='enterprise_customer' AND module_type='enterprise_customer';
DELETE FROM eh_var_field_ranges WHERE field_id = 12117 AND module_name='enterprise_customer' AND module_type='enterprise_customer';
DELETE FROM eh_var_field_ranges WHERE field_id = 12118 AND module_name='enterprise_customer' AND module_type='enterprise_customer';
DELETE FROM eh_var_field_ranges WHERE field_id = 12119 AND module_name='enterprise_customer' AND module_type='enterprise_customer';

DELETE FROM eh_var_field_ranges WHERE field_id = 5 AND module_name='enterprise_customer' AND module_type='enterprise_customer';


-- AUTHOR 黄鹏宇 2018年9月11日
-- REMARK 增加招商客户权限细化
SET @id = (select max(id) from eh_service_module_privileges);

INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150020, 0, 150001, '查看客户', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150020, 0, 150002, '创建客户', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150020, 0, 150003, '编辑客户', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150020, 0, 150004, '删除客户', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150020, 0, 150005, '客户转换', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150020, 0, 150008, '导入客户', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150020, 0, 150009, '导出客户', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150020, 0, 150010, '一键转为资质客户', 0, SYSDATE());


INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150001, 0, '招商管理 查看客户权限', '招商管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150002, 0, '招商管理 创建客户权限', '招商管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150003, 0, '招商管理 编辑客户权限', '招商管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150004, 0, '招商管理 删除客户权限', '招商管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150005, 0, '招商管理 一键转为租客权限', '招商管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150008, 0, '招商管理 导入权限', '招商管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150009, 0, '招商管理 导出权限', '招商管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150010, 0, '招商管理 一键转为资质客户权限', '招商管理 业务模块权限', NULL);


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
UPDATE eh_var_fields SET mandatory_flag = 1 WHERE id = 5;

-- AUTHOR: 黄鹏宇
-- REMARK: 更改企业客户管理菜单为租客管理
update eh_web_menus set name = '租客管理' where name = '企业客户管理';
-- END

-- AUTHOR: 黄鹏宇
-- REMARK: 导出招商本地化
set @id = (select max(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id := @id +1, 'enterpriseCustomer.export', '2', 'zh_CN', '招商客户数据导出');


UPDATE eh_var_fields SET display_name = '行业领域' WHERE id = 24;



-- AUTHOR 黄鹏宇
-- REMARK 增加默认字段

set @item_id = (select max(id) from `eh_var_field_group_scopes`);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'investment_promotion', 1, '客户信息', 1, 2, 1, '2018-09-20 19:11:22', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'investment_promotion', 10, '基本信息', 2, 2, 1, '2018-09-20 19:11:22', NULL, NULL, NULL, NULL, NULL);



UPDATE `eh_var_field_scopes` SET STATUS = 0 WHERE `namespace_id` = 0 AND `module_name` = 'investment_promotion';
set @item_id = (select max(id) from `eh_var_field_scopes`);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 2, '{\"fieldParamType\": \"text\", \"length\": 32}', '客户名称', 1, 1, 2, 1, SYSDATE(), NULL, NULL, NULL, '/1/10/', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 5, '{\"fieldParamType\": \"unRenameSelect\", \"length\": 32}', '客户状态', 1, 2, 2, 1, SYSDATE(), NULL, NULL, NULL, '/1/10/', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 12115, '{\"fieldParamType\": \"text\", \"length\": 32}', '客户联系人', 1, 3, 2, 1, SYSDATE(), NULL, NULL, NULL, '/1/10/', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 24, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}', '行业领域', 0, 4, 2, 1, SYSDATE(), NULL, NULL, NULL, '/1/10/', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 12118, '{\"fieldParamType\": \"text\", \"length\": 32}', '成交几率', 0, 5, 2, 1, SYSDATE(), NULL, NULL, NULL, '/1/10/', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 12119, '{\"fieldParamType\": \"datetime\", \"length\": 32}', '预计签约时间', 0, 6, 2, 1, SYSDATE(), NULL, NULL, NULL, '/1/10/', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 12116, '{\"fieldParamType\": \"text\", \"length\": 32}', '渠道联系人', 0, 7, 2, 1, SYSDATE(), NULL, NULL, NULL, '/1/10/', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 48, '{\"fieldParamType\": \"multiText\", \"length\": 2048}', '备注', 0, 8, 2, 1, SYSDATE(), NULL, NULL, NULL, '/1/10/', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 12077, '{\"fieldParamType\": \"file\", \"length\": 9}', '附件', 0, 9, 2, 1, SYSDATE(), NULL, NULL, NULL, '/1/10/', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 6, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}', '客户来源', 0, 10, 2, 1, SYSDATE(), NULL, NULL, NULL, '/1/10/', NULL);
INSERT INTO `eh_var_field_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 10, 12117, '{\"fieldParamType\": \"text\", \"length\": 32}', '招商跟进人', 0, 11, 2, 1, SYSDATE(), NULL, NULL, NULL, '/1/10/', NULL);


UPDATE `eh_var_field_item_scopes` SET STATUS = 0 WHERE `namespace_id` = 0 AND `module_name` = 'investment_promotion';
set @item_id = (select max(id) from `eh_var_field_item_scopes`);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 5, 3, '初次接触', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 5, 4, '潜在客户', 2, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 5, 5, '意向客户', 3, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 5, 6, '已成交客户', 4, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 5, 7, '历史客户', 5, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 5, 5003, '流失客户', 6, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 202, '集成电路', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 203, '软件', 2, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 204, '通信技术', 3, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 205, '生物医药', 4, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 206, '医疗器械', 5, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 207, '光机电', 6, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 208, '金融服务', 7, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 209, '新能源与环保', 8, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 210, '文化创意', 9, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 211, '商业-餐饮', 10, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 212, '商业-超市', 11, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 213, '商业-食堂', 12, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 214, '商业-其他', 13, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 24, 215, '其他', 14, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 6, 8, '其他', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 6, 192, '广告', 2, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 6, 193, '报纸', 3, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 6, 194, '中介', 4, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 6, 195, '网站', 5, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes`(`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES ((@item_id:=@item_id+1), 0, 'investment_promotion', 6, 196, '活动', 6, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);

-- AUTHOR: tangcen
-- REMARK: 修改园区入驻提交申请的默认模板
UPDATE eh_general_forms SET template_text='[{"dataSourceType":"USER_NAME","dynamicFlag":1,"fieldDisplayName":"用户姓名","fieldExtra":"{\\"limitWord\\":10}","fieldName":"USER_NAME","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"USER_PHONE","dynamicFlag":1,"fieldDisplayName":"手机号码","fieldExtra":"{\\"limitWord\\":11}","fieldName":"USER_PHONE","fieldType":"INTEGER_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"CUSTOMER_NAME","dynamicFlag":1,"fieldDisplayName":"承租方","fieldExtra":"{\\"limitWord\\":50}","fieldName":"CUSTOMER_NAME","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":0,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROJECT_NAME","dynamicFlag":1,"fieldDisplayName":"项目名称","fieldExtra":"{\\"limitWord\\":20}","fieldName":"LEASE_PROJECT_NAME","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROMOTION_BUILDING","dynamicFlag":1,"fieldDisplayName":"楼栋名称","fieldExtra":"{\\"limitWord\\":20}","fieldName":"LEASE_PROMOTION_BUILDING","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROMOTION_APARTMENT","dynamicFlag":1,"fieldDisplayName":"门牌号码","fieldExtra":"{\\"limitWord\\":20}","fieldName":"LEASE_PROMOTION_APARTMENT","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":0,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROMOTION_DESCRIPTION","dynamicFlag":0,"fieldDisplayName":"备注说明","fieldName":"LEASE_PROMOTION_DESCRIPTION","fieldType":"MULTI_LINE_TEXT","renderType":"DEFAULT","requiredFlag":0,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"CUSTOM_DATA","dynamicFlag":0,"fieldName":"CUSTOM_DATA","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"visibleType":"HIDDEN"}]' WHERE owner_type='EhLeasePromotions';
UPDATE eh_general_forms SET template_text='[{"dataSourceType":"USER_NAME","dynamicFlag":1,"fieldDisplayName":"用户姓名","fieldExtra":"{\\"limitWord\\":10}","fieldName":"USER_NAME","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"USER_PHONE","dynamicFlag":1,"fieldDisplayName":"手机号码","fieldExtra":"{\\"limitWord\\":11}","fieldName":"USER_PHONE","fieldType":"INTEGER_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"CUSTOMER_NAME","dynamicFlag":1,"fieldDisplayName":"承租方","fieldExtra":"{\\"limitWord\\":50}","fieldName":"CUSTOMER_NAME","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":0,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROJECT_NAME","dynamicFlag":1,"fieldDisplayName":"项目名称","fieldExtra":"{\\"limitWord\\":20}","fieldName":"LEASE_PROJECT_NAME","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROMOTION_BUILDING","dynamicFlag":1,"fieldDisplayName":"楼栋名称","fieldExtra":"{\\"limitWord\\":20}","fieldName":"LEASE_PROMOTION_BUILDING","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROMOTION_DESCRIPTION","dynamicFlag":0,"fieldDisplayName":"备注说明","fieldName":"LEASE_PROMOTION_DESCRIPTION","fieldType":"MULTI_LINE_TEXT","renderType":"DEFAULT","requiredFlag":0,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"CUSTOM_DATA","dynamicFlag":0,"fieldName":"CUSTOM_DATA","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"visibleType":"HIDDEN"}]' WHERE owner_type='EhBuildings';
UPDATE eh_general_forms SET template_text='[{"dataSourceType":"USER_NAME","dynamicFlag":1,"fieldDisplayName":"用户姓名","fieldExtra":"{\\"limitWord\\":10}","fieldName":"USER_NAME","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"USER_PHONE","dynamicFlag":1,"fieldDisplayName":"手机号码","fieldExtra":"{\\"limitWord\\":11}","fieldName":"USER_PHONE","fieldType":"INTEGER_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"CUSTOMER_NAME","dynamicFlag":1,"fieldDisplayName":"承租方","fieldExtra":"{\\"limitWord\\":50}","fieldName":"CUSTOMER_NAME","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":0,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROJECT_NAME","dynamicFlag":1,"fieldDisplayName":"项目名称","fieldExtra":"{\\"limitWord\\":20}","fieldName":"LEASE_PROJECT_NAME","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROMOTION_DESCRIPTION","dynamicFlag":0,"fieldDisplayName":"备注说明","fieldName":"LEASE_PROMOTION_DESCRIPTION","fieldType":"MULTI_LINE_TEXT","renderType":"DEFAULT","requiredFlag":0,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"CUSTOM_DATA","dynamicFlag":0,"fieldName":"CUSTOM_DATA","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"visibleType":"HIDDEN"}]' WHERE owner_type='EhLeaseProjects';

-- AUTHOR: 黄良铭
-- REMARK: 脚本管理菜单
INSERT INTO eh_service_modules(id ,NAME , parent_id,path ,TYPE ,LEVEL ,STATUS ,default_order ,menu_auth_flag,category, operator_uid , creator_uid)
VALUES(60300,'脚本管理',60000,'/100/60000/60300',1,3,2,30,1,'module', 1, 1);
INSERT INTO eh_web_menus(id,NAME,parent_id,icon_url,data_type ,leaf_flag,STATUS,path,TYPE,sort_num,module_id,LEVEL,condition_type,category,config_type)
VALUES(79000000 ,'脚本管理',21000000,NULL,'script-management',1,2,'/11000000/21000000/79000000','zuolin',30,60300,3,'system','module',NULL);

-- AUTHOR: 马世亨
-- REMARK: 更新访客表单
update eh_general_forms set template_text = '[{"dynamicFlag":0,"fieldDesc":"输入车牌号码","fieldDisplayName":"车牌号码","fieldExtra":"{\\"limitWord\\":512}","fieldName":"plateNo","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":0,"validatorType":"TEXT_LIMIT","visibleType":"HIDDEN"},{"dynamicFlag":0,"fieldDesc":"输入证件号码","fieldDisplayName":"证件号码","fieldExtra":"{\\"limitWord\\":512}","fieldName":"idNumber","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":0,"validatorType":"TEXT_LIMIT","visibleType":"HIDDEN"},{"dynamicFlag":0,"fieldDesc":"输入到访楼层","fieldDisplayName":"到访楼层","fieldExtra":"{\\"limitWord\\":512}","fieldName":"visitFloor","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":0,"validatorType":"TEXT_LIMIT","visibleType":"HIDDEN"},{"dynamicFlag":0,"fieldDesc":"输入到访门牌","fieldDisplayName":"到访门牌","fieldExtra":"{\\"limitWord\\":512}","fieldName":"visitAddresses","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":0,"validatorType":"TEXT_LIMIT","visibleType":"HIDDEN"}]' where module_id = 41800;


-- AUTHOR: tangcen
-- REMARK: 添加房源招商的权限
SET @id = (select max(id) from eh_service_module_privileges);
-- 发布招商信息权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150010, 0, 150101, '发布招商信息', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150101, 0, '房源招商 发布招商信息权限', '招商管理 业务模块权限', NULL);
-- 编辑招商信息权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150010, 0, 150102, '编辑招商信息', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150102, 0, '房源招商 编辑招商信息权限', '招商管理 业务模块权限', NULL);
-- 删除招商信息权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150010, 0, 150103, '删除招商信息', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150103, 0, '房源招商 删除招商信息权限', '招商管理 业务模块权限', NULL);
-- 导出招商信息权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150010, 0, 150104, '导出招商信息', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150104, 0, '房源招商 导出招商信息权限', '招商管理 业务模块权限', NULL);
-- 排序权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150010, 0, 150105, '排序', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150105, 0, '房源招商 排序权限', '招商管理 业务模块权限', NULL);
-- 导出申请记录权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150010, 0, 150106, '导出申请记录', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150106, 0, '房源招商 导出申请记录权限', '招商管理 业务模块权限', NULL);
-- 转为意向客户权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150010, 0, 150107, '转为意向客户', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150107, 0, '房源招商 转为意向客户权限', '招商管理 业务模块权限', NULL);
-- 删除申请记录权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150010, 0, 150108, '删除申请记录', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150108, 0, '房源招商 删除申请记录权限', '招商管理 业务模块权限', NULL);

-- AUTHOR: tangcen
-- REMARK: 添加招商申请表单的默认字段
SET @general_form_templates_id = (SELECT MAX(id) FROM `eh_general_form_templates`);
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES (@general_form_templates_id:=@general_form_templates_id+1, '0', '0', '0', 'EhOrganizations', '150010', 'investmentAd', '房源招商', '0', 'DEFAULT_JSON', '[\r\n{\r\n	\"dynamicFlag\": 0,\r\n	\"fieldDesc\": \"承租方\",\r\n	\"fieldDisplayName\": \"承租方\",\r\n	\"fieldExtra\": \"{}\",\r\n	\"fieldName\": \"ENTERPRISE_NAME\",\r\n	\"fieldType\": \"SINGLE_LINE_TEXT\",\r\n	\"remark\": \"允许用户手动输入；\",\r\n	\"disabled\": true,\r\n	\"renderType\": \"DEFAULT\",\r\n	\"requiredFlag\": 1,\r\n	\"validatorType\": \"TEXT_LIMIT\",\r\n	\"visibleType\": \"EDITABLE\",\r\n	\"filterFlag\": 1\r\n},{\r\n	\"dynamicFlag\": 0,\r\n	\"fieldDesc\": \"用户姓名\",\r\n	\"fieldDisplayName\": \"用户姓名\",\r\n	\"fieldExtra\": \"{}\",\r\n	\"fieldName\": \"USER_NAME\",\r\n	\"fieldType\": \"SINGLE_LINE_TEXT\",\r\n	\"remark\": \"系统自动获取APP端登录用户的姓名；\",\r\n	\"disabled\": true,\r\n	\"renderType\": \"DEFAULT\",\r\n	\"requiredFlag\": 1,\r\n	\"validatorType\": \"TEXT_LIMIT\",\r\n	\"visibleType\": \"EDITABLE\",\r\n	\"filterFlag\": 1\r\n},\r\n{\r\n	\"dynamicFlag\": 0,\r\n	\"fieldDesc\": \"手机号码\",\r\n	\"fieldDisplayName\": \"手机号码\",\r\n	\"fieldExtra\": \"{}\",\r\n	\"fieldName\": \"USER_PHONE\",\r\n	\"fieldType\": \"NUMBER_TEXT\",\r\n	\"remark\": \"系统自动获取APP端登录用户的手机号码；\",\r\n	\"disabled\": true,\r\n	\"renderType\": \"DEFAULT\",\r\n	\"requiredFlag\": 1,\r\n	\"validatorType\": \"TEXT_LIMIT\",\r\n	\"visibleType\": \"EDITABLE\",\r\n	\"filterFlag\": 1\r\n},\r\n{\r\n	\"dynamicFlag\": 0,\r\n	\"fieldDesc\": \"意向房源\",\r\n	\"fieldDisplayName\": \"意向房源\",\r\n	\"fieldExtra\": \"{}\",\r\n	\"fieldName\": \"APARTMENT\",\r\n	\"fieldType\": \"SINGLE_LINE_TEXT\",\r\n	\"remark\": \"允许用户手动选择；\",\r\n	\"disabled\": true,\r\n	\"renderType\": \"DEFAULT\",\r\n	\"requiredFlag\": 1,\r\n	\"validatorType\": \"TEXT_LIMIT\",\r\n	\"visibleType\": \"EDITABLE\",\r\n	\"filterFlag\": 1\r\n}]', '1', '1', NULL, '2018-09-12 11:52:26');

-- AUTHOR: tangcen
-- REMARK: 添加房源招商的报错信息
SET @id = (select max(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'investmentAd', '100000', 'zh_CN', '无招商广告数据');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'investmentAd', '100001', 'zh_CN', '招商广告不存在');

-- AUTHOR: tangcen
-- REMARK: 资产管理的管理配置页面添加默认的tab卡
set @item_id = (select max(id) from `eh_var_field_group_scopes`);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'asset_management', 1001, '租客管理', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'asset_management', 1003, '合同管理', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'asset_management', 1004, '账单管理', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'asset_management', 1005, '活动记录', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'asset_management', 1007, '服务信息', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'asset_management', 1008, '历史房源', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'asset_management', 1009, '附件', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);


-- ---------------------------------------------执行冲突了，应该是之前已经合并过了 start---------------------------------------------
-- -- AUTHOR: tangcen
-- -- REMARK:添加房源招商、招商客户管理模块
-- INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('150010', '房源招商', '110000', '/200/110000/150010', '1', '3', '2', '0', NOW(), '{\"url\":\"${home.url}/park-entry-web/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0#/home/#sign_suffix\"}', '14', NOW(), '0', '0', '0', '0', 'community_control', '1', '1', 'module');
-- INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('150020', '招商客户管理', '110000', '/200/110000/150020', '1', '3', '2', '0', NOW(), '{\"url\":\"${home.url}/rentCustomer/build/index.html?hideNavigationBar=1#/home#sign_suffix\"}', '14', NOW(), '0', '0', '0', '0', 'community_control', '1', '1', 'module');

-- ---------------------------------------------执行冲突了，应该是之前已经合并过了 end---------------------------------------------

-- -- AUTHOR: tangcen
-- REMARK:添加房源招商、招商客户管理的web menu
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79500000', '房源招商', '16210000', NULL, 'investment-ad', '1', '2', '/16000000/16210000/79500000', 'zuolin', '80', '150010', '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79600000', '房源招商', '16210000', NULL, 'investment-ad', '1', '2', '/16000000/16210000/79600000', 'park', '80', '150010', '3', 'system', 'module', NULL);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79710000', '招商客户管理', '16210000', NULL, 'business-invitation', '1', '2', '/16000000/16210000/79710000', 'zuolin', '80', '150020', '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79720000', '招商客户管理', '16210000', NULL, 'business-invitation', '1', '2', '/16000000/16210000/79720000', 'park', '80', '150020', '3', 'system', 'module', NULL);

-- AUTHOR: yanjun
-- REMARK: #34097  域空间配置V1.9（支持唤起小程序）
UPDATE eh_service_modules SET `name` = '普通链接' WHERE id = 90100;

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('180000', '第三方应用', '90000', '/400/90000/180000', '1', '3', '2', '20', '2018-09-21 15:03:32', NULL, '14', NULL, '0', '0', NULL, '1', '', '1', '1', 'module');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('190000', '微信小程序', '90000', '/400/90000/190000', '1', '3', '2', '30', '2018-09-21 15:04:26', NULL, '14', NULL, '0', '0', NULL, '1', '', '1', '1', 'module');


-- AUTHOR: 黄鹏宇
-- REMARK: 更改group_path编写不规范的问题
UPDATE eh_var_fields SET group_path = '/1/10/' WHERE group_path = '/1/10';
UPDATE eh_var_fields SET display_name = '来源渠道' WHERE id = 6;
UPDATE eh_var_field_scopes SET field_display_name = '来源渠道' WHERE field_id = 6 AND field_display_name = '客户来源';
UPDATE eh_var_field_scopes SET group_path = '/1/10/' WHERE group_path = '/1/10' and status = 2;


-- AUTHOR: 黄鹏宇
-- REMARK: 去除有两个拜访人的问题
DELETE FROM eh_var_field_scopes WHERE field_id = (SELECT id FROM eh_var_fields WHERE name = 'visitPersonName');
DELETE FROM eh_var_fields WHERE name = 'visitPersonName';

-- AUTHOR: 黄鹏宇
-- REMAKE: 将module中的企业客户换成租客
UPDATE eh_service_module_apps SET name = '租客管理' WHERE module_id = 21100;
UPDATE eh_service_modules SET name = '租客管理' WHERE id = 21100;


-- AUTHOR: 黄鹏宇
-- REMARK: 将系统中租客scope的客户类型去除
UPDATE eh_var_field_scopes SET `status` = 0 WHERE module_name = 'enterprise_customer' AND field_id = 5;

-- AUTHOR 黄鹏宇
-- REMARK : 删除数据库中的脏数据
UPDATE eh_var_field_item_scopes SET `status` = 0 WHERE item_id = 3 AND field_id != 5 ;

-- AUTHOR 黄鹏宇
-- REMARK : 删除租客中的资质客户权限
DELETE FROM eh_acl_privileges WHERE id = 21116;
DELETE FROM eh_service_module_privileges WHERE privilege_id = 21116;




-- AUTHOR: 严军
-- REMARK: 增加默认主题色

DELETE from eh_configurations WHERE NAME = 'theme.color';

INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '1000000', '#0B87D9', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999999', '#1E274E', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999996', '#1FA24D', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999993', '#1A538F', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999992', '#0B87D9', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999991', '#F2C500', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999990', '#D10000', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999989', '#D10000', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999988', '#D10000', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999986', '#D10000', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999985', '#719B87', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999984', '#673AB7', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999983', '#1FBCD2', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999982', '#F2C500', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999981', '#00B9EF', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999980', '#1FBCD2', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999979', '#00B9EF', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999978', '#253754', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999977', '#00B9EF', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999976', '#1FA24D', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999975', '#1FA24D', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999974', '#D10000', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999973', '#F2C500', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999972', '#10A1F1', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999971', '#0B87D9', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999970', '#F2C500', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999969', '#10A1F1', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999968', '#DBA561', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999967', '#D10000', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999966', '#3781E6', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999965', '#0B87D9', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999964', '#3781E6', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999962', '#0B87D9', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999961', '#10A1F1', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999960', '#673AB7', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999959', '#673AB7', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999958', '#00B9EF', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999957', '#F2C500', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999956', '#1FBCD2', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999955', '#00B9EF', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999954', '#0B87D9', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999953', '#333333', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999952', '#D10000', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999951', '#D10000', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999950', '#D10000', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999949', '#F2C500', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999948', '#DBA561', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999947', '#0B87D9', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999946', '#1FA24D', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999945', '#10A1F1', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999944', '#10A1F1', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999942', '#0B87D9', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999941', '#DBA561', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999940', '#DBA561', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999939', '#0B87D9', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999938', '#1A538F', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999936', '#1FA24D', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999935', '#DBA561', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999932', '#1A538F', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999930', '#F2C500', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999929', '#D10000', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '999927', '#F06416', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '11', '#1FA24D', '主题色', '主题色');
INSERT INTO `eh_configurations` (`name`, `namespace_id`, `value`, `description`, `display_name`) VALUES ('theme.color', '1', '#10A1F1', '主题色', '主题色');

-- end

-- AUTHOR: tangcen
-- REMARK: 添加申请单的报错信息
SET @id = (select max(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'requisition', '506', 'zh_CN', '未配置审批管理');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'requisition', '507', 'zh_CN', '未启用审批管理');

-- AUTHOR 黄鹏宇
-- REMARK 插入资质客户白名单和更新资质客户按钮
UPDATE eh_service_module_functions SET module_id = 150020 WHERE id = 43980;


set @id=(SELECT max(id) FROM eh_service_module_include_functions);
INSERT INTO `eh_service_module_include_functions`(`id`, `namespace_id`, `module_id`, `community_id`, `function_id`) VALUES (@id:= @id+1, 999944, 150020, NULL, 43980);
update eh_customer_trackings t1 set t1.customer_source = (select customer_source from eh_enterprise_customers t2 where t2.id = t1.customer_id);
-- END


-- AUTHOR 黄鹏宇
-- REMARK 同步名称
-- UPDATE eh_var_field_item_scopes a inner join eh_var_field_items b on a.item_id = b.id SET a.item_display_name = b.display_name;
-- UPDATE eh_var_field_scopes a inner join eh_var_fields b on a.field_id = b.id SET a.field_display_name = b.display_name, a.field_param = b.field_param;
-- UPDATE eh_var_field_group_scopes a inner join eh_var_field_groups b on a.group_id = b.id SET a.group_display_name = b.title;
-- END


-- AUTHOR 黄鹏宇
-- REMARK 修改客户的名称
UPDATE eh_var_fields set display_name = '客户状态' where id = 5;
UPDATE eh_var_field_items set  display_name ='初次接触' where id  = 3;
UPDATE eh_var_field_items set  display_name ='潜在客户' where id  = 4;

update eh_var_field_item_scopes set item_display_name ='初次接触' where item_id = 3 and field_id =5 and `status` = 2;
update eh_var_field_item_scopes set item_display_name ='潜在客户' where item_id = 4 and field_id =5 and `status` = 2;
-- END


-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR: liangqishi  20180702
-- REMARK: （此行放说明，可以多行，多行时每行前面都有REMARK）某模块增加初始化数据。。。。。。。
-- REMARK:。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。

-- 大沙河创新大厦停车场
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_DSHCXMall.cid', '880075501000675', '客户号', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_DSHCXMall.usr', '880075501000675', '捷顺登录账户名', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_DSHCXMall.psw', '880075501000675', '捷顺登录密码', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_DSHCXMall.signKey', 'a07b3c136b683440b5b80ab5e492fed6', 'signKey', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_DSHCXMall.parkCode', '0000000749', '小区编号', 0, NULL, 1);

set @max_lots_id := (select ifnull(max(id),0)  from eh_parking_lots);
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`, `order_tag`, `order_code`, `id_hash`, `func_list`) VALUES ((@max_lots_id := @max_lots_id + 1), 'community', 240111044332059932, '创新大厦停车场', 'JIESHUN_DSHCXMall', '', 2, 1, now(), 999967, '{"expiredRechargeFlag":1,"expiredRechargeMonthCount":1,"expiredRechargeType":1,"maxExpiredDay":3,"monthlyDiscountFlag":0,"tempFeeDiscountFlag":0}', '{"tempfeeFlag": 1, "rateFlag": 0, "lockCarFlag": 0, "searchCarFlag": 0, "currentInfoType": 0,"identityCardFlag":0}', right(@max_lots_id, 3), @max_lots_id, NULL, '["tempfee","monthRecharge"]');

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

-- AUTHOR:huangmingbo 20180919
-- REMARK: issue-36688 停车缴费V6.6.3 光企云二期停车场
update eh_parking_lots set name = '光大we谷A区停车场' where vendor_name = 'GUANG_DA_WE_GU';

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_GQY2.cid', '880076901009202', '客户号', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_GQY2.usr', '880076901009202', '捷顺登录账户名', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_GQY2.psw', '880076901009202', '捷顺登录密码', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_GQY2.signKey', '4b86380928753347638b4b6f3db7eb22', 'signKey', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_GQY2.parkCode', '0000009122', '小区编号', 0, NULL, 1);


set @max_lots_id := (select ifnull(max(id),0)  from eh_parking_lots);
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`, `order_tag`, `order_code`, `id_hash`, `func_list`) VALUES ((@max_lots_id := @max_lots_id + 1), 'community', 240111044331056800, '光大we谷B区停车场', 'JIESHUN_GQY2', '', 2, 1, now(), 999979, '{"expiredRechargeFlag":1,"expiredRechargeType":1,,"expiredRechargeMonthCount":1,"maxExpiredDay":180,"monthlyDiscountFlag":0,"tempFeeDiscountFlag":0}', '{"tempfeeFlag": 1, "rateFlag": 0, "lockCarFlag": 0, "searchCarFlag": 0, "currentInfoType": 0,"identityCardFlag":0}', right(@max_lots_id, 3), @max_lots_id, NULL, '["tempfee","monthRecharge"]');

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


-- AUTHOR: 杨崇鑫 2018年9月17日
-- REMARK: 物业缴费V6.7（对接统一订单） ：历史支付记录数据迁移
INSERT INTO `eh_payment_bill_orders`(id, `namespace_id`, `bill_id`, `order_number`, `payment_order_id`,`general_order_id`,`amount`, `payment_status`, `payment_order_type`, `payment_type`, `payment_time`, `payment_channel`, `uid`, `create_time`) VALUES (1, 999949, 226, 'WUF00000000000000116', 26941, 10001,0.1, 2, 3, 8, '2018-06-21 11:07:56', 2, 484095, '2018-06-21 11:07:56');
INSERT INTO `eh_payment_bill_orders`(id, `namespace_id`, `bill_id`, `order_number`, `payment_order_id`,`general_order_id`,`amount`, `payment_status`, `payment_order_type`, `payment_type`, `payment_time`, `payment_channel`, `uid`, `create_time`) VALUES (2, 999949, 221, 'WUF00000000000000124', 26947, 10002,0.3, 2, 3, 8, '2018-06-21 11:17:42', 2, 487333, '2018-06-21 11:17:42');
INSERT INTO `eh_payment_bill_orders`(id, `namespace_id`, `bill_id`, `order_number`, `payment_order_id`,`general_order_id`,`amount`, `payment_status`, `payment_order_type`, `payment_type`, `payment_time`, `payment_channel`, `uid`, `create_time`) VALUES (3, 999949, 222, 'WUF00000000000000124', 26947, 10003,0.3, 2, 3, 8, '2018-06-21 11:17:42', 2, 487333, '2018-06-21 11:17:42');
INSERT INTO `eh_payment_bill_orders`(id, `namespace_id`, `bill_id`, `order_number`, `payment_order_id`,`general_order_id`,`amount`, `payment_status`, `payment_order_type`, `payment_type`, `payment_time`, `payment_channel`, `uid`, `create_time`) VALUES (4, 999949, 225, 'WUF00000000000000124', 26947, 10004,0.3, 2, 3, 8, '2018-06-21 11:17:42', 2, 487333, '2018-06-21 11:17:42');
INSERT INTO `eh_payment_bill_orders`(id, `namespace_id`, `bill_id`, `order_number`, `payment_order_id`,`general_order_id`,`amount`, `payment_status`, `payment_order_type`, `payment_type`, `payment_time`, `payment_channel`, `uid`, `create_time`) VALUES (5, 999949, 223, 'WUF00000000000000124', 26947, 10005,0.14, 2, 3, 8, '2018-06-21 11:17:42', 2, 487333, '2018-06-21 11:17:42');
INSERT INTO `eh_payment_bill_orders`(id, `namespace_id`, `bill_id`, `order_number`, `payment_order_id`,`general_order_id`,`amount`, `payment_status`, `payment_order_type`, `payment_type`, `payment_time`, `payment_channel`, `uid`, `create_time`) VALUES (6, 999949, 228, 'WUF00000000000000126', 26950, 10006,0.01, 2, 3, 8, '2018-06-21 11:22:43', 2, 487333, '2018-06-21 11:22:43');
INSERT INTO `eh_payment_bill_orders`(id, `namespace_id`, `bill_id`, `order_number`, `payment_order_id`,`general_order_id`,`amount`, `payment_status`, `payment_order_type`, `payment_type`, `payment_time`, `payment_channel`, `uid`, `create_time`) VALUES (7, 999949, 226, 'WUF00000000000000145', 27040, 10007,0.1, 2, 3, 8, '2018-06-21 14:02:39', 2, 484095, '2018-06-21 14:02:39');
INSERT INTO `eh_payment_bill_orders`(id, `namespace_id`, `bill_id`, `order_number`, `payment_order_id`,`general_order_id`,`amount`, `payment_status`, `payment_order_type`, `payment_type`, `payment_time`, `payment_channel`, `uid`, `create_time`) VALUES (8, 999949, 226, 'WUF00000000000000147', 27046, 10008,0.1, 2, 3, 8, '2018-06-21 14:11:16', 2, 484095, '2018-06-21 14:11:16');
INSERT INTO `eh_payment_bill_orders`(id, `namespace_id`, `bill_id`, `order_number`, `payment_order_id`,`general_order_id`,`amount`, `payment_status`, `payment_order_type`, `payment_type`, `payment_time`, `payment_channel`, `uid`, `create_time`) VALUES (9, 999949, 228, 'WUF00000000000000150', 27049, 10009,0.01, 2, 3, 8, '2018-06-21 14:16:15', 2, 487333, '2018-06-21 14:16:15');
INSERT INTO `eh_payment_bill_orders`(id, `namespace_id`, `bill_id`, `order_number`, `payment_order_id`,`general_order_id`,`amount`, `payment_status`, `payment_order_type`, `payment_type`, `payment_time`, `payment_channel`, `uid`, `create_time`) VALUES (10, 999949, 229, 'WUF00000000000000151', 27051, 10010,0.02, 2, 3, 9, '2018-06-21 14:18:44', 2, 487333, '2018-06-21 14:18:44');
INSERT INTO `eh_payment_bill_orders`(id, `namespace_id`, `bill_id`, `order_number`, `payment_order_id`,`general_order_id`,`amount`, `payment_status`, `payment_order_type`, `payment_type`, `payment_time`, `payment_channel`, `uid`, `create_time`) VALUES (11, 999949, 230, 'WUF00000000000000156', 27066, 10011,0.02, 2, 3, 10, '2018-06-21 15:04:19', 2, 487333, '2018-06-21 15:04:19');
INSERT INTO `eh_payment_bill_orders`(id, `namespace_id`, `bill_id`, `order_number`, `payment_order_id`,`general_order_id`,`amount`, `payment_status`, `payment_order_type`, `payment_type`, `payment_time`, `payment_channel`, `uid`, `create_time`) VALUES (12, 999949, 231, 'WUF00000000000000156', 27066, 10012,0.04, 2, 3, 11, '2018-06-21 15:04:19', 2, 487333, '2018-06-21 15:04:19');
INSERT INTO `eh_payment_bill_orders`(id, `namespace_id`, `bill_id`, `order_number`, `payment_order_id`,`general_order_id`,`amount`, `payment_status`, `payment_order_type`, `payment_type`, `payment_time`, `payment_channel`, `uid`, `create_time`) VALUES (13, 999949, 232, 'WUF00000000000000180', 27107, 10013,0.01, 2, 3, 12, '2018-06-21 16:17:13', 2, 486174, '2018-06-21 16:17:13');


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

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: inno
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本
-- AUTHOR: lifei  20180913
-- REMARK: （此行放说明，可以多行，多行时每行前面都有REMARK）某模块增加初始化数据。。。。。。。
-- REMARK:。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。
INSERT INTO xxxx() VALUES();
-- --------------------- SECTION END ---------------------------------------------------------
