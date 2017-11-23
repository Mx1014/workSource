
-- 打卡审批工作流的title，add by wh, 20170502
SET @id =(SELECT MAX(id) FROM eh_locale_strings); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'general_approval.contact.key','0','zh_CN','企业联系人'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'general_approval.contact.key','1','zh_CN','企业'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'general_approval.contact.key','2','zh_CN','楼栋-门牌'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'general_approval.contact.key','3','zh_CN','联系人'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'general_approval.contact.key','4','zh_CN','联系方式');  