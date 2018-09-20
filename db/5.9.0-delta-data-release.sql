-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR: liangqishi  20180702
-- REMARK: 某某模块涉及到数据迁移，升级后需要调用/xxxx/xxxx接口更新ES
-- REMARK: content图片程序升级，从本版中的content二进制更新到正式环境中

-- AUTHOR: 杨崇鑫 2018年9月5日
-- REMARK: 1、备份表eh_asset_module_app_mappings
--         2、调用接口/asset/tranferAssetMappings

-- AUTHOR: ryan  20180918
-- REMARK: 执行 /workReport/syncWorkReportReceiver 接口, 用以同步工作汇报接收人公司信息

-- AUTHOR: ryan  20180918
-- REMARK: 执行 /workReport/updateWorkReportReceiverAvatar 接口, 用以更新工作汇报接收人头像

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

-- AUTHOR: 吴寒
-- REMARK: issue-33943 日程提醒1.2 增加5.9.0之后使用的提醒设置
SET @id = (SELECT MAX(id) FROM eh_remind_settings);
INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'准时','0',NULL,'1','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);
INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前5分钟','0',NULL,'2','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',300000);
INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前15分钟','0',NULL,'3','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',900000);
INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前30分钟','0',NULL,'4','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',1800000);
INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前1小时','0',NULL,'5','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',3600000);
INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前2小时','0',NULL,'6','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',7200000);
INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前1天','1',NULL,'7','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);
INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前2天','2',NULL,'8','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);
INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前3天','3',NULL,'9','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);
INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前5天','5',NULL,'10','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);
INSERT INTO `eh_remind_settings` (`id`, `name`, `offset_day`, `fix_time`, `default_order`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `app_version`, `before_time`) VALUES((@id := @id + 1),'提前1周','7',NULL,'11','0','2018-08-22 18:27:36',NULL,NULL,'5.9.0',NULL);

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES('remind.version.segmen','5.9.0','日程提醒的版本分隔','0',NULL,NULL);

SET @tem_id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','1','zh_CN','完成日程发消息','${trackContractName}的日程“${planDescription}” 已完成','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','2','zh_CN','重置未完成日程发消息','${trackContractName}的日程“${planDescription}” 重置为未完成','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','3','zh_CN','修改日程发消息','${trackContractName}修改了日程“${planDescription}”','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','4','zh_CN','删除日程发消息','${trackContractName}删除了日程“${planDescription}”','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','5','zh_CN','取消共享日程发消息','${trackContractName}的日程“${planDescription}” 取消共享了','0');

-- AUTHOR: 吴寒
-- REMARK: 会议管理V1.2
UPDATE eh_configurations SET VALUE = 5000 WHERE NAME ='meeting.record.word.limit';

-- AUTHOR: 荣楠
-- REMARK: 工作汇报V1.2
UPDATE `eh_locale_templates` SET `text`='${applierName}给你提交了${reportName}（${reportTime}）', `code` = 101 WHERE `scope`='work.report.notification' AND `code`='1';
UPDATE `eh_locale_templates` SET `text`='${applierName}更新了Ta的${reportName}（${reportTime}）', `code` = 102 WHERE `scope`='work.report.notification' AND `code`='2';
UPDATE `eh_locale_templates` SET `text`='${commentatorName}在Ta的${reportName}（${reportTime}）中 回复了你', `code` = 1, `description` = '评论消息类型1' WHERE `scope`='work.report.notification' AND `code`='3';
UPDATE `eh_locale_templates` SET `text`='${commentatorName}在Ta的${reportName}（${reportTime}）中 发表了评论', `code` = 2, `description` = '评论消息类型2' WHERE `scope`='work.report.notification' AND `code`='4';
UPDATE `eh_locale_templates` SET `text`='${commentatorName}在你的${reportName}（${reportTime}）中 回复了你', `code` = 3, `description` = '评论消息类型3' WHERE `scope`='work.report.notification' AND `code`='5';
UPDATE `eh_locale_templates` SET `text`='${commentatorName}在${applierName}的${reportName}（${reportTime}）中 回复了你', `code` = 4, `description` = '评论消息类型4' WHERE `scope`='work.report.notification' AND `code`='6';
UPDATE `eh_locale_templates` SET `text`='${commentatorName}在你的${reportName}（${reportTime}）中 发表了评论', `code` = 5, `description` = '评论消息类型5' WHERE `scope`='work.report.notification' AND `code`='7';


UPDATE `eh_work_reports` SET `validity_setting` = '{"endTime":"10:00","endType":1,"startTime":"15:00","startType":0}' WHERE `report_type` = 0;
UPDATE `eh_work_reports` SET `validity_setting` = '{"endMark":"1","endTime":"10:00","endType":1,"startMark":"5","startTime":"15:00","startType":0}' WHERE `report_type` = 1;
UPDATE `eh_work_reports` SET `validity_setting` = '{"endMark":"1","endTime":"10:00","endType":1,"startMark":"31","startTime":"15:00","startType":0}' WHERE `report_type` = 2;

