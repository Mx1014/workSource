-- --------------企业OA相关功能提前融合到标准版，在5.9.0全量合并到标准版发布时需要跳过这部分脚本的执行-----------

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

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES('remind.version.segmen','5.8.4','日程提醒的版本分隔','0',NULL,NULL);

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

-- AUTHOR: ryan  20180827
-- REMARK: 执行 /workReport/syncWorkReportReceiver 接口, 用以同步工作汇报接收人公司信息(以下接口需等待上一接口执行完毕,基线大约需要10分钟)

-- AUTHOR: ryan  20180827
-- REMARK: 执行 /workReport/updateWorkReportReceiverAvatar 接口, 用以更新工作汇报接收人头像(需等待上一接口执行完毕,基线大约需要10分钟)

-- AUTHOR: ryan  20180926
-- REMARK: 执行 /workReport/updateWorkReportValAvatar 接口, 用以更新历史工作汇报值的头像(需等待上一接口执行完毕,基线大约需要10分钟)
-- --------------企业OA相关功能提前融合到标准版，END 张智伟 -----------

-- 在 5.8.1-delta-data-release.sql 中已存在

-- -- ---------------------个人中心数据初始化sql--------------------------
-- -- AUTHOR: 梁燕龙
-- -- REMARK: 个人中心初始化数据
-- set @id = IFNULL((select MAX(id) FROM eh_personal_center_settings), 1);
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
-- VALUES ((@id := @id + 1),0,'钱包','钱包',1,1,1,1,2,2,'cs://1/image/aW1hZ2UvTVRwaFkyUmhZV1F3WkdRMk56RXpNMlptWkRVek0ySXlNbVkxTVRJNFkyVTRZUQ',1,1,'/app/wallet');
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
-- VALUES ((@id := @id + 1),0,'订单','订单',1,2,1,0,3,2,'cs://1/image/aW1hZ2UvTVRvNFptTTFOalZrWkRrd01tVXdOak5rWmpKa09UY3lOR1E1TlRJeVpUUXpZZw',1,1,'/zl-ec/rest/service/front/logon?sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fmall%2findex.html#sign_suffix');
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid,link_url)
-- VALUES ((@id := @id + 1),0,'卡券','卡券',1,3,1,0,4,2,'cs://1/image/aW1hZ2UvTVRvMU5qUXhZV0prWXpBM01UazVOell4TVdWaU1qZzNPVEZsWXpneE9XSmtaZw',1,1,'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Froll%2F1%3F_k%3Dzlbiz#sign_suffix');
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
-- VALUES ((@id := @id + 1),0,'发票','发票',1,4,0,1,5,2,'cs://1/image/aW1hZ2UvTVRwaE9EYzRORGd5WkRJd01URmpZMlV5T0dSbU5tVXdNemRtTjJGbU5EZG1ZUQ',1,1);
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
-- VALUES ((@id := @id + 1),0,'我的申请','我的申请',2,1,1,1,0,6,2,'cs://1/image/aW1hZ2UvTVRvMlpHUXlaVGxoTWpoa016Sm1OR1U1TXpsbE5ESTNNbVpqWVRFM1ptWTFPQQ',1,1);
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
-- VALUES ((@id := @id + 1),0,'我的地址','我的地址',2,1,2,1,0,7,2,'cs://1/image/aW1hZ2UvTVRvNFlXRmxObVZqWkRVeE5EWTROamRsTWpFNVltSTBNV0poT0dNd01UVTRZZw',1,1);
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
-- VALUES ((@id := @id + 1),0,'我的店铺','我的店铺',2,1,3,1,1,8,2,'cs://1/image/aW1hZ2UvTVRvellUZzJOR0UzT0RSbU5UTmpaalUxWmpjM01XWTBaakptTmpNMk9ETXpaUQ',1,1);
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
-- VALUES ((@id := @id + 1),0,'我的发布','我的发布',2,2,1,1,1,9,2,'cs://1/image/aW1hZ2UvTVRwaE1HUmlOV1l4WXpKaVl6a3dNak01TmpVeU9HVmxPRFl6TVRrME4yRTBZUQ',1,1);
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
-- VALUES ((@id := @id + 1),0,'我的收藏','我的收藏',2,2,2,1,1,10,2,'cs://1/image/aW1hZ2UvTVRvMU16bGhZV0l4TVROaU5XSXdNRE13TVdRMFlURmpNV0l4TlRRM01qWmhZZw',1,1);
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
-- VALUES ((@id := @id + 1),0,'我的报名','我的报名',2,2,3,1,1,11,2,'cs://1/image/aW1hZ2UvTVRwaVlqTXpabVJoTldZM056SXhaR1E1TjJWaE9URTRPVEE0TW1RM1kyUTJZUQ',1,1);
-- INSERT INTO `eh_personal_center_settings` (id, namespace_id, name, function_name, region,group_type, sort_num, showable, editable, type, status, icon_uri, create_uid, update_uid)
-- VALUES ((@id := @id + 1),0,'设置','设置',2,3,1,1,0,12,2,'cs://1/image/aW1hZ2UvTVRwaU5tUXhNR013T1RGaVlUVmtNalF6TmpkaVpqZzVNVGhtWlRoaU1XVTRaQQ',1,1);
-- -- -------------------END-----------------------------------------------

