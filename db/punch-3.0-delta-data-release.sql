-- 打卡
SET @id =(SELECT MAX(id) FROM eh_locale_templates); 
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'punch.tool.uri','1','zh_CN','tools跳转uri','zl://attendance/punchClockTool?type=${qrtype}&token=${token}',0); 
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'punch.excel','1','zh_CN','排班excel说明','${timeRules}休息（只能按现有班次排班，否则无法识别。班次信息可以在管理后台修改）',0); 