-- 打卡
SET @id =(SELECT MAX(id) FROM eh_locale_templates); 
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'punch.tool.uri','1','zh_CN','tools跳转uri','zl://attendance/punchClockTool?type=${qrtype}&token=${token}',0); 
