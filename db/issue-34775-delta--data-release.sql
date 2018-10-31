 
-- AUTHOR: 吴寒
-- REMARK: 打卡考勤V8.2 - 支持人脸识别关联考勤；支持自动打卡
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES('punch.create','1','zh_CN','打卡发送消息','${createType}: ${punchTime}','0');

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('punch.create.type','1','zh_CN','自动打卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('punch.create.type','2','zh_CN','人脸识别打卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('punch.create.type','3','zh_CN','门禁打卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('punch.create.type','4','zh_CN','其他第三方打卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('oa.unit','1','zh_CN','天');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('oa.unit','2','zh_CN','次');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('remind.msg','1','zh_CN','日程提醒');

INSERT INTO eh_locale_strings(scope,CODE,locale,TEXT) VALUE( 'PunchStatusStatisticsItemName', 11, 'zh_CN', '外出');

-- AUTHOR: 吴寒
-- REMARK: 日程提醒给共享人发消息
SET @tem_id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@tem_id := @tem_id + 1),'remind.msg','6','zh_CN','创建给被共享人发消息','${trackContractName}共享了日程“${planDescription}” ','0');