-- ------------------------园区公告功能提前融合到标准版 ----------------------
-- AUTHOR: 梁燕龙
-- REMARK: 活动报名人数不足最低限制人数自动取消活动消息推送
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
VALUES( 'announcement.notification', 1, 'zh_CN', '公告消息', '${subject}');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`) VALUES ('announcement',1,'zh_CN','公告消息');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`) VALUES ('forum',10007,'zh_CN','来晚啦，公告已不存在');
---------------------------园区公告功能提前融合到标准版 END -------------


-- ------------------------- 物业融合标准版------------------------------------
-- AUTHOR:jiarui

-- 通用脚本
-- AUHOR: jiarui 20180726
-- REMARK：动态表单迁移ownerId
update eh_var_field_group_scopes t1 set owner_id = IFNULL((select id  from eh_organizations  t2 where organization_type = 'PM' and t2.namespace_id = t1.namespace_id and parent_id = 0 LIMIT 1 ),0);
update eh_var_field_group_scopes set owner_type ='EhOrganizations';
update eh_var_field_scopes t1 set owner_id = IFNULL((select id  from eh_organizations  t2 where organization_type = 'PM' and t2.namespace_id = t1.namespace_id and parent_id = 0 LIMIT 1 ),0);
update eh_var_field_scopes set owner_type ='EhOrganizations';
update eh_var_field_item_scopes t1 set owner_id = IFNULL((select id  from eh_organizations  t2 where organization_type = 'PM' and t2.namespace_id = t1.namespace_id and parent_id = 0 LIMIT 1),0);
update eh_var_field_item_scopes set owner_type ='EhOrganizations';
-- 物业巡检 by jiatui 20180730
update eh_equipment_inspection_equipments set owner_type = 'EhOrganizations';
update eh_equipment_inspection_standards set owner_type = 'EhOrganizations';
update eh_equipment_inspection_accessories set owner_type = 'EhOrganizations';
update eh_equipment_inspection_plans set owner_type = 'EhOrganizations';
update eh_equipment_inspection_tasks set owner_type = 'EhOrganizations';
update eh_equipment_inspection_templates set owner_type = 'EhOrganizations';
update eh_pm_notify_configurations t1 set target_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =(select namespace_id from eh_communities t3 where t3.id = scope_id ) and t2.parent_id = 0 LIMIT 1 ) where t1.scope_type = 2;
update eh_pm_notify_configurations t1 set target_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =t1.scope_id and t2.parent_id = 0 LIMIT 1 ) where t1.scope_type = 1;
update eh_equipment_inspection_review_date t1 set target_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =(select namespace_id from eh_communities t3 where t3.id = scope_id ) and t2.parent_id = 0 LIMIT 1 ) where t1.scope_type = 2;
update eh_equipment_inspection_review_date t1 set target_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =t1.scope_id and t2.parent_id = 0 LIMIT 1 ) where t1.scope_type = 1;
update eh_equipment_inspection_review_date set target_type = 'EhOrganizations';
update eh_pm_notify_configurations set target_type = 'EhOrganizations';
-- 品质核查 by jiarui  20180730
update eh_quality_inspection_standards set owner_type ='EhOrganizations';
update eh_quality_inspection_tasks set owner_type ='EhOrganizations';
update eh_quality_inspection_task_templates set owner_type ='EhOrganizations';
update eh_quality_inspection_specifications set owner_type ='EhOrganizations';
update eh_quality_inspection_samples set owner_type ='EhOrganizations';
update eh_quality_inspection_sample_score_stat set owner_type ='EhOrganizations';
update eh_quality_inspection_sample_community_specification_stat set owner_type ='EhOrganizations';
update eh_quality_inspection_logs set owner_type ='EhOrganizations';
update eh_quality_inspection_evaluations set owner_type ='EhOrganizations';
-- 能耗管理  by jiarui 20180731
update eh_energy_meter_categories set owner_type ='EhOrganizations';

-- 合同管理 by jiarui 20180731
update eh_contract_templates t1 set org_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =t1.namespace_id and t2.parent_id = 0 LIMIT 1);
update eh_contract_params t1 set owner_id = (select id  from eh_organizations  t2 where organization_type = 'PM' and t2.namespace_id = t1.namespace_id and parent_id = 0 LIMIT 1 );
update eh_contract_params set owner_type = 'EhOrganizations';


