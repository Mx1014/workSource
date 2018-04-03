
-- 打卡审批工作流的title，add by wh, 20170502
SET @id =(SELECT MAX(id) FROM eh_locale_strings); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow.title','1','zh_CN','请假申请'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow.title','2','zh_CN','异常申请'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow.title','3','zh_CN','加班申请');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','creator','zh_CN','发起人'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','contact','zh_CN','联系电话'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','requestDate','zh_CN','申请日期'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','overtimeLength','zh_CN','加班时长'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','punchDetail','zh_CN','打卡详情'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','requestReson','zh_CN','申请理由'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','category','zh_CN','请假类型'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','absentTime','zh_CN','请假时间'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'approval.flow','absentLength','zh_CN','请假时长'); 


-- 打卡审批工作流的内容，add by wh, 20170502
SET @id =(SELECT MAX(id) FROM eh_locale_templates); 
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'approval.flow.context','1','zh_CN','请假申请的内容','请假类型：${absentCategory}\n请假时间${beginTime}至${endTime}',0); 
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'approval.flow.context','2','zh_CN','异常申请的内容','异常日期：${exceptionDate}\n打卡详情：${punchDetail}',0); 
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'approval.flow.context','3','zh_CN','加班申请的内容','加班日期：${overTimeDate}\n加班时长：${timeLength}',0); 
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'approval.flow','1','zh_CN','请假申请的时间','${beginTime}至${endTime}',0); 