UPDATE `eh_work_report_templates` SET `icon_uri`='cs://1/image/aW1hZ2UvTVRwaFlqazJNVFk1WkdabFpEbGhNamc0T1RjNVltWmlOakl3TmpobE1qUXpOUQ' WHERE (`id`='1') LIMIT 1;
UPDATE `eh_work_report_templates` SET `icon_uri`='cs://1/image/aW1hZ2UvTVRvMU1tRTVPV0kwWlRjeU16WmlOR05rTnpWbE9HUTFZV1ExTlRZMFpHUm1Odw' WHERE (`id`='2') LIMIT 1;
UPDATE `eh_work_report_templates` SET `icon_uri`='cs://1/image/aW1hZ2UvTVRvd1lXRmtObUkwWkRaaFlXRXdPRFV5TmpjMU5qa3hNelk1TVRRNE5XUTRNdw' WHERE (`id`='3') LIMIT 1;

SET @config_id = (SELECT MAX(id) FROM eh_configurations);
INSERT INTO `ehcore`.`eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES (@config_id := @config_id + 1, 'work.report.icon', 'cs://1/image/aW1hZ2UvTVRveE9UVm1aRGhsTTJRek9HRmpaRFV3WXpKaU5ESTNNV1ppT0RneVpHRTFaQQ', 'the icon of the customize workReport ', '0', NULL, NULL);

UPDATE eh_work_reports SET `icon_uri`='cs://1/image/aW1hZ2UvTVRveE9UVm1aRGhsTTJRek9HRmpaRFV3WXpKaU5ESTNNV1ppT0RneVpHRTFaQQ' ;
UPDATE eh_work_reports SET `icon_uri`='cs://1/image/aW1hZ2UvTVRwaFlqazJNVFk1WkdabFpEbGhNamc0T1RjNVltWmlOakl3TmpobE1qUXpOUQ' WHERE report_template_id = 1;
UPDATE eh_work_reports SET `icon_uri`='cs://1/image/aW1hZ2UvTVRvMU1tRTVPV0kwWlRjeU16WmlOR05rTnpWbE9HUTFZV1ExTlRZMFpHUm1Odw' WHERE report_template_id = 2;
UPDATE eh_work_reports SET `icon_uri`='cs://1/image/aW1hZ2UvTVRvd1lXRmtObUkwWkRaaFlXRXdPRFV5TmpjMU5qa3hNelk1TVRRNE5XUTRNdw' WHERE report_template_id = 3;

-- AUTHOR: 梁燕龙
-- REMARK: 活动报名人数不足最低限制人数自动取消活动消息推送
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
VALUES( 'announcement.notification', 1, 'zh_CN', '公告消息', '${subject}');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`) VALUES ('announcement',1,'zh_CN','公告消息');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`) VALUES ('forum',10007,'zh_CN','来晚啦，公告已不存在');


-- AUTHOR: 严军
-- REMARK: 删除待办事项菜单
UPDATE eh_web_menus SET `status` = 0 WHERE id in = 48130000;
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

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.loginUrl', 'http://syxtest.zuolin.com/jsaims/login', '捷顺登录地址', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.funcUrl', 'http://syxtest.zuolin.com/jsaims/as', '捷顺接口请求地址', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.version', '2', '捷顺接口版本', 0, NULL, 1);


-- AUTHOR: 严军
-- REMARK: 增加“企业钱包”模块，更新“会议室管理”菜单path
-- issue null
UPDATE eh_web_menus SET path = '/23000000/23010000/16041900' WHERE id = 16041900;
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('59200', '企业钱包', '50000', '/100/50000/59200', '1', '3', '2', '240', '2018-09-19 14:42:53', NULL, NULL, '2018-09-19 14:43:07', '0', '0', '0', '0', 'org_control', '1', '1', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79100000', '企业钱包', '23010000', NULL, 'enterprise-wallet', '1', '2', '/23000000/23010000/79100000', 'zuolin', '240', '59200', '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79200000', '企业钱包', '77000000', NULL, 'enterprise-wallet', '1', '2', '/70000010/77000000/79200000', 'organization', '240', '59200', '3', 'system', 'module', NULL);
UPDATE eh_web_menus SET parent_id = 53000000, path = '/40000010/53000000/79200000', type = 'park', sort_num = 80 WHERE id =  79200000;





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

SET @parkinglotid = (select max(id) from eh_parking_lots);
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`, `order_tag`, `order_code`, `id_hash`, `func_list`)
VALUES (@parkinglotid := @parkinglotid + 1, 'community', '240111044332063798', '基金小镇停车场', 'CHEAN', '', '2', '1', '2018-07-27 17:48:07', '999938', NULL, '{\"tempfeeFlag\": 1, \"rateFlag\": 0, \"lockCarFlag\": 0, \"searchCarFlag\": 1, \"currentInfoType\": 0,\"identityCardFlag\":1}', SUBSTRING(@id, -3), '0', NULL,  '[\"tempfee\",\"monthRecharge\",\"searchCar\",\"carNum\",\"monthCardApply\"]');

