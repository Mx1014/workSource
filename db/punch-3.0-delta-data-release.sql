-- 打卡
SET @id =(SELECT MAX(id) FROM eh_locale_templates); 
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'punch.tool.uri','1','zh_CN','tools跳转uri','zl://attendance/punchClockTool?type=${qrtype}&token=${token}',0); 
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'punch.excel','1','zh_CN','排班excel说明','${timeRules}休息（只能按现有班次排班，否则无法识别。班次信息可以在管理后台修改）',0); 

DELETE FROM eh_web_menus WHERE path LIKE '/500000/506000/%';
DELETE FROM eh_web_menus WHERE path LIKE '/50000/50600/%';
UPDATE eh_web_menus SET data_type = 'react:/attendance-management/attendance-record/1' WHERE id IN (506000,50600);

SET @id =(SELECT MAX(id) FROM eh_locale_strings); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','17','zh_CN','非工作日'); 
UPDATE eh_locale_strings SET TEXT = '缺卡' WHERE scope = 'punch.status' AND  CODE = '17' ;

-- 更改工作流显示
UPDATE eh_locale_templates SET TEXT = '异常日期：${exceptionDate}
打卡时间：${punchDetail}' WHERE scope = 'approval.flow.context' AND CODE = 2 ;

UPDATE eh_locale_strings SET TEXT ='打卡时间' WHERE scope ='approval.flow' AND  CODE = 'punchDetail';