-- 缴费管理  by jiarui 20180806
UPDATE eh_asset_bills set owner_type ='EhOrganizations';
UPDATE eh_asset_bill_template_fields set owner_type ='EhOrganizations';
UPDATE  eh_payment_charging_item_scopes t1 set org_id = (select t2.id  from eh_organizations  t2 where t2.organization_type = 'PM' and t2.namespace_id =t1.namespace_id and t2.parent_id = 0 LIMIT 1);
update eh_asset_bill_notify_records set owner_type = 'EhOrganizations';

-- -------------------------- 物业融合标准版---------------------------------




-- ------------------------更新广场layout ----------------------
-- 更新 layout
SET @versionCode = '201809260800';

SET @bizAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 92100 AND `namespace_id` = 2);
SET @activityAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10600 AND `namespace_id` = 2);
SET @forumAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10100 AND `namespace_id` = 2);
SET @communityBulletinsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10300 AND `namespace_id` = 2);
SET @enterpriseBulletinsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 57000 AND `namespace_id` = 2);

UPDATE eh_launch_pad_layouts set version_code = @versionCode, layout_json  = CONCAT('{"versionCode":"',@versionCode,'","layoutName":\"ServiceMarketLayout\",\"displayName\":\"工作台\",\"groups\":[{\"defaultOrder\":3,\"groupName\":\"公告\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"rowCount\":1,\"noticeCount\":1,\"style\":1,\"shadow\":1.0,\"moduleId\":57000.0,\"appId\":', @enterpriseBulletinsAppId ,'},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Bulletins\"},{\"columnCount\":1,\"defaultOrder\":2,\"groupName\":\"园区运营\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":16.0,\"paddingLeft\":16.0,\"paddingBottom\":16.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"backgroundColor\":\"#ffffff\",\"appType\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Card\"},{\"columnCount\":1,\"defaultOrder\":4,\"groupName\":\"企业办公\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":16.0,\"paddingLeft\":16.0,\"paddingBottom\":16.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"backgroundColor\":\"#ffffff\",\"appType\":0.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Card\"}]}') WHERE type = 4 AND namespace_id = 2;
UPDATE eh_launch_pad_layouts set version_code = @versionCode, layout_json  = CONCAT('{"versionCode":"',@versionCode,'","layoutName":\"ServiceMarketLayout\",\"displayName\":\"服务广场\",\"groups\":[{\"defaultOrder\":1,\"groupName\":\"banner图片1\","style":"Shape",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31056\",\"widthRatio\":16.0,\"heightRatio\":9.0,\"shadowFlag\":1.0,\"paddingFlag\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Banners\"},{\"groupId\":0,\"groupName\":\"容器\",\"columnCount\":4,\"defaultOrder\":2,\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":0.0,\"paddingLeft\":16.0,\"paddingBottom\":0.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"cssStyleFlag\":1.0,\"backgroundColor\":\"#ffffff\",\"allAppFlag\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Navigator\"},{\"defaultOrder\":3,\"groupName\":\"公告\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31057\",\"rowCount\":1,\"noticeCount\":1,\"style\":2.0,\"shadow\":1.0,\"moduleId\":10300.0,\"appId\":', @communityBulletinsAppId ,'},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Bulletins\"},{\"groupName\":\"电商入口\",\"widget\":\"NavigatorTemp\"},{\"defaultOrder\":4,\"groupName\":\"商品精选\",\"instanceConfig\":{\"itemGroup\":\"OPPushBiz\",\"moduleId\":92100.0,\"appId\":', @bizAppId, ',\"entityCount\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":24,\"style\":\"HorizontalScrollSquareView\",\"widget\":\"OPPush\"},{\"defaultOrder\":5,\"groupName\":\"活动\",\"instanceConfig\":{\"itemGroup\":\"OPPushActivity\",\"entityCount\":5.0,\"subjectHeight\":0.0,\"descriptionHeight\":0.0,\"newsSize\":5.0,\"moduleId\":10600.0,\"appId\":', @activityAppId, ',\"actionType\":61.0,\"appConfig\":{\"categoryId\":1.0,\"publishPrivilege\":1.0,\"livePrivilege\":0.0,\"listStyle\":2.0,\"scope\":3.0,\"style\":4.0}},\"separatorFlag\":1,\"separatorHeight\":24,\"style\":\"HorizontalScrollWideView\",\"widget\":\"OPPush\"},{\"defaultOrder\":7,\"groupName\":\"论坛\",\"instanceConfig\":{\"moduleId\":10100.0,\"appId\":', @forumAppId, ',\"actionType\":62.0,\"newsSize\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":24,\"style\":\"TextImageWithTagListView\",\"widget\":\"OPPush\"}]}') WHERE type = 5 AND namespace_id = 2 ;

-- -------------------END-----------------------------------------------


-- "固定资产管理" 设置为oa模块
UPDATE eh_service_modules set app_type = 0 WHERE id = 59000;
UPDATE eh_service_module_apps a set app_type = 0 WHERE module_id = 59000;