SET @parkingcardid = (select max(id) from eh_parking_lots);
INSERT INTO `eh_parking_card_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `card_type_id`, `card_type_name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
VALUES (@parkingcardid := @parkingcardid + 1, '999938', 'community', '240111044332063798', @parkinglotid, '1', '月卡', '2', '1', '2018-05-03 10:49:48', NULL, NULL);

SET @locId = (select max(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@locId := @locId + 1, 'parking', '10033', 'zh_CN', '未查询到停车记录');

-- AUTHOR: 马世亨  20180919
-- REMARK: 停车缴费V6.6.4 【大沙河建投】二期智园项目，对接车安系统
SET @namespaceid = '999967';
SET @communityid = '240111044332064133';
SET @configid = (select max(id) from eh_configurations);
INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name, is_readonly)
VALUES (@configid := @configid + 1 , 'parking.cheanzhiyuan.url', 'http://113.98.59.44:9022', '车安大沙河智园停车场url', '0', NULL, NULL);

INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name, is_readonly)
VALUES (@configid := @configid + 1 , 'parking.cheanzhiyuan.privatekey', '71cfa1c59773ddfa289994e6d505bba3', '车安大沙河智园停车场私钥', '0', NULL, NULL);

INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name, is_readonly)
VALUES (@configid := @configid + 1 , 'parking.cheanzhiyuan.accessKeyId', 'UT', '车安大沙河智园停车场接入方标识', '0', NULL, NULL);

INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name, is_readonly)
VALUES (@configid := @configid + 1 , 'parking.cheanzhiyuan.branchno', '0', '车安大沙河智园停车分点编号', '0', NULL, NULL);

SET @parkinglotid = (select max(id) from eh_parking_lots);
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`, `order_tag`, `order_code`, `id_hash`, `func_list`)
VALUES (@parkinglotid := @parkinglotid + 1, 'community', @communityid, '大沙河智园停车场', 'CHEANZHIYUAN', @namespaceid, '2', '1', '2018-05-21 14:36:29', '0', '{\"expiredRechargeFlag\": 0,\"monthlyDiscountFlag\":0}', '{\"tempfeeFlag\": 1, \"rateFlag\": 1, \"lockCarFlag\": 0, \"searchCarFlag\": 1, \"currentInfoType\": 0,\"identityCardFlag\":1,\"monthCardFlag\":1,\"monthRechargeFlag\":1}', SUBSTRING(@parkinglotid, -3), '8', NULL, '[\"tempfee\",\"monthRecharge\",\"searchCar\"]');

SET @parkingcardid = (select max(id) from eh_parking_card_types);
INSERT INTO `eh_parking_card_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `card_type_id`, `card_type_name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
VALUES (@parkingcardid := @parkingcardid + 1, @namespaceid, 'community', @communityid, @parkinglotid, '1', '月卡', '2', '1', '2018-05-03 10:49:48', NULL, NULL);


-- AUTHOR:丁建民 20180920
-- REMARK: issue-37007 资产设置一房一价，租赁价格大于设定的价格，则合同不需要审批
SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '20017', 'zh_CN', '周期输入格式不正确');
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'address', '20018', 'zh_CN', '授权价格式不正确');

INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'contract', '10013', 'zh_CN', '合同计价条款计费周期不存在');
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'contract', '10014', 'zh_CN', '该房源信息已经存在');



-- --------------------- SECTION END ---------------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR: liangqishi  20180702
-- REMARK: （此行放说明，可以多行，多行时每行前面都有REMARK）某模块增加初始化数据。。。。。。。
-- REMARK:。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。

-- 大沙河创新大厦停车场
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_DSHCXMall.cid', '880075500000001', '客户号', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_DSHCXMall.usr', '880075500000001', '捷顺登录账户名', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_DSHCXMall.psw', '888888', '捷顺登录密码', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_DSHCXMall.signKey', '7ac3e2ee1075bf4bb6b816c1e80126c0', 'signKey', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_DSHCXMall.parkCode', '0010028888', '小区编号', 0, NULL, 1);

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

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_GQY2.cid', '880075500000001', '客户号', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_GQY2.usr', '880075500000001', '捷顺登录账户名', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_GQY2.psw', '888888', '捷顺登录密码', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_GQY2.signKey', '7ac3e2ee1075bf4bb6b816c1e80126c0', 'signKey', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_GQY2.parkCode', '0010028888', '小区编号', 0, NULL, 1);


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